package neutered_defensive_duck;

import battlecode.common.*;

public class Defense extends Bot {

    public static boolean tryToAddMeToDefense() throws GameActionException {
    	if (MessageBoard.NUM_DEFENDERS.readInt()>0) {
	    	int[] defenderIDs = MessageBoard.DEFENSIVE_BOTS.getDefenderList();
	    	if (defenderIDs!=null && defenderIDs.length > 0) {
		    	for (int i=0; i<defenderIDs.length; i++) {
		    		if (!rc.canSenseRobot(defenderIDs[i])) { // if this spot on the list is unoccupied
		    			MessageBoard.DEFENSIVE_BOTS.addMeToDefenderList(i, rc.getID()); // put us on the list
		    			return true;
		    		} else {
		    			MapLocation defensivePost = MessageBoard.DEFENSE_LOC.readMapLocation();
		    			if (rc.senseRobot(defenderIDs[i]).location.distanceSquaredTo(defensivePost) > Bot.rc.getLocation().distanceSquaredTo(defensivePost)) { // if we are closer
		    				MessageBoard.DEFENSIVE_BOTS.addMeToDefenderList(i, rc.getID()); // put us on the list
		        			return true;
		    			}
		    		}
		    	}
	    	} else { // nobody on the list yet so add me
	    		MessageBoard.DEFENSIVE_BOTS.addMeToDefenderList(0, rc.getID()); // put us on the list
    			return true;
	    	}
    	}
    	return false;
    }
    
    public static boolean amIOnDefense() throws GameActionException {
    	// first check if defensive tower is viable
		if (MessageBoard.NUM_DEFENDERS.readInt()>0) {
	    	int[] defenderIDs = MessageBoard.DEFENSIVE_BOTS.getDefenderList();
	    	if (defenderIDs!=null && defenderIDs.length > 0) {
		    	for (int i=0; i<defenderIDs.length; i++) {
		    		if (rc.getID() == defenderIDs[i]) {
		    			return true;
		    		}
		    	}
	    	}
    	}
    	return false;
    }
    
    public static boolean requestDefenders(RobotInfo[] nearbyEnemies) throws GameActionException { // decide whether to send defenders and how many
    	if (here.equals(MessageBoard.DEFENSE_LOC.readMapLocation())) {// == MessageBoard.DEFENSE_LOC.readMapLocation().x && Bot.here.y == MessageBoard.DEFENSE_LOC.readMapLocation().y) {
    		return true;
    	}
    	if (newDefensiveLocationTakesPrecedence(nearbyEnemies)) {
    		//setDefenseLocation(here, Math.min(5,numEnemiesAttackingLocation(here, nearbyEnemies)+1));
    		setDefenseLocation(Bot.rc.getLocation(), Math.min(5,nearbyEnemies.length+1));
    		//System.out.println("Tower set as defensive rally.");
    		return true;
    	}
    	return false;
    }
    
    private static void setDefenseLocation(MapLocation loc, int num) throws GameActionException {
    	MessageBoard.DEFENSE_LOC.writeMapLocation(loc);
    	MessageBoard.NUM_DEFENDERS.writeInt(num);
    	//System.out.println("Requested " + num + " defenders");
    }
    
    private static int calculatePotentialDefensivePower(MapLocation loc, RobotInfo[] nearbyEnemies) throws GameActionException {
    	// this will be the amount of firepower our defense can unleash on attackers before the defensive target is destroyed
    	// taking into account transit time
    	int numEnemies = nearbyEnemies.length; //numEnemiesAttackingLocation(loc, nearbyEnemies);
    	int numAttacksTillTowerDies =  (int) (rc.getHealth()) / (numEnemies*(int)RobotType.MISSILE.attackPower);
    	int numTurnsTillTowerDies = (int) (numAttacksTillTowerDies*8);
    	//System.out.println(numTurnsTillTowerDies + " till i die");
    	// current defenders
    	int[] defenderIDs = MessageBoard.DEFENSIVE_BOTS.getDefenderList();
    	int power = 0;
    	if (defenderIDs!=null) {
	    	for (int id : defenderIDs)
	    	{
	    		if (rc.canSenseRobot(id)) {
	    			power = power + Math.max(0, numTurnsTillTowerDies - rc.senseRobot(id).location.distanceSquaredTo(loc) + 6);
	    		}
	    	}
    	}
    	// potential defenders
    	RobotInfo[] potentialDefenders = rc.senseNearbyRobots(numTurnsTillTowerDies + 6, Bot.us);
    	for (RobotInfo bot : potentialDefenders) {
    		power = power + Math.max(0, numTurnsTillTowerDies - bot.location.distanceSquaredTo(loc) + 6);
    	}
    	return power;
    }
    
//    private static int numEnemiesAttackingLocation(MapLocation loc, RobotInfo[] nearbyEnemies) {
//        int ret = 0;
//        for (int i = nearbyEnemies.length; i-- > 0;) {
//            if (nearbyEnemies[i].type.attackRadiusSquared >= loc.distanceSquaredTo(nearbyEnemies[i].location)) ret++;
//            else if (nearbyEnemies[i].type == RobotType.MISSILE && 15 >= loc.distanceSquaredTo(nearbyEnemies[i].location)) ret++;
//        }
//        return ret;
//    }
    
    public static boolean isDefensiveLocTowerStillAlive() throws GameActionException {
    	// is it alive
    	MapLocation currentDefensivePost = MessageBoard.DEFENSE_LOC.readMapLocation();
    	MapLocation[] ourTowerLocations = rc.senseTowerLocations();
    	for (MapLocation loc : ourTowerLocations) {
    		if (loc.equals(currentDefensivePost)) {
    			return true;
    		}
    	}
    	return false;
    }
    
    private static boolean newDefensiveLocationTakesPrecedence(RobotInfo[] nearbyEnemies) throws GameActionException {
    	// whether this location takes precedence over current location
    	if (isDefensiveLocTowerStillAlive()) {
    		int powerForNewLocation = calculatePotentialDefensivePower(Bot.rc.getLocation(), nearbyEnemies);
    		int powerForCurrentLocation = calculatePotentialDefensivePower(MessageBoard.DEFENSE_LOC.readMapLocation(), nearbyEnemies);
    		if (powerForNewLocation > powerForCurrentLocation && powerForNewLocation > 0) {
    			return true;
    		} else {
    			return false;
    		}
    	} else {
    		return true;
    	}
    }
    
    public static void deleteDefensiveTarget() throws GameActionException {
    	MessageBoard.NUM_DEFENDERS.writeInt(0);
    	MessageBoard.DEFENSE_LOC.writeMapLocation(Bot.theirHQ);
    	//System.out.println("Defense location deleted.");
    }
    
}
