package ball_about_that_base;

import battlecode.common.*;

import java.util.*;

public class MapInfo extends RobotPlayer
{
	public static FastLocSet zombieDenLocations = new FastLocSet();
	public static FastLocSet goodPartsLocations = new FastLocSet();
	
	public static MapLocation mapMin = new MapLocation(-18000,-18000);
	public static MapLocation mapMax = new MapLocation(18001,18001);
	
	// update the boundary on one of the map min/maxes
	public static Direction newMapDir = null;
	public static int newMapVal;
	
	public static MapLocation newZombieDen = null;
	public static MapLocation newParts = null;

	// the following functions are to be called mainly by Archons to set destinations
	
	// just do something random for now
	public static MapLocation getExplorationWaypoint()
	{
		// this one should get transmitted to a scout
		return new MapLocation(rand.nextInt(mapMax.x - mapMin.x) + mapMin.x,
							   rand.nextInt(mapMax.y - mapMin.y) + mapMin.y);
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
	
	public static boolean isOnMap(MapLocation loc)
	{
		return (loc.x > mapMin.x) && (loc.y > mapMin.y) &&
				(loc.x < mapMax.x) && (loc.y < mapMax.y);
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
	
	// these are to be called by scouts or by Message,
	// to update the given edge
	public static void updateMapEdge(Direction dir, int val, boolean sendUpdate)
	{
		boolean newval = false;
		
		// update the correct direction
		switch (dir)
		{
		case NORTH:
			if (val > mapMin.y)
			{
				mapMin = new MapLocation(mapMin.x,val);
				if (sendUpdate)
					newval = true;
			}
			break;
		case EAST:
			if (val < mapMax.x)
			{
				mapMax = new MapLocation(val,mapMax.y);
				if (sendUpdate)
					newval = true;
			}
			break;
		case WEST:
			if (val > mapMin.x)
			{
				mapMin = new MapLocation(val,mapMin.y);
				if (sendUpdate)
					newval = true;
			}
			break;
		case SOUTH:
			if (val < mapMax.y)
			{
				mapMax = new MapLocation(mapMax.x,val);
				if (sendUpdate)
					newval = true;
			}
			break;
		default:
			System.out.print("Invalid direction to updateMapEdge");
			break;
		}
		
		// and post it to be sent
		if (newval)
		{
			newMapDir = dir;
			newMapVal = val;
		}		
	}

	
	// these are to be called by scouts/message to add a zombie den/part if it isn't in there already
	public static void updateZombieDens(MapLocation loc, boolean sendUpdate)
	{
		// if we already reported it, or we already have one queued to send,
		// don't do anything
		if (zombieDenLocations.contains(loc) || newZombieDen != null)
			return;
		
		zombieDenLocations.add(loc);
		
		// flag that we want to send this location
		if (sendUpdate)
			newZombieDen = loc;
	}
	
	public static void updateParts(MapLocation loc, boolean sendUpdate)
	{
		if (goodPartsLocations.contains(loc) || newParts != null)
			return;
		
		goodPartsLocations.add(loc);
		
		if (sendUpdate)
			newParts = loc;
	}
	
	public static void removeWaypoint(MapLocation loc)
	{
		// remove the part, and send that it got removed
		goodPartsLocations.remove(loc);
		zombieDenLocations.remove(loc);
	}
	
	// function to send updated info as a scout
	public static boolean doScoutSendUpdates() throws GameActionException
	{
		// only send one at a time
		if (newMapDir != null)
		{
			Message.sendMessageSignal(fullMapDistanceSq(), MessageType.MAP_EDGE,
					new MapLocation(newMapDir.ordinal(), newMapVal));
			newMapDir = null;
			return true;
		}
		if (newZombieDen != null)
		{
			Message.sendMessageSignal(fullMapDistanceSq(), MessageType.ZOMBIE_DEN, newZombieDen);
			newZombieDen = null;
			return true;
		}
		if (newParts != null)
		{
			Message.sendMessageSignal(fullMapDistanceSq(), MessageType.GOOD_PARTS, newParts);
			newParts = null;
			return true;
		}
		
		return false;
	}
	
	// function to look for nearby parts, neutrals, etc
	private static int offsetInd = 0;
	
	public static void scoutAnalyzeSurroundings() throws GameActionException
	{
		double visibleparts = 0;
		
		// neutral robot check
		RobotInfo[] neutralRobots = rc.senseNearbyRobots(rc.getType().sensorRadiusSquared, Team.NEUTRAL);
		for (RobotInfo ri : neutralRobots)
		{
			updateParts(ri.location, true);
			visibleparts += ri.type.partCost;
		}
		
		// zombie den check
		for (RobotInfo ri : Micro.getNearbyHostiles())
			if (ri.type == RobotType.ZOMBIEDEN)
				updateZombieDens(ri.location, true);
		
		// monkeypatch for now
		here = rc.getLocation();
		
		// loop through and see if we need to update anyone
		Direction d = Direction.NORTH;
		for (int j=0; j<4; j++)
		{
			int newval = 0;
			// loop until we hit a location on the map
			for (int i=7; i>=1; i--)
			{
				MapLocation loc = here.add(d,i);
				if (rc.onTheMap(loc))
					break;
				// even means y, odd means x
				newval = ((j&1)==0)	? loc.y : loc.x;
			}
			if (newval > 0)
			{
				updateMapEdge(d, newval, true);
				break;
			}
			
			d = d.rotateRight().rotateRight();
		}
		
		int nchecked = 0;
		
		while (Clock.getBytecodesLeft() > 8000)
		{
			MapLocation loc = here.add(MapUtil.allOffsX[offsetInd], MapUtil.allOffsY[offsetInd]);
			offsetInd = (offsetInd+1) % MapUtil.allOffsX.length;
			
			// check for parts first
			double nparts = rc.senseParts(loc);
			if (nparts > 50)
				updateParts(loc, true);
			visibleparts += nparts;
			
			nchecked++;
			if (nchecked == MapUtil.allOffsX.length)
				break;
		}
		//System.out.println("Scanned " + nchecked + "/" + MapUtil.allOffsX.length + " locations");
	}
}