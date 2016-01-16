package i_bot_the_sheriff;

import battlecode.common.*;

import java.util.*;

public class BuildingStrategy extends RobotPlayer implements Strategy
{
	// creating the strategy actually forces us to build, so it better be valid
	public BuildingStrategy(RobotType robot, Direction dir) throws GameActionException
	{
		rc.build(dir, robot);
	}
	
	// takes over until we can move again, then sends a built signal
	// and exits the strategy
	public boolean tryTurn() throws GameActionException
	{
		if (!rc.isCoreReady())
			return true;
		
		Message.sendBuiltMessage();
		return false;
	}
}
