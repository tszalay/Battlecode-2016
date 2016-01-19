package botumn_leaves;

import battlecode.common.*;

import java.util.*;

public class BuildingStrat extends RobotPlayer implements Strategy
{
	// which strategy to use
	Strategy.Type myStrat = null;
	
	public String getName()
	{
		return "Building";
	}
	
	// creating the strategy actually forces us to build, so it better be valid
	public BuildingStrat(RobotType robot, Direction dir, Strategy.Type strat) throws GameActionException
	{
		myStrat = strat;
		rc.build(dir, robot);
	}
	
	// takes over until we can move again, then sends a built signal
	// and exits the strategy
	public boolean tryTurn() throws GameActionException
	{
		if (!rc.isCoreReady())
			return true;
		
		Message.sendBuiltMessage(myStrat);
		return false;
	}
}
