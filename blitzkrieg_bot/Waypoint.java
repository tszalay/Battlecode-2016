package blitzkrieg_bot;

import battlecode.common.*;

import java.util.*;

public class Waypoint extends RobotPlayer
{
	private static MapLocation randomDest = null;
	private static int randomDestRound = -10000;
	
	public static MapLocation getRandomRetreatWaypoint()
	{
		// we haven't changed it in a while, or we're there
		if (roundsSince(randomDestRound) > 300 || here.distanceSquaredTo(randomDest) < 24)
			randomDest = MapInfo.getRandomLocation();
		
		return randomDest;
	}
	
	public static MapLocation getBestRetreatLocation()
	{
		// try retreating to nearby archons first
		MapLocation loc = getClosestFriendlyWaypoint();
		if (loc == null)
			loc = getRandomRetreatWaypoint();
		
		return loc;
	}
	
	public static MapLocation getClosestFriendlyWaypoint()
	{
		MapLocation loc1 = Message.getClosestArchon();
		MapLocation loc2 = Message.getRecentFriendlyLocation();
		
		if (loc1 == null)
			return loc2;
		if (loc2 == null)
			return loc1;
		
		return here.distanceSquaredTo(loc1) < here.distanceSquaredTo(loc2) ? loc1 : loc2;
	}
}
