package botline_bling;

import battlecode.common.*;

public class Micro extends RobotPlayer
{
	public static boolean tryAttackSomebody() throws GameActionException
	{
		// can we even shoot bro?
		if (!rc.isWeaponReady())
			return false;
		
		// check zombies first, then enemies
		RobotInfo[] nearbyEnemies = rc.senseNearbyRobots(rc.getType().attackRadiusSquared, Team.ZOMBIE);
		if (nearbyEnemies.length == 0)
			nearbyEnemies = rc.senseNearbyRobots(rc.getType().attackRadiusSquared, theirTeam);
		
		// nobody to shoot at :(
		if (nearbyEnemies.length == 0)
			return false;
		
		RobotInfo bestTarget = null;
		
		for (RobotInfo ri : nearbyEnemies)
		{
			if (bestTarget == null)
			{
				bestTarget = ri;
				continue;
			}
			
			if (ri.health < bestTarget.health)
				bestTarget = ri;
		}
		
		rc.attackLocation(bestTarget.location);
		return true;
	}
	
}
