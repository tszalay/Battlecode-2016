package botumn_leaves;

import battlecode.common.*;

public class RoboGuard extends RobotPlayer
{
	public static double myHealth;
	
	public static void init() throws GameActionException
	{
		myStrategy = new MobFightStrat();
		myHealth = rc.getType().maxHealth;
	}
	
	public static void turn() throws GameActionException
	{
		if (rc.getHealth() < myHealth)
		{
			myHealth = rc.getHealth();
			Message.sendSignal(RobotType.GUARD.sensorRadiusSquared*2);
		}
		
		myStrategy.tryTurn();
	}
}
