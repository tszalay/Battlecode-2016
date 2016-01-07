package botline_bling;

import battlecode.common.*;

public class RoboArchon extends RobotPlayer
{
	
	static MapLocation rallyLoc;
	static int RoundDiffuseInTurtle =100;
	static int WaitForBeaconUntilRound = 50;
	static Direction[] directions = {Direction.NORTH, Direction.NORTH_EAST, Direction.EAST, Direction.SOUTH_EAST,
            Direction.SOUTH, Direction.SOUTH_WEST, Direction.WEST, Direction.NORTH_WEST};
	//static int fate = rand.nextInt(20);

	public static void init() throws GameActionException
	{
		rallyLoc = null;
	}
	
	public static void turn() throws GameActionException
	{
		
		//read beacon
		if (rc.getRoundNum() == RoboScout.SIGNAL_ROUND)
		{
			//readBeacon();
			rallyLoc = rc.getLocation();//; new MapLocation (506+fate,30+fate);
		}
		if(rc.getRoundNum()== 1)
		{
			tryBuildEven(RobotType.SCOUT);
		}else if(rallyLoc == null && rc.getRoundNum() < WaitForBeaconUntilRound)
		{//wait for the scout
			Clock.yield();
		}else if(rallyLoc != null && here.distanceSquaredTo(rallyLoc) > 2  && rc.getRoundNum()<RoundDiffuseInTurtle)
		{
			//Debug.setStringRR("rallyLocx" + rallyLoc.x + "rallyLocy" + rallyLoc.y);
			NavSafetyPolicy safety = new SafetyPolicyAvoidAllUnits();
			if (rc.isCoreReady()){ 
				Nav.goTo(rallyLoc, safety);
			}
		}else
		{
			tryBuildEven(RobotType.TURRET);
		}

		//do this for the rest of the game
		//repair and run inside turtle...which doesn't always exist
		if (!tryrepair() && rc.getRoundNum() > RoundDiffuseInTurtle){
//			variable x = (expression) ? value if true : value if false
			int dx = rand.nextInt(3)-1;
			int	dy = rand.nextInt(3)-1;
			Debug.setStringRR("dx "+dx+"dy "+dy);
			NavSafetyPolicy safety = new SafetyPolicyAvoidAllUnitsAndStayInTurtle();
			if (rc.isCoreReady()){ 
				Nav.goTo(here.add(dx,dy), safety);
			}
		}
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
	
//	public static boolean readBeacon() throws GameActionException
//	{
//		int minID = 1000000;
//
//		Signal[] sigs = rc.emptySignalQueue();
//
//		if (sigs.length != 0)
//		{
//			for (Signal s : sigs)
//			{
//				// ignore enemy signals for now
//				if (s.getTeam() != ourTeam)
//					continue;
//
//				Message m = new Message(s);
//				switch(m.type)
//				{
//				case SPAWN:
//					MapLocation loc = m.readLocation();
//					if (s.getID() < minID)//calc location
//					{
//						rallyLoc = loc;
//						minID = s.getID();
//						//Debug.setStringRR(""+ minID);
//					}
//					//System.out.println(s.getID() + " " + s.getLocation() + " " + loc + " / MY LOC:" + here);
//					return true;
//				default:
//					return false;
//				}
//			}
//		}
//
//		return false;
//	}
	
	public static boolean tryBuildEven(RobotType robotToBuild) throws GameActionException {
		//builds on 'even' squares where (x+y)%2==0
		Direction dirToBuild;
		MapLocation here = rc.getLocation();
		if (((here.x + here.y) % 2) == 0)
		{//on even, build diag
			for(int i=1; i<=7; i=i+2)
			{
				dirToBuild = directions[i];
				if (buildValid(dirToBuild, robotToBuild))
				{
					rc.build(dirToBuild, robotToBuild);
				}
			}
		} else {//on odd, build rook
			for(int i=0; i<=7; i=i+2){
				dirToBuild = directions[i];
				if (rc.hasBuildRequirements(robotToBuild)){
					if (rc.canBuild(dirToBuild, robotToBuild)) {
						if (rc.isCoreReady()){
							rc.build(dirToBuild, robotToBuild);
							return true;
						}
					}
				}
			}
		}
	return false;
	}
	
	public static boolean buildValid(Direction dirToBuild, RobotType robotToBuild)
	{//helper build function
		if (rc.hasBuildRequirements(robotToBuild)){
			if (rc.canBuild(dirToBuild, robotToBuild)) {
				if (rc.isCoreReady()){
					return true;
				}
			}
		}
		return false;
	}
	
	
}//end of class






