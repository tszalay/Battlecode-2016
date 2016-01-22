package blitzkrieg_bot;

import battlecode.common.*;
import java.util.*;

public class Sighting extends RobotPlayer
{
	// nearby sighted targets and turrets
	public static FastLocSet enemySightedTargets = new FastLocSet();
	public static FastLocSet enemySightedTurrets = new FastLocSet();
	
	public static int lastSightingBroadcastRound = 0;
	public static int lastFriendlyBroadcastRound = 0;
	
	public static final int SIGHT_MESSAGE_RADIUS = 63;
	public static final int TURRET_MESSAGE_RADIUS = 255;
	public static final int FRIENDLY_MESSAGE_RADIUS = 1500;
	public static final int TURRET_TIMEOUT_ROUNDS = 200;
	public static final int BROADCAST_DELAY = 3;
	public static final int FRIENDLY_DELAY = 50;
	
	// to be called by scouts and archons
	public static void doSendSightingMessage() throws GameActionException
	{
		if (roundsSince(lastSightingBroadcastRound) < BROADCAST_DELAY)
			return;
		
		if (Micro.getRoundsUntilDanger() < 5)
			return;
		
		RobotInfo bestTarget = null;
		
		// prioritize the target with the biggest attack radius
		for (RobotInfo ri : Micro.getNearbyHostiles())
		{
			if (bestTarget == null || ri.type.attackRadiusSquared > bestTarget.type.attackRadiusSquared)
				bestTarget = ri;
		}
		
		// leave off zombie dens because we already have a list
		if (bestTarget != null && bestTarget.type != RobotType.ZOMBIEDEN)
		{
			lastSightingBroadcastRound = rc.getRoundNum();
			if (bestTarget.type == RobotType.TURRET)
				Message.sendMessageSignal(TURRET_MESSAGE_RADIUS,Message.Type.SIGHT_TARGET,bestTarget.location);
			else
				Message.sendMessageSignal(SIGHT_MESSAGE_RADIUS,Message.Type.SIGHT_TARGET,bestTarget.location);
		}
		
		trySendFriendlyMessage();
	}

	// send a periodic beacon of places with clumps of allied forces
	public static void trySendFriendlyMessage() throws GameActionException
	{
		if (rc.getCoreDelay() > 3)
			return;
		
		if (Micro.getNearbyAllies().length > 8 && roundsSince(lastFriendlyBroadcastRound) > FRIENDLY_DELAY)
		{
			lastFriendlyBroadcastRound = rc.getRoundNum();
			Message.sendMessageSignal(FRIENDLY_MESSAGE_RADIUS, Message.Type.LOTSA_FRIENDLIES, Micro.getNearbyAllies().length);
		}
	}
	
	public static void addSightedTarget(MapLocation loc, RobotType target, int round)
	{
		if (target == RobotType.TURRET)
			enemySightedTurrets.add(loc, round);
		else
			enemySightedTargets.add(loc, round);
	}
	
	public static MapLocation getClosestSightedTarget()
	{
		int closestSq = 1000;
		MapLocation closest = null;
		
		for (MapLocation ml : enemySightedTargets.elements())
		{
			if (here.distanceSquaredTo(ml) < closestSq)
			{
				closest = ml;
				closestSq = here.distanceSquaredTo(ml);
			}
		}
		
		// clear it because all sighting messages are assumed to have timed out by this point
		enemySightedTargets.clear();
		
		return closest;
	}
	
	public static DirectionSet getTurretSafeDirs()
	{
		// find out which directions are safe vis-a-vis enemy turrets
		DirectionSet dirs = DirectionSet.makeAll();
		
		int curround = rc.getRoundNum();
		
		for (MapLocation ml : enemySightedTurrets.elements())
		{
			// is there any chance we're close to turret, and is it maybe still there?
			if (here.distanceSquaredTo(ml) < RobotType.SCOUT.sensorRadiusSquared)
			{
				if (curround-enemySightedTurrets.get(ml) < TURRET_TIMEOUT_ROUNDS)
				{
					// loop through and remove directions that are still safe
					for (Direction d : Direction.values())
					{ 
						if (d != Direction.OMNI && here.add(d).distanceSquaredTo(ml) <= RobotType.TURRET.attackRadiusSquared)
							dirs.remove(d);
					}
				}
				else
				{
					enemySightedTurrets.remove(ml);
				}
			}
		}
				
		return dirs;
	}
}
