package blitzkrieg_bot;

import battlecode.common.*;

import java.util.*;

public class Waypoint extends RobotPlayer
{
	private static MapLocation randomDest = null;
	private static int randomDestRound = -10000;
	
	public static MapLocation getRetreatWaypoint()
	{
		// we haven't changed it in a while, or we're there
		if (roundsSince(randomDestRound) > 300 || here.distanceSquaredTo(randomDest) < 24)
			randomDest = MapInfo.getRandomLocation();
		
		return randomDest;
	}
	
	
	public static MapLocation getBestRetreatLocation()
	{
		// try retreating to nearby archons first
		MapLocation loc = Message.getClosestArchon();
		if (loc == null)
			loc = Message.getRecentFriendlyLocation();
		if (loc == null)
			loc = getRetreatWaypoint();
		
		return loc;
	}
}
