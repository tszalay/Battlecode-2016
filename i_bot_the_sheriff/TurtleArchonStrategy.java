package i_bot_the_sheriff;

import battlecode.common.*;

import java.util.*;

import ball_me_maybe.MapInfo;

// turtle move tries to allow units to move around close to or inside a turtle ball
public class TurtleArchonStrategy extends RobotPlayer implements Strategy
{
	private MapLocation turtleLocation = null;
	
	public TurtleArchonStrategy()
	{
		// find a good turtle location
		// for now, pick a random corner of the map
		turtleLocation = new MapLocation(
					rand.nextBoolean() ? MapInfo.mapMin.x : MapInfo.mapMax.x,
					rand.nextBoolean() ? MapInfo.mapMin.y : MapInfo.mapMax.y
				);
	}
	
	public boolean tryTurn() throws GameActionException
	{
		switch (rc.getType())
		{
		case TURRET:
			break;
		case TTM:
			break;
		case SCOUT:
			break;
		case SOLDIER:
			break;
		}
		return true;
	}
}
