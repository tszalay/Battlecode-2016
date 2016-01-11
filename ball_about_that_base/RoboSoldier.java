package ball_about_that_base;

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
//		if (rc.isCoreReady())
//		{
//			DirectionSet dirs = Micro.getCanMoveDirs();
//			
//			if (Micro.isInDanger())
//			{
//				Direction d = Micro.getBestEscapeDir();
//				Micro.tryMove(d);
//			}
//			else
//			{
//				Micro.tryMove(dirs.getRandomValid());
//			}
//		}
		Behavior.tryShootWhileRetreating();
	}
}
