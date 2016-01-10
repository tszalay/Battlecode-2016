package ball_is_life;

import battlecode.common.*;
import java.util.*;

public class MapInfo extends RobotPlayer
{
	public static FastLocSet zombieDenLocations = new FastLocSet();
	public static FastLocSet goodPartsLocations = new FastLocSet();
	public static FastLocSet exploredMapWaypoints = new FastLocSet();
	
	public static int mapMinX = 0;
	public static int mapMinY = 0;
	public static int mapMaxX = 0;
	public static int mapMaxY = 0;

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
}
