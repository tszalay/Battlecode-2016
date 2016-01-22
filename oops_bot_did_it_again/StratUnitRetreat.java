package oops_bot_did_it_again;

import battlecode.common.*;

import java.util.*;

public class StratUnitRetreat extends RobotPlayer implements Strategy
{
	private Strategy overrideStrategy = null;
	private MapLocation myDest = null;
	
	public String getName()
	{
		if (overrideStrategy != null)
			return overrideStrategy.getName();

		return "Retreating to Archon at " + myDest;
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
		
		// if we're fully healed, let me in, coach!
		if (rc.getHealth()+1 >= rc.getType().maxHealth)
			return false;
		
		myDest = Message.getClosestArchon();
		if (myDest == null)
			myDest = myBuilderLocation;
		
		DirectionSet bufferDirs = Micro.getBufferDirs();
		bufferDirs = bufferDirs.and(Micro.getTurretSafeDirs());
		
		if (bufferDirs.any())
			Nav.tryGoTo(myDest, bufferDirs);
		else
			Nav.tryGoTo(myDest, Micro.getCanMoveDirs());
		
		//Rubble.doClearRubble(Rubble.getRandomAdjacentRubble());
		
		return true;
	}
}