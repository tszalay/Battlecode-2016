package i_bot_the_sheriff;

import battlecode.common.*;

import java.util.*;

// turtle move tries to allow units to move around close to or inside a turtle ball
public class TurtleArchonStrategy extends RobotPlayer implements Strategy
{
	private MapLocation turtleLocation = null;
	private Strategy overrideStrategy = null;
	
	public TurtleArchonStrategy()
	{
		// find a good turtle location
		// for now, pick a random corner of the map
		turtleLocation = new MapLocation(
						rand.nextBoolean() ? MapInfo.mapMin.x : MapInfo.mapMax.x,
						rand.nextBoolean() ? MapInfo.mapMin.y : MapInfo.mapMax.y
					);
	}
	
	public boolean tryTurn() throws GameActionException
	{
		// do we have a strategy that takes precedence over this one?
		// (e.g. combat or building)
		if (overrideStrategy != null)
		{
			if (overrideStrategy.tryTurn())
				return true;
			else
				overrideStrategy = null;
		}
		
		// if we're far from the closest corner, let's go there
		if (MapInfo.closestCornerDistanceSq() > 53)
		{
			Nav.tryGoTo(turtleLocation, Micro.getSafeMoveDirs());
			return true;
		}
		
		// are we in any danger?
		if (Micro.getRoundsUntilDanger() < 10)
		{
			Action.tryRetreatTowards(Micro.getAllyCOM(), Micro.getSafeMoveDirs());
			return true;
		}
		
		// count number of units nearby to decide what to build
		
		
			
		return true;
	}
}
