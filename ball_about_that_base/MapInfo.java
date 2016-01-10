package ball_about_that_base;

import battlecode.common.*;
import java.util.*;

public class MapInfo extends RobotPlayer
{
	public static FastLocSet zombieDenLocations = new FastLocSet();
	public static FastLocSet goodPartsLocations = new FastLocSet();
	//public static FastLocSet exploredMapWaypoints = new FastLocSet();
	
	public static MapLocation mapMin = new MapLocation(-18000,-18000);
	public static MapLocation mapMax = new MapLocation(18000,18000);
	
	public static boolean mapMinUpdated = false;
	public static boolean mapMaxUpdated = false;

	// the following functions are to be called mainly by Archons to set destinations
	public static MapLocation getExplorationWaypoint()
	{
		// this one should get transmitted to a scout
		return new MapLocation(rand.nextInt(mapMax.x - mapMin.x) + mapMax.x,
							   rand.nextInt(mapMax.y - mapMin.y) + mapMax.y);
	}
	
	public static MapLocation getClosestPart()
	{
		return Micro.getClosestLocationTo(goodPartsLocations.elements(), here);
	}
	
	public static MapLocation getClosestDen()
	{
		return Micro.getClosestLocationTo(zombieDenLocations.elements(), here);
	}
	
	public static MapLocation getClosestPartOrDen()
	{
		MapLocation partloc = getClosestPart();
		MapLocation denloc = getClosestDen();
		
		if (partloc == null)
			return denloc;
		
		if (denloc == null || here.distanceSquaredTo(partloc) < here.distanceSquaredTo(denloc))
			return partloc;
		
		return denloc;
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
	public static boolean tryScoutSendUpdates() throws GameActionException
	{
		if (Micro.isInDanger())
			return false;
		
		// only send one at a time
		if (mapMinUpdated)
		{
			Message.sendMessageSignal(fullMapDistanceSq(), MessageType.MAP_MIN, mapMin);
			mapMinUpdated = false;
			return true;
		}
		if (mapMaxUpdated)
		{
			Message.sendMessageSignal(fullMapDistanceSq(), MessageType.MAP_MAX, mapMax);
			mapMaxUpdated = false;
			return true;
		}
		
		return false;
	}
}
