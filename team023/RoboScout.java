package team023;

import battlecode.common.*;

import java.util.*;

public class RoboScout extends RobotPlayer
{
	public static final int SIGNAL_ROUND = 30; 
	
	public static void init() throws GameActionException
	{
		// first-spawn scout, send the message right away
		// to a distance of 100 units
		if (rc.getRoundNum() < SIGNAL_ROUND)
			new Message(Message.MessageType.SPAWN, rc.getLocation()).send(100);
	}
	
	public static void turn() throws GameActionException
	{		
        int fate = rand.nextInt(1000);

        if (fate % 5 == 3) {
            // Send a normal signal
            rc.broadcastSignal(80);
        }

        if (rc.isCoreReady()) {
        	NavSafetyPolicy safety = new SafetyPolicyAvoidAllUnits();
        	MapLocation target = here.add(rand.nextInt(200)-100,rand.nextInt(200)-100);
        	if (rand.nextInt(7) == 3)
        		target = here.add(myArchon.location.directionTo(here));
            Nav.goTo(target, safety);
        }
	}
}
