package dropitlikeitsBot;

import battlecode.common.*;

import java.util.*;

public class RoboScout extends RobotPlayer
{
	private enum ScoutState
	{
		EXPLORING,
		BALLING,
		RUSHING,
		HERDING
	}

	public static final int SIGNAL_ROUND = 30; 	
	public static ScoutState myState = ScoutState.BALLING;
	public static MapLocation myExploringTarget;
	public static MapLocation rushingStartLoc = null;
	public static MapLocation herdingDestLoc = null;
	
	public static void init() throws GameActionException
	{
		// first-spawn scout, send the message right away
		if (rc.getRoundNum() < SIGNAL_ROUND)
		{
			if (myBuilderLocation != null)
				myState = ScoutState.BALLING;
				Message.sendMessageSignal(Message.FULL_MAP_DIST_SQ, MessageType.SPAWN, myBuilderLocation);
		}
		else
		{
			// start off balling around closest archon
			if (myBuilderLocation != null)
			{
				myState = ScoutState.BALLING;
				BallMove.startBalling(myBuilderID);
			}
		}
	}
	
	public static void turn() throws GameActionException
	{
		// state machine update
		updateState();
		Debug.setStringAK(myState.toString());
		
		// do turn according to state
		switch (myState)
		{
		case EXPLORING:
			doScoutExploring();
			break;
		case BALLING:
			doScoutBalling();
			break;
		case RUSHING:
			doScoutRushing();
			break;
		case HERDING:
			doScoutHerding();
			break;
		}
		
        // always send out info about sighted targets
		Sighting.doSendSightingMessage();
		
		// and use spare bytecodes to look for stuff
		MapInfo.analyzeSurroundings();
		// and send the updates
		MapInfo.doScoutSendUpdates();
		
		Debug.setStringTS("Scout: " + myState);
	}
	
	private static void updateState() throws GameActionException
	{
		RobotInfo[] zombies = Micro.getNearbyZombies();
		RobotInfo[] allies = Micro.getNearbyAllies();
		
		switch (myState)
		{
		case EXPLORING:
			// we just keep exploring, oh yeah
			break;
		case BALLING:
			// if i see zombies, rush them
			if (zombies.length != 0)
			{
				myState = ScoutState.RUSHING;
				rushingStartLoc = rc.getLocation();
				
				int herdDist = 50;
				Direction rushDir = rushingStartLoc.directionTo(Micro.getZombieCOM());
				herdingDestLoc = rushingStartLoc.add(rushDir.dx*herdDist,rushDir.dy*herdDist);
				break;
			}
				
//			// if i see too many scouts, i am free to explore
//			int numScouts = 0;
//			for (RobotInfo ri : allies)
//			{
//				if (ri.type == RobotType.SCOUT)
//					numScouts += 1;
//			}
//			if (numScouts > 5)
//				myState = ScoutState.EXPLORING;
//			break;
		case RUSHING:
			// if there is an adjacent zombie, start herding!
			for (RobotInfo ri : zombies)
			{
				if (ri.location.isAdjacentTo(rc.getLocation())) myState = ScoutState.HERDING;
				break;
			}
			break;
		case HERDING:
			// for now, just keep on herding until you are overrun or achieve ultimate glory
			
			break;
		}		
	}
	
	private static void doScoutExploring() throws GameActionException
	{
		Debug.setStringTS("Exploring to " + myExploringTarget);
		// get a random waypoint and move towards it
		if (myExploringTarget == null || here.distanceSquaredTo(myExploringTarget) < 24 || !MapInfo.isOnMap(myExploringTarget))
			myExploringTarget = MapInfo.getExplorationWaypoint();
		
		DirectionSet goodDirs = Micro.getSafeMoveDirs();
		goodDirs = goodDirs.and(Micro.getTurretSafeDirs());
		
		if (Micro.getRoundsUntilDanger() < 5)
			Behavior.tryRetreatTowards(Message.recentEnemySignal, goodDirs);
		else
			Behavior.tryGoToWithoutBeingShot(myExploringTarget, goodDirs);
	}

	private static void doScoutBalling() throws GameActionException

	{
		// try to update the position of our ball target
		if (!BallMove.tryUpdateTarget())
		{
			// AK for now, disable exploring
			// myState = ScoutState.EXPLORING;
			return;
		}
		
		// if there is a scout with a lower ID than me within two sq dist of ball target
		int lowestScoutID = rc.getID();
		RobotInfo[] nearby = rc.senseNearbyRobots(BallMove.lastBallLocation,2,ourTeam);
		for (RobotInfo ri : nearby)
			if (ri.type == RobotType.SCOUT && ri.ID < lowestScoutID)
				lowestScoutID = ri.ID;
		
		//if (lowestScoutID == rc.getID())
		//	BallMove.ballMove(0, 2);
		//else
		BallMove.ballMove(5,13);
	}

	private static void doScoutRushing() throws GameActionException
	{
		// got towards zombie enter of mass
		MapLocation zCOM = Micro.getZombieCOM();
		Behavior.tryDirectMove(here.directionTo(zCOM));
	}
	
	private static void doScoutHerding() throws GameActionException
	{
		//Micro.getRoundsUntilDanger()
	}
}