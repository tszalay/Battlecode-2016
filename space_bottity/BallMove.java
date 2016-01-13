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
		
		MapLocation archonLoc = Message.recentArchonLocation;
		
		if (archonLoc == null)
			archonLoc = here;

		// if you see an archon update your location
		for (RobotInfo ri : Micro.getNearbyAllies())
		{
			if (ri.type == RobotType.ARCHON && ri.location.distanceSquaredTo(here) < archonLoc.distanceSquaredTo(here))
				archonLoc = ri.location;
		}

		
		// try to retreat towards the ball or shoot if we're in danger
		if (Micro.getRoundsUntilDanger() < 10)
		{
			if (archonLoc != null)
				Behavior.tryRetreatTowards(archonLoc, Micro.getSafeMoveDirs());
			else
				Behavior.tryGoToWithoutBeingShot(here, Micro.getSafeMoveDirs());
			
			Debug.setStringTS("Last ball: retreat");
			
			return;
		}
		
		// if we're not in danger, try to shoot something anyway
		// (e.g. zombie dens)
		if (Micro.getNearbyHostiles().length > 0)
		{
			Behavior.tryAdjacentSafeMoveToward(here.directionTo(Micro.getNearbyHostiles()[0].location));
			Behavior.tryAttackSomeone();
			Debug.setStringTS("Last ball: tryAttack");
			return;
		}
		
		// if we're far, move closer
		if (here.distanceSquaredTo(archonLoc) > maxDistSq)
		{
			Nav.tryGoTo(archonLoc,Micro.getSafeMoveDirs());
			Debug.setStringTS("Last ball: closer");
			return;
		}
		// if we're too close, move farther
		if (here.distanceSquaredTo(archonLoc) < minDistSq)
		{
			Behavior.tryAdjacentSafeMoveToward(here.directionTo(archonLoc).opposite());
			Debug.setStringTS("Last ball: farther");
			return;
		}
		
		MapLocation ml = Micro.getUnitCOM(rc.senseNearbyRobots(24, ourTeam));
		Direction ar = here.directionTo(archonLoc).rotateRight().rotateRight();
		Direction al = here.directionTo(archonLoc).rotateLeft().rotateLeft();
		if (here.add(al).distanceSquaredTo(ml) > here.add(ar).distanceSquaredTo(ml))
			Behavior.tryAdjacentSafeMoveToward(al);
		else
			Behavior.tryAdjacentSafeMoveToward(ar);
		Debug.setStringTS("Last ball: move perp.");
	}
}
