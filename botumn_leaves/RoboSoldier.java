package botumn_leaves;

import battlecode.common.*;

public class RoboSoldier extends RobotPlayer
{
	public static Strategy myStrategy;
	public static double myHealth;
	public static MapLocation lastBallLocation;
	
	public static void init() throws GameActionException
	{
		myStrategy = new BallMoveStrategy(RobotPlayer.myBuilderID, 8, 16);
//		myStrategy = new MobFightStrat();
		
//		if (rc.getID() % 2 == 0)
//			myStrategy = new BallMoveStrategy(RobotPlayer.myBuilderID, 2, 10);
//		else
//			myStrategy = new MobFightStrat();
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
		
		MapLocation closestDen = MapInfo.getClosestDen();
		if (closestDen != null && here.distanceSquaredTo(closestDen) < 400)
		{
			lastBallLocation = here;
			myStrategy = new MobFightStrat(closestDen);
		}
		
		if (!myStrategy.tryTurn())
		{
			myStrategy = new BallMoveStrategy(RobotPlayer.myBuilderID, 8, 16);
			if (lastBallLocation != null)
				Action.tryGoToWithoutBeingShot(lastBallLocation, Micro.getSafeMoveDirs());
		}
	}
}
	