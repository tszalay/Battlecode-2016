package blitzkrieg_bot;

import battlecode.common.*;


import java.util.*;

public class StratExplore extends RobotPlayer implements Strategy
{
	// linear distance of the spacing of exploration waypoints
	private static final int EXPLORE_POINT_DIST = 7;
	// square distance of how close we need to be as a scout to 'visit'
	private static final int EXPLORE_VISIT_SQDIST = 8;
	
	private int			myExploringQuadrant;
	private FastLocSet 	myExploringTargets;
	private MapLocation myExploringTarget;
	
	private int lastNewTargetRound = 0;
	
	
	public String getName()
	{
		return "Explored Q" + myExploringQuadrant + " "
				+ (64-myExploringTargets.elements().size()) + "/64";
	}
	
	public StratExplore() throws GameActionException
	{
		myExploringQuadrant = rand.nextInt(4);
		
		resetTargets();
	}
	
	private void resetTargets()
	{
		myExploringTargets = new FastLocSet();
		// populate myExploringTargets with an array of locations
		// in the specified map quadrant that the scout has to visit within
		// EXPLORE_VISIT_SQDIST units to remove
		
		MapLocation minpt = MapInfo.mapMin.add(EXPLORE_POINT_DIST/2,EXPLORE_POINT_DIST/2);
		MapLocation maxpt = MapInfo.mapMax.add(-EXPLORE_POINT_DIST/2,-EXPLORE_POINT_DIST/2);
		MapLocation stoppt = MapInfo.mapCenter.add(EXPLORE_POINT_DIST/2,EXPLORE_POINT_DIST/2);
		
		boolean flipX = ((myExploringQuadrant&1)==0);
		boolean flipY = ((myExploringQuadrant/2)==0);
		
		int dx = flipX ? EXPLORE_POINT_DIST : -EXPLORE_POINT_DIST;
		int dy = flipY ? EXPLORE_POINT_DIST : -EXPLORE_POINT_DIST;
		
		int x = flipX ? minpt.x : maxpt.x;
		for (int xl=minpt.x; xl<stoppt.x; xl += EXPLORE_POINT_DIST)
		{
			int y = flipY ? minpt.y : maxpt.y;
			for (int yl=minpt.y; yl<stoppt.y; yl += EXPLORE_POINT_DIST)
			{
				// add the point
				myExploringTargets.add(new MapLocation(x,y));
				y += dy;
			}
			x += dx;
		}
		
		// tell it to go to the first target, which is the corner
		myExploringTarget = myExploringTargets.elements().get(0);
		lastNewTargetRound = rc.getRoundNum();		
	}
	
	private void updateTargets()
	{
		// first, remove any targets within distance yay
		Iterator<MapLocation> it = myExploringTargets.elements().iterator();
		while (it.hasNext())
		{
			MapLocation loc = it.next();
			if (here.distanceSquaredTo(loc) <= EXPLORE_VISIT_SQDIST)
			{
				it.remove();
				myExploringTargets.remove(loc);
			}
		}
		
		// no targets for me? reset all of them
		if (myExploringTargets.elements().size() == 0)
		{
			myExploringQuadrant = (myExploringQuadrant+1)%4;
			resetTargets();
		}
		
		// now do we need to update my target?
		if (myExploringTarget == null || !myExploringTargets.contains(myExploringTarget) ||
				!MapInfo.isOnMap(myExploringTarget) || roundsSince(lastNewTargetRound) > 100)
		{
			int ind = rand.nextInt(myExploringTargets.elements().size());
			myExploringTarget = myExploringTargets.elements().get(ind);
			lastNewTargetRound = rc.getRoundNum();
		}
	}
	
	public boolean tryTurn() throws GameActionException
	{		
		// update all of our targets, visited and otherwise
		updateTargets();
		
		DirectionSet goodDirs = Micro.getSafeMoveDirs();
		
		//if (Micro.getRoundsUntilDanger() < 10)
		{
			//if (!Action.tryRetreatTowards(Message.recentEnemySignal, goodDirs))
			//	Action.tryRetreatOrShootIfStuck();
		}
		//else
		{
			if (goodDirs.any())
				Nav.tryGoTo(myExploringTarget, goodDirs);
			else
				Action.tryRetreatOrShootIfStuck();
		}

		return true;
	}
}
