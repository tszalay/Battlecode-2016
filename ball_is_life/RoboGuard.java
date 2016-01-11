package ball_is_life;

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
				Micro.tryMove(d);
			}
			else
			{
				Micro.tryMove(dirs.getRandomValid());
			}
		}
	}
}