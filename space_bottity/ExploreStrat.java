package space_bottity;

import battlecode.common.*;


import java.util.*;

public class ExploreStrat extends RobotPlayer implements Strategy
{	
	private MapLocation myTarget = null;
	private String stratName;
	public static MapLocation myExploringTarget;
	
	public ExploreStrat() throws GameActionException
	{
		myTarget = MapInfo.getExplorationWaypoint();
		this.stratName = "ExploreStrat";
	}
	
	public boolean tryTurn() throws GameActionException
	{
		
		Debug.setStringAK("My Strategy: " + this.stratName);
		
		Debug.setStringTS("Exploring to " + myExploringTarget);
		
		// get a random waypoint and move towards it
		if (myExploringTarget == null || here.distanceSquaredTo(myExploringTarget) < 24 || !MapInfo.isOnMap(myExploringTarget))
			myExploringTarget = MapInfo.getExplorationWaypoint();
		
		DirectionSet goodDirs = Micro.getSafeMoveDirs();
		goodDirs = goodDirs.and(Micro.getTurretSafeDirs());
		
		if (Micro.getRoundsUntilDanger() < 5)
			Action.tryRetreatTowards(Message.recentEnemySignal, goodDirs);
		else
			Action.tryGoToWithoutBeingShot(myExploringTarget, goodDirs);
		
        // always send out info about sighted targets
		Sighting.doSendSightingMessage();
		
		// and use spare bytecodes to look for stuff
		 MapInfo.analyzeSurroundings();
		// and send the updates AK don't do this automtically - herding one's can't do it
		MapInfo.doScoutSendUpdates();
		
		return true;
	}
}
