package botline_bling;

import battlecode.common.*;

public class RoboArchon extends RobotPlayer
{	
	static MapLocation rallyLoc;

	public static void init() throws GameActionException
	{
		rallyLoc = null;
	}
	
	public static void turn() throws GameActionException
	{
		if (rc.getRoundNum() == RoboScout.SIGNAL_ROUND)
		{
			int bestID = 1000000;
			MapLocation bestloc = null;
			
			for (SignalLocation sm : Message.archonLocs)
			{
				if (sm.sig.getID() < bestID)
				{
					bestloc = sm.loc;
					bestID = sm.sig.getID();
				}
			}
			
			rallyLoc = bestloc;
		}

		if(rc.getRoundNum()== 1)
		{
			tryBuildEven(RobotType.SCOUT);
		}
		else if(rallyLoc == null)
		{
			//rc.yield();//replace later
		}
		else if(here.distanceSquaredTo(rallyLoc) > 4)
		{
			NavSafetyPolicy safety = new SafetyPolicyAvoidAllUnits();
			if (rc.isCoreReady())
			{ 
				Nav.goTo(rallyLoc, safety);
			}
		}
		else
		{
			tryBuildEven(RobotType.TURRET);
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
	/*
	public static boolean readBeacon() throws GameActionException
	{
		//Boolean isgood = true;
//		if (rc.getRoundNum() == RoboScout.SIGNAL_ROUND)
//		{
			// check for signals by a time we should have received some

			int minID = 1000000;

			Signal[] sigs = rc.emptySignalQueue();

			if (sigs.length != 0)
			{
				for (Signal s : sigs)
				{
					// ignore enemy signals for now
					if (s.getTeam() != ourTeam)
						continue;

					Message m = new Message(s);
					switch(m.type)
					{
					case SPAWN:
						MapLocation loc = m.readLocation();
						if (s.getID() < minID)//calc location
						{
							rallyLoc = loc;
							minID = s.getID();
							//Debug.setStringRR(""+ minID);
						}
						//System.out.println(s.getID() + " " + s.getLocation() + " " + loc + " / MY LOC:" + here);
						return true;
					default:
						return false;
					}
				}
			}
//		}
		return false;
	}
	*/
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
	
	
}//end of class






