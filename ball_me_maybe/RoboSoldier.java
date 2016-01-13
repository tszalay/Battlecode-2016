package ball_me_maybe;

import battlecode.common.*;

public class RoboSoldier extends RobotPlayer
{
	public static void init()
	{
	}
	
	public static void turn() throws GameActionException
	{
		// trial code: get rid of waypoints
		
		if (rc.senseParts(here)==0)
			MapInfo.removeWaypoint(here);
		
		BallMove.ballMove(7, 53);
	}
}
