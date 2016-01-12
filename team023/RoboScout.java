package team023;

import battlecode.common.*;

import java.util.*;

public class RoboScout extends RobotPlayer
{
	private enum ScoutState
	{
		SIGHTING,
		EXPLORING,
		SHADOWING
	}

	public static final int SIGNAL_ROUND = 30; 
	
	public static ScoutState myState = ScoutState.EXPLORING;
	public static MapLocation myTarget;
	
	public static void init() throws GameActionException
	{
		// first-spawn scout, send the message right away
		if (rc.getRoundNum() < SIGNAL_ROUND)
		{
			if (myArchon != null)
			{
				Message.sendMessageSignal(Message.FULL_MAP_DIST_SQ, MessageType.SPAWN, myArchon.location);
			}
		}
	}
	
	
	public static void turn() throws GameActionException
	{
		// state machine update
		updateState();
			
		if (rc.isCoreReady())
		{
			// do turn according to state
			switch (myState)
			{
			case EXPLORING:
				doScoutExploring();
				break;
			case SHADOWING:
				doScoutShadowing();
				break;
			case SIGHTING:
				doScoutSighting();
				break;
			}
		}
		
        // always send out info about sighted targets
		Sighting.doSendSightingMessage();
		
		// and use spare bytecodes to look for stuff
		MapInfo.analyzeSurroundings();
		// and send the updates
		if (!Micro.isInDanger())
			MapInfo.doScoutSendUpdates();
	}
	
	private static void updateState() throws GameActionException
	{
//		// if i am close to a turret and no one else is, post up
//		RobotInfo[] allies = Micro.getNearbyAllies();
//		int numTurrets = 0;
//		int numScouts = 0;
//		for (RobotInfo ri : allies)
//		{
//			if (ri.type == RobotType.TURRET)
//				numTurrets += 1;
//			else if (ri.type == RobotType.SCOUT)
//				numScouts += 1;
//		}
//		if (numTurrets > 0 && numScouts > 1)
//			myState = ScoutState.SIGHTING;
//		else
//			myState = ScoutState.EXPLORING;
		
		// if i see 2 scouts, i am free to explore
		RobotInfo[] allies = Micro.getNearbyAllies();
		int numScouts = 0;
		for (RobotInfo ri : allies)
		{
			if (ri.type == RobotType.SCOUT)
				numScouts += 1;
		}
		if (numScouts > 1)
			myState = ScoutState.SIGHTING;
		else
			myState = ScoutState.EXPLORING;
		
	}
	
	
	private static void doScoutExploring() throws GameActionException
	{
		Debug.setStringTS("Exploring to " + myTarget);
		// get a random waypoint and move towards it
		if (myTarget == null || here.distanceSquaredTo(myTarget) < 24 || !MapInfo.isOnMap(myTarget))
			myTarget = MapInfo.getExplorationWaypoint();
		
		DirectionSet goodDirs = Micro.getSafeMoveDirs();
		goodDirs = goodDirs.and(Micro.getTurretSafeDirs());
		
		Behavior.tryGoToWithoutBeingShot(myTarget, goodDirs);
	}

	private static void doScoutSighting() throws GameActionException
	{
		// stay close to closest turret
		// move away from nearby scouts
//		Debug.setStringTS("Sighting for nobody ");
//		RobotInfo[] allies = Micro.getNearbyAllies();
//		for (RobotInfo ri : allies)
//		{
//			if (ri.type == RobotType.TURRET)
//			{
//				Behavior.tryGoToWithoutBeingShot(ri.location, Micro.getSafeMoveDirs());
//				Debug.setStringTS("Sighting for " + ri.ID);
//			}
//		}
		
		RobotInfo[] allies = Micro.getNearbyAllies();
		
		MapLocation[] locs = BallMove.updateBallDests(allies); // updates archon and archon destination using messaging
		MapLocation archonLoc = locs[0];
		MapLocation destLoc = locs[1];
		
		if (rc.isCoreReady())	
		{
			if (Micro.getRoundsUntilDanger() < 5 && Behavior.tryRetreat())
				return;
			
			BallMove.ballMove(archonLoc, destLoc, allies);
		}
		
	}

	private static void doScoutShadowing() throws GameActionException
	{
		// stay close to the target we are shadowing
		MapLocation archonLoc = Message.recentArchonDest;
		if (archonLoc != null)
		{
			Behavior.tryGoToWithoutBeingShot(archonLoc, Micro.getSafeMoveDirs());
		}
	}
	
/*
	public static int[] getNumAlliedScoutsAndTurretsInRange() throws GameActionException
	{
		RobotInfo[] allies = rc.senseNearbyRobots(here, RobotType.SCOUT.sensorRadiusSquared, ourTeam);
		int numAlliedScoutsInRange = 0;
		int numAlliedTurretsInRange = 0;
		for (RobotInfo ri : allies)
		{
			if (ri.type == RobotType.SCOUT)
				numAlliedScoutsInRange += 1;
			else if (ri.type == RobotType.TURRET)
				numAlliedTurretsInRange += 1;
		}
		int[] packagedOutput = {numAlliedScoutsInRange, numAlliedTurretsInRange};
		return packagedOutput;
	}
	
	public static void doFreeScout() throws GameActionException
	{
		// if you see the turtle and no other scouts, then you are now a turtle scout
		int[] allyInfo = getNumAlliedScoutsAndTurretsInRange();
		int numAlliedScoutsInRange = allyInfo[0];
		int numAlliedTurretsInRange = allyInfo[1];

		
		// relay important sightings
		RobotInfo[] dens = doRelayDens();
		RobotInfo[] turrets = doRelayTurrets();
		
		if (!rc.isCoreReady())
			return;
		
		// pick a target: go to dens before spawn, go to enemies after
		int[] spawnRounds = rc.getZombieSpawnSchedule().getRounds();
		
		for (int round : spawnRounds) // go through zombie spawns
		{
			// if a spawn is in less than 20 rounds from now
			if (round - rc.getRoundNum() < 20 && rc.getRoundNum() - round > 0)
			{
				// rush the nearest den
				if (dens != null)
				{
					MapLocation closestDenLoc = dens[0].location;
					for (RobotInfo d : dens)
					{
						if (here.distanceSquaredTo(d.location) < here.distanceSquaredTo(closestDenLoc))
							closestDenLoc = d.location;
					}
					Nav.tryGoTo(closestDenLoc, Micro.getSafeMoveDirs());
					return;
				}
			}
			
			// if a spawn happened less than 200 rounds ago
			if (rc.getRoundNum() - round < 200 && rc.getRoundNum() - round > 0)
			{
				// rush the nearest enemies
				if (turrets != null)
				{
					MapLocation closestTurretLoc = turrets[0].location;
					for (RobotInfo t : turrets)
					{
						if (here.distanceSquaredTo(t.location) < here.distanceSquaredTo(closestTurretLoc))
							closestTurretLoc = t.location;
					}
					Nav.tryGoTo(closestTurretLoc, Micro.getSafeMoveDirs());
					return;
				}
				RobotInfo[] nearbyEnemies = rc.senseNearbyRobots(here, RobotType.SCOUT.sensorRadiusSquared, theirTeam);
				if (nearbyEnemies != null && nearbyEnemies.length > 0)
				{
					MapLocation closestEnemyLoc = nearbyEnemies[0].location;
					for (RobotInfo en : nearbyEnemies)
					{
						if (here.distanceSquaredTo(en.location) < here.distanceSquaredTo(closestEnemyLoc))
							closestEnemyLoc = en.location;
					}
					Nav.tryGoTo(closestEnemyLoc, Micro.getSafeMoveDirs());
					return;
				}
			}
		}
	}
	
	public static RobotInfo[] doRelayTurrets() throws GameActionException
	{
		// read signals and rebroadcast enemy turrets
		ArrayList<SignalLocation> knownEnemyTurretSignalLocs = Message.enemyTurretLocs;
        if (knownEnemyTurretSignalLocs != null && knownEnemyTurretSignalLocs.size() > 0)
        {
	        RobotInfo[] turrets = new RobotInfo[knownEnemyTurretSignalLocs.size()];
        	for (int i=0; i<knownEnemyTurretSignalLocs.size(); i++)
	        {
	        	MapLocation loc = knownEnemyTurretSignalLocs.get(i).loc;
	        	turrets[i] = new RobotInfo(0, theirTeam, RobotType.TURRET, loc, 0, 0, RobotType.TURRET.attackPower, RobotType.TURRET.maxHealth, RobotType.TURRET.maxHealth, 0, 0); 
	        }
	        sendSightedTarget(turrets);
	        return turrets;
        }
        return null;
	}
	
	public static void doTurtleScout() throws GameActionException
	{
		// go to edge of turtle by moving away from rally location but staying in turtle
		if (getNumTurretsNearby(here, 6) > 7 || getNumTurretsNearby(here, 3) < 2) // we're not at the edge
		{
			Nav.tryGoTo(here.add(here.directionTo(rally).opposite()), Micro.getSafeMoveDirs());
			//Nav.goTo(here.add(here.directionTo(rally).opposite()), safety); // move only if we're not at the edge
			//Nav.goTo(MapUtil.findClosestTurtle(), safety);
			
			// free the scout if it is not at the turtle edge and sees more than one other scout
			int[] allyInfo = getNumAlliedScoutsAndTurretsInRange();
			int numAlliedScoutsInRange = allyInfo[0];
			if (numAlliedScoutsInRange > 1)
				isFreeScout = true;
		}
		//Nav.goTo(MapUtil.findClosestTurtle(), safety);
	}
	
	public static int getNumTurretsNearby(MapLocation loc, int squareDist)
    {
    	RobotInfo units[] = rc.senseNearbyRobots(loc, squareDist, ourTeam);
    	int numTurretsAdjacent = 0;
    	for (RobotInfo friend : units)
    	{
    		if (friend.type == RobotType.TURRET)
    			numTurretsAdjacent += 1;
    	}
    	return numTurretsAdjacent;
    }
	

	*/
}
