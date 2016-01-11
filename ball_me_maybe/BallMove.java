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
		int tooManyAdjAllies = 1;
		Direction dir = null;
		MapLocation repelLoc = here;
		int numAdjAllies = 0;

		DirectionSet dirs = Micro.getCanMoveDirs();
		Direction archonDir = archonLoc.directionTo(destLoc);
		MapLocation gotoLoc = archonLoc;
		
		// Offset gotoLoc towards dest to make soldiers shortcut a little
		int goToOffset = 5;
		for (int i=0;i<goToOffset;i++) gotoLoc = gotoLoc.add(archonDir);
		
		for (RobotInfo ri : allies)
		{
			if (ri.location.isAdjacentTo(here))
			{
				numAdjAllies++;
				repelLoc = repelLoc.add(ri.location.directionTo(here));
			}
		}
		
		if (numAdjAllies >= tooManyAdjAllies) Nav.tryGoTo(repelLoc, dirs);
		else Nav.tryGoTo(gotoLoc, dirs);
		
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
}
