package ball_is_life;

import battlecode.common.*;

public class RoboSoldier extends RobotPlayer
{
	static int fate;
	
	public static void init()
	{
		fate = rand.nextInt(1000);
	}
	
	public static void turn() throws GameActionException
	{
		RobotInfo[] allies = Micro.getNearbyAllies();
		boolean archonInSight = false;
		
		// update archon and dest locations
		MapLocation archonLoc = Message.recentArchonLocation;
		MapLocation destLoc = Message.recentArchonDest;
		if (archonLoc == null) archonLoc = here;
		if (destLoc == null) destLoc = here;
		
		// if you see an archon update your location
		for (RobotInfo ri : allies)
		{
			if (ri.type == RobotType.ARCHON) archonLoc = ri.location;
			archonInSight = true;
		}
		
		// If you can see your saved archonLoc but can't find the archon, go straight to dest
		if (rc.canSenseLocation(archonLoc) && !archonInSight) archonLoc = destLoc;
		
		
		if (rc.isCoreReady())	
		{
			DirectionSet dirs = Micro.getCanMoveDirs();
			Direction archonDir = archonLoc.directionTo(destLoc);
			MapLocation gotoLoc = archonLoc;
			
			// Offset gotoLoc towards dest to make soldiers shortcut a little
			int goToOffset = 5;
			for (int i=0;i<goToOffset;i++) gotoLoc = gotoLoc.add(archonDir);
	
			Nav.tryGoTo(gotoLoc, dirs);
			
//			if (Micro.isInDanger())
//			{
//				Direction d = Micro.getBestEscapeDir();
//				Micro.tryMove(d);
//			}
//			else
//			{
//
//				// Micro.tryMove(dirs.getRandomValid());
//			}
		}
	}
}
