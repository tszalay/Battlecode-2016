package botline_bling;

import battlecode.common.*;

public class Micro extends RobotPlayer
{
	public static Direction dirToClosestZombie = null;
	public static Direction dirToClosestEnemy = null;
	public static RobotInfo[] nearbyEnemies = null;
	public static RobotInfo[] nearbyZombies = null;
	public static RobotInfo[] nearbyAllies = null;
	private static double enemyTotalDamagePerTurn = 0;
	private static double zombieTotalDamagePerTurn = 0;
	private static int turnsUntilFirstZombieAttacks = 100;
	private static RobotInfo enemyPriorityTarget = null;
	private static RobotInfo closestEnemy = null;
	private static RobotInfo closestZombie = null;
	
	public static void updateEnemyInfo() throws GameActionException
	{
		nearbyEnemies = rc.senseNearbyRobots(rc.getType().sensorRadiusSquared, theirTeam);
        nearbyZombies = rc.senseNearbyRobots(rc.getType().sensorRadiusSquared, Team.ZOMBIE);
	}
	
	public static void updateEnemyInfo(RobotInfo[] opponents, RobotInfo[] zombies) throws GameActionException
	{
		nearbyEnemies = opponents;
		nearbyZombies = zombies;
	}
	
	public static void updateAllies() throws GameActionException
	{
		nearbyAllies = rc.senseNearbyRobots(rc.getType().sensorRadiusSquared, ourTeam);
	}
	
	public static void doAvoidBeingKilled() throws GameActionException
	{
		
	}
	
	public static void doAvoidDyingInfectedAtAnyCost() throws GameActionException
	{
		
	}
	
	public static boolean tryKiteZombies() throws GameActionException
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
	
	public static boolean tryRetreat() throws GameActionException
	{
		
		return true;
	}
	
	public static boolean tryRushEnemies() throws GameActionException
	{
		
		return true;
	}
	
	public static boolean tryAttackSomebody() throws GameActionException
	{
		// try to attack someone in weapons range
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
	
	private static void collateNearbyRobotInfo() throws GameActionException
	{
		// loop through zombies
		zombieTotalDamagePerTurn = 0;
		if (nearbyZombies.length>0)
		{
			closestZombie = nearbyZombies[0];
			for (RobotInfo zombie : nearbyZombies)
			{
				// update the closest zombie
				if (here.distanceSquaredTo(zombie.location) < here.distanceSquaredTo(closestZombie.location))
					closestZombie = zombie;
				
				// add up the firepower of relevant zombies
				if (here.distanceSquaredTo(zombie.location) <= zombie.type.attackRadiusSquared)
					zombieTotalDamagePerTurn += (zombie.attackPower / (zombie.type.attackDelay+1));
			}
			
			// figure out how long until closest zombie attacks
			RobotInfo[] alliesInClosestZombieSightRange = rc.senseNearbyRobots(closestZombie.location, closestZombie.type.sensorRadiusSquared, ourTeam);
			if (alliesInClosestZombieSightRange.length > 0)
			{
				// loop through to find the ally that the closest zombie will attack
				RobotInfo allyTargetOfClosestZombie = alliesInClosestZombieSightRange[0];
				for (RobotInfo allyTarget : alliesInClosestZombieSightRange)
				{
					if (closestZombie.location.distanceSquaredTo(allyTarget.location) < closestZombie.location.distanceSquaredTo(allyTargetOfClosestZombie.location))
						allyTargetOfClosestZombie = allyTarget;
				}
				
				if (rc.getID() != allyTargetOfClosestZombie.ID)
				{
					// if the closest zombie's target is not me, calculate how many turns it will be engaged with them
					turnsUntilFirstZombieAttacks = (int) ( allyTargetOfClosestZombie.health / (closestZombie.attackPower / (closestZombie.type.attackDelay+1)) );
				}
			}
		}
		
		// loop through enemy team
		enemyTotalDamagePerTurn = 0;
		if (nearbyEnemies.length>0)
		{
			// go through enemies, find targets, count up enemy firepower
			RobotInfo targetEnemy = null;
			for (RobotInfo enemy : nearbyEnemies)
			{
				// look for sitting duck enemy targets
				if (rc.getType().attackRadiusSquared >= here.distanceSquaredTo(enemy.location))
				{
					switch (enemy.type)
					{
						case ARCHON: // archons cannot attack
							targetEnemy = enemy; // archons take priority as targets
							break;
							
						case TTM: // TTMs cannot attack in their packed-up form
							if (targetEnemy.equals(null))
								targetEnemy = enemy;
							break;
							
						case SCOUT: // scouts cannot attack
							if (targetEnemy.equals(null))
								targetEnemy = enemy;
							break;
		
						default: // for other units in attack range, add up firepower
							break;
					}
				}
				
				// count up enemy firepower
				if (here.distanceSquaredTo(enemy.location) <= enemy.type.attackRadiusSquared)
					enemyTotalDamagePerTurn += (enemy.attackPower / (enemy.type.attackDelay+1));
			}
		}
	}
}
