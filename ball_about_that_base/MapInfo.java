package ball_about_that_base;

import battlecode.common.*;
import java.util.*;

public class MapInfo extends RobotPlayer
{
	public static FastLocSet zombieDenLocations = new FastLocSet();
	public static FastLocSet goodPartsLocations = new FastLocSet();
	public static FastLocSet exploredMapWaypoints = new FastLocSet();
	
	public static MapLocation mapMin = new MapLocation(-18000,-18000);
	public static MapLocation mapMax = new MapLocation(18000,18000);
	
	public static boolean mapMinUpdated = false;
	public static boolean mapMaxUpdated = false;

	// the following functions are to be called mainly by Archons to set destinations
	public static MapLocation getExplorationWaypoint()
	{
		return null;
	}
	
	public static MapLocation getClosestPart()
	{
		return null;
	}
	
	public static MapLocation getClosestDen()
	{
		return null;
	}
	
	public static MapLocation getClosestPartOrDen()
	{
		return null;
	}
	
	// these are to be called by scouts or by Message,
	// using a new point that is known to be off the map
	public static void updateMapEdges(MapLocation loc)
	{
		if (loc.x > mapMin.x || loc.y > mapMin.y)
		{
			mapMin = new MapLocation(
					Math.max(loc.x,mapMin.x),
					Math.max(loc.y,mapMin.y));
			mapMinUpdated = true;
		}
		if (loc.x < mapMax.x || loc.y < mapMax.y)
		{
			mapMax = new MapLocation(
					Math.min(loc.x,mapMax.x),
					Math.min(loc.y,mapMax.y));
			mapMaxUpdated = true;
		}
	}
	
	// distance required to cover the full map, for scout transmission
	public static int fullMapDistanceSq()
	{
		int maxDx = Math.abs(mapMin.x-here.x);
		int maxDy = Math.abs(mapMin.y-here.y);
		maxDx = Math.max(maxDx, Math.abs(mapMax.x-here.x));
		maxDy = Math.max(maxDy, Math.abs(mapMax.y-here.y));
		
		int maxDistSq = maxDx*maxDx + maxDy*maxDy;
		if (maxDistSq > Message.FULL_MAP_DIST_SQ)
			maxDistSq = Message.FULL_MAP_DIST_SQ;
		
		return maxDistSq;
	}
	
	// function to send updated info as a scout
	public static boolean tryScoutSendUpdates()
	{
		return false;
	}
}
