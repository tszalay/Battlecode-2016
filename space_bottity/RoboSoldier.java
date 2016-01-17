package space_bottity;

import battlecode.common.*;

public class RoboSoldier extends RobotPlayer
{
	public static Strategy myStrategy;
	public static double myHealth;
	
	public static void init() throws GameActionException
	{
//		myStrategy = new BallMoveStrategy(RobotPlayer.myBuilderID, 2, 10);
//		myStrategy = new MobFightStrat(RobotType.SOLDIER);
		
		if (rc.getID() % 2 == 0)
			myStrategy = new BallMoveStrategy(RobotPlayer.myBuilderID, 2, 10);
		else
			myStrategy = new MobFightStrat(RobotType.SOLDIER);
		myHealth = rc.getType().maxHealth;
	}
	
	public static void turn() throws GameActionException
	{
//		if (!myStrategy.tryTurn())
//			myStrategy = new FreeUnitStrategy();
		if (rc.getHealth() < myHealth)
		{
			myHealth = rc.getHealth();
			Message.sendSignal(RobotType.SOLDIER.sensorRadiusSquared*2);
		}
		
		myStrategy.tryTurn();
	}
}
	