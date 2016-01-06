package botline_bling;

import battlecode.common.*;

import java.util.*;

public class RoboScout extends RobotPlayer
{
	public static final int SIGNAL_ROUND = 30; 
	public static MapLocation kiteTarget = null;
	
	public static void init() throws GameActionException
	{
		// first-spawn scout, send the message right away
		// to a distance of 100 units
		if (rc.getRoundNum() < SIGNAL_ROUND)
			new Message(Message.MessageType.SPAWN, myArchon.location).send(100);
		
		kiteTarget = here.add(rand.nextInt(200)-100,rand.nextInt(200)-100);
	}
	
	public static void turn() throws GameActionException
	{		
        int fate = rand.nextInt(1000);

        if (fate % 5 == 3) {
            // Send a normal signal
            rc.broadcastSignal(80);
        }

        if (rc.isCoreReady())
        {
        	RobotInfo[] nearbyEnemies = rc.senseNearbyRobots(rc.getType().sensorRadiusSquared, theirTeam);
            RobotInfo[] nearbyZombies = rc.senseNearbyRobots(rc.getType().sensorRadiusSquared, Team.ZOMBIE);
            
        	if (!Micro.tryKiteZombies(kiteTarget)  && rc.isCoreReady())
        	{
        		NavSafetyPolicy safety = new SafetyPolicyAvoidAllUnits(nearbyEnemies, nearbyZombies);
        		MapLocation target = here.add(rand.nextInt(200)-100,rand.nextInt(200)-100);
                Nav.goTo(target, safety);
        	}
        }
	}
}
