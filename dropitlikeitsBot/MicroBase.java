package dropitlikeitsBot;

import battlecode.common.*;

import java.util.*;

public class MicroBase extends RobotPlayer
{
	private RobotInfo[] nearbyEnemies = null;
	private RobotInfo[] nearbyZombies = null;
	private RobotInfo[] nearbyHostiles = null;
	private RobotInfo[] nearbyAllies = null;
	
	private DirectionSet canMoveDirs = null;
	private DirectionSet safeMoveDirs = null;
	private DirectionSet noPartsDirs = null;
	private DirectionSet turretSafeDirs = null;
	
	private static final int DIST_MAX = 1000;
	private int[] distToClosestHostile = null;
	
	private Direction bestRetreatDirection = null;
	
	private int roundsUntilDanger = -1;
	
	
	public RobotInfo[] getNearbyEnemies()
	{
		if (nearbyEnemies != null)
			return nearbyEnemies;
		
		nearbyEnemies = rc.senseNearbyRobots(rc.getType().sensorRadiusSquared, theirTeam);
		return nearbyEnemies;
	}
	
	public RobotInfo[] getNearbyAllies()
	{
		if (nearbyAllies != null)
			return nearbyAllies;
		
		nearbyAllies = rc.senseNearbyRobots(rc.getType().sensorRadiusSquared, ourTeam);
		return nearbyAllies;
	}
	
	public RobotInfo[] getNearbyZombies()
	{
		if (nearbyZombies != null)
			return nearbyZombies;
		
		nearbyZombies = rc.senseNearbyRobots(rc.getType().sensorRadiusSquared, Team.ZOMBIE);
		return nearbyZombies;
	}
	
	public RobotInfo[] getNearbyHostiles()
	{
		if (nearbyHostiles != null)
			return nearbyHostiles;
		
		nearbyHostiles = rc.senseHostileRobots(here, rc.getType().sensorRadiusSquared);
		return nearbyHostiles;
	}
		
	// this gets called by other functions to compile various stats and info
	// before they return the computed values
	private void computeSafetyStats()
	{
		// already done
		if (distToClosestHostile != null)
			return;
		
		RobotInfo[] nearby = getNearbyHostiles();
		
		// this is used for best retreat direction calculation
		distToClosestHostile = new int[9];
		
		safeMoveDirs = new DirectionSet();
		noPartsDirs = new DirectionSet();

		turretSafeDirs = Sighting.getTurretSafeDirs();
		
		// only check directions we can actually move
		for (Direction d : getCanMoveDirs().getDirections())
		{
			MapLocation testloc = here.add(d);
			
			int closestDistSq = DIST_MAX;
			boolean isThisSquareSafe = true;
			
			for (RobotInfo ri : nearby)
			{
				if (ri.attackPower == 0)
					continue;
				
				int distSq = testloc.distanceSquaredTo(ri.location);
				if (distSq < closestDistSq)
					closestDistSq = distSq;

				if (distSq <= ri.type.attackRadiusSquared)
				{
					isThisSquareSafe = false;
					if (ri.type == RobotType.TURRET)
						turretSafeDirs.remove(d);
				}

			}
			
			distToClosestHostile[d.ordinal()] = closestDistSq;
			
			if (isThisSquareSafe)
				safeMoveDirs.add(d);
			
			// keep track of directions without parts
			if (rc.senseParts(here.add(d)) == 0)
				noPartsDirs.add(d);
		}
	}
	
	// get number of rounds until we are in danger
	public int getRoundsUntilDanger()
	{
		if (roundsUntilDanger >= 0)
			return roundsUntilDanger;
		
		roundsUntilDanger = 100;
		
		for (RobotInfo ri : getNearbyHostiles())
		{
			int dangerTime = 100;
			
			switch (ri.type)
			{
			// noncombatants
			case ZOMBIEDEN:
			case ARCHON:
			case SCOUT:
				break;
				
			case TTM:
				dangerTime = (int) Math.floor(ri.coreDelay) + GameConstants.TURRET_TRANSFORM_DELAY;
				break;
				
			// ranged units
			case SOLDIER:
			case RANGEDZOMBIE:
			case VIPER:
				// time to shoot us is rounds until
				// coreDelay + cooldownDelay
				MapLocation closer = ri.location;
				double dangerAccum = 0;
				while (closer.distanceSquaredTo(here) > ri.type.attackRadiusSquared)
				{
					Direction d = closer.directionTo(here);
					closer = closer.add(d);
					dangerAccum += ri.type.movementDelay*(d.isDiagonal() ? 1.4 : 1.0);
				}
				
				// if they have to move, include cooldown and current core
				if (dangerAccum > 0)
					dangerAccum += ri.type.cooldownDelay + ri.coreDelay - ri.type.movementDelay;
				else // otherwise just weapon delay
					dangerAccum += ri.weaponDelay;
				
				dangerTime = (int)dangerAccum;
				break;
				
			// unranged units
			case BIGZOMBIE:
			case FASTZOMBIE:
			case GUARD:
			case STANDARDZOMBIE:
				// move one square closer to enemy
				// the square enemy needs to get to to attack us
				MapLocation onecloser = here.add(here.directionTo(ri.location));
				int dx = Math.abs(onecloser.x-ri.location.x);
				int dy = Math.abs(onecloser.y-ri.location.y);

				// number of longest straight steps + 1.4 * number of diagonal steps
				// scaling of movement delay to get here
				double effDistanceTo = Math.min(dx, dy)*0.4 + Math.max(dx, dy); // verified
				
				dangerTime = (int)Math.floor(ri.coreDelay + ri.type.movementDelay*(effDistanceTo-1) + ri.type.cooldownDelay);
				
				break;
				
			case TURRET:
				if (rc.getType() != RobotType.SCOUT)
					dangerTime = (int)Math.floor(ri.weaponDelay);
				break;
			}
			
			if (dangerTime < roundsUntilDanger)
			{
				roundsUntilDanger = dangerTime;
				bestRetreatDirection = ri.location.directionTo(here);
			}
		}
		
		if (roundsUntilDanger < 0)
			roundsUntilDanger = 0;

		return roundsUntilDanger;
	}
	
	// how many rounds until we can shoot and move at once
	public int getRoundsUntilShootAndMove()
	{
		double ourDelayDecrement = 1;
		return (int)Math.floor( Math.max( (rc.getWeaponDelay() + rc.getType().cooldownDelay), rc.getCoreDelay()) / ourDelayDecrement);
	}
	
	// compute which direction moves us the farthest from the closest enemy
	// (trying to use safe squares first)
	public Direction getBestEscapeDir()
	{
		DirectionSet dirs = getSafeMoveDirs();
		
		// if there are no safe moves, just do something
		if (!dirs.any())
		{
			// first, check if there are any turret safe moves
			dirs = getCanMoveDirs().clone();
			dirs.remove(Direction.NONE);
		}
		
		if (!dirs.any())
			return Direction.NONE;
		
		Direction bestDir = null;
		int distToClosest = 0;
		
		for (Direction d : dirs.getDirections())
		{
			if (distToClosestHostile[d.ordinal()] > distToClosest)
			{
				distToClosest = distToClosestHostile[d.ordinal()];
				bestDir = d;
			}
		}
		
		if (distToClosest == DIST_MAX)
			return null;
		
		return bestDir;
	}
	
	public Direction getBestRetreatDir()
	{
		// force recomputing of this
		getRoundsUntilDanger();
		
		return bestRetreatDirection;
	}

	public DirectionSet getTurretSafeDirs()
	{
		computeSafetyStats();
		return turretSafeDirs;
	}

	public DirectionSet getNoPartsDirs()
	{
		computeSafetyStats();
		return noPartsDirs;
	}
	
	public DirectionSet getSafeMoveDirs()
	{
		computeSafetyStats();
		return safeMoveDirs;
	}
	
	// get directions we can move in
	public DirectionSet getCanMoveDirs()
	{
		if (canMoveDirs != null)
			return canMoveDirs;
		
		canMoveDirs = DirectionSet.makeStay();
		
		for (Direction d : Direction.values())
			if (rc.canMove(d))
				canMoveDirs.add(d);
		
		return canMoveDirs;
	}
	
	public DirectionSet getCanFastMoveDirs() throws GameActionException
	{
		// returns moves without any rubble
		// (except for scout who cares)
		
		// also we don't need to save it
		
		if (rc.getType() == RobotType.SCOUT)
			return getCanMoveDirs();

		DirectionSet dirs = new DirectionSet();
		
		if (rc.senseRubble(here) < GameConstants.RUBBLE_SLOW_THRESH)
			dirs.add(Direction.NONE);
		
		for (Direction d : Direction.values())
			if (rc.canMove(d) && rc.senseRubble(here.add(d)) < GameConstants.RUBBLE_SLOW_THRESH)
				dirs.add(d);
		
		return dirs;
	}
	
	public boolean isInDanger()
	{
		return (getRoundsUntilDanger() < 10);
	}
	
	// tryMove expects to be given a valid direction
	public boolean tryMove(Direction d) throws GameActionException
	{
		// don't do anything, but don't throw error, this is ok
		if (d == Direction.NONE)
		{
			//System.out.println("Given a NONE!");
			return false;
		}
		
		// double check!
		if (d != null && rc.canMove(d) && rc.isCoreReady())
		{
			rc.move(d);
			return true;
		}
//		else
//		{
//			System.out.println("Movement exception: tried to move but couldn't!");
//			if (d == null)
//			{
//				System.out.println("Reason: null direction");
//				return false;
//			}
//			if (!rc.isCoreReady())
//				System.out.println("Reason: core not ready");
//			if (rc.isLocationOccupied(here.add(d)))
//				System.out.println("Reason: location occupied");
//			if (rc.senseRubble(here.add(d)) > GameConstants.RUBBLE_OBSTRUCTION_THRESH)
//				System.out.println("Reason: too much rubble");
//			
//			return false;
//		}
		return false;
	}
	
	public static RobotInfo getClosestTurretTarget(RobotInfo[] nearby, MapLocation loc)
	{
		int minsqdist = 1000;
		RobotInfo closest = null;
		
		for (RobotInfo ri : nearby)
		{
			int dist = ri.location.distanceSquaredTo(loc);
			
			if (dist < minsqdist && dist > 5)
			{
				closest = ri;
				minsqdist = dist;
			}
		}
		
		return closest;
	}
	
	public MapLocation getAllyCOM()
	{
		return getUnitCOM(this.getNearbyAllies());
	}
	
	public MapLocation getEnemyCOM()
	{
		return getUnitCOM(this.getNearbyEnemies());
	}
	
	public MapLocation getZombieCOM()
	{
		return getUnitCOM(this.getNearbyZombies());
	}
	public MapLocation getUnitCOM(RobotInfo[] nearby)
	{
		int xtot = 0;
		int ytot = 0;
		
		if (nearby.length == 0)
			return here;
		
		for (RobotInfo ri : nearby)
		{
			xtot += ri.location.x;
			ytot += ri.location.y;
		}
		
		xtot /= nearby.length;
		ytot /= nearby.length;
		
		return new MapLocation(xtot,ytot);
	}
	
	public RobotInfo getClosestUnitTo(RobotInfo[] nearby, MapLocation loc)
	{
		if (nearby.length == 0)
			return null;
		
		RobotInfo closest = null;
		int closestDistSq = 1000;
		
		for (RobotInfo ri : nearby)
		{
			int distSq = loc.distanceSquaredTo(ri.location);
			if (distSq < closestDistSq)
			{
				closest = ri;
				closestDistSq = distSq;
			}
		}
		
		return closest;
	}
	
	public MapLocation getClosestLocationTo(List<MapLocation> locs, MapLocation center)
	{
		int minDistSq = 1000000;
		MapLocation closest = null;
		
		for (MapLocation ml : locs)
		{
			int dsq = ml.distanceSquaredTo(center);
			if (dsq < minDistSq)
			{
				minDistSq = dsq;
				closest = ml;
			}
		}
		
		return closest;
	}
	
	public RobotInfo getLowestHealth(RobotInfo[] bots)
	{
		RobotInfo target = null;
		
		for (RobotInfo ri : bots)
		{
			if (target == null || ri.health < target.health)
				target = ri;
			if (ri.type == RobotType.ARCHON)
			{
				target = ri;
				break; // archon targets take priority over ALL
			}
		}
		
		return target;
	}

	
//	public boolean canWin1v1(RobotInfo enemy) throws GameActionException
//	{
//		MapLocation onecloser = here.add(here.directionTo(enemy.location));
//		
//		boolean iAmAlive = true;
//		boolean theyAreAlive = false;
//		
//		MapLocation myLoc = here;
//		MapLocation enemyLoc = enemy.location;
//		
//		double myCoreDelay = rc.getCoreDelay();
//		double theirCoreDelay = enemy.coreDelay;
//		double myWeaponDelay = rc.getWeaponDelay();
//		double theirWeaponDelay = enemy.weaponDelay;
//		
//		while (iAmAlive && theyAreAlive)
//		{
//			// me
//			if (myCoreDelay < 1)
//				myLoc
//			
//			// enemy
//			
//			
//		}
//		
//		int numRoundsTillIKillThem = ;
//		int numRoundsTillTheyKillMe = ;
//		
//		if (numRoundsTillIKillThem < numRoundsTillTheyKillMe)
//			return true;
//		
//		return false;
//	}
}