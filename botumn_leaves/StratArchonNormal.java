package botumn_leaves;

import battlecode.common.*;

import java.util.*;

public class StratArchonNormal extends RoboArchon implements Strategy
{
	private Strategy overrideStrategy = null;

	static int lastBuiltRound = 0;
	
	public String getName()
	{
		if (overrideStrategy != null)
			return overrideStrategy.getName();

		return "Normal Archon";
	}
	
	public boolean tryTurn() throws GameActionException
	{
		// do we have a strategy that takes precedence over this one?
		if (overrideStrategy != null)
		{
			if (overrideStrategy.tryTurn())
				return true;
			else
				overrideStrategy = null;
		}
		
		// first priority, avoid stuff
		if (Micro.getRoundsUntilDanger() < 5 && (rc.getRoundNum()%5) == 0)
		{
			Message.sendMessageSignal(400,MessageType.UNDER_ATTACK,0);
			Action.tryRetreatOrShootIfStuck();
			return true;
		}
		if (Micro.getRoundsUntilDanger() < 20)
		{
			Message.sendSignal(120);
			MapLocation retreatloc = MapInfo.getClosestDen();
			if (retreatloc == null)
				Action.tryRetreatOrShootIfStuck();
			else
				Action.tryGoToWithoutBeingShot(retreatloc, Micro.getSafeMoveDirs());
			return true;
		}

		// next, try doing some building
		if (tryBuild())
			return true;

		// look for waypoint
		MapLocation dest = senseClosestNeutral();
		if (dest == null)
			dest = senseClosestPart();
		if (dest == null)
			dest = MapInfo.getClosestDen();
		
		
		// check if it should be deleted
		if (dest != null && rc.canSenseLocation(dest) && rc.senseParts(dest) == 0 && rc.senseRobotAtLocation(dest) == null)
		{
			dest = MapInfo.getClosestPartOrDen();
		}
		
		if (dest != null)
		{
			// go where we should
			Action.tryGoToWithoutBeingShot(dest, Micro.getSafeMoveDirs());
			// send a bit of a "ping"
			if (rc.getRoundNum() % 7 == 0)
				Message.sendSignal(63);
		}
		
		return true;
	}

	private boolean tryBuild() throws GameActionException
	{
		if (!rc.isCoreReady())
			return false;
		
		if (rc.getTeamParts() < 1*RobotType.TURRET.partCost)
			return false;
		
		// AK wait a few rounds so we can move
		if ((rc.getRoundNum()+rc.getID())%3 != 0)
			return false;
		
		// figure out what robot to try and build
		//UnitCounts units = new UnitCounts(Micro.getNearbyAllies());
		
		RobotType robotToBuild = null;
		Strategy.Type buildStrat = null;
		
		// need to build a shadow scout, top priority
		if (roundsSince(RoboArchon.lastAdjacentScoutRound) > 20)
		{
			robotToBuild = RobotType.SCOUT;
			buildStrat = Strategy.Type.SHADOW_ARCHON;
		}
		else if (rand.nextInt() % 8 < 6)
		{
			robotToBuild = RobotType.SOLDIER;
			buildStrat = Strategy.Type.MOB_MOVE;
		}
		else if (rand.nextBoolean())
		{
			robotToBuild = RobotType.SCOUT;
			buildStrat = Strategy.Type.SHADOW_SOLDIER;
		}
		else
		{
			robotToBuild = RobotType.SCOUT;
			buildStrat = Strategy.Type.EXPLORE;
		}

		if (!rc.hasBuildRequirements(robotToBuild))
			return false;

		Direction buildDir = Micro.getCanBuildDirectionSet(robotToBuild).getRandomValid();
		if (buildDir != null)
		{
			overrideStrategy = new StratBuilding(robotToBuild, buildDir, buildStrat);
			return true;
		}
		
		return false;
	}
	
	public static MapLocation senseClosestPart() throws GameActionException
	{
		MapLocation[] parts = rc.sensePartLocations(rc.getType().sensorRadiusSquared);
		
		if (parts == null || parts.length == 0)
			return null;
		
		MapLocation closestPartLoc = null;
		for (MapLocation loc : parts)
		{
			// don't try to go to a part we can't get
			if ( (closestPartLoc == null && rc.senseRobotAtLocation(loc) == null && rc.senseRubble(loc) < GameConstants.RUBBLE_OBSTRUCTION_THRESH) || (closestPartLoc != null && here.distanceSquaredTo(loc) < here.distanceSquaredTo(closestPartLoc) && rc.senseRobotAtLocation(loc) == null && rc.senseRubble(loc) < GameConstants.RUBBLE_OBSTRUCTION_THRESH))
				closestPartLoc = loc;
		}
		
		return closestPartLoc;
	}
	
	public static MapLocation senseClosestNeutral() throws GameActionException
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
