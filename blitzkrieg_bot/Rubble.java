package blitzkrieg_bot;

import battlecode.common.*;

import java.util.*;

public class Rubble extends RobotPlayer
{
	public static boolean tryClearRubble(MapLocation target) throws GameActionException
	{
		//System.out.println("tryClearRubble got called for " + target.toString());
		
		if (target == null)
			return false;
		
		if (!rc.isCoreReady())
			return false;
		
		if (rc.canSense(target) && rc.senseRubble(here.add(here.directionTo(target))) < GameConstants.RUBBLE_OBSTRUCTION_THRESH)
			return false;
		
		// DIG to target
		
		// move off target
		if (target.equals(here))
		{
			if (!Action.tryMove(Micro.getCanMoveDirs().getRandomValid()))
			{
				Direction rubbleDir = getRandomAdjacentRubble();
				if (rubbleDir != null)
				{
					doClearRubble(rubbleDir);
					return true;
				}
			}
			else
			{
				return true;
			}
		}
		
		// clear rubble in the way of parts
		if (rc.senseRubble(here.add(here.directionTo(target))) > GameConstants.RUBBLE_OBSTRUCTION_THRESH)
		{
			doClearRubble(here.directionTo(target));
			return true;
		}
		
		return false;
	}
	
	public static boolean tryClearSmallRubble(MapLocation target) throws GameActionException
	{
		//System.out.println("tryClearRubble got called for " + target.toString());
		
		if (target == null)
			return false;
		
		if (!rc.isCoreReady())
			return false;
		
		if (rc.canSense(target) && rc.senseRubble(here.add(here.directionTo(target))) < GameConstants.RUBBLE_OBSTRUCTION_THRESH)
			return false;
		
		// DIG to target
		Direction towards = here.directionTo(target);
		double rubble = rc.senseRubble(here.add(towards));
		if (rubble < 200 && rubble >= 100)
		{
			doClearRubble(towards);
			return true;
		}
		
		Direction tLeft = towards.rotateLeft();
		rubble = rc.senseRubble(here.add(tLeft));
		if (rubble < 200 && rubble >= 100)
		{
			doClearRubble(tLeft);
			return true;
		}
		
		Direction tRight = towards.rotateRight();
		rubble = rc.senseRubble(here.add(tRight));
		if (rubble < 200 && rubble >= 100)
		{
			doClearRubble(tRight);
			return true;
		}
		
		return false;
	}
	
	public static Direction getRandomAdjacentRubble() throws GameActionException
	{
		DirectionSet dirs = DirectionSet.makeAll();
		dirs.remove(Direction.NONE);
		for(Direction dir : Direction.values())
		{
			if (!dirs.isValid(dir))
				continue;
			
			if (rc.senseRubble(here.add(dir)) > GameConstants.RUBBLE_OBSTRUCTION_THRESH && rc.senseRubble(here.add(dir)) < 5000)
			{
				return dir;
			}
		}
		
		return null;
	}
	
	public static void doClearRubble(Direction dir) throws GameActionException
	{
		if (rc.isCoreReady() && dir != null)
			rc.clearRubble(dir);
	}
	
	public static MapLocation senseClosestPart() throws GameActionException
	{
		MapLocation[] parts = rc.sensePartLocations(rc.getType().sensorRadiusSquared);
		
		if (parts == null || parts.length == 0)
			return null;
		
		MapLocation closestPartLoc = null;
		for (MapLocation loc : parts)
		{
			// don't try to go to a part we can't get
			if (rc.senseRobotAtLocation(loc) == null)
				if (closestPartLoc == null || here.distanceSquaredTo(loc) < here.distanceSquaredTo(closestPartLoc))
					closestPartLoc = loc;
		}
		
		return closestPartLoc;
	}
}
