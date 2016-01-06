package botline_bling;

import battlecode.common.*;

public class Micro extends RobotPlayer
{
	public static Direction dirToClosestZombie = null;
	public static Direction dirToClosestEnemy = null;
	public static RobotInfo[] nearbyEnemies = new RobotInfo[0];
	public static RobotInfo[] nearbyZombies = new RobotInfo[0];
	public static RobotInfo[] nearbyAllies = new RobotInfo[0];
	private static double enemyTotalDamagePerTurn = 0;
	private static double zombieTotalDamagePerTurn = 0;
	private static double allyTotalDamagePerTurn = 0;
	private static double enemyTotalHealth = 0;
	private static double zombieTotalHealth = 0;
	private static double allyTotalHealth = 0;
	private static int turnsUntilFirstZombieAttacks = 100;
	private static RobotInfo enemyPriorityTarget = null;
	private static RobotInfo closestEnemy = null;
	private static RobotInfo closestZombie = null;
	private static RobotInfo lowestHealthEnemy = null;
	private static RobotInfo lowestHealthZombie = null;
	private static boolean enemyViperInSightRange = false;
	private static boolean inZombieSensorRange = false;
	
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
	
	public static void doAvoidBeingKilled() throws GameActionException
	{
		collateNearbyRobotInfo();
		Debug.setStringSJF("enemies = " + nearbyEnemies.length + ", zombies = " + nearbyZombies.length + ", allies = " + nearbyAllies.length);
		
		// first priority is to attack an enemy archon
		if (enemyPriorityTarget != null)
			tryAttackSomebody();
		
		// deal with the case of zombies nearby
		if (nearbyZombies.length > 0)
		{
			if (amOverpowered())
			{
				// first try to retreat, if we cannot, then attack
				if (!tryRetreat())
					tryAttackSomebody();
			}
			else
			{
				if (willDieInfected())
					tryRushEnemies();
				else
				{
					if (!tryRetreat())
						tryAttackSomebody();
				}
			}
		}
		
		// deal with the case of enemies nearby
		if (nearbyEnemies.length > 0)
		{
			if (amOverpowered())
			{
				// first try to retreat, if we cannot, then attack
				if (!tryRetreat())
					tryAttackSomebody();
			}
			else
				tryAttackSomebody();
		}
		return;
	}
	
	public static void doAvoidDyingInfectedAtAnyCost() throws GameActionException
	{
		collateNearbyRobotInfo();
		
		// check if we're in danger of dying infected
		if (!willDieInfected())
		{
			if (imminentInfection())
			{
				// check if we can retreat to prevent infection
				if (!tryRetreat())
					// otherwise commit suicide
					rc.disintegrate();
			}
			else // we are not in danger
			{
				if (nearbyZombies.length==0)
					tryAttackSomebody();
				else
				{
					if (!tryRetreat())
						tryAttackSomebody();
				}
			}
		}
		else // we will die infected, try to rush enemies
		{
			if (!tryRushEnemies())
				tryAttackSomebody();
		}
		
	}
	
	public static boolean tryKiteZombies(MapLocation target) throws GameActionException
	{
		// fool around avoiding things until we are seen by zombies, then kite toward target
		
		// update enemies
		collateNearbyRobotInfo();
		
		// check if we can be seen by zombies
		if (inZombieSensorRange)
		{
			// if so, kite toward target
			NavSafetyPolicy safety = new SafetyPolicyAvoidAllUnits(nearbyEnemies, nearbyZombies);
			Nav.goTo(target, safety);
			return true;
		}
		else
		{
			// skittish behavior, avoidance tactics
			// first try to retreat, if we cannot, then attack
			if (!tryRetreat())
				tryAttackSomebody();
			return false;
		}
	}
	
	public static boolean amOverpowered() throws GameActionException
	{
		// add up friendly firepower
		updateAllies();
		
		// compare enemy firepower with ours
		return ( allyTotalDamagePerTurn < (enemyTotalDamagePerTurn + zombieTotalDamagePerTurn) );
	}

	public static int howLongCanSurviveCurrentSkirmish() throws GameActionException
	{
		// add up friendly firepower
		updateAllies();
		
		// figure out about how many rounds skirmish will take
		int roundsToWin = (int) ( (enemyTotalHealth + zombieTotalHealth) / allyTotalDamagePerTurn );
		int roundsToLose = (int) ( allyTotalHealth / (enemyTotalDamagePerTurn + zombieTotalDamagePerTurn) );
		
		// return the number of rounds after which the skirmish will be over
		return Math.min(roundsToWin, roundsToLose);
	}
	
	public static boolean imminentInfection() throws GameActionException
	{
		return ( (turnsUntilFirstZombieAttacks < 2) || enemyViperInSightRange);
	}
	
	public static boolean willDieInfected() throws GameActionException
	{
		// get self infection status
		int healingTime = rc.getInfectedTurns();
		int roundsTillDeath = howLongCanSurviveCurrentSkirmish();
		
		// if another infection is imminent, add to healing time
		if (imminentInfection())
			healingTime += 10;
		
		// return whether we will be infected when we die
		return (roundsTillDeath <= healingTime);

	}
	
	private static boolean tryMove(Direction dir, NavSafetyPolicy safety) throws GameActionException
	{
		Nav.goTo(here.add(dir), safety);
		if (here.equals(rc.getLocation())) // we didn't move
			return false;
		else
			return true;
	}
	
	public static boolean tryRetreat() throws GameActionException
	{
		if (!rc.isCoreReady())
			return false;
		
		NavSafetyPolicy safety = new SafetyPolicyAvoidZombies(nearbyZombies);
		
		// get the retreat direction
		Direction retreatDir = dirToClosestZombie.opposite();
		if (dirToClosestZombie == null) // no zombie attackers
		{
			retreatDir = dirToClosestEnemy.opposite();
			//safety = new SafetyPolicyAvoidAllUnits(nearbyEnemies, nearbyZombies);
		}
		
		// figure out if we can safely retreat, and do it if we can
		if (safety.isSafeToMoveTo(here.add(retreatDir)) || safety.isSafeToMoveTo(here.add(retreatDir.rotateRight())) || safety.isSafeToMoveTo(here.add(retreatDir.rotateLeft())))
			return tryMove(retreatDir, safety);
		
		return false;
	}
	
	public static boolean tryRushEnemies() throws GameActionException
	{
		// bum rush enemies in sight range while avoiding zombies
		NavSafetyPolicy safety = new SafetyPolicyAvoidZombies(nearbyZombies);
		if (dirToClosestEnemy != null)
			return tryMove(dirToClosestEnemy, safety);
		
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
		if (enemyPriorityTarget != null)
		{
			if (rc.canAttackLocation(enemyPriorityTarget.location))
			{
				rc.attackLocation(enemyPriorityTarget.location);
				Debug.setStringSJF("attacking [" + enemyPriorityTarget.location.x + ", " + enemyPriorityTarget.location.y + "]");
				return true;
			}
		}
		if (lowestHealthZombie != null)
		{
			if (rc.canAttackLocation(lowestHealthZombie.location))
			{
				rc.attackLocation(lowestHealthZombie.location);
				Debug.setStringSJF("attacking [" + lowestHealthZombie.location.x + ", " + lowestHealthZombie.location.y + "]");
				return true;
			}
		}
		if (lowestHealthEnemy != null)
		{
			if (rc.canAttackLocation(lowestHealthEnemy.location))
			{
				rc.attackLocation(lowestHealthEnemy.location);
				Debug.setStringSJF("attacking [" + lowestHealthEnemy.location.x + ", " + lowestHealthEnemy.location.y + "]");
				return true;
			}
		}
		
		return false;
	}
	
	private static void collateNearbyRobotInfo() throws GameActionException
	{
		// get robot info
		updateEnemyInfo();
		
		// loop through zombies
		zombieTotalDamagePerTurn = 0;
		zombieTotalHealth = 0;
		lowestHealthZombie = null;
		closestZombie = null;
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
	}
}
