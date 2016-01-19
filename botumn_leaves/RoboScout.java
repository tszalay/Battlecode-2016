package botumn_leaves;

import battlecode.common.*;

import java.util.*;

public class RoboScout extends RobotPlayer
{
	public static void init() throws GameActionException
	{
		if (Message.recentStrategySignal == null)
		{
			myStrategy = new StratExplore();
			return;
		}
		// start off balling around closest archon
		switch (Message.recentStrategySignal)
		{
		case SHADOW_ARCHON:
			myStrategy = new StratScoutShadow(myBuilderID);
			break;
		case SHADOW_SOLDIER:
			myStrategy = new StratScoutSighting();
			break;
		case EXPLORE:
		default:
			myStrategy = new StratExplore();
			break;
		}
	}

	
	public static void turn() throws GameActionException
	{
		boolean sendUpdates = true;
		
		// Default is Ball (see init)
		
		// zombie herd if necessary
		/*
		if (ZombieHerdingStrat.shouldHerd())
		{
			myStrategy = new ZombieHerdingStrat();
			ZombieHerdingStrat.rushingStartLoc = here;
			
			Direction rushDir = here.directionTo(Micro.getClosestUnitTo(Micro.getNearbyZombies(), here).location).rotateRight().rotateRight();//go perp
			ZombieHerdingStrat.herdingDestLoc = here.add(rushDir.dx*ZombieHerdingStrat.herdDist,rushDir.dy*ZombieHerdingStrat.herdDist);
			
			sendUpdates = false;
		}
		
		// explore if you are free to do so
		if (ExploreStrat.shouldExplore())
		{
			myStrategy = new ExploreStrat();
		}*/

		// Free unit if everything else breaks
		if (!myStrategy.tryTurn())
			myStrategy = new StratFreeUnit();
		//myStrategy.tryTurn();
		
		Sighting.doSendSightingMessage();

		if (sendUpdates) // zombie herding zombies don't have time to send updates
		{
			MapInfo.doScoutSendUpdates();
		}
	}
}
