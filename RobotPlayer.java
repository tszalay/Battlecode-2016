package grid366;

import battlecode.common.*;

import java.util.*;
import java.lang.*;

/* Sensing location defines etc */
class RobotConsts
{
	int senseLocsShort = 69;
	int senseLocsLong = 109;
	int[] senseLocsX = {0,-1,0,0,1,-1,-1,1,1,-2,0,0,2,-2,-2,-1,-1,1,1,2,2,-2,-2,2,2,-3,0,0,3,-3,-3,-1,-1,1,1,3,3,-3,-3,-2,-2,2,2,3,3,-4,0,0,4,-4,-4,-1,-1,1,1,4,4,-3,-3,3,3,-4,-4,-2,-2,2,2,4,4,-5,-4,-4,-3,-3,0,0,3,3,4,4,5,-5,-5,-1,-1,1,1,5,5,-5,-5,-2,-2,2,2,5,5,-4,-4,4,4,-5,-5,-3,-3,3,3,5,5};
	int[] senseLocsY = {0,0,-1,1,0,-1,1,-1,1,0,-2,2,0,-1,1,-2,2,-2,2,-1,1,-2,2,-2,2,0,-3,3,0,-1,1,-3,3,-3,3,-1,1,-2,2,-3,3,-3,3,-2,2,0,-4,4,0,-1,1,-4,4,-4,4,-1,1,-3,3,-3,3,-2,2,-4,4,-4,4,-2,2,0,-3,3,-4,4,-5,5,-4,4,-3,3,0,-1,1,-5,5,-5,5,-1,1,-2,2,-5,5,-5,5,-2,2,-4,4,-4,4,-3,3,-5,5,-5,5,-3,3};
	int[] senseLocsR = {0,10,10,10,10,14,14,14,14,20,20,20,20,22,22,22,22,22,22,22,22,28,28,28,28,30,30,30,30,31,31,31,31,31,31,31,31,36,36,36,36,36,36,36,36,40,40,40,40,41,41,41,41,41,41,41,41,42,42,42,42,44,44,44,44,44,44,44,44,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,53,53,53,53,53,53,53,53,56,56,56,56,58,58,58,58,58,58,58,58};
	float[] sqrt = {0.000000f,1.000000f,1.414214f,1.732051f,2.000000f,2.236068f,2.449490f,2.645751f,2.828427f,3.000000f,3.162278f,3.316625f,3.464102f,3.605551f,3.741657f,3.872983f,4.000000f,4.123106f,4.242641f,4.358899f,4.472136f,4.582576f,4.690416f,4.795832f,4.898979f,5.000000f,5.099020f,5.196152f,5.291503f,5.385165f,5.477226f,5.567764f,5.656854f,5.744563f,5.830952f,5.916080f,6.000000f,6.082763f,6.164414f,6.244998f,6.324555f,6.403124f,6.480741f,6.557439f,6.633250f,6.708204f,6.782330f,6.855655f,6.928203f,7.000000f,7.071068f,7.141428f,7.211103f,7.280110f,7.348469f,7.416198f,7.483315f,7.549834f,7.615773f,7.681146f,7.745967f,7.810250f,7.874008f,7.937254f,8.000000f,8.062258f,8.124038f,8.185353f,8.246211f,8.306624f,8.366600f,8.426150f,8.485281f,8.544004f,8.602325f,8.660254f,8.717798f,8.774964f,8.831761f,8.888194f,8.944272f,9.000000f};
	float[] invSqrt = {0.000000f,1.000000f,0.707107f,0.577350f,0.500000f,0.447214f,0.408248f,0.377964f,0.353553f,0.333333f,0.316228f,0.301511f,0.288675f,0.277350f,0.267261f,0.258199f,0.250000f,0.242536f,0.235702f,0.229416f,0.223607f,0.218218f,0.213201f,0.208514f,0.204124f,0.200000f,0.196116f,0.192450f,0.188982f,0.185695f,0.182574f,0.179605f,0.176777f,0.174078f,0.171499f,0.169031f,0.166667f,0.164399f,0.162221f,0.160128f,0.158114f,0.156174f,0.154303f,0.152499f,0.150756f,0.149071f,0.147442f,0.145865f,0.144338f,0.142857f,0.141421f,0.140028f,0.138675f,0.137361f,0.136083f,0.134840f,0.133631f,0.132453f,0.131306f,0.130189f,0.129099f,0.128037f,0.127000f,0.125988f,0.125000f,0.124035f,0.123091f,0.122169f,0.121268f,0.120386f,0.119523f,0.118678f,0.117851f,0.117041f,0.116248f,0.115470f,0.114708f,0.113961f,0.113228f,0.112509f,0.111803f,0.111111f};
	
	int[] towerMask = {0,0,128,129,131,131,131,3,2,0,0,0,128,193,195,199,199,199,135,7,2,0,128,193,227,247,255,255,255,223,143,7,2,192,225,247,255,255,255,255,255,223,15,6,224,241,255,255,255,255,255,255,255,31,14,224,241,255,255,255,255,255,255,255,31,14,224,241,255,255,255,255,255,255,255,31,14,96,240,253,255,255,255,255,255,127,30,12,32,112,248,253,255,255,255,127,62,28,8,0,32,112,120,124,124,124,60,28,8,0,0,0,32,48,56,56,56,24,8,0,0};
	int[] hqMask = {0,0,128,129,131,131,131,131,131,3,2,0,0,0,128,193,195,199,199,199,199,199,135,7,2,0,128,193,227,247,255,255,255,255,255,223,143,7,2,192,225,247,255,255,255,255,255,255,255,223,15,6,224,241,255,255,255,255,255,255,255,255,255,31,14,224,241,255,255,255,255,255,255,255,255,255,31,14,224,241,255,255,255,255,255,255,255,255,255,31,14,224,241,255,255,255,255,255,255,255,255,255,31,14,224,241,255,255,255,255,255,255,255,255,255,31,14,96,240,253,255,255,255,255,255,255,255,127,30,12,32,112,248,253,255,255,255,255,255,127,62,28,8,0,32,112,120,124,124,124,124,124,60,28,8,0,0,0,32,48,56,56,56,56,56,24,8,0,0};
	int[] dirOffsets = {0,7,1,6,2,5,3,4};
	
	// maps position offsets in our 5x5 bit-grid to direction flags
	int[] gridToDir = { 0,   0,   0,   0,   0,
					    0,1<<7,1<<0,1<<1,   0,
					    0,1<<6,   0,1<<2,   0,
					    0,1<<5,1<<4,1<<3,   0,
					    0,   0,   0,   0,   0 };

	/* Grid weight values */
	int WEIGHT_UNKNOWN_EDGE = -10;
	int WEIGHT_ENEMY_HQ = 500;
	int WEIGHT_ENEMY_TOWER = 100;
	int ENEMY_DECAY_RATE = 10;
	
	int WEIGHT_ORE_MINER = -50;
	int WEIGHT_ORE_BEAVER = -30;
	
	int MINING_DECAY = -40;
	int RAGE_DECAY = -1;
	
	/* Ordinal unit weight array */
	int[] friendWeights = {
			200, // HQ 
			0, // TOWER
			0, // SUPPLYDEPOT
			0, // TECHNOLOGYINSTITUTE
			0, // BARRACKS
			0, // HELIPAD
			0, // TRAININGFIELD
			0, // TANKFACTORY
			0, // MINERFACTORY
			0, // HANDWASHSTATION
			0, // AEROSPACELAB
			0, // BEAVER
			0, // COMPUTER
			0, // SOLDIER
			0, // BASHER
			0, // MINER
			0, // DRONE
			0, // TANK
			0, // COMMANDER
			0, // LAUNCHER
			0  // MISSILE
	};
	int[] enemyWeights = {
			0, // HQ 
			0, // TOWER
			0, // SUPPLYDEPOT
			0, // TECHNOLOGYINSTITUTE
			0, // BARRACKS
			0, // HELIPAD
			0, // TRAININGFIELD
			0, // TANKFACTORY
			0, // MINERFACTORY
			0, // HANDWASHSTATION
			0, // AEROSPACELAB
			2000, // BEAVER
			0, // COMPUTER
			1000, // SOLDIER
			1000, // BASHER
			2000, // MINER
			1000, // DRONE
			900, // TANK
			2000, // COMMANDER
			600, // LAUNCHER
			0  // MISSILE
	};
}

public class RobotPlayer {
	
	static RobotController rc;
	
	// Game-given values
	static Team myTeam;
	static Team enemyTeam;
	static RobotType myType;
	
	// Our assigned values n stuff
	static int myCID;
	static int myGameID;
	
	static int myRageTarget;
	
	// did we move since last turn?
	static boolean justMoved;
	
	static int curRound;
	
	static MapLocation myTarget;
	
	static MapLocation myLocation;
	static Direction facing;
	static GridComponent myGrid;
	static int lastGridInd=-1;

	// standard defines
	static MapLocation center;
	
	static MapLocation myHQ;
	static MapLocation enemyHQ;
	
	// map symmetry
	// 0 - rotation/diag
	// 1 - rotation/horiz flip
	// 2 - rotation/vert flip
	// 3 - rotation
	// 4 - h flip
	// 5 - v flip
	// 6 - diag x = y
	// 7 - diag x = -y
	static int mapSymmetry;
	// what we read last time we did this
	static int lastGridExtents;
	// minimum and maximum x/y coords of grid elements
	static int gridMinX;
	static int gridMinY;
	static int gridMaxX;
	static int gridMaxY;
	// and the total number of grid cells in a rectangular grid
	static int gridNum;
	
	static Random rand;
	static Direction[] directions = {Direction.NORTH, Direction.NORTH_EAST, Direction.EAST, Direction.SOUTH_EAST, Direction.SOUTH, Direction.SOUTH_WEST, Direction.WEST, Direction.NORTH_WEST};
	static int myRounds = 0;
	
	
	// sizes of all of our arrays
	static final int MAX_SQUADS = 16;
	
	static final int CID_NUM = 2000;
	
	// grid, not map
	// how many map elements per grid element?
	static final int GRID_SPC = 5;
	// and what is required to sense the whole grid?
	static final int GRID_SENSE = 2*(GRID_SPC/2)*(GRID_SPC/2);
	// max possible extents in one direction
	// needs to be at least 240/GRID_SPC
	// (make sure we've very nice and padded)
	static final int GRID_DIM = (240/GRID_SPC)+4;
	// and how many elements in base grid. 784 total, of which we will use at most 196
	static final int GRID_NUM = GRID_DIM*GRID_DIM;
	// offset to get to upper-right corner of grid
	static final int GRID_OFFSET = GRID_DIM*GRID_SPC/2;
	// mask for first 25 bits
	static final int GRID_MASK = (1<<25)-1;
	static final int EDGE_MASK = (1<<5)-1;
	// bitmask for center of each edge
	static final int GRID_EDGE_CENTERS = (1<<2)|(1<<22)|(1<<10)|(1<<14);
	// number of subgrid cells
	static final int GRID_N = GRID_SPC*GRID_SPC;

	
	// update frequencies
	static final int GRID_DIFFUSE_FREQ = 2;
	static final int GRID_CONNECTIVITY_FREQ = 3;
	static final int GRID_UPDATE_FREQ = 2;
	static final int GRID_ORE_FREQ = 5;
	static final int GRID_MINING_FREQ = 4;
	static final int GRID_RAGE_FREQ = 2;
	
	static final int GRID_MAX_CC = 4;
	static final int GRID_CC_MASK = 7;
	
	// note for status flags: lowest 2-3 bits are CC, upper 16 bits are gridid base 
	// rest can be flags just fine and dandy
	
	// have we been there or seen it?
	static final int STATUS_SEEN = (1<<4);
	static final int STATUS_VISITED = (1<<5);
	// have we updated the connectivity recently?
	static final int STATUS_PATHED = (1<<6);
	// is there an enemy HQ/tower there? combination of 7 bits total
	static final int STATUS_HQ = (1<<7);
	// tower is defined by six bits, flagging which towers might hit this square
	static final int STATUS_TOWER = (1<<8);
	static final int STATUS_TOWERS_MASK = ((1<<7)-1)<<7;
	
	static final int STATUS_NORTH=0;
	static final int STATUS_SOUTH=3;
	static final int STATUS_EAST=1;
	static final int STATUS_WEST=2;
	static final int STATUS_NE=4;
	static final int STATUS_SE=5;
	static final int STATUS_SW=6;
	static final int STATUS_NW=7;
	
	static int curChan = 0;
	
	/* Contig ID counting, to use for enemies and friends */
	static final int nextCIDChan = curChan++;
	static final int cidBase = curChan; static {curChan += CID_NUM;}
		
	/* Map info */
	// encodes known/unknown map symmetry
	static final int mapSymmetryChan = curChan++;
	// packed grid min/max values in each direction
	static final int gridExtentsChan = curChan++;

	/* Grid info */
	
	// map array of basic grid square status
	static final int gridInfoBase = curChan; static {curChan+=GRID_NUM;}
	// map list of next grid IDs
	static final int gridNextIDBase = curChan; static {curChan+=GRID_NUM;}
	static final int gridNextIDChan = curChan++;	

	// FRIEND-ENEMY GRID TOTALS
	static final int gridLastUpdateChan = curChan++;
	static final int gridUpdatePtrChan = curChan++;
	// AND THEIR VALUES
	static final int gridFriendBase = curChan; static {curChan+=GRID_NUM;}
	static final int gridEnemyBase = curChan; static {curChan+=GRID_NUM;}

	// ORE GRID TOTALS
	static final int gridLastOreChan = curChan++;
	static final int gridOrePtrChan = curChan++;
	// AND THEIR VALUES
	static final int gridOreBase = curChan; static {curChan+=GRID_NUM;}
	//static final int gridEnemyBase = curChan; static {curChan+=GRID_NUM;}
	
	
	// THE "POTENTIAL" DIFFUSIVE GRID VALUES & LOOP
	static final int gridLastPotentialChan = curChan++;
	static final int gridPotentialPtrChan = curChan++;
	static final int gridPotentialTotalChan = curChan++;
	static final int gridPotentialOffsetChan = curChan++;
	static final int gridPotentialBase = curChan; static {curChan+=GRID_NUM;}
	
	// MINING-ORE GRID
	// THE "POTENTIAL" DIFFUSIVE GRID VALUES & LOOP
	static final int gridLastMiningChan = curChan++;
	static final int gridMiningPtrChan = curChan++;
	static final int gridMiningBase = curChan; static {curChan+=GRID_NUM;}
	
	// TARGET LIST GRID
	static final int gridLastRageChan = curChan++;
	static final int gridRagePtrChan = curChan++;
	static final int gridRageBase = curChan; static {curChan+=GRID_NUM;}
	
	// AND THE CONNECTIVITY GRID LOOP
	static final int gridLastConnectivityChan = curChan++;
	static final int gridPtrChan = curChan++;
	// these ones are indexed by location "properties"
	static final int gridNormalBase = curChan; static {curChan+=GRID_NUM;}
	static final int gridKnownBase = curChan; static {curChan+=GRID_NUM;}
	static final int gridVoidBase = curChan; static {curChan+=GRID_NUM;}
	// and these ones are indexed by ID "values"
	static final int gridPathableBase = curChan; static {curChan+=GRID_NUM;}
	static final int gridEdgesNSBase = curChan; static {curChan+=GRID_NUM;}
	static final int gridEdgesEWBase = curChan; static {curChan+=GRID_NUM;}
	
	//===============================================================================================================
	
	// number of targets in rage list
	static final int TARGET_COUNT = 2;
	
	static final int rageIDBase = curChan; static {curChan+=TARGET_COUNT;}
	static final int rageGridBase = curChan; static {curChan+=TARGET_COUNT;}
	
	//===============================================================================================================
	
	// Keep track of best mine, score and location
	static int bestMineScoreChan = curChan++;
	static int bestMineXChan = curChan++;
	static int bestMineYChan = curChan++;
	
	// Keep track of number of each type of miner
	static int minersSupplying = curChan++;
	static int minersSearching = curChan++;
	static int minersLeading = curChan++;
	
	// Allow HQ to allocate mining duties
	static int minerShuffle = curChan++;
	
	static final int TARGET_SUPPLYING_MINERS = 10;
	
	// Keep track of miner IDs : using channel 499 and onward
	static int minerContiguousID = 499;
	static int myMinerID;
	
	// Supply transfer parameter
	static double supplyTransferFraction = 0.5;

	// Miner-specific
	static enum MinerState
	{
		SUPPLYING,	// supply line, usually stationary
		SEARCHING,	// executing a local ore finding algorithm, and generally following leaders
		LEADING,	// mining and moving forward only
	}
	static String toString(MinerState state) {
		String ans = null;
		switch(state)
		{
		case SUPPLYING:
			ans = "SUPPLYING";
			break;
		case SEARCHING:
			ans =  "SEARCHING";
			break;
		case LEADING:
			ans =  "LEADING";
			break;
		}
		return ans;
	}
	static MinerState minerState;
//	public RobotPlayer(MinerState state){ // each robot has a MinerState field accessed by rc.minerState
//		this.minerState = state;
//	}
	static double lastLocationStartingOre;
	static double currentLocationStartingOre;
	static boolean bestMine;
	static double oreHere;
	static int[] minersPerState = {0,0,0}; // initially no miners: minersPerState[0]=SUPPLYING, minersPerState[1]=SEARCHING, minersPerState[2]=LEADING
	
	//===========================================================================================

	
	// Unit Power Rankings (unitless) 
	// HQ, TOWER, SD, TECH, RACKS, HELI, FIELD, TFACT, MFACT, WASH, SPACE, BEAV, COMP, SOLDIER, BASH, MINE, DRONE, TANK, COMM, LAUNCH, MISSILE
		static float unitVal[] = {30.0f, 20.0f, 0.0f, 0.0f, 0.0f, 0.0f,0.0f, 0.0f,0.0f, 0.0f,0.0f, 0.6f, 0.0f, 1.0f, 2.3f, 0.6f, 1.2f, 3.8f, 8.0f, 10.0f, 0.2f};
		
	// Unit Attack Affinities (unitless)
	static float unitAtt[] = {0.0f, 0.0f, 1.0f, 1.0f,4.0f, 4.0f,1.0f, 4.0f,5.0f, 1.0f,1.0f, 3.0f, 1.0f, 2.0f, 2.0f, 3.0f, 1.0f, 1.0f, 1.0f, 1.0f,1.0f};

	// Shoot Impatience - Sweeping 
	static int shootImpatience = 100; // in clicks
	static int lastShotCounter = 0;
	static int sweepCounter = 0;
	static int sweeping = 0;
	static int sweepBoredomLength = 50; // in clicks
	
	
	// Move Impatience Counter
	static int moveImpatience = 15; // in clicks
	static int moveRecSize = 2000;
	static MapLocation moveRec[] = new MapLocation[moveRecSize];
	static int moveIdx = 3; //start at 3 so it doesnt crash
	static int lastMoveCounter = 0;
	static int bored;
	static int boredCounter = 0;
	static int moveBoredomLength = 20;
	static int boredomLength = 20;
	static int boredDirIdx;
	
	// agg coefficient
	static float aggCoef = 0.8f;

	//===========================================================================================	
	
	// Adjustable parameters
    static int numBeavers = 8;
    static int numMinerFactories = 1;
    static int numMiners = 100;
    static int numBarracks = 2;
    static int numSoldiers = 20;
    static int numHelipads = 0;
	static int numSupplyDepots = 0;
	static int numTankFactories = 5;
	static int numTanks = 50;

	
	
	static RobotConsts Consts = new RobotConsts();
	
	static MapLocation[] enemyBuildings;
	
	static double lastOre = 0;
	static double curOre = 0;
	
	
	static final int[] gridOffset = {-GRID_DIM,1,-1,GRID_DIM,-GRID_DIM+1,GRID_DIM+1,GRID_DIM-1,-GRID_DIM-1};
	static final int[] gridOffX = {-2,-1,0,1,2,-2,-1,0,1,2,-2,-1,0,1,2,-2,-1,0,1,2,-2,-1,0,1,2};
	static final int[] gridOffY = {-2,-2,-2,-2,-2,-1,-1,-1,-1,-1,0,0,0,0,0,1,1,1,1,1,2,2,2,2,2};
	static final int[] dirOffX = {0,1,-1,0,1,1,-1,-1};
	static final int[] dirOffY = {-1,0,0,1,-1,1,1,-1};

	static int[] bitAdjacency = {99,231,462,924,792,3171,7399,14798,29596,25368,101472,236768,473536,947072,811776,3247104,7576576,15153152,30306304,25976832,3244032,7569408,15138816,30277632,25952256};
	// upper 4 are diagonals, next 4 are their opposites
	static int[] bitEdge = {31,17318416,1082401,32505856,(1<<4),(1<<24),(1<<20),(1<<0),(1<<20),(1<<0),(1<<4),(1<<24)};
	
	static final int[] edgeChans = {gridEdgesNSBase,gridEdgesEWBase,gridEdgesEWBase,gridEdgesNSBase};

	
	// used for status displays on grid drawing functions
	static int[] colorR = {1, 0, 0, 0};
	static int[] colorG = {0, 1, 0, 1};
	static int[] colorB = {0, 0, 1, 1};
	
	
	static class MapValue implements Comparable<MapValue>
	{
		int x;
		int y;
		double value;
		
		public MapValue(int lx, int ly, double val)
		{
			this.x = lx;
			this.y = ly;
			this.value = val;
		}
		
		public int compareTo(MapValue mv)
		{
			return Double.compare(this.value, mv.value);
		}
		
		public MapLocation offsetFrom(MapLocation ml)
		{
			return new MapLocation(x+ml.x,y+ml.y);
		}
		
		public boolean equals(MapLocation ml)
		{
			return (ml.x == this.x) && (ml.y == this.y);
		}
		
		public String toString()
		{
			return new MapLocation(this.x,this.y).toString();
		}
	}
	

	public static void run(RobotController robotc)
	{
		rc = robotc;
		
		//while (lastTowers == 0)
		//	rc.yield();
		//System.out.println(java.util.Arrays.asList(RobotType.values()));
		// Initialization code
		try {
			
			init();
			
			switch (myType)
			{
			case HQ:
				System.out.println(curChan + " channels in use");
				
				rc.broadcast(minerContiguousID,500);
				
				mapHQ();
				// initialize pointers
				rc.broadcast(gridNextIDChan, 1);
				int ptr = gridMinX+gridMinY*GRID_DIM;
				rc.broadcast(gridPtrChan, ptr);
				rc.broadcast(gridMiningPtrChan, ptr);
				rc.broadcast(gridPotentialPtrChan, ptr);
				rc.broadcast(gridUpdatePtrChan, ptr);
				rc.broadcast(gridOrePtrChan, ptr);
				rc.broadcast(gridRagePtrChan, ptr);
				
				// initialize to empty, so we fill it and do stuff
				enemyBuildings = new MapLocation[0];
				break;
			case BEAVER:
				facing = rc.getLocation().directionTo(myHQ).opposite();
				break;
			case MINER:
				minerState = MinerState.SUPPLYING; // initially miners are in supply chain
				myMinerID = rc.readBroadcast(minerContiguousID); // obtain a minerID
				rc.broadcast(minerContiguousID, myMinerID + 1); // update next minerID
				rc.broadcast(myMinerID, rc.getID()); // save my robot ID on the message board
				break;
			case DRONE:
				break;
			}
		} catch (Exception e) {
			System.out.println("Initialization exception");
			e.printStackTrace();
		}
		
		while(true) {
			
			curRound = Clock.getRoundNum();
			
			try {
				update();
			} catch (Exception e) {
				System.out.println("General update exception");
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
			case HELIPAD:
				doHelipad();
				break;
			case BARRACKS:
				doBarracks();
				break;
			case MINERFACTORY:
				doMinerFactory();
				break;
			case MINER:
				doMiner();
				break;
			case DRONE:
				doDrone();
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
			case TANK:
				doTank();
				break;
			case TANKFACTORY:
				doTankFactory();
				break;
			}
			
			lastOre = curOre;

			// IMPORTANT SUPPLY TRANSFER CHANGE =======================================================================================================
			
			lastOre = curOre;
			int bytecodesLeft = Clock.getBytecodesLeft();
			if(bytecodesLeft>1200) // if you try to transfer supply and are out of bytecodes, you will queue a transfer to a location that might not be valid next round.  things move.
			{
				try {
					transferSupplies(supplyTransferFraction);
				} catch (Exception e) {
					System.out.println("Supply exception: " + myType.toString() + ".  " + bytecodesLeft + " bytecodes before transferSupplies() call.");
					//e.printStackTrace();
				}
			}
			
			//==========================================================================================================================================
			
			try {
//				rc.setIndicatorString(0,"Gradient: " + gridGradient(myGridInd) + ", " + rc.readBroadcast(gridPotentialBase+myGridInd) + "," + "Offset: " + rc.readBroadcast(gridPotentialOffsetChan)
//						+ ", Friend: " + rc.readBroadcast(gridFriendBase+myGridInd) + ", Enemy:" + rc.readBroadcast(gridEnemyBase+myGridInd));
				spare();
			} catch (Exception e) {
				System.out.println("Grid exception");
				e.printStackTrace();
			}

			if (Clock.getRoundNum() == curRound)
				rc.yield();
			/*else
				System.out.println("Bytecode wraparound error!");*/
		}
	}
	
	// what to do in extra cycles
	static void spare() throws GameActionException
	{
		while (Clock.getBytecodesLeft() > 1000 && Clock.getRoundNum() == curRound)
		{
			switch (rand.nextInt(6))
			{
			case 0:
				gridConnectivity();
				break;
			case 1:
				gridDiffuse();
				break;
			case 2:
				gridUpdate();
				break;
			case 3:
				gridOre();
				break;
			case 4:
				gridMining();
				break;
			case 5:
				gridRage();
				break;
			}
		}
	}
	
	// general initialization, for everyone
	static void init() throws GameActionException
	{
		rand = new Random();
		myTeam = rc.getTeam();
		enemyTeam = myTeam.opponent();
		myType = rc.getType();
		
		// calculate center of map, as defined for everyone
		myHQ = rc.senseHQLocation();
		enemyHQ = rc.senseEnemyHQLocation();
		
		myTarget = enemyHQ;
		
		center = new MapLocation((myHQ.x+enemyHQ.x)/2,(myHQ.y+enemyHQ.y)/2);

		facing = getRandomDirection();
		
		// initialize things
		lastOre = rc.getTeamOre();
		curOre = lastOre;
		
		// do contiguous ID calculation
		myGameID = rc.getID();
		myCID = rc.readBroadcast(nextCIDChan);
		rc.broadcast(nextCIDChan,myCID+1);
		rc.broadcast(cidBase+myCID, myGameID);
		
		// load map extents
		getExtents();
	}
	
	static void update() throws GameActionException
	{
		MapLocation newloc = rc.getLocation();
		if (!newloc.equals(myLocation))
		{
			myLocation = newloc;
			justMoved = true;
		}
		else
		{
			justMoved = false;
		}
		
		getExtents();

		curOre = rc.getTeamOre();
		
		if (justMoved)
		{
			// update internal grid coordinate values
			int gridX = (GRID_OFFSET+myLocation.x-center.x+GRID_SPC/2)/GRID_SPC;
			int gridY = (GRID_OFFSET+myLocation.y-center.y+GRID_SPC/2)/GRID_SPC;
			int gridInd = gridY*GRID_DIM + gridX;
			
			// did we change and stuff?
			if (myGrid == null || gridInd != myGrid.gridIndex)
			{
				myGrid = new GridComponent(gridInd);
				gridVisited(myGrid);
			}
			
			myGrid.findComponent(myLocation);
		}
	}
	
	
	// calculate symmetry of map, etc, for HQ only

	// 0 - rotation/diag
	// 1 - rotation/horiz flip
	// 2 - rotation/vert flip
	// 3 - rotation
	// 4 - h flip
	// 5 - v flip
	// 6 - diag x = y
	// 7 - diag x = -y
	// about 1300 bytecodes for this function
	static void mapHQ() throws GameActionException
	{
		// get each set of buildings
		MapLocation[] myblds = getBuildings(myTeam);
		MapLocation[] enblds = getBuildings(myTeam.opponent());
		// 0 is HQ location
		// check horizontal/vertical first, easy
		if (myblds[0].x == enblds[0].x)
		{
			mapSymmetry = 1;
		}
		else if (myblds[0].y == enblds[0].y)
		{
			mapSymmetry = 2;
		}
		else // diag or rotation
		{
			if (Math.abs(enblds[0].x - myblds[0].x) != Math.abs(enblds[0].y - myblds[0].y))
				mapSymmetry = 3; // has to be rotational
			else
				mapSymmetry = 0; // diagonal/rotational still
		}
		
		// and save it, why not
		rc.broadcast(mapSymmetryChan, mapSymmetry);
		
		// get min and max bounds of visible buildings
		Arrays.sort(myblds, new Comparator<MapLocation>() {
		    public int compare(MapLocation ml1, MapLocation ml2) {
	 	      return Integer.compare(ml1.x,ml2.x);}});
		Arrays.sort(enblds, new Comparator<MapLocation>() {
		    public int compare(MapLocation ml1, MapLocation ml2) {
	 	      return Integer.compare(ml1.x,ml2.x);}});
		
		int mapMinX = Math.min(myblds[0].x,enblds[0].x);
		int mapMaxX = Math.max(myblds[myblds.length-1].x,enblds[enblds.length-1].x);
		
		Arrays.sort(myblds, new Comparator<MapLocation>() {
		    public int compare(MapLocation ml1, MapLocation ml2) {
	 	      return Integer.compare(ml1.y,ml2.y);}});
		Arrays.sort(enblds, new Comparator<MapLocation>() {
		    public int compare(MapLocation ml1, MapLocation ml2) {
	 	      return Integer.compare(ml1.y,ml2.y);}});

		int mapMinY = Math.min(myblds[0].y,enblds[0].y);
		int mapMaxY = Math.max(myblds[myblds.length-1].y,enblds[enblds.length-1].y);

		mapMinX -= center.x;
		mapMaxX -= center.x;
		mapMinY -= center.y;
		mapMaxY -= center.y;
		
		// now get grid extents, as best we know them
		gridMinX = (mapMinX+GRID_OFFSET+GRID_SPC/2)/GRID_SPC;
		gridMinY = (mapMinY+GRID_OFFSET+GRID_SPC/2)/GRID_SPC;
		gridMaxX = (mapMaxX+GRID_OFFSET+GRID_SPC/2)/GRID_SPC;
		gridMaxY = (mapMaxY+GRID_OFFSET+GRID_SPC/2)/GRID_SPC;
		gridNum = (gridMaxX-gridMinX+1)*(gridMaxY-gridMinY+1);

		setExtents();
	}
	
	static void setExtents() throws GameActionException
	{
		// pack them and set them
		int gridextents = (gridMinX&255) | ((gridMaxX&255)<<8) | ((gridMinY&255)<<16) | ((gridMaxY&255)<<24);
		rc.broadcast(gridExtentsChan, gridextents);
	}
	
	// reads map extents from global mapextents channel
	static void getExtents() throws GameActionException
	{
		// unpack map extents
		int gridextents = rc.readBroadcast(gridExtentsChan);
		
		// nothing new
		if (gridextents == lastGridExtents)
			return;
		
		lastGridExtents = gridextents;
		
		gridMinX = (int)(byte)((gridextents)&255);
		gridMaxX = (int)(byte)((gridextents>>8)&255);
		gridMinY = (int)(byte)((gridextents>>16)&255);
		gridMaxY = (int)(byte)((gridextents>>24)&255);
		
		// grid extents x/y:
		// and minimum number in order to populate whole map
		gridNum = (gridMaxX-gridMinX+1)*(gridMaxY-gridMinY+1);
		System.out.println("Grid size: " + gridNum);

		// and symmetry
		mapSymmetry = rc.readBroadcast(mapSymmetryChan);
	}
	
	// get all buildings, including HQ, in a single uniform list
	static MapLocation[] getBuildings(Team team)
	{
		MapLocation[] buildings;
		if (team == myTeam)
		{
			MapLocation[] towers = rc.senseTowerLocations();
			MapLocation hq = rc.senseHQLocation();
			buildings = new MapLocation[towers.length+1];
			System.arraycopy(towers,0,buildings,1,towers.length);
			buildings[0] = hq;
		}
		else
		{
			MapLocation[] towers = rc.senseEnemyTowerLocations();
			MapLocation hq = rc.senseEnemyHQLocation();
			buildings = new MapLocation[towers.length+1];
			System.arraycopy(towers,0,buildings,1,towers.length);
			buildings[0] = hq;
		}
		return buildings;
	}
	
	
	static void doHQ()
	{
		try
		{
			rc.setIndicatorString(1,"Current ore: " + curOre);
			if (rc.isWeaponReady())
				attackSomething();
			
			// update enemy buildings every once in a while
			
			if (Clock.getRoundNum()%10 == 0)
			{
				MapLocation[] buildings = getBuildings(myTeam.opponent());
				if (buildings.length != enemyBuildings.length)
				{
					// something changed. unset old buildings, set new buildings
					for (int i=0; i<enemyBuildings.length; i++)
					{
						int gridind = gridIndex(enemyBuildings[i]);
						int gridinfo = rc.readBroadcast(gridInfoBase+gridind);
						gridinfo &= ~(i==0?STATUS_HQ:STATUS_TOWER);
						rc.broadcast(gridInfoBase+gridind,gridinfo);
					}
					for (int i=0; i<buildings.length; i++)
					{
						int gridind = gridIndex(buildings[i]);
						int gridinfo = rc.readBroadcast(gridInfoBase+gridind);
						gridinfo |= (i==0?STATUS_HQ:STATUS_TOWER);
						rc.broadcast(gridInfoBase+gridind,gridinfo);
					}
					enemyBuildings = buildings;
				}
			}
			
			// figure out miner state updates ======================================================================================
			int roundNow = Clock.getRoundNum();
			
			// suppliers
			int supplierCount = rc.readBroadcast(minersSupplying);
			if(supplierCount >> 16 == roundNow - 1){ // if the most recent update was last round
				supplierCount = supplierCount & 255; // bit shifting nonsense
			}
			else
				supplierCount = 0; // no units reporting, all are dead
			minersPerState[0] = supplierCount;
			
			// searchers
			int searcherCount = rc.readBroadcast(minersSearching);
			if(searcherCount >> 16 == roundNow - 1){ // if the most recent update was last round
				searcherCount = searcherCount & 255; // bit shifting nonsense
			}
			else
				searcherCount = 0; // no units reporting, all are dead
			minersPerState[1] = searcherCount;
			
			// leaders
			int leaderCount = rc.readBroadcast(minersLeading);
			if(leaderCount >> 16 == roundNow - 1) // if the most recent update was last round
				leaderCount = leaderCount & 255; // bit shifting nonsense
			else
				leaderCount = 0; // no units reporting, all are dead
			minersPerState[2] = leaderCount;
			rc.setIndicatorString(0,"Miners supplying: " + supplierCount + ", searching: " + searcherCount + ", leading: " + leaderCount);
			int bestMineScore = rc.readBroadcast(bestMineScoreChan);
			int bestMineX = rc.readBroadcast(bestMineXChan);
			int bestMineY = rc.readBroadcast(bestMineYChan);
			rc.setIndicatorDot(new MapLocation(bestMineX,bestMineY), 0,255,0);
			rc.setIndicatorString(2,"Best mine = " + bestMineScore + " at (" + bestMineX + "," + bestMineY + ")");
			rc.broadcast(bestMineScoreChan, bestMineScore - 2); // slight decay ensures best mine won't last for too long
			
			// if we have too many suppliers, promote a supplier to searcher (numbers track until supplierCount reaches TARGET_SUPPLYING_MINERS)
			if(supplierCount<TARGET_SUPPLYING_MINERS)
				rc.broadcast(minerShuffle, supplierCount - searcherCount); // the miners only react to a positive number
			else
				rc.broadcast(minerShuffle, supplierCount - TARGET_SUPPLYING_MINERS); // the miners only react to a positive number
			
			//========================================================================================================================
			
			RobotInfo[] ourTeam = rc.senseNearbyRobots(1000, rc.getTeam());
			int n = 0; // current number of beavers
			for(RobotInfo ri: ourTeam){ // count up beavers
				if(ri.type==RobotType.BEAVER){
					n++;
				}
			}
			if(Clock.getRoundNum()<300 && n<numBeavers){ // in the beginning, spawn 'numBeavers' beavers and send them out in all directions
				Direction dir = getRandomDirection();
				if(rc.isCoreReady() && rc.canSpawn(dir, RobotType.BEAVER)) {
					rc.spawn(dir, RobotType.BEAVER);
				}
			}
			
		} catch (Exception e) {
			System.out.println("HQ Exception");
			e.printStackTrace();
		}
	}
	
	static void setTowerBits(int gridind, int towerbits) throws GameActionException
	{
		for (int dir=0; dir<8; dir++)
		{
			int gridinfo = rc.readBroadcast(gridInfoBase+gridind+gridOffset[dir]);
			gridinfo |= towerbits;
			rc.broadcast(gridInfoBase+gridind+gridOffset[dir], gridinfo);
		}
	}
	
	static void doTower()
	{
		try {
			if (myLocation.equals(rc.senseTowerLocations()[0]))
				debug_drawGridVals();

			if (rc.isWeaponReady())
			{
				attackSomething();
			}
		} catch (Exception e) {
			System.out.println("Tower Exception");
			e.printStackTrace();
		}
	}
	
	static void doHelipad()
	{
		try {
			if (rand.nextInt(100) < 20)
				spawnUnit(RobotType.DRONE);
		} catch (Exception e) {
			System.out.println("Helipad Exception");
			e.printStackTrace();
		}
	}
	
	static void doMinerFactory()
	{
		try {
			RobotInfo[] bots = rc.senseNearbyRobots(99999, myTeam);
			int nmine = 0;
			for (RobotInfo b: bots)
				if (b.type == RobotType.MINER)
					nmine++;
			
			if (curOre - lastOre < 25 && nmine < numMiners)
			{
				spawnUnit(RobotType.MINER);
			}
		} catch (Exception e) {
			System.out.println("Miner Factory Exception");
			e.printStackTrace();
		}
	}

	
	static void doBarracks()
	{
		try {
			/*RobotInfo[] bots = rc.senseNearbyRobots(99999, myTeam);
			for (RobotInfo b: bots)
				if (b.type == RobotType.SOLDIER)
					return;
*/
			double r = rand.nextDouble();
			if(r < 0.75)
				trySpawn(facing,RobotType.SOLDIER);
			else if(r<0.75)
				trySpawn(facing,RobotType.BASHER);
		} catch (Exception e) {
			System.out.println("Barracks Exception");
			e.printStackTrace();
		}
	}

	
	static void doDrone()
	{
		try {
			attackSomething();
			movePotential();
		} catch (Exception e) {
			System.out.println("Drone Exception");
			e.printStackTrace();
		}
	}

	static void doBasher()
	{
		try {
			//updateSquadInfo();
			if (Clock.getRoundNum()<1800) {
			myTarget = rc.senseEnemyHQLocation();
			}

			else{
				aggCoef = 1000;
				MapLocation towers[] = rc.senseEnemyTowerLocations();
				MapLocation closest = rc.senseEnemyHQLocation();
				for(MapLocation loc: towers) {
					if(myLocation.distanceSquaredTo(loc)<myLocation.distanceSquaredTo(closest))
						closest = loc;
				myTarget = closest;
				}
			}
			attackSomething();
			movePotential();
			supplyTransferFraction = 0.5;

		} catch (Exception e) {
			System.out.println("Basher Exception");
			e.printStackTrace();
		}
	}

	/*static void doSoldier()
	{
		try {
			attackSomething();
			if (rc.isCoreReady()) {
				MapValue dest = gridGradient(myGridInd);
				MapLocation m = new MapLocation(dest.x+myGridCenter.x,dest.y+myGridCenter.y);
		        facing = rc.getLocation().directionTo(m);
		        
		        if (facing == Direction.OMNI)
		        	facing = Direction.values()[rand.nextInt(8)];
		        
		        while (!rc.canMove(facing))
		        {
		        	if(rand.nextDouble()<0.5)
			        	facing = facing.rotateLeft();
		        	else
		        		facing = facing.rotateRight();
		        }
		        rc.move(facing);
			}
		} catch (Exception e) {
			System.out.println("Soldier Exception");
			e.printStackTrace();
		}
	}*/
	
	static void doTankFactory()
	{
		try {
			RobotInfo[] bots = rc.senseNearbyRobots(99999, myTeam);
			int tanks = 0;
			for (RobotInfo b: bots)
			{
				if (b.type == RobotType.TANK)
				{
					tanks++;
				}
			}
			if (tanks < numTanks)
			{
				trySpawn(facing,RobotType.TANK);
			}
		} catch (Exception e) {
			System.out.println("Tank Factory Exception");
			e.printStackTrace();
		}
	}
	
	static void doTank()
	{
		try {
			attackSomething();
			rageUpdate();
			rageMove();
		} catch (Exception e) {
			System.out.println("Tank Exception");
			e.printStackTrace();
		}
	}
	
	static void doSoldier()
	{
		try {
			//updateSquadInfo();
			/*if (Clock.getRoundNum()<1800) {
				myTarget = rc.senseEnemyHQLocation();
			}

			else{
				aggCoef = 1000;
				MapLocation towers[] = rc.senseEnemyTowerLocations();
				MapLocation closest = rc.senseEnemyHQLocation();
				for(MapLocation loc: towers){
					if(myLocation.distanceSquaredTo(loc)<myLocation.distanceSquaredTo(closest))
						closest = loc;
				myTarget = closest;
				}
			}*/
			attackSomething();
			//movePotential();
			//int bc = Clock.getBytecodeNum();
			//int bitdir = gridPathfind(myLocation,gridRageBase,true);
			//tryMove(bitdir);
			rageUpdate();
			rageMove();
			//System.out.println("rage time: " + (Clock.getBytecodeNum()-bc));
			//if (d != Direction.NONE && d != Direction.OMNI)
			//	tryMove(d);
			//else if (d != Direction.OMNI)
			//	rc.breakpoint();
			
			//rc.setIndicatorLine(myLocation, myLocation.add(d,3), 0, 255, 255);
			myGrid.firstComponent();
			rc.setIndicatorString(0, "Rage: " + (myGrid.readValue(gridRageBase)&65535) + " | Target: " + (myGrid.readValue(gridRageBase)>>16));
			//debug_drawGridMask(myGridInd,myGridID);
			//debug_drawGridMask(myGridCenter,rc.readBroadcast(gridEdgesEWBase+myGridID));
			//supplyTransferFraction = 0.5;

		} catch (Exception e) {
			System.out.println("Soldier Exception");
			e.printStackTrace();
		}
	}

	static void doBeaver()
	{
		try {
			facing = myLocation.directionTo(rc.senseEnemyHQLocation());
			RobotInfo[] ourTeam = rc.senseNearbyRobots(100000, rc.getTeam());
			int n = 0; // current number of miner factories
			int m = 0; // current number of barracks
			int o = 0; // current number of helipads
			int s = 0; // current number of supply depots
			int mi = 0; // current number of miners
			int tf = 0; // tank factories
			for(RobotInfo ri: ourTeam)
			{ // count up stuff
				if(ri.type==RobotType.MINERFACTORY){
					n++;
				}else if(ri.type==RobotType.BARRACKS){
					m++;
				}else if(ri.type==RobotType.TANKFACTORY){
					tf++;
				}else if (ri.type==RobotType.HELIPAD){
					o++;
				}else if (ri.type==RobotType.SUPPLYDEPOT){
					s++;
				}else if(ri.type==RobotType.MINER){
					mi++;
				}
			}
			// sit still and mine until we have 
			if(n<1)
			{
				buildUnit(RobotType.MINERFACTORY);
				if(rc.isCoreReady()&&rc.canMine()){
					rc.mine();
				}
			}
			/*
			else if (o<1)
			{
				tryBuild(facing.opposite(),RobotType.HELIPAD);
				if(rc.isCoreReady()&&rc.canMine()){
					rc.mine();
				}
			}
			
			else if (m<1)
			{
				tryBuild(facing.opposite(),RobotType.BARRACKS);
				if(rc.isCoreReady()&&rc.canMine()){
					rc.mine();
				}
			}
			*/
			else
			{
				if(n < numMinerFactories && (n == o)) 
				{
					buildUnit(RobotType.MINERFACTORY);
				} 
				else if(s<numSupplyDepots && mi>1)// && m>1)
				{
					buildUnit(RobotType.SUPPLYDEPOT);
				}
				else if(o<numHelipads)
				{
					buildUnit(RobotType.HELIPAD);
				}
				else if(m<numBarracks)
				{
					buildUnit(RobotType.BARRACKS);
				}
				else if(tf<numTankFactories  && m>0)
				{
					buildUnit(RobotType.TANKFACTORY);
				}
				attackSomething();
				minerOperation(true, false); // (searching?, supplying?)
			}
			
			supplyTransferFraction = 0.5;
			
		} catch (Exception e) {
			System.out.println("Beaver Exception");
			e.printStackTrace();
		}
	}
	
//===========================================================================================
	
	static void doMiner()
	{
		try {
			rc.setIndicatorString(2, toString(minerState));
			attackSomething();
			// defensiveManeuvers();  ???
			//if(rc.isCoreReady()) // otherwise don't waste the bytecode
			//	miningDuties();
			if (rc.isCoreReady())
			{
				if (rc.senseOre(myLocation) > 1)
				{
					rc.mine();
				}
				else
				{
					int bitdir = gridPathfind(myLocation,gridMiningBase,true);
					tryMove(bitdir);
				}
			}
			
			int message = rc.readBroadcast(myMinerID);
			rc.setIndicatorString(0, "Miner ID = " + myMinerID);
		} catch (Exception e) {
			System.out.println("Miner Exception");
			e.printStackTrace();
		}
	}

	static void miningDuties() throws GameActionException
	{
		oreHere = rc.senseOre(myLocation);
		double mineScore;
		updateMinerInfo(); // tells HQ numbers of each type of miner and gets instructions about promoting suppliers to searchers
		// depending on what state the miner is in, execute functionality
		switch (minerState)
		{
		case LEADING:
			mineScore = mineScore();
			if( (oreHere<lastLocationStartingOre && mineScore < 60) || oreHere < 10) // less ore and can't max out
				minerState = MinerState.SEARCHING; // switch to searching, this will be reported to HQ next round
			else // we've been sitting still and mining
				mineAndMoveStraight();
			break;
		case SEARCHING:
			minerOperation(true, false); // (searching?, supplying?)
			mineScore = mineScore();
			if(mineScore > 60)
				minerState = MinerState.LEADING; // switch to a leader, this will be reported to HQ next round
			supplyTransferFraction = 0.5;
			break;
		case SUPPLYING:
			rc.setIndicatorDot(myLocation.add(0,-1), 255,0,0); // white dots on supply line
			if(myMinerID!=rc.readBroadcast(minerContiguousID)-1) // if we're the most recent miner, stay put
				minerOperation(false, true); // (searching?, supplying?)
			supplyTransferFraction = 0.9;
			break;
		}
	}

	static double mineScore() throws GameActionException {
		int[] x = {0,-1, 0, 0, 1,-1,-1, 1, 1,-2, 0, 0, 2,-2,-2, 2, 2};
		int[] y = {0, 0,-1, 1, 0,-1, 1,-1, 1, 0,-2, 2, 0,-2, 2,-2, 2};
		double[] thisMine = vectorSumOre(x,y);
		double thisMineScore = thisMine[2];
		return thisMineScore;
	}
	
	static void mineAndMoveStraight() throws GameActionException {
		if(rc.senseOre(myLocation)>=10){ //there is plenty of ore, so try to mine
			if(rand.nextDouble()<1){ // mine 100% of time we can collect max
				if(rc.isCoreReady()&&rc.canMine()){
					rc.mine();
				}
			}else{
				tryMove(facing);
			}
		}else{
			tryMove(facing);
		}
		if(justMoved) // update the starting amount of ore on each space we move to
		{
			lastLocationStartingOre = currentLocationStartingOre; 
			currentLocationStartingOre = rc.senseOre(rc.getLocation());
		}
	}
	
	static void minerOperation(boolean searching, boolean supplying) throws GameActionException {
		
		if(rc.senseOre(myLocation)>=10){ //there is plenty of ore, so try to mine
			if(rand.nextDouble()<0.98){ // mine 98% of time we can collect max
				if(rc.isCoreReady()&&rc.canMine()){
					rc.mine();
				}
			}else{
				facing = minerPotential(searching,supplying);
				tryMove(facing);
			}
			
		}else if( rc.senseOre(myLocation)>0.8){ //there is a bit of ore, so maybe try to mine, maybe move on (suppliers don't mine)
			if(rand.nextDouble()<0.2){ // mine
				if(rc.isCoreReady()&&rc.canMine()){
					rc.mine();
				}
			}else{ // look for more ore
				facing = minerPotential(searching,supplying);
				tryMove(facing);
			}
		}else{ //no ore, so look for more
			facing = minerPotential(searching,supplying);
			tryMove(facing);
		}
	}
	
	
	// Miner's potential field calculation.  Yields an integer representing the movement direction 0-7.  A null value means not to move.
	static Direction minerPotential(boolean searching, boolean supplying) throws GameActionException {
		
		double mineScore = 0;
		int dx = 0;
		int dy = 0;
		int totalPotential[] = {0,0};
		
		if(searching)
		{
			// nearest-neighbor ore sensing
			int x1[] = {0,1,1,1,0,-1,-1,-1};
			int y1[] = {1,1,0,-1,-1,-1,0,1};
			double innerPotential[] = vectorSumOre(x1,y1);
			mineScore += innerPotential[2];
			totalPotential[0] += (int) (2*innerPotential[0]);
			totalPotential[1] += (int) (2*innerPotential[1]);
			
			// next-nearest-neighbor ore sensing
			int x2[] = {0,2,2,2,0,-2,-2,-2};
			int y2[] = {2,2,0,-2,-2,-2,0,2};
			double outerPotential[] = vectorSumOre(x2,y2);
			mineScore += outerPotential[2];
			totalPotential[0] += (int) (outerPotential[0]/10);
			totalPotential[1] += (int) (outerPotential[1]/10);
			
			// global target direction: WORK ON THIS!!!!
			// IDEA: save nearby locations within this robot's internal variables
			int globalBestMineScore = rc.readBroadcast(bestMineScoreChan);
			if(mineScore > globalBestMineScore){
				rc.broadcast(bestMineScoreChan, (int)mineScore);
				rc.broadcast(bestMineXChan, myLocation.x);
				rc.broadcast(bestMineYChan, myLocation.y);
				//if(rc.getType()==RobotType.BEAVER && mineScore > 140){ // have a beaver build a miner factory on the spot
				//	tryBuild(facing,RobotType.MINERFACTORY);
				//}
			}
			else{
				int targetX = rc.readBroadcast(bestMineXChan);
				int targetY = rc.readBroadcast(bestMineYChan);
				double globalPullFactor = Math.max(0,globalBestMineScore)/20; // pull towards a good mine is proportional to the value at that mine
				dx = (targetX - myLocation.x);
				dy = (targetY - myLocation.y);
				double dist = dx*dx + dy*dy;
				double px = 0;
				double py = 0;
				if(mineScore<40) // terrible spot, make the pull great, and distance-independent!
				{
					px = dx*globalPullFactor/20;
					py = dy*globalPullFactor/20;
				}
				else
				{
					px = dx*globalPullFactor/dist;
					py = dy*globalPullFactor/dist;
				}
				totalPotential[0] += (int) px;
				totalPotential[1] += (int) py;
				//System.out.println(myMinerID + " global pull = (" + (int)px + "," + (int)py + "), inner = (" + (int)(2*innerPotential[0]) + "," + (int)(2*innerPotential[1]) + "), outer = (" + (int)(outerPotential[0]/10) + "," + (int)(outerPotential[1]/10) + ")");
			}
		}
		if(supplying)
		{
			// attraction to chain of suppliers: attract twice as much to one in front (to within supply transfer radius) as to one behind
			// miners have "contiguous ID" starting at message board channel 500, each new miner assigned the next ID.  contiguous ID = channel.
			int message = rc.readBroadcast(myMinerID);
			
			// find guy in front
			if(myMinerID!=500) // unless i'm first
			{
				boolean found = false;
				boolean endOfLine = false;
				int nextMinerID = myMinerID-1;
				RobotInfo robo = null;
				while( !found && nextMinerID >= 500 && !endOfLine )
				{
					int nextRobotID = rc.readBroadcast(nextMinerID);
					if(nextRobotID==-1) // we've reached ones that have already been recruited to be searchers
					{
						endOfLine = true;
						continue;
					}
					try	{
						robo = rc.senseRobot(nextRobotID);
					}
					catch (Exception e)	{
						nextMinerID--;
					}
					if(robo!=null)
						found = true;
				}
				if(found) // here's the robot to follow
				{
					dx = (robo.location.x - myLocation.x);
					dy = (robo.location.y - myLocation.y);
				}
				else // no one ahead of me to follow
				{
					// go toward the best mine!
					int targetX = rc.readBroadcast(bestMineXChan);
					int targetY = rc.readBroadcast(bestMineYChan);
					int globalBestMineScore = rc.readBroadcast(bestMineScoreChan);
					double globalPullFactor = Math.max(0,globalBestMineScore)*10; // pull towards a good mine is proportional to the value at that mine
					dx = (targetX - myLocation.x);
					dy = (targetY - myLocation.y);
					double dist = dx*dx + dy*dy;
					double px = dx*globalPullFactor/dist;
					double py = dy*globalPullFactor/dist;
					totalPotential[0] += (int) px;
					totalPotential[1] += (int) py;
					//if(!endOfLine) // end of line should have no one to follow, but if i'm not the end of the line, then i have lost my way
					//	minerState = MinerState.SEARCHING; // by default i start searching
				}
			}
			int forwardPullFactor = 2*Math.max(dx*dx + dy*dy - 15, 0); // zero if we're in supply transfer radius, increasing as we're farther away
			totalPotential[0] += dx*forwardPullFactor;
			totalPotential[1] += dy*forwardPullFactor;
			
			// find guy behind
			int unbornMinerID = rc.readBroadcast(minerContiguousID);
			if(myMinerID != unbornMinerID-1) // as long as i am not the last miner created
			{
				boolean found = false;
				int prevMinerID = myMinerID+1;
				RobotInfo robo = null;
				while( !found && prevMinerID < unbornMinerID )
				{
					int prevRobotID = rc.readBroadcast(prevMinerID);
					try	{
						robo = rc.senseRobot(prevRobotID);
					}
					catch (Exception e)	{
						prevMinerID++;
					}
					if(robo!=null)
						found = true;
				}
				if(found) // here's the robot to follow
				{
					dx = (robo.location.x - myLocation.x);
					dy = (robo.location.y - myLocation.y);
				}
			}
			int backwardPullFactor = 10*Math.max(dx*dx + dy*dy - 15, 0); // zero if we're in supply transfer radius, increasing as we're farther away
			totalPotential[0] += dx*backwardPullFactor;
			totalPotential[1] += dy*backwardPullFactor;
			
		}
		
		// total direction
		Direction bestDirection = myLocation.directionTo( myLocation.add((int)totalPotential[0],(int)totalPotential[1]) ); // direction to move
		rc.setIndicatorString(1, "mining value =  " + (int) mineScore + "best direction =  " + bestDirection.toString());
		// don't go back to your last spot ever
		if(bestDirection==facing.opposite())
			bestDirection = getRandomDirection();
		return bestDirection;
	}

	static double[] vectorSumOre(int[] x, int[] y) throws GameActionException {
		MapLocation sensingRegion[] = new MapLocation[x.length];
		for(int a=0; a<x.length; a++){
			sensingRegion[a] = myLocation.add(x[a],y[a]);
		}
		double ore = 0;
		int i=0;
		double potentialX = 0;
		double potentialY = 0;
		double mineScore = -1*x.length*5; // makes it so that a flat region of 5 ore will have a score of 0
		for(MapLocation m: sensingRegion){
			ore = rc.senseOre(m);
			ore = (double)ore;
			RobotInfo robo = rc.senseRobotAtLocation(m);
			TerrainTile tile = rc.senseTerrainTile(m);
			mineScore += ore;
			//if(robo.type!=RobotType.MINER || robo.team!=rc.getTeam()){ // if there's a miner there, don't go toward it
			if( robo==null && tile.isTraversable() && isSafeDirection(myLocation.directionTo(m))){
				potentialX += ore*x[i];
				potentialY += ore*y[i];
			}else if(robo!=null){ // repulsion from other robots
				potentialX -= 10*x[i];
				potentialY -= 10*y[i];
			}else{ // try to make it so we won't try to go where we can't
				potentialX -= 1*x[i];
				potentialY -= 1*y[i];
			}
			i++;
		}
		double potential[] = {potentialX, potentialY, mineScore};
		return potential;
	}
	
	// This method updates miner-specific info on the message board
	static void updateMinerInfo() throws GameActionException
	{
		int roundNow = Clock.getRoundNum();
		
		// first check to see if HQ says a supplier should be promoted to a searcher
		if(minerState == MinerState.SUPPLYING){
			int orders = rc.readBroadcast(minerShuffle);
			boolean firstInLine = false;
			int nextRobotID = rc.readBroadcast(myMinerID-1);
			if(nextRobotID==-1 || myMinerID==500) // is the guy before me a searcher, or am i the very first?
				firstInLine = true;
			if(orders > 0 && firstInLine) // only promote the ones at the end of the supply line
			{
				minerState = MinerState.SEARCHING; // promotion
				rc.broadcast(minerShuffle, orders-1); // make it known
				rc.broadcast(myMinerID, -1); // ruin my robot ID
			}
		}
		
		// now update message board and signify my presence
		switch(minerState){
		
		case SUPPLYING:
			int numSupplying = rc.readBroadcast(minersSupplying);
			//System.out.println("read " + numSupplying + " supplying");
			if (roundNow != (numSupplying >> 16)) // if this value has not been updated this round already by another miner
			{
				numSupplying = roundNow << 16; // make it known that i updated it this round
				numSupplying++; // and add one for me to the accumulated number
			}
			else
			{
				numSupplying++; // and add one for me to the accumulated number
			}
			rc.broadcast(minersSupplying, numSupplying); // broadcast
			//System.out.println("wrote " + numSupplying + " supplying, and that is really " + (numSupplying & 255) );
			break;
			
		case SEARCHING:
			int numSearching = rc.readBroadcast(minersSearching);
			//System.out.println("read " + numSearching + " supplying");
			if (roundNow != (numSearching >> 16)) // if this value has not been updated this round already by another miner
			{
				numSearching = roundNow << 16; // make it known that i updated it this round
				numSearching++; // and add one for me to the accumulated number
			}
			else
			{
				numSearching++; // and add one for me to the accumulated number
			}
			rc.broadcast(minersSearching, numSearching); // broadcast
			//System.out.println("wrote " + numSearching + " supplying");
			break;
			
		case LEADING:
			int numLeading = rc.readBroadcast(minersLeading);
			//System.out.println("read " + numLeading + " supplying");
			if (roundNow != (numLeading >> 16)) // if this value has not been updated this round already by another miner
			{
				numLeading = roundNow << 16; // make it known that i updated it this round
				numLeading++; // and add one for me to the accumulated number
			}
			else
			{
				numLeading++; // and add one for me to the accumulated number
			}
			rc.broadcast(minersLeading, numLeading); // broadcast
			//System.out.println("wrote " + numLeading + " supplying");
			break;
		}
	}
	
//=============================================================================================
	
	static void rageSetTarget(RobotInfo target, int ind) throws GameActionException
	{
		// we can add it
		rc.broadcast(rageIDBase+ind, target.ID);
		// and its grid square, for later convenience			
		rc.broadcast(rageGridBase+ind, GridComponent.indexFromLocation(target.location));
	}
	
	static int rageOpenSlots() throws GameActionException
	{
		int rageslots = 0;
		
		for (int i=1; i<TARGET_COUNT; i++)
		{
			int targetID = rc.readBroadcast(rageIDBase+i);
			// is this rage target taken/valid?
			if (!rc.canSenseRobot(targetID))
			{
				rageslots |= (1<<i);
				rc.broadcast(rageIDBase+i, 0);
			}
		}
		
		return rageslots;
	}

	static void rageUpdate() throws GameActionException
	{
		// get full list of robots
		RobotInfo[] bots = rc.senseNearbyRobots(myType.sensorRadiusSquared,myTeam.opponent());
		
		// and open rage target slots
		int rageslots = rageOpenSlots();
		
		for (RobotInfo ri : bots)
		{
			// can we add any?
			if (rageslots == 0)
				break;
			// then add it
			int rageind = Integer.numberOfTrailingZeros(rageslots);
			rageSetTarget(ri,rageind);
			// and nuke rageslots
			rageslots &= rageslots-1;
		}
	}
	
	static void rageMove() throws GameActionException
	{
		// if we can't move, meh
		if (!rc.isCoreReady())
			return;
		
		int rageVal = myGrid.readValue(gridRageBase);
		// this rage target is the index, not the ID
		int rageID = (rageVal>>>16);
		
		// first, check if this is still a live rage target...
		if (rc.canSenseRobot(rageID))
		{
			RobotInfo ri = rc.senseRobot(rageID);
			// update its grid location
//			rc.broadcast(rageGridBase+rageTarget, rageGrid);
			// check if we're within attack range or so
			if (myLocation.distanceSquaredTo(ri.location) < 35)
			{
				// then just move directly towards the target
				tryMove(myLocation.directionTo(ri.location));
				return;
			}
		}
		
		// otherwise, do long-range move towards target
		int bitdir = gridPathfind(myLocation,gridRageBase,true);
		tryMove(bitdir);
	}
	
//=============================================================================================
	
	static MapLocation gridCenter(int gridX, int gridY)
	{
		return new MapLocation(gridX*GRID_SPC+center.x-GRID_OFFSET,gridY*GRID_SPC+center.y-GRID_OFFSET);
	}
	
	static MapLocation gridCenter(int gridInd)
	{
		return new MapLocation((gridInd%GRID_DIM)*GRID_SPC+center.x-GRID_OFFSET,(gridInd/GRID_DIM)*GRID_SPC+center.y-GRID_OFFSET);
	}
	
	static int gridIndex(MapLocation loc)
	{
		return GRID_DIM*((GRID_OFFSET+loc.y-center.y+GRID_SPC/2)/GRID_SPC)
				+ ((GRID_OFFSET+loc.x-center.x+GRID_SPC/2)/GRID_SPC);
	}
	
	// this is for local units to do
	static void gridVisited(GridComponent grid) throws GameActionException
	{
		// if we have been here before, we have called this function
		if (grid.getFlag(STATUS_VISITED))
			return;
		
		grid.setFlag(STATUS_VISITED|STATUS_SEEN);
			
		int gridX = grid.gridX();
		int gridY = grid.gridY();

		// check if the grid needs to be enlarged
		getExtents();
		gridMinX = Math.min(gridMinX, gridX-1);
		gridMaxX = Math.max(gridMaxX, gridX+1);
		gridMinY = Math.min(gridMinY, gridY-1);
		gridMaxY = Math.max(gridMaxY, gridY+1);				
		setExtents();
		
		// queue this grid cell for pathing
		//rc.broadcast(gridPtrChan, grid.gridIndex);
		
		// mark adjacent 4 squares as "SEEN", meaning do connectivity processing
		// (if it's inside gridMinX, etc), but not yet "VISITED"
		for (int dir=0;dir<4;dir++)
		{
			GridComponent adjgrid = grid.offsetTo(dir);
			if (!adjgrid.getFlag(STATUS_SEEN))
				adjgrid.setFlag(STATUS_SEEN);
		}
	}
	
	static int gridPathfind(MapLocation loc, int gridChannel, boolean ascend) throws GameActionException
	{
		GridComponent grid = new GridComponent(loc);
		// and figure out which connected component we're on
		int pathind = grid.getPathInd(loc);
		int pathindex = Integer.numberOfTrailingZeros(pathind);
		int pathable = grid.findComponent(pathind);
		
		// if for some reason this didn't work, abandon ship
		if ((pathind&pathable) == 0)
		{
			System.out.println("Pathable is 0");
			return 0;
		}
		
		// ok, so now we're on the right subgrid/connected component
		// find the min-value neighbor and corresponding connected edge
		// now, let's loop through each adjacent direction & their gridids
		
		// and stored values of best
		int bestedge = 0;
		int bestval = ascend?(grid.readValue(gridChannel)&65535):-(grid.readValue(gridChannel)&65535);
		int bestptr = 0;
		int bestdir = -1;
		
		// check each adjacent direction
		for (int dir=0; dir<4; dir++)
		{
			GridComponent adjgrid = grid.offsetTo(dir);
			
			while (adjgrid.isValid())
			{
				int edges = adjgrid.readValue(edgeChans[dir]);
				edges &= (pathable&bitEdge[dir]);
				// possibly connected?
				if (edges > 0) // add adjacent value
				{
					int adjval = ascend?(adjgrid.readValue(gridChannel)&65535):-(adjgrid.readValue(gridChannel)&65535);
					if (adjval > bestval)
					{
						bestval = adjval;
						bestedge = edges;
						bestptr = adjgrid.getPointer();
						bestdir = dir;
					}
				}
				// and cycle on to the next connected component
				adjgrid.nextComponent();
			}
		}

		// and diagonals
		for (int dir=4; dir<8; dir++)
		{
			if ((pathable&bitEdge[dir]) == 0)
				continue;
			
			GridComponent adjgrid = grid.offsetTo(dir);
			while (adjgrid.isValid())
			{
				int adjpath = adjgrid.getMaybes();
				if ((adjpath&bitEdge[dir+4]) > 0)
				{
					int adjval = ascend?(adjgrid.readValue(gridChannel)&65535):-(adjgrid.readValue(gridChannel)&65535);
					if (adjval > bestval)
					{
						bestval = adjval;
						bestedge = bitEdge[dir];
						bestptr = adjgrid.getPointer();
						bestdir = dir;
					}
				}
				adjgrid.nextComponent();
			}
		}
		
		// nothing better found
		if (bestedge == 0)
		{
			System.out.println("On best square");
			return 0xFF;
		}
		
		// ok, now we have the best edge that we want to go to
		// we flood-fill out until we reach pathind
		pathable = bestedge;
		
		// if we're already on the edge, we really just want to step over to the target square
		if ((pathind&bestedge) > 0)
		{
			// again, ignore unknowns, we're assuming connectivity calculation is running smoothly
			// offset our grid center to the adjacent square, including connectivity info
			GridComponent adjgrid = new GridComponent(bestptr);
			// where do we go on the adjacent guy
			int adjpathable = adjgrid.readValue(gridPathableBase);
			// mask it first, then shift
			if (bestdir < STATUS_NE)
				adjpathable &= bitEdge[3-bestdir];

			// now transform it to our grid
			switch (bestdir)
			{
			case STATUS_NORTH:
				// bottom-to-top
				pathindex += 5;
				adjpathable >>= 20;
				break;
			case STATUS_SOUTH:
				// top-to-bottom
				pathindex -= 5;
				adjpathable <<= 20;
				break;
			case STATUS_EAST:
				// left-right
				pathindex--;
				adjpathable <<= 4;
				break;
			case STATUS_WEST:
				// right-left
				pathindex++;
				adjpathable >>= 4;
				break;
			// for the diagonals, only have one possible step 
			case STATUS_NE:
				return (1<<1);
			case STATUS_SE:
				return (1<<3);
			case STATUS_SW:
				return (1<<5);
			case STATUS_NW:
				return (1<<7);
			}
			
			// and -and- it with our possible steps
			adjpathable &= bitAdjacency[pathindex];
			// and mask it to the steps
			adjpathable &= bitEdge[bestdir];
			
			// now adjpathable is 1 in places on our edge where we can step to on adjacent edge
			// and pathind is our relative location on the same grid
			// so I want to shift it over to be @ 12
			if (pathindex>12) adjpathable >>= (pathindex-12);
			else if (pathindex<12) adjpathable <<= (12-pathindex);
			
			//debug_drawGridMask(loc,adjpathable,255,255,255);

			// now create the direction flags
			int bitdirs = 0;
			while (adjpathable > 0)
			{
				int setbit = Integer.numberOfTrailingZeros(adjpathable);
				bitdirs |= Consts.gridToDir[setbit]; 
				// and drop off last bit
				adjpathable &= adjpathable-1;
			}

			return bitdirs;
		}
		
		int norms = grid.readProperty(gridNormalBase);

		for (int i=0; i<GRID_N; i++)
		{
			int path = relaxGrid(pathable,norms);
			// now set newpath to difference between old and new
			if ((path&pathind)>0)
				break;
			// or we didn't change anything, but still didn't reach us, somehow?
			// return error
			if (pathable==path)
			{
				System.out.println("Path unreachable");
				return 0;
			}
			
			pathable = path;
		}
		
		// now, if we got to here, pathable is one step before we hit pathind, so we find the possible directions by masking it
		pathable &= bitAdjacency[pathindex];

		// now let's shift it to the center
		if (pathindex>12) pathable >>= (pathindex-12);
		else if (pathindex<12) pathable <<= (12-pathindex);

		int bitdirs = 0;
		while (pathable > 0)
		{
			int setbit = Integer.numberOfTrailingZeros(pathable);
			bitdirs |= Consts.gridToDir[setbit]; 
			// and drop off last bit
			pathable &= pathable-1;
		}
		
		return bitdirs;
	}
	
/*	static MapValue gridGradient(GridComponent grid) throws GameActionException
	{
		int bc = Clock.getBytecodeNum();
		
		MapValue gradient = new MapValue(0,0,0);
		
		// get our square's pathable U maybe
		int pathable = grid.getMaybes();
		
		// now, let's loop through each adjacent direction & their gridids
		
		int gridval = grid.readValue(gridPotentialBase);
		
		for (int dir=0; dir<4; dir++)
		{
			GridComponent adjgrid = grid.offsetTo(dir);

			int dirval = 0;
			int dircount = 0;
			
			while (adjgrid.isValid())
			{
				int edges = adjgrid.readValue(edgeChans[dir]);
				
				// if they're not unconnected, add the value difference
				if ((edges&pathable&bitEdge[dir]) > 0)
				{
					dirval += adjgrid.readValue(gridPotentialBase);
					dircount++;
				}

				// and cycle on to the next connected component
				adjgrid.nextComponent();
			}
			
			if (dircount > 0)
			{
				gradient.x -= (dirval/dircount - gridval)*dirOffX[dir];
				gradient.y -= (dirval/dircount - gridval)*dirOffY[dir];
			}
		}
		
		gradient.value = Math.sqrt(gradient.x*gradient.x+gradient.y*gradient.y);
		
		//System.out.println("Gradient time: " + (Clock.getBytecodeNum()-bc));
		return gradient;
	}
	*/
	
	static boolean gridOre() throws GameActionException
	{
		int bc = Clock.getBytecodeNum();

		GridComponent grid = new GridComponent(rc.readBroadcast(gridOrePtrChan));
		if (!grid.isValid()) grid.initialize();
		
		// check if we made it all the way around
		if (grid.isFirst())
		{
			int curround = Clock.getRoundNum();
			int lastup = rc.readBroadcast(gridLastOreChan);
			// recently updated, don't run again
			if (curround - lastup < GRID_ORE_FREQ)
				return false;

			// broadcast the start of the last update
			rc.broadcast(gridLastOreChan,curround);
			System.out.println("Ore update @ " + curround);
		}
		
		
		// advance the pointer
		rc.broadcast(gridOrePtrChan,grid.nextCCPointer());
		
		// if we haven't seen it, we clearly can't sense the ore
		if (!grid.getFlag(STATUS_SEEN))
			return true;
		
		
		MapLocation loc = grid.getCenter();

		// gridid now contains id of relevant connected component, is what we want to read/write
		// so figure out which regions belong to it
		int pathable = grid.readValue(gridPathableBase);
		// ignore unknowns, because we literally can't sense anything there

		double gridOre = 0;
		
		// now loop through all the grid's ore
		for (int ind=0; ind<GRID_N; ind++)
		{
			// first, check if this is pathable
			if (((1<<ind)&pathable)==0)
				continue;
			
			MapLocation testloc = loc.add(gridOffX[ind],gridOffY[ind]);
			double newore = rc.senseOre(testloc); 
			if (newore > 0)
				gridOre += newore; 
		}
		
		// and sense miners as well
		RobotInfo[] bots = rc.senseNearbyRobots(loc,GRID_SENSE,myTeam);
		
		for (RobotInfo ri : bots)
		{
			switch (ri.type)
			{
			case BEAVER:
				gridOre += Consts.WEIGHT_ORE_BEAVER; 
				break;
			case MINER:
				gridOre += Consts.WEIGHT_ORE_MINER;
				break;
			default:
				continue;
			}
		}
		
		grid.writeValue(gridOreBase, (int)gridOre);
		
		//System.out.println("Ore time: " + (Clock.getBytecodeNum()-bc));
		
		return true;
	}

	
	static boolean gridUpdate() throws GameActionException
	{
		// 100 bytecodes for preamble
		// 100 bytecodes for senseRobots
		// ~100 bytecodes/robot
		// 100 bytecodes for broadcasts
		
		int bc = Clock.getBytecodeNum();

		GridComponent grid = new GridComponent(rc.readBroadcast(gridUpdatePtrChan));
		if (!grid.isValid()) grid.initialize();
		
		// check if we made it all the way around
		if (grid.isFirst())
		{
			int curround = Clock.getRoundNum();
			int lastup = rc.readBroadcast(gridLastUpdateChan);
			// recently updated, don't run again
			if (curround - lastup < GRID_UPDATE_FREQ)
				return false;

			// broadcast the start of the last update
			rc.broadcast(gridLastUpdateChan,curround);
			System.out.println("Potentials update @ " + curround);
		}
		
		
		// advance the pointer
		rc.broadcast(gridUpdatePtrChan,grid.nextCCPointer());
		
		MapLocation loc = grid.getCenter();

		MapLocation ul = loc.add(-GRID_SPC/2,-GRID_SPC/2);
				
		// gridid now contains id of relevant connected component, is what we want to read/write
		// so figure out which regions belong to it
		int pathable = grid.readValue(gridPathableBase);
		// ignore unknowns, because we literally can't sense anything there
		
		// now sense frienemies in all grid cells
		RobotInfo[] bots = new RobotInfo[0];
		if (grid.getFlag(STATUS_SEEN))
			bots = rc.senseNearbyRobots(loc,GRID_SENSE,null);
		
		int prevFriend = grid.readValue(gridFriendBase);
		int prevEnemy = grid.readValue(gridFriendBase);
		int prevPotential = grid.readValue(gridPotentialBase);
		
		int gridFriend = 0;
		int gridEnemy = 0;
		
		for (RobotInfo ri : bots)
		{
			// get pathable index
			int pathind = (1<<((ri.location.x-ul.x)+GRID_SPC*(ri.location.y-ul.y)));
			
			// if it doesn't belong to this connected component, continue
			if ((pathind&pathable)==0 && ri.type != RobotType.DRONE)
				continue;
			
			//int strength = getRobotStrength(ri.health,ri.supplyLevel,ri.type);
			if (ri.team == myTeam)
				gridFriend += Consts.friendWeights[ri.type.ordinal()];
			else
				gridEnemy += Consts.enemyWeights[ri.type.ordinal()];
		}
		
		// relax enemy value to 0
//		if (Math.abs(gridEnemy) < Math.abs(prevEnemy))
//			gridEnemy = prevEnemy+Consts.ENEMY_DECAY_RATE*(prevEnemy>0?-1:1);
		
		// quick check for myself. if my gridID equals this one, I'm here
		if (myGrid.gridID == grid.gridID && myGrid.isValid())
			gridFriend += Consts.friendWeights[myType.ordinal()];
		
		//Clock.getRoundNum();
		//System.out.print("");
		//if (gridind == 1328+52*1)
		//	System.out.println("#######1328 etc, " + Clock.getRoundNum() + " called senseNearbyRobots at " + loc + " in radius " + GRID_SENSE + " and found " + nunits + " units" + 
		//" my location: " + myLocation + "{}{}{}{}{}" + gridFriend);

		
		// and rebroadcast the values
		if (gridFriend != prevFriend)
			grid.writeValue(gridFriendBase, gridFriend);

		if (gridEnemy != prevEnemy)
			grid.writeValue(gridEnemyBase, gridEnemy);
		
		//System.out.println("Update time (" + bots.length + "): " + (Clock.getBytecodeNum()-bc));
		
		return true;
	}
	
	static boolean gridDiffuse() throws GameActionException
	{
		int bc = Clock.getBytecodeNum();

		GridComponent grid = new GridComponent(rc.readBroadcast(gridPotentialPtrChan));
		if (!grid.isValid()) grid.initialize();
		
		// check if we made it all the way around
		if (grid.isFirst())
		{
			int curround = Clock.getRoundNum();
			int lastup = rc.readBroadcast(gridLastPotentialChan);
			// recently updated, don't run again
			if (curround - lastup < GRID_DIFFUSE_FREQ)
				return false;

			// broadcast the start of the last update
			rc.broadcast(gridLastPotentialChan,curround);
			System.out.println("Diffusion update @ " + curround);

			System.out.println("Total: " + rc.readBroadcast(gridPotentialTotalChan) + ", offset:" + rc.readBroadcast(gridPotentialOffsetChan) );

			
			// and, let's go for another round
			int offset = rc.readBroadcast(gridPotentialTotalChan);
			// divide total by # of live grid cells
			offset /= rc.readBroadcast(gridNextIDChan);
			// and save it as the offset (negating as we go)
			rc.broadcast(gridPotentialOffsetChan,-offset);
			rc.broadcast(gridPotentialTotalChan, 0);
		}
		
		
		// advance the pointer
		rc.broadcast(gridPotentialPtrChan,grid.nextCCPointer());		
		// # of grid squares to closest friendly unit
				
		// source term for potential is difference between friend and enemy squares
		int gridval = grid.readValue(gridPotentialBase);
		
		int source = grid.readValue(gridFriendBase) + grid.readValue(gridEnemyBase);		
		
		if (grid.getFlag(STATUS_HQ))
			source += Consts.WEIGHT_ENEMY_HQ;
		//else if ((gridinfo&STATUS_TOWER)>0)
		//	source += Consts.WEIGHT_ENEMY_TOWER;

		
		// get our square's pathable U unknown, and see what it connects to...
		int pathable = grid.getMaybes();
		
		// now, let's loop through each adjacent direction & their gridids

		int newval = 0;
		
		for (int dir=0; dir<4; dir++)
		{
			GridComponent adjgrid = grid.offsetTo(dir);

			int dirval = 0;
			int diradd = 0;
			while (adjgrid.isValid())
			{
				int edges = adjgrid.readValue(edgeChans[dir]);
				
				// possibly connected?
				if ((edges&pathable&bitEdge[dir]) > 0) // add adjacent value
					dirval += adjgrid.readValue(gridPotentialBase);
				else // insulating boundary, add my value
					dirval += gridval;
				diradd++;
				// and cycle on to the next connected component
				adjgrid.nextComponent();
			}
			
			if (adjgrid.gridNCC > 0)
				dirval /= diradd;//adjgrid.gridNCC;
			else
				dirval = gridval;
			
			newval += dirval;
		}
		
		// and take the average
		newval = newval / 4;
		
		newval += source;
		
		// shift the potential by the offset
		newval += rc.readBroadcast(gridPotentialOffsetChan);
		
		//if (grid.getFlag(STATUS_HQ))
		//	newval = Consts.WEIGHT_ENEMY_HQ;
		
		// and add the value we're writing to the accumulating total
		rc.broadcast(gridPotentialTotalChan, newval+rc.readBroadcast(gridPotentialTotalChan));
		
		grid.writeValue(gridPotentialBase, newval);
		//rc.broadcast(gridPotentialPtrChan,(ptr+1)%gridn);
		//System.out.println("Diffuse time: " + (Clock.getBytecodeNum()-bc));
		
		return true;
	}
	
	
	static boolean gridMining() throws GameActionException
	{
		int bc = Clock.getBytecodeNum();

		GridComponent grid = new GridComponent(rc.readBroadcast(gridMiningPtrChan));
		if (!grid.isValid()) grid.initialize();
		
		// check if we made it all the way around
		if (grid.isFirst())
		{
			int curround = Clock.getRoundNum();
			int lastup = rc.readBroadcast(gridLastMiningChan);
			// recently updated, don't run again
			if (curround - lastup < GRID_MINING_FREQ)
				return false;

			// broadcast the start of the last update
			rc.broadcast(gridLastMiningChan,curround);
			System.out.println("Mining update @ " + curround);
		}
		
		
		// advance the pointer
		rc.broadcast(gridMiningPtrChan,grid.nextCCPointer());		
				
		// ok, get the base value for this grid square
		int gridval = grid.readValue(gridOreBase) + Consts.MINING_DECAY;
		
		// get our square's pathable U unknown, and see what it connects to...
		int pathable = grid.getMaybes();
		
		// now, let's loop through each adjacent direction & their gridids
		for (int dir=0; dir<4; dir++)
		{
			GridComponent adjgrid = grid.offsetTo(dir);

			while (adjgrid.isValid())
			{
				int edges = adjgrid.readValue(edgeChans[dir]);
				
				// possibly connected?
				if ((edges&pathable&bitEdge[dir]) > 0) // keep largest value
					gridval = Math.max(gridval,adjgrid.readValue(gridMiningBase)+Consts.MINING_DECAY);

				// and cycle on to the next connected component
				adjgrid.nextComponent();
			}			
		}
		
		grid.writeValue(gridMiningBase, gridval);
		//System.out.println("Mining time: " + (Clock.getBytecodeNum()-bc));
		
		return true;
	}
	
	
	static boolean gridRage() throws GameActionException
	{
		int bc = Clock.getBytecodeNum();

		GridComponent grid = new GridComponent(rc.readBroadcast(gridRagePtrChan));
		if (!grid.isValid()) grid.initialize();
		
		// check if we made it all the way around
		if (grid.isFirst())
		{
			int curround = Clock.getRoundNum();
			int lastup = rc.readBroadcast(gridLastRageChan);
			// recently updated, don't run again
			if (curround - lastup < GRID_RAGE_FREQ)
				return false;

			// broadcast the start of the last update
			rc.broadcast(gridLastRageChan,curround);
			System.out.println("Rage update @ " + curround);
		}
		
		// advance the pointer
		rc.broadcast(gridRagePtrChan,grid.nextCCPointer());
				
		// ok, now calculate the rage value for this grid square
		int gridval = 0;
		// and the ID of the highest-priority target here
		int maxid = 0;
		int maxval = 0;
		
		// start at 1, 0 is always HQ
		for (int i=1; i<TARGET_COUNT; i++)
		{
			// check if it's in this grid cell
			//int ragegrid = rc.readBroadcast(rageGridBase+i);

			// and that it's set
			int rageid = rc.readBroadcast(rageIDBase+i);
			if (rageid == 0)
				continue;
			
			// so it's in this grid square, try to sense it
			if (rc.canSenseRobot(rageid))
			{
				RobotInfo ri = rc.senseRobot(rageid);
				
				int ragegrid = GridComponent.indexFromLocation(ri.location);
				if (ragegrid != grid.gridIndex)
					continue;

				// check if it's in this grid component
				if (!grid.isInMaybe(ri.location))
					continue;
				
				// ok, now let's add to the gridval
				int weight = Consts.enemyWeights[ri.type.ordinal()]; 
				gridval += weight;
				if (weight > maxval)
				{
					maxid = rageid;
					maxval = weight;
				}
			}
		}
		
		// check for HQ and towers
		if (grid.getFlag(STATUS_HQ))
		{
			if (grid.isInMaybe(enemyHQ))
			{
				gridval += Consts.WEIGHT_ENEMY_HQ;
				// hq has rage-id of 0
				maxid = 0;
			}
		}

		// now pack the (max) rage id into gridval
		gridval |= (maxid<<16);
		
		// if there are no valid targets around me, i'll just write the highest invalid one
		// so it keeps pointing in the right general direction
		int invalidval = 0;
		
		// get our square's pathable U unknown, and see what it connects to...
		int pathable = grid.getMaybes();
		
		// now, let's loop through each adjacent direction & their gridids
		for (int dir=0; dir<4; dir++)
		{
			GridComponent adjgrid = grid.offsetTo(dir);

			while (adjgrid.isValid())
			{
				int edges = adjgrid.readValue(edgeChans[dir]);
				
				// possibly connected?
				if ((edges&pathable&bitEdge[dir]) > 0)
				{
					// keep largest value, with id
					int adjval = adjgrid.readValue(gridRageBase);
					// initialized values...
					if (adjval != 0)
					{
						int rageid = (adjval >>> 16);
						adjval += Consts.RAGE_DECAY;
						// need to either be heading towards HQ or a valid target
						if (rageid == 0 || rc.canSenseRobot(rageid))
						{
							if((adjval&65535) > (gridval&65535))
								gridval = adjval;
						}
						else
						{
							if((adjval&65535) > (invalidval&65535))
								invalidval = adjval;
						}
					}
				}

				// and cycle on to the next connected component
				adjgrid.nextComponent();
			}			
		}
		
		// if all targets are un-findable, and we don't have any on our square, we don't want to be set to zero
		if (gridval != 0)		
			grid.writeValue(gridRageBase, gridval);
//		else
//			grid.writeValue(gridRageBase, invalidval);
		//System.out.println("Rage time: " + (Clock.getBytecodeNum()-bc));
		
		return true;
	}
	
	
	static boolean gridConnectivity() throws GameActionException
	{
		int bc = Clock.getBytecodeNum();
		
		int ptr = rc.readBroadcast(gridPtrChan);
		// start off the grid at the first connected component for this function
		GridComponent grid = new GridComponent(ptr&65535);
		if (!grid.isValid()) grid.initialize();
		
		int atomicStartRound = Clock.getRoundNum();
		
		// go on to the next connected component, so we don't double-count things
		rc.broadcast(gridPtrChan,grid.nextIndexPointer());
		
		// check if we made it all the way around
		if (grid.isFirst())
		{
			int curround = Clock.getRoundNum();
			int lastup = rc.readBroadcast(gridLastConnectivityChan);
			// recently updated, don't run again
			if (curround - lastup < GRID_CONNECTIVITY_FREQ)
				return false;

			rc.broadcast(gridLastConnectivityChan,curround);
			System.out.println("Connectivity update @ " + curround);
		}
		
		// now, if we haven't seen it at all, do nothing
		if (!grid.getFlag(STATUS_SEEN))
			return true;
		
		// CHECKPOINT #1: CHECK IF WE ARE ADDING ANY NEW TERRAIN TILES
		MapLocation loc = grid.getCenter();
		
		// get previously loaded values for this grid cell
		int norms = grid.readProperty(gridNormalBase);
		int voids = grid.readProperty(gridVoidBase);
		int known = grid.readProperty(gridKnownBase);
		
		int oldknown = known;
		
		// do we know all the tiles?
		// if we don't, try finding some new ones
		
		int nadd=0;
		
		// only add terrain tiles right before connected-component 0
		// to make sure next steps are consistent
		if (known != GRID_MASK && grid.gridCC == 0)
		{
			// first, get the terrain tiles
			int unknown = GRID_MASK&(~known);
			
			// set unknown to known+1, if we have too many unknowns
			if (Integer.bitCount(unknown) > 15)
			{
				if (known>0)
				{
					unknown = relaxGrid(known,GRID_MASK);
					// and mask it off
					unknown = unknown & (~known);
					// and include middle edges
					unknown |= GRID_EDGE_CENTERS;
				}
				else
				{
					unknown = GRID_EDGE_CENTERS;
				}
			}
			
			// drop down the unknown bits one by one
			while (unknown>0)
			{
				int i = Integer.numberOfTrailingZeros(unknown);
				unknown &= (unknown-1);
				int z = (1<<i);
				switch (rc.senseTerrainTile(loc.add(gridOffX[i],gridOffY[i])))
				{
				case NORMAL:
					norms |= z;
					known |= z;
					nadd++;
					break;
				case UNKNOWN:
					break;
				case VOID:
				case OFF_MAP:
					voids |= z;
					known |= z;
					nadd++;
					break;
				}
				// limit how many times we add things just to slow it down a bit to 1000-bc-size chunks
				if (nadd>10) break;
			}
			
			// then write them back if it changed
			if (known != oldknown)
			{
				grid.writeProperty(gridNormalBase, norms);
				grid.writeProperty(gridVoidBase, voids);
				grid.writeProperty(gridKnownBase, known);
				// and flag it as needing a path update
				grid.unsetFlag(STATUS_PATHED);
			}
			
			// and if we added a lot, return without doing stuff, but stay on the same square
			if (nadd > 10)
			{
				System.out.println("Connectivity add time for " + grid.gridIndex + ": " + (Clock.getBytecodeNum()-bc));
				// stay on same round, til we're done adding
				if (Clock.getRoundNum() == atomicStartRound)
					rc.broadcast(gridPtrChan,grid.gridIndex);
				return true;
			}
		}
		
		// do we know anything at all, or do we need to re-path?
		if (known == 0 || grid.getFlag(STATUS_PATHED))
		{
			//System.out.println("Blank grid time (" + nadd + "): " + (Clock.getBytecodeNum()-bc));
			// already went on to next round
			return true;
		}
		
		// CHECKPOINT #3: ADD UP PATHABLES UP TO THIS CONNECTED COMPONENT INDEX
		// note that this is where gridid gets advanced to current grid cell
		int prevpathable = 0;
		int gridcc = (ptr>>>16);
		for (int i=0; i<gridcc; i++)
		{
			prevpathable |= grid.readValue(gridPathableBase);
			grid.nextComponent();
			debug_assert(grid.gridID != 0, "AWWWWWWWWWWW" + grid);
		}

		// now flood-fill the current connected component as far as we can
		int pathable = grid.readValue(gridPathableBase);
		
		// if pathable is 0, it's a fresh connected component (or first connected component)
		// which means we *know* it's not connected to prevpathable, if we did our math right
		// so we can just set it to any single norm that isn't part of prevpathable, if there are any
		if (pathable == 0)
		{
			pathable = Integer.lowestOneBit(norms & ~(prevpathable));
//			System.out.println("pathable set to " + Integer.toBinaryString(GRID_MASK&(norms&~(prevpathable))));
		}
		
		// now flood-fill this connected component for all we're worth
		for (int i=0; i<GRID_N; i++)
		{
			int path = relaxGrid(pathable,norms);
			// now set newpath to difference between old and new
			int newpath = path & (~pathable);
			pathable = path;
			// if nothing new, stop looping
			if (newpath == 0)
				break;
		}
		
		// save the path we found
		grid.writeValue(gridPathableBase,pathable);

		// this is the region that is pathed so far
		prevpathable |= pathable;
		// if it's connected to unknown
		int maybe = relaxGrid((~known)&GRID_MASK,(~voids)&GRID_MASK);
		
		// norms that we are considering adding
		int newnorms = norms & (~prevpathable);
		
		// CHECKPOINT #4: ANY NORMS NOT IN PREVIOUS PATHABLES THAT ALSO AREN'T CONNECTED VIA MAYBE
		if (grid.gridCC+1 == grid.gridNCC)
		{
			if ((newnorms>0) && ((newnorms&maybe)==0||(prevpathable&maybe)==0))
			{
				// add a new connected component
				// first, add a new grid cell at the particular index
				grid.addComponent();
				// now, we shouldn't need to do anything
				// the norms should be filled in automatically next update
				System.out.println("newnorms = " + Integer.toBinaryString(newnorms) + " | " + Integer.toBinaryString(pathable) + 
						" | " + Integer.toBinaryString(prevpathable));
			}
			else
			{
				// no new components on the last update, so set pathable
				grid.setFlag(STATUS_PATHED);
			}
		}
		
		// CHECKPOINT #5: NOW CALCULATE OUR MAYBE EDGES
		// for this pathable, it's defined as edges on adjacent square that can get to pathable
		// or to pathable|unknown, if they connect
		if ((pathable&maybe)>0 || pathable == 0)
			pathable |= GRID_MASK&(~known);
		
		pathable = grid.getMaybes();
		
		// do UD-DU flip
		int nsEdge = (pathable >>> 20)&bitEdge[0]; // south edge -> north
		nsEdge |= (pathable << 20)&bitEdge[3]; // north edge -> south
		// and LR/RL
		int ewEdge = (pathable << 4)&bitEdge[1]; // west -> east
		ewEdge |= (pathable >>> 4)&bitEdge[2]; // east -> west
		// now expand them  by one and re-mask
		nsEdge = relaxGrid(nsEdge,GRID_MASK&~voids)&(bitEdge[0]|bitEdge[3]);
		ewEdge = relaxGrid(ewEdge,GRID_MASK&~voids)&(bitEdge[1]|bitEdge[2]);
		// and save them
		grid.writeValue(gridEdgesNSBase,nsEdge);
		grid.writeValue(gridEdgesEWBase,ewEdge);
		
		//System.out.println("Connectivity path time: " + (Clock.getBytecodeNum()-bc));
		
		// go on to next CC, if we made it in a single round
		if (Clock.getRoundNum() == atomicStartRound)
			rc.broadcast(gridPtrChan,grid.nextCCPointer());

		return true;
	}	
	
	
	static int relaxGrid(int fill, int mask)
	{
		int path = fill;
		while (fill > 0)
		{
			int lbit = Integer.numberOfTrailingZeros(fill);
			// add the lowest bit to the growing path
			path |= (bitAdjacency[lbit] & mask);
			// and unset lowest bit
			fill &= (fill-1);
		}
		return path;
	}
	
	// Supply Transfer Protocol
	static void transferSupplies() throws GameActionException {
		
		if (myType == RobotType.MINERFACTORY || Clock.getBytecodesLeft() < 600)
			return;
		
		RobotInfo[] nearbyAllies = rc.senseNearbyRobots(rc.getLocation(),GameConstants.SUPPLY_TRANSFER_RADIUS_SQUARED,myTeam);
		double mySupply = rc.getSupplyLevel();
		double lowestSupply = mySupply;
		double transferAmount = 0;
		MapLocation suppliesToThisLocation = null;
		for(RobotInfo ri:nearbyAllies){
			if(ri.supplyLevel<lowestSupply){
				lowestSupply = ri.supplyLevel;
				transferAmount = (mySupply-ri.supplyLevel)/2;
				suppliesToThisLocation = ri.location;
			}
		}
		if(suppliesToThisLocation!=null){
			rc.transferSupplies((int)transferAmount, suppliesToThisLocation);
		}
	}
	
	
	// Supply Transfer Protocol
	static void transferSupplies(double fraction) throws GameActionException {
		RobotInfo[] nearbyAllies = rc.senseNearbyRobots(rc.getLocation(),GameConstants.SUPPLY_TRANSFER_RADIUS_SQUARED,myTeam);
		double mySupply = rc.getSupplyLevel();
		double lowestSupply = mySupply;
		double transferAmount = 0;
		MapLocation suppliesToThisLocation = null;
		for(RobotInfo ri:nearbyAllies){ // only transfer to bots with less supply
			if(ri.supplyLevel<lowestSupply){
				lowestSupply = ri.supplyLevel;
				transferAmount = mySupply*fraction;
				suppliesToThisLocation = ri.location;
			}
		}
		if(suppliesToThisLocation!=null && transferAmount>100){ // if we're doing a transfer
			MapLocation myHQ = rc.senseHQLocation();
			int dxOther = (suppliesToThisLocation.x - myHQ.x);
			int dyOther = (suppliesToThisLocation.y - myHQ.y);
			int dxMe = (myLocation.x - myHQ.x);
			int dyMe = (myLocation.y - myHQ.y);
			int distance = dxOther*dxOther + dyOther*dyOther - dxMe*dxMe - dyMe*dyMe;
			if(distance>0){ // make sure the other guy is farther from HQ
				rc.transferSupplies((int)transferAmount, suppliesToThisLocation);
			}
		}
	}

	
	static boolean isSafeDirection(Direction dir) throws GameActionException { //checks if the facing direction is safe from towers
		myLocation = rc.getLocation();
		MapLocation tileInFront = myLocation.add(dir);
		boolean goodSpace = true;
		//check that the direction in front is not a tile that can be attacked by the enemy towers
		MapLocation[] enemyTowers = rc.senseEnemyTowerLocations();
		for(MapLocation m: enemyTowers){
			if(m.distanceSquaredTo(tileInFront)<=RobotType.TOWER.attackRadiusSquared){
				goodSpace = false; //space in range of enemy towers
				break;
			}
		}
		if(tileInFront.distanceSquaredTo(rc.senseEnemyHQLocation())<=RobotType.HQ.attackRadiusSquared){
			goodSpace = false; //space in range of enemy towers
		}
		return goodSpace;
	}

	
	static void moveStraight() throws GameActionException {
		if(isGoodMovementDirection()){
			//try to move in the facing direction
			if(rc.isCoreReady()&&rc.canMove(facing)){
				rc.move(facing);
			}
		}else{
			facing = facing.rotateLeft();
		}
	}
	
	static boolean isGoodMovementDirection() throws GameActionException { //checks if the facing direction is "good", meaning safe from towers and not a blockage or off-map or occupied
		MapLocation tileInFront = rc.getLocation().add(facing);
		boolean goodSpace = true;
		//check that we are not facing off the edge of the map or are blocked
		if(rc.senseTerrainTile(tileInFront)!=TerrainTile.NORMAL){
			goodSpace = false;
		}else{
			//check that the space is not occupied by a robot
			if(rc.isLocationOccupied(tileInFront)){
				goodSpace = false; //space occupied
			}else{
				//check that the direction in front is not a tile that can be attacked by the enemy towers
				MapLocation[] enemyTowers = rc.senseEnemyTowerLocations();
				for(MapLocation m: enemyTowers){
					if(m.distanceSquaredTo(tileInFront)<=RobotType.TOWER.attackRadiusSquared){
						goodSpace = false; //space in range of enemy towers
						break;
					}
				}
			}
		}
		return goodSpace;
	}

	// This method will attack an enemy in sight, if there is one
	static void attackSomething() throws GameActionException
	{
		// if we can attack squad target (eg. tower), so do
		if (!rc.isWeaponReady())
			return;
		if (myTarget != null && rc.canAttackLocation(myTarget))
		{
			RobotInfo ri = rc.senseRobotAtLocation(myTarget);
			if (ri != null && ri.team == myTeam.opponent())
			{
				rc.attackLocation(myTarget);
				return;
			}
		}
		
		RobotInfo[] enemies = rc.senseNearbyRobots(myType.attackRadiusSquared, enemyTeam);
		double minhealth = 1000;
		
		if (enemies.length == 0)
			return;

		MapLocation minloc = enemies[0].location;
		for (RobotInfo en: enemies)
		{
			if (en.health < minhealth)
			{
				minhealth = en.health;
				minloc = en.location;
			}
		}
		if (rc.canAttackLocation(minloc))
			rc.attackLocation(minloc);
	}
	
	
	static Direction getRandomDirection() {
		return Direction.values()[(int)(rand.nextDouble()*8)];
	}

	static void mineAndMove() throws GameActionException {
		if(rc.senseOre(rc.getLocation())>12){ //there is plenty of ore, so try to mine
			if(rand.nextDouble()<0.9){ // mine 90%
				if(rc.isCoreReady()&&rc.canMine()){
					rc.mine();
				}
			}else{
				facing = minerPotential();
				moveStraight();
			}
			
		}else if(rc.senseOre(rc.getLocation())>0.8){ //there is a bit of ore, so maybe try to mine, maybe move on
			if(rand.nextDouble()<0.2){ // mine
				if(rc.isCoreReady()&&rc.canMine()){
					rc.mine();
				}
			}else{ // look for more ore
				facing = minerPotential();
				moveStraight();
			}
		}else{ //no ore, so look for more
			facing = minerPotential();
			moveStraight();
		}
	}
	
	
	// Miner's potential field calculation.  Yields an integer representing the movement direction 0-7.  A null value means not to move.
	static Direction minerPotential() throws GameActionException {
		// just random
		//return (int)(rand.nextDouble()*8);
		
		float mineScore = 0;
		MapLocation here = rc.getLocation();
		
		// nearest-neighbor ore sensing
		int x1[] = {0,1,1,1,0,-1,-1,-1};
		int y1[] = {1,1,0,-1,-1,-1,0,1};
		//int x1[] = {0,1,0,-1,};
		//int y1[] = {1,0,-1,-0};
		float innerPotential[] = vectorSumOreAndMiners(x1,y1);
		mineScore += innerPotential[2];
		
		// next-nearest-neighbor ore sensing
		int x2[] = {0,2,2,2,0,-2,-2,-2};
		int y2[] = {2,2,0,-2,-2,-2,0,2};
		//int x2[] = {2,2,-2,-2};
		//int y2[] = {2,-2,-2,2};
		float outerPotential[] = vectorSumOreAndMiners(x2,y2);
		mineScore += outerPotential[2];

		// global target direction: WORK ON THIS!!!!
		rc.setIndicatorString(1, "mining value =  " + mineScore);
		if(mineScore > (float)rc.readBroadcast(bestMineScoreChan)){
			rc.broadcast(bestMineScoreChan, (int)mineScore);
			rc.broadcast(bestMineXChan, here.x);
			rc.broadcast(bestMineYChan, here.y);
		}
		int targetX = rc.readBroadcast(bestMineXChan);
		int targetY = rc.readBroadcast(bestMineYChan);
		float globalPullFactor = Math.max(0,mineScore)/20; // pull towards a good mine is proportional to the value at that mine
		float dx = (targetX - here.x);
		float dy = (targetY - here.y);
		float dist = dx*dx + dy*dy;
		float px = dx*globalPullFactor/dist;
		float py = dy*globalPullFactor/dist;
		float globalPotential[] = {px,py};

		// total direction
		float totalPotentialX = innerPotential[0]*10 + outerPotential[0] + globalPotential[0];
		float totalPotentialY = innerPotential[1]*10 + outerPotential[1] + globalPotential[1];
		Direction bestDirection = here.directionTo(here.add((int)totalPotentialX,(int)totalPotentialY)); // direction to move
		if(bestDirection==Direction.OMNI){ // can't decide where to go, don't let it get stuck
			bestDirection = getRandomDirection();
		}
		rc.setIndicatorString(0, "best direction =  " + bestDirection.toString());
		return bestDirection;
	}

	static float[] vectorSumOreAndMiners(int[] x, int[] y) throws GameActionException {
		MapLocation here = rc.getLocation();
		MapLocation sensingRegion[] = new MapLocation[x.length];
		for(int a=0; a<x.length; a++){
			sensingRegion[a] = here.add(x[a],y[a]);
		}
		double ore = 0;
		int i=0;
		float potentialX = 0;
		float potentialY = 0;
		float mineScore = -1*x.length*10; // makes it so that a flat region of 10 ore will have a score of 0
		for(MapLocation m: sensingRegion){
			ore = rc.senseOre(m);
			ore = (float)ore;
			RobotInfo robo = rc.senseRobotAtLocation(m);
			//TerrainTile tile = rc.senseTerrainTile(m);
			mineScore += ore;
			//if(robo.type!=RobotType.MINER || robo.team!=rc.getTeam()){ // if there's a miner there, don't go toward it
			if(robo==null){
				potentialX += ore*x[i];
				potentialY += ore*y[i];
			}else{ // move away from others
				potentialX -= 5*x[i];
				potentialY -= 5*y[i];
			}
			i++;
		}
		float potential[] = {potentialX, potentialY, mineScore};
		return potential;
	}
		
	
	// Move Around: random moves; go left if hitting barrier; avoid towers
	static void moveAround() throws GameActionException 
	{
		if(rand.nextDouble()<0.05){
			if(rand.nextDouble()<0.5){
				facing = facing.rotateLeft();
			}else{
				facing = facing.rotateRight();
			}
		}
		MapLocation tileInFront = rc.getLocation().add(facing);
		
		//check that the direction in front is not a tile that can be attacked by the enemy towers
		MapLocation[] enemyTowers = rc.senseEnemyTowerLocations();
		boolean tileInFrontSafe = true;
		for(MapLocation m: enemyTowers){
			if(m.distanceSquaredTo(tileInFront)<=RobotType.TOWER.attackRadiusSquared){
				tileInFrontSafe = false;
				break;
			}
		}

		//check that we are not facing off the edge of the map
		if(rc.senseTerrainTile(tileInFront)!=TerrainTile.NORMAL||!tileInFrontSafe){
			facing = facing.rotateLeft();
		}else{
			//try to move in the facing direction
			if(rc.isCoreReady()&&rc.canMove(facing)){
				rc.move(facing);
			}
		}
	}
	
	// Aggressive Move (does not avoid towers)
	static void aggMove() throws GameActionException {

        facing = rc.getLocation().directionTo(myTarget);
        
		if(rand.nextDouble()<0.15){
			if(rand.nextDouble()<0.5){
				facing = facing.rotateLeft();
			}else{
				facing = facing.rotateRight();
			}
		}
		
		MapLocation tileInFront = rc.getLocation().add(facing);

		//check that we are not facing off the edge of the map
		if(rc.senseTerrainTile(tileInFront)!=TerrainTile.NORMAL){
			facing = facing.rotateLeft();
		}else{
			//try to move in the facing direction
			if(rc.isCoreReady()&&rc.canMove(facing)){
				rc.move(facing);
			}
		}
    }
	
	
	// Potential field move
	static void movePotential() throws GameActionException {

		float forceX = 0.0f;
		float forceY = 0.0f;

		float fDest = 3.0f;
		float fBored = 1.5f;

		float qFriendly = -2.0f; // repel, goes as 1/r

		float fSticky = 0.0f; // goes as 1/r
		float fBinding = 1.0f;
		float fNoiseMax = 1.0f;
		float fEnemySighted = 1.5f;
		float qEnemyInRange = -2.0f; //is multiplied by attack strength
		//float aggCoef = 0.8f;
		float fNoise = 0.0f;
		
		float strengthBal = unitVal[rc.getType().ordinal()];
		
		// get dem robots
		RobotInfo[] friendlyRobots = rc.senseNearbyRobots(
				myType.sensorRadiusSquared, myTeam);
		RobotInfo[] enemyRobots = rc.senseNearbyRobots(
				myType.sensorRadiusSquared, myTeam.opponent());
		for (RobotInfo bot : friendlyRobots) strengthBal += unitVal[bot.type.ordinal()];
		for (RobotInfo bot : enemyRobots) strengthBal -= unitVal[bot.type.ordinal()];

		// attracted to squad target, far away
		int destX = (myTarget.x - myLocation.x);
		int destY = (myTarget.y - myLocation.y);
		float d2dest = (float) Math.sqrt(destX * destX + destY * destY);
		//MapValue dest = enemyHQ;//gridGradient(myGridInd);
		//int destX = dest.x;
		//int destY = dest.y;
		//float d2dest = (float)dest.value;


		forceX = (fDest + aggCoef)* destX / d2dest;
		forceY = (fDest + aggCoef) * destY / d2dest;
		// attraction to target goes as const qDest
		
		
		// ********** BOREDOM ENGINE  **************
		// Move impatience: if not moving or rocking, stop it and get attracted randomly for a while.
		//rc.setIndicatorString(0, "moves: " + myLocation + " " + moveRec[moveIdx] + " " + moveRec[moveIdx-1]+" " + moveRec[moveIdx-2]);
		lastMoveCounter += 1;
		lastShotCounter +=1;

		if (bored == 1){

			// if bored add a force in direction boredDir for boredLength ticks;
			forceX += fBored * Consts.senseLocsX[boredDirIdx];
			forceY += fBored * Consts.senseLocsY[boredDirIdx];		
			boredCounter += 1; 
			rc.setIndicatorString(1, "Boredom Counter = " + boredCounter);
			if (boredCounter > boredomLength){
				bored = 0;
				boredCounter = 0;
				lastMoveCounter = 0;
				lastShotCounter = 0;
				//rc.setIndicatorString(0, "not bored");
			}

		}else if (lastMoveCounter > moveImpatience || myLocation.equals(moveRec[moveIdx - 2])){
			//rc.setIndicatorString(0, "BORED!");
			bored = 1;
			boredomLength = moveBoredomLength;
			boredDirIdx = rand.nextInt(8);
			
		}else if (lastShotCounter > shootImpatience){
			//rc.setIndicatorString(0, "BORED! Sweeping...");
			bored = 1;
			boredomLength = sweepBoredomLength;
			boredDirIdx = rand.nextInt(8);
			
		}else rc.setIndicatorString(0, "not bored");

		

		rc.setIndicatorString(2, "lastShot Counter = " + lastShotCounter);

	
		for (RobotInfo bot : friendlyRobots) {


			// doesn't apply to towers and HQ
			if (bot.type == RobotType.TOWER || bot.type == RobotType.HQ)
				continue;

			int vecx = bot.location.x - myLocation.x;
			int vecy = bot.location.y - myLocation.y;
			int d2 = bot.location.distanceSquaredTo(myLocation);
			float id = Consts.invSqrt[d2];

			// weakly attract, constant normalized to unitVal 
			forceX += unitVal[bot.type.ordinal()] * fSticky *id * vecx; // constant
			forceY += unitVal[bot.type.ordinal()] * fSticky *id* vecy;	
	
			// don't get too close, at close range, as -1/r
			forceX += qFriendly * id * id*vecx;
			forceY += qFriendly * id * id*vecy;
			
		}

		for (RobotInfo bot : enemyRobots) {
			// dangerous ones, dealt with below
			if (bot.type == RobotType.TOWER || bot.type == RobotType.HQ)
				continue;

			int vecx = bot.location.x - myLocation.x;
			int vecy = bot.location.y - myLocation.y;
			int d2 = bot.location.distanceSquaredTo(myLocation);


			float id = Consts.invSqrt[d2];

			// attract to enemy units, adjusted for strength balance, constant
			forceX += strengthBal * fEnemySighted * unitAtt[bot.type.ordinal()] * id  * vecx ; // constant 
			forceY += strengthBal * fEnemySighted * unitAtt[bot.type.ordinal()] * id  * vecy ;


			// enemies in range, 1/r
			float dattack = Consts.sqrt[bot.type.attackRadiusSquared] - Consts.sqrt[d2]
					+ 2.0f;
			if (dattack > 0) {
				forceX += unitVal[bot.type.ordinal()] * qEnemyInRange * id*id * vecx; // constant
				forceY += unitVal[bot.type.ordinal()] * qEnemyInRange * id *id * vecy;	
			}
		}

//		boolean hasSquad = friendlyRobots.length > 8
//				|| ((rc.readBroadcast(squadTaskBase + mySquad) >> 8) & 255) > 8;

			MapLocation[] towers = getBuildings(myTeam.opponent());
			
			MapLocation enHQ = towers[0];
			int d2 = enHQ.distanceSquaredTo(myLocation);
			// check if it's in range
			if (d2 <= RobotType.HQ.attackRadiusSquared + 20){

				float id = Consts.invSqrt[d2];

				int vecx = enHQ.x - myLocation.x;
				int vecy = enHQ.y - myLocation.y;


				float dattack = Consts.sqrt[RobotType.HQ.attackRadiusSquared] - Consts.sqrt[d2]
						+ 2.0f;		

				if (dattack > 0) {
					// repel with fEnemyInRange 
					forceX += unitVal[0] * qEnemyInRange * id * vecx ; // hard shell
					forceY += unitVal[0] * qEnemyInRange * id * vecy ;	
				}

			}
			
			for (MapLocation tower : towers) {
				d2 = tower.distanceSquaredTo(myLocation);
				// check if it's in range
				if (d2 > RobotType.TOWER.attackRadiusSquared + 20)
					continue;

				float id = Consts.invSqrt[d2];

				int vecx = tower.x - myLocation.x;
				int vecy = tower.y - myLocation.y;


				float dattack = Consts.sqrt[RobotType.TOWER.attackRadiusSquared] - Consts.sqrt[d2]
						+ 2.0f;		
				
				if (dattack > 0) {
					// repel with fEnemyInRange 
					forceX += unitVal[1] * qEnemyInRange * id * vecx ; // hard shell
					forceY += unitVal[1] * qEnemyInRange * id * vecy ;	
				}
			}
			
				
				// get direction of force
			MapValue[] mvs = new MapValue[9];

			for (int i = 0; i < 9; i++)
				mvs[i] = new MapValue(Consts.senseLocsX[i], Consts.senseLocsY[i], forceX
						* Consts.senseLocsX[i] + forceY * Consts.senseLocsY[i]);

/*			rc.setIndicatorLine(myLocation, new MapLocation(myLocation.x
					+ (int) (forceX * 10), myLocation.y + (int) (forceY * 10)), 0,
					255, 255);
*/
			Arrays.sort(mvs);

			for (int i = 8; i > 0; i--) {
				// don't move unless you exceed "binding force"
				fNoise *= fNoiseMax * 2 * (rand.nextDouble() - 0.5);
				if (mvs[i].value < fBinding + fNoise)
					break;
				Direction newdir = myLocation.directionTo(mvs[i]
						.offsetFrom(myLocation));
				if (rc.isCoreReady() && rc.canMove(newdir)) {
					
					rc.move(newdir);					
					moveIdx += 1;
					moveRec[moveIdx] = myLocation.add(newdir);
					lastMoveCounter = 0;
					//rc.setIndicatorString(0, "moveIdx = " + moveIdx);

				}
			}
	}
	
	static int getDangerMoves()
	{
		MapLocation[] towers = rc.senseEnemyTowerLocations();
		MapLocation hq = rc.senseEnemyHQLocation();
		MapLocation myloc = rc.getLocation();
		
		int bitmoves = 0;
		
		if (Math.abs(myloc.x-hq.x)<7 && Math.abs(myloc.y-hq.y)<7)
		{
			if (towers.length>1) // use range 35
			{
				int ind = (hq.x-myloc.x+6) + 13*(hq.y-myloc.y+6);
				bitmoves |= Consts.hqMask[ind];				
			}
			else // use 24
			{
				int ind = (hq.x-myloc.x+5) + 11*(hq.y-myloc.y+5);
				bitmoves |= Consts.towerMask[ind];
			}
		}
		
		for (MapLocation t : towers)
		{
			if (Math.abs(myloc.x-t.x)>=6 || Math.abs(myloc.y-t.y)>=6)
				continue;
				
			int ind = (t.x-myloc.x+5) + 11*(t.y-myloc.y+5);
			bitmoves |= Consts.towerMask[ind];
		}
		
		return bitmoves;
	}
	
	// this method will attempt to move in one of the given directions at random
	static boolean tryMove(int bitmoves) throws GameActionException
	{
		if (!rc.isCoreReady())
			return false;

		if (bitmoves == 0)
			return false;
		
		// take into account enemies
		int safemoves = ~getDangerMoves();
		bitmoves &= safemoves;
		
		// check which directions we can move in
		int canmoves = 0;
		for (int i=0; i<8; i++)
		{
			if (rc.canMove(Direction.values()[i]))
				canmoves |= (1<<i);
		}
		
		// now only move in directions we can move
		bitmoves &= canmoves;
		
		if (bitmoves == 0)
		{
			// if we're here, it means we said to move, but we can't move anywhere
			// so we just pick a random safe direction instead, if possible
			bitmoves = (canmoves&safemoves);
			if (bitmoves == 0)
				return false;
		}

		// now pick randomly from canmove&bitmoves
		
		// and extract a random bit of remaining ones
		int nrem = Integer.bitCount(bitmoves);
		int randval = rand.nextInt(nrem);
		
		// shave off randval bits
		for (int i=0; i<randval; i++)
			bitmoves &= bitmoves-1;
		
		rc.move(Direction.values()[Integer.numberOfTrailingZeros(bitmoves)]);
		return true;
	}

	
	// This method will attempt to move in Direction d (or as close to it as possible)
	static boolean tryMove(Direction d) throws GameActionException
	{
		if (!rc.isCoreReady())
			return false;
		
		int isDanger = getDangerMoves();
		int dirint = d.ordinal();
		
		for (int i=0; i<8; i++)
		{
			int dir = (dirint+Consts.dirOffsets[i])&7;
			if ((isDanger&(1<<dir))>0)
				continue;
			
			Direction dd = Direction.values()[dir];

			if (!rc.canMove(dd))
				continue;
			
			rc.move(dd);
			return true;
		}
		
		return false;
	}
	
	// This method will attempt to move in Direction d (or as close to it as possible)
	static boolean tryAggressiveMove(Direction d) throws GameActionException
	{
		if (!rc.isCoreReady())
			return false;
		
		int dirint = d.ordinal();
		
		for (int i=0; i<8; i++)
		{
			int dir = (dirint+Consts.dirOffsets[i])&7;
			
			Direction dd = Direction.values()[dir];

			if (!rc.canMove(dd))
				continue;
			
			rc.move(dd);
			return true;
		}
		
		return false;
	}
	

	// This method will attempt to spawn in the given direction (or as close to it as possible)
	static boolean trySpawn(Direction d, RobotType type) throws GameActionException
	{
		if (!rc.isCoreReady())
			return false;
		
		int dirint = d.ordinal();
		
		for (int i=0; i<8; i++)
		{
			int dir = (dirint+Consts.dirOffsets[i])&7;
			
			Direction dd = Direction.values()[dir];
			if (!rc.canSpawn(dd,type))
				continue;
			
			rc.spawn(dd,type);
			return true;
		}
		
		return false;
	}
	
	static void buildUnit(RobotType type) throws GameActionException
	{
		if(rc.getTeamOre()>type.oreCost && rc.isCoreReady())
		{			
			Direction buildDir = getRandomDirection();
			for (int i=0; i<8; i++)
			{
				if(rc.canBuild(buildDir, type))
				{
					rc.build(buildDir, type);
					return;
				}
				buildDir = buildDir.rotateLeft();
			}
		}
	}

	static void spawnUnit(RobotType type) throws GameActionException
	{
		trySpawn(getRandomDirection(),type);
	}
	
	static int[] unitCounts()
	{
		int[] units = new int[RobotType.values().length];
		
		RobotInfo[] bots = rc.senseNearbyRobots(999999, myTeam);
		
		for (RobotInfo ri : bots)
			units[ri.type.ordinal()]++;
		
		return units;
	}
	
	static void debug_drawGridMask(MapLocation loc, int mask, int r, int g, int b) throws GameActionException
	{
		for (int i=0; i<25; i++)
			if ((mask&(1<<i)) > 0)
				rc.setIndicatorDot(loc.add(gridOffX[i],gridOffY[i]),r,g,b);
	}

	static void debug_drawConnections(GridComponent grid) throws GameActionException
	{
		// loop through all of my connected components
		while (grid.isValid())
		{
			// get our square's pathable U unknown
			int pathable = grid.getMaybes();

			// for each edge, now draw locations that connect to adjacent grid indices
			
			for (int dir=0; dir<4; dir++)
			{
				// get index of adjacent square here
				GridComponent adjgrid = grid.offsetTo(dir);
				
				while (adjgrid.isValid())
				{
					int edges = adjgrid.readValue(edgeChans[dir]);					
					
					int conn = (edges&pathable&bitEdge[dir]); 
					if (conn > 0)
					{
						// draw connected edges for this connected component
						debug_drawGridMask(grid.getCenter(),conn,150*colorR[grid.gridCC],150*colorG[grid.gridCC],150*colorB[grid.gridCC]);
					}
					
					adjgrid.nextComponent();
				}
			}

			grid.nextComponent();
		}
		grid.firstComponent();
	}
	
	static void debug_drawBestDirection(GridComponent grid, int channel) throws GameActionException
	{
		grid.firstComponent();
		// loop through all of my connected components
		while (grid.isValid())
		{
			// get our square's pathable U unknown
			int pathable = grid.getMaybes();

			// for each edge, find best direction
			int bestval = (65535&grid.readValue(channel));
			int bestdir = -1;
			
			for (int dir=0; dir<4; dir++)
			{
				// get index of adjacent square here
				GridComponent adjgrid = grid.offsetTo(dir);
				
				while (adjgrid.isValid())
				{
					int edges = adjgrid.readValue(edgeChans[dir]);					
					
					int conn = (edges&pathable&bitEdge[dir]); 
					if (conn > 0)
					{
						int adjval = (65535&adjgrid.readValue(channel));
						if (adjval > bestval)
						{
							bestval = adjval;
							bestdir = dir;
						}
					}
					
					adjgrid.nextComponent();
				}
			}
			
			// and diagonals
			for (int dir=4; dir<8; dir++)
			{
				if ((pathable&bitEdge[dir]) == 0)
					continue;
				
				GridComponent adjgrid = grid.offsetTo(dir);
				while (adjgrid.isValid())
				{
					int adjpath = adjgrid.getMaybes();
					if ((adjpath&bitEdge[dir+4])>0)
					{
						int adjval = (65535&adjgrid.readValue(channel));
						if (adjval > bestval)
						{
							bestval = adjval;
							bestdir = dir;
						}
					}
					adjgrid.nextComponent();
				}
			}
			
			// and draw the best direction
			MapLocation loc = grid.getCenter().add(grid.gridCC,0);
			if (bestdir >= 0)
				rc.setIndicatorLine(loc,loc.add(2*dirOffX[bestdir],2*dirOffY[bestdir]),255*colorR[grid.gridCC],255*colorG[grid.gridCC],255*colorB[grid.gridCC]);

			grid.nextComponent();
		}
		grid.firstComponent();
	}
	
	static void debug_drawValue(MapLocation loc, int value)
	{
		if (value > 0)
		{
			if (value > 255) value = 255;
			rc.setIndicatorDot(loc, value, 0, 0);
		}
		else
		{
			value = -value;
			if (value > 255) value = 255;
			rc.setIndicatorDot(loc, 0, 0, value);			
		}
	}
	
	static void debug_drawGridVals() throws GameActionException
	{
		getExtents();
		for (int x=0; x<GRID_DIM; x++)
		{
			for (int y=0;y<GRID_DIM;y++)
			{
				int gridind = y*GRID_DIM+x;
				
				// not mapped yet
				if (x < gridMinX || x > gridMaxX || y < gridMinY || y > gridMaxY)
					continue;
				
				GridComponent grid = new GridComponent(gridind);
				
				if (Clock.getRoundNum()%50 < 5)
					debug_drawConnections(grid);
				
				//debug_drawBestDirection(grid,gridMiningBase);
				debug_drawBestDirection(grid,gridRageBase);
//				System.out.println(grid.readValue(gridPotentialBase));
				
				int brt = 0;
				if (grid.getFlag(STATUS_SEEN)) brt = 100;
				if (grid.getFlag(STATUS_VISITED)) brt = 255;
				
				
				//if (grid.gridNCC > 0)
				//if ((grid.readValue(gridRageBase)&65535) > 490)
				//	rc.setIndicatorDot(grid.getCenter().add(0,1),255,255,255);
				
				for (int i=0; i<grid.gridNCC; i++)
				{
					//MapValue g = gridGradient(grid);
					//g.x = g.x/(int)(g.value/4+1);
					//g.y = g.y/(int)(g.value/4+1);
										
					//rc.setIndicatorLine(grid.getCenter().add(i,0), grid.getCenter().add(i+g.x,0+g.y), 255, 255, 0);
					debug_drawValue(grid.getCenter().add(i,0),(grid.readValue(gridRageBase)&65535)/4);
					//System.out.println("Grid: " + g.x + "," + g.y + "," + grid.readValue(gridPotentialBase));
					
					grid.nextComponent();
				}
			}
		}
	}
	
	static void debug_assert(boolean val, String s) throws GameActionException
	{
		if (val)
			return;
		
		System.out.println(s);
		rc.breakpoint();
		throw new GameActionException(GameActionExceptionType.CANT_DO_THAT_BRO,s);
	}
}

//class to represent information about a grid location & connected component
class GridComponent
{
	int gridIndex;
	int gridID;
	int gridInfo;
	int gridCC; // 0...NCC-1
	int gridNCC;
	
	// start from a map location, and set without doing pathable-finding
	public GridComponent(MapLocation loc) throws GameActionException
	{
		this(GridComponent.indexFromLocation(loc));
	}
	
	// initialize to a grid index etc
	public GridComponent(int gridPtr) throws GameActionException
	{
		gridIndex = gridPtr&65535;
		gridInfo = RobotPlayer.rc.readBroadcast(RobotPlayer.gridInfoBase+gridIndex);
		gridCC = 0;
		gridNCC = gridInfo&RobotPlayer.GRID_CC_MASK;
		gridID = (gridInfo>>>16);
		
		// we need a minimum of one connected component...
		/*if (gridID == 0)
		{
			addComponent();
			gridID = (gridInfo>>>16);
		}*/
		
		// what connected component did we request?
		int destcc = (gridPtr >>> 16);
		for (int i=0; i<destcc; i++)
			nextComponent();
	}
	
	public void firstComponent()
	{
		gridCC = 0;
		gridNCC = gridInfo&RobotPlayer.GRID_CC_MASK;
		gridID = (gridInfo>>>16);
	}
	
	public void nextComponent() throws GameActionException
	{
		if (gridCC<gridNCC-1)
		{
			gridCC++;
			gridID = RobotPlayer.rc.readBroadcast(RobotPlayer.gridNextIDBase+gridID);
		}
		else
		{
			gridID = 0;
		}
	}
	
	public boolean isValid()
	{
		return gridID>0;
	}
	
	public int getPointer()
	{
		return gridIndex|(gridCC<<16);
	}
	
	public int readValue(int chan) throws GameActionException
	{
		int val = RobotPlayer.rc.readBroadcast(chan + gridID); 
		
		//RobotPlayer.debug_assert(gridID != 0, "Reading from ID 0 for index " + gridIndex + " and cc " + gridCC + " and gridInfo " + Integer.toBinaryString(gridInfo));
		//RobotPlayer.debug_assert(val == 0 || chan != RobotPlayer.gridRageBase || (gridY() >= RobotPlayer.gridMinY && gridY() <= RobotPlayer.gridMaxY && gridX() >= RobotPlayer.gridMinX && gridX() <= RobotPlayer.gridMaxX), 
		//		"Reading off map: " + gridIndex + "/" + gridID + " and cc " + gridCC + " and val " + val);
		return val;
	}
	
	public int readProperty(int chan) throws GameActionException
	{
		return RobotPlayer.rc.readBroadcast(chan + gridIndex);
	}
	
	public void writeValue(int chan, int val) throws GameActionException
	{
		//RobotPlayer.debug_assert(gridID != 0, "Writing to ID 0 for index " + gridIndex + " and cc " + gridCC + " and gridInfo " + Integer.toBinaryString(gridInfo));
		RobotPlayer.debug_assert(chan != RobotPlayer.gridRageBase || (gridY() >= RobotPlayer.gridMinY && gridY() <= RobotPlayer.gridMaxY && gridX() >= RobotPlayer.gridMinX && gridX() <= RobotPlayer.gridMaxX), 
				"Writing off map: " + gridIndex + "/" + gridID + " and cc " + gridCC + " with value " + val);
		
		RobotPlayer.rc.broadcast(chan+gridID, val);
	}

	public void writeProperty(int chan, int val) throws GameActionException
	{
		RobotPlayer.rc.broadcast(chan+gridIndex, val);
	}
	
	public GridComponent offsetTo(int dir) throws GameActionException
	{
		return new GridComponent(gridIndex+RobotPlayer.gridOffset[dir]);
	}
	
	public MapLocation getCenter() throws GameActionException
	{
		return RobotPlayer.gridCenter(gridIndex);
	}
	
	public int getMaybes() throws GameActionException
	{
		// get our square's pathable U unknown, but only if connected
		// only fails if there are two (known) unconnected unknown regions
		// which is fairly pathological, or more probably impossible
		int pathable = readValue(RobotPlayer.gridPathableBase);
		int unknown = RobotPlayer.GRID_MASK&~readProperty(RobotPlayer.gridKnownBase);
		if (pathable == 0 || (RobotPlayer.relaxGrid(unknown,RobotPlayer.GRID_MASK)&pathable)>0)
			pathable |= unknown;
		return pathable;
	}
	
	public int getFastMaybes() throws GameActionException
	{
		int pathable = readValue(RobotPlayer.gridPathableBase);
		int unknown = RobotPlayer.GRID_MASK&~readProperty(RobotPlayer.gridKnownBase);
		return pathable|unknown;
	}
	
	public boolean getFlag(int flag) throws GameActionException
	{
		return (gridInfo&flag)>0;
	}
	
	public void setFlag(int flag) throws GameActionException
	{
		gridInfo = readProperty(RobotPlayer.gridInfoBase);
		gridInfo |= flag;
		writeProperty(RobotPlayer.gridInfoBase,gridInfo);
	}
	
	public void unsetFlag(int flag) throws GameActionException
	{
		gridInfo = readProperty(RobotPlayer.gridInfoBase);
		gridInfo &= ~flag;
		writeProperty(RobotPlayer.gridInfoBase,gridInfo);
	}
	
	int nextCCPointer()
	{		
		int gridx = gridIndex%RobotPlayer.GRID_DIM;
		int gridy = gridIndex/RobotPlayer.GRID_DIM;
		
		int gridcc = gridCC + 1;
		// can we advance by one?
		if (gridcc >= gridNCC)
		{
			gridcc = 0;
			gridx++;
			if (gridx > RobotPlayer.gridMaxX)
			{
				gridx = RobotPlayer.gridMinX;
				gridy++;
				if (gridy > RobotPlayer.gridMaxY)
					gridy = RobotPlayer.gridMinY;
			}
		}
		
		return (gridy*RobotPlayer.GRID_DIM + gridx) + (gridcc << 16);
	}
	
	int nextIndexPointer()
	{		
		int gridx = gridIndex%RobotPlayer.GRID_DIM;
		int gridy = gridIndex/RobotPlayer.GRID_DIM;
		
		gridx++;
		if (gridx > RobotPlayer.gridMaxX)
		{
			gridx = RobotPlayer.gridMinX;
			gridy++;
			if (gridy > RobotPlayer.gridMaxY)
				gridy = RobotPlayer.gridMinY;
		}
		
		return (gridy*RobotPlayer.GRID_DIM + gridx);
	}
	
	public int gridX()
	{
		return gridIndex%RobotPlayer.GRID_DIM;
	}
	
	public int gridY()
	{
		return gridIndex/RobotPlayer.GRID_DIM;
	}
	
	public void initialize() throws GameActionException
	{
		addComponent();
		gridID = (gridInfo>>>16);
	}
	
	// adds a new ID to the linked list of grid cell connected components
	// note that this does not actually advance the block to the next comp.
	// but it does update gridInfo to reflect the addition (unless block is first)
	public void addComponent() throws GameActionException
	{
		if (gridNCC >= RobotPlayer.GRID_MAX_CC)
			return;
		
		gridNCC++;

		// now get the next ID of the next grid element
		int nextid = RobotPlayer.rc.readBroadcast(RobotPlayer.gridNextIDChan);
		RobotPlayer.rc.broadcast(RobotPlayer.gridNextIDChan,nextid+1);
		
		// so this grid cell should be at the current end of the linked list 
		if (gridID > 0)
		{
			// and save it to either the next ID channel
			writeValue(RobotPlayer.gridNextIDBase, nextid);
		}
		else
		{
			gridID = nextid;
			System.out.println("Initial gridID set to " + nextid + " for " + gridIndex);
			RobotPlayer.debug_assert((gridInfo>>16)==0,"New block on bad cell");
			// or it is the first, so set base info
			gridInfo = (gridInfo&65535) | (gridID<<16);
			// this is the first block, so we just added it, so set the edges to 0, since unknown
			writeValue(RobotPlayer.gridEdgesNSBase,RobotPlayer.GRID_MASK);
			writeValue(RobotPlayer.gridEdgesEWBase,RobotPlayer.GRID_MASK);
		}
		
		// save the ncc in grid info
		gridInfo = (gridInfo&(~RobotPlayer.GRID_CC_MASK)) | gridNCC;
		// we need to re-path
		gridInfo &= ~RobotPlayer.STATUS_PATHED;
		// write that we have entered it
		writeProperty(RobotPlayer.gridInfoBase,gridInfo);
	}
	
	// check if we're at the start of the list
	public boolean isFirst() throws GameActionException
	{
		return (gridIndex == (RobotPlayer.gridMinX + RobotPlayer.gridMinY*RobotPlayer.GRID_DIM) && gridCC == 0);
	}
	
	public int getPathInd(MapLocation loc) throws GameActionException
	{
		// construct from a map location
		MapLocation center = this.getCenter();
		return 1<<((loc.x-center.x+RobotPlayer.GRID_SPC/2)+RobotPlayer.GRID_SPC*(loc.y-center.y+RobotPlayer.GRID_SPC/2));
	}
	
	public boolean isInMaybe(MapLocation loc) throws GameActionException
	{
		//int pathable = readValue(RobotPlayer.gridPathableBase);
		int maybe = getMaybes();
		return (maybe&getPathInd(loc))>0;
	}
	
	// find which component corresponds to a square
	// (or none, if we ain't have none)
	public int findComponent(MapLocation loc) throws GameActionException
	{
		// get the path index of the location
		return findComponent(getPathInd(loc));
	}
	
	public int findComponent(int pathind) throws GameActionException
	{
		// recycle to the first component
		firstComponent();
		
		int pathable = 0;
		while (isValid())
		{
			pathable = readValue(RobotPlayer.gridPathableBase);
			if ((pathind&pathable)>0)
				return pathable;
			nextComponent();
		}
		// or none found
		return 0;
	}
	
	public String toString()
	{
		return "Grid (" + gridIndex + "." + gridCC + "/" + gridNCC + ":" + gridID + ")";
	}
	
	static int indexFromLocation(MapLocation loc)
	{
		return RobotPlayer.GRID_DIM * ((RobotPlayer.GRID_OFFSET+loc.y-RobotPlayer.center.y+RobotPlayer.GRID_SPC/2)/RobotPlayer.GRID_SPC)
		+ (RobotPlayer.GRID_OFFSET+loc.x-RobotPlayer.center.x+RobotPlayer.GRID_SPC/2)/RobotPlayer.GRID_SPC;
	}
}