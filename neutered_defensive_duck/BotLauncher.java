package neutered_defensive_duck;

import java.util.Random;

import battlecode.common.*;

public class BotLauncher extends Bot {
	
	public static void loop(RobotController theRC) throws GameActionException {
        Bot.init(theRC);
        // Debug.init("supply");
        while (true) {
            try {
                turn();
            } catch (Exception e) {
                e.printStackTrace();
            }
            rc.yield();
        }
    }

    // Here is how far a missile can go in five turns, plus splash
    // ..........
    // ..........
    // ssssss....
    // OOOOOss...
    // OOOOOOss..
    // OOOOOOOs..
    // OOOOOOOs..
    // OOOOOOOs..
    // OOOOOOOs..
    // XOOOOOOs..
    // The farthest distance it can travel is (6, 4), for a squared distance of 52
    // Also, the diagonal displacement of (5, 5) gives a squared distance of 50
    // The closest distance it cannot reach is 7 squares away orthogonally, a squared distance of 49
    // If we neglect the few abnormally far reachable squares, we can say that the missile can
    // travel to any location with a squared distance of <= 48
    //
    // The closest non-splashable square is at a squared distance of 64. There are a few splashable
    // squares that are farther than 64, but to a good approximation the missile can splash those
    // squares with a squared distance of <= 63.

    private static MapLocation[] enemyTowers;
    private static boolean rogue;
    private static int rogueInt;

    static int lastKitedBackRound = -99;

    private static void turn() throws GameActionException {
        here = rc.getLocation();
        
        Random generator = new Random(rc.getID());
        rogue = generator.nextDouble()>0.9;
        rogueInt = (int) generator.nextDouble()*6;

        Supply.shareSupply();
        Supply.requestResupplyIfNecessary();

        MeasureMapSize.checkForMapEdges();

        enemyTowers = rc.senseEnemyTowerLocations();

        if (rc.getMissileCount() > 0) {
            tryLaunchMissile();
        }
        
        boolean onDefense = false;
        if (Defense.amIOnDefense()) {
        	rc.setIndicatorDot(here.add(0,1), 255, 0, 0);
        	onDefense = true;
        } else {
        	Defense.tryToAddMeToDefense();
        }

        if (rc.isCoreReady()) {
            if (kiteBack(onDefense)) {
                lastKitedBackRound = Clock.getRoundNum();
                return;
            } else {
                // best to be a bit cautious about moving forward when we have recently been forced to retreat
                // and we don't have any missiles to fire
                if (Clock.getRoundNum() - lastKitedBackRound > 8 || rc.getMissileCount() > 0) {
                    MapLocation rallyLoc = MessageBoard.RALLY_LOC.readMapLocation();
                    if (rogue) {
                    	if (enemyTowers.length==0) {
                    		rallyLoc = rc.senseEnemyHQLocation();
                    	} else {
                    		rallyLoc = enemyTowers[rogueInt % enemyTowers.length];
                    	}
                    	rc.setIndicatorDot(here.add(0,1), 0, 255, 0);
                    }
                    if (onDefense) {
                    	MapLocation defenseLoc = MessageBoard.DEFENSE_LOC.readMapLocation();
	                    	RobotInfo[] nearbyEnemies = rc.senseNearbyRobots(defenseLoc, 81, them);
	                    	NavSafetyPolicy safetyPolicy = new SafetyPolicyAvoidTowersAndHQ(enemyTowers);
	                    	if (nearbyEnemies.length==0) {
	                    		Nav.goTo(defenseLoc, safetyPolicy); // try to get between tower and enemies
	                    	} else {
	                    		//Nav.goTo(defenseLoc.add(defenseLoc.directionTo(nearbyEnemies[0].location)), safetyPolicy); // try to get between tower and enemies
	                    		Nav.goTo(nearbyEnemies[0].location, safetyPolicy);
	                    	}
                    } else {
                    	if (here.distanceSquaredTo(rallyLoc) > 35) { // stop short because we can shoot the target from long range
                            RobotInfo[] nearbyEnemies = rc.senseNearbyRobots(35, them);
                            if (nearbyEnemies.length == 0) { // stop while we kill the enemy
                                NavSafetyPolicy safetyPolicy = new SafetyPolicyAvoidAllUnits(enemyTowers, nearbyEnemies);
                                Nav.goTo(rallyLoc, safetyPolicy);
                            }
                        }
                    }
                }
            }
        }
    }

    private static boolean kiteBack(boolean onDefense) throws GameActionException {
        // be more skittish when we have no missiles because then we can't fight back
    	// but also kite in defensive directions, so you put yourself between enemies and your towers
        int tooCloseRange = rc.getMissileCount() > 0 ? 15 : 24;

        RobotInfo[] tooCloseEnemies = rc.senseNearbyRobots(tooCloseRange, them);
        if (tooCloseEnemies.length == 0) return false;

        RobotInfo[] nearbyEnemies = rc.senseNearbyRobots(35, them);

        Direction bestRetreatDir = null;
        RobotInfo currentClosestEnemy = Util.closest(nearbyEnemies, here);
        MapLocation[] towerLocs = rc.senseTowerLocations();
        MapLocation closestTowerLoc = rc.senseHQLocation();
        for (MapLocation loc : towerLocs) {
        	if (here.distanceSquaredTo(loc) < here.distanceSquaredTo(closestTowerLoc)) {
        		closestTowerLoc = loc;
        	}
        }
        
        int bestMetric;
        if (onDefense) {
        	bestMetric = here.distanceSquaredTo(currentClosestEnemy.location) - here.distanceSquaredTo(closestTowerLoc);
        } else {
        	bestMetric = here.distanceSquaredTo(currentClosestEnemy.location);
        }
        
        for (Direction dir : Direction.values()) {
            if (!rc.canMove(dir)) continue;

            MapLocation retreatLoc = here.add(dir);
            if (inEnemyTowerOrHQRange(retreatLoc, enemyTowers)) continue;

            RobotInfo closestEnemy = Util.closest(nearbyEnemies, retreatLoc);
            int distSqToClosestEnemy = retreatLoc.distanceSquaredTo(closestEnemy.location);
            int distSqToClosestTower = retreatLoc.distanceSquaredTo(closestTowerLoc);
            int metric;
            if (onDefense) {
            	metric = distSqToClosestEnemy - distSqToClosestTower;
            } else {
            	metric = distSqToClosestEnemy;  
            }
            if (metric > bestMetric) {
            	bestMetric = metric;
                bestRetreatDir = dir;
            }
            
        }

        if (bestRetreatDir == null) return false;

        rc.move(bestRetreatDir);
        return true;
    }

    static boolean tryLaunchMissile() throws GameActionException {
        RobotInfo[] nearbyEnemies = rc.senseNearbyRobots(63, them);

        for (RobotInfo enemy : nearbyEnemies) {
            if (enemy.type != RobotType.MISSILE) {
                if (tryLaunchAt(enemy.location)) return true;
            }
        }

        for (MapLocation enemyTower : rc.senseEnemyTowerLocations()) {
            if (here.distanceSquaredTo(enemyTower) <= 49) {
                if (tryLaunchAt(enemyTower)) return true;
            }
        }

        if (here.distanceSquaredTo(theirHQ) <= 49) {
            if (tryLaunchAt(theirHQ)) return true;
        }

        return false;
    }

    static boolean tryLaunchAt(MapLocation enemyLoc) throws GameActionException {
        if (here.distanceSquaredTo(enemyLoc) <= 8) {
            // can't shoot directly at nearby enemies without hurting ourselves unless we are clever
            // hopefuly this is half-clever: if we want to shoot at an adjacent enemy, wait until we can kite back in the same turn
            // this is not a huge burden because our move delay is 4 while our missile spawn delay is 8
            if (rc.getCoreDelay() >= 1) return false;

            // adjacent enemies are a special case, since we have to fire to the side
            if (here.isAdjacentTo(enemyLoc)) {
                Direction toEnemy = here.directionTo(enemyLoc);
                MapLocation target1 = here.add(toEnemy.rotateLeft());
                if (rc.isPathable(RobotType.MISSILE, target1)) {
                    rc.launchMissile(toEnemy.rotateLeft());
                    MissileGuidance.setMissileTarget(rc, target1, target1);
                    return true;
                }
                MapLocation target2 = here.add(toEnemy.rotateRight());
                if (rc.isPathable(RobotType.MISSILE, target2)) {
                    rc.launchMissile(toEnemy.rotateRight());
                    MissileGuidance.setMissileTarget(rc, target2, target2);
                    return true;
                }
                return false;
            }
        }

        boolean clearPath = true;
        MapLocation loc = here;
        while (true) {
            loc = loc.add(loc.directionTo(enemyLoc));
            RobotInfo robotInWay = rc.canSenseLocation(loc) ? rc.senseRobotAtLocation(loc) : null;
            if (robotInWay != null && robotInWay.type != RobotType.MISSILE) {
                clearPath = false;
                break;
            }
            if (loc.isAdjacentTo(enemyLoc)) {
                RobotInfo[] alliesInFriendlyFire = rc.senseNearbyRobots(loc, 2, us);
                for (RobotInfo ally : alliesInFriendlyFire) {
                    if (ally.type != RobotType.MISSILE) {
                        clearPath = false;
                        break;
                    }
                }
                break;
            }
        }

        if (clearPath) {
            Direction dir = here.directionTo(enemyLoc);
            if (rc.canLaunch(dir)) {
                rc.launchMissile(dir);
                MissileGuidance.setMissileTarget(rc, here.add(dir), enemyLoc);
                return true;
            }
        }
        return false;
    }
}
