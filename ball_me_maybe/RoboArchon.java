package ball_me_maybe;

import java.util.ArrayList;

import battlecode.common.*;

public class RoboArchon extends RobotPlayer
{
	enum ArchonState
	{
		INIT,
		RALLYING,
		BUILDING,
		WAYPOINT
	}

	static int robotSchedule[] = {10, 10, 10, 6}; // 10-turret 6 - scout
	static int scheduleCounter = rand.nextInt(100);
	
	static final int MAX_ROUNDS_TO_RALLY = 200;
	static final int DEST_MESSAGE_FREQ = 10;
	static final int DEST_MESSAGE_RANGE = 63;
	
	static ArchonState myState = ArchonState.INIT;

	public static void init() throws GameActionException
	{
	}
	
	public static void turn() throws GameActionException
	{
		if (!Behavior.tryStayFarFromHostiles())
		{
			// state machine update
			updateState();
			Debug.setStringSJF(myState.toString());
			
			// always try this if we can, before moving
			tryActivateNeutrals();
			
			// do turn according to state
			switch (myState)
			{
			case INIT:
				doInit();
				break;
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
			
			Debug.setStringTS("D:" + MapInfo.zombieDenLocations.elements().size()
					+ ",P:" + MapInfo.goodPartsLocations.elements().size());
		}
	}
	
	private static void updateState() throws GameActionException // this will probably need tweaking
	{
		switch (myState)
		{
		case INIT:
			break;
			
		case BUILDING:
			// keep going to the next location if we're done building
			// (and send startup built messages)
			if (rc.isCoreReady())
			{
				myState = ArchonState.WAYPOINT;
				Message.sendBuiltMessage();
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
	
	public static void doInit() throws GameActionException
	{
		tryBuildEven(RobotType.SCOUT);
		myState = ArchonState.RALLYING;
	}
	
	public static void doWaypoint() throws GameActionException
	{
		// look for waypoint
		MapLocation loc = MapInfo.getClosestPartOrDen();
		
		// check to see if we should remove a waypoint
		if (here.equals(loc))
			MapInfo.removeWaypoint(loc);
		
		// if we don't have a waypoint, explore
		if (loc == null)
			loc = MapInfo.getExplorationWaypoint();
		
		// if we are the leader
		if (rc.getID() <= Message.recentArchonID)
		{
			Debug.setStringRR("LEADER");
		}
		
		// if we are a follower
		else
		{
			// look for leader
			loc = Message.recentArchonLocation;
			Debug.setStringRR("FOLLOWER: leader's ID is " + Message.recentArchonID);
		}

		if (loc != null)
		{
			// and send a message every certain few rounds
			if (rc.getRoundNum() % DEST_MESSAGE_FREQ == 0)
				Message.sendMessageSignal(DEST_MESSAGE_RANGE, MessageType.ARCHON_DEST, loc);
			
			// go where we should
			Behavior.tryGoToWithoutBeingShot(loc, Micro.getSafeMoveDirs());
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
		
		Behavior.tryGoToWithoutBeingShot(Message.rallyLocation, Micro.getSafeMoveDirs());
	}
	
	private static void doBuild() throws GameActionException
	{
		RobotType nextRobotType = getNextBuildRobotType();
		
		// return false quickly if we cannot build for an obvious reason
		if (nextRobotType == null)
			return;
		if (!rc.isCoreReady())
			return;
		if (!rc.hasBuildRequirements(nextRobotType))
			return;
		
		// find okay direction set
		DirectionSet canBuild = getCanBuildDirectionSet(nextRobotType);
		DirectionSet parity = getParityAllowedDirectionSet(nextRobotType);
		DirectionSet validBuildDirectionSet = canBuild.and(parity);
		
		// other considerations like safety can be considered here
		
		// pick one of the allowed directions at random
		Direction dir = validBuildDirectionSet.getRandomValid();
		
		// convert to directions and try to build
		tryBuildUnit(nextRobotType, dir);
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

	public static boolean tryBuildEven(RobotType robotToBuild) throws GameActionException
	{
		if (!rc.isCoreReady())
			return false;
		if (!rc.hasBuildRequirements(robotToBuild))
			return false;


		Direction dirToBuild = Direction.values()[rand.nextInt(8)];

		// make sure we are building on even square (see function name)
		if (MapUtil.isLocOdd(here.add(dirToBuild)))
			dirToBuild = dirToBuild.rotateRight();

		// rotate right two at a time
		for(int i=0; i<4; i++)
		{
			if (rc.canBuild(dirToBuild, robotToBuild))
			{
				rc.build(dirToBuild, robotToBuild);
				return true;
			}

			dirToBuild = dirToBuild.rotateRight().rotateRight();
		} 

		// failed to find any build locations
		return false;
	}

	public static boolean tryBuildOdd(RobotType robotToBuild) throws GameActionException
	{
		if (!rc.isCoreReady())
			return false;
		if (!rc.hasBuildRequirements(robotToBuild))
			return false;


		Direction dirToBuild = Direction.values()[rand.nextInt(8)];

		// make sure we are building on odd square (see function name)
		if (!MapUtil.isLocOdd(here.add(dirToBuild)))
			dirToBuild = dirToBuild.rotateRight();

		// rotate right two at a time
		for(int i=0; i<4; i++)
		{
			if (rc.canBuild(dirToBuild, robotToBuild))
			{
				rc.build(dirToBuild, robotToBuild);
				return true;
			}

			dirToBuild = dirToBuild.rotateRight().rotateRight();
		} 

		// failed to find any build locations
		return false;
	}

	public static boolean tryBuildUnit(RobotType nextRobot, Direction dir) throws GameActionException
	{
		// final checks
		if (!rc.isCoreReady() || dir == null)
			return false;
		if (!rc.hasBuildRequirements(nextRobot))
			return false;
		if (!rc.canBuild(dir, nextRobot))
			return false;
		
		// so other archons might get a chance to build.
		//if (rand.nextBoolean()) return false;
		
		rc.build(dir, nextRobot);
		return true;
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
	
	private static RobotType getNextBuildRobotType() throws GameActionException
	{
		// what's next to build: the old thing is not working?
		// RobotType nextRobotType = RobotType.values()[robotSchedule[scheduleCounter%robotSchedule.length]];
		
		// make a turret if we don't have enough around in our ball
/*		RobotInfo[] allies = rc.senseNearbyRobots(2, ourTeam);
		int numTurretsAround = 0;
		for (RobotInfo ri : allies)
		{
			if (ri.type == RobotType.TURRET || ri.type == RobotType.TTM)
				numTurretsAround += 1;
		}
		
		RobotType nextRobotType = RobotType.TURRET;
		if (numTurretsAround >= 3)
		{
			nextRobotType = RobotType.SCOUT;
		}
*/		
		RobotType nextRobotType = (rand.nextInt(5) == 0) ? RobotType.SCOUT : RobotType.SOLDIER;
		
		return nextRobotType;
	}
	
	private static boolean canBuildNow() throws GameActionException
	{
		// what's next to build
		RobotType nextRobotType = getNextBuildRobotType();
		
		// return false quickly if we cannot build for an obvious reason
		if (nextRobotType == null)
			return false;
		if (!rc.isCoreReady())
			return false;
		if (!rc.hasBuildRequirements(nextRobotType))
			return false;
		
		// find okay direction set
		DirectionSet canBuild = getCanBuildDirectionSet(nextRobotType);
		DirectionSet parity = getParityAllowedDirectionSet(nextRobotType);
		DirectionSet validBuildDirectionSet = canBuild.and(parity);
		
		// other considerations like safety can be considered here
		
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
