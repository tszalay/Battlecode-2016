package dropitlikeitsBot;

import battlecode.common.*;

import java.util.*;

public class Behavior extends RobotPlayer
{
	public static boolean tryAttackSomeone() throws GameActionException
	{
		// attack someone in range if possible, low health first, prioritizing zombies
		
		if (!rc.isWeaponReady())
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
		// only relevant if we're a turret
		if (rc.getType() != RobotType.TURRET)
			return false;
		
		MapLocation targetLoc = Sighting.getClosestSightedTarget();
		
		// disabled so that turrets only attack dens within sight range
		//if (targetLoc == null)
		//	targetLoc = MapInfo.getClosestDen();
		
		if (targetLoc != null && rc.canAttackLocation(targetLoc))
		{
			rc.attackLocation(targetLoc);
			return true;
		}
		
		return false;
	}
	
	// this function always runs away, no matta whats
	public static boolean tryRetreatOrShootIfStuck() throws GameActionException
	{
		Direction escapeDir = Micro.getBestEscapeDir();
		// if we can't escape (we're stuck), try to shoot
		if (escapeDir == null)
			return tryAttackSomeone();
		else
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
		
		int roundsUntilDanger = Micro.getRoundsUntilDanger();
		int dangerThreshold = 2;
		int roundsUntilShootAndMove = Micro.getRoundsUntilShootAndMove();
		Debug.setStringTS("DR: " + roundsUntilDanger + " SR: " + roundsUntilShootAndMove);
		
		// if we're in danger, try to retreat before we try to shoot
		if (roundsUntilDanger <= dangerThreshold)
			if (tryRetreatOrShootIfStuck() || tryAttackSomeone())
				return true;
		
		// if we are in a small amount of danger, try to shoot first
		if (roundsUntilDanger >= dangerThreshold + roundsUntilShootAndMove)
			if (tryAttackSomeone())// || tryRetreat())
				return true;
		
		return Nav.tryGoTo(target, dirSet);
	}
	
	public static boolean tryRetreatTowards(MapLocation target, DirectionSet dirSet) throws GameActionException
	{
		int roundsUntilDanger = Micro.getRoundsUntilDanger();
		int dangerThreshold = 2;
		int roundsUntilShootAndMove = Micro.getRoundsUntilShootAndMove();
		
		if (target == null)
			return tryRetreatOrShootIfStuck();
		
		// if we're in danger, try to retreat before we try to shoot
		if (roundsUntilDanger <= dangerThreshold)
			if (tryAdjacentSafeMoveToward(here.directionTo(target)) || tryAttackSomeone())
				return true;
		
		// if we are in a small amount of danger, try to shoot first
		if (roundsUntilDanger >= dangerThreshold + roundsUntilShootAndMove)
			if (tryAttackSomeone())// || tryRetreat())
				return true;
		
		return Nav.tryGoTo(target, dirSet);
	}
	
	public static boolean tryAdjacentSafeMoveToward(MapLocation loc) throws GameActionException
	{
		return tryAdjacentSafeMoveToward(here.directionTo(loc));
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
    	Direction bestMoveDir = safeMoveDirs.getDirectionTowards(dir);
    	
    	// move if it's valid
    	if (bestMoveDir != null)
    		return Micro.tryMove(bestMoveDir);
    	
        return false;
    }
	
	public static boolean tryClearRubble(Direction dir) throws GameActionException
	{
		if (!rc.getType().canClearRubble())
			return false;
		
		if (!rc.isCoreReady() || dir.equals(Direction.OMNI)) // can't clear our own square
			return false;
		
		if (rc.senseRubble(here.add(dir)) >= GameConstants.RUBBLE_SLOW_THRESH)
		{
			rc.clearRubble(dir);
		}
		
		return false;
	}
}
