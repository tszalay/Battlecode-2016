package ball_me_maybe;

import battlecode.common.*;

import java.util.*;

public class MicroBase extends RobotPlayer
{
	private RobotInfo[] nearbyEnemies = null;
	private RobotInfo[] nearbyZombies = null;
	private RobotInfo[] nearbyHostiles = null;
	private RobotInfo[] nearbyAllies = null;
	
	private MapLocation[] sightedHostileLocs = null;
	private MapLocation[] sightedTurretLocs = null;
	
	private DirectionSet canMoveDirs = null;
	private DirectionSet safeMoveDirs = null;
	private DirectionSet noTurretMoveDirs = null;
	
	private int[] distToClosestHostile = null;
	
	private int roundsUntilDanger = -1;
	
	public MicroBase()
	{
	}
	
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
	
	public MapLocation[] getSightedHostileLocs()
	{
		return null;
		
		/*if (sightedHostileLocs != null)
			return sightedHostileLocs;
		
		// get this information from Message, from scout sighting
		ArrayList<SignalLocation> sightedHostileSigLocs = Message.sightLocs;
		
		if (sightedHostileSigLocs == null || sightedHostileSigLocs.size() == 0)
		{
			sightedHostileLocs = null;
		}
		else
		{
			sightedHostileLocs = new MapLocation[sightedHostileSigLocs.size()];
			for (int i = 0; i < sightedHostileSigLocs.size(); i ++)
			{
				sightedHostileLocs[i] = sightedHostileSigLocs.get(i).loc;
			}
		}
		
		return sightedHostileLocs;*/
	}
	
	public MapLocation[] getSightedTurretLocs()
	{
		if (sightedTurretLocs != null)
			return sightedTurretLocs;
		
		// get this information from Message, from scout sighting
		ArrayList<SignalLocation> sightedTurretSigLocs = Message.enemyTurretLocs;
		
		if (sightedTurretSigLocs == null || sightedTurretSigLocs.size() == 0)
		{
			sightedTurretLocs = new MapLocation[0];
		}
		else
		{
			sightedTurretLocs = new MapLocation[sightedTurretSigLocs.size()];
			for (int i = 0; i < sightedTurretSigLocs.size(); i ++)
			{
				sightedTurretLocs[i] = sightedTurretSigLocs.get(i).loc;
			}
		}
		
		return sightedTurretLocs;
	}
	
	public RobotInfo getLowestHealth(RobotInfo[] bots)
	{
		RobotInfo target = null;
		
		for (RobotInfo ri : bots)
		{
			if (target == null || ri.health < target.health)
				target = ri;
		}
		
		return target;
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
		noTurretMoveDirs = new DirectionSet();

		// only check directions we can actually move
		for (Direction d : getCanMoveDirs().getDirections())
		{
			MapLocation testloc = here.add(d);
			
			int closestDistSq = 1000;
			boolean isThisSquareSafe = true;
			boolean isThisSquareTurretSafe = true;
			
			for (RobotInfo ri : nearby)
			{
				int distSq = testloc.distanceSquaredTo(ri.location);
				if (distSq < closestDistSq)
					closestDistSq = distSq;

				if (distSq <= ri.type.attackRadiusSquared)
				{
					isThisSquareSafe = false;
					if (ri.type == RobotType.TURRET)
						isThisSquareTurretSafe = false;
				}

			}
			
			/*// additionally check if squares are turret safe by looking at sighted enemy turrets
			sightedTurretLocs = getSightedTurretLocs();
			if (sightedTurretLocs != null && sightedTurretLocs.length > 0)
			{
				for (MapLocation turretLoc : sightedTurretLocs)
				{
					if (testloc.distanceSquaredTo(turretLoc) <= RobotType.TURRET.attackRadiusSquared)
						isThisSquareTurretSafe = false;
				}
			}*/
			
			distToClosestHostile[d.ordinal()] = closestDistSq;
			
			if (isThisSquareSafe)
				safeMoveDirs.add(d);
			if (isThisSquareTurretSafe)
				noTurretMoveDirs.add(d);
		}
	}
	
	// get number of rounds until we are in danger
	public int getRoundsUntilDanger()
	{
		if (roundsUntilDanger >= 0)
			return roundsUntilDanger;
		
		roundsUntilDanger = 100;
		
		// FIX THIS
		
		for (RobotInfo ri : getNearbyHostiles())
		{
			if (true)//isRangedUnit[ri.type.ordinal()])
			{
				// time to shoot us is rounds until
				// coreDelay + cooldownDelay
				int dangerTime = (int)Math.floor(ri.coreDelay + ri.type.cooldownDelay);
				if (dangerTime < roundsUntilDanger)
					roundsUntilDanger = dangerTime;
			}
			else
			{
				// move one square closer to enemy
				// the square enemy needs to get to to attack us
				MapLocation onecloser = here.add(here.directionTo(ri.location));
				int dx = Math.abs(onecloser.x-ri.location.x);
				int dy = Math.abs(onecloser.y-ri.location.y);

				// number of longest straight steps + 1.4 * number of diagonal steps
				// scaling of movement delay to get here
				double effDistanceTo = Math.min(dx, dy)*0.4 + Math.max(dx, dy);
				
				int dangerTime = (int)Math.floor(ri.coreDelay + ri.type.movementDelay*effDistanceTo + ri.type.cooldownDelay);
				if (dangerTime < roundsUntilDanger)
					roundsUntilDanger = dangerTime;
			}			
		}

		return roundsUntilDanger;
	}
	
	// compute which direction moves us the farthest from the closest enemy
	// (trying to use safe squares first)
	public Direction getBestEscapeDir()
	{
		DirectionSet dirs = getSafeMoveDirs();
		
		// if there are no safe moves, just do something
		if (!dirs.any())
		{
			dirs = getCanMoveDirs().clone();
			dirs.remove(Direction.NONE);
		}
		
		if (!dirs.any())
			return Direction.NONE;
		
		Direction bestDir = null;
		int distToClosest = 0;
		
		for (Direction d : dirs.getDirections())
		{
			if (bestDir == null || distToClosestHostile[d.ordinal()] > distToClosest)
			{
				distToClosest = distToClosestHostile[d.ordinal()];
				bestDir = d;
			}
		}
		
		return bestDir;
	}

	public DirectionSet getNoTurretMoveDirs()
	{
		computeSafetyStats();
		return noTurretMoveDirs;
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
		return getNearbyHostiles().length > 0;
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
		
		Debug.setStringTS("AA" + new MapLocation(xtot,ytot));
		
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
	
	public boolean tryAttackSomeone() throws GameActionException
	{
		// attack someone in range if possible, low health first, prioritizing zombies
		
		RobotInfo zombieTarget = this.getLowestHealth(this.getNearbyZombies());
		RobotInfo enemyTarget = this.getLowestHealth(this.getNearbyEnemies());
		
		if (zombieTarget != null && rc.canAttackLocation(zombieTarget.location))
		{
			rc.attackLocation(zombieTarget.location);
			return true;
		}
		
		if (enemyTarget != null && rc.canAttackLocation(enemyTarget.location))
		{
			rc.attackLocation(enemyTarget.location);
			return true;
		}
		
		return false;
	}
	
	public boolean tryAvoidBeingShot() throws GameActionException
	{
		if (this.getNearbyHostiles().length == 0)
			return false;
		
		if (!rc.isCoreReady())
			return this.tryAttackSomeone();
		
		Direction escapeDir = this.getBestEscapeDir();
		
		// escape
		tryMove(escapeDir);
		
		return this.tryAttackSomeone();
	}
}