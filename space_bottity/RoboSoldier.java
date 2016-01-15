package space_bottity;

import battlecode.common.*;
import space_bottity.RoboArchon.ArchonState;

public class RoboSoldier extends RobotPlayer
{
	public static Strategy myStrategy;
	
	public static void init() throws GameActionException
	{
		myStrategy = new BallMoveStrategy(RobotPlayer.myBuilderID, 2, 10);
	}
	
	public static void turn() throws GameActionException
	{
		if (!myStrategy.tryTurn())
			myStrategy = new FreeUnitStrategy();
	}
}
	