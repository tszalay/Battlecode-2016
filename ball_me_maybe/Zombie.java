package ball_me_maybe;

import battlecode.common.*;
import java.util.*;

public class Zombie extends RobotPlayer
{
	// to be used by turrets only
	public static MapLocation getSightedTarget()
	{
		return null;
	}
	
	// to be used by scouts only
	// (others can't send message signals dur)
	public static boolean sendSightedTarget() throws GameActionException
	{
		return false;
	}

	public static boolean sendSightedTarget(RobotInfo[] nearbyTargets) throws GameActionException
	{
		return false;
	}
}
