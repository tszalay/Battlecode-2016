package botline_bling;

import org.apache.commons.io.output.NullWriter;

import battlecode.common.*;

public class RoboArchon extends RobotPlayer
{
	
	static MapLocation rallyLoc;
	
	public static void init() throws GameActionException
	{
		rallyLoc = null;	
	}

    //Build scout
    //Read signal queue to get location round 20
    	//Store number of buddy archons
    	//Store their start positions
    	//Map analysis?
    //Calculate location to move to
    //Move to desired location (nav.goto, no odd or evens)
		//Check core
		//Nav.goTo(safetypolicy stuff)
		//move
    //Build turrets/ttms figure out which 
    	//Build on even squares
    	//Check core
    	//Throw error if you can't
	//archon 
	
	public static void turn() throws GameActionException
	{
		Direction[] directions = {Direction.NORTH, Direction.NORTH_EAST, Direction.EAST, Direction.SOUTH_EAST,
                Direction.SOUTH, Direction.SOUTH_WEST, Direction.WEST, Direction.NORTH_WEST};


		//read beacon
		if (rc.getRoundNum() == RoboScout.SIGNAL_ROUND)
		{
			// check for signals by a time we should have received some

			int minID = 1000000;

			Signal[] sigs = rc.emptySignalQueue();
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
					break;
				default:
					break;
				}
			}
		}
		
		// try to heal
		RobotInfo[] nearbyFriends = rc.senseNearbyRobots(rc.getType().attackRadiusSquared,ourTeam);
		RobotInfo minBot = null;
		for (RobotInfo ri : nearbyFriends)
		{
			if (minBot == null || ri.health < minBot.health)
				minBot = ri;
		}
		if (minBot != null && minBot.type != RobotType.ARCHON && minBot.health < minBot.maxHealth-1)
			rc.repair(minBot.location);
		
		//build scout
		RobotType robotToBuild = RobotType.TURRET;
		if (rc.getRoundNum() <= 10){
			robotToBuild = RobotType.SCOUT;
			Debug.setStringRR("set to scout");
			Direction dirToBuild;
			MapLocation archonLoc = rc.getLocation();
			//System.out.println((archonLoc.x + archonLoc.y) % 2);
			if (((archonLoc.x + archonLoc.y) % 2) == 0){
				//System.out.println("on black diag");			
				for(int i=1; i<=7; i=i+2){
					Debug.setStringRR("black");
					dirToBuild = directions[i];
					if (rc.hasBuildRequirements(robotToBuild)){
						if (rc.canBuild(dirToBuild, robotToBuild)) {
							if (rc.isCoreReady()){
								rc.build(dirToBuild, robotToBuild);
							}
							break;
						}
					}
				}
			} else {//archon is on white, build on 
				//System.out.println("on white rook");
				for(int i=0; i<=7; i=i+2){
					Debug.setStringRR("white");
					dirToBuild = directions[i];
					if (rc.hasBuildRequirements(robotToBuild)){
						if (rc.canBuild(dirToBuild, robotToBuild)) {
							if (rc.isCoreReady()){
								rc.build(dirToBuild, robotToBuild);
							}
							break;
						}
					}
				}
			}
		}else if(rallyLoc == null){//then wait
			//rc.yield();//replace later
		}else if(here.distanceSquaredTo(rallyLoc) > 4){
			Debug.setStringRR("rallyLocx" + rallyLoc.x + "rallyLocy" + rallyLoc.y);
			NavSafetyPolicy safety = new SafetyPolicyAvoidAllUnitsAndStayInTurtle();
			if (rc.isCoreReady()){ 
				Nav.goTo(rallyLoc, safety);
			}
		}else{
			robotToBuild = RobotType.TURRET;

			Direction dirToBuild;
			MapLocation archonLoc = rc.getLocation();
			//System.out.println((archonLoc.x + archonLoc.y) % 2);
			if (((archonLoc.x + archonLoc.y) % 2) == 0){
				//System.out.println("on black diag");			
				for(int i=1; i<=7; i=i+2){
					dirToBuild = directions[i];
					if (rc.hasBuildRequirements(robotToBuild)){
						if (rc.canBuild(dirToBuild, robotToBuild)) {
							if (rc.isCoreReady()){
								rc.build(dirToBuild, robotToBuild);
							}
							break;
						}
					}
				}
			} else {//archon is on white, build on 
				//System.out.println("on white rook");
				for(int i=0; i<=7; i=i+2){
					dirToBuild = directions[i];
					if (rc.hasBuildRequirements(robotToBuild)){
						if (rc.canBuild(dirToBuild, robotToBuild)) {
							if (rc.isCoreReady()){
								rc.build(dirToBuild, robotToBuild);
							}
							break;
						}
					}
				}
			}
		}
	}
}
