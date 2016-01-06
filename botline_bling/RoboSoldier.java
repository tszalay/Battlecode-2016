package botline_bling;

import battlecode.common.*;

public class RoboSoldier extends RobotPlayer
{
	static int fate;
	
	public static void init()
	{
		fate = rand.nextInt(1000);
	}
	
	public static void turn() throws GameActionException
	{
        if (rc.isCoreReady()) {
        	Micro.doAvoidBeingKilled();
        }
	}
}
