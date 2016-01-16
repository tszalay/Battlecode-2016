package i_bot_the_sheriff;

import battlecode.common.*;

import java.util.*;

// turtle move tries to allow units to move around close to or inside a turtle ball
public class TurtleMoveStrategy extends RobotPlayer implements Strategy
{
	public TurtleMoveStrategy()
	{
	}
	
	public boolean tryTurn() throws GameActionException
	{
		return true;
	}
}
