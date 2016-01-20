package blitzkrieg_bot;

import battlecode.common.*;

public class RoboTurret extends RobotPlayer
{
	public static void init() throws GameActionException
	{
		myStrategy = new StratTurtleTurret();
	}
	
	public static void turn() throws GameActionException
	{
		myStrategy.tryTurn();
	}
}

