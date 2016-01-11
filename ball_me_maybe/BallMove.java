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
//		int tooManyAdjAllies = 3;
//		MapLocation repelLoc = here;
//		int numAdjAllies = 0;
//
//		DirectionSet dirs = Micro.getCanMoveDirs();
//		Direction archonDir = archonLoc.directionTo(destLoc);
//		MapLocation gotoLoc = archonLoc;
//		
//		// Offset gotoLoc towards dest to make soldiers shortcut a little
//		int goToOffset = 5;
//		for (int i=0;i<goToOffset;i++) gotoLoc = gotoLoc.add(archonDir);
//		
//		for (RobotInfo ri : allies)
//		{
//			if (ri.location.isAdjacentTo(here))
//			{
//				numAdjAllies++;
//				repelLoc = repelLoc.add(ri.location.directionTo(here));
//			}
//		}
//		
//		if (numAdjAllies >= tooManyAdjAllies) Nav.tryGoTo(repelLoc, dirs);
//		else Nav.tryGoTo(gotoLoc, dirs);
		
		// if we can see our archon, clear rubble
		if (rc.canSense(archonLoc))
		{
			tryClearRubble(here.add(here.directionTo(destLoc)));
		}
		
		Behavior.tryGoToWithoutBeingShot(destLoc);
		
	}
	
	public static MapLocation[] updateBallDests(RobotInfo[] allies) throws GameActionException
	{
		MapLocation[] locs = new MapLocation[]{null, null};

		boolean archonInSight = false;

		// update archon and dest locations
		MapLocation archonLoc = Message.recentArchonLocation;
		MapLocation destLoc = Message.recentArchonDest;
		if (archonLoc == null) archonLoc = here;
		if (destLoc == null) destLoc = here;

		// if you see an archon update your location
		for (RobotInfo ri : allies)
		{
			if (ri.type == RobotType.ARCHON) archonLoc = ri.location;
			archonInSight = true;
		}

		// If you can see your saved archonLoc but can't find the archon, assume archon is at Dest
		if (rc.canSenseLocation(archonLoc) && !archonInSight) archonLoc = destLoc;
		
		locs[0] = archonLoc;
		locs[1] = destLoc;
		return locs;
	}
	
	public static boolean tryClearRubble(MapLocation loc) throws GameActionException
	{
		if (!rc.isCoreReady())
			return false;
		
		Direction rubbleDir = null;
		if (rc.senseRubble(loc) >= GameConstants.RUBBLE_SLOW_THRESH)
		{
			rc.clearRubble(here.directionTo(loc));
		}
		
		return false;
	}
}