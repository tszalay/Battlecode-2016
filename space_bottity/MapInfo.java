package space_bottity;

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
	
	
	public static MapLocation getClosestDenThenPart()
	{
		MapLocation ml = Micro.getClosestLocationTo(zombieDenLocations.elements(), here);
		if (ml == null)
			ml = Micro.getClosestLocationTo(goodPartsLocations.elements(), here);
		return ml;
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
	
	public static void updateLocalWaypoints() throws GameActionException
	{
		MapLocation loc = getClosestPart();
		if (loc != null && rc.senseParts(loc) <= 0)
			goodPartsLocations.remove(loc);
		
		loc = getClosestDen();
		if (loc != null && rc.canSense(loc))
		{
			RobotInfo ri = rc.senseRobotAtLocation(loc);
			if (ri == null || ri.type != RobotType.ZOMBIEDEN)
				zombieDenLocations.remove(loc);
		}
	}
	
	// function to send updated info as a scout
	public static boolean doScoutSendUpdates() throws GameActionException
	{
		if (Micro.isInDanger() || rc.getCoreDelay() > 5)
			return false;
		
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
	
	// function to look for nearby parts, neutrals, etc, can be used by scouts or archons
	public static void analyzeSurroundings() throws GameActionException
	{
		double visibleParts = 0;
		
		boolean isScout = (rc.getType() == RobotType.SCOUT);
		
		// neutral robot check
		RobotInfo[] neutralRobots = rc.senseNearbyRobots(rc.getType().sensorRadiusSquared, Team.NEUTRAL);
		for (RobotInfo ri : neutralRobots)
		{
			updateParts(ri.location, isScout);
			visibleParts += ri.type.partCost;
		}
		
		// zombie den check
		for (RobotInfo ri : Micro.getNearbyHostiles())
			if (ri.type == RobotType.ZOMBIEDEN)
				updateZombieDens(ri.location, isScout);
		
		// monkeypatch for now
		here = rc.getLocation();
		
		// loop through and see if we need to update anyone
		Direction d = Direction.NORTH;
		for (int j=0; j<4; j++)
		{
			int newval = 0;
			// loop until we hit a location on the map
			int i = isScout ? 7 : 5;
			for (; i>=1; i--)
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
		
		MapLocation[] partsLocs = rc.sensePartLocations(rc.getType().sensorRadiusSquared);
		
		for (MapLocation loc : partsLocs)
		{
			updateParts(loc, isScout);
			visibleParts += rc.senseParts(loc);
		}
		//System.out.println("Scanned " + nchecked + "/" + MapUtil.allOffsX.length + " locations");
	}
	
	public static void calculateSymmetry()
	{
		// create fastlocset without internal list
		FastLocSet archonLocs = new FastLocSet(false);
		
		
	}
}
