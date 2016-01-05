package botline_bling;

import battlecode.common.*;



public class RoboTurret extends RobotPlayer
{
	static MapLocation unpackLoc=null;
	static final int[] unpackSearchTableX = {0,-2,0,2,2,-2,-2,2};
	static final int[] unpackSearchTableY = {2, 0, -2, 0, 2, 2, -2 ,2};
	
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
		
		boolean isFourOdd= MapUtil.isFourOdd(rc.getLocation());
		 // If this robot type can attack, check for enemies within range and attack one
        if (rc.isWeaponReady()) {
            RobotInfo[] enemiesWithinRange = rc.senseNearbyRobots(rc.getType().attackRadiusSquared, theirTeam);
            RobotInfo[] zombiesWithinRange = rc.senseNearbyRobots(rc.getType().attackRadiusSquared, Team.ZOMBIE);
            if (enemiesWithinRange.length > 0) {
            	for (RobotInfo enemy : enemiesWithinRange) {
            		// Check whether the enemy is in a valid attack range (turrets have a minimum range)
            		if (rc.canAttackLocation(enemy.location)) {
            			rc.attackLocation(enemy.location);
            			break;
            		}
            	}
            } else if (zombiesWithinRange.length > 0) {
            	for (RobotInfo zombie : zombiesWithinRange) {
            		if (rc.canAttackLocation(zombie.location)) {
            			rc.attackLocation(zombie.location);
            			break;
            		}
            	}
            } else if (!MapUtil.isFourOdd(rc.getLocation())){
            	rc.pack();
            }
         
        }
	}

	public static void turnTTM() throws GameActionException
	{
		if (MapUtil.isFourOdd(rc.getLocation())){
			rc.unpack();
		}
		
		else if (unpackLoc == null) {
			unpackLoc = findNewUnpackLoc();
		}
		else if (rc.getLocation() != unpackLoc){
			if (rc.isCoreReady()) {
				RobotInfo[] nearbyEnemies = rc.senseNearbyRobots(rc.getType().sensorRadiusSquared, theirTeam);
				RobotInfo[] nearbyZombies = rc.senseNearbyRobots(rc.getType().sensorRadiusSquared, Team.ZOMBIE);
				NavSafetyPolicy safety = new SafetyPolicyAvoidAllUnits(nearbyEnemies, nearbyZombies);
				unpackLoc = findNewUnpackLoc();
				Nav.goTo(unpackLoc, safety);
			}
		}
		else {
			// you've reached your destination!
			rc.unpack();
		}
	}



	// AK find new unpack location
	public static MapLocation findNewUnpackLoc() throws GameActionException {
		MapLocation newUnpackLoc = rc.getLocation().add(Direction.NORTH);
		if (!MapUtil.isFourOdd(newUnpackLoc)) {
			newUnpackLoc = newUnpackLoc.add(Direction.SOUTH_EAST); // now will be 4-odd
		}
		for (int i = 0; i < 8; i++) {
			if (rc.isLocationOccupied(newUnpackLoc)) {
				newUnpackLoc = newUnpackLoc.add(unpackSearchTableX[i],unpackSearchTableY[i]);
			}
			else if (i == 8) {
				newUnpackLoc = null; // For now, if you can't find a close location just give up
			}
			else {
				break;
			}
		}
		return newUnpackLoc;
	}	

}

