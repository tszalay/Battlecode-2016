package space_bottity;

import battlecode.common.*;

import java.util.*;

public class BlitzTeamStrat extends RobotPlayer implements Strategy
{
	private RobotType type;
	private RobotInfo myArchon = null;
	
	public BlitzTeamStrat(RobotType type, RobotInfo myArchon)
	{
		this.type = type;
		this.myArchon = myArchon;
	}
	
	public boolean tryTurn() throws GameActionException
	{
		switch (type)
		{
		case ARCHON:
			MapLocation partLoc = senseClosestPart();
			MapLocation neutralLoc = senseClosestNeutral();
			MapLocation mapPartLoc = MapInfo.getClosestPart();
			RobotInfo[] hostiles = Micro.getNearbyHostiles();
			
			// avoid all hostiles
			if (hostiles != null && hostiles.length > 0)
			{
				if (!Action.tryRetreatTowards(neutralLoc, Micro.getSafeMoveDirs().and(Micro.getTurretSafeDirs())))
					if (!Action.tryRetreatTowards(partLoc, Micro.getSafeMoveDirs().and(Micro.getTurretSafeDirs())))
						if (!Action.tryRetreatTowards(mapPartLoc, Micro.getSafeMoveDirs().and(Micro.getTurretSafeDirs())))
							Action.tryRetreatOrShootIfStuck();
				return true;
			}
			
			// run around and grab stuff as fast as possible
			if (neutralLoc != null)
				Action.tryGoToWithoutBeingShot(neutralLoc, Micro.getSafeMoveDirs().and(Micro.getTurretSafeDirs()));
			
			else if (partLoc != null)
				Action.tryGoToWithoutBeingShot(partLoc, Micro.getSafeMoveDirs().and(Micro.getTurretSafeDirs()));
			
			else if (mapPartLoc != null)
				Action.tryGoToWithoutBeingShot(mapPartLoc, Micro.getSafeMoveDirs().and(Micro.getTurretSafeDirs()));
			
			else if (MapInfo.getExplorationWaypoint() != null)
				Action.tryGoToWithoutBeingShot(MapInfo.getExplorationWaypoint(), Micro.getSafeMoveDirs().and(Micro.getTurretSafeDirs()));
			
			else
				return false;
			
			return true;
			
		case SCOUT:
			// scout for the archon
			if (!rc.canSenseRobot(myArchon.ID))
				return false;
			
			MapLocation archonLoc = rc.senseRobot(myArchon.ID).location; // have to update this
			Debug.setStringSJF("my archon is at: " + archonLoc.toString());
			if (here.distanceSquaredTo(archonLoc) > Math.floor(RobotType.ARCHON.sensorRadiusSquared/2))
			{
				Action.tryGoToWithoutBeingShot(archonLoc, Micro.getSafeMoveDirs());
			}
			
			return true;
		
		default:
			// not sure, probably shoot and run away from Archon
			if (!Action.tryAttackSomeone())
			{
				if (myArchon == null)
					return false;
				
				if ( !Nav.tryGoTo(here.add(here.directionTo(myArchon.location).opposite()), Micro.getSafeMoveDirs()) )
					if ( !Nav.tryGoTo(here.add(here.directionTo(myArchon.location).opposite()), Micro.getCanFastMoveDirs()) )
						if ( !Nav.tryGoTo(here.add(here.directionTo(myArchon.location).opposite()), Micro.getCanMoveDirs()) )
							return false;
			}
			return true;
		}
	}
	
	public MapLocation senseClosestPart() throws GameActionException
	{
		MapLocation[] parts = rc.sensePartLocations(type.sensorRadiusSquared);
		
		if (parts == null || parts.length == 0)
			return null;
		
		MapLocation closestPartLoc = null;
		for (MapLocation loc : parts)
		{
			if (closestPartLoc == null || here.distanceSquaredTo(loc) < here.distanceSquaredTo(closestPartLoc))
				closestPartLoc = loc;
		}
		
		return closestPartLoc;
	}
	
	public MapLocation senseClosestNeutral() throws GameActionException
	{
		RobotInfo[] neutrals = rc.senseNearbyRobots(type.sensorRadiusSquared,Team.NEUTRAL);
		
		if (neutrals == null || neutrals.length == 0)
			return null;
		
		MapLocation closestNeutralLoc = null;
		for (RobotInfo ri : neutrals)
		{
			if (closestNeutralLoc == null || ri.type == RobotType.ARCHON || (ri.type != RobotType.ARCHON && here.distanceSquaredTo(ri.location) < here.distanceSquaredTo(closestNeutralLoc)))
				closestNeutralLoc = ri.location;
		}
		
		return closestNeutralLoc; // will return an archon location even if it's not really the closest
	}
}
