package botumn_leaves;

import battlecode.common.*;
import java.util.*;

public class ScoutShadowStrat extends RobotPlayer implements Strategy
{	
	// the target for this scout and its last location and direction
	public int 		 	shadowTargetID = -1;
	public MapLocation	lastShadowLocation = null;
	public int		 	lastShadowRound = 0;
	public Direction  	lastShadowMoveDir = null;

	public static final int SHADOW_LOST_TIMEOUT = 20;
	
	
	public String getName()
	{
		return "Shadowing unit " + shadowTargetID;
	}
	
	public ScoutShadowStrat(int targetID) throws GameActionException
	{
		if (!rc.canSenseRobot(targetID))
			return;
		
		shadowTargetID = targetID;
		lastShadowLocation = rc.senseRobot(targetID).location;
		lastShadowRound = rc.getRoundNum();
	}
	
	// this is called either by tryUpdateTarget() or by Message
	public void updateShadowLocation(MapLocation newLoc)
	{
		lastShadowRound = rc.getRoundNum();
		
		if (lastShadowLocation.equals(newLoc))
			return;
		
		// otherwise, update the direction
		lastShadowMoveDir = lastShadowLocation.directionTo(newLoc);
		lastShadowLocation = newLoc;
	}
	
	private boolean tryUpdateTarget() throws GameActionException
	{
		// if we're still within sight range, just update the position
		if (rc.canSenseRobot(shadowTargetID))
		{
			updateShadowLocation(rc.senseRobot(shadowTargetID).location);
			return true;
		}
		// if we aren't, check for a timeout before saying "oh god we lost the ball"
		if (rc.getRoundNum() < lastShadowRound + SHADOW_LOST_TIMEOUT)
			return true;
		
		// we lost the ball!
		return false;
	}

	public boolean tryTurn() throws GameActionException
	{
		// first check if we can still ball
		if (!tryUpdateTarget())
			return false;
		
		Action.tryAttackSomeone();
		
		// can't do anything, but can stay in state
		if (!rc.isCoreReady())
			return true;
		
		// try to retreat towards the ball or shoot if we're in danger
		if (Micro.getRoundsUntilDanger() < 20)
		{
			if (lastShadowLocation != null)
			{
				RobotInfo closestEnemy = Micro.getClosestUnitTo(Micro.getNearbyEnemies(), lastShadowLocation);
				if (closestEnemy != null && here.distanceSquaredTo(closestEnemy.location) < lastShadowLocation.distanceSquaredTo(closestEnemy.location))
				{
					Action.tryRetreatTowards(lastShadowLocation, Micro.getSafeMoveDirs());
					return true;
				}
			}
			else
			{
				Action.tryGoToWithoutBeingShot(here, Micro.getSafeMoveDirs());
				return true;
			}
		}
		/*
		// if we're far, try moving closer using bugging nav probably
		if (here.distanceSquaredTo(lastShadowLocation) > maxDistSq)
		{
			Nav.tryGoTo(lastShadowLocation,Micro.getSafeMoveDirs());
			return true;
		}
		if (here.distanceSquaredTo(lastShadowLocation) < minDistSq)
		{
			Action.tryAdjacentSafeMoveToward(here.directionTo(lastShadowLocation).opposite());
			return true;
		}
		
		// try mimicking the ball
		if (lastShadowMoveDir != null)
		{
			// if it worked, set it to null to indicate we took care of it
			Direction d = ballDirs.getDirectionTowards(lastShadowMoveDir);
			if (d != null)
			{
				//first try clear rubble if it's stopping you
				if (Action.tryClearRubble(d)) return true;
				
				Action.tryMove(d);
				lastShadowMoveDir = null;
				return true;
			}
		}
				*/
		return true;
	}
	

}
