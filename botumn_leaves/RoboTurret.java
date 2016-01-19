package botumn_leaves;

import battlecode.common.*;

public class RoboTurret extends RobotPlayer
{
	public static void init() throws GameActionException
	{
		myStrategy = new TurtleTurretStrategy();
	}
	
	public static void turn() throws GameActionException
	{
		myStrategy.tryTurn();
	}
}

