package botline_bling;

import battlecode.common.*;

public class RoboArchon extends RobotPlayer
{	
	static MapLocation nextTurretLoc = null;
	static int robotSchedule[] = {10, 10, 10, 6}; // 10-turret 6 - scout
	static int scheduleCounter = rand.nextInt(100);
	static int waitingToBuild = 0;
	static int maxWaitBuildTime = 10;
	static int maxWaitForRally = 600;
	static boolean arrivedAtRally = false;

	public static void init() throws GameActionException
	{

	}
	
	public static void turn() throws GameActionException
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
		
		if (!arrivedAtRally) 
		{
			arrivedAtRally = tryMoveNearRally();
		}
		else {
			nextTurretLoc = MapUtil.findClosestTurtle();		
			if	(!tryBuildUnits(nextTurretLoc)) {
				Debug.setStringAK("trying to move to turret dest" + nextTurretLoc);
				tryMoveNearTurretDest(nextTurretLoc);
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

	public static MapLocation findNextTurretDest(MapLocation dest) throws GameActionException
	{
		// start in random direction
		Direction d0 = Direction.values()[rand.nextInt(8)];
		if (!MapUtil.isLocOdd(here.add(d0)))
			d0 = d0.rotateRight();

		// check four adjacent odd squares
		for (int i=0; i<4; i++)
		{
			MapLocation ml = here.add(d0);
			if (!rc.isLocationOccupied(ml) && rc.senseRubble(ml) < GameConstants.RUBBLE_SLOW_THRESH && rc.onTheMap(ml))
			{
				return ml;
			}
			// rotate right twice to keep parity
			d0 = d0.rotateRight().rotateRight();
		}

		if (dest != null)
			return dest;

		// all else fails, return a random direction or something
		return here.add(rand.nextInt(200)-100,rand.nextInt(200)-100);

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
		if (here.isAdjacentTo(nextTurretLoc)) return false;
		
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
}
