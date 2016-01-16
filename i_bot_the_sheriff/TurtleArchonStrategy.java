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
			// here is where should give an override strategy if shit be going down
			if (Micro.getRoundsUntilDanger() < 10)
			{
				Action.tryRetreatTowards(turtleLocation, Micro.getSafeMoveDirs());
			}
			else
			{
				Nav.tryGoTo(turtleLocation, Micro.getSafeMoveDirs());
			}
			return true;
		}
		
		// are we in any danger?
		if (Micro.getRoundsUntilDanger() < 10)
		{
			Action.tryRetreatTowards(Micro.getAllyCOM(), Micro.getSafeMoveDirs());
			return true;
		}
		
		if ()
		
			
		return true;
	}
	
	private boolean tryBuild() throws GameActionException
	{
		if (!rc.isCoreReady())
			return false;
		
		if (rc.getTeamParts() < 1.5*RobotType.TURRET.partCost)
			return false;
		
		// figure out what robot to try and build
		UnitCounts units = new UnitCounts(Micro.getNearbyAllies());
		
		RobotType robotToBuild = null;
		
		if (units.Scouts < 2 || units.Scouts < units.Turrets/4)
		{
			robotToBuild = RobotType.SCOUT;
		}
		else if (units.Turrets < 8)
		{
			robotToBuild = RobotType.TURRET;
		}
		else
		{
			return false;
		}
		
		if (!rc.hasBuildRequirements(robotToBuild))
			return false;

		Direction buildDir = getCanBuildDirectionSet(robotToBuild).getRandomValid();
		if (buildDir != null)
		{
			overrideStrategy = new BuildingStrategy(robotToBuild, buildDir);
			return true;
		}
		
		return false;
	}
	
	public static DirectionSet getCanBuildDirectionSet(RobotType nextRobotType) throws GameActionException
	{
		// check nearby open squares
		DirectionSet valid = new DirectionSet();
		for (Direction dir : Direction.values()) // check all Directions around
		{
			if (rc.canBuild(dir, nextRobotType))
				valid.add(dir); // add this direction to the DirectionSet of valid build directions
		}
		return valid;
	}

}
