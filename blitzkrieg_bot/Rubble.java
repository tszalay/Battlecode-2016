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
	
	public static boolean tryClearRubbleInPathIfClearBeyondAndAlliesAround(MapLocation target) throws GameActionException
	{
		if (target == null)
			return false;
		
		if (!rc.isCoreReady())
			return false;
		
		if (rc.canSense(target) && rc.senseRubble(here.add(here.directionTo(target))) < GameConstants.RUBBLE_OBSTRUCTION_THRESH)
			return false;
		
		// if not many others are around, we're not piling up, so assume we can nav and don't dig
		if (Micro.getNearbyAllies() == null || Micro.getNearbyAllies().length < 4)
			return false;
		
		// look for a direction where digging is minimal
		
		// straight ahead
		MapLocation temp = here;
		double rubAhead = 0;
		for (int i=0; i<3; i++)
		{
			temp = temp.add(temp.directionTo(target)); // projected location
			double rubble = rc.senseRubble(temp);
			if (rubble > GameConstants.RUBBLE_OBSTRUCTION_THRESH)
				rubAhead += rubble - GameConstants.RUBBLE_OBSTRUCTION_THRESH;
			// if there is no light at the end... don't go
			if (rubble > GameConstants.RUBBLE_OBSTRUCTION_THRESH && i==2)
				rubAhead += 10001;
		}
		
		// ahead right
		temp = here;
		double rubRight = 0;
		for (int i=0; i<3; i++)
		{
			temp = temp.add(temp.directionTo(target).rotateRight()); // projected location
			double rubble = rc.senseRubble(temp);
			if (rubble > GameConstants.RUBBLE_OBSTRUCTION_THRESH)
				rubRight += rubble - GameConstants.RUBBLE_OBSTRUCTION_THRESH;
			// if there is no light at the end... don't go
			if (rubble > GameConstants.RUBBLE_OBSTRUCTION_THRESH && i==2)
				rubAhead += 10001;
		}
		
		// ahead left
		temp = here;
		double rubLeft = 0;
		for (int i=0; i<3; i++)
		{
			temp = temp.add(temp.directionTo(target).rotateLeft()); // projected location
			double rubble = rc.senseRubble(temp);
			if (rubble > GameConstants.RUBBLE_OBSTRUCTION_THRESH)
				rubLeft += rubble - GameConstants.RUBBLE_OBSTRUCTION_THRESH;
			// if there is no light at the end... don't go
			if (rubble > GameConstants.RUBBLE_OBSTRUCTION_THRESH && i==2)
				rubAhead += 10001;
		}
		
		// get min rubble direction
		double rubble = rubAhead;
		Direction towards = here.directionTo(target);
		if (rubRight < rubAhead)
		{
			rubble = rubRight;
			towards = here.directionTo(target).rotateRight();
		}
		if (rubLeft < rubRight && rubLeft < rubAhead)
		{
			rubble = rubLeft;
			towards = here.directionTo(target).rotateLeft();
		}
		
		// DIG to target
		if (rubble < 10000 && rc.senseRubble(here.add(towards)) >= GameConstants.RUBBLE_OBSTRUCTION_THRESH)
		{
			doClearRubble(towards);
			return true;
		}
		
		return false;
	}
	
	public static Direction getRandomAdjacentRubble() throws GameActionException
	{
		DirectionSet dirs = DirectionSet.makeAll();
		dirs.remove(Direction.NONE);
		for(Direction dir : dirs.getDirections())
		{
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
