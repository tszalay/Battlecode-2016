package ball_me_maybe;

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
			if (ri.type == RobotType.SCOUT && ri.ID > rc.getID())
				numScouts += 1;
		}
		if (numScouts > 1)
			myState = ScoutState.EXPLORING;
		else
			myState = ScoutState.SIGHTING;
		
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
}
