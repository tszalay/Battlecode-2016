package botline_bling;

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
//build scout
		Direction dirToBuild;
		MapLocation archonLoc = rc.getLocation();
		//System.out.println((archonLoc.x + archonLoc.y) % 2);
		if (((archonLoc.x + archonLoc.y) % 2) == 0){
			//System.out.println("on black diag");			
			for(int i=1; i<=7; i=i+2){
				dirToBuild = directions[i];
				if (rc.hasBuildRequirements(RobotType.SCOUT)){
					if (rc.canBuild(dirToBuild, RobotType.SCOUT)) {
						rc.build(dirToBuild, RobotType.SCOUT);
						break;
					}
				}
			}
		} else {//archon is on white, build on 
			//System.out.println("on white rook");
			for(int i=0; i<=7; i=i+2){
				dirToBuild = directions[i];
				if (rc.hasBuildRequirements(RobotType.SCOUT)){
					if (rc.canBuild(dirToBuild, RobotType.SCOUT)) {
						rc.build(dirToBuild, RobotType.SCOUT);
						break;
					}
				}
			}
		}
//read beacon
		if (rc.getRoundNum() > RoboScout.SIGNAL_ROUND)
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
						//Debug.setStringRR("rallyLocx" +rallyLoc.x + "rallyLocy" +rallyLoc.y);
					}
					//System.out.println(s.getID() + " " + s.getLocation() + " " + loc + " / MY LOC:" + here);
					break;
				default:
					break;
				}
			}

			Debug.setStringRR("rallyLocx" + rallyLoc.x + "rallyLocy" + rallyLoc.y);
			NavSafetyPolicy safety = new SafetyPolicyAvoidAllUnits();
			Nav.goTo(rallyLoc, safety);

//			MapLocation target = rallyLoc;
			// now the rally location is in rallyLoc



			//		int fate=100;
			//        if (rc.isCoreReady()) {
			//            if (fate < 800) {
			//                // Choose a random direction to try to move in
			//                Direction dirToMove = Direction.values()[fate % 8];
			//                // Check the rubble in that direction
			//                if (rc.senseRubble(rc.getLocation().add(dirToMove)) >= GameConstants.RUBBLE_OBSTRUCTION_THRESH) {
			//                    // Too much rubble, so I should clear it
			//                    rc.clearRubble(dirToMove);
			//                    // Check if I can move in this direction
			//                } else if (rc.canMove(dirToMove)) {
			//                    // Move
			//                    rc.move(dirToMove);
			//                }
			//            } else {
			//                // Choose a random unit to build
			//                RobotType typeToBuild = RobotType.values()[fate % 8];
			//                // Check for sufficient parts
			//                if (rc.hasBuildRequirements(typeToBuild)) {
			//                    // Choose a random direction to try to build in
			//                    Direction dirToBuild = Direction.values()[rand.nextInt(8)];
			//                    for (int i = 0; i < 8; i++) {
			//                        // If possible, build in this direction
			//                        if (rc.canBuild(dirToBuild, typeToBuild)) {
			//                            rc.build(dirToBuild, typeToBuild);
			//                            break;
			//                        } else {
			//                            // Rotate the direction to try
			//                            dirToBuild = dirToBuild.rotateLeft();
			//                        }
			//                    }
			//                }
			//            }
			//        }
		}
	}
}
