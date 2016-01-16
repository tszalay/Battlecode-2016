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
		HERDING,
		KAMIKAZE;
	}

	public static final int SIGNAL_ROUND = 30; 	
	public static ScoutState myState = ScoutState.BALLING;
	public static MapLocation myExploringTarget;
	public static MapLocation rushingStartLoc = null;
	public static MapLocation herdingDestLoc = null;	
	public static int herdDist = 100;
	
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
		case KAMIKAZE:
			doScoutKamikaze();
			break;
		}
		
        // always send out info about sighted targets
		
		//Sighting.doSendSightingMessage();
		
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
		RobotInfo[] enemies = Micro.getNearbyEnemies();
		
		switch (myState)
		{
		case EXPLORING:
			// we just keep exploring, oh yeah
			break;
		case BALLING:
			//
			if (shouldRush(allies, zombies))
			{
				myState = ScoutState.RUSHING;
				rushingStartLoc = here;
				
				Direction rushDir = rushingStartLoc.directionTo(Micro.getClosestUnitTo(zombies, here).location);
				herdingDestLoc = rushingStartLoc.add(rushDir.dx*herdDist,rushDir.dy*herdDist);
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
			// If you are closer to the closest Zombie than any ally, switch to Herding state
			RobotInfo closestAlly = Micro.getClosestUnitTo(allies, here);
			RobotInfo closestZombie = Micro.getClosestUnitTo(zombies, here);

			if (closestZombie !=null)
			{
				if (closestAlly == null)
				{
					myState = ScoutState.HERDING;
				}
				else if (here.distanceSquaredTo(closestZombie.location) <= here.distanceSquaredTo(closestAlly.location))
				{
					myState = ScoutState.HERDING;
				}
			}
			break;
		case HERDING:
			// for now, just keep on herding until you are overrun or achieve ultimate glory
			
			// if you don't see zombies anymore, they're probably gone!
			if (zombies.length == 0)
			{
				myState = ScoutState.BALLING;
			}
			
			if (shouldKamikaze(enemies, zombies))
			{
				myState = ScoutState.KAMIKAZE;
			}
			break;
		case KAMIKAZE:
			break;
		}		
	}
	private static boolean shouldRush(RobotInfo[] allies, RobotInfo[] zombies) throws GameActionException
	{
		boolean canSeeArchon = false;
		
		// don't rush if no zombies
		if (zombies.length == 0) return false;
		RobotInfo closestZombie = Micro.getClosestUnitTo(zombies, rc.getLocation());
		int dist2ToZombie = rc.getLocation().distanceSquaredTo(closestZombie.location);
		
		// don't rush if you see fast zombies
		for (RobotInfo ri : zombies)
		{
			if (ri.type == RobotType.FASTZOMBIE) return false;
		}

		for (RobotInfo ri : allies)
		{
			if (ri.type == RobotType.ARCHON) canSeeArchon = true;
			// don't rush if another scout is closer to zombie
			else if (ri.type == RobotType.SCOUT)
			{
				int otherDist2ToZombie = ri.location.distanceSquaredTo(closestZombie.location);
				if  (otherDist2ToZombie < dist2ToZombie ) return false;
			}
		}	
		// don't rush if you can't see your archon
		if (!canSeeArchon) return false;
		return true;

	}

	private static boolean shouldKamikaze(RobotInfo[] enemies, RobotInfo[] zombies) throws GameActionException
	{
		// don't rush if no enemies
		if (enemies.length == 0) return false;
		// don't rush if no zombies
		if (zombies.length == 0) return false;
		else return true;
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
		// go towards closest Zombie
		RobotInfo[] zombies = Micro.getNearbyZombies();
		
		// got towards zombie center of mass
		//MapLocation zCOM = Micro.getZombieCOM();
		RobotInfo closestZombie = Micro.getClosestUnitTo(zombies, here);
		if (closestZombie !=null)
		{
			if (!Behavior.tryRetreatTowards(closestZombie.location, Micro.getSafeMoveDirs()))
			{
				Behavior.tryDirectMove(here.directionTo(closestZombie.location));
			}
		}
	}
	
	private static void doScoutHerding() throws GameActionException
	{
		int rud = Micro.getRoundsUntilDanger();
		Direction herdingDir = here.directionTo(herdingDestLoc);
		//Direction perpDir = herdingDir.rotateRight().rotateRight();
	
		if (herdingDir == null) return;
		
		// if there's a unit in your way, bug! It might be a neutral or zombie den
		if (rc.isLocationOccupied(here.add(herdingDir)))
		{
			Nav.tryGoTo(herdingDestLoc, Micro.getCanMoveDirs());
		}
		
		// if you are going off the edge of the map, rotate right and keep going!
		// check 3 squares ahead to avoid corners
		MapLocation lookAhead = here.add(herdingDir);
		if (!rc.onTheMap(lookAhead.add(herdingDir)))
		{
			Direction newDir = getMapEdgeDir(lookAhead,herdingDir);
			Debug.setStringAK("going off the map need to rotate!");
			if (newDir !=null)
			{
				herdingDestLoc = here.add(newDir.dx*herdDist, newDir.dy*herdDist);	
			}
		}
		
		
		
		if (rud > 2)
			{
			Clock.yield();// wait if not in immediate danger;
			}
		else if (!Behavior.tryRetreatTowards(herdingDestLoc, Micro.getSafeMoveDirs().and(Micro.getTurretSafeDirs())))
				{
				Behavior.tryDirectMove(here.directionTo(herdingDestLoc));
				}
	}
	
	public static Direction getMapEdgeDir(MapLocation loc, Direction dir) throws GameActionException
	{
		// Tries the direction given in order of how close to the direction it I want to go.
		// Doesn't move directly back
		if (dir !=null)
		{
			for (int i=0;i<7;i++)
			{
			dir = dir.rotateRight();
			if (rc.onTheMap(loc.add(dir))) return dir;
			}
		}
		return null;
	}
	
	private static void doScoutKamikaze() throws GameActionException
	{
		RobotInfo[] zombies = Micro.getNearbyZombies();
		RobotInfo[] allies = Micro.getNearbyAllies();
		RobotInfo[] enemies = Micro.getNearbyEnemies();
		
		if (enemies.length != 0)
		{
			RobotInfo closestEnemy = Micro.getClosestUnitTo(enemies, here);
			if (!Behavior.tryRetreatTowards(closestEnemy.location, Micro.getSafeMoveDirs()))
			{
				Behavior.tryDirectMove(here.directionTo(closestEnemy.location));
			}
		}
	}
}
