package ball_about_that_base;

import java.util.ArrayList;

import battlecode.common.*;

//enum for encoding state
enum State
{
	RALLYING,
	BUILDING,
	REPAIRING,
	SEARCHING
}

public class RoboArchon extends RobotPlayer
{	
	static int robotSchedule[] = {10, 10, 10, 6}; // 10-turret 6 - scout
	static int scheduleCounter = rand.nextInt(100);
	
	static final int MAX_ROUNDS_TO_RALLY = 200;
	
	static boolean arrivedAtRally = false;
	static boolean builtMyScout = false;
	
	static State myState;

	public static void init() throws GameActionException
	{
		myState = State.RALLYING;
	}
	
	
//	public static void bull()
//	{
//		if (rc.getRoundNum() == RoboScout.SIGNAL_ROUND)
//			Message.calcRallyLocation();
//
//		if(rc.getRoundNum()== 1)
//		{
//			if (tryBuildEven(RobotType.SCOUT)) 
//			{
//				Message.sendBuiltMessage();
//			}
//		}
//		
//		if (!Micro.tryAvoidBeingShot())
//		{
//			if (amIBeingAttacked())
//			{
//				Micro.updateAllies();
//				// find closest turtle location, and move away
//				NavSafetyPolicy safety = new SafetyPolicyAvoidAllUnits();
//				if (rc.isCoreReady())
//					Nav.goTo(Micro.getUnitCOM(Micro.nearbyAllies), safety);
//			}
//			else
//			{
//				if (!arrivedAtRally) 
//				{
//					arrivedAtRally = tryMoveNearRally();
//				}
//				else
//				{
//					nextTurretLoc = MapUtil.findClosestTurtle();
//					
//					// if it's too close to where i was last shot, change the nextTurretLoc
//					//if (nextTurretLoc != null && locLastAttacked != null  && nextTurretLoc.distanceSquaredTo(locLastAttacked)<2 )
//					//	nextTurretLoc = here.add(here.directionTo(locLastAttacked).opposite());
//					
//					if	(!tryBuildUnits(nextTurretLoc))
//					{
//						Debug.setStringAK("trying to move to turret dest" + nextTurretLoc);
//						tryMoveNearTurretDest(nextTurretLoc);
//					}
//				}
//			}
//		}
//		
//		//repair anyone nearby
//		tryRepair();
//
//	}
	
	

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
		if (!rc.isCoreReady())
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
		RobotInfo[] allies = rc.senseNearbyRobots(2, ourTeam);
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
	
	public static boolean canRepair() throws GameActionException
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
			return true;
		
		return false;
	}
	
	public static void doSearch() throws GameActionException
	{
		MicroBase micro = new MicroBase();
		DirectionSet safeDirs = micro.getSafeMoveDirs();
		ArrayList<Direction> safeDirArray = safeDirs.getDirections();
		
		return;
		
		// look for nearby neutrals and parts
		MapLocation[] parts = Message.partsLocs.loc;  // WANTED
		MapLocation[] neutrals = Message.neutralsLocs.loc;  // WANTED
		
		// move toward closest
		MapLocation closest = null;
		if (neutrals != null && neutrals.length > 0)
		{
			for (MapLocation loc : neutrals)
			{
				if (closest == null || here.distanceSquaredTo(loc) < here.distanceSquaredTo(closest))
					closest = loc;
			}
		}
		if (closest == null && parts != null && parts.length > 0)
		{
			for (MapLocation loc : parts)
			{
				if (closest == null || here.distanceSquaredTo(loc) < here.distanceSquaredTo(closest))
					closest = loc;
			}
		}
		
		Nav.tryAdjacentSafeMove(here.directionTo(closest), safeDirs);
	}
	
	public static void doRally() throws GameActionException
	{
		// update rally location
		if (rc.getRoundNum() == RoboScout.SIGNAL_ROUND)
			Message.calcRallyLocation();
		
		// make sure i built one scout
		if (!builtMyScout)
		{
			builtMyScout = tryBuildEven(RobotType.SCOUT);
		}
		
		// rally
		if (Message.rallyLocation == null)
			return;
		
		if (here.distanceSquaredTo(Message.rallyLocation) < 20)
		{
			arrivedAtRally = true;
			return;
		}
		else
		{
			
			MicroBase micro = new MicroBase();
			Nav.tryGoTo(Message.rallyLocation, micro.getSafeMoveDirs());
		}	
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
	
	public static void doRepair() throws GameActionException
	{
		tryRepair();
	}
	
	private static void updateState() throws GameActionException // this will probably need tweaking
	{
		// fact 0: if we are mid-building, we are still building
		if (myState == State.BUILDING && !rc.isCoreReady())
		{
			return;
		}
		
		// priority 1: go to rally
		if (!arrivedAtRally && rc.getRoundNum() < MAX_ROUNDS_TO_RALLY)
		{
			myState = State.RALLYING;
			return;
		}
		
		// priority 2: repair bots
		if (canRepair())
		{
			myState = State.REPAIRING;
			return;
		}
		
		// priority 3: if we can build, build
		if (canBuildNow()) // problem with this logic flow is i am calculating where to build twice...
		{
			myState = State.BUILDING;
			return;
		}
			
		// priority 4: search for stuff
		myState = State.SEARCHING;
		return;
	}
	
	public static void turn() throws GameActionException
	{
		// avoiding taking damage is the top priority
		if (!Micro.tryAvoidBeingShot())
		{
			// state machine update
			updateState();
			Debug.setStringSJF(myState.toString());
			
			if (rc.isCoreReady())
			{
				// do turn according to state
				switch (myState)
				{
				case RALLYING:
					doRally();
					break;
				case BUILDING:
					doBuild();
					break;
				case REPAIRING:
					doRepair();
					break;
				case SEARCHING:
					doSearch();
					break;
				default:
					System.out.println("Archon state machine is messed up.");
					break;
				}
			}
		}
	}
}
