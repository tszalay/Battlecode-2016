package i_bot_the_sheriff;

import java.util.ArrayList;

import battlecode.common.*;

public class RoboArchon extends RobotPlayer
{
	private static Strategy myStrategy;
	
	public static void init()
	{
		myStrategy = new TurtleArchonStrategy();
	}
	
	public static void turn() throws GameActionException
	{
		myStrategy.tryTurn();
		MapInfo.analyzeSurroundings();
	}
}
