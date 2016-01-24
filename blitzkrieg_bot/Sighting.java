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
	public static final int FRIENDLY_MESSAGE_RADIUS = 2000;
	public static final int TURRET_TIMEOUT_ROUNDS = 500;
	public static final int FRIENDLY_DELAY = 50;
	
	// every 25 rounds we can send a signal really far
	private static SignalDelay farBroadcastSignal = new SignalDelay(25);
	
	// to be called by scouts and archons
	public static void doSendSightingMessage() throws GameActionException
	{
		RobotInfo bestHostile = null;
		ArrayList<RobotInfo> nearbyTurrets = new ArrayList<RobotInfo>(5);
		
		// prioritize the target with the biggest attack radius
		for (RobotInfo ri : Micro.getNearbyHostiles())
		{
			if (ri.type == RobotType.TURRET)
			{
				nearbyTurrets.add(ri);
			}
			else
			{
				if (bestHostile == null || ri.type.attackRadiusSquared > bestHostile.type.attackRadiusSquared)
					bestHostile = ri;
			}
		}
		
		// leave off zombie dens because we already have a list
		if (bestHostile != null)
		{
			MapLocation targetloc = MapInfo.nullLocation;
			if (bestHostile != null && bestHostile.type != RobotType.ZOMBIEDEN)
				targetloc = bestHostile.location;
			// get a random turret from our list
			MapLocation turretloc = (nearbyTurrets.size() > 0) ? nearbyTurrets.get(rand.nextInt(nearbyTurrets.size())).location : MapInfo.nullLocation;
			
			lastSightingBroadcastRound = rc.getRoundNum();
			int broadcast_dist = 63;
			if (Micro.getRoundsUntilDanger() < 5 && Micro.getRoundsUntilDanger() > 0)
				broadcast_dist = 121;
			if (Micro.getRoundsUntilDanger() < 10)
				broadcast_dist = 400;
			if (Micro.getRoundsUntilDanger() > 12 && farBroadcastSignal.canSend())
				broadcast_dist = MapInfo.fullMapDistanceSq();
			
			// also count up nearby combat units to set value
			UnitCounts units = Micro.getEnemyUnits();
			int val = 100*units.Archons + units.Guards + units.Soldiers + 3*units.TurrTTMs + units.Vipers;
			
			Message.sendMessageSignal(broadcast_dist,Message.Type.SIGHT_TARGET,targetloc,turretloc,val);
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
	
	public static void addSightedTarget(MapLocation sight_loc, MapLocation turret_loc)
	{
		// only use sighting target list if we're a turret
		if (sight_loc != null && (rc.getType() == RobotType.TURRET || rc.getType() == RobotType.TTM))
			enemySightedTargets.add(sight_loc);
		// but everyone has to know about turrets, kinda importante
		if (turret_loc != null)
			enemySightedTurrets.addOrSet(turret_loc, rc.getRoundNum());
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
	
	public static MapLocation getClosestTurret()
	{
		return Micro.getClosestLocationTo(enemySightedTurrets.elements(), here);
	}
	
	public static DirectionSet getTurretSafeDirs()
	{
		// find out which directions are safe vis-a-vis enemy turrets
		DirectionSet dirs = DirectionSet.makeAll();
		
		/*if (Debug.DISPLAY_DEBUG && rc.getHealth() < 30)
		{
			for (MapLocation ml : enemySightedTurrets.elements())
				rc.setIndicatorLine(here, ml, 0, 255, 255);
		}*/
		
		// any turrets need removin'?
		MapLocation remove_turret = null;
		
		for (MapLocation ml : enemySightedTurrets.elements())
		{
			// is there any chance we're close to turret, and is it maybe still there?
			if (here.distanceSquaredTo(ml) < 81)
			{
				// loop through and remove directions that are still safe
				for (Direction d : Direction.values())
				{ 
					if (d != Direction.OMNI && here.add(d).distanceSquaredTo(ml) <= RobotType.SCOUT.sensorRadiusSquared)
						dirs.remove(d);
				}
				if (roundsSince(enemySightedTurrets.get(ml)) > TURRET_TIMEOUT_ROUNDS)
					remove_turret = ml;
			}
		}
		
		if (remove_turret != null)
			enemySightedTurrets.remove(remove_turret);
		
		// hack, since scouts can see turrets, duh
		if (rc.getType() == RobotType.SCOUT)
			dirs = DirectionSet.makeAll();
				
		return dirs;
	}
}
