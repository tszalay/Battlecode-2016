package botline_bling;

import battlecode.common.*;

public class Micro extends RobotPlayer
{
	public static Direction dirToZombies = null;
	public static Direction dirToEnemies = null;
	
	public static boolean tryAttackSomebody() throws GameActionException
	{
		// can we even shoot bro?
		if (!rc.isWeaponReady())
			return false;
		
		// check zombies first, then enemies (priority is zombies, even over enemy archons...)
		RobotInfo[] nearbyEnemies = rc.senseNearbyRobots(rc.getType().attackRadiusSquared, Team.ZOMBIE);
		if (nearbyEnemies.length == 0)
			nearbyEnemies = rc.senseNearbyRobots(rc.getType().attackRadiusSquared, theirTeam);
		
		// nobody to shoot at :(
		if (nearbyEnemies.length == 0)
			return false;
		
		// if there is somebody to shoot at, pick the bot with the least health
		RobotInfo bestTarget = nearbyEnemies[0];
		for (RobotInfo enemy : nearbyEnemies)
		{	
			if (enemy.health < bestTarget.health)
				bestTarget = enemy;
		}
		
		rc.attackLocation(bestTarget.location);
		return true;
	}
	
	public static void doAvoidBeingKilled() throws GameActionException
	{
		
	}
	
	public static void doAvoidDyingInfectedAtAnyCost() throws GameActionException
	{
		
	}
	
	public static boolean doKiteZombies() throws GameActionException
	{
		
		return true;
	}
	
	public static boolean amOverpowered() throws GameActionException
	{
		
		return true;
	}
	
	public static boolean canWin1v1() throws GameActionException
	{
		
		return true;
	}
	
	public static boolean canSurviveCurrentSkirmish() throws GameActionException
	{
		
		return true;
	}
	
	public static boolean willDieInfected() throws GameActionException
	{
		
		return true;
	}
	
}
