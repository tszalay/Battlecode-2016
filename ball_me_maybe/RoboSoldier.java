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
		
		//BallMove.ballMove(5, 24);
		Debug.setStringSJF("CD = " + rc.getCoreDelay() + ", WD = " + rc.getWeaponDelay() + "... shoot and move in " + Micro.getRoundsUntilShootAndMove() + ", danger in " + Micro.getRoundsUntilDanger());
		if (Micro.getRoundsUntilShootAndMove() < Micro.getRoundsUntilDanger()-1)
			Behavior.tryAttackSomeone();
		else
			Behavior.tryRetreatOrShootIfStuck();
	}
}
