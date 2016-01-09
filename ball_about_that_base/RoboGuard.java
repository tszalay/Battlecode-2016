package ball_about_that_base;

import battlecode.common.*;

public class RoboGuard extends RobotPlayer
{
	public static void init() throws GameActionException
	{
	}
	
	public static void turn() throws GameActionException
	{
		MicroBase micro = new MicroBase();
		
		if (rc.isCoreReady())
		{
			DirectionSet dirs = micro.getCanMoveDirs();
			
			if (micro.isInDanger())
			{
				Direction d = micro.getBestEscapeDir();
				micro.tryMove(d);
			}
			else
			{
				micro.tryMove(dirs.getRandomValid());
			}
		}
	}
}
