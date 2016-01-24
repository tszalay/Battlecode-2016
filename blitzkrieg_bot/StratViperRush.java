package blitzkrieg_bot;

import battlecode.common.*;

import java.util.*;

public class StratViperRush extends RobotPlayer implements Strategy
{
	private Strategy overrideStrategy = null;
	
	private MapLocation lastDest = null;

	private MapLocation enemyLoc = null;
	private MapLocation farArchon = null;
	private MapLocation[] enemyArchonLocs = null;
	
	public String getName()
	{
		if (overrideStrategy != null)
			return overrideStrategy.getName();

		return "Viper rush: " + lastDest;
	}
	
	public StratViperRush()
	{
		enemyArchonLocs = rc.getInitialArchonLocations(theirTeam);
		enemyLoc = enemyArchonLocs[0];
		farArchon = enemyArchonLocs[0];
		for (MapLocation loc : enemyArchonLocs)
		{
			if (here.distanceSquaredTo(loc) < here.distanceSquaredTo(enemyLoc))
				enemyLoc = loc;
			if (here.distanceSquaredTo(loc) > here.distanceSquaredTo(farArchon))
				farArchon = loc;
		}
		Debug.setStringSJF("target = " + enemyLoc.toString());
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
		
		// shoot self if almost dead
		if (rc.isWeaponReady() && rc.getHealth() <= 12 && Micro.getNearbyAllies().length == 0)
		{
			rc.attackLocation(here);
		}
		
		// run from zombies
//		if (Micro.getNearbyZombies() != null && Micro.getNearbyZombies().length > 0)
//		{
//			Action.tryRetreatTowards(enemyLoc, Micro.getCanMoveDirs());
//		}
		
		// attack
        Action.tryViperAttack();
        
        // try to have only one enemy in attack range at a time
        RobotInfo[] enemies = Micro.getNearbyEnemies();
        if (enemies != null && enemies.length > 1)
        {
        	Action.tryRetreatOrShootIfStuck();
        }
        else if (enemies != null && enemies.length > 0)
        {
        	Nav.tryGoTo(Micro.getEnemyCOM(),Micro.getSafeMoveDirs());
        }
        else if (here.distanceSquaredTo(enemyLoc) < 10 && (enemies == null || enemies.length == 0))
        {
        	if (enemyLoc.equals(farArchon))
        	{
        		farArchon = null;
        	}
        	if (farArchon == null)
        	{
        		enemyLoc = Message.recentEnemySignal;
        		if (enemyLoc == null)
        			enemyLoc = MapInfo.getExplorationWaypoint();
        	}
        	else
        	{
        		enemyLoc = farArchon;
        	}
        	Debug.setStringSJF("target = " + enemyLoc.toString() + ", enemies = " + enemies.length);
        }
        
        // try to go to enemy (will dig if necessary)
        Nav.tryGoTo(enemyLoc, Micro.getCanMoveDirs());
        
        return true;

	}
}
