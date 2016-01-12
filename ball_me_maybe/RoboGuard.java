package ball_me_maybe;

import battlecode.common.*;

public class RoboGuard extends RobotPlayer
{
	public static void init() throws GameActionException
	{
	}
	
	public static void turn() throws GameActionException
	{
		if (!Behavior.tryShootWhileRetreatingFromZombies())
		{
			RobotInfo[] allies = Micro.getNearbyAllies();
			
			MapLocation[] locs = BallMove.updateBallDests(allies); // updates archon and archon destination using messaging
			MapLocation archonLoc = locs[0];
			MapLocation destLoc = locs[1];
			
			if (rc.isCoreReady())	
			{
				BallMove.ballMove(archonLoc, destLoc, allies);
			}
		}
	}
}
