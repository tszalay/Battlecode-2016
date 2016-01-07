package botline_bling;

import battlecode.common.*;

import java.util.*;

public class RoboScout extends RobotPlayer
{
	public static final int SIGNAL_ROUND = 30; 
	public static MapLocation kiteTarget = null;
	public static boolean isFreeScout = false;
	
	public static void init() throws GameActionException
	{
		// first-spawn scout, send the message right away
		// to a distance of 100 units
		if (rc.getRoundNum() < SIGNAL_ROUND)
		{
			if (myArchon != null)
			{
				Message.sendMessageSignal(10000, MessageType.SPAWN, myArchon.location);
			}
		}
		kiteTarget = here.add(rand.nextInt(200)-100,rand.nextInt(200)-100);
		
		// =========================================================
		// for now, a hacked way of deciding if you are a free scout
		Micro.updateAllies();
		int numOtherScouts = 0;
		for (RobotInfo bot : Micro.nearbyAllies)
		{
			if (bot.type == RobotType.SCOUT)
				numOtherScouts += 1;
		}
		if (numOtherScouts >= 1)
			isFreeScout = true;
		// =========================================================
			
	}
	
	public static void doFreeScout() throws GameActionException
	{
		// signal any interesting information about enemies, zombies, dens
		
		// pick a target
		
		// move toward target
		
	}
	
	public static void doTurtleScout() throws GameActionException
	{
		// check for attackers and signal
		
		// go to edge of turtle
		
	}
	
	public static void turn() throws GameActionException
	{
        if (rc.isCoreReady())
        {
        	if (!Micro.tryAvoidBeingKilled()) // avoidance micro
        	{
	        	if (isFreeScout)
	        		doFreeScout();
	        	else
	        		doTurtleScout();
        	}
        }
	}
}
