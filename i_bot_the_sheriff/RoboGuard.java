package i_bot_the_sheriff;

import battlecode.common.*;

public class RoboGuard extends RobotPlayer
{
	public static Strategy myStrategy;
	public static double myHealth;
	
	public static void init() throws GameActionException
	{
		myStrategy = new MobFightStrat(RobotType.GUARD);
		myHealth = rc.getType().maxHealth;
	}
	
	public static void turn() throws GameActionException
	{
		if (rc.senseParts(here)==0)
			MapInfo.removeWaypoint(here);
		
		if (rc.getHealth() < myHealth)
		{
			myHealth = rc.getHealth();
			Message.sendSignal(RobotType.GUARD.sensorRadiusSquared*2);
		}
		
		myStrategy.tryTurn();
	}
}
