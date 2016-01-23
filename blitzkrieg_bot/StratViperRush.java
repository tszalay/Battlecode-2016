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
		
        Action.tryViperAttack();
        //Action.tryMove(here.directionTo(enemyLoc));
        
        RobotInfo[] enemies = Micro.getNearbyEnemies();
        if (enemies != null && enemies.length > 0)
        {
        	Action.tryMove(here.directionTo(Micro.getEnemyCOM()));
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
        
        if (!Action.tryRetreatTowards(enemyLoc, Micro.getCanFastMoveDirs()))
        	if (!Action.tryRetreatTowards(enemyLoc, Micro.getCanMoveDirs()))
        		if (!Rubble.tryClearRubble(enemyLoc))
        			Action.tryMove(Micro.getCanMoveDirs().getRandomValid());
        
        return true;

	}
}
