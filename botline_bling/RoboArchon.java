package botline_bling;

import org.apache.commons.io.output.NullWriter;

import battlecode.common.*;

public class RoboArchon extends RobotPlayer
{
	
	static MapLocation rallyLoc;
	static Direction[] directions = {Direction.NORTH, Direction.NORTH_EAST, Direction.EAST, Direction.SOUTH_EAST,
            Direction.SOUTH, Direction.SOUTH_WEST, Direction.WEST, Direction.NORTH_WEST};

	public static void init() throws GameActionException
	{
		rallyLoc = null;
	}
	
	public static void turn() throws GameActionException
	{
		
		//read beacon
		if (rc.getRoundNum() == RoboScout.SIGNAL_ROUND)
			readBeacon();

		if(rc.getRoundNum()== 1){
			tryBuildEven(RobotType.SCOUT);
		}else if(rallyLoc == null){//then wait
			//rc.yield();//replace later
		}else if(here.distanceSquaredTo(rallyLoc) > 4){
			//Debug.setStringRR("rallyLocx" + rallyLoc.x + "rallyLocy" + rallyLoc.y);
			NavSafetyPolicy safety = new SafetyPolicyAvoidAllUnits();
			if (rc.isCoreReady()){ 
				Nav.goTo(rallyLoc, safety);
			}
		}else{
			tryBuildEven(RobotType.TURRET);
		}
		//repair anyone nearby
		tryrepair();
	}
	

	public static boolean tryrepair() throws GameActionException
	{
		RobotInfo[] nearbyFriends = rc.senseNearbyRobots(rc.getType().attackRadiusSquared,ourTeam);
		RobotInfo minBot = null;
		for (RobotInfo ri : nearbyFriends)
		{
			if (minBot == null || ri.health < minBot.health)
				minBot = ri;
		}
		if (minBot != null && minBot.type != RobotType.ARCHON && minBot.health < minBot.maxHealth-1)
		{
			rc.repair(minBot.location);
			return true;
		}
		return false;
	}
	
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
	
	public static boolean tryBuildEven(RobotType robotToBuild) throws GameActionException {
		//return true;
		//Direction[] directions = {Direction.NORTH, Direction.NORTH_EAST, Direction.EAST, Direction.SOUTH_EAST,Direction.SOUTH, Direction.SOUTH_WEST, Direction.WEST, Direction.NORTH_WEST};
		Direction dirToBuild;
		MapLocation here = rc.getLocation();

		if (((here.x + here.y) % 2) == 0){
			//System.out.println("on black diag");			
			for(int i=1; i<=7; i=i+2){
				//Debug.setStringRR("black");
				dirToBuild = directions[i];
				if (rc.hasBuildRequirements(robotToBuild)){
					if (rc.canBuild(dirToBuild, robotToBuild)) {
						if (rc.isCoreReady()){
							rc.build(dirToBuild, robotToBuild);
							return true;
						}
						//break;
					}
				}
			}
		} else {//archon is on white, build on 
			for(int i=0; i<=7; i=i+2){
				//Debug.setStringRR("white");
				dirToBuild = directions[i];
				if (rc.hasBuildRequirements(robotToBuild)){
					if (rc.canBuild(dirToBuild, robotToBuild)) {
						if (rc.isCoreReady()){
							rc.build(dirToBuild, robotToBuild);
							return true;
						}
						//break;						
					}
				}
			}
		}
	return false;
	}
	
	
}//end of class






