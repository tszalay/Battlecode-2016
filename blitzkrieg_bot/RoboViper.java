package blitzkrieg_bot;

import battlecode.common.*;

public class RoboViper extends RobotPlayer
{
	public static void init() throws GameActionException
	{
		myStrategy = new StratViperRush();
	}
	
	public static void turn() throws GameActionException
	{
		if (roundsSince(lastDamageRound) == 0)
			Message.sendSignal(rc.getType().sensorRadiusSquared*2);
		
		myStrategy.tryTurn();
	}
}
