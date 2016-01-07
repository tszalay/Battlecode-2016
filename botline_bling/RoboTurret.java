package botline_bling;

import battlecode.common.*;



public class RoboTurret extends RobotPlayer
{
	static MapLocation unpackLoc=null;
	static final int[] unpackSearchTableX = {0,1,0,-1,-1,1,2,2,1,-1,-2,-2,-2,0,2,3,3,3,2,0,-2,-3,-3,-3,-1,-1,4,4,1,-1,-4,-4};
	static final int[] unpackSearchTableY = {1,0,-1,0,2,2,1,1,-2,-2,-1,1,3,3,3,2,0,-2,-3,-3,-3,-2,0,2,4,4,1,-1,-4,-4,-1,1};

	// AK: unpack search in this pattern
	// [][][]25[]26[][][]
	// [][]13[]14[]15[][]
	// []24[]05[]06[]16[]
	// 32[]12[]01[]07[]27
	// []23[]04{}02[]17[]
	// 31[]11[]03[]08[]28
	// []22[]10[]09[]18[]
	// [][]21[]20[]19[][]
	// [][][]30[]29[][][]
	
	public static void init() throws GameActionException
	{
	}
	
	public static void turn() throws GameActionException
	{
		if (rc.getType() == RobotType.TURRET)
			turnTurret();
		else
			turnTTM();
	}
	
	public static void turnTurret() throws GameActionException
	{
		
		boolean isOverpowered = false;
		
		 // If this robot type can attack, check for enemies within range and attack one
        if (rc.isWeaponReady())
        {
            RobotInfo[] enemiesWithinRange = rc.senseNearbyRobots(rc.getType().sensorRadiusSquared, theirTeam);
            RobotInfo[] zombiesWithinRange = rc.senseNearbyRobots(rc.getType().sensorRadiusSquared, Team.ZOMBIE);
            
            if (enemiesWithinRange.length > 0)
            {
            	for (RobotInfo enemy : enemiesWithinRange)
            	{
            		// Check whether the enemy is in a valid attack range (turrets have a minimum range)
            		if (rc.canAttackLocation(enemy.location)) {
            			rc.attackLocation(enemy.location);
            			break;
            		}
            	}
            } 
            else if (zombiesWithinRange.length > 0)
            {
            	RobotInfo bestTarget = null;
            	for (RobotInfo zombie : zombiesWithinRange)
            	{
            		// are we about to be dealt a killing blow
            		if (zombie.location.distanceSquaredTo(here) <= zombie.type.attackRadiusSquared && rc.getHealth() < 10*zombie.type.attackPower)
            			isOverpowered = true;

            		if (rc.canAttackLocation(zombie.location))
            		{
            			rc.attackLocation(zombie.location);
            			break;
            		}
            	}
            }
            else if (!MapUtil.isLocOdd(rc.getLocation()))
            {
            	rc.pack();
            	//System.out.println("Just re-packed!");
            }
        }
        
        RobotInfo ri = rc.senseRobot(rc.getID());
        
        if (isOverpowered && ri.zombieInfectedTurns == 0)
        	rc.disintegrate();
	}

	public static void turnTTM() throws GameActionException
	{
		
		if (MapUtil.isLocOdd(rc.getLocation())){
			rc.unpack();
		}
		
		else {
			if (rc.isCoreReady()) {

				RobotInfo[] nearbyEnemies = rc.senseNearbyRobots(rc.getType().sensorRadiusSquared, theirTeam);
				RobotInfo[] nearbyZombies = rc.senseNearbyRobots(rc.getType().sensorRadiusSquared, Team.ZOMBIE);
				NavSafetyPolicy safety = new SafetyPolicyAvoidAllUnits(nearbyEnemies, nearbyZombies);
				// if ready to move, update closest unpack location and nav there
				unpackLoc = findNewUnpackLoc();
				
				// If you failed, try to move to a random location
				if (unpackLoc == null) {
					Nav.goTo(rc.getLocation().add(rand.nextInt(200)-100,rand.nextInt(200)-100), safety);
				}
				
				// Otherwise go towards the best location
				else {
				Nav.goTo(unpackLoc, safety);
				}
			}
		}


	}



	// AK find new unpack location
	public static MapLocation findNewUnpackLoc() throws GameActionException
	{
		MapLocation myLoc = rc.getLocation();
		MapLocation newUnpackLoc = myLoc;
		
		for (int i = 0; i < 32; i++) {
			if (rc.isLocationOccupied(newUnpackLoc) || rc.senseRubble(newUnpackLoc) >= GameConstants.RUBBLE_SLOW_THRESH || !rc.onTheMap(newUnpackLoc)) {
				newUnpackLoc = myLoc.add(unpackSearchTableX[i],unpackSearchTableY[i]);
			}
			else if (i == 32) {
				newUnpackLoc = null; // For now, if you can't find a close location just give up
			}
			else {
				break;
			}
		}
		return newUnpackLoc;
	}	

}

