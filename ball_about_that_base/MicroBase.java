package ball_about_that_base;

import java.util.ArrayList;

import battlecode.common.*;

public class MicroBase extends RobotPlayer
{
	public RobotInfo[] nearbyEnemies = null;
	public RobotInfo[] nearbyZombies = null;
	public RobotInfo[] nearbyAllies = null;
	
	public DirectionSet canMoveDirs = null;
	public DirectionSet safeMoveDirs = null;
	public DirectionSet reallySafeMoveDirs = null;
	public DirectionSet canBeSeenDirs = null;
	
	public DirectionSet oddDirs = null;
	
	public Direction closestZombieDir = null;
	public Direction closestEnemyDir = null;
	public Direction bestEscapeDir = null;
	
	
	public MicroBase()
	{
	}
	
	public RobotInfo[] getNearbyEnemies()
	{
		if (nearbyEnemies != null)
			return nearbyEnemies;
		
		nearbyEnemies = rc.senseNearbyRobots(rc.getType().sensorRadiusSquared, theirTeam);
		return nearbyEnemies;
	}
	
	public RobotInfo[] getNearbyAllies()
	{
		if (nearbyAllies != null)
			return nearbyAllies;
		
		nearbyAllies = rc.senseNearbyRobots(rc.getType().sensorRadiusSquared, ourTeam);
		return nearbyAllies;
	}
	
	public RobotInfo[] getNearbyZombies()
	{
		if (nearbyZombies != null)
			return nearbyZombies;
		
		nearbyZombies = rc.senseNearbyRobots(rc.getType().sensorRadiusSquared, Team.ZOMBIE);
		return nearbyZombies;
	}
	
	/*	
	public static void updateAllies() throws GameActionException
	{
		// find allies within sensor range
		nearbyAllies = rc.senseNearbyRobots(rc.getType().sensorRadiusSquared, ourTeam);
		
		// go though and tally up firepower
		RobotInfo me = rc.senseRobot(rc.getID());
		allyTotalDamagePerTurn = ( me.attackPower / (me.type.attackDelay + 1) );
		allyTotalHealth = me.health;
		if (nearbyAllies.length > 0)
		{
			for (RobotInfo friend : nearbyAllies)
			{
				allyTotalDamagePerTurn += ( friend.attackPower / (friend.type.attackDelay + 1) );
				allyTotalHealth += friend.health;
			}
		}
	}
	
	public static boolean tryMove(MapLocation loc, NavSafetyPolicy safety) throws GameActionException
	{
		Nav.goTo(loc, safety);
		if (here.equals(rc.getLocation())) // we didn't move
			return false;
		else
			return true;
	}
	
	public static boolean tryRetreat() throws GameActionException
	{
		if (!rc.isCoreReady())
			return false;
		
		NavSafetyPolicy safety = new SafetyPolicyRecklessAbandon();
		
		// get the retreat direction
		Direction retreatDir = null;
		if (dirToClosestEnemy == Direction.NONE && dirToClosestZombie == Direction.NONE) // no attackers
			return false;
		
		// retreat if we can within the bounds of the safety policy
		MapLocation retreatLoc = here.add(dirToClosestEnemy.opposite()).add(dirToClosestZombie.opposite());
		return (tryMove(retreatLoc, safety));
	}
	
	public static boolean tryRushEnemies() throws GameActionException
	{
		// bum rush enemies in sight range while avoiding zombies
		NavSafetyPolicy safety = new SafetyPolicyAvoidZombies(nearbyZombies);
		if (dirToClosestEnemy != null)
			return tryMove(here.add(dirToClosestEnemy), safety);
		
		return false;
	}
	
	public static boolean tryAttackBot(RobotInfo targetBot) throws GameActionException
	{
		if (targetBot == null)
			return false;
		if (rc.canAttackLocation(targetBot.location))
		{
			rc.attackLocation(targetBot.location);
			return true;
		}
		return false;
	}
	
	public static boolean tryAttackSomebody() throws GameActionException
	{
		// try to attack someone in weapons range
		// this method SHOULD BE CALLED AFTER CALLING collateNearbyRobotInfo()
		// this method can be called from outside without doing anything first, it will just default to shooting at bot with least health
		
		// can we even shoot bro?
		if (!rc.isWeaponReady())
			return false;
		
		// use the info we've collated
		// priority: enemy archon, zombies, enemies
		if (tryAttackBot(enemyPriorityTarget))
			return true;
		if (tryAttackBot(lowestHealthZombie))
			return true;
		if (tryAttackBot(lowestHealthEnemy))
			return true;
		
		// if this is called from outside Micro.java, and the targets are not set, just default to this:
		RobotInfo[] localEnemies = rc.senseHostileRobots(here, rc.getType().attackRadiusSquared);
		if (localEnemies.length == 0)
			return false;
		// find enemy with lowest health and try to attack
		RobotInfo target = localEnemies[0];
		for (RobotInfo bot : localEnemies)
		{
			if (bot.health < target.health)
				target = bot;
		}
		if (tryAttackBot(target))
			return true;
		
		return false;
	}
	
	private static void collateNearbyRobotInfo() throws GameActionException
	{
		// loop through zombies
		zombieTotalDamagePerTurn = 0;
		zombieTotalHealth = 0;
		lowestHealthZombie = null;
		closestZombie = null;
		
		dirToClosestZombie = Direction.NONE;
		inZombieSensorRange = false;
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
				
				// check if we are in zombie sensor range
				if (inZombieSensorRange == false && here.distanceSquaredTo(zombie.location) <= zombie.type.sensorRadiusSquared)
					inZombieSensorRange = true;
				
				// add up total health
				zombieTotalHealth += zombie.health;
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
		enemyTotalHealth = 0;
		lowestHealthEnemy = null;
		closestEnemy = null;
		dirToClosestEnemy = Direction.NONE;
		enemyViperInSightRange = false;
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
				
				// check for vipers
				if (enemy.type == RobotType.VIPER)
					enemyViperInSightRange = true;
				
				// add up total health
				enemyTotalHealth += enemy.health;
			}
			
			// compute direction to closest enemy
			dirToClosestEnemy = here.directionTo(closestEnemy.location);
			
		}
		
		//Debug.setStringSJF("enemy dir = " + dirToClosestEnemy.toString() + ", zombie dir = " + dirToClosestZombie.toString());
		
		// figure out if i am in shooting range of anyone
		boolean enemyInRange = ( closestEnemy != null && here.distanceSquaredTo(closestEnemy.location) <= closestEnemy.type.attackRadiusSquared );
		boolean zombieInRange = ( closestZombie != null && here.distanceSquaredTo(closestZombie.location) <= closestZombie.type.attackRadiusSquared );
		if (enemyInRange || zombieInRange)
			amISafe = false;
		else
			amISafe = true;
	}
	*/
	public static RobotInfo getClosestRobot(RobotInfo[] nearby, MapLocation loc)
	{
		int minsqdist = 1000;
		RobotInfo closest = null;
		
		for (RobotInfo ri : nearby)
		{
			if (ri.location.distanceSquaredTo(loc) < minsqdist)
			{
				closest = ri;
				minsqdist = ri.location.distanceSquaredTo(loc);
			}
		}
		
		return closest;
	}
	
	public static RobotInfo getClosestTurretTarget(RobotInfo[] nearby, MapLocation loc)
	{
		int minsqdist = 1000;
		RobotInfo closest = null;
		
		for (RobotInfo ri : nearby)
		{
			int dist = ri.location.distanceSquaredTo(loc);
			
			if (dist < minsqdist && dist > 5)
			{
				closest = ri;
				minsqdist = dist;
			}
		}
		
		return closest;
	}
	
	public MapLocation getAllyCOM()
	{
		return getUnitCOM(this.getNearbyAllies());
	}
	
	public MapLocation getEnemyCOM()
	{
		return getUnitCOM(this.getNearbyEnemies());
	}
	
	public MapLocation getZombieCOM()
	{
		return getUnitCOM(this.getNearbyZombies());
	}
	public MapLocation getUnitCOM(RobotInfo[] nearby)
	{
		int xtot = 0;
		int ytot = 0;
		
		if (nearby.length == 0)
			return here;
		
		for (RobotInfo ri : nearby)
		{
			xtot += ri.location.x;
			ytot += ri.location.y;
		}
		
		xtot /= nearby.length;
		ytot /= nearby.length;
		
		Debug.setStringTS("AA" + new MapLocation(xtot,ytot));
		
		return new MapLocation(xtot,ytot);
	}
}
