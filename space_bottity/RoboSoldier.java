package space_bottity;

import battlecode.common.*;

public class RoboSoldier extends RobotPlayer
{
	public static void init() throws GameActionException
	{
		BallMove.startBalling(RobotPlayer.myBuilderID);
	}
	
	public static void turn() throws GameActionException
	{
		if (rc.senseParts(here)==0)
			MapInfo.removeWaypoint(here);
		
		BallMove.tryUpdateTarget();
		BallMove.ballMove(3, 15);
	}
}
