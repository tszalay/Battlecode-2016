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

	/* Grid weight values */
	int WEIGHT_UNKNOWN_EDGE = -10;
	int WEIGHT_ENEMY_HQ = -200;
	int WEIGHT_ENEMY_TOWER = -50;
	int ENEMY_DECAY_RATE = 10;
	
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
	static int mySquad;
	
	// did we move since last turn?
	static boolean justMoved;
	
	static int curRound;
	
	static MapLocation myTarget;
	
	static MapLocation myLocation;
	static Direction facing;
	static int myGridX;
	static int myGridY;
	static int myGridInd;
	static int myGridID;
	static int lastGridInd=-1;

	// my grid location values
	static MapLocation myGridUL;
	static MapLocation myGridBR;
	static MapLocation myGridCenter;
		
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
	static final int GRID_UPDATE_FREQ = 1;
	
	static final int GRID_MAX_CC = 4;
	static final int GRID_CC_MASK = 7;
	
	// note for status flags: lowest 2-3 bits are CC, upper 16 bits are gridid base 
	// rest can be flags just fine and dandy
	
	// have we been there or seen it?
	static final int STATUS_SEEN = (1<<4);
	static final int STATUS_VISITED = (1<<5);
	// have we updated the connectivity recently?
	static final int STATUS_PATHED = (1<<6);
	// is there an enemy HQ/tower there?
	static final int STATUS_HQ = (1<<10);
	static final int STATUS_TOWER = (1<<11);
	
	static final int STATUS_NORTH=0;
	static final int STATUS_SOUTH=3;
	static final int STATUS_EAST=1;
	static final int STATUS_WEST=2;
	
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
	// times that things were last fully computed
	static final int gridLastUpdateChan = curChan++;
	static final int gridLastPotentialChan = curChan++;
	static final int gridLastConnectivityChan = curChan++;

	// pointer to which grid cells we're calculating the above
	static final int gridUpdatePtrChan = curChan++;
	static final int gridPotentialPtrChan = curChan++;
	static final int gridConnectivityPtrChan = curChan++;
	static final int gridNextIDChan = curChan++;
	
	// potential field totals, for averaging
	static final int gridPotentialTotalChan = curChan++;
	static final int gridPotentialOffsetChan = curChan++;

	// map array of basic grid square status
	static final int gridInfoBase = curChan; static {curChan+=GRID_NUM;}
	// map list of next grid IDs
	static final int gridNextIDBase = curChan; static {curChan+=GRID_NUM;}
	
	// these are calculated in a distributed fashion 
	static final int gridFriendBase = curChan; static {curChan+=GRID_NUM;}
	static final int gridEnemyBase = curChan; static {curChan+=GRID_NUM;}
	// diffusion values for frienemy potential
	static final int gridPotentialBase = curChan; static {curChan+=GRID_NUM;}
	
	
	// connectivity values
	// these ones are indexed by location
	static final int gridConnectivityNormalBase = curChan; static {curChan+=GRID_NUM;}
	static final int gridConnectivityKnownBase = curChan; static {curChan+=GRID_NUM;}
	static final int gridConnectivityVoidBase = curChan; static {curChan+=GRID_NUM;}
	// and these ones are indexed by ID
	static final int gridConnectivityPathableBase = curChan; static {curChan+=GRID_NUM;}
	static final int gridConnectivityEdgesNSBase = curChan; static {curChan+=GRID_NUM;}
	static final int gridConnectivityEdgesEWBase = curChan; static {curChan+=GRID_NUM;}
	
	
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
    static int numBarracks = 5;
    static int numSoldiers = 100;
    static int numHelipads = 0;
	static int numSupplyDepots = 0;
	static int numTankFactories = 5;
	static int numTanks = 50;

	
	
	static RobotConsts Consts = new RobotConsts();
	
	static MapLocation[] enemyBuildings;
	
	static double lastOre = 0;
	static double curOre = 0;
	
	
	static final int[] gridOffX = {-2,-1,0,1,2,-2,-1,0,1,2,-2,-1,0,1,2,-2,-1,0,1,2,-2,-1,0,1,2};
	static final int[] gridOffY = {-2,-2,-2,-2,-2,-1,-1,-1,-1,-1,0,0,0,0,0,1,1,1,1,1,2,2,2,2,2};
	static final int[] dirOffsX = {0,1,-1,0};
	static final int[] dirOffsY = {-1,0,0,1};

	static int[] bitAdjacency = {99,231,462,924,792,3171,7399,14798,29596,25368,101472,236768,473536,947072,811776,3247104,7576576,15153152,30306304,25976832,3244032,7569408,15138816,30277632,25952256};
	static int[] bitEdge = {31,17318416,1082401,32505856};
	
	
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
				rc.broadcast(gridConnectivityPtrChan, gridMinX+gridMinY*GRID_DIM);
				rc.broadcast(gridPotentialPtrChan, gridMinX+gridMinY*GRID_DIM);
				rc.broadcast(gridUpdatePtrChan, gridMinX+gridMinY*GRID_DIM);
				
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
			else
				System.out.println("Bytecode wraparound error!");
		}
	}
	
	// what to do in extra cycles
	static void spare() throws GameActionException
	{
		while (Clock.getBytecodesLeft() > 1000 && Clock.getRoundNum() == curRound)
		{
			switch (rand.nextInt(3))
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
			myGridX = (GRID_OFFSET+myLocation.x-center.x+GRID_SPC/2)/GRID_SPC;
			myGridY = (GRID_OFFSET+myLocation.y-center.y+GRID_SPC/2)/GRID_SPC;
			myGridInd = myGridY*GRID_DIM + myGridX;
			
			if (myGridInd != lastGridInd)
			{
				myGridCenter = gridCenter(myGridInd);
				// both coords are inclusive
				myGridUL = myGridCenter.add(-GRID_SPC/2,-GRID_SPC/2);
				myGridBR = myGridCenter.add(GRID_SPC/2,GRID_SPC/2);

				gridAddSeen(myGridInd);
				
				lastGridInd = myGridInd;
			}
			
			int gridinfo = rc.readBroadcast(gridInfoBase+myGridInd);
			int ncc = gridinfo & GRID_CC_MASK;
			int gridid = gridinfo >>> 16;
			int pathind = 1<<((myLocation.x-myGridUL.x)+GRID_SPC*(myLocation.y-myGridUL.y));
	
			int pathable = 0;
			
			for (int cc=0; cc<ncc; cc++)
			{
				pathable = rc.readBroadcast(gridConnectivityPathableBase+gridid);
				if ((pathind&pathable)>0)
					break;
				gridid = rc.readBroadcast(gridNextIDBase+gridid);
			}
			
			if ((pathable&pathind)>0)
				myGridID = gridid;
			else
				myGridID = 0;
			
			debug_drawGridMask(myGridCenter,pathable);
			
			rc.setIndicatorString(0, "Potential: " + rc.readBroadcast(gridPotentialBase+myGridID));
			
			// draw grid boundaries?
			/*
			rc.setIndicatorLine(myGridUL, myGridUL.add(GRID_SPC-1,0), 0, 255, 255);
			rc.setIndicatorLine(myGridUL, myGridUL.add(0,GRID_SPC-1), 0, 255, 255);
			rc.setIndicatorLine(myGridBR, myGridBR.add(0,-GRID_SPC+1), 0, 255, 255);
			rc.setIndicatorLine(myGridBR, myGridBR.add(-GRID_SPC+1,0), 0, 255, 255);
			*/
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
			if (Clock.getRoundNum()<1800) {
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
			}
			attackSomething();
			movePotential();
			supplyTransferFraction = 0.5;
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
			//attackSomething();
			//movePotential();
			int bc = Clock.getBytecodeNum();
			Direction d = gridDescend(myLocation);
			System.out.println((Clock.getBytecodeNum()-bc));
			if (d != Direction.NONE && d != Direction.OMNI)
				tryMove(d);
			//else if (d != Direction.OMNI)
			//	rc.breakpoint();
			
			rc.setIndicatorLine(myLocation, myLocation.add(d,3), 0, 255, 255);
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
			if(rc.isCoreReady()) // otherwise don't waste the bytecode
				miningDuties();
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
	static void gridAddSeen(int gridind) throws GameActionException
	{
		// add "seen" flag to grid cell
		int gridinfo = rc.readBroadcast(gridInfoBase+gridind);
		
		// if we have been here before, we have called this function
		if ((gridinfo&STATUS_VISITED) > 0)
			return;
		
		rc.broadcast(gridInfoBase+gridind, gridinfo|STATUS_VISITED|STATUS_SEEN);
			
		int gridX = gridind%GRID_DIM;
		int gridY = gridind/GRID_DIM;

		// check if the grid needs to be enlarged
		getExtents();
		gridMinX = Math.min(gridMinX, gridX);
		gridMaxX = Math.max(gridMaxX, gridX);
		gridMinY = Math.min(gridMinY, gridY);
		gridMaxY = Math.max(gridMaxY, gridY);				
		setExtents();
		
		// mark adjacent 4 squares as "SEEN", meaning do connectivity processing
		// (if it's inside gridMinX, etc), but not yet "VISITED"
		for (int dir=0;dir<4;dir++)
		{
			int adjind = gridind + gridOffset(dir);
			int adjinfo = rc.readBroadcast(gridInfoBase+adjind);
			if ((adjinfo&STATUS_SEEN)==0)
				rc.broadcast(gridInfoBase+adjind,adjinfo|STATUS_SEEN);
		}
	}
	
	// adds a new ID to the linked list of grid cell connected components
	// this is for distributed grid calculations
	static int gridAddID(int gridind) throws GameActionException
	{
		int gridinfo = rc.readBroadcast(gridInfoBase+gridind);
		
		int gridid = (gridinfo >>> 16);
		int lastgridid = 0;
		
		int gridncc = 0;
		while (gridid > 0)
		{
			lastgridid = gridid;
			gridid = rc.readBroadcast(gridNextIDBase+gridid);
			gridncc++;
		}
		
		// now add one
		gridncc++;
		if (gridncc > GRID_MAX_CC)
			return 0;

		// lastgridid is now the id of the current end of chain, or 0 if beginning
		// now get the next ID of the next grid element
		gridid = rc.readBroadcast(gridNextIDChan);
		rc.broadcast(gridNextIDChan,gridid+1);
		
		if (lastgridid > 0)
		{
			// and save it to either the next ID channel
			rc.broadcast(gridNextIDBase+lastgridid, gridid);
		}
		else
		{
			// or the info channel (base id)
			gridinfo = (gridinfo&65535) | (gridid<<16);
		}
		
		// save the ncc in grid info
		gridinfo = (gridinfo&(~GRID_CC_MASK)) | gridncc;
		// we need to re-path
		gridinfo &= ~STATUS_PATHED;
		// write that we have entered it
		rc.broadcast(gridInfoBase+gridind, gridinfo);
		
		/*
		if (gridind == 1124)
		{
			System.out.println("Added " + gridid + " to the end of " + lastgridid + " at " + gridind);
			rc.breakpoint();
		}
*/
		
		return gridid;
	}
		
	static Direction gridDescend(MapLocation loc) throws GameActionException
	{
		int gridX = (GRID_OFFSET+loc.x-center.x+GRID_SPC/2)/GRID_SPC;
		int gridY = (GRID_OFFSET+loc.y-center.y+GRID_SPC/2)/GRID_SPC;
		int gridind = gridX + gridY*GRID_DIM;
		
		int gridinfo = rc.readBroadcast(gridInfoBase+gridind);
		// get the list ID of the grid cell
		int gridid = (gridinfo >>> 16);
		
		MapLocation gridcenter = gridCenter(gridind);

		int pathindex = ((loc.x-gridcenter.x+GRID_SPC/2)+GRID_SPC*(loc.y-gridcenter.y+GRID_SPC/2));
		int pathind = 1<<pathindex;
		
		// ignoring unknowns for this function
		int pathable = rc.readBroadcast(gridConnectivityPathableBase+gridid);
		
		int ncc = GRID_CC_MASK&gridinfo;
		
		// figure out which subgrid we're on
		for (int cc=0;cc<ncc;cc++)
		{
			// if we're on this one, we're good
			if ((pathind&pathable) > 0)
				break;
			// otherwise, go on to the next one
			gridid = rc.readBroadcast(gridNextIDBase+gridid);
			pathable = rc.readBroadcast(gridConnectivityPathableBase+gridid);
		}
		// if for some reason this didn't work, abandon ship
		if ((pathind&pathable) == 0)
		{
			System.out.println("Pathable is 0");
			return Direction.NONE;
		}
		
		// ok, so now we're on the right subgrid/connected component, as defined by gridid
		// find the max-value neighbor and corresponding connected edge
		// now, let's loop through each adjacent direction & their gridids
		
		// my grid value
		int gridval = rc.readBroadcast(gridPotentialBase+gridid);

		// and minimum values
		int bestedge = 0;
		int bestval = gridval;
		int bestdir = -1;
		int bestid = 0;

		// check each adjacent direction
		for (int dir=0; dir<4; dir++)
		{
			int adjind = gridind+gridOffset(dir);
			int adjinfo = rc.readBroadcast(gridInfoBase+adjind);
			int adjid = adjinfo>>>16;
			int adjncc = adjinfo & GRID_CC_MASK;
			
			final int[] edgeChans = {gridConnectivityEdgesNSBase,gridConnectivityEdgesEWBase,gridConnectivityEdgesEWBase,gridConnectivityEdgesNSBase};
			
			for (int cc=0; cc<adjncc; cc++)
			{
				int edges = rc.readBroadcast(edgeChans[dir]+adjid);
				
				// if they're not unconnected, add the value difference
				if ((edges&pathable&bitEdge[dir]) > 0)
				{
					int dirval = rc.readBroadcast(gridPotentialBase+adjid);
					if (dirval < bestval)
					{
						bestval = dirval;
						bestedge = (edges&pathable&bitEdge[dir]);
						bestdir = dir;
						bestid = adjid;
					}
				}
				
				// and cycle on to the next connected component
				adjid = rc.readBroadcast(gridNextIDBase+adjid);
			}
		}
		
		// nothing better found
		if (bestdir == -1)
		{
			System.out.println("On best square");
			return Direction.OMNI;
		}
		
		// ok, now we have the best edge that we want to go to
		// we flood-fill out until we reach pathind
		pathable = bestedge;
		
		// if we're already on the edge, however, we really just want to step over
		// to the target square
		if ((pathind&pathable)>0)
		{
			// where do we go on the adjacent guy
			int adjpathable = rc.readBroadcast(gridConnectivityPathableBase+bestid);
			// again, ignore unknowns, we're assuming connectivity calculation is running smoothly
			// offset our grid center to the adjacent square
			
			MapLocation adjcenter = gridcenter.add(GRID_SPC*dirOffsX[bestdir],GRID_SPC*dirOffsY[bestdir]);
			//rc.setIndicatorDot(gridcenter, 255, 0, 0);
			// and figure out which pathable indices we can go to, boom-boom
			//System.out.println("adjpathable of " + bestid + " is " + Integer.toBinaryString(adjpathable) + " in direction " + bestdir);
			
			
			adjpathable &= bitEdge[3-bestdir];
			// fooooo
			debug_drawGridMask(adjcenter,adjpathable);
			
			// now adjpathable is 1 in places on adjacent grid where we can step
			// we just need to find which one we want to step to
			// so whittle off the bits one by one
			while (adjpathable > 0)
			{
				int setbit = Integer.numberOfTrailingZeros(adjpathable);
				MapLocation dest = adjcenter.add(gridOffX[setbit],gridOffY[setbit]);
				if (dest.distanceSquaredTo(loc) <= 2)
					return loc.directionTo(dest);
				// and drop off last bit
				adjpathable &= adjpathable-1;
			}
			System.out.println("No adjacent squares traversable");
			debug_drawGridMask(gridcenter,rc.readBroadcast(gridConnectivityPathableBase+gridid));
			debug_drawGridMask(adjcenter,rc.readBroadcast(gridConnectivityEdgesEWBase+bestid));
			return Direction.NONE;
		}
		
		int norms = rc.readBroadcast(gridConnectivityNormalBase+gridind);

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
				return Direction.NONE;
			}
			
			pathable = path;
		}
		
		// now, if we got to here, pathable is one step before we hit pathind, so we find the possible directions by masking it
		pathable &= bitAdjacency[pathindex];
		
		debug_drawGridMask(gridcenter,bestedge);
		
		// now we figure a bit that is set
		int setbit = Integer.numberOfTrailingZeros(pathable);
		// so now we have the row-by-row offset, shift it to the middle of the grid, and use the grid offsets to get direction
		setbit = setbit-pathindex+12;
		Direction dir = loc.directionTo(loc.add(gridOffX[setbit],gridOffY[setbit]));
		
		System.out.println(setbit + "/" + dir);
		
		return dir;
	}
	
	static MapValue gridGradient(int ptr) throws GameActionException
	{
		int bc = Clock.getBytecodeNum();
		
		int gridind = (ptr & 65535);
		int gridcc = (ptr>>>16);
		// and corresponding info
		int gridinfo = rc.readBroadcast(gridInfoBase+gridind);
		// get the list ID of the grid cell
		int gridid = (gridinfo >>> 16);

		MapValue gradient = new MapValue(0,0,0);
		
		// now step forward to the relevant cc  
		for (int i=0; i<gridcc; i++)
			gridid = rc.readBroadcast(gridNextIDBase+gridid);

		// get our square's pathable U maybe
		int pathable = rc.readBroadcast(gridConnectivityPathableBase+gridid);
		pathable |= GRID_MASK&(~rc.readBroadcast(gridConnectivityKnownBase+gridind));
		
		// now, let's loop through each adjacent direction & their gridids
		int[] dirvals = new int[4];
		
		int gridval = rc.readBroadcast(gridPotentialBase+gridid);
		
		for (int dir=0; dir<4; dir++)
		{
			int adjind = gridind+gridOffset(dir);
			int adjinfo = rc.readBroadcast(gridInfoBase+adjind);
			int adjid = adjinfo>>>16;
			int adjncc = adjinfo & GRID_CC_MASK;
			
			final int[] edgeChans = {gridConnectivityEdgesNSBase,gridConnectivityEdgesEWBase,gridConnectivityEdgesEWBase,gridConnectivityEdgesNSBase};
			
			int dircount = 0;
			
			for (int cc=0; cc<adjncc; cc++)
			{
				int edges = rc.readBroadcast(edgeChans[dir]+adjid);
				
				// if they're not unconnected, add the value difference
				if ((edges&pathable&bitEdge[dir]) > 0)
				{
					dirvals[dir] += rc.readBroadcast(gridPotentialBase+adjid);
					dircount++;
				}
				
				// and cycle on to the next connected component
				adjid = rc.readBroadcast(gridNextIDBase+adjid);
			}
			
			if (dircount > 0)
			{
				gradient.x -= (dirvals[dir]/dircount - gridval)*dirOffsX[dir];
				gradient.y -= (dirvals[dir]/dircount - gridval)*dirOffsY[dir];
			}
		}
		
		gradient.value = Math.sqrt(gradient.x*gradient.x+gradient.y*gradient.y);
		
		//System.out.println("Gradient time: " + (Clock.getBytecodeNum()-bc));
		return gradient;
	}
	
	static boolean gridUpdate() throws GameActionException
	{
		// 100 bytecodes for preamble
		// 100 bytecodes for senseRobots
		// ~100 bytecodes/robot
		// 100 bytecodes for broadcasts
		
		int bc = Clock.getBytecodeNum();

		int ptr = rc.readBroadcast(gridUpdatePtrChan);
		
		// position-based grid index
		int gridind = (ptr & 65535);
		int gridcc = (ptr>>>16);
		// and corresponding info
		int gridinfo = rc.readBroadcast(gridInfoBase+gridind);
		// get the list ID of the grid cell
		int gridid = (gridinfo >>> 16);
		
		// if it's zero, make sure we add it
		if (gridid==0) gridid = gridAddID(gridind);
		
		// check if we made it all the way around
		if (ptr == (gridMinX + gridMinY*GRID_DIM))
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
		rc.broadcast(gridUpdatePtrChan,nextGridCC(ptr,gridinfo));
		
		MapLocation loc = gridCenter(gridind);

		MapLocation ul = loc.add(-GRID_SPC/2,-GRID_SPC/2);
		MapLocation br = loc.add(GRID_SPC/2,GRID_SPC/2);
		
		
		// now step forward to the relevant cc  
		for (int i=0; i<gridcc; i++)
			gridid = rc.readBroadcast(gridNextIDBase+gridid);
		
		// gridid now contains id of relevant connected component, is what we want to read/write
		// so figure out which regions belong to it
		int pathable = rc.readBroadcast(gridConnectivityPathableBase+gridid);
		// ignore unknowns, because we literally can't sense anything there
		//pathable |= GRID_MASK&(~rc.readBroadcast(gridConnectivityKnownBase+gridind));
		
		// now sense frienemies in all grid cells
		RobotInfo[] bots = new RobotInfo[0];
		if ((gridinfo&STATUS_SEEN)>0)
			bots = rc.senseNearbyRobots(loc,GRID_SENSE,null);
		
		int prevFriend = rc.readBroadcast(gridFriendBase+gridid);
		int prevEnemy = rc.readBroadcast(gridEnemyBase+gridid);
		int prevPotential = rc.readBroadcast(gridPotentialBase+gridid);
		
		int gridFriend = 0;
		int gridEnemy = 0;
		
		for (RobotInfo ri : bots)
		{
			// not in this grid cell?
			if ((ri.location.x < ul.x) || (ri.location.x > br.x) ||
					(ri.location.y < ul.y) || (ri.location.y > br.y))
				continue;
			
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
		
		// quick check for myself
		if (myGridInd == gridind)
		{
			int pathind = (1<<((myLocation.x-ul.x)+GRID_SPC*(myLocation.y-ul.y)));
			if ((pathind&pathable)>0)
				gridFriend += Consts.friendWeights[myType.ordinal()];
		}
		
		//Clock.getRoundNum();
		//System.out.print("");
		//if (gridind == 1328+52*1)
		//	System.out.println("#######1328 etc, " + Clock.getRoundNum() + " called senseNearbyRobots at " + loc + " in radius " + GRID_SENSE + " and found " + nunits + " units" + 
		//" my location: " + myLocation + "{}{}{}{}{}" + gridFriend);

		
		// and rebroadcast the values
		if (gridFriend != prevFriend)
			rc.broadcast(gridFriendBase+gridid,gridFriend);

		if (gridEnemy != prevEnemy)
			rc.broadcast(gridEnemyBase+gridid,gridEnemy);
		
		//System.out.println("Update time (" + bots.length + "): " + (Clock.getBytecodeNum()-bc));
		
		return true;
	}
	
	static boolean gridDiffuse() throws GameActionException
	{
		int bc = Clock.getBytecodeNum();

		int ptr = rc.readBroadcast(gridPotentialPtrChan);
		
		// position-based grid index
		int gridind = (ptr & 65535);
		int gridcc = (ptr>>>16);
		// and corresponding info
		int gridinfo = rc.readBroadcast(gridInfoBase+gridind);
		// get the list ID of the grid cell
		int gridid = (gridinfo >>> 16);
		
		// if it's zero, make sure we add it
		if (gridid==0) gridid = gridAddID(gridind);
		
		// check if we made it all the way around
		if (ptr == (gridMinX + gridMinY*GRID_DIM))
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
		rc.broadcast(gridPotentialPtrChan,nextGridCC(ptr,gridinfo));		
		// # of grid squares to closest friendly unit
				
		// now step forward to the relevant cc  
		for (int i=0; i<gridcc; i++)
			gridid = rc.readBroadcast(gridNextIDBase+gridid);

		
		// source term for potential is difference between friend and enemy squares
		int gridval = rc.readBroadcast(gridPotentialBase+gridid);
		int source = rc.readBroadcast(gridFriendBase+gridid);		
		source = source + rc.readBroadcast(gridEnemyBase+gridid);

		
		if ((gridinfo&STATUS_HQ)>0)
			source += Consts.WEIGHT_ENEMY_HQ;
		//else if ((gridinfo&STATUS_TOWER)>0)
		//	source += Consts.WEIGHT_ENEMY_TOWER;

		
		// get our square's pathable U unknown, and see what it connects to...
		int pathable = rc.readBroadcast(gridConnectivityPathableBase+gridid);
		pathable |= GRID_MASK&(~rc.readBroadcast(gridConnectivityKnownBase+gridind));
		
		// now, let's loop through each adjacent direction & their gridids

		int newval = 0;
		
		for (int dir=0; dir<4; dir++)
		{
			int adjind = gridind+gridOffset(dir);
			int adjinfo = rc.readBroadcast(gridInfoBase+adjind);
			int adjid = adjinfo>>>16;
			int adjncc = adjinfo & GRID_CC_MASK;
			
			final int[] edgeChans = {gridConnectivityEdgesNSBase,gridConnectivityEdgesEWBase,gridConnectivityEdgesEWBase,gridConnectivityEdgesNSBase};
			
			int dirval = 0;

			for (int cc=0; cc<adjncc; cc++)
			{
				int edges = rc.readBroadcast(edgeChans[dir]+adjid);
				
				// possibly connected?
				if ((edges&pathable&bitEdge[dir]) > 0) // add adjacent value
					dirval += rc.readBroadcast(gridPotentialBase+adjid);
				else // insulating boundary, add my value
					dirval += gridval;
				
				/*if (gridind == 1123)
				{
					System.out.println(adjind + "@ " + dir + "," + adjid + " ----- " + adjncc + " | " + newval + " ; " + gridval);
					System.out.println("offset: " + rc.readBroadcast(gridPotentialOffsetChan) + ", adjval = " + rc.readBroadcast(gridPotentialBase+adjid));
				}
				*/
				// and cycle on to the next connected component
				adjid = rc.readBroadcast(gridNextIDBase+adjid);
			}
			
			if (adjncc > 0)
				dirval /= adjncc;
			else
				dirval = gridval;
			
			newval += dirval;
		}
		
		/*if (gridind == 1123)
		{
			System.out.println("newval sum: " + newval);
		}*/

		// and take the average
		newval = newval / 4;
		
		newval += source;
		
		// shift the potential by the offset
		newval += rc.readBroadcast(gridPotentialOffsetChan);
		
		// and add the value we're writing to the accumulating total
		rc.broadcast(gridPotentialTotalChan, newval+rc.readBroadcast(gridPotentialTotalChan));
		
		rc.broadcast(gridPotentialBase+gridid, newval);
		//rc.broadcast(gridPotentialPtrChan,(ptr+1)%gridn);
		//System.out.println("Diffuse time (" + edgecount + "): " + (Clock.getBytecodeNum()-bc));
		
		return true;
	}
	
		
	static boolean gridConnectivity() throws GameActionException
	{
		int bc = Clock.getBytecodeNum();
		
		int ptr = rc.readBroadcast(gridConnectivityPtrChan);
		
		// position-based grid index
		int gridind = (ptr & 65535);
		int gridcc = (ptr>>>16);
		// and corresponding info
		int gridinfo = rc.readBroadcast(gridInfoBase+gridind);
		// get the list ID of the grid cell
		int gridid = (gridinfo >>> 16);
		
		// if it's zero, make sure we add it
		if (gridid==0) gridid = gridAddID(gridind);
		
		// go on to the next connected component, so we don't double-count things
		rc.broadcast(gridConnectivityPtrChan,nextGridCC(ptr,gridinfo));
		
		// check if we made it all the way around
		if (ptr == (gridMinX + gridMinY*GRID_DIM))
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
		if ((gridinfo&STATUS_SEEN)==0)
		{
			rc.broadcast(gridConnectivityEdgesEWBase+gridid,GRID_MASK);
			rc.broadcast(gridConnectivityEdgesNSBase+gridid,GRID_MASK);
			return true;
		}
		
		// CHECKPOINT #1: CHECK IF WE ARE ADDING ANY NEW TERRAIN TILES
		MapLocation loc = gridCenter(gridind);
		
		// get previously loaded values for this grid cell
		int norms = rc.readBroadcast(gridConnectivityNormalBase+gridind);
		int voids = rc.readBroadcast(gridConnectivityVoidBase+gridind);
		int known = rc.readBroadcast(gridConnectivityKnownBase+gridind);
		
		int oldknown = known;
		
		// do we know all the tiles?
		// if we don't, try finding some new ones
		
		int nadd=0;
		
		// only add terrain tiles right before connected-component 0
		// to make sure next steps are consistent
		if (known != GRID_MASK && gridcc == 0)
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
				rc.broadcast(gridConnectivityNormalBase+gridind,norms);
				rc.broadcast(gridConnectivityVoidBase+gridind,voids);
				rc.broadcast(gridConnectivityKnownBase+gridind,known);
				// and flag it as needing a path update
				gridinfo &= ~STATUS_PATHED;
				rc.broadcast(gridInfoBase+gridind, gridinfo);
			}
			
			// and if we added a lot, return without doing stuff, but stay on the same square
			if (nadd > 10)
			{
				System.out.println("Connectivity add time for " + gridind + ": " + (Clock.getBytecodeNum()-bc));
				return true;
			}
		}
		
		// do we know anything at all, or do we need to re-path?
		if (known == 0 || (gridinfo&STATUS_PATHED) > 0)
		{
			//System.out.println("Blank grid time (" + nadd + "): " + (Clock.getBytecodeNum()-bc));
			return true;
		}
		
		// CHECKPOINT #3: ADD UP PATHABLES UP TO THIS CONNECTED COMPONENT INDEX
		// note that this is where gridid gets advanced to current grid cell
		int prevpathable = 0;
		for (int i=0; i<gridcc; i++)
		{
			prevpathable |= rc.readBroadcast(gridConnectivityPathableBase+gridid);
			gridid = rc.readBroadcast(gridNextIDBase+gridid);
		}

		// now flood-fill the current connected component as far as we can
		int pathable = rc.readBroadcast(gridConnectivityPathableBase+gridid);
		
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
			if (newpath == 0)
				break;
		}
		
		// save the path we found
		rc.broadcast(gridConnectivityPathableBase+gridid,pathable);

		// this is the region that is pathed so far
		prevpathable |= pathable;
		// if it's connected to unknown
		int maybe = relaxGrid((~known)&GRID_MASK,(~voids)&GRID_MASK);
		
		// norms that we are considering adding
		int newnorms = norms & (~prevpathable);
		
		// CHECKPOINT #4: ANY NORMS NOT IN PREVIOUS PATHABLES THAT ALSO AREN'T CONNECTED VIA MAYBE
		if (gridcc+1 == (gridinfo&GRID_CC_MASK))
		{
			if ((newnorms>0) && ((newnorms&maybe)==0||(prevpathable&maybe)==0))
			{
				// add a new connected component
				// first, add a new grid cell at the particular index
				int newid = gridAddID(gridind);
				// now, we shouldn't need to do anything
				// the norms should be filled in automatically next update
				System.out.println("newnorms = " + Integer.toBinaryString(newnorms) + " | " + Integer.toBinaryString(pathable) + 
						" | " + Integer.toBinaryString(prevpathable));
			}
			else
			{
				// no new components on the last update, so set pathable
				rc.broadcast(gridInfoBase+gridind,gridinfo|STATUS_PATHED);
			}
		}
		
		// CHECKPOINT #5: NOW CALCULATE OUR MAYBE EDGES
		// for this pathable, it's defined as edges on adjacent square that can get to pathable
		// or to pathable|unknown, if they connect
		if ((pathable&maybe)>0 || pathable == 0)
			pathable |= GRID_MASK&(~known);
		
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
		rc.broadcast(gridConnectivityEdgesNSBase+gridid, nsEdge);
		rc.broadcast(gridConnectivityEdgesEWBase+gridid, ewEdge);
		
		System.out.println("Connectivity path time: " + (Clock.getBytecodeNum()-bc));

		return true;
	}
	
	// moves on to the next connected component index
	static int nextGridCC(int ptr, int gridinfo)
	{		
		int gridind = ptr&65535;
		int gridcc = (ptr >>> 16);
		int gridx = gridind%GRID_DIM;
		int gridy = gridind/GRID_DIM;
		int gridncc = gridinfo&GRID_CC_MASK;
		
		gridcc++;
		if (gridcc >= gridncc)
		{
			gridcc = 0;
			gridx++;
			if (gridx > gridMaxX)
			{
				gridx = gridMinX;
				gridy++;
				if (gridy > gridMaxY)
					gridy = gridMinY;
			}
		}
		
		return (gridy*GRID_DIM + gridx) + (gridcc << 16);
	}
	
	// moves on to the next grid square, ignoring connected components
	static int nextGridIndex(int ptr, int gridinfo)
	{		
		int gridind = ptr&65535;
		int gridx = gridind%GRID_DIM;
		int gridy = gridind/GRID_DIM;
		
		int gridcc = 0;
		gridx++;
		if (gridx > gridMaxX)
		{
			gridx = gridMinX;
			gridy++;
			if (gridy > gridMaxY)
				gridy = gridMinY;
		}
		
		return (gridy*GRID_DIM + gridx) + (gridcc << 16);
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
	
	static int gridOffset(int dir)
	{
		switch (dir)
		{
		case STATUS_NORTH:
			return -GRID_DIM;
		case STATUS_SOUTH:
			return GRID_DIM;
		case STATUS_EAST:
			return +1;
		case STATUS_WEST:
			return -1;
		}
		return 0;
	}
	
	static int getRobotStrength(double health, double supply, RobotType type)
	{
		return 10*(int)(type.attackPower + ((supply>type.supplyUpkeep*10)?5:0));
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
	
	private static boolean isGoodMovementDirection() throws GameActionException { //checks if the facing direction is "good", meaning safe from towers and not a blockage or off-map or occupied
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
			if (dd == facing.opposite())
				continue;
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
			if (dd == facing.opposite())
				continue;
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
	
	static void debug_drawGridMask(MapLocation loc, int pathable) throws GameActionException
	{
		for (int i=0; i<25; i++)
			if ((pathable&(1<<i)) > 0)
				rc.setIndicatorDot(loc.add(gridOffX[i],gridOffY[i]),255,255,255);
	}
	
	static void debug_drawGridMask(int gridind) throws GameActionException
	{
		int gridinfo = rc.readBroadcast(gridInfoBase+gridind);
		int ncc = gridinfo & GRID_CC_MASK;
		int[] pathables = new int[ncc];
		
		int gridid =  gridinfo >>> 16;
		
		if (gridid == 0)
			return;
		
		if (ncc == 0)
			return;

		for (int i=0; i<ncc; i++)
		{
			pathables[i] = rc.readBroadcast(gridConnectivityPathableBase+gridid);
			//pathables[i] = rc.readBroadcast(gridConnectivityEdgesNSBase+gridid);
			//pathables[i] = rc.readBroadcast(gridConnectivityEdgesEWBase+gridid);
			gridid = rc.readBroadcast(gridNextIDBase+gridid);
		}
		
		MapLocation loc = gridCenter(gridind);
		
		for (int i=0; i<25; i++)
		{
			for (int j=0; j<ncc; j++)
			{
				if ((pathables[j]&(1<<i)) > 0)
				{
					rc.setIndicatorDot(loc.add(gridOffX[i],gridOffY[i]),50*(j+1),50*(j+1),50*(j+1));
				}
			}
		}
	}
	
	static int debug_getGridVal(int gridinfo, int cc, int chanbase) throws GameActionException
	{
		int gridid = (gridinfo>>>16);
			
		if (gridid==0)
			return 0;
		
		for (int i=0; i<cc; i++)
			gridid = rc.readBroadcast(gridNextIDBase+gridid);
		
		return rc.readBroadcast(chanbase+gridid);
	}
	
	static int debug_nConnected(int ptr, int dir) throws GameActionException
	{
		int gridind = (ptr & 65535);
		int gridcc = (ptr>>>16);
		// and corresponding info
		int gridinfo = rc.readBroadcast(gridInfoBase+gridind);
		// get the list ID of the grid cell
		int gridid = (gridinfo >>> 16);

		// now step forward to the relevant cc  
		for (int i=0; i<gridcc; i++)
			gridid = rc.readBroadcast(gridNextIDBase+gridid);

		// get our square's pathable U unknown
		int pathable = rc.readBroadcast(gridConnectivityPathableBase+gridid);
		pathable |= GRID_MASK&(~rc.readBroadcast(gridConnectivityKnownBase+gridind));
		
		int adjind = gridind+gridOffset(dir);
		int adjinfo = rc.readBroadcast(gridInfoBase+adjind);
		int adjid = adjinfo>>>16;
		int adjncc = adjinfo & GRID_CC_MASK;
		
		int nconn = 0;
		
		final int[] edgeChans = {gridConnectivityEdgesNSBase,gridConnectivityEdgesEWBase,gridConnectivityEdgesEWBase,gridConnectivityEdgesNSBase};
			
		for (int cc=0; cc<adjncc; cc++)
		{
			int edges = rc.readBroadcast(edgeChans[dir]+adjid);
			
			// and cycle on to the next connected component
			adjid = rc.readBroadcast(gridNextIDBase+adjid);

			if ((edges&pathable&bitEdge[dir]) > 0)
				nconn++;
		}
		return nconn;
	}
	
	static void debug_drawGridVals() throws GameActionException
	{
		if (Clock.getRoundNum() % 5 > 5)
			return;
		
		getExtents();
		for (int x=0; x<GRID_DIM; x++)
		{
			for (int y=0;y<GRID_DIM;y++)
			{
				int gridind = y*GRID_DIM+x;
				
				// not mapped yet
				int gridinfo = rc.readBroadcast(gridInfoBase+gridind);
				if (x < gridMinX || x > gridMaxX || y < gridMinY || y > gridMaxY)
					continue;
				
				//System.out.println("Found seen block @ " + gridind);
				//if (Clock.getRoundNum()%5 < 3)
				//	debug_drawGridMask(gridind);
				//else {
				MapLocation loc = gridCenter(gridind);
				//int val = (rc.readBroadcast(gridPotentialBase+gridind));
				
				// display connectivity #
				int val = 0;
				int ncc = (gridinfo & GRID_CC_MASK);
				for (int i=0; i<ncc; i++)
				{
					val = debug_getGridVal(gridinfo,i,gridFriendBase);
					if (val > 255) val = 255;
					if (val < 0) val = 0;
					rc.setIndicatorDot(loc.add(i,0),val,val,0);
					
					MapValue gradient = gridGradient(gridind | (i<<16));
					int len = (int)(gradient.value/4) + 1;
					//int len = 40;
					gradient.x /= len;
					gradient.y /= len;
					rc.setIndicatorLine(loc.add(i,0),loc.add(i+gradient.x,gradient.y),0,255,0);
					
					val = debug_getGridVal(gridinfo,i,gridPotentialBase);
					if (val > 255) val = 255;
					if (val < 0) val = 0;
					rc.setIndicatorDot(loc.add(i,1),val,val,0);
					
					for (int dir=0; dir<4; dir++)
					{
						int nconn = debug_nConnected(gridind|(i<<16),dir); 
						if (nconn > 0)
						{
							rc.setIndicatorLine(loc.add(i,0),loc.add(i+dirOffsX[dir]*2,dirOffsY[dir]*2),255-80*nconn,0,0);
						}
					}
				}
				
				//}
				/*switch (ncc)
				{
				case 0:
					rc.setIndicatorDot(loc,0,0,0);
					break;
				case 1:
					rc.setIndicatorDot(loc,0,0,255);
					break;
				case 2:
					rc.setIndicatorDot(loc,0,255,0);
					break;
				case 3:
					rc.setIndicatorDot(loc,255,0,0);
					break;
				case 4:
					rc.setIndicatorDot(loc,255,255,255);
					break;
				}*/
				/*if ((gridinfo&STATUS_HQ) > 0)
				{
					rc.setIndicatorDot(loc,255,255,255);
					continue;
				}
				if ((gridstatus&STATUS_TOWER) > 0)
				{
					rc.setIndicatorDot(loc,128,128,128);
					continue;
				}
				*/
				//int dist2friends = val >> 16;
				/*if (dist2friends > 2)
				{
					rc.setIndicatorDot(loc,255,255,0);
				}
				else*/
				/*
				if (val > 0)
				{
					// friendly-controlled, set blue
					if (val>5)
						val += 100;
					if (val>255) val = 255;
					rc.setIndicatorDot(loc,0,0,val);
				}
				else
				{
					// enemy-controlled, set red
					val = -val;
					if (val>5)
						val += 100;
					if (val>255) val = 255;
					rc.setIndicatorDot(loc,val,0,0);
				}
				*/
				// check connectivities
				/*
				if ((gridstatus&STATUS_CONN_NORTH)>0)
				{
					int l = 1;
					if ((gridstatus&STATUS_KNOW_NORTH)>0)
						l=GRID_SPC;
					rc.setIndicatorLine(loc,loc.add(0,-l),255,255,0);
				}
				else if ((gridstatus&STATUS_KNOW_NORTH)>0)
					rc.setIndicatorDot(loc.add(0,-1),255,255,0);

				if ((gridstatus&STATUS_CONN_SOUTH)>0)
				{
					int l = 1;
					if ((gridstatus&STATUS_KNOW_SOUTH)>0)
						l=GRID_SPC;
					rc.setIndicatorLine(loc,loc.add(0,l),255,255,0);
				}
				else if ((gridstatus&STATUS_KNOW_SOUTH)>0)
					rc.setIndicatorDot(loc.add(0,1),255,255,0);

				if ((gridstatus&STATUS_CONN_EAST)>0)
				{
					int l = 1;
					if ((gridstatus&STATUS_KNOW_EAST)>0)
						l=GRID_SPC;
					rc.setIndicatorLine(loc,loc.add(l,0),255,255,0);
				}
				else if ((gridstatus&STATUS_KNOW_EAST)>0)
					rc.setIndicatorDot(loc.add(1,0),255,255,0);
				
				if ((gridstatus&STATUS_CONN_WEST)>0)
				{
					int l = 1;
					if ((gridstatus&STATUS_KNOW_WEST)>0)
						l=GRID_SPC;
					rc.setIndicatorLine(loc,loc.add(-l,0),255,255,0);
				}
				else if ((gridstatus&STATUS_KNOW_WEST)>0)
					rc.setIndicatorDot(loc.add(-1,0),255,255,0);

				MapValue gradient = gridGradient(gridind);
				int len = (int)(gradient.value/4) + 1;
				//int len = 2;
				gradient.x /= len;
				gradient.y /= len;
				
				rc.setIndicatorLine(loc,loc.add(gradient.x,gradient.y),0,255,0);
				*/
				// draw a dot
				/*MapLocation loc = gridCenter(gridind);
				int val = (rc.readBroadcast(gridFriendBase+gridind));
				if (val > 255) val = 255;
				rc.setIndicatorDot(loc,0,0,val);
				
				val = (rc.readBroadcast(gridEnemyBase+gridind));
				if (val > 255) val = 255;
				rc.setIndicatorDot(loc.add(1,0),val,0,0);*/
			}
		}
	}
	
	
	
}
