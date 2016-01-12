package ball_on_me;

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
