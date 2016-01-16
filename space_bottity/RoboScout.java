package space_bottity;

import battlecode.common.*;

import java.util.*;

public class RoboScout extends RobotPlayer
{
	public static Strategy myStrategy;
	
	public static void init() throws GameActionException
	{
		// start off balling around closest archon
		if (myBuilderLocation != null)
			myStrategy = new BallMoveStrategy(myBuilderID, 16, 48);
		else
			myStrategy = new FreeUnitStrategy();
	}
	
	public static void turn() throws GameActionException
	{
		if (!myStrategy.tryTurn())
			myStrategy = new FreeUnitStrategy();
			
        // always send out info about sighted targets
		Sighting.doSendSightingMessage();
		
		// and use spare bytecodes to look for stuff
		MapInfo.analyzeSurroundings();
		// and send the updates
		MapInfo.doScoutSendUpdates();
	}
}
