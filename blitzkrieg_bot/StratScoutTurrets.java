package blitzkrieg_bot;

import battlecode.common.*;
import java.util.*;

public class StratScoutTurrets extends RobotPlayer implements Strategy
{	
	private Strategy overrideStrategy = null;
	
	private Direction lastMoveDir = Direction.NORTH;
	private boolean isClockwise = true;

	public String getName()
	{
		if (overrideStrategy != null)
			return overrideStrategy.getName();

		return "Turret monitoring";
	}
	
	// we oughta scout turrets if there are turrets to scout
	public static boolean shouldScoutTurrets()
	{
		return Micro.getFriendlyUnits().Turrets > 0 && Micro.getFriendlyUnits().Scouts == 0;
	}
	
	public boolean tryTurn() throws GameActionException
	{
		// do we have a strategy that takes precedence over this one?
		if (overrideStrategy != null)
		{
			if (overrideStrategy.tryTurn())
				return true;
			else
				overrideStrategy = null;
		}
		
		// can't do anything, but can stay in state
		if (!rc.isCoreReady())
			return true;
		
		MapLocation loc = null;
		for (RobotInfo ri : Micro.getNearbyEnemies())
		{
			if (ri.type != RobotType.TURRET)
				continue;
			
			if (loc == null || here.distanceSquaredTo(ri.location) < here.distanceSquaredTo(loc))
				loc = ri.location;
		}
		
		// if there's nothing to go to nearby, do something else
		if (loc == null)
			return false;
		
		// otherwise go towards nearest turret
		Direction dir = here.directionTo(loc);
		DirectionSet dirs = Micro.getSafeMoveDirs();

		for (int i=0; i<5; i++)
		{
			if (dirs.isValid(dir))
			{
				Action.tryMove(dir);
				return true;
			}
			dir = isClockwise ? dir.rotateLeft() : dir.rotateRight();
		}
		// didn't find one :(
		// change direction for next time
		isClockwise = !isClockwise;
		//Nav.tryGoTo(loc, Micro.getSafeMoveDirs());
		
		return true;
	}
	

}
