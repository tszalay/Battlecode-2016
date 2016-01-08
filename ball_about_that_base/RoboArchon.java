package ball_about_that_base;

import battlecode.common.*;

//enum for encoding state
enum State
{
	RALLYING,
	BUILDING,
	SEARCHING
}

public class RoboArchon extends RobotPlayer
{	
	static MapLocation nextTurretLoc = null;
	static int robotSchedule[] = {10, 10, 10, 6}; // 10-turret 6 - scout
	static int scheduleCounter = rand.nextInt(100);
	static int waitingToBuild = 0;
	static int maxWaitBuildTime = 10;
	static final int MAX_ROUNDS_TO_RALLY = 600;
	static boolean arrivedAtRally = false;
	static boolean canBuildNow = false;
	static State myState;

	public static void init() throws GameActionException
	{
		myState = State.RALLYING;
	}
	
	
	public static void bull()
	{
		if (rc.getRoundNum() == RoboScout.SIGNAL_ROUND)
			Message.calcRallyLocation();

		if(rc.getRoundNum()== 1)
		{
			if (tryBuildEven(RobotType.SCOUT)) 
			{
				Message.sendBuiltMessage();
			}
		}
		
		if (!Micro.tryAvoidBeingShot())
		{
			if (amIBeingAttacked())
			{
				Micro.updateAllies();
				// find closest turtle location, and move away
				NavSafetyPolicy safety = new SafetyPolicyAvoidAllUnits();
				if (rc.isCoreReady())
					Nav.goTo(Micro.getUnitCOM(Micro.nearbyAllies), safety);
			}
			else
			{
				if (!arrivedAtRally) 
				{
					arrivedAtRally = tryMoveNearRally();
				}
				else
				{
					nextTurretLoc = MapUtil.findClosestTurtle();
					
					// if it's too close to where i was last shot, change the nextTurretLoc
					if (nextTurretLoc != null && locLastAttacked != null  && nextTurretLoc.distanceSquaredTo(locLastAttacked)<2 )
						nextTurretLoc = here.add(here.directionTo(locLastAttacked).opposite());
					
					if	(!tryBuildUnits(nextTurretLoc))
					{
						Debug.setStringAK("trying to move to turret dest" + nextTurretLoc);
						tryMoveNearTurretDest(nextTurretLoc);
					}
				}
			}
		}
		
		//repair anyone nearby
		tryRepair();

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
	
	public static boolean tryMoveNearRally() throws GameActionException
	{
		if(Message.rallyLocation == null)
		{
			// have to wait until rally loc arrives. is this robust??
		}
		else if(here.distanceSquaredTo(Message.rallyLocation) > 4)
		{
			NavSafetyPolicy safety = new SafetyPolicyAvoidAllUnits();
			if (rc.isCoreReady())
			{ 
				Nav.goTo(Message.rallyLocation, safety);
			}
		}
		else
		{
			return true;
		}
		return false;		
	}

	public static boolean tryBuildUnits(MapLocation nextTurretLoc) throws GameActionException
	{
		// so other archons might get a chance to build.
		if (rand.nextBoolean()) return false;
		
		RobotType nextRobot = RobotType.values()[robotSchedule[scheduleCounter%robotSchedule.length]];
		if (nextRobot == RobotType.SCOUT)
		{
			if (tryBuildEven(nextRobot)) 
			{
				scheduleCounter++;
				return true;
			}
		}
		else if (nextRobot == RobotType.TURRET) 
		{
			// Debug.setStringAK("NextTurretDest: " + nextTurretLoc);

			if (tryBuildOdd(nextRobot))
			{
				waitingToBuild = 0;
				scheduleCounter++;
				Message.sendBuiltMessage();
				return true;
			}
		}
		
		return false;
	}

	public static boolean tryMoveNearTurretDest(MapLocation nextTurretLoc) throws GameActionException
	{
		if (nextTurretLoc == null)
			return false;
		
		if (here.isAdjacentTo(nextTurretLoc))
			return false;
		
		if (rc.isCoreReady())
		{
			NavSafetyPolicy safety = new SafetyPolicyAvoidAllUnits();
			Nav.goTo(nextTurretLoc, safety);
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public static DirectionSet getValidBuildDirections() throws GameActionException
	{
		// what's next to build
		RobotType nextRobotType = RobotType.values()[robotSchedule[scheduleCounter%robotSchedule.length]];
		
		// check nearby open squares
		DirectionSet d = new DirectionSet();
		for (Direction dir : Direction.values()) // check all Directions around
		{
			if (rc.canBuild(dir, nextRobotType))
				d.add(dir); // add this direction to the DirectionSet of valid build directions
		}
		return d;
	}
	
	public static boolean canBuildNow() throws GameActionException
	{
		// what's next to build
		RobotType nextRobotType = RobotType.values()[robotSchedule[scheduleCounter%robotSchedule.length]];
		
		// check parts
		if (rc.getTeamParts() >= nextRobotType.partCost) // we have enough parts to build the next robot
		{
			// check nearby open squares
			for (Direction dir : Direction.values()) // check all Directions around
			{
				if (rc.canBuild(dir, nextRobotType))
					return true; // stop as soon as we find a valid spot
			}
		}
		
		return false;
	}
	
	public static void doRally() throws GameActionException
	{
		
	}
	
	public static void doBuild() throws GameActionException
	{
		DirectionSet dValidBuild = getValidBuildDirections();
	}
	
	public static void doSearch() throws GameActionException
	{
		
	}
	
	private static void updateState() throws GameActionException // this will probably need tweaking
	{
		// priority 1: go to rally
		if (!arrivedAtRally && rc.getRoundNum() < MAX_ROUNDS_TO_RALLY)
		{
			myState = State.RALLYING;
			return;
		}
		
		// priority 2: if we can build, build
		if (canBuildNow())
		{
			myState = State.BUILDING;
			return;
		}
			
		// priority 3: search for stuff
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
			
			// do turn according to state
			switch (myState)
			{
			case RALLYING:
				doRally();
				break;
			case BUILDING:
				doBuild();
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
