package space_bottity;

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
	public static MapLocation myExploringTarget;
	public static int myShadowID;
	
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
		else
		{
			if (myArchon != null)
			{
				myState = ScoutState.SHADOWING;
				myShadowID = myArchon.ID;
				for (RobotInfo ri : rc.senseNearbyRobots(myArchon.location,4,ourTeam))
				{
					if (ri.type == RobotType.SCOUT)
					{
						myState = ScoutState.SIGHTING;
						break;
					}
				}
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
		MapInfo.doScoutSendUpdates();
		
		Debug.setStringTS("Scout: " + myState);
	}
	
	private static void updateState() throws GameActionException
	{
		switch (myState)
		{
		case EXPLORING:
			// we just keep exploring, oh yeah
			break;
		case SIGHTING:
			// if i see 2 scouts, i am free to explore
			RobotInfo[] allies = Micro.getNearbyAllies();
			int numScouts = 0;
			for (RobotInfo ri : allies)
			{
				if (ri.type == RobotType.SCOUT && ri.ID > rc.getID())
					numScouts += 1;
			}
			if (numScouts > 3)
				myState = ScoutState.EXPLORING;
			else
				myState = ScoutState.SIGHTING;
			
			// or if i haven't seen an archon in a while
			if (rc.getRoundNum()-Message.recentArchonRound > 20)
				myState = ScoutState.EXPLORING;

			break;
		case SHADOWING:
			if (!rc.canSenseRobot(myShadowID))
				myState = ScoutState.SIGHTING;
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

	private static void doScoutSighting() throws GameActionException
	{
		BallMove.ballMove(24, 53);
	}

	private static void doScoutShadowing() throws GameActionException
	{
		if (!rc.canSenseRobot(myShadowID))
			return;
		
		RobotInfo shadowInfo = rc.senseRobot(myShadowID);
		
		if (Micro.isInDanger())
		{
			Behavior.tryRetreatTowards(shadowInfo.location.add(Micro.getBestRetreatDir()), Micro.getSafeMoveDirs());
			return;
		}

		if (here.distanceSquaredTo(shadowInfo.location) > 2)
			Behavior.tryAdjacentSafeMoveToward(shadowInfo.location);
	}
}
