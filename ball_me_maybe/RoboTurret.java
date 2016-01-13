package ball_me_maybe;

import battlecode.common.*;



public class RoboTurret extends RobotPlayer
{
	// last round that a turret performed an action
	// (unpacking or firing)
	static int lastTurretRound = 0;

	static int lastPackRound = 0;
	static int lastMovedRound = 0;
	
	static final int PACK_DELAY = 20;

	public static void init() throws GameActionException
	{
	}
	
	public static void turn() throws GameActionException
	{
		if (rc.getType() == RobotType.TURRET)
		{
			turnTurret();
			return;
		}
		else
		{
			turnTTM();
			return;
		}
	}
	
	public static void turnTurret() throws GameActionException
	{
//		int roundSinceLastPack = rc.getRoundNum() - lastPackRound;
//		if (roundSinceLastPack < TURRET_PACK_DELAY)
//			return;
		
		if (Behavior.tryAttackSomeone())
		{
			lastTurretRound = rc.getRoundNum();
			return;
		}
		
		// if we fired recently, don't pack
		if (rc.getRoundNum() - lastTurretRound < PACK_DELAY)
			return;
		
		rc.pack();
		lastPackRound = rc.getRoundNum();
	}

	public static void turnTTM() throws GameActionException
	{
		//mandatory wait in ttm time
		//int roundSinceLastUnpack = rc.getRoundNum() - lastUnpackRound;
		//if (roundSinceLastUnpack < GameConstants.TURRET_TRANSFORM_DELAY)
		//	return;
		
		//unpack if we see any enemies unpack and turn into a turret
		
		if (shouldUnpack() && rc.isCoreReady())
		{
			rc.unpack();
			lastTurretRound = rc.getRoundNum();
			return;
		}
		
		//try to move wherever everyone else is going
		BallMove.ballMove(7,35);
	}
	
	public static boolean shouldUnpack() throws GameActionException
	{
		// don't unpack with hostiles too close
//		int numRoundsUntilDanger = Micro.getRoundsUntilDanger();
//		
//		if (numRoundsUntilDanger < TURRET_PACK_DELAY)
//			return false;
		
		RobotInfo[] hostiles = Micro.getNearbyHostiles();
		RobotInfo closestHostile = Micro.getClosestUnitTo(hostiles, here);
		
//		if (closestHostile != null && closestHostile.location.distanceSquaredTo(here) <= closestHostile.type.attackRadiusSquared)
//			return false;
//		else
//			return true;
		
		if (closestHostile != null)
			return true;
		else
			return false;
		
		//MapLocation closestSighted = Sighting.getClosestSightedTarget();
		
		//return (closestSighted != null && here.distanceSquaredTo(closestSighted) <= rc.getType().attackRadiusSquared);
	}
}

