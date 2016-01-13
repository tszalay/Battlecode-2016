package space_bottity;

import battlecode.common.*;
import java.util.*;

public class BallMove extends RobotPlayer
{
	public static int 		 	ballTargetID = -1;
	public static MapLocation	lastBallLocation = null;
	public static int		 	lastBallRound = 0;
	public static Direction  	lastBallMoveDir = null;
	
	public static final int BALL_LOST_TIMEOUT = 20;
	
	public static void startBalling(int targetID) throws GameActionException
	{
		if (!rc.canSenseRobot(targetID))
			return;
		
		ballTargetID = targetID;
		lastBallLocation = rc.senseRobot(targetID).location;
		lastBallRound = rc.getRoundNum();
	}
	
	// this is called either by tryUpdateTarget() or by Message
	public static void updateBallLocation(MapLocation newLoc)
	{
		lastBallRound = rc.getRoundNum();
		
		if (lastBallLocation.equals(newLoc))
			return;
		
		// otherwise, update the direction
		lastBallMoveDir = lastBallLocation.directionTo(newLoc);
		lastBallLocation = newLoc;
	}
	
	public static boolean tryUpdateTarget() throws GameActionException
	{
		// if we're still within sight range, just update the position
		if (rc.canSenseRobot(ballTargetID))
		{
			updateBallLocation(rc.senseRobot(ballTargetID).location);
			return true;
		}
		// if we aren't, check for a timeout before saying "oh god we lost the ball"
		if (rc.getRoundNum() < lastBallRound + BALL_LOST_TIMEOUT)
			return true;
		
		// we lost the ball!
		ballTargetID = -1;
		return false;
	}
	
	public static boolean hasBallTarget()
	{
		return ballTargetID >= 0;
	}
	
	public static void ballMove(int minDistSq, int maxDistSq) throws GameActionException
	{
		if (!rc.isCoreReady())
			return;
		
		// try to retreat towards the ball or shoot if we're in danger
		if (Micro.getRoundsUntilDanger() < 10)
		{
			if (lastBallLocation != null)
				Behavior.tryRetreatTowards(lastBallLocation, Micro.getSafeMoveDirs());
			else
				Behavior.tryGoToWithoutBeingShot(here, Micro.getSafeMoveDirs());
			
			return;
		}
		
		// if we're not in danger, try to shoot something anyway
		// (e.g. zombie dens)
		if (Micro.getNearbyHostiles().length > 0 &&  rc.getType().canAttack())
		{
			Behavior.tryAdjacentSafeMoveToward(here.directionTo(Micro.getNearbyHostiles()[0].location));
			Behavior.tryAttackSomeone();
			Debug.setStringTS("Last ball: tryAttack");
			return;
		}
		
		// if we're far, move closer using bugging nav probably
		if (here.distanceSquaredTo(lastBallLocation) > maxDistSq)
		{
			Nav.tryGoTo(lastBallLocation,Micro.getSafeMoveDirs());
			return;
		}
		if (here.distanceSquaredTo(lastBallLocation) < minDistSq)
		{
			Behavior.tryAdjacentSafeMoveToward(here.directionTo(lastBallLocation).opposite());
			return;
		}
		
		DirectionSet ballDirs = Micro.getSafeMoveDirs().clone();
		
		for (Direction d : ballDirs.getDirections())
		{
			int dSq = here.add(d).distanceSquaredTo(lastBallLocation);
			if (dSq < minDistSq || dSq > maxDistSq)
				ballDirs.remove(d);
		}
		
		// try mimicking the ball
		if (lastBallMoveDir != null)
		{
			// if it worked, set it to null to indicate we took care of it
			Direction d = ballDirs.getDirectionTowards(lastBallMoveDir);
			if (d != null)
			{
				Micro.tryMove(d);
				lastBallMoveDir = null;
				return;
			}
		}

		MapLocation ml = Micro.getUnitCOM(rc.senseNearbyRobots(24, ourTeam));
		Direction ar = here.directionTo(lastBallLocation).rotateRight().rotateRight();
		Direction al = here.directionTo(lastBallLocation).rotateLeft().rotateLeft();
		Direction moveDir = null;
		if (here.add(al).distanceSquaredTo(ml) > here.add(ar).distanceSquaredTo(ml))
			moveDir = ballDirs.getDirectionTowards(al);
		else
			moveDir = ballDirs.getDirectionTowards(ar);
		
		if (moveDir != null)
			Micro.tryMove(moveDir);
	}
}
