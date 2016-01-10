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
		if (rc.isCoreReady())	
		{
			DirectionSet dirs = Micro.getCanMoveDirs();
			
			// AK Testing Soldier Behavior. Use with Soldier Ball Test Map.
			MapLocation archonLoc = Message.recentArchonLocation;
			//rc.setIndicatorDot(archonLoc, 0, 0, 255);
			MapLocation destLoc = Message.recentArchonDest;
			
			if (archonLoc == null) archonLoc = here;
			if (destLoc == null) destLoc = here;
			//rc.setIndicatorDot(destLoc, 0, 255, 0);
			Direction archonDir = archonLoc.directionTo(destLoc);
			MapLocation gotoLoc = archonLoc;
			
			// Offset gotoLoc towards dest to make soldiers shortcut a little
			int goToOffset = 3;
			for (int i=0;i<goToOffset;i++) gotoLoc = gotoLoc.add(archonDir);
			//rc.setIndicatorDot(gotoLoc, 255, 0, 0);
			
			
			
			
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
