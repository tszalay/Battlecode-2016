package botumn_leaves;

import battlecode.common.GameActionException;

public interface Strategy
{
	public enum Type
	{
		TURTLE,
		BALL_MOVE,
		FREE_UNIT,
		MOB_FIGHT,
		HERDING
	}
	
	// run the turn for this strategy
	// returns false if we can't do this strategy no more, no more, no more
	boolean tryTurn() throws GameActionException;
	default boolean fooTurn() throws GameActionException
	{
		Debug.setStringAK("Strategy: " + getName());
		return tryTurn();
	}
	
	// return the name of this strategys
	String getName();
}
