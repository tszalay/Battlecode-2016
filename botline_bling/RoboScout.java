package botline_bling;

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
		if (rc.getRoundNum() > RoboScout.SIGNAL_ROUND)
		{
			// check for signals by a time we should have received some
			
			int minID = 1000000;
			MapLocation rallyLoc = null;
			
			Signal[] sigs = rc.emptySignalQueue();
			for (Signal s : sigs)
			{
				// ignore enemy signals for now
				if (s.getTeam() != ourTeam)
					continue;
				
				Message m = new Message(s);
				switch(m.type)
				{
				case SPAWN:
					MapLocation loc = m.readLocation();
					if (s.getID() < minID)
					{
						rallyLoc = loc;
						minID = s.getID();
					}
					//System.out.println(s.getID() + " " + s.getLocation() + " " + loc + " / MY LOC:" + here);
					break;
				default:
					break;
				}
			}

			// now the rally location is in rallyLoc
		}
		
		
        int fate = rand.nextInt(1000);

        if (fate % 5 == 3) {
            // Send a normal signal
            rc.broadcastSignal(80);
        }

        boolean shouldAttack = false;

        // If this robot type can attack, check for enemies within range and attack one
        if (rc.getType().attackRadiusSquared > 0) {
            RobotInfo[] enemiesWithinRange = rc.senseNearbyRobots(rc.getType().attackRadiusSquared, theirTeam);
            RobotInfo[] zombiesWithinRange = rc.senseNearbyRobots(rc.getType().attackRadiusSquared, Team.ZOMBIE);
            if (enemiesWithinRange.length > 0) {
                shouldAttack = true;
                // Check if weapon is ready
                if (rc.isWeaponReady()) {
                    rc.attackLocation(enemiesWithinRange[rand.nextInt(enemiesWithinRange.length)].location);
                }
            } else if (zombiesWithinRange.length > 0) {
                shouldAttack = true;
                // Check if weapon is ready
                if (rc.isWeaponReady()) {
                    rc.attackLocation(zombiesWithinRange[rand.nextInt(zombiesWithinRange.length)].location);
                }
            }
        }

        if (!shouldAttack) {
            if (rc.isCoreReady()) {
                if (fate < 600) {
                    // Choose a random direction to try to move in
                    Direction dirToMove = Direction.values()[fate % 8];
                    // Check the rubble in that direction
                    if (rc.senseRubble(rc.getLocation().add(dirToMove)) >= GameConstants.RUBBLE_OBSTRUCTION_THRESH) {
                        // Too much rubble, so I should clear it
                        rc.clearRubble(dirToMove);
                        // Check if I can move in this direction
                    } else if (rc.canMove(dirToMove)) {
                        // Move
                        rc.move(dirToMove);
                    }
                }
            }
        }
	}
}
