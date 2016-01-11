package ball_is_life;

import ball_me_maybe.BallMove;
import battlecode.common.*;

public class RoboSoldier extends RobotPlayer
{
	static int fate;
	
	public static void init()
	{
		fate = rand.nextInt(1000);
	}
	
	public static void turn() throws GameActionException
	{
		RobotInfo[] allies = Micro.getNearbyAllies();
		
		MapLocation[] locs = updateDests(allies); // updates archon and archon destination using messaging
		MapLocation archonLoc = locs[0];
		MapLocation destLoc = locs[1];
		
		if (rc.isCoreReady())	
		{
			BallMove.ballMove(archonLoc, destLoc, allies);
		}
	}
	
	public static MapLocation[] updateDests(RobotInfo[] allies) throws GameActionException
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
