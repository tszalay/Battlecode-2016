package bassplayer;

import battlecode.common.*;
import java.util.*;

public class RobotPlayer {
	
	static RobotController rc;
	
	// Game-given values
	static Team myTeam;
	static Team enemyTeam;
	static RobotType myType;
	static int myRange;
	
	// Our assigned values n stuff
	static int mySquad;
	
	static Random rand;
	static Direction[] directions = {Direction.NORTH, Direction.NORTH_EAST, Direction.EAST, Direction.SOUTH_EAST, Direction.SOUTH, Direction.SOUTH_WEST, Direction.WEST, Direction.NORTH_WEST};
	static int myRounds = 0;
	
	
	
	static final int MAX_SQUADS = 32;
	
	/* Squad task defines */
	static final int squadTaskBase = 0;
	
	/* Squad target defines */
	static final int squadTargetBase = squadTaskBase + MAX_SQUADS;
	
	/* Squad unit counts */
	static final int squadUnitsBase = squadTargetBase + MAX_SQUADS;
	
	/* Next squad channel */
	static final int nextSquadChan = squadUnitsBase + 1;
		
	
	
	/* Sensing location defines etc */
	static int senseLocsShort = 69;
	static int senseLocsLong = 109;
	static int[] senseLocsX = {0,-1,0,0,1,-1,-1,1,1,-2,0,0,2,-2,-2,-1,-1,1,1,2,2,-2,-2,2,2,-3,0,0,3,-3,-3,-1,-1,1,1,3,3,-3,-3,-2,-2,2,2,3,3,-4,0,0,4,-4,-4,-1,-1,1,1,4,4,-3,-3,3,3,-4,-4,-2,-2,2,2,4,4,-5,-4,-4,-3,-3,0,0,3,3,4,4,5,-5,-5,-1,-1,1,1,5,5,-5,-5,-2,-2,2,2,5,5,-4,-4,4,4,-5,-5,-3,-3,3,3,5,5};
	static int[] senseLocsY = {0,0,-1,1,0,-1,1,-1,1,0,-2,2,0,-1,1,-2,2,-2,2,-1,1,-2,2,-2,2,0,-3,3,0,-1,1,-3,3,-3,3,-1,1,-2,2,-3,3,-3,3,-2,2,0,-4,4,0,-1,1,-4,4,-4,4,-1,1,-3,3,-3,3,-2,2,-4,4,-4,4,-2,2,0,-3,3,-4,4,-5,5,-4,4,-3,3,0,-1,1,-5,5,-5,5,-1,1,-2,2,-5,5,-5,5,-2,2,-4,4,-4,4,-3,3,-5,5,-5,5,-3,3};
	static int[] senseLocsR = {0,10,10,10,10,14,14,14,14,20,20,20,20,22,22,22,22,22,22,22,22,28,28,28,28,30,30,30,30,31,31,31,31,31,31,31,31,36,36,36,36,36,36,36,36,40,40,40,40,41,41,41,41,41,41,41,41,42,42,42,42,44,44,44,44,44,44,44,44,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,53,53,53,53,53,53,53,53,56,56,56,56,58,58,58,58,58,58,58,58};
	
	
	// HQ-specific
	static int[] squadCounts = new int[MAX_SQUADS];
	

	public static void run(RobotController robotc)
	{
		rc = robotc;
		rand = new Random();
		myRange = rc.getType().attackRadiusSquared;
		myTeam = rc.getTeam();
		enemyTeam = myTeam.opponent();
		myType = rc.getType();
		
		
		// Initialization code
		try {
			switch (myType)
			{
			case HQ:
				break;
			case BEAVER:
				break;
			case MINER:
				break;
			case DRONE:
				// get the next squad, as assigned by the HQ
				int nextSquad = rc.readBroadcast(nextSquadChan);
				// save the lowest byte
				mySquad = nextSquad & 255;
				// and write it back
				rc.broadcast(nextSquadChan, nextSquad >> 8);
				break;
			}
		} catch (Exception e) {
			System.out.println("Unexpected exception");
			e.printStackTrace();
		}
		
		while(true) {
			try {
				rc.setIndicatorString(0, "This is an indicator string.");
				rc.setIndicatorString(1, "I am a " + rc.getType());
			} catch (Exception e) {
				System.out.println("Unexpected exception");
				e.printStackTrace();
			}
			
			switch (myType)
			{
			case HQ:
				doHQ();
				break;
			case TOWER:
				doTower();
				break;
			case BASHER:
				doBasher();
				break;
			case SOLDIER:
				doSoldier();
				break;
			case BEAVER:
				doBeaver();
				break;
			}
			
			rc.yield();
		}
	}
	
	
	
	static void doHQ()
	{
		try 
		{
			int nextSquad = 0;
			// the number we fill squads to
			int squadMin = 8;
			for (int i=0; i<MAX_SQUADS; i++)
			{
				int squadcount = rc.readBroadcast(squadUnitsBase + i);
				if (squadcount >> 16 == Clock.getRoundNum() - 1)
				{
					squadcount &= 255;
					squadCounts[i] = squadcount;
					// refill squad with fewest nonzero units
					if (squadcount > 0 && squadcount < squadMin)
					{
						squadMin = squadcount;
						nextSquad = i;
					}
				}
				else
				{
					squadCounts[i] = 0;
					break;
				}
			}
			rc.broadcast(nextSquadChan, nextSquad);
					
			/*int fate = rand.nextInt(10000);
			RobotInfo[] myRobots = rc.senseNearbyRobots(999999, myTeam);
			int numSoldiers = 0;
			int numBashers = 0;
			int numBeavers = 0;
			int numBarracks = 0;
			for (RobotInfo r : myRobots) {
				RobotType type = r.type;
				if (type == RobotType.SOLDIER) {
					numSoldiers++;
				} else if (type == RobotType.BASHER) {
					numBashers++;
				} else if (type == RobotType.BEAVER) {
					numBeavers++;
				} else if (type == RobotType.BARRACKS) {
					numBarracks++;
				}
			}
			rc.broadcast(0, numBeavers);
			rc.broadcast(1, numSoldiers);
			rc.broadcast(2, numBashers);
			rc.broadcast(100, numBarracks);
			*/
			if (rc.isWeaponReady()) {
				attackSomething();
			}

			if (rc.isCoreReady() && rc.getTeamOre() >= 100 && fate < Math.pow(1.2,12-numBeavers)*10000) {
				trySpawn(directions[rand.nextInt(8)], RobotType.BEAVER);
			}
		} catch (Exception e) {
			System.out.println("HQ Exception");
			e.printStackTrace();
		}
	}
	
	static void doTower()
	{
		try {
			if (rc.isWeaponReady()) {
				attackSomething();
			}
		} catch (Exception e) {
			System.out.println("Tower Exception");
			e.printStackTrace();
		}
	}
	
	static void doDrone()
	{
		try {
			updateSquadInfo();
			if (rc.isWeaponReady()) {
				attackSomething();
			}
		} catch (Exception e) {
			System.out.println("Drone Exception");
			e.printStackTrace();
		}
	}

	static void doBasher()
	{
		try {
			RobotInfo[] adjacentEnemies = rc.senseNearbyRobots(2, enemyTeam);

			// BASHERs attack automatically, so let's just move around mostly randomly
		if (rc.isCoreReady()) {
			int fate = rand.nextInt(1000);
			if (fate < 800) {
				tryMove(directions[rand.nextInt(8)]);
			} else {
				tryMove(rc.getLocation().directionTo(rc.senseEnemyHQLocation()));
			}
		}
		} catch (Exception e) {
			System.out.println("Basher Exception");
			e.printStackTrace();
		}
	}

	static void doSoldier()
	{
		try {
			if (rc.isWeaponReady()) {
				attackSomething();
			}
			if (rc.isCoreReady()) {
				int fate = rand.nextInt(1000);
				if (fate < 800) {
					tryMove(directions[rand.nextInt(8)]);
				} else {
					tryMove(rc.getLocation().directionTo(rc.senseEnemyHQLocation()));
				}
			}
		} catch (Exception e) {
			System.out.println("Soldier Exception");
			e.printStackTrace();
		}
	}

	static void doBeaver()
	{
		try {
			if (rc.isWeaponReady()) {
				attackSomething();
			}
			if (rc.isCoreReady()) {
				int fate = rand.nextInt(1000);
				if (fate < 8 && rc.getTeamOre() >= 300) {
					tryBuild(directions[rand.nextInt(8)],RobotType.BARRACKS);
				} else if (fate < 600) {
					rc.mine();
				} else if (fate < 900) {
					tryMove(directions[rand.nextInt(8)]);
				} else {
					tryMove(rc.senseHQLocation().directionTo(rc.getLocation()));
				}
			}
			rc.setIndicatorString(0,Integer.toString(myRounds));
			if (myRounds < 200)
			{
				MapLocation loc = rc.getLocation();
				/*MapLocation[] locs = MapLocation.getAllMapLocationsWithinRadiusSq(loc,24);
				for (MapLocation l: locs)
				{
					rc.setIndicatorDot(l, 255, 255, 0);
				}
					*/
				
				for (int i=0; i<senseLocsShort; i++)
				{
					rc.setIndicatorDot(new MapLocation(senseLocsX[i]+loc.x,senseLocsY[i]+loc.y), 255, 255, 0);
				}
			}
		} catch (Exception e) {
			System.out.println("Beaver Exception");
			e.printStackTrace();
		}
	}

	static void doBarracks()
	{
		try {
			int fate = rand.nextInt(10000);

			// get information broadcasted by the HQ
			int numBeavers = rc.readBroadcast(0);
			int numSoldiers = rc.readBroadcast(1);
			int numBashers = rc.readBroadcast(2);

			if (rc.isCoreReady() && rc.getTeamOre() >= 60 && fate < Math.pow(1.2,15-numSoldiers-numBashers+numBeavers)*10000) {
				if (rc.getTeamOre() > 80 && fate % 2 == 0) {
					trySpawn(directions[rand.nextInt(8)],RobotType.BASHER);
				} else {
					trySpawn(directions[rand.nextInt(8)],RobotType.SOLDIER);
				}
			}
		} catch (Exception e) {
			System.out.println("Barracks Exception");
			e.printStackTrace();
		}
	}
	
	
	// This method updates all squad-specific info
	static void updateSquadInfo() throws GameActionException
	{
		int squadUnits = rc.readBroadcast(squadUnitsBase + mySquad);
		if (Clock.getRoundNum() > squadUnits >> 16)
			squadUnits = 0;
		squadUnits++;
		rc.broadcast(squadUnitsBase + mySquad, squadUnits);
	}

	

	// This method will attack an enemy in sight, if there is one
	static void attackSomething() throws GameActionException {
		RobotInfo[] enemies = rc.senseNearbyRobots(myRange, enemyTeam);
		if (enemies.length > 0) {
			rc.attackLocation(enemies[0].location);
		}
	}

	// This method will attempt to move in Direction d (or as close to it as possible)
	static void tryMove(Direction d) throws GameActionException {
		int offsetIndex = 0;
		int[] offsets = {0,1,-1,2,-2};
		int dirint = directionToInt(d);
		boolean blocked = false;
		while (offsetIndex < 5 && !rc.canMove(directions[(dirint+offsets[offsetIndex]+8)%8])) {
			offsetIndex++;
		}
		if (offsetIndex < 5) {
			rc.move(directions[(dirint+offsets[offsetIndex]+8)%8]);
		}
	}

	// This method will attempt to spawn in the given direction (or as close to it as possible)
	static void trySpawn(Direction d, RobotType type) throws GameActionException {
		int offsetIndex = 0;
		int[] offsets = {0,1,-1,2,-2,3,-3,4};
		int dirint = directionToInt(d);
		boolean blocked = false;
		while (offsetIndex < 8 && !rc.canSpawn(directions[(dirint+offsets[offsetIndex]+8)%8], type)) {
			offsetIndex++;
		}
		if (offsetIndex < 8) {
			rc.spawn(directions[(dirint+offsets[offsetIndex]+8)%8], type);
		}
	}

	// This method will attempt to build in the given direction (or as close to it as possible)
	static void tryBuild(Direction d, RobotType type) throws GameActionException {
		int offsetIndex = 0;
		int[] offsets = {0,1,-1,2,-2,3,-3,4};
		int dirint = directionToInt(d);
		boolean blocked = false;
		while (offsetIndex < 8 && !rc.canMove(directions[(dirint+offsets[offsetIndex]+8)%8])) {
			offsetIndex++;
		}
		if (offsetIndex < 8) {
			rc.build(directions[(dirint+offsets[offsetIndex]+8)%8], type);
		}
	}

	static int directionToInt(Direction d) {
		switch(d) {
			case NORTH:
				return 0;
			case NORTH_EAST:
				return 1;
			case EAST:
				return 2;
			case SOUTH_EAST:
				return 3;
			case SOUTH:
				return 4;
			case SOUTH_WEST:
				return 5;
			case WEST:
				return 6;
			case NORTH_WEST:
				return 7;
			default:
				return -1;
		}
	}
}
