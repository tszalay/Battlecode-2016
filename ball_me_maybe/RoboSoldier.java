package ball_me_maybe;

import battlecode.common.*;

public class RoboSoldier extends RobotPlayer
{
	public static void init()
	{
	}
	
	public static void turn() throws GameActionException
	{
		Message.sendSignal(2*rc.getType().attackRadiusSquared);
		
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
