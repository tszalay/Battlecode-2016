package neutered_defensive_duck;

import battlecode.common.*;

public class BotTower extends Bot {
    public static void loop(RobotController theRC) throws GameActionException {
        Bot.init(theRC);
        while (true) {
            try {
                turn();
            } catch (Exception e) {
                e.printStackTrace();
            }
            rc.yield();
        }
    }

    private static void turn() throws GameActionException {
        if (rc.isWeaponReady()) Combat.shootAtNearbyEnemies();

        Supply.shareSupply();
        
        RobotInfo[] nearbyEnemies = rc.senseNearbyRobots(63, Bot.them);
        if (nearbyEnemies.length > 0) {
        	boolean gotD = Defense.requestDefenders(nearbyEnemies);
	        if (gotD) {
	        	rc.setIndicatorDot(here.add(0, 1), 255, 255, 255);
	        }
        } else {
        	// if i am the defense post but no longer need it, clear me
        	MapLocation defensivePost = MessageBoard.DEFENSE_LOC.readMapLocation();
        	if (here.equals(defensivePost)) {
        		Defense.deleteDefensiveTarget();
        	}
        }
    }

}
