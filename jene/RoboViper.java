package jene;

import battlecode.common.*;

public class RoboViper extends RobotPlayer
{
	public static MapLocation enemyLoc = null;
	public static MapLocation farArchon = null;
	public static MapLocation[] enemyArchonLocs = null;
	
	public static void init() throws GameActionException
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
	
	public static void turn() throws GameActionException
	{
        Action.tryViperAttack();
        //Action.tryMove(here.directionTo(enemyLoc));
        
        RobotInfo[] enemies = Micro.getNearbyEnemies();
        
        if (enemies != null && enemies.length > 0)
        	enemyLoc = Micro.getClosestUnitTo(enemies, here).location;
        
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
        			enemyLoc = Message.getClosestAllyUnderAttack();
        	}
        	else
        	{
        		enemyLoc = farArchon;
        	}
        	Debug.setStringSJF("target = " + enemyLoc.toString() + ", enemies = " + enemies.length);
        }
        
        if (!Action.tryGoToWithoutBeingShot(enemyLoc, Micro.getCanFastMoveDirs()))
        	if (!Action.tryGoToWithoutBeingShot(enemyLoc, Micro.getCanMoveDirs()))
        		Rubble.tryClearRubble(enemyLoc);
	}
}
