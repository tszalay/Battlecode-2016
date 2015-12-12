package neutered_rageduck;

import battlecode.common.*;

public class Rage extends Bot {

    private static boolean rageTargetAlreadyExists() {
    	int rageID = 0;
		try {
			rageID = MessageBoard.RAGE_LOC.readInt();
		} catch (GameActionException e) {
			e.printStackTrace();
		}
    	if (Bot.rc.canSenseRobot(rageID)) {
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public static boolean rageTargetCanBeSensed() {
    	int rageID = 0;
		try {
			rageID = MessageBoard.RAGE_LOC.readInt();
		} catch (GameActionException e) {
			e.printStackTrace();
		}
    	if (Bot.rc.canSenseRobot(rageID)) {
    		return true;
    	} else {
    		return false;
    	}
    }
	
	public static boolean tryBroadcastNewRageLocation(RobotInfo enemyLauncher) throws GameActionException {
        if (rageTargetAlreadyExists()) {
        	return false;
        } else {
        	MessageBoard.RAGE_LOC.writeInt(enemyLauncher.ID);
        	return true;
        }
    }
	
}
