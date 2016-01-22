package oops_bot_did_it_again;

import battlecode.common.*;

public class RoboSoldier extends RobotPlayer
{
	public static void init() throws GameActionException
	{
		myStrategy = new StratUnitCombat();
	}
	
	public static void turn() throws GameActionException
	{
		if (roundsSince(lastDamageRound) == 0)
			Message.sendSignal(rc.getType().sensorRadiusSquared*2);
		
		myStrategy.tryTurn();
	}
}
	