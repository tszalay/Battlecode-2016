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
	
	// this function always runs away, no matta whats
	public static boolean tryRetreatOrShootIfStuck() throws GameActionException
	{
		Direction dir = Micro.getBestRetreatDir();
		
		if (dir == null || !rc.canMove(dir))
			dir = Micro.getBestEscapeDir();
		
		// we can't move in best retreat dir or best escape dir, but we have some allowed moves
		if ((dir == null || !rc.canMove(dir)) && Micro.getCanMoveDirs().any())
		{
			DirectionSet canMove = Micro.getCanMoveDirs();
			DirectionSet safeMove = Micro.getSafeMoveDirs();
			DirectionSet fastMove = Micro.getCanFastMoveDirs();
			DirectionSet turretSafe = Micro.getTurretSafeDirs();
			
			// priority 1: canMove and safeMove and turretSafe and fastMove
			if (canMove.and(turretSafe).and(safeMove).and(fastMove).any())
				dir = canMove.and(turretSafe).and(safeMove).and(fastMove).getRandomValid();
			
			// priority 2: canMove and safeMove and turretSafe
			else if (canMove.and(turretSafe).and(safeMove).any())
				dir = canMove.and(turretSafe).and(safeMove).getRandomValid();
			
			// priority 3: canMove and safeMove and fastMove
			else if (canMove.and(safeMove).and(fastMove).any())
				dir = canMove.and(safeMove).and(fastMove).getRandomValid();
			
			// priority 4: canMove and turretSafe and fastMove
			else if (canMove.and(turretSafe).and(fastMove).any())
				dir = canMove.and(turretSafe).and(fastMove).getRandomValid();
			
			// priority 5: canMove and safeMove
			else if (canMove.and(safeMove).any())
				dir = canMove.and(safeMove).getRandomValid();

			// priority 6: canMove and turretSafe
			else if (canMove.and(turretSafe).any())
				dir = canMove.and(turretSafe).getRandomValid();
			
			// priority 7: canMove and fastMove
			else if (canMove.and(fastMove).any())
				dir = canMove.and(fastMove).getRandomValid();

			// priority 8: canMove baby, and you better like it
			else
				dir = canMove.getRandomValid();
			
		}
		
		// if we can't escape (we're stuck), try to shoot
		if (dir == null)
			return tryAttackSomeone();
		else
			return Micro.tryMove(dir);
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
