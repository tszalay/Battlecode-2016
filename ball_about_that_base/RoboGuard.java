package ball_about_that_base;

import battlecode.common.*;

public class RoboGuard extends RobotPlayer
{
	public static void init() throws GameActionException
	{
	}
	
	public static void turn() throws GameActionException
	{
		if (rc.isCoreReady())
		{
			DirectionSet dirs = Micro.getCanMoveDirs();
			
			if (Micro.isInDanger())
			{
				Direction d = Micro.getBestEscapeDir();
				MicroBase.tryMove(d);
			}
			else
			{
				MicroBase.tryMove(dirs.getRandomValid());
			}
		}
	}
}
