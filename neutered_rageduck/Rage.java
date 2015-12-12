package neutered_rageduck;

import battlecode.common.*;

public class Rage extends Bot {
    
    public static boolean rageTargetCanBeSensed(int robotID) {
    	if (Bot.rc.canSenseRobot(robotID)) {
    		return true;
    	} else {
    		return false;
    	}
    	
    }
	
    private static int rageTargetLastUpdateRound;
    
	public static boolean tryBroadcastNewRageLocation(RobotInfo enemyLauncher) throws GameActionException {
        if (Bot.rc.getRoundLimit() - rageTargetLastUpdateRound > 5) {
        	MessageBoard.RAGE_LOC.writeMapLocation(enemyLauncher.location);
        	rageTargetLastUpdateRound = Bot.rc.getRoundLimit();
        	return true;
        } else {
        	return false;
        }
    }
}
