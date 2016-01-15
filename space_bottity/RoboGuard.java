package space_bottity;

import battlecode.common.*;

public class RoboGuard extends RobotPlayer
{
	public static double health = RobotType.GUARD.maxHealth;
	
	public static void init() throws GameActionException
	{
		BallMove.startBalling(RobotPlayer.myBuilderID);
	}
	
	public static void turn() throws GameActionException
	{
		if (rc.senseParts(here)==0)
			MapInfo.removeWaypoint(here);
		
		MobFightStrat strat = new MobFightStrat(RobotType.GUARD);
		strat.doTurn();
		
		// ping if attacked
		if (rc.getHealth() < health)
		{
			Message.sendSignal(RobotType.GUARD.sensorRadiusSquared*2);
			health = rc.getHealth();
		}
		
		//BallMove.ballMove(3, 15);
	}
}
