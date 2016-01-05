package botline_bling;

import battlecode.common.*;

public class RoboSoldier extends RobotPlayer
{
	static int fate;
	
	public static void init()
	{
		fate = rand.nextInt(1000);
	}
	
	public static void turn() throws GameActionException
	{
        

//        if (fate % 5 == 3) {
//            // Send a normal signal
//            rc.broadcastSignal(80);
//        }

        boolean shouldAttack = false;

        // If this robot type can attack, check for enemies within range and attack one
        if (rc.getType().attackRadiusSquared > 0) {
            RobotInfo[] enemiesWithinRange = rc.senseNearbyRobots(rc.getType().attackRadiusSquared, theirTeam);
            RobotInfo[] zombiesWithinRange = rc.senseNearbyRobots(rc.getType().attackRadiusSquared, Team.ZOMBIE);
            if (enemiesWithinRange.length > 0) {
                shouldAttack = true;
                // Check if weapon is ready
                if (rc.isWeaponReady()) {
                    rc.attackLocation(enemiesWithinRange[rand.nextInt(enemiesWithinRange.length)].location);
                }
            } else if (zombiesWithinRange.length > 0) {
                shouldAttack = true;
                // Check if weapon is ready
                if (rc.isWeaponReady()) {
                    rc.attackLocation(zombiesWithinRange[rand.nextInt(zombiesWithinRange.length)].location);
                }
            }
        }

        if (!shouldAttack) {
            if (rc.isCoreReady()) {
                RobotInfo[] nearbyEnemies = rc.senseNearbyRobots(rc.getType().sensorRadiusSquared, theirTeam);
                RobotInfo[] nearbyZombies = rc.senseNearbyRobots(rc.getType().sensorRadiusSquared, Team.ZOMBIE);
            	NavSafetyPolicy safety = new SafetyPolicyAvoidAllUnits(nearbyEnemies, nearbyZombies);
//            	if (fate < 600) {
                    // try to move to middle of map
                    MapLocation target = new MapLocation(436,159);
                    Nav.goTo(target, safety);
                    
                    
//                    // Check the rubble in that direction
//                    if (rc.senseRubble(rc.getLocation().add(dirToMove)) >= GameConstants.RUBBLE_OBSTRUCTION_THRESH) {
//                        // Too much rubble, so I should clear it
//                        rc.clearRubble(dirToMove);
//                        // Check if I can move in this direction
//                    } else if (rc.canMove(dirToMove)) {
//                        // Move
//                        rc.move(dirToMove);
//                    }
//                }
            }
        }
	}
}
