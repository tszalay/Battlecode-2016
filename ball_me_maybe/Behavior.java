package ball_me_maybe;

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
	
	public static boolean tryRetreat() throws GameActionException
	{
		DirectionSet ds = Micro.getSafeMoveDirs().clone();
		ds.remove(Direction.NONE);
		
		if (ds.any() || !rc.getType().canAttack())
		{
			Direction escapeDir = Micro.getBestEscapeDir();
			return Micro.tryMove(escapeDir);
		}
		else
		{
			return false;
		}
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
			if (tryRetreat() || tryAttackSomeone())
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
    	Direction bestMoveDir = safeMoveDirs.getDirectionTowards(here, here.add(dir));
    	
    	// move if it's valid
    	if (bestMoveDir != null)
    		Micro.tryMove(bestMoveDir);
    	
        return false;
    }
}
