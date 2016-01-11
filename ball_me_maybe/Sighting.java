package ball_me_maybe;

import battlecode.common.*;
import java.util.*;

public class Sighting extends RobotPlayer
{
	// nearby sighted targets and turrets
	public static FastLocSet enemySightedTargets = new FastLocSet();
	public static FastLocSet enemySightedTurrets = new FastLocSet();
	
	public static final int SIGHT_MESSAGE_RADIUS = 63;
	
	// to be called by scouts and archons
	public static void doSendSightingMessages() throws GameActionException
	{
		RobotInfo bestTarget = null;
		
		// prioritize the target with the biggest attack radius
		for (RobotInfo ri : Micro.getNearbyHostiles())
		{
			if (bestTarget == null || ri.type.attackRadiusSquared > bestTarget.type.attackRadiusSquared)
				bestTarget = ri;
		}
		
		if (bestTarget != null)
		{
			Message.sendMessageSignal(SIGHT_MESSAGE_RADIUS,MessageType.SIGHT_TARGET,bestTarget.location);
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
}
