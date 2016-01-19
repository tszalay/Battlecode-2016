package botumn_leaves;

import battlecode.common.*;

import java.util.*;

public class ArchonNormalStrat extends RobotPlayer implements Strategy
{
	static RobotType myNextBuildRobotType = RobotType.SCOUT;
	
	static int lastBuiltRound = 0;
	
	public String getName()
	{
		return "Normal Archon";
	}
	
	public boolean tryTurn() throws GameActionException
	{
		myNextBuildRobotType = getNextBuildRobotType();
		if (canBuildNow() && (rc.getRoundNum()+rc.getID())%3 == 0) //AK give them some time to move
		{
			doBuild();
		}
		else
			doWaypoint();
		
		return true;
	}
	
	public static void doWaypoint() throws GameActionException
	{
		// first priority, avoid stuff
		if (Micro.isInDanger())
		{
			Action.tryRetreatOrShootIfStuck();
			return;
		}
		
		// look for waypoint
		MapLocation dest = senseClosestNeutral();
		if (dest == null)
			dest = senseClosestPart();
		if (dest == null)
			dest = MapInfo.getClosestDen();
		
		
//		if (dest != null && here.distanceSquaredTo(dest) < 15)
//			return;
		
		// check if it should be deleted
		if (dest != null && rc.canSenseLocation(dest) && rc.senseParts(dest) == 0 && rc.senseRobotAtLocation(dest) == null)
		{
			MapInfo.removeWaypoint(dest);
			dest = MapInfo.getClosestPartOrDen();
		}
		
		if (dest != null)
		{
			// put indicator at waypoint
			rc.setIndicatorDot(dest, rc.getID()%255, 255, 255);
			Debug.setStringAK("Waypoint = " + dest);
			// go where we should
			
			Action.tryGoToWithoutBeingShot(dest, Micro.getSafeMoveDirs());
			// send a bit of a "ping"
			if (rc.getRoundNum() % 7 == 0)
				Message.sendSignal(63);
			
		}
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
		
		return RobotType.SOLDIER;
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
