package blitzkrieg_bot;

import battlecode.common.*;

import java.util.*;

public class StratTheft extends RobotPlayer implements Strategy
{
	private Strategy overrideStrategy = null;
	
	public String getName()
	{
		if (overrideStrategy != null)
			return overrideStrategy.getName();

		try {
		return "Shamelessly plagiarizing " + Nav.roundsToDigThrough();
		} 
		catch (Exception e) {
			
		}
		return "Aefawef";
	}
	
	public boolean tryTurn() throws GameActionException
	{
		// do we have a strategy that takes precedence over this one?
		if (overrideStrategy != null)
		{
			if (overrideStrategy.tryTurn())
				return true;
			else
				overrideStrategy = null;
		}
		
		if (rc.getRoundNum() > 2500)
		{
			if (Micro.getNearbyHostiles().length > 0)
				Nav.tryGoTo(Micro.getUnitCOM(Micro.getNearbyHostiles()), Micro.getCanMoveDirs());
			else
				Nav.tryGoTo(MapInfo.getSymmetricLocation(MapInfo.farthestArchonLoc), Micro.getCanMoveDirs());
			Action.tryAttackSomeone();
			return true;
		}
		
		MapLocation dest = null;
		
		RobotInfo closestUnit = Micro.getClosestUnitTo(Micro.getNearbyHostiles(), here);
		if (closestUnit != null)
			dest = closestUnit.location;
		
		if (dest == null)
			dest = MapInfo.getClosestDen();
		if (dest == null)
			dest = MapInfo.ourArchonCenter;
			
		DirectionSet bufferDirs = Micro.getBufferDirs();
		bufferDirs = bufferDirs.and(Micro.getTurretSafeDirs());
		
		boolean gtfo = rc.getHealth() < rc.getType().maxHealth / 3;
		
		if (gtfo)
		{
			dest = Message.getRecentFriendlyLocation();
			if (dest == null)
				dest = myBuilderLocation;
		}

		if (Message.getClosestAllyUnderAttack() != null)
			dest = Message.getClosestAllyUnderAttack();
		
		// only shoot if we're safe here
		if (!gtfo && bufferDirs.isValid(Direction.NONE) && Action.tryAttackSomeone())
			return true;
		
		// don't move if we're safe and just shot
		if (bufferDirs.isValid(Direction.NONE) && !rc.isWeaponReady())
			return true;
		
		if (bufferDirs.any())
			Nav.tryGoTo(dest, bufferDirs);
		else
			Action.tryRetreatOrShootIfStuck();
		
		Rubble.doClearRubble(Rubble.getRandomAdjacentRubble());
		
		// GTFO
		
		/*
		MapLocation allyAttacked = Message.getClosestAllyUnderAttack();
		if (allyAttacked != null)
		{
			if (here.distanceSquaredTo(allyAttacked) > 200)
			{
				Nav.tryGoTo(allyAttacked, Micro.getSafeMoveDirs());
				return true;
			}
			overrideStrategy = new StratMobFight(Message.getClosestAllyUnderAttack());
			overrideStrategy.tryTurn();
			return true;
		}
		
*/
		
		return true;
	}
}
