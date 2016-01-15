package space_bottity;

import battlecode.common.GameActionException;

public interface Strategy
{
	// run the turn for this strategy
	// returns false if we can't do this strategy no more, no more, no more
	boolean tryTurn() throws GameActionException;
	
}
