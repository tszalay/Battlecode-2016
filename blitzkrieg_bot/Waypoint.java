package blitzkrieg_bot;

import battlecode.common.*;

import java.util.*;

public class Waypoint extends RobotPlayer
{
	// subclass for target round info
	public static class TargetInfo
	{
		public int 			round;
		public int 			value;
		public MapLocation 	location;
		
		public TargetInfo(MapLocation loc, int val)
		{
			round = rc.getRoundNum();
			value = val;
			location = loc;
		}
	}
	
	public static class TargetStore
	{
		public TargetInfo[] targets = new TargetInfo[5];
		private final int timeout;
		
		public TargetStore(int time)
		{
			timeout = time;
		}
		
		public void add(TargetInfo ti)
		{
			// invalid values for stuff (0 for zombies)
			if (ti.location == null || ti.value == 0)
				return;
			
			int oldestInd = 0;
			int lowestInd = 0;
			
			for (int i=0; i<targets.length; i++)
			{
				if (targets[i] == null)
				{
					targets[i] = ti;
					return;
				}
				if (targets[i].round < targets[oldestInd].round)
					oldestInd = i;
				if (targets[i].value < targets[lowestInd].value)
					lowestInd = i;
			}
			// replace a "dead" one first, if we can
			if (roundsSince(oldestInd) > timeout)
			{
				targets[oldestInd] = ti;
				return;
			}
			// otherwise the one with the lowest value
			if (ti.value > targets[lowestInd].value)
			{
				targets[lowestInd] = ti;
				return;
			}
		}
		
		public int size()
		{
			int sz = 0;
			for (int i=0; i<targets.length; i++)
				if (targets[i] != null && roundsSince(targets[i].round) < timeout)
					sz++;
			return sz;
		}

		public MapLocation getClosestRecent()
		{
			return getClosestRecent(timeout);
		}
		
		public MapLocation getClosestRecent(int timelimit)
		{
			TargetInfo ti = null;
			
			for (int i=0; i<targets.length; i++)
			{
				if (targets[i] == null)
					continue;
				
				if (roundsSince(targets[i].round)<timelimit &&
						(ti == null || here.distanceSquaredTo(targets[i].location) < here.distanceSquaredTo(ti.location)))
					ti = targets[i];
			}
			
			if (ti == null)
				return null;
			
			// if the closest has a value of 1, try to find a better one
			// (so we don't chase scouts everywheres)
			if (ti.value == 1)
			{
				for (int i=0; i<targets.length; i++)
				{
					if (targets[i] == null)
						continue;
					if (targets[i].value > 1 && roundsSince(targets[i].round)<timelimit &&
							here.distanceSquaredTo(targets[i].location) < here.distanceSquaredTo(ti.location))
						ti = targets[i];
				}
			}
			
			return ti.location;
		}
	}
	
	public static TargetStore enemyTargetStore = new TargetStore(200);
	public static TargetStore friendlyTargetStore = new TargetStore(200);
	
	private static MapLocation randomDest = null;
	private static int randomDestRound = -10000;
	
	public static MapLocation getRandomRetreatWaypoint()
	{
		// we haven't changed it in a while, or we're there
		if (roundsSince(randomDestRound) > 300 || here.distanceSquaredTo(randomDest) < 24)
		{
			randomDest = MapInfo.getRandomLocation();
			// make sure it's not too close to a corner...
			while (MapInfo.closestCornerDistanceSq(randomDest) < 81)
				randomDest = MapInfo.getRandomLocation();
		}
		
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
		return friendlyTargetStore.getClosestRecent();
	}
	
	public static MapLocation getBestEnemyLocation()
	{
		return enemyTargetStore.getClosestRecent();
	}
}
