package team023;

import battlecode.common.*;

import java.util.*;

public class Behavior extends RobotPlayer
{
	public Behavior()
	{
	}
	
	public static boolean tryAttackSomeone() throws GameActionException
	{
		// attack someone in range if possible, low health first, prioritizing zombies
		
		if (rc.getWeaponDelay() >= 1)
			return false;
		
		RobotInfo zombieTarget = Micro.getLowestHealth(Micro.getNearbyZombies());
		RobotInfo enemyTarget = Micro.getLowestHealth(Micro.getNearbyEnemies());
		
		if (zombieTarget != null && rc.canAttackLocation(zombieTarget.location))
		{
			rc.attackLocation(zombieTarget.location);
			return true;
		}
		
		if (enemyTarget != null && rc.canAttackLocation(enemyTarget.location))
		{
			rc.attackLocation(enemyTarget.location);
			return true;
		}
		
		// attack a sighted target if possible
		if (rc.getType() != RobotType.TURRET)
			return false;
		
		MapLocation targetLoc = Sighting.getClosestSightedTarget();
		
		if (targetLoc == null)
			targetLoc = MapInfo.getClosestDen();
		
		if (targetLoc != null && rc.canAttackLocation(targetLoc))
		{
			rc.attackLocation(targetLoc);
			return true;
		}
		
		return false;
	}
	
	public static boolean tryShootAndAvoidBeingShot() throws GameActionException
	{
		// this method only performs an action if we are in imminent danger of being shot
		// otherwise it does nothing and returns false
		
		// check if we are on a safe square (not in attack range of any hostile)
		DirectionSet safeDirs = Micro.getSafeMoveDirs();
		if (safeDirs.isValid(Direction.NONE))
		{
			// we are on a safe square, no need to move
			return false;
		}
		
		// if we are not safe, try to retreat and get in as many pot shots as possible
		// if we cannot move, shoot
		if (tryShootWhileRetreating())
			return true;
		
		return tryAttackSomeone();
	}
	
	public static boolean tryRetreat() throws GameActionException
	{
		// tries to move to the square farthest from the closest enemy
		// if this move is not safe, it tries the squares rotated right and left (IS THIS A GOOD THING OR NECESSARY?)
		// if those are also not safe, it does nothing
		
		Direction escapeDir = Micro.getBestEscapeDir();
		return Micro.tryMove(escapeDir);
	}
	
	public static boolean tryGoToWithoutBeingShot(MapLocation target, DirectionSet dirSet) throws GameActionException
	{
		// uses Nav to move safely toward a given MapLocation
		// avoids being shot, but gives the minimum berth to hostiles
		
		// count rounds until danger
		// if < value
		// retreat
		// else
		// bug to dest
		
//		int roundsUntilDanger = Micro.getRoundsUntilDanger();
//		if (roundsUntilDanger == 0)
//		{
//			Debug.setStringAK("Decided to retreat because roundsUntilDanger = " + roundsUntilDanger);
//			if (!tryRetreat())
//				return tryAttackSomeone();
//			else
//				return true;
//		}
		
		RobotInfo closestHostile = Micro.getClosestUnitTo(Micro.getNearbyHostiles(), here);
		if (closestHostile != null && closestHostile.location.distanceSquaredTo(here) <= closestHostile.type.attackRadiusSquared)
		{
			Debug.setStringAK("Decided to retreat because I am in attack range of " + closestHostile.ID);
			if (!tryRetreat())
				return tryAttackSomeone();
			else
				return true;
		}
		else
		{
			Debug.setStringAK("Decided to bug nav.");
			return Nav.tryGoTo(target, dirSet);
		}
	}
	
	public static boolean tryGoToFarFromHostiles(MapLocation target) throws GameActionException
	{
		// moves not only safely toward a given MapLocation, but also prioritizes keeping hostiles out of sensor range
		// can move so that a hostile enters sensor range, but once in range, this will move away
		// extremely shy
		
		if (tryStayFarFromHostiles())
			return true;
		
		return Nav.tryGoTo(target, Micro.getSafeMoveDirs());
	}
	
	public static boolean tryShootWhileRetreating() throws GameActionException
	{
		// while retreating
		// if you can get off a shot before moving without getting caught by the enemy, do so
		// if not, go to spot that is farthest from closest enemy
		
		RobotInfo[] hostiles = Micro.getNearbyHostiles();
		
		if (hostiles == null || hostiles.length == 0)
			return false;
		
		double ourDelayDecrement = 1; // valid for bytecode use <= 2000.  if we are going over this, we'll need to use the formula in the future
		int roundsForUsToShootAndMove = (int) Math.floor( Math.max( (rc.getWeaponDelay() + rc.getType().cooldownDelay), rc.getCoreDelay()) / ourDelayDecrement);
		
//		int minRoundsBeforeTheyCanShoot = 100;
//		for (RobotInfo ri : hostiles)
//		{
//			int movesNeededToBringThemIntoRange = Math.max(0, here.distanceSquaredTo(ri.location) - ri.type.attackRadiusSquared);
//			int roundsBeforeTheyCanShoot = (int) Math.floor( Math.max( (ri.coreDelay + ri.type.movementDelay * movesNeededToBringThemIntoRange), ri.weaponDelay) );
//			if (roundsBeforeTheyCanShoot < minRoundsBeforeTheyCanShoot)
//				minRoundsBeforeTheyCanShoot = roundsBeforeTheyCanShoot;
//		}
		
		int minRoundsBeforeTheyCanShoot = Micro.getRoundsUntilDanger();
		
		if (minRoundsBeforeTheyCanShoot >= roundsForUsToShootAndMove)
		{
			if (!tryAttackSomeone())
				return tryRetreat();
			else
				return true;
		}
		else
		{
			return tryRetreat();
		}
	}
	
	public static boolean tryShootWhileRetreatingFromZombies() throws GameActionException
	{
		// while retreating
		// if you can get off a shot before moving without getting caught by the enemy, do so
		// if not, go to spot that is farthest from closest enemy
		
		RobotInfo[] zombies = Micro.getNearbyZombies();
		
		if (zombies == null || zombies.length == 0)
		{
			return tryAttackSomeone();
		}
		
		double ourDelayDecrement = 1; // valid for bytecode use <= 2000.  if we are going over this, we'll need to use the formula in the future
		int roundsForUsToShootAndMove = (int) Math.floor( Math.max( (rc.getWeaponDelay() + rc.getType().cooldownDelay), rc.getCoreDelay()) / ourDelayDecrement);
		
//		int minRoundsBeforeTheyCanShoot = 100;
//		for (RobotInfo ri : hostiles)
//		{
//			int movesNeededToBringThemIntoRange = Math.max(0, here.distanceSquaredTo(ri.location) - ri.type.attackRadiusSquared);
//			int roundsBeforeTheyCanShoot = (int) Math.floor( Math.max( (ri.coreDelay + ri.type.movementDelay * movesNeededToBringThemIntoRange), ri.weaponDelay) );
//			if (roundsBeforeTheyCanShoot < minRoundsBeforeTheyCanShoot)
//				minRoundsBeforeTheyCanShoot = roundsBeforeTheyCanShoot;
//		}
		
		int minRoundsBeforeTheyCanShoot = Micro.getRoundsUntilDanger();
		
		if (minRoundsBeforeTheyCanShoot >= roundsForUsToShootAndMove)
		{
//			if (!tryAttackSomeone())
//				return tryRetreat();
//			else
//				return true;
			return tryAttackSomeone();
		}
		else // can't get a shot off, so run
		{
			if (!tryRetreat())
			{
				Debug.setStringSJF("I want to retreat but can't, so I'm shooting.");
				return tryAttackSomeone(); // can't run, just shoot
			}
			else
				return true;
		}
	}
	
	public static boolean tryStayFarFromHostiles() throws GameActionException
	{
		// check for hostiles in sensor range, if none, do nothing
		RobotInfo[] hostiles = Micro.getNearbyHostiles();
		
		if (hostiles == null || hostiles.length == 0)
			return false;
		
		// if we cannot move at all, try to shoot
		if (!rc.isCoreReady())
			return tryAttackSomeone();
		
		// try to escape
		Direction escapeDir = Micro.getBestEscapeDir();
		if (Micro.tryMove(escapeDir))
			return true;
		
		// if we cannot move in a safe escape direction, try to shoot
		return tryAttackSomeone();
	}
	
	public static boolean tryAdjacentSafeMoveToward(Direction dir) throws GameActionException
    {
    	if (!rc.isCoreReady() || dir == null)
    		return false;
    	
    	// check safe moves
    	DirectionSet safeMoveDirs = Micro.getSafeMoveDirs();
    	if(!safeMoveDirs.any())
    		return false;
    	
    	// get safe direction closest to dir
    	Direction bestMoveDir = safeMoveDirs.getDirectionTowards(here, here.add(dir));
    	
    	// move if it's valid
    	if (bestMoveDir != null)
    		Micro.tryMove(bestMoveDir);
    	
        return false;
    }
}
