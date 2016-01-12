package ball_on_me;

import battlecode.common.*;

import java.util.*;

public class BallMove extends RobotPlayer
{
	public BallMove()
	{
	}
	
	public static void ballMove(MapLocation archonLoc, MapLocation destLoc, RobotInfo[] allies) throws GameActionException
	{
		// if we can see our archon, clear rubble
		if (rc.canSense(archonLoc))
		{
			if (rc.senseParts(here) > 0 || here.equals(archonLoc.directionTo(destLoc)) || here.distanceSquaredTo(archonLoc) < 6)
			{
				// move off parts, or move if we're in the archon's way
				Behavior.tryAdjacentSafeMoveToward(here.directionTo(archonLoc).opposite());
			}
			tryClearRubble(here.add(here.directionTo(destLoc)));
		}
		
		DirectionSet safeDirs = Micro.getSafeMoveDirs();
		DirectionSet safeNoPartsDirs = safeDirs.and(Micro.getNoPartsDirs());
		
		int tooManyAdjAllies = 2;
		MapLocation repelLoc = here;
		int numAdjAllies = 0;

		Direction archonDir = archonLoc.directionTo(destLoc);
		MapLocation gotoLoc = archonLoc;
		
		// Offset gotoLoc towards dest to make soldiers shortcut a little
		//gotoLoc = gotoLoc.add(archonDir, 0);
		
		for (RobotInfo ri : allies)
		{
			if (ri.location.isAdjacentTo(here))
			{
				numAdjAllies++;
				repelLoc = repelLoc.add(ri.location.directionTo(here));
			}
		}
		
		if (numAdjAllies >= tooManyAdjAllies)
			gotoLoc = repelLoc;
		
		Behavior.tryGoToWithoutBeingShot(gotoLoc, safeNoPartsDirs);
		
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
	
	public static boolean tryClearRubble(MapLocation loc) throws GameActionException
	{
		if (!rc.isCoreReady() || here.directionTo(loc).equals(Direction.OMNI) || rc.getType() == RobotType.TTM) // can't clear our own square
			return false;
		
		if (rc.senseRubble(loc) >= GameConstants.RUBBLE_SLOW_THRESH)
		{
			rc.clearRubble(here.directionTo(loc));
		}
		
		return false;
	}
}
