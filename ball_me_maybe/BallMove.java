package ball_me_maybe;

import battlecode.common.*;

import java.util.*;

public class BallMove extends RobotPlayer
{
	public BallMove()
	{
	}
	
	public static void ballMove(MapLocation archonLoc, MapLocation destLoc, RobotInfo[] allies) throws GameActionException
	{
		// try to retreat towards the ball or shoot if we're in danger
		if (Micro.getRoundsUntilDanger() < 10)
		{
			if (archonLoc != null)
				Behavior.tryRetreatTowards(archonLoc, Micro.getSafeMoveDirs());
			else
				Behavior.tryGoToWithoutBeingShot(here, Micro.getSafeMoveDirs());
			
			return;
		}
		
		if (here.distanceSquaredTo(archonLoc) > 40)
		{
			Nav.tryGoTo(archonLoc,Micro.getSafeMoveDirs());
			return;
		}
		if (here.distanceSquaredTo(archonLoc) < 7)
		{
			Behavior.tryAdjacentSafeMoveToward(here.directionTo(archonLoc).opposite());
			return;
		}
		
		MapLocation ml = Micro.getUnitCOM(rc.senseNearbyRobots(24, ourTeam));
		Direction ar = here.directionTo(archonLoc).rotateRight().rotateRight();
		Direction al = here.directionTo(archonLoc).rotateLeft().rotateLeft();
		if (here.add(al).distanceSquaredTo(ml) > here.add(ar).distanceSquaredTo(ml))
			Behavior.tryAdjacentSafeMoveToward(al);
		else
			Behavior.tryAdjacentSafeMoveToward(ar);
	}
	
	public static MapLocation[] updateBallDests(RobotInfo[] allies) throws GameActionException
	{
		MapLocation[] locs = new MapLocation[]{null, null};

		boolean archonInSight = false;

		// update archon and dest locations
		MapLocation archonLoc = Message.recentArchonLocation;
		MapLocation destLoc = Message.recentArchonDest;
		
		if (archonLoc == null)
			archonLoc = here;
		if (destLoc == null)
			destLoc = here;

		// if you see an archon update your location
		for (RobotInfo ri : allies)
		{
			if (ri.type == RobotType.ARCHON)
				archonLoc = ri.location;
			archonInSight = true;
		}

		// If you can see your saved archonLoc but can't find the archon, assume archon is at Dest
		if (rc.canSenseLocation(archonLoc) && !archonInSight)
			archonLoc = destLoc;
		
		locs[0] = archonLoc;
		locs[1] = destLoc;
		return locs;
	}
	
	public static boolean tryClearRubble(Direction dir) throws GameActionException
	{
		if (!rc.isCoreReady() || dir.equals(Direction.OMNI) || rc.getType() == RobotType.TTM) // can't clear our own square
			return false;
		
		if (rc.senseRubble(here.add(dir)) >= GameConstants.RUBBLE_SLOW_THRESH)
		{
			rc.clearRubble(dir);
		}
		
		return false;
	}
}
