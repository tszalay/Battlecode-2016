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
//		 
//		RobotInfo[] allies = rc.senseNearbyRobots(RobotType.SCOUT.sensorRadiusSquared, ourTeam);
//		RobotInfo archon = null;
//		if (allies != null && allies.length > 0)
//		{
//			for (RobotInfo ri : allies)
//			{
//				if (ri.type == RobotType.ARCHON)
//				{
//					archon = ri;
//					continue;
//				}
//			}
//			if (archon != null)
//				myStrategy = new BlitzTeamStrat(RobotType.SCOUT, archon);
//		}
	}
	
	public static void turn() throws GameActionException
	{
		RobotInfo[] zombies = Micro.getNearbyZombies();
		RobotInfo[] allies = Micro.getNearbyAllies();
		RobotInfo[] enemies = Micro.getNearbyEnemies();
		
		// Default is Ball (see init)
		
		// zombie herd if necessary
		if (ZombieHerdingStrat.shouldRush(allies, zombies))
		{
			myStrategy = new ZombieHerdingStrat();
			ZombieHerdingStrat.rushingStartLoc = here;
			
			Direction rushDir = here.directionTo(Micro.getClosestUnitTo(zombies, here).location).rotateRight().rotateRight();//go perp
			ZombieHerdingStrat.herdingDestLoc = here.add(rushDir.dx*ZombieHerdingStrat.herdDist,rushDir.dy*ZombieHerdingStrat.herdDist);
		}
		
		// Free unit if everything else breaks
		if (!myStrategy.tryTurn())
			myStrategy = new FreeUnitStrategy();
		//myStrategy.tryTurn();
			
        // always send out info about sighted targets
		Sighting.doSendSightingMessage();
		
		// and use spare bytecodes to look for stuff
		// MapInfo.analyzeSurroundings();
		// and send the updates AK don't do this automtically - herding one's can't do it
		//MapInfo.doScoutSendUpdates();

	}
}
