package botline_bling;

import com.fasterxml.jackson.databind.AnnotationIntrospector.ReferenceProperty.Type;

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
	private static RobotInfo lowestHealthEnemy = null;
	private static RobotInfo lowestHealthZombie = null;
	
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
		collateNearbyRobotInfo();
		
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
		NavSafetyPolicy safety = new SafetyPolicyAvoidZombies(nearbyZombies);
		
		// get the retreat direction
		Direction retreatDir = dirToClosestZombie.opposite();
		if (dirToClosestZombie.equals(null)) // no zombie attackers
		{
			retreatDir = dirToClosestEnemy.opposite();
			safety = new SafetyPolicyAvoidAllUnits(nearbyEnemies, nearbyZombies);
		}
		
		// figure out if we can safely retreat, and do it if we can
		if (safety.isSafeToMoveTo(here.add(retreatDir)) || safety.isSafeToMoveTo(here.add(retreatDir.rotateRight())) || safety.isSafeToMoveTo(here.add(retreatDir.rotateLeft())))
		{
			Nav.goTo(here.add(retreatDir), safety);
			return true;
		}
		return false;
	}
	
	public static boolean tryRushEnemies() throws GameActionException
	{
		// bum rush enemies in sight range while avoiding zombies
		NavSafetyPolicy safety = new SafetyPolicyAvoidZombies(nearbyZombies);
		if (!dirToClosestEnemy.equals(null))
		{
			Nav.goTo(here.add(dirToClosestEnemy), safety);
			return true;
		}
		return false;
	}
	
	public static boolean tryAttackSomebody() throws GameActionException
	{
		// try to attack someone in weapons range
		// this method MUST BE CALLED AFTER CALLING collateNearbyRobotInfo()
		
		// can we even shoot bro?
		if (!rc.isWeaponReady())
			return false;
		
		// use the info we've collated
		// priority: enemy archon, zombies, enemies
		if (!enemyPriorityTarget.equals(null))
		{
			rc.attackLocation(enemyPriorityTarget.location);
			return true;
		}
		if (!lowestHealthZombie.equals(null))
		{
			rc.attackLocation(lowestHealthZombie.location);
			return true;
		}
		if (!lowestHealthEnemy.equals(null))
		{
			rc.attackLocation(lowestHealthEnemy.location);
			return true;
		}
		
		return false;
	}
	
	private static void collateNearbyRobotInfo() throws GameActionException
	{
		// loop through zombies
		zombieTotalDamagePerTurn = 0;
		lowestHealthZombie = null;
		closestZombie = null;
		if (nearbyZombies.length>0)
		{
			closestZombie = nearbyZombies[0];
			lowestHealthZombie = nearbyZombies[0];
			for (RobotInfo zombie : nearbyZombies)
			{
				// update the closest zombie
				if (here.distanceSquaredTo(zombie.location) < here.distanceSquaredTo(closestZombie.location))
					closestZombie = zombie;
				
				// add up the firepower of relevant zombies
				if (here.distanceSquaredTo(zombie.location) <= zombie.type.attackRadiusSquared)
					zombieTotalDamagePerTurn += (zombie.attackPower / (zombie.type.attackDelay+1));
				
				// keep track of the zombie with lowest health
				if (zombie.health < lowestHealthZombie.health)
					lowestHealthZombie = zombie;
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
			
			// compute direction to closest zombie
			dirToClosestZombie = here.directionTo(closestZombie.location);
		}
		
		// loop through enemy team
		enemyTotalDamagePerTurn = 0;
		lowestHealthEnemy = null;
		closestEnemy = null;
		if (nearbyEnemies.length>0)
		{
			// reset priority target
			enemyPriorityTarget = null;
			closestEnemy = nearbyEnemies[0];
			lowestHealthEnemy = nearbyEnemies[0];
			
			// go through enemies, find targets, count up enemy firepower
			for (RobotInfo enemy : nearbyEnemies)
			{
				// look for archon enemy targets
				if (rc.getType().attackRadiusSquared >= here.distanceSquaredTo(enemy.location))
				{
					if (enemy.type == RobotType.ARCHON)
						enemyPriorityTarget = enemy;
				}
				
				// update the closest enemy
				if (here.distanceSquaredTo(enemy.location) < here.distanceSquaredTo(closestEnemy.location))
					closestEnemy = enemy;
				
				// count up enemy firepower
				if (here.distanceSquaredTo(enemy.location) <= enemy.type.attackRadiusSquared)
					enemyTotalDamagePerTurn += (enemy.attackPower / (enemy.type.attackDelay+1));
				
				// keep track of the enemy with lowest health
				if (enemy.health < lowestHealthEnemy.health)
					lowestHealthEnemy = enemy;
			}
			
			// compute direction to closest enemy
			dirToClosestEnemy = here.directionTo(closestEnemy.location);
		}
	}
}
