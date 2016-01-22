package blitzkrieg_bot;

import battlecode.common.*;

import java.util.*;

public class StratUnitCombat extends RobotPlayer implements Strategy
{
	private Strategy overrideStrategy = null;
	private MapLocation lastDest = null;
	private String myTask = "";
	
	public String getName()
	{
		if (overrideStrategy != null)
			return overrideStrategy.getName();

		return "Fighting: " + myTask + " at " + lastDest;
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
		
		/*if (rc.getRoundNum() > 2500)
		{
			if (Micro.getNearbyHostiles().length > 0)
				Nav.tryGoTo(Micro.getUnitCOM(Micro.getNearbyHostiles()), Micro.getCanMoveDirs());
			else
				Nav.tryGoTo(MapInfo.getSymmetricLocation(MapInfo.farthestArchonLoc), Micro.getCanMoveDirs());
			Action.tryAttackSomeone();
			return true;
		}*/
		
		// retreat to the archon?
		if (rc.getHealth() < 20)
		{
			overrideStrategy = new StratUnitRetreat();
			overrideStrategy.tryTurn();
			return true;
		}
		
		// target priority: nearby enemy > nearby ally > zombie den > our archon stuff
		lastDest = null;
		RobotInfo closestUnit = Micro.getClosestUnitTo(Micro.getNearbyHostiles(), here);
		if (closestUnit != null)
		{
			myTask = "enemy";
			lastDest = closestUnit.location;
		}

		if (lastDest == null)
		{
			myTask = "ally";
			lastDest = Message.getClosestAllyUnderAttack();
		}
		if (lastDest == null)
		{
			myTask = "den";
			lastDest = MapInfo.getClosestDen();
		}
		if (lastDest == null)
		{
			myTask = "rally";
			lastDest = MapInfo.ourArchonCenter;
		}
			
		DirectionSet bufferDirs = Micro.getBufferDirs();
		bufferDirs = bufferDirs.and(Micro.getTurretSafeDirs());
		
		// only shoot if we're safe here
		if (bufferDirs.isValid(Direction.NONE) && Action.tryAttackSomeone())
			return true;
		
		// don't move if we're safe and just shot
		if (bufferDirs.isValid(Direction.NONE) && !rc.isWeaponReady())
		{
			if (Micro.getRoundsUntilDanger() > 10)
			{
				Direction d = bufferDirs.getDirectionTowards(here,lastDest);
				if (d != null)
					Action.tryMove(d);
			}
			return true;
		}
		
		if (bufferDirs.any())
			Nav.tryGoTo(lastDest, bufferDirs);
		else
		{
			lastDest = Message.getClosestArchon();
			if (lastDest == null)
				lastDest = MapInfo.farthestArchonLoc;
			Nav.tryGoTo(lastDest, Micro.getCanMoveDirs());
		}
		
		// or shoot if we couldn't move
		Action.tryAttackSomeone();
		//Rubble.doClearRubble(Rubble.getRandomAdjacentRubble());
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
