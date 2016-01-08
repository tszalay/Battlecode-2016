package botline_bling;

import battlecode.common.*;

import java.util.*;

public class RoboScout extends RobotPlayer
{
	public static final int SIGNAL_ROUND = 30; 
	public static MapLocation target = null;
	public static boolean isFreeScout = false;
	public static MapLocation rally = null;
	
	public static void init() throws GameActionException
	{
		// first-spawn scout, send the message right away
		// to a distance of 100 units
		if (rc.getRoundNum() < SIGNAL_ROUND)
		{
			if (myArchon != null)
			{
				Message.sendMessageSignal(100, MessageType.SPAWN, myArchon.location);
			}
		}
		target = here.add(rand.nextInt(200)-100,rand.nextInt(200)-100);
		
//		// =========================================================
//		// for now, a hacked way of deciding if you are a free scout
//		Micro.updateAllies();
//		int numOtherScouts = 0;
//		for (RobotInfo bot : Micro.nearbyAllies)
//		{
//			if (bot.type == RobotType.SCOUT)
//				numOtherScouts += 1;
//		}
//		if (numOtherScouts >= 1)
//			isFreeScout = true;
//		// =========================================================
		isFreeScout = false;
		
		rally = here.add(0,1);
			
	}
	
	public static void doFreeScout() throws GameActionException
	{
		// signal any interesting information about enemies, zombies, dens
		
		// pick a target
		
		// move toward target
		
	}
	
	public static void doTurtleScout() throws GameActionException
	{
		// go to edge of turtle by moving away from rally location but staying in turtle
		NavSafetyPolicy safety = new SafetyPolicyAvoidAllUnits();
		//if (getNumTurretsNearby(here, 6) > 11 || getNumTurretsNearby(here, 3) < 3) // we're not at the edge
			//Nav.goTo(here.add(here.directionTo(rally).opposite()), safety); // move only if we're not at the edge
		Nav.goTo(MapUtil.findClosestTurtle(), safety);
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
	
	public static boolean sendSightedTarget(RobotInfo[] nearbyTargets) throws GameActionException
	{
		RobotInfo closestTarget = null;
		
		// prioritize the target with the biggest attack radius
		for (RobotInfo ri : nearbyTargets)
		{
			if (closestTarget == null || ri.type.attackRadiusSquared > closestTarget.type.attackRadiusSquared)
				closestTarget = ri;
		}
		
		if (closestTarget != null)
		{
			switch (closestTarget.type)
			{
				case TURRET:
					Message.sendMessageSignal(7,MessageType.ENEMY_TURRET,closestTarget.location);
					break;
				case ZOMBIEDEN:
					Message.sendMessageSignal(7,MessageType.ZOMBIE_DEN,closestTarget.location);
					break;
				default:
					Message.sendMessageSignal(7,MessageType.SIGHT_TARGET,closestTarget.location);
					break;
			}
			return true;
		}
		
		return false;
	}
	
	public static void turn() throws GameActionException
	{
        // send out info about sighted targets
		sendSightedTarget(rc.senseHostileRobots(here, RobotType.SCOUT.sensorRadiusSquared));
		
		if (rc.isCoreReady())
        {
        	if (!Micro.tryAvoidBeingShot()) // avoidance micro
        	{
	        	if (isFreeScout)
	        		doFreeScout();
	        	else
	        		doTurtleScout();
        	}
        	else
        		Debug.setStringSJF("retreating");
        }
	}
}
