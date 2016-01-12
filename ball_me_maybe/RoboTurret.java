package ball_me_maybe;

import battlecode.common.*;



public class RoboTurret extends RobotPlayer
{
	static MapLocation unpackDest = null;
	//static final int[] unpackSearchTableX = {-1,1,1,-1,-2,0,2,0,-2,-2,2,2,1,-1,-3,-3,-1,1};
	//static final int[] unpackSearchTableY = {1, 1, -1, -1, 0, 2, 0 ,-2,-2,2,2,-2,-3,-3,-1,1,3,3};
	
	static int lastFiredRound = 0;
	static int lastUnpackRound = 0;
	static int lastMovedRound = 0;
	static int lastPackRound = 0;
	static final int TURRET_PACK_DELAY = 15;

	// AK: unpack search in this pattern
	// [][]18[]18[][]
	// []10[]06[]11[]
	// 16[]01[]02[]19
	// []05[]XX[]07[]
	// 15[]04[]03[]20
	// []09[]08[]12[]
	// [][]14[]13[][]
	
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
		int roundSinceLastPack = rc.getRoundNum() - lastPackRound;
		if (roundSinceLastPack < TURRET_PACK_DELAY)
			return;

		if (!Behavior.tryAttackSomeone())
		{
			rc.pack();
			lastPackRound = rc.getRoundNum();
			return;
		}	
	}

	public static void turnTTM() throws GameActionException
	{
		//mandatory wait in ttm time
		int roundSinceLastUnpack = rc.getRoundNum() - lastUnpackRound;
		if (roundSinceLastUnpack < TURRET_PACK_DELAY)
			return;
		
		//unpack if we see any enemies unpack and turn into a turret
		MapLocation closestSighted = Sighting.getClosestSightedTarget();
		RobotInfo[] hostilesSeen   = Micro.getNearbyHostiles();
		if (closestSighted != null 
				&& here.distanceSquaredTo(closestSighted) <= rc.getType().attackRadiusSquared
				&& hostilesSeen.length !=0)
		{
			rc.unpack();
			lastUnpackRound = rc.getRoundNum();
			return;
		}

		//try to move wherever everyone else is going
		RobotInfo[] allies = Micro.getNearbyAllies();
		MapLocation[] locs = BallMove.updateBallDests(allies); // updates archon and archon destination using messaging
		MapLocation archonLoc = locs[0];
		MapLocation destLoc = locs[1];
		if (rc.isCoreReady())	
		{
			BallMove.ballMove(archonLoc, destLoc, allies);
		}
	}
}

