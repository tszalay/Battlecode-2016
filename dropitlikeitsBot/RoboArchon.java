package dropitlikeitsBot;

import java.util.ArrayList;

import battlecode.common.*;

public class RoboArchon extends RobotPlayer
{
	enum ArchonState
	{
		RALLYING,
		BUILDING,
		WAYPOINT
	}

	static final int MAX_ROUNDS_TO_RALLY = 400;

	static ArchonState myState = ArchonState.BUILDING; // AK for testing always build
	static RobotType myNextBuildRobotType = RobotType.SCOUT;
	static int lastBuiltRound = 0;

	public static void init() throws GameActionException
	{
	}
	
	public static void turn() throws GameActionException
	{
		// state machine update
		updateState();
		Debug.setStringSJF(myState.toString());
		
		MapInfo.removeWaypoint(here);
		
		// always try this if we can, before moving
		tryActivateNeutrals();
		
		// do turn according to state
		switch (myState)
		{
		case RALLYING:
			doRally();
			break;
		case BUILDING:
			doBuild();
			break;
		case WAYPOINT:
			doWaypoint();
			break;
		}
	
		// always do this, no reason not to
		tryRepair();

		// and this - look for & update nearby cool stuff
		MapInfo.analyzeSurroundings();
		
		Debug.setStringTS("D:" + MapInfo.zombieDenLocations.elements().size()
				+ ",P:" + MapInfo.goodPartsLocations.elements().size());
	}
	
	private static void updateState() throws GameActionException // this will probably need tweaking
	{
		switch (myState)
		{
		case BUILDING:
			// keep going to the next location if we're done building
			// (and send startup built messages)
			if (rc.isCoreReady())
			{
				// myState = ArchonState.WAYPOINT; AK for testing always build
				Message.sendBuiltMessage();
				// also set the next robot type y knot
				myNextBuildRobotType = getNextBuildRobotType(); 
			
				//myNextBuildRobotType = RobotType.SCOUT;
				lastBuiltRound = rc.getRoundNum();
			}
			break;
			
		case RALLYING:
			if (arrivedAtRally() || rc.getRoundNum() > MAX_ROUNDS_TO_RALLY)
				myState = ArchonState.WAYPOINT;			
			break;
		
		case WAYPOINT:
			if (canBuildNow())
				myState = ArchonState.BUILDING;
			break;			
		}
	}
	
	public static void doWaypoint() throws GameActionException
	{
		// first priority, avoid stuff
		if (Micro.isInDanger())
		{
			Behavior.tryRetreatOrShootIfStuck();
			return;
		}
		
		// next priority, any of nearby units in trouble?
		RobotInfo[] nearby = rc.senseNearbyRobots(2, ourTeam);
		for (RobotInfo ri : nearby)
		{
			// aka if there are any too close, retreat
			if (ri.type != RobotType.SCOUT)
			{
				if (Behavior.tryAdjacentSafeMoveToward(ri.location.directionTo(here)))
					return;
			}
		}
		
		// look for waypoint
		MapLocation dest = MapInfo.getClosestDen();
		
		if (dest != null && here.distanceSquaredTo(dest) < 15)
			return;
		
		// check if it should be deleted
		if (dest != null && rc.canSenseLocation(dest) && rc.senseParts(dest) == 0 && rc.senseRobotAtLocation(dest) == null)
		{
			MapInfo.removeWaypoint(dest);
			dest = MapInfo.getClosestPartOrDen();
		}
		
		// if we don't have a waypoint, explore
		if (dest == null)
			dest = MapInfo.getExplorationWaypoint();

		if (dest != null)
		{
			// go where we should
			Behavior.tryGoToWithoutBeingShot(dest, Micro.getSafeMoveDirs());
			// send a bit of a "ping"
			if (rc.getRoundNum() % 7 == 0)
				Message.sendSignal(63);
		}
	}
	
	public static void doRally() throws GameActionException
	{
		// update rally location
		if (rc.getRoundNum() == RoboScout.SIGNAL_ROUND)
			Message.calcRallyLocation();
		
		// rally
		if (Message.rallyLocation == null)
			return;
		
		MapLocation closestPart = MapInfo.getClosestPart();
		if (closestPart != null && closestPart.distanceSquaredTo(here) < rc.getType().sensorRadiusSquared)
			Behavior.tryGoToWithoutBeingShot(closestPart, Micro.getSafeMoveDirs());
		else
			Behavior.tryGoToWithoutBeingShot(Message.rallyLocation, Micro.getSafeMoveDirs());
	}
	
	private static void doBuild() throws GameActionException
	{
		RobotType nextRobotType = myNextBuildRobotType;
		
		// return false quickly if we cannot build for an obvious reason
		if (nextRobotType == null)
			return;
		if (!rc.isCoreReady())
			return;
		
		tryBuild(nextRobotType);
		return;
	}
	
	public static boolean tryActivateNeutrals() throws GameActionException
	{
		if (!rc.isCoreReady())
			return false;
		
		// check adjacent squares for neutrals
		RobotInfo[] adjNeutrals = rc.senseNearbyRobots(2, Team.NEUTRAL);
		
		if (adjNeutrals.length > 0)
		{
			// activate just the first one
			rc.activate(adjNeutrals[0].location);
			// remove it from parts list
			MapInfo.removeWaypoint(adjNeutrals[0].location);
			return true;
		}
		
		return false;
	}
	
	public static boolean tryRepair() throws GameActionException
	{
		RobotInfo[] nearbyFriends = rc.senseNearbyRobots(rc.getType().attackRadiusSquared,ourTeam);
		RobotInfo minBot = null;

		for (RobotInfo ri : nearbyFriends)
		{
			if (ri.type == RobotType.ARCHON)
				continue;

			if (minBot == null || (ri.maxHealth - ri.health) > (minBot.maxHealth - minBot.health) || minBot.zombieInfectedTurns < ri.zombieInfectedTurns)
				minBot = ri;
		}

		if (minBot != null && minBot.health < minBot.maxHealth-1)
		{
			rc.repair(minBot.location);
			return true;
		}
		return false;
	}

	public static boolean tryBuild(RobotType robotToBuild) throws GameActionException
	{
		if (!rc.isCoreReady())
			return false;
		if (!rc.hasBuildRequirements(robotToBuild))
			return false;

		Direction buildDir = getCanBuildDirectionSet(robotToBuild).getRandomValid();
		if (buildDir != null)
		{
			rc.build(buildDir, robotToBuild);
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
	
	public static DirectionSet getParityAllowedDirectionSet(RobotType nextRobotType) throws GameActionException
	{
		DirectionSet allowedParity = null;
		if (nextRobotType == RobotType.TURRET)
		{
			// build turrets only on odd squares
			allowedParity = DirectionSet.getOddSquares(here);
		}
		else
		{
			// build all else only on even squares
			allowedParity = DirectionSet.getEvenSquares(here);
		}
		return allowedParity;
	}
	
	static final int SOLDIER = RobotType.SOLDIER.ordinal();
	static final int GUARD = RobotType.GUARD.ordinal();
	static final int TURRET = RobotType.TURRET.ordinal();
	static final int SCOUT = RobotType.SCOUT.ordinal();
	
	private static RobotType getNextBuildRobotType() throws GameActionException
	{
		// what's next to build: the old thing is not working?
		RobotInfo[] nearby = Micro.getNearbyAllies();
		int[] nearbyUnits = new int[RobotType.values().length];
		for (RobotInfo ri : nearby)
			nearbyUnits[ri.type.ordinal()]++;
		
		int combatUnits = nearbyUnits[SOLDIER]
						  + nearbyUnits[GUARD]
						  + nearbyUnits[TURRET];
		
		if (nearbyUnits[SCOUT] < 2 || nearbyUnits[SCOUT] < combatUnits/4)
			return RobotType.SCOUT;
		
		if (nearbyUnits[SOLDIER] < 10)
			return RobotType.SOLDIER;
		//if (nearbyUnits[TURRET] < 5)
		//	return RobotType.TURRET;
		
		return RobotType.GUARD;
	}
	
	private static boolean canBuildNow() throws GameActionException
	{
		RobotType nextRobotType = myNextBuildRobotType;
		// return false quickly if we cannot build for an obvious reason
		if (nextRobotType == null)
			return false;
		if (!rc.isCoreReady())
			return false;
		if (!rc.hasBuildRequirements(nextRobotType))
			return false;
		if (Micro.isInDanger())
			return false;
		
		// find okay direction set
		DirectionSet canBuild = getCanBuildDirectionSet(nextRobotType);
		DirectionSet parity = getParityAllowedDirectionSet(nextRobotType);
		DirectionSet validBuildDirectionSet = canBuild.and(parity);
		
		// if we have a valid direction return true
		return (validBuildDirectionSet.any());
	}
	
	private static boolean arrivedAtRally()
	{
		if (Message.rallyLocation == null)
			return false;
		
		return here.distanceSquaredTo(Message.rallyLocation) < 20;
	}
}
