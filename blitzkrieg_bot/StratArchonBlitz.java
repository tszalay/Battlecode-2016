package blitzkrieg_bot;

import battlecode.common.*;

import java.util.*;

public class StratArchonBlitz extends RoboArchon implements Strategy
{
	private Strategy overrideStrategy = null;

	private MapLocation dest = null;
	
	private String stratName;

	public String getName()
	{
		if (overrideStrategy != null)
			return overrideStrategy.getName();

		return "Blitzing";
	}

	public StratArchonBlitz(MapLocation dest) throws GameActionException
	{
		this.dest = dest;
		this.stratName = getName();
	}

	public boolean tryTurn() throws GameActionException
	{
		Debug.setStringAK("My Strategy: " + this.stratName);

		// check for enemies or zombies, and retreat if necessary
		if (Micro.getRoundsUntilDanger() < 5)
			overrideStrategy = new StratArchonGTFO();
		
		// do we have a strategy that takes precedence over this one?
		if (overrideStrategy != null)
		{
			if (overrideStrategy.tryTurn())
				return true;
			else
				overrideStrategy = null;
		}
		
		// go to the target desitnation as fast as possible
		if (dest == null)
		{
			dest = MapInfo.getClosestNeutralArchon();
			if (dest == null)
				dest = senseClosestNeutral();
			if (dest == null)
				return false;
		}
		else // we have a priority destination, a neutral archon, go fast
		{
			if (here.distanceSquaredTo(dest) == 1)
			{
				dest = null;
				return false;
			}
		}
		
		doCallSoldierBackup(); // from RoboArchon.java
		if (Micro.getFriendlyUnits().Soldiers < 3)
			return false;
			
		Action.tryGoToSafestOrRetreat(dest);
		return true;
	}

	public MapLocation senseClosestNeutral() throws GameActionException
	{
		RobotInfo[] neutrals = rc.senseNearbyRobots(rc.getType().sensorRadiusSquared,Team.NEUTRAL);

		if (neutrals == null || neutrals.length == 0)
			return null;

		MapLocation closestNeutralLoc = null;
		for (RobotInfo ri : neutrals)
		{
			if (closestNeutralLoc == null || ri.type == RobotType.ARCHON || (ri.type != RobotType.ARCHON && here.distanceSquaredTo(ri.location) < here.distanceSquaredTo(closestNeutralLoc)))
				closestNeutralLoc = ri.location;
		}

		return closestNeutralLoc; // will return an archon location even if it's not really the closest
	}
}
