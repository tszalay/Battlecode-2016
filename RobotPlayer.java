package rAAAge_5_2;

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
	
	long[] attackMask = {0L,0L,17179869184L,17314086912L,17582522368L,17582522368L,17582522368L,17582522368L,17582522368L,402653184L,268435456L,0L,0L,0L,17179869184L,25904021504L,60565749760L,61102882816L,61103407104L,61103407104L,61103407104L,61069852672L,52479655936L,939524096L,268435456L,0L,17179869184L,25904021504L,64860717056L,67562176512L,68703552000L,68704601600L,68704601600L,68704601600L,68687758848L,64292127744L,53553397760L,939524096L,268435456L,25769803776L,64592281600L,67562176512L,68711973504L,68717348481L,68719447683L,68719447683L,68719447683L,68711026179L,68689858050L,64292127744L,36373528576L,805306368L,30064771072L,66756542464L,68711448576L,68717363904L,68719472577L,68719476679L,68719476679L,68719476679L,68719460103L,68710964742L,68656303104L,38522060800L,1879048192L,30064771072L,66764931072L,68715659264L,68719469280L,68719476721L,68719476735L,68719476735L,68719476735L,68719476511L,68719361550L,68660501504L,38524157952L,1879048192L,30064771072L,66764931072L,68715659264L,68719469280L,68719476721L,68719476735L,68719476735L,68719476735L,68719476511L,68719361550L,68660501504L,38524157952L,1879048192L,30064771072L,66764931072L,68715659264L,68719469280L,68719476721L,68719476735L,68719476735L,68719476735L,68719476511L,68719361550L,68660501504L,38524157952L,1879048192L,30064771072L,66731376640L,68715331584L,68718944352L,68719475568L,68719476604L,68719476604L,68719476604L,68719410972L,68685806604L,68660238336L,38523633664L,1879048192L,12884901888L,66580381696L,68346200064L,68717436960L,68718882864L,68719409208L,68719409208L,68719409208L,68685821976L,68668635144L,51412733952L,38388367360L,1610612736L,4294967296L,15032385536L,67654123520L,68346200064L,68683849728L,68684902400L,68684902400L,68684902400L,68668108800L,51412733952L,42683334656L,3758096384L,1073741824L,0L,4294967296L,15032385536L,50474254336L,51015319552L,51017416704L,51017416704L,51017416704L,51009028096L,42414899200L,3758096384L,1073741824L,0L,0L,0L,4294967296L,6442450944L,7516192768L,7516192768L,7516192768L,7516192768L,7516192768L,3221225472L,1073741824L,0L,0L};
	int[] dirOffsets = {0,7,1,6,2,5,3,4};
	
	static int[] directionLocsX = {0, 1, 1, 1, 0, -1, -1, -1, 0, 0};
	static int[] directionLocsY = {-1, -1, 0, 1, 1, 1, 0, -1, 0, 0};
	static float[] cos = {1.0f, .707f, 0.0f, -.707f, -1.0f, -.707f, 0.0f, .707f};
	static float[] sin = {0.0f, .707f, 1.0f, .707f, 0.0f, -.707f, -1.0f, -.707f};
	static float[] directionLengths = {1, 1.414f, 1, 1.414f, 1, 1.414f, 1, 1.414f, 0, 0};
	
	// maps position offsets in our 5x5 bit-grid to direction flags
	int[] gridToDir = { 0,   0,   0,   0,   0,
					    0,1<<7,1<<0,1<<1,   0,
					    0,1<<6,   0,1<<2,   0,
					    0,1<<5,1<<4,1<<3,   0,
					    0,   0,   0,   0,   0 };

	/* Grid weight values */
	int WEIGHT_UNKNOWN_EDGE = -10;
	int WEIGHT_ENEMY_HQ = 1000;
	int WEIGHT_ENEMY_TOWER = 1000;
	int ENEMY_DECAY_RATE = 10;
	
	int MINING_DECAY = -20; // additive factor for neighboring grid squares
	int MINING_GRID_MAX = 500; // all squares in 5x5 grid having 20 ore
	int MINING_OFFSET = 1000;
	int WEIGHT_ORE_MINER = -50;
	int WEIGHT_ORE_BEAVER = -30;
	
	int GRID_DECAY = -1;
	int GRID_BASE = 1000;
	int GRID_INIT_BASE = 500;
	
	int RAGE_DIST = 49;
	
	int HOLD_DISTANCE = 24;
	int RALLY_STRENGTH_THRESH = 47;
	
	int ATTACK_ROUND = 1700;
	
	int TOWER_REMOVE_ROUND = 1500;
	
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
			1000, // BEAVER
			0, // COMPUTER
			1000, // SOLDIER
			1000, // BASHER
			1000, // MINER
			1000, // DRONE
			100, // TANK
			1000, // COMMANDER
			1000, // LAUNCHER
			0  // MISSILE
	};

	int[] unitVals = {
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
			4, // SOLDIER
			4, // BASHER
			1, // MINER
			4, // DRONE
			8, // TANK
			8, // COMMANDER
			10, // LAUNCHER
			0  // MISSILE
	};
	
	int[] droneUnitAtt = {
			0, // HQ 
			0, // TOWER
			1, // SUPPLYDEPOT
			1, // TECHNOLOGYINSTITUTE
			4, // BARRACKS
			4, // HELIPAD
			1, // TRAININGFIELD
			5, // TANKFACTORY
			6, // MINERFACTORY
			1, // HANDWASHSTATION
			4, // AEROSPACELAB
			2, // BEAVER
			2, // COMPUTER
			1, // SOLDIER
			1, // BASHER
			6, // MINER
			1, // DRONE
			0, // TANK
			7, // COMMANDER
			0, // LAUNCHER
			0  // MISSILE
	};
	
	int[] minerUnitVals = {
			100, // HQ 
			100, // TOWER
			0, // SUPPLYDEPOT
			0, // TECHNOLOGYINSTITUTE
			0, // BARRACKS
			0, // HELIPAD
			0, // TRAININGFIELD
			0, // TANKFACTORY
			0, // MINERFACTORY
			0, // HANDWASHSTATION
			0, // AEROSPACELAB
			1, // BEAVER
			0, // COMPUTER
			1, // SOLDIER
			2, // BASHER
			1, // MINER
			2, // DRONE
			5, // TANK
			5, // COMMANDER
			8, // LAUNCHER
			0  // MISSILE
	};
	
	float[] supplyFactors = {
			1000, // HQ 
			1000, // TOWER
			1000, // SUPPLYDEPOT
			1000, // TECHNOLOGYINSTITUTE
			1000, // BARRACKS
			1000, // HELIPAD
			1000, // TRAININGFIELD
			1000, // TANKFACTORY
			1000, // MINERFACTORY
			1000, // HANDWASHSTATION
			1000, // AEROSPACELAB
			1, // BEAVER
			1, // COMPUTER
			1, // SOLDIER
			1, // BASHER
			1, // MINER
			1, // DRONE
			1, // TANK
			1, // COMMANDER
			1, // LAUNCHER
			1  // MISSILE
	};

	int[] unitRanges = {
		   3, // HQ 
		   2, // TOWER
		   4, // SUPPLYDEPOT
		   4, // TECHNOLOGYINSTITUTE
		   4, // BARRACKS
		   4, // HELIPAD
		   4, // TRAININGFIELD
		   4, // TANKFACTORY
		   4, // MINERFACTORY
		   4, // HANDWASHSTATION
		   4, // AEROSPACELAB
		   0, // BEAVER
		   4, // COMPUTER
		   0, // SOLDIER
		   0, // BASHER
		   0, // MINER
		   0, // DRONE
		   1, // TANK
		   1, // COMMANDER
		   4, // LAUNCHER
		   0  // MISSILE
	};
	
	int[] ragePriorities = {
		   0, // HQ 
		   0, // TOWER
		   5, // SUPPLYDEPOT
		   5, // TECHNOLOGYINSTITUTE
		   5, // BARRACKS
		   5, // HELIPAD
		   5, // TRAININGFIELD
		   5, // TANKFACTORY
		   5, // MINERFACTORY
		   5, // HANDWASHSTATION
		   5, // AEROSPACELAB
		   4, // BEAVER
		   2, // COMPUTER
		   2, // SOLDIER
		   2, // BASHER
		   4, // MINER
		   1, // DRONE
		   3, // TANK
		   6, // COMMANDER
		   3, // LAUNCHER
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
	
	static MapLocation myTarget = null;
	
	static MapLocation myLocation;
	static Direction facing;
	static GridComponent myGrid;
	static int lastGridInd=-1;

	// standard defines
	static MapLocation center;
	
	static MapLocation myHQ;
	static MapLocation enemyHQ;
	
	// only for hq
	static int hqLastTowerCount=0;
	
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

	
	static final int GRID_MAX_CC = 4;
	static final int GRID_CC_MASK = 7;
	
	// note for status flags: lowest 2-3 bits are CC, upper 16 bits are gridid base 
	// rest can be flags just fine and dandy
	
	// have we been there or seen it?
	static final int STATUS_SEEN = (1<<4);
	static final int STATUS_VISITED = (1<<5);
	// have we updated the connectivity recently?
	static final int STATUS_PATHED = (1<<6);
	// contains the enemy HQ
	static final int STATUS_HQ = (1<<7);
	// has a nearby tower's influence
	static final int STATUS_TOWER = (1<<8);
	
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
	
	
	// update frequencies
	static final int NUM_GRIDS = 8;
	
	static int GRID_DIFFUSE_FREQ = 20;
	static int GRID_CONNECTIVITY_FREQ = 8;
	static int GRID_UPDATE_FREQ = 6;
	static int GRID_ORE_FREQ = 6;
	static int GRID_MINING_FREQ = 6;
	static int GRID_RR_FREQ = 4;
	static int GRID_OD_FREQ = 4;
	
	
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
	
	// TARGET LIST GRIDS
	static final int gridLastRRChan = curChan++;
	static final int gridRRPtrChan = curChan++;
	
	static final int gridRageBase = curChan; static {curChan+=GRID_NUM;}
	static final int gridRallyBase = curChan; static {curChan+=GRID_NUM;}
	
	// OFFENSIVE/DEFENSIVE GRIDS
	static final int gridLastODChan = curChan++;
	static final int gridODPtrChan = curChan++;

	static final int gridOffenseBase = curChan; static {curChan+=GRID_NUM;}
	static final int gridDefenseBase = curChan; static {curChan+=GRID_NUM;}

	
	// THE CONNECTIVITY GRID LOOP
	static final int gridLastConnectivityChan = curChan++;
	static final int gridConnectivityPtrChan = curChan++;
	// these ones are indexed by location "properties"
	static final int gridNormalBase = curChan; static {curChan+=GRID_NUM;}
	static final int gridKnownBase = curChan; static {curChan+=GRID_NUM;}
	static final int gridVoidBase = curChan; static {curChan+=GRID_NUM;}
	static final int gridTowerBase = curChan; static {curChan+=GRID_NUM;}
	// and these ones are indexed by ID "values"
	static final int gridPathableBase = curChan; static {curChan+=GRID_NUM;}
	static final int gridEdgesNSBase = curChan; static {curChan+=GRID_NUM;}
	static final int gridEdgesEWBase = curChan; static {curChan+=GRID_NUM;}

	// TOWER GRID LOOP, JUST MODIFIES ^^ CONNECTIVITY INFO
	static final int gridTowerPtrChan = curChan++;
	static final int gridTowerDoneChan = curChan++;
	
	//===============================================================================================================
	
	
	// rage and defense targets
	static final int rageTargetChan = curChan++;
	static final int defenseTargetChan = curChan++;
	// and leaders
	static final int rallyLeaderChan = curChan++;
	static final int rageLeaderChan = curChan++;
	// um duhh
	static final int rallyStrengthChan = curChan++;
	static final int rageStrengthChan = curChan++;

	// which offensive building is set as the global target, this is set by HQ
	// (0 is HQ, rest are towers)
	static final int offenseBuildingChan = curChan++;

	
	static enum UnitState
	{
		CONVOY,
		HOLD, 
		ATTACK_MOVE, 
	}
	
	
	static enum UnitAggression
	{
		NEVER_MOVE_INTO_RANGE,
		NO_TOWERS,
		NO_RESTRICTIONS,
		CHARGE
	}

	static UnitState myState = UnitState.CONVOY;
	static UnitAggression myAggression = UnitAggression.NEVER_MOVE_INTO_RANGE;
	
	static enum BuildOrder
	{
		STANDARD,
		LARGE_MAP,
		SMALL_MAP
	}
	static BuildOrder myBuild = BuildOrder.STANDARD;
	static String toString(BuildOrder myBuild) {
		String ans = null;
		switch(myBuild)
		{
		case STANDARD:
			ans =  "STANDARD";
			break;
		case LARGE_MAP:
			ans = "LARGE_MAP";
			break;
		case SMALL_MAP:
			ans =  "SMALL_MAP";
			break;
		}
		return ans;
	}

	//===============================================================================================================
	
	// Scouts and Resupply
	static enum ScoutState
	{
		LOAD, //
		DELIVER,	// 
		HARASS,	//
		SCOUT, //
		LAUNCHER_BLITZ,	// 
	}

	static String toString(ScoutState state) {
		String ans = null;
		switch(state)
		{
		case LOAD:
			ans =  "LOAD";
			break;
		case DELIVER:
			ans = "DELIVER";
			break;
		case SCOUT:
			ans =  "SCOUT";
			break;
		case HARASS:
			ans =  "HARASS";
			break;
		case LAUNCHER_BLITZ:
			ans =  "LAUNCHER_BLITZ";
			break;
		}
		return ans;
	}
	static ScoutState scoutState;
	static double supplyFactor;
	static double supplyPackage = 2500;
	static double supplyMin = 100;
	static int supplyLoadingChan = curChan++;
	static int supplyUrgencyChan = curChan++;
	static int supplyRequestID = curChan++;
	
	// Drones
	static int t;
	static int launcherID;
	static int launcherIDchan = curChan++;
	
	//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
	
	// For the scouted attack routine: attackSmart()
	
	static final int RAGE_RADIUS = 100; // ragers will ignore things farther than 100 square distance away
	static final int MAX_SQUADS = 1; // TO DO: define these so there are MAX_SQUADS number of each of these broadcast channels
	static final int SCOUT_TIME = 10; // max rounds before scouting decision gets made
	static final int ATTACK_THRESHOLD = 45; // enemy power, sum of unitVal[] values, over which we retreat.  6 tanks out of about 38 squares would be 6*8=48
	static int scoutEnemyDetectionLocationX;
	static int scoutEnemyDetectionLocationY;
	// broadcast channels:
	static int scoutID = curChan++;
	static int scoutDecision = curChan++;
	static int scoutEnemyDetectionTime = curChan++;
	static int scoutedAttack = curChan++; // is my squad currently taking part in a scouted attack? 0 is no, 1 is yes
	// Local enemy targets
	static int[] defenseTargetChannels = {curChan++, curChan++, curChan++, curChan++, curChan++}; // channels to keep their robotIDs
	static int[] minerRageTargets = {curChan++, curChan++};
	static int rallyXChan = curChan++;
	static int rallyYChan = curChan++;
	static int engageDirChan = curChan++;

	// build order chans
	static int firstBeaverChan = curChan++;
	static int nextSpawnChan = curChan++;
	
	//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

	// enemy tower bookkeeping
	static final int towerCountChan = curChan++;
	static MapLocation[] enemyTowers;
	static int lastTowerCount=0;

	//===============================================================================================================
	
	// Keep track of best mine, score and location
	static int bestMineScoreChan = curChan++;
	static int bestMineXChan = curChan++;
	static int bestMineYChan = curChan++;
	
	// Keep track of number of each type of miner
	static int minersSupplying = curChan++;
	static int minersSearching = curChan++;
	static int minersLeading = curChan++;
	static int minerRageTarget = curChan++;
	static int minerRageTarget2 = curChan++;
	
	// Keep track of global best ore on map so far
	static int bestOrePatchAvg = curChan++;
	
	// Allow HQ to allocate mining duties
	static int minerShuffle = curChan++;
	
	static final int TARGET_SUPPLYING_MINERS = 10;
	
	// Keep track of miner IDs : using channel 499 and onward
	static int minerContiguousID = 499;
	static int myMinerID;
	static int minersCounter = 0;
	
	// Miner-specific
	static enum MinerState
	{
		LEADING,	// mining and following grid ore directions
		SEARCHING,	// executing a local ore finding algorithm, and generally following front line
		EXPLORING,	// fanning out generally
	}
	static String toString(MinerState state) {
		String ans = null;
		switch(state)
		{
		case EXPLORING:
			ans = "EXPLORING";
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
    static int numBeavers = 2;
    static int maxMiners = 40;
	static int numSoldiers = 10;
	static int numDrones = 20;
	static int numTanks = 999;

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
				
				mapHQ();
				// initialize pointers
				rc.broadcast(gridNextIDChan, 1);
				int ptr = gridMinX+gridMinY*GRID_DIM;
				rc.broadcast(gridConnectivityPtrChan, ptr);
				rc.broadcast(gridTowerPtrChan, ptr);
				
				rc.broadcast(gridOrePtrChan, ptr);
				rc.broadcast(gridUpdatePtrChan, ptr);
				
				rc.broadcast(gridMiningPtrChan, ptr);
				rc.broadcast(gridPotentialPtrChan, ptr);
				
				rc.broadcast(gridRRPtrChan, ptr);
				rc.broadcast(gridODPtrChan, ptr);
				
				
				// initialize to empty, so we fill it and do stuff
				enemyBuildings = new MapLocation[0];
				break;
			case BEAVER:
				facing = rc.getLocation().directionTo(myHQ).opposite();
				break;
			case MINER:
				minerState = MinerState.SEARCHING; // initially miners are in supply chain
				break;
			case DRONE:
				scoutState= ScoutState.LOAD; // initially drones are loading
				t = 0; // move timer for SCOUT state
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
				try
				{
					transferSupplies(supplyFactor);
				} catch (Exception e) {
					System.out.println("Supply exception: " + myType.toString() + ".  " + bytecodesLeft + " bytecodes before transferSupplies() call.");
					//e.printStackTrace();
				}
			}
			
			//==========================================================================================================================================
			
			try 
			{
				spare();
			}
			catch (Exception e)
			{
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
		// which grids still require computing?
		int validgrids = (1<<NUM_GRIDS)-1;
		
		while (Clock.getBytecodesLeft() > 1500 && Clock.getRoundNum() == curRound)
		{
			int ngrids = Integer.bitCount(validgrids);
			
			if (ngrids == 0)
			{
				System.out.println("All grids calculated");
				//rc.breakpoint();
				break;
			}
			//System.out.println("Grids left: " + ngrids + " | " + Integer.toBinaryString(validgrids));
			
			int gridindex = rand.nextInt(ngrids);
			int gridbits = validgrids;
			for (int i=0; i<gridindex; i++)
				gridbits &= gridbits-1;
			gridindex = Integer.numberOfTrailingZeros(gridbits);
			
			switch (gridindex)
			{
			case 0:
				if (!gridConnectivity())
					validgrids &= ~(1<<gridindex);
				break;
			case 1:
				//if (!gridDiffuse())
					validgrids &= ~(1<<gridindex);
				break;
			case 2:
				if (!gridUpdate())
					validgrids &= ~(1<<gridindex);
				break;
			case 3:
				if (!gridOre())
					validgrids &= ~(1<<gridindex);
				break;
			case 4:
				if (!gridMining())
					validgrids &= ~(1<<gridindex);
				break;
			case 5:
				if (!gridRageRally())
					validgrids &= ~(1<<gridindex);
				break;
			case 6:
				if (!gridTowers())
					validgrids &= ~(1<<gridindex);
				break;
			case 7:
				if (!gridOffenseDefense())
					validgrids &= ~(1<<gridindex);
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
		
		supplyFactor = Consts.supplyFactors[myType.ordinal()];
		
		// calculate center of map, as defined for everyone
		myHQ = rc.senseHQLocation();
		enemyHQ = rc.senseEnemyHQLocation();
		
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
		
		// and every once in a while, update enemy tower locations
		int ntowers = rc.readBroadcast(towerCountChan);
		if (ntowers != lastTowerCount)
			enemyTowers = rc.senseEnemyTowerLocations();
		
		strategyCheck();
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
		// add 1 to include the full coverage range of towers and HQ
		gridMinX = (mapMinX+GRID_OFFSET+GRID_SPC/2)/GRID_SPC - 1;
		gridMinY = (mapMinY+GRID_OFFSET+GRID_SPC/2)/GRID_SPC - 1;
		gridMaxX = (mapMaxX+GRID_OFFSET+GRID_SPC/2)/GRID_SPC + 1;
		gridMaxY = (mapMaxY+GRID_OFFSET+GRID_SPC/2)/GRID_SPC + 1;
		gridNum = (gridMaxX-gridMinX+1)*(gridMaxY-gridMinY+1);

		setExtents();
		
		// and set enemy HQ location
		new GridComponent(GridComponent.indexFromLocation(enemyHQ)).setFlag(STATUS_HQ);
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
			if (rc.isWeaponReady())
				attackSomething();
			
			// initialize first beaver flag
			rc.broadcast(firstBeaverChan,0);
			
			// initialize number of supply scouts loading
			rc.broadcast(supplyLoadingChan,0);
			supplyFactor = 1000; // HQ always giving away supply
			
			//initialize supply requests
			rc.broadcast(supplyLoadingChan,0);
			rc.broadcast(supplyUrgencyChan,0);
			// HQ RUNS OUT OF BYTECODES AND MESSES THIS UP SOMETIMES!
			
			// check last tower count
			MapLocation[] towers = rc.senseEnemyTowerLocations();
			if (towers.length != lastTowerCount)
			{
				lastTowerCount = towers.length;
				enemyTowers = towers;
				// flag towers for recomputation
				int towerstatus = rc.readBroadcast(gridTowerDoneChan);
				if (towerstatus <= 2)
					towerstatus = 0;
				rc.broadcast(gridTowerDoneChan, towerstatus);
				rc.broadcast(towerCountChan, towers.length);
			}
			
			// and if number of towers is > 1, set it to a tower that just cycles through
			if (lastTowerCount > 1)
				rc.broadcast(offenseBuildingChan,1+((Clock.getRoundNum()/400)%lastTowerCount));
			else
				rc.broadcast(offenseBuildingChan,0);
			
			// and check if we want to remove all towers from the map
			if (Clock.getRoundNum() > Consts.TOWER_REMOVE_ROUND)
			{
				int towerstatus = rc.readBroadcast(gridTowerDoneChan);
				if (towerstatus < 3)
					rc.broadcast(towerstatus, 3);
			}
			
			int rallyID = rc.readBroadcast(rallyLeaderChan);
			if (rc.canSenseRobot(rallyID))
			{
				RobotInfo[] unitsNearHold = rc.senseNearbyRobots(rc.senseRobot(rallyID).location,49,null);
				int holdStrengthBal = 0;
				for (RobotInfo b : unitsNearHold)
				{
					if (b.team == myTeam)
					{
						holdStrengthBal += Consts.unitVals[b.type.ordinal()]; // add units to strength bal
						//myMapSumX += unitVal[b.type.ordinal()]*b.location.x;
						//myMapSumY += unitVal[b.type.ordinal()]*b.location.y;
						//myUnitNum += 1;
					}
					else if (b.team == enemyTeam)
					{
	
						holdStrengthBal -= Consts.unitVals[b.type.ordinal()]; // subtract units to strength bal
						//enemyMapSumX += unitVal[b.type.ordinal()]*b.location.x;
						//enemyMapSumY += unitVal[b.type.ordinal()]*b.location.y;
						//enemyUnitNum += 1;
					}
				}
				rc.broadcast(rallyStrengthChan, holdStrengthBal);
			}
			else
			{
				rc.broadcast(rallyStrengthChan, 0);
			}
			
			RobotInfo[] ourTeam = rc.senseNearbyRobots(1000, rc.getTeam());
			int n = 0; // current number of beavers
			for(RobotInfo ri: ourTeam){ // count up beavers
				if(ri.type==RobotType.BEAVER){
					n++;
				}
			}
			if(n<numBeavers){ // in the beginning, spawn 'numBeavers' beavers and send them out in all directions
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
			/*
			static int numTanks = 999;
			int nextSpawn = rc.readBroadcast(nextSpawnChan);
		rc.setIndicatorString(0,"Next Spawn = " + RobotType.values()[nextSpawn]);
		
		if (nextSpawn == RobotType.DRONE.ordinal())
			spawnUnit(RobotType.DRONE);
		*/
			if(rand.nextInt(100) < 20)
			{
				RobotInfo[] units = rc.senseNearbyRobots(10000,myTeam);
				int d = 0;
				for(RobotInfo b: units)
				{
					if(b.type==RobotType.DRONE)
						d++;
				}
				if(d<numDrones)
					spawnUnit(RobotType.DRONE);
				
			}
		} catch (Exception e) {
			System.out.println("Helipad Exception");
			e.printStackTrace();
		}
	}
	
	static void doMinerFactory()
	{
		try {
			/*
			int nextSpawn = rc.readBroadcast(nextSpawnChan);
		rc.setIndicatorString(0,"Next Spawn = " + RobotType.values()[nextSpawn]);
		
		if (nextSpawn == RobotType.MINER.ordinal())
			spawnUnit(RobotType.MINER);
			*/
			
			/*
			// MINER Maximums
			int roundNum = Clock.getRoundNum();
			if (roundNum <250)
			minMiners = 5;
			else if (roundNum <1000)
			minMiners = 18;
			else 
			minMiners = 20; 
			  
			*/	
			// MINER Maximums
			int maxMiners = 0;
			int roundNum = Clock.getRoundNum();
			if (roundNum <250)
				maxMiners = 5;
			else if (roundNum <1000)
				maxMiners = 30;
			else 
				maxMiners = 60; 
			
			RobotInfo[] bots = rc.senseNearbyRobots(99999, myTeam);
			int nmine = 0;
			for (RobotInfo b: bots)
				if (b.type == RobotType.MINER)
					nmine++;
			
			if (nmine < maxMiners)
			{
				spawnUnit(RobotType.MINER);
			}
			/*
			if (curOre - lastOre < 25 && nmine < numMiners)
			{
				spawnUnit(RobotType.MINER);
			}
			*/
		} catch (Exception e) {
			System.out.println("Miner Factory Exception");
			e.printStackTrace();
		}
	}

	
	static void doBarracks()
	{
		try {
			
			/*
			int nextSpawn = rc.readBroadcast(nextSpawnChan);
			rc.setIndicatorString(0,"Next Spawn = " + RobotType.values()[nextSpawn]);
		
			if (nextSpawn == RobotType.SOLDIER.ordinal())
			spawnUnit(RobotType.SOLDIER);
		
			if (nextSpawn == RobotType.BASHER.ordinal())
			spawnUnit(RobotType.BASHER);
			*/
			
			RobotInfo[] bots = rc.senseNearbyRobots(99999, myTeam);
		
			int soldiers = 0;
			for (RobotInfo b: bots)
			{
				if (b.type == RobotType.SOLDIER)
				{
					soldiers++;
				}
			}
			
			if (soldiers < numSoldiers)
			{
				trySpawn(facing,RobotType.SOLDIER);
			}
		} catch (Exception e) {
			System.out.println("Barracks Exception");
			e.printStackTrace();
		}
	}

	
	static void doDrone()
	{
		int numLoading = 0;
		double loadingSupplyFactor = 0.01;
		double enRouteSupplyFactor = 0.001;
		double deliverSupplyFactor = 10;
		double harassSupplyFactor = 10; // this needs to be low bc sometimes new drones come out and push loading ones into harass state.
		try {
			switch (scoutState)
			{
			case LOAD:
				// stay out of enemy tower range
				myAggression = UnitAggression.NO_TOWERS;
				numLoading = rc.readBroadcast(supplyLoadingChan);
				rc.setIndicatorString(2, "LoadingNum = " + numLoading);
				if (numLoading > 0) // make sure only one drone loads at a time
				{
					scoutState = ScoutState.HARASS;
					supplyFactor = harassSupplyFactor;
				}
				else
				{
					numLoading += 1;
					rc.broadcast(supplyLoadingChan,numLoading);
				}
				// make my target my HQ to load up
				MapLocation scoutTarget = myHQ;
				if (myLocation.distanceSquaredTo(scoutTarget) > GameConstants.SUPPLY_TRANSFER_RADIUS_SQUARED)
				{
					Direction dir = myLocation.directionTo(scoutTarget);
					attackSomething(); // attack something if on the way
					tryMove(dir,0,myAggression); // go back to HQ to load up
				}
				// check my supply and change to DELIVER status when we have the full amount
				supplyFactor = loadingSupplyFactor;
				if (rc.getSupplyLevel() > supplyPackage)
					scoutState = ScoutState.DELIVER;
				break;

			case DELIVER:
				// move very cautiously with supply
				myAggression = UnitAggression.NEVER_MOVE_INTO_RANGE;
				// find target destination
				scoutTarget = myHQ; // if no target, defaults to own HQ
				int supplyDestID = rc.readBroadcast(supplyRequestID);
				if(rc.canSenseRobot(supplyDestID))
					scoutTarget = rc.senseRobot(supplyDestID).location;
				rc.setIndicatorString(2, "supply destination = (" + scoutTarget.x + ", " + scoutTarget.y + ")");
				// go to destination
				if (myLocation.distanceSquaredTo(scoutTarget) > GameConstants.SUPPLY_TRANSFER_RADIUS_SQUARED)
				{
					supplyFactor = enRouteSupplyFactor; 
					// do not attack, only move
					Direction dir = myLocation.directionTo(scoutTarget);
					tryMove(dir,0,myAggression); // go to supply drop location
				}
				else if (rc.getSupplyLevel() > supplyMin)
				{
					supplyFactor = deliverSupplyFactor;
				}
				else
				{
					supplyFactor = harassSupplyFactor;
					scoutState = ScoutState.HARASS;
				}
				break;

			case HARASS:
				boolean attacking = attackSomething();
				if(!attacking)
				{
					// avoid towers
					RobotInfo[] friends = rc.senseNearbyRobots(3, myTeam);
					if(friends.length>=1)
						myAggression = UnitAggression.NO_TOWERS;
					else
						myAggression = UnitAggression.NEVER_MOVE_INTO_RANGE;
					supplyFactor = harassSupplyFactor;
					// acquire target destination
					scoutTarget = enemyHQ; // for now

					// harass code!!!!!!!!!!!!!!!!!!
					if(rc.isCoreReady()){
						MapLocation here = myLocation;

						// check if we should be harassing launchers
						launcherID = rc.readBroadcast(launcherIDchan);
						if(launcherID!=0 && rc.canSenseRobot(launcherID))
						{
							scoutState = ScoutState.LAUNCHER_BLITZ;
							facing = here.directionTo(rc.senseRobot(launcherID).location);
							tryMove(facing, 0, myAggression);
						}
						else
							rc.broadcast(launcherIDchan, 0); // that launcher is dead, nullify it

						// local variables
						boolean threatened = false;
						boolean launcher = false;
						int dx = 0; // direction of pull toward targets
						int dy = 0; // "
						int evadeX = 0; // direction of best escape from enemy range
						int evadeY = 0; // "

						// find out who and what is around
						MapLocation enemyHQ = rc.senseEnemyHQLocation();
						RobotInfo enemies[] = rc.senseNearbyRobots(here,24,enemyTeam);
						int scale=0;
						for(RobotInfo ri: enemies){
							// go toward targets, take note of powerful enemies
							int dist = here.distanceSquaredTo(ri.location);
							if(dist>myType.attackRadiusSquared)
								scale = dist-myType.attackRadiusSquared;
							else if(ri.type.attackRadiusSquared>dist)
								scale = ri.type.attackRadiusSquared-dist;
							int x = (ri.location.x - here.x)*scale;
							int y = (ri.location.y - here.y)*scale;
							if(ri.type==RobotType.TANK)// || ri.type==RobotType.COMMANDER)
							{
								threatened = true;
							}
							else if(ri.type==RobotType.LAUNCHER || ri.type==RobotType.COMMANDER) // for now, blitz the commander too, can change this
							{
								launcher = true;
								launcherID = ri.ID;
							}
							else
							{
								dx += 2 * Consts.droneUnitAtt[ri.type.ordinal()] * x;
								dy += 2 * Consts.droneUnitAtt[ri.type.ordinal()] * y;
							}
							evadeX -= Consts.unitVals[ri.type.ordinal()] * x;
							evadeY -= Consts.unitVals[ri.type.ordinal()] * y;
						}
						rc.setIndicatorString(2, "scale = " + scale + ", (dx, dy) = (" + (int)dx + ", " + (int)dy + "), and (evadeX, evadeY) = (" + (int)evadeX + ", " + (int)evadeY + ")");
						rc.setIndicatorString(1, "drone harass, launcher = " + launcher);
						if(launcher) // go for a launcher if possible
						{
							scoutState = ScoutState.LAUNCHER_BLITZ;
							rc.broadcast(launcherIDchan,launcherID);
							facing = here.directionTo(rc.senseRobot(launcherID).location);
							tryMove(facing, 0, myAggression);
						}
						// make sure we're not taking too much damage
						if( rc.getHealth()<=6 ){ // arbitrary danger level
							// run away and scout the map, since i'm basically as good as dead!
							scoutState = ScoutState.SCOUT;
						}
						// if facing a big threat or if health is low, get away
						else if(threatened)
						{
							rc.setIndicatorString(0, "Evade!" + "(" + evadeX + ", " + evadeY + ")");
							facing = here.directionTo(here.add(evadeX,evadeY));
							tryMove(facing, 0, myAggression);
						}
						// if not, follow pulls and pushes of enemies
						else if(!(dx==0 && dy==0)){
							rc.setIndicatorString(0, "Attack!" + "(" + dx + ", " + dy + ")");
							facing = here.directionTo(here.add( dx + evadeX, dy + evadeY ));
							tryMove(facing, 0, myAggression);
						}
						// if no one to attack, move toward enemy HQ, generally, but look around too
						else
						{
							t += 1;
							if(t%5 == 0) // go straight 10ish rounds, then turn
							{
								if(rand.nextDouble()<0.9)
								{
									if(rand.nextDouble()<0.1)
										facing = getRandomDirection();
									else
										facing = here.directionTo(enemyHQ); // 90% of the time
								}
							}
							tryMove(facing, 0, myAggression);
						}
					}
				}
				break;

			case SCOUT: // you're basically dead, avoid everything and explore map
				// avoid towers
				myAggression = UnitAggression.NEVER_MOVE_INTO_RANGE;
				supplyFactor = 0.001; // keep supply
				// explore using a Levy flight
				if(rc.isCoreReady())
				{
					t += 1; // add to counter
					if(t%5 == 0) // every fifth move... sort of
					{
						if(rand.nextDouble()<0.8)
						{
							facing = getRandomDirection(); // make a random turn
						}
					}
					tryMove(facing, 0, myAggression); // try to continue straight
				}
				break;

			case LAUNCHER_BLITZ:
				// rush launchers
				myAggression = UnitAggression.NO_TOWERS;
				supplyFactor = 1;
				// check if we should be harassing launchers
				if(!rc.canSenseRobot(launcherID)) // check my local launcher ID first, if i saw one
					launcherID = rc.readBroadcast(launcherIDchan); // if not, get global
				if(launcherID!=0 && rc.canSenseRobot(launcherID))
				{
					facing = myLocation.directionTo(rc.senseRobot(launcherID).location);
					tryMove(facing, 0, myAggression);
				}
				else
				{
					rc.broadcast(launcherIDchan, 0); // that launcher is dead, nullify it
					scoutState = ScoutState.HARASS; // go back to general harass mode
				}
				attackSomething();
				break;

			}
			rc.setIndicatorString(0, "Scout State = " + toString(scoutState) + ", Supply Factor = " + supplyFactor);		
		} catch (Exception e) {
			System.out.println("Drone Exception");
			e.printStackTrace();
		}
	}

	static void doBasher()
	{
		try {
			//updateSquadInfo();
			if (Clock.getRoundNum()<1800)
			{
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
			supplyFactor = 0.5;

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
			/*
			int nextSpawn = rc.readBroadcast(nextSpawnChan);
			rc.setIndicatorString(0,"Next Spawn = " + RobotType.values()[nextSpawn]);
			
			if (nextSpawn == RobotType.TANK.ordinal())
				spawnUnit(RobotType.TANK);
			*/
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
			// always attack if you can
			attackSomething();
			doRageRally();
			
			supplyFactor = requestSupplies();
			
		} catch (Exception e) {
			System.out.println("Tank Exception");
			e.printStackTrace();
		}
	}
	
	static void doSoldier()
	{
		try 
		{
			attackSomething();
			doRageRally();

			supplyFactor = requestSupplies();
			
		} catch (Exception e) {
			System.out.println("Soldier Exception");
			e.printStackTrace();
		}
	}
	
	static void doBeaver()
	{
		try {
			defenseUpdate();
			myAggression = UnitAggression.NEVER_MOVE_INTO_RANGE;
			attackSomething();
			defenseUpdate();

			
			// move off of directly diag squares to HQ
			int distSq = myLocation.distanceSquaredTo(myHQ);
	
			if (distSq == 2)
			{
				Direction moveDir = myHQ.directionTo(myLocation).rotateRight();	
					tryMove(moveDir,0,myAggression);
			}

			// read build order instructions
			int toBuild = rc.readBroadcast(firstBeaverChan);
			//rc.setIndicatorString(1, "toBuild = " + toBuild);
			
			if (toBuild == 0) // 1st beaver to move thinks, deciding builds
				decideNextBuilds(); // ALL BUILDS ENTERED IN THIS FUNCTION
			
			
			
			else if (toBuild > 0 && toBuild !=1)
			{
				boolean spaceToBuild = buildUnitParity(RobotType.values()[toBuild]);
				if (rand.nextDouble()< 0.3 && spaceToBuild && rc.getTeamOre()>RobotType.values()[toBuild].oreCost && rc.isCoreReady())
					rc.broadcast(firstBeaverChan,-toBuild); // flag it as done
				else if (!spaceToBuild) // if no space to build, try going farther away.
					tryMove(myHQ.directionTo(myLocation),0,myAggression);
			}
			else
			{ // otherwise, try to get close to HQ
				tryMove(myLocation.directionTo(myHQ),0,myAggression);
			}

			// mine in place
			if(rc.isCoreReady()&&rc.canMine()){
				rc.mine();
			}

			supplyFactor = 1;
			/*
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
				buildUnitParity(RobotType.MINERFACTORY);
				if(rc.isCoreReady()&&rc.canMine()){
					rc.mine();
				}
			}

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
		
			else
			{
				if(n < numMinerFactories && (n == o)) 
				{
					buildUnitParity(RobotType.MINERFACTORY);
				} 
				else if(s<numSupplyDepots && mi>1)// && m>1)
				{
					buildUnitParity(RobotType.SUPPLYDEPOT);
				}
				else if(o<numHelipads)
				{
					buildUnitParity(RobotType.HELIPAD);
				}
				else if(m<numBarracks)
				{
					buildUnitParity(RobotType.BARRACKS);
				}
				else if(tf<numTankFactories  && m>0)
				{
					buildUnitParity(RobotType.TANKFACTORY);
				}
				attackSomething();
				int oreMiningCriterion = rc.readBroadcast(bestOrePatchAvg);
				double[] localOre = mineScore();
				mineLocally(localOre, oreMiningCriterion);
			}
			
			supplyFactor = 0.5;
			*/
		} catch (Exception e) {
			System.out.println("Beaver Exception");
			e.printStackTrace();
		}
	}

	
	private static void decideNextBuilds() throws GameActionException {
		double[] numUnits = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		int toBuild = 0;
		// count up all units
		RobotInfo[] ourTeam = rc.senseNearbyRobots(100000, rc.getTeam());
		for(RobotInfo ri: ourTeam)
		{
			numUnits[ri.type.ordinal()] += 1;
			if (ri.builder == null)
				numUnits[ri.type.ordinal()] += .001; // this tells us a building is done!
		}
					
		rc.setIndicatorString(1, "numUnits = " + Arrays.toString(numUnits));

		int[] buildOrder = {}; // initialize build order
		double[] spawnPriorities = {}; // initialize spawn priorities
		
		switch (myBuild)
		{
		case STANDARD:		
			// this list defines the build order. make sure you don't mess up building prereqs.
			// terminate preset build order with a 1;
			int[] stdBuildOrder = {8, 4, 5, 7, 7, 2, 7, 1};
			buildOrder = stdBuildOrder;
		
			break;
		case LARGE_MAP:
			// this list defines the build order. make sure you don't mess up building prereqs.
			int[] lgBuildOrder = {8, 4, 5, 7, 8, 7, 2, 7, 2, 7, 2, 2, 7, 7, 7, 7, 7, 1};
			buildOrder = lgBuildOrder;	
			break;	
			
		case SMALL_MAP:
			// this list defines the build order. make sure you don't mess up building prereqs.
			int[] smBuildOrder = {8, 5, 4, 7, 8, 7, 2, 7, 2, 7, 2, 2, 7, 7, 7, 7, 7, 1};
			buildOrder = smBuildOrder;
			break;

		}
		
		// execute build order
		boolean buildComplete = false;
		boolean spaceToBuild = true; // initialize ok to build
		int[] buildTotals = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};

		// check to see where we are in the build. will fill in holes if things were destroyed
		int currentBuild = 0;
		for (int i=0; i<buildOrder.length; i++)
		{
			buildTotals[buildOrder[i]] += 1;
			if (buildOrder[i] == 1)
			{
				rc.setIndicatorString(0, "Current Build Index = COMPLETE");
				buildComplete = true;
				toBuild = 1;
				break;
			}
			if (buildTotals[buildOrder[i]]>numUnits[buildOrder[i]])
			{
				currentBuild = i;
				toBuild = buildOrder[i];
				rc.broadcast(firstBeaverChan,toBuild); // broadcast next build
				rc.setIndicatorString(0, "Current Build Index = " + currentBuild);
				break;
			}
		}	
		
		// if build complete, build SD's.  If too much ore, build TF's!
		if (buildComplete)
		{
			toBuild = 2;
			rc.broadcast(firstBeaverChan,toBuild);			
			if (rc.getTeamOre() > RobotType.TANKFACTORY.oreCost+1)
			{
				toBuild = 7;
				rc.broadcast(firstBeaverChan,toBuild);
			}	
		}
		
		rc.setIndicatorString(2, "buildTotals = " + Arrays.toString(buildTotals));


		// build next building, give other beavers a chance to do it
		if (rand.nextDouble()< 0.3 && !buildComplete && toBuild !=1)
		{
			spaceToBuild = buildUnitParity(RobotType.values()[buildOrder[currentBuild]]);
			rc.broadcast(firstBeaverChan,-toBuild); // negative means done
		}
		
		if (!spaceToBuild) // if trouble building, probably because no space, move away from HQ
		{
			tryMove(myHQ.directionTo(myLocation),0,myAggression);
			rc.broadcast(firstBeaverChan,toBuild); // positive means didn't build
		}
		
	}

	//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%  All you do is call attackWithScout() instead of attackSomething()
	
	static boolean attackWithScout() throws GameActionException
	{
		// a scouted attack, where a scout (first bot to see enemy) rushes ahead and makes final attack decision
		
		// check whether we are taking part in a scouted attack
		int areWeInScoutedAttack = rc.readBroadcast(scoutedAttack);
		if(areWeInScoutedAttack == 1)
		{
			rc.setIndicatorString(0, "I am in a scouted attack!");
			// am i the scout, or a follower?
			int scoutRobotID = rc.readBroadcast(scoutID);
			if(rc.getID()==scoutRobotID) // i am the scout
			{
				rc.setIndicatorString(2,"Scout attack routine: I am the SCOUT");
				rc.setIndicatorDot(myLocation.add(0,-1), 200, 200, 200);
				
				// count enemies and firepower, and note their locations
				MapLocation here = rc.getLocation();
				RobotInfo[] enemies = rc.senseNearbyRobots(here, myType.sensorRadiusSquared, enemyTeam);
				int enemyPower = 0;
				int enemyX = 0;
				int enemyY = 0;
				for(RobotInfo en: enemies)
				{
					enemyPower += unitVal[en.type.ordinal()];
					enemyX += (en.location.x - here.x);
					enemyY += (en.location.y - here.y);
				}
				rc.setIndicatorString(1,"Sighted enemy power = " + enemyPower);
				
				// check if we're still really the scout, or have lost sight of everyone
				if(enemyPower == 0)
				{
					rc.broadcast(scoutedAttack, 0); // no more attack
					rc.broadcast(scoutID, 0); // relinquish post as a scout, while still alive, a rare feat
					rc.setIndicatorString(0, " ");
					rc.setIndicatorString(1, " ");
					rc.setIndicatorString(2, " ");
					return false; // give it up man, it's over
				}

				// run toward enemy
				tryMove(here.directionTo(here.add(enemyX,enemyY)),0,myAggression);

				// make a tactical attack decision
				if(enemyPower > ATTACK_THRESHOLD)
				{
					rc.broadcast(scoutDecision, 0); // sound the retreat.  otherwise, the default is set to attack.
					// tell the troops where to form their defensive line: where the scout was when he first saw the enemy
					rc.broadcast(rallyXChan, scoutEnemyDetectionLocationX);
					rc.broadcast(rallyYChan, scoutEnemyDetectionLocationY);
					myTarget = new MapLocation(scoutEnemyDetectionLocationX,scoutEnemyDetectionLocationY); // for now, since the rallyChan thing isn't working in this code
					rc.setIndicatorString(1,"Sighted enemy power = " + enemyPower + ", I am sounding the retreat!");
				}
				
				// continue shooting
				attackSomething();
			}
			else // i am not the scout
			{
				rc.setIndicatorString(2,"Scout attack routine: I am a FOLLOWER of " + scoutRobotID);
				
				// check to see if the scout sounded the retreat
				int attack = rc.readBroadcast(scoutDecision);
				if(attack == 0) // we are retreating
				{
					// NOTE: this code is an old movePotential() that doesn't do this yet, it just uses myTarget
					int x = rc.readBroadcast(rallyXChan);
					int y = rc.readBroadcast(rallyYChan);
					myTarget = new MapLocation(x,y); // for now, since the rallyChan thing isn't working in this code
					movePotential(); // forms the defensive line at (rallyXChan, rallyYChan), set by the scout to (scoutEnemyDetectionLocationX, scoutEnemyDetectionLocationY)
					
					rc.setIndicatorString(1,"Retreating and forming a defensive line, because the scout said to.");
					rc.setIndicatorDot(myLocation.add(0,-1), 200, 200, 0);
				}
				else
				{
					rc.setIndicatorDot(myLocation.add(0,-1), 0,0,0);
					// check to see if the scout is dead
					try{
						rc.senseRobot(scoutRobotID);
						// if we get here in the code, he's still alive
						// check on the time from the scout's alarm
						int alarmTime = rc.readBroadcast(scoutEnemyDetectionTime);
						if(alarmTime + SCOUT_TIME < Clock.getRoundNum()) //
						{
							// the scout didn't sound off the retreat in the allotted time, so attack
							rc.setIndicatorString(1,"Rage attacking, because I didn't hear from the scout in time.");
							rageAttack();
						}
						else
						{
							// scout is alive, but hasn't said to retreat, and time's not up, so wait
							rc.setIndicatorString(1,"Waiting, because I didn't hear from the scout, and time is not up.");
						}
					}catch (Exception e){
						// scout is dead, so attack
						rc.setIndicatorString(1,"Rage attacking, because the scout died wihtout calling for a retreat.");
						rc.setIndicatorString(2,"Scout attack routine: I am a FOLLOWER but my scout is dead.");
						rageAttack();
					}
				}
			}
			return true;
		}
		else // not yet in a scouted attack
		{
			//System.out.println("I got to the part of the code that says we don't have a scout.");
			rc.setIndicatorString(0, "I am NOT in a scouted attack, I'm looking around.");
			rc.setIndicatorString(1, " ");
			rc.setIndicatorString(2, " ");
			// do i see someone?  if so, i'm the scout
			RobotInfo [] enemies = rc.senseNearbyRobots(myType.sensorRadiusSquared, enemyTeam);
			if(enemies.length > 0)
			{
				int enemyID = enemies[0].ID; // pick the first one
				scoutAlert(enemyID); // alert everyone and claim position of being the scout
				return true;
			}
			else{
				return false;
			}
		}
	}
	
	
	static void scoutAlert(int enemyID) throws GameActionException{
		// the scout, being the first bot to see the enemy, executes these commands to start a scouted attack, laying groundwork for attackWithScout()
		rc.broadcast(scoutID, rc.getID()); // broadcast my ID as being that of the scout
		rc.broadcast(scoutedAttack, 1); // alerts the squad that we are now taking part in a scouted attack
		rc.broadcast(scoutDecision, 1); // initialize attack decision to the default: 1 = yes, attack.  0 = no, retreat.
		scoutEnemyDetectionLocationX =  rc.getLocation().x;
		scoutEnemyDetectionLocationY =  rc.getLocation().y;
		rc.broadcast(scoutEnemyDetectionTime, Clock.getRoundNum());
		rc.broadcast(defenseTargetChannels[0], enemyID); // put this enemy as first priority on rage hit list
		return;
	}


	static void rageAttack() throws GameActionException
	{
		boolean attacking = attackSomething();
		if(!attacking) // chase a target on the list and go for it
		{
			MapLocation targetLoc = chooseEnemyTarget(defenseTargetChannels, 25);
			if(targetLoc!=null)
			{
				tryMove(myLocation.directionTo(targetLoc),0,myAggression);
			}
			else // we're done, reset this thing
			{
				rc.broadcast(scoutedAttack, 0); // alerts the squad that we are done
				rc.setIndicatorString(0, " ");
				rc.setIndicatorString(1, " ");
				rc.setIndicatorString(2, " ");
			}	
		}
		return;
	}
	
	static MapLocation chooseEnemyTarget(int[] channels, int limit) throws GameActionException
	{
		MapLocation targetLoc = null;
		// choose a target from the hit list
		for(int i: channels)
		{
			int targetID = rc.readBroadcast(i);
			if(targetID!=0)
			{
				if(rc.canSenseRobot(targetID))
				{
					MapLocation thisTarget = rc.senseRobot(targetID).location;
					int dist = myLocation.distanceSquaredTo(thisTarget);
					if(dist<limit)
					{
						targetLoc = thisTarget;
						return targetLoc;
					}
				}
			}
		}
		return targetLoc;
	}
	
	static void addEnemyToTargets(int[] channels, int ID) throws GameActionException {
		// adds an enemy ID to a list of targets, if that list is not full
		for(int i: channels)
		{
			int enemyID = rc.readBroadcast(i);
			if(!rc.canSenseRobot(enemyID))
			{
				rc.broadcast(i,ID);
				return;
			}
		}
	}

	
	//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
	
//===========================================================================================
	
	static void doMiner()
	{
		try {
			rc.setIndicatorString(2, toString(minerState));
			rc.setIndicatorString(2, "gridOreBase = " + myGrid.readValue(gridOreBase) + ", gridMiningBase = "+ myGrid.readValue(gridMiningBase) + " ");
			boolean engagedDefinsively = false;
			engagedDefinsively = minerDefensiveManeuvers();
			if(engagedDefinsively)
			{
				attackSomething();
			}
			else if (rc.isCoreReady())
			{
				rc.setIndicatorString(0,"Mining");
				miningDuties();
			}
			defenseUpdate();
			
		} catch (Exception e) {
			System.out.println("Miner Exception");
			e.printStackTrace();
		}
	}

	// does various sanity checks as a function of in-game status
	static void strategyCheck() throws GameActionException
	{
		// prioritize filling in offense-defense grids early on
		if (Clock.getRoundNum() < 300)
			GRID_OD_FREQ = 2;
		else
			GRID_OD_FREQ = 5;
		
		// check if we're relatively far along and can't path to the enemy HQ
		/*if (Clock.getRoundNum() > 600)
		{
			// check the enemy HQ's grid values
			GridComponent engrid = new GridComponent(enemyHQ);
			engrid.findComponent(enemyHQ);
			// and remove towers if the "offense value" is less than 1000
			if (engrid.readValue(gridDefenseBase) <= Consts.GRID_BASE)
			{
				// oh shit oh shit man, this removes towers as "no-fly zones" from grid
				rc.broadcast(gridTowerDoneChan, 3);
			}
		}*/
	}

	static void miningDuties() throws GameActionException
	{
		double oreHere = rc.senseOre(myLocation);
		int oreMiningCriterion = rc.readBroadcast(bestOrePatchAvg);
		//System.out.println("Ore mining criterion = " + oreMiningCriterion);
		double[] localOre = mineScore();
		double mineScore = localOre[2];
		// depending on what state the miner is in, execute functionality
		switch (minerState)
		{
		case LEADING:
			rc.setIndicatorDot(myLocation.add(1,0), 0,255,0); // green dots on front line miners
			if(mineScore < 3*oreMiningCriterion && oreHere < 10) // less ore and can't max out
				minerState = MinerState.SEARCHING; // switch to searching, this will be reported to HQ next round
			else // we've been sitting still and mining
				mineAndMoveStraight(oreMiningCriterion);
			break;
		case SEARCHING:
			rc.setIndicatorDot(myLocation.add(1,0), 255,255,0); // yellow dots on searchers
			mineLocally(localOre, oreMiningCriterion);
			if(mineScore > 4*oreMiningCriterion && oreHere > oreMiningCriterion/2)
				minerState = MinerState.LEADING; // switch to a leader, this will be reported to HQ next round
			supplyFactor = 1;
			break;
		case EXPLORING:
			rc.setIndicatorDot(myLocation.add(1,0), 255,255,255); // white dots on explorers
			if(rc.senseOre(myLocation)>=oreMiningCriterion/2 || rc.senseOre(myLocation)>=10)
			{
				if(rc.canMine())
					rc.mine();
				minerState = MinerState.SEARCHING;
			}
			else if(mineScore>2*oreMiningCriterion)
			{
				minerState = MinerState.SEARCHING;
				mineLocally(localOre, oreMiningCriterion);
			}
			else
			{
				rc.setIndicatorString(0, "Exploring");
				rc.setIndicatorString(1, "Following the grid: " + minersCounter);
				int bitdir = gridPathfind(myLocation, gridMiningBase, true);
				tryMove(Direction.OMNI, bitdir, UnitAggression.NO_TOWERS);
			}
			supplyFactor = 9;
			break;
		}
		return;
	}

	static double[] mineScore() throws GameActionException {
		int[] x = {0,-1, 0, 0, 1,-1,-1, 1, 1,-2, 0, 0, 2,-2,-2, 2, 2};
		int[] y = {0, 0,-1, 1, 0,-1, 1,-1, 1, 0,-2, 2, 0,-2, 2,-2, 2};
		double[] localOre = vectorSumOre(x,y);
		return localOre;
	}
	
	static void mineAndMoveStraight(int oreMiningCriterion) throws GameActionException {
		double oreHere = rc.senseOre(myLocation);
		if(oreHere>=10 || oreHere>=oreMiningCriterion/2){ //there is plenty of ore, so try to mine
			if(rand.nextDouble()<0.95){ // mine 95% of time we can collect max
				if(rc.isCoreReady()&&rc.canMine()){
					rc.mine();
					rc.setIndicatorString(1, "Mining");
				}
			}else{
				tryMove(facing, 0, UnitAggression.NO_TOWERS);
				rc.setIndicatorString(1, "Moving straight");
			}
		}else{
			minerState = MinerState.SEARCHING;
		}
	}
	
	static void mineLocally(double[] localOre, int oreMiningCriterion) throws GameActionException {
		double oreHere = rc.senseOre(myLocation);
		if(oreHere>=10 || oreHere>=oreMiningCriterion/2){ //there is plenty of ore, so try to mine
			if(rand.nextDouble()<0.958){ // mine 98% of time we can collect max
				if(rc.isCoreReady()&&rc.canMine()){
					rc.mine();
					rc.setIndicatorString(1, "Mining good stuff");
				}
			}else{
				facing = minerLocalOreDirection(localOre, oreMiningCriterion);
				tryMove(facing, 0, UnitAggression.NO_TOWERS);
			}
			
		}else if(oreHere>0.8){ //there is a bit of ore, so maybe try to mine, maybe move on (suppliers don't mine)
			if(rand.nextDouble()<0.7){ // mine
				if(rc.isCoreReady()&&rc.canMine()){
					rc.mine();
					rc.setIndicatorString(1, "Mining suboptimal area");
				}
			}else{ // look for more ore
				facing = minerLocalOreDirection(localOre, oreMiningCriterion);
				tryMove(facing, 0, UnitAggression.NO_TOWERS);
			}
		}else if(localOre[2]>2*oreMiningCriterion){ // still look locally
			facing = minerLocalOreDirection(localOre, oreMiningCriterion);
			tryMove(facing, 0, UnitAggression.NO_TOWERS);
		}else{ //no ore, so look for more
			minersCounter = 0;
			minerState = MinerState.EXPLORING;
			int bitdir = gridPathfind(myLocation, gridMiningBase, true);
			tryMove(Direction.OMNI, bitdir, UnitAggression.NO_TOWERS);
			//facing = Direction.values()[Integer.numberOfTrailingZeros(bitdir)];
			//tryMove(facing);
			rc.setIndicatorString(1, "Following grid without counting moves, since the ore is so bad: " + minersCounter);
			//rc.setIndicatorDot(myLocation.add(0,1), 0, 0, 255);
		}
	}
	
	
	// Miner's potential field calculation.  Yields an integer representing the movement direction 0-7.  A null value means not to move.
	static Direction minerLocalOreDirection(double[] localOre, int oreMiningCriterion) throws GameActionException {
		
		Direction bestDirection = null;
		
		// for the final direction
		double mineScore = localOre[2];
		minersCounter += 1; // avoid flip-flopping between local mining and grid following.  make up your fucking mind.
		int counterMax = 5;
			// local ore potential
			double totalPotential[] = {0,0};
			totalPotential[0] += localOre[0];
			totalPotential[1] += localOre[1];
			
			// repel from local miners
			RobotInfo[] miners = rc.senseNearbyRobots(myLocation,1,myTeam);
			for(RobotInfo bot : miners)
			{
				int dx = (bot.location.x - myLocation.x);
				int dy = (bot.location.y - myLocation.y);
				int factor = -10;
				totalPotential[0] += factor*dx;
				totalPotential[1] += factor*dy;
			}
			
			bestDirection = myLocation.directionTo( myLocation.add((int)totalPotential[0],(int)totalPotential[1]) ); // direction to move
			rc.setIndicatorString(1, "Looking for local best ore, best direction = " + bestDirection.toString());
			//System.out.println("px = " + totalPotential[0] + ", py = " + totalPotential[1]);
			// don't go back to your last spot ever
			if(bestDirection==facing.opposite() || bestDirection==Direction.OMNI)
				bestDirection = getRandomDirection();
		return bestDirection;
	}

	static double[] vectorSumOre(int[] x, int[] y) throws GameActionException {
		MapLocation sensingRegion[] = new MapLocation[x.length];
		MapLocation here = rc.getLocation();
		for(int a=0; a<x.length; a++){
			MapLocation loc = myLocation.add(x[a],y[a]);
			if(isSafeDirection(loc.add(0,1),loc.add(0,1).directionTo(loc)))
				sensingRegion[a] = loc;
			else
				sensingRegion[a] = null; // neglects spots we can't go
		}
		double ore = 0;
		int i=0;
		double potentialX = 0;
		double potentialY = 0;
		double mineScore = 0;
		
		// go through and look at ore
		for(MapLocation m: sensingRegion){
			if(m!=null) // put in to neglect spots we can't go
			{
				ore = rc.senseOre(m);
				TerrainTile tile = rc.senseTerrainTile(m);
				RobotInfo bot = rc.senseRobotAtLocation(m);
				mineScore += ore;
				double d2 = Math.max(1, here.distanceSquaredTo(m));
				if(tile.isTraversable() && bot==null){
					potentialX += ore*(double)x[i]/d2;
					potentialY += ore*(double)y[i]/d2;
				}
			}
			i++;
		}
		double potential[] = {potentialX, potentialY, mineScore};
		return potential;
	}
	
	static boolean minerDefensiveManeuvers() throws GameActionException
	{
		// rage if there is anything nearby to rage at
		MapLocation here = rc.getLocation();
		MapLocation target = chooseEnemyTarget(minerRageTargets, 15); // (list, max distance)
		if(target==null)
		{
			// if we didn't acquire a target, check our surroundings
			RobotInfo[] enemyRobots = rc.senseNearbyRobots(myType.sensorRadiusSquared, myTeam.opponent());
			int strengthBal = 0;
			int evadeX = 0;
			int evadeY = 0;

			if (enemyRobots.length != 0)
			{
				RobotInfo[] friendlyRobots = rc.senseNearbyRobots(myType.sensorRadiusSquared, myTeam);
				// attract enemies, and count up enemy firepower
				for (RobotInfo bot : enemyRobots) {
					int d2 = bot.location.distanceSquaredTo(myLocation);
					int scale = 1;
					if(d2>myType.attackRadiusSquared)
						scale = d2-myType.attackRadiusSquared;
					else if(bot.type.attackRadiusSquared>d2)
						scale = bot.type.attackRadiusSquared-d2;
					double vecx = (bot.location.x - myLocation.x) * scale;
					double vecy = (bot.location.y - myLocation.y) * scale;

					// repulsion from enemy location, proportional to attack power
					// subtracts a positive # if we're in range
					evadeX -= bot.type.attackPower * vecx;
					evadeY -= bot.type.attackPower * vecy;

					// enemy firepower
					strengthBal -= Consts.minerUnitVals[bot.type.ordinal()] * bot.type.attackPower;

				}

				// attract friends, and count up friendly firepower
				for (RobotInfo bot: friendlyRobots)
				{
					// doesn't apply to towers and HQ
					if (bot.type == RobotType.TOWER || bot.type == RobotType.HQ)
						continue;

					// our firepower
					strengthBal += Consts.unitVals[bot.type.ordinal()] * bot.type.attackPower;
				}
				if(strengthBal<=0)
				{
					Direction dir = here.directionTo(here.add((int)evadeX,(int)evadeY));
					tryMove(dir, 0, UnitAggression.NO_TOWERS);
					rc.setIndicatorString(0,"Evading: (" + evadeX + ", " + evadeY + ")");
					rc.setIndicatorString(1,"Strength balance = " + strengthBal);
				}
				else
				{
					// set a rage attack target
					rc.setIndicatorString(0,"Attacking enemy " + enemyRobots[0].ID);
					rc.setIndicatorString(1,"Strength balance = " + strengthBal);
					addEnemyToTargets(minerRageTargets,enemyRobots[0].ID);
					tryMove(here.directionTo(enemyRobots[0].location), 0, UnitAggression.NO_TOWERS);
				}
				return true; // evading, but enemies in sight
			}
			else // no enemies in sight
				return false;
		}
		else
		{
			tryMove(here.directionTo(target), 0, UnitAggression.NO_TOWERS);
			rc.setIndicatorString(0,"Attacking enemy at (" + target.x + ", " + target.y + "), from list.");
			return true; // raging at someone on list
		}
	}

	//=============================================================================================

	// sets the state machine for what we're doing right now
	static void doRageRally() throws GameActionException
	{
		RobotInfo[] enemyRobots = rc.senseNearbyRobots(myType.sensorRadiusSquared, enemyTeam);
		
		// get the ID of the robot we're rallying around
		// (this only matters for hold/convoy)
	
		int rallyID = rc.readBroadcast(rallyLeaderChan);
	
		switch (myState)
		{
		case CONVOY:
			// switch to HOLD if we made it to the squad leader
	
			// do we become the conservative squad leader?
			if (enemyRobots.length != 0 || !rc.canSenseRobot(rallyID))
			{
				// instantly become squad leader
				rallyID = rc.getID();
				rc.broadcast(rallyLeaderChan, rallyID);
				// and act defensive
				myAggression = UnitAggression.NEVER_MOVE_INTO_RANGE;
			}
			
			rc.setIndicatorString(0, "CONVOY, Leader: " + rallyID + ", strength " + rc.readBroadcast(rallyStrengthChan));
	
			// get the location. this is always valid thanks to the above line
			myTarget = rc.senseRobot(rallyID).location;
	
			// if we made it to where we're going, switch to HOLD
			if (myLocation.distanceSquaredTo(myTarget) < Consts.HOLD_DISTANCE)
			{
				myState = UnitState.HOLD; // switch to HOLD state
				myAggression = UnitAggression.NO_TOWERS;
			}
			rallyMove();
			break;
			
		case HOLD:
			// switch to attack if our entire squad is strong enough
			int rallyStrength = rc.readBroadcast(rallyStrengthChan);
			
			if (rallyStrength > Consts.RALLY_STRENGTH_THRESH)
			{
				if (rallyID == rc.getID())
				{
					rc.broadcast(rallyLeaderChan, 0);
				}
				myState = UnitState.ATTACK_MOVE;
				myAggression = UnitAggression.NO_TOWERS;
				break;
			}
			
			// or, still holding....
			if (!rc.canSenseRobot(rallyID))
			{
				// become the new squad leader
				rallyID = rc.getID();
				rc.broadcast(rallyLeaderChan, rallyID);
				myAggression = UnitAggression.NEVER_MOVE_INTO_RANGE;
			}
			
			rc.setIndicatorString(0, "HOLD, Leader: " + rallyID + ", strength " + rc.readBroadcast(rallyStrengthChan));
	
			myTarget = rc.senseRobot(rallyID).location;
			
			// and then move toward rally location
			rallyMove();
			break;
			
		case ATTACK_MOVE:
			// JUST RAAAAAAAAAAAAAAAAAAAAAAGE
			// first, check leader status
			int rageLeader = rc.readBroadcast(rageLeaderChan);
			// if there isn't one, we're doin' it, baby
			if (!rc.canSenseRobot(rageLeader))
			{
				// set ourselves as rage leader
				rageLeader = rc.getID();
				rc.broadcast(rageLeaderChan,rageLeader);
			}
			
			rc.setIndicatorString(0, "RAGE, Leader: " + rageLeader + ", val: " + myGrid.readValue(gridRageBase));
			myAggression = UnitAggression.NO_TOWERS;
			rageUpdateTarget();
			rageMove();
			break;
		}
	}
		
	// moves toward rally target if not the squad leader with simple move potential
	// or other stuff if we are the squad leader
	static void rallyMove() throws GameActionException
	{
		if (!rc.isCoreReady())
			return;
		
		if (rc.getID() == rc.readBroadcast(rallyLeaderChan))
		{
			// descend the defensive grid if we're not too far already
			int defval = myGrid.readValue(gridDefenseBase);
			//rc.setIndicatorString(1, "LEADER! defense val: " + defval + ", offense: " + myGrid.readValue(gridOffenseBase) + ", ID: " + myGrid.gridID);
			if (defval < (Consts.GRID_BASE-6))
				return;
			//int bitmoves = gridPathfind(myLocation,gridRageBase,true);
			
			//move at half speed so others can catch up
			if(rand.nextDouble()<0.5)
			{	
			// follow to best mining loc	
				
	
				// int bitdir = gridPathfind(myLocation, gridMiningBase, true);
				//tryMove(bitdir);
				
				//Direction dir = myLocation.directionTo(myHQ); // for now
				int bitdir = gridPathfind(myLocation, gridOffenseBase, true);
				tryMove(myLocation.directionTo(enemyHQ),bitdir,myAggression);
				//tryMove(myLocation.directionTo(enemyHQ),bitmoves);
			}
			return;
		}
		
		if (myTarget != null)
		{
			if (myTarget.equals(myLocation))
				return;
			
			//rc.setIndicatorDot(myTarget.add(Direction.NORTH), 255, 255, 255);
			// see if we're close, move straight towards it
			if (myLocation.distanceSquaredTo(myTarget)<=24)
			{
				tryMove(myLocation.directionTo(myTarget),0,myAggression);
				return;
			}
			else
			{
				int bitdir = gridPathfind(myLocation,gridRallyBase,true);
				tryMove(myLocation.directionTo(myTarget),bitdir,myAggression);
				return;
			}
		}
		// not close, or no target, just go in the general direction
		int bitdir = gridPathfind(myLocation,gridOffenseBase,true);
		if (myTarget != null)
			tryMove(myLocation.directionTo(myTarget),bitdir,myAggression);
	}
		
	static void rageUpdateTarget() throws GameActionException
	{
		// get full list of robots
		RobotInfo[] bots = rc.senseNearbyRobots(myType.sensorRadiusSquared,myTeam.opponent());
		
		int ragePriority = 0;
		int curTargetID = rc.readBroadcast(rageTargetChan);
		
		// set the priority, if we know the target
		if (rc.canSenseRobot(curTargetID))
			ragePriority = Consts.ragePriorities[rc.senseRobot(curTargetID).type.ordinal()];
		
		boolean rageset = false;
		
		// and see if we have a better target...
		for (RobotInfo ri : bots)
		{
			int newpriority = Consts.ragePriorities[ri.type.ordinal()];
//			if (ri.supplyLevel == 0 && ri.type.supplyUpkeep > 0)
//				newpriority *= 2;
			
			if (newpriority > ragePriority)
			{
				// then add it, and set ourselves as leader
				curTargetID = ri.ID;
				ragePriority = newpriority;
				rageset = true;
			}
		}
		
		// and if we changed rage targets, broadcast our ID and the target ID
		if (rageset)
		{
			rc.broadcast(rageTargetChan, curTargetID);
			rc.broadcast(rageLeaderChan, rc.getID());
		}
	}
	
	static void defenseUpdate() throws GameActionException
	{
		// get full list of robots
		RobotInfo[] bots = rc.senseNearbyRobots(myType.sensorRadiusSquared,myTeam.opponent());
		
		if (bots.length == 0)
			rc.setIndicatorString(3, "No Enemies Spotted");
		
		for (RobotInfo ri : bots)
		{
			addEnemyToTargets(defenseTargetChannels, ri.ID);
		}
	}
	
	
	static void rageMove() throws GameActionException
	{
		// if we can't move, meh
		if (!rc.isCoreReady())
			return;
		
		int rageLeaderID = rc.readBroadcast(rageLeaderChan);
		int rageTargetID = rc.readBroadcast(rageTargetChan);
		
		MapLocation leaderLoc = null;
		MapLocation targetLoc = null;
		
		if (rc.canSenseRobot(rageLeaderID))
			leaderLoc = rc.senseRobot(rageLeaderID).location;
		if (rc.canSenseRobot(rageTargetID))
			targetLoc = rc.senseRobot(rageTargetID).location;
		
		// if we're close to our rage target, unconditionally move towards it
		if (targetLoc != null && myLocation.distanceSquaredTo(targetLoc) < Consts.RAGE_DIST)
		{
			tryMove(myLocation.directionTo(targetLoc),0,myAggression);
			return;
		}
		
		// otherwise, long-range move, what do we do?
		
		// if rage leader
		if (rc.getID() == rageLeaderID)
		{
			// ok, if we don't have a target, or we're somehow too far from it, just walk towards the enemy
			int bitdir = gridPathfind(myLocation,gridOffenseBase,true);
			tryMove(myLocation.directionTo(enemyHQ),bitdir,myAggression);
			return;
		}
		else
		{
			// not rage leader, move towards rage leader
			if (leaderLoc != null && myLocation.distanceSquaredTo(leaderLoc) < Consts.RAGE_DIST)
			{
				tryMove(myLocation.directionTo(leaderLoc),0,myAggression);
				return;
			}
			
			// otherwise, do long-range move towards leader
			int bitdir = gridPathfind(myLocation,gridRageBase,true);
			if (leaderLoc != null)
				tryMove(myLocation.directionTo(leaderLoc),bitdir,myAggression);
			else
				tryMove(Direction.NONE,bitdir,myAggression);
		}
	}
	
	

	static void defenseMove(boolean enemiesInSight) throws GameActionException
	{
		// if we can't move, meh
		if (!rc.isCoreReady())
			return;
			
		// if rally leader
		if (rc.getID() == rc.readBroadcast(rallyLeaderChan))
		{
			
			MapLocation targetLoc = chooseEnemyTarget(defenseTargetChannels, 100);
			rc.setIndicatorString(1, "Hold Leader: Defense Target = " + targetLoc);			
			
			// if at enemy location and don't see anyone
			if (targetLoc == null)
			{
				rc.setIndicatorString(1, "Hold Leader: No enemies - continue to rally");		
				rallyMove();
				//just go to normal rally.
			}
			else // otherwise, move towards targetLoc.
			{
				// then just move directly towards the target
				tryMove(myLocation.directionTo(targetLoc),0,myAggression);
				return;
			}

		}

		// other tanks follow
		if (myTarget != null)
		{
			if (myTarget.equals(myLocation))
				return;
			
			rc.setIndicatorDot(myTarget, 255, 255, 255);
			// see if we're close, move straight towards it
			if (myLocation.distanceSquaredTo(myTarget)<=24)
			{
				tryMove(myLocation.directionTo(myTarget),0,myAggression);
				return;
			}
		}
		// not close, or no target, just go in the general direction
		rallyMove();
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
		int bestval = ascend?grid.readValue(gridChannel):-grid.readValue(gridChannel);
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
					int adjval = ascend?adjgrid.readValue(gridChannel):-adjgrid.readValue(gridChannel);
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
					int adjval = ascend?adjgrid.readValue(gridChannel):-adjgrid.readValue(gridChannel);
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
			//System.out.println("On best square");
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
	
	static boolean gridTowers() throws GameActionException
	{
		int bc = Clock.getBytecodeNum();

		GridComponent grid = new GridComponent(rc.readBroadcast(gridTowerPtrChan));
		if (!grid.isValid()) grid.initialize();
		
		int towerstatus = rc.readBroadcast(gridTowerDoneChan);

		// towerstatus for setting towers to voids goes HQ -> 0, then gridTowers->1->2
		// and for removing towers from voids goes HQ -> 3, then gridTowers->4->5, then never changes again
		
		// first check if we're done
		if (towerstatus == 2 || towerstatus == 5)
			return false;
		
		// if we're not done, check if we made it all the way around
		if (grid.isFirst())
		{
			if (towerstatus == 1 || towerstatus == 4)
			{
				// just finished an update cycle, advance by one
				towerstatus++;
				rc.broadcast(gridTowerDoneChan, towerstatus);
				System.out.println("Towers update @ " + curRound);
				//rc.breakpoint();
				return false;
			}
			else if (towerstatus == 0 || towerstatus == 3)
			{
				// flag that we're starting an update cycle
				// and continue updating
				towerstatus++;
				rc.broadcast(gridTowerDoneChan, towerstatus);
			}
			else
			{
				return false;
			}
		}
		
		// advance the pointer
		rc.broadcast(gridTowerPtrChan,grid.nextIndexPointer());
		
		// ok, now let's do this
		MapLocation[] towers = rc.senseEnemyTowerLocations();
		
		MapLocation gridloc = grid.getCenter();
		
		boolean flagset = grid.getFlag(STATUS_TOWER);
		
		// areas within tower range
		int towermask = 0;
		
		for (MapLocation b : towers)
		{
			// is this grid possibly within that building's radius?
			if (b.distanceSquaredTo(gridloc) > 50)
				continue;
			// otherwise, quick loop to check what's in range
			for (int i=0; i<25; i++)
			{
				MapLocation loc = gridloc.add(gridOffX[i],gridOffY[i]);
				if (loc.distanceSquaredTo(b) <= 24)
					towermask |= (1<<i);
			}
		}

		// remove towers
		if (towerstatus>2)
			towermask = 0;
		
		// are we under tower influence, or were we?
		if (towermask > 0 || flagset == true)
		{
			//debug_drawGridMask(gridloc,towermask,255,255,255);
			// we have towers, let's make sure we re-path it
			if (towermask > 0)
				grid.setFlag(STATUS_TOWER);
			else
				grid.unsetFlag(STATUS_TOWER);
			// we are adding a building to this location
			int norms = grid.readProperty(gridNormalBase);
			int voids = grid.readProperty(gridVoidBase);
			int known = grid.readProperty(gridKnownBase);
			
			// set that we *know* that tower locations are voids, and not norms...
			// (and that we don't know the rest, since it might have changed)
			norms &= ~towermask;
			voids |= towermask;
			known = towermask;
			
			grid.writeProperty(gridNormalBase,norms);
			grid.writeProperty(gridVoidBase,voids);
			grid.writeProperty(gridKnownBase,known);
			
			// save it to a value for later use for this grid index (not ID)
			grid.writeProperty(gridTowerBase,towermask);
			
			// and reset it, so that we recalculate the path/connected components
			grid.reset();
		}
		
		//System.out.println("Tower time: " + (Clock.getBytecodeNum()-bc));
		
		return true;
	}
	
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
		
		gridOre = Math.min(gridOre, Consts.MINING_GRID_MAX);
		
		grid.writeValue(gridOreBase, (int)gridOre);
		int oreMiningCriterion = rc.readBroadcast(bestOrePatchAvg);
		rc.broadcast(bestOrePatchAvg, Math.max( oreMiningCriterion, (int) (gridOre/25) ));
		
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
		
		int gridFriend = 0;
		int gridEnemy = 0;
		
		for (RobotInfo ri : bots)
		{
			// get pathable index
			int pathind = (1<<((ri.location.x-ul.x)+GRID_SPC*(ri.location.y-ul.y)));
			
			// if it doesn't belong to this connected component, continue
			if ((pathind&pathable)==0 && ri.type != RobotType.DRONE)
				continue;
			
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
		int gridval = grid.readValue(gridOreBase) + Consts.MINING_DECAY + Consts.MINING_OFFSET;
		
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
	
	static boolean gridRageRally() throws GameActionException
	{
		int bc = Clock.getBytecodeNum();

		GridComponent grid = new GridComponent(rc.readBroadcast(gridRRPtrChan));
		if (!grid.isValid()) grid.initialize();
		
		// check if we made it all the way around
		if (grid.isFirst())
		{
			int curround = Clock.getRoundNum();
			int lastup = rc.readBroadcast(gridLastRRChan);
			// recently updated, don't run again
			if (curround - lastup < GRID_RR_FREQ)
				return false;

			// broadcast the start of the last update
			rc.broadcast(gridLastRRChan,curround);
			System.out.println("Rage/Rally update @ " + curround);
		}
		
		
		// advance the pointer
		rc.broadcast(gridRRPtrChan,grid.nextCCPointer());		

		// now find the rally target
		int rallyLeader = rc.readBroadcast(rallyLeaderChan);
		int rallyval = grid.readValue(gridRallyBase) + Consts.GRID_DECAY;
		if (rc.canSenseRobot(rallyLeader))
		{
			RobotInfo ri = rc.senseRobot(rallyLeader);
			if (ri.location.distanceSquaredTo(grid.getCenter()) <= GRID_SENSE && grid.isInMaybe(ri.location))
				rallyval = Consts.GRID_BASE;
		}
		
		int rageLeader = rc.readBroadcast(rageLeaderChan);
		int rageval = grid.readValue(gridRageBase) + Consts.GRID_DECAY;
		if (rc.canSenseRobot(rageLeader))
		{
			RobotInfo ri = rc.senseRobot(rageLeader);
			if (ri.location.distanceSquaredTo(grid.getCenter()) <= GRID_SENSE && grid.isInMaybe(ri.location))
			{
				// set gridval to a large value
				rageval = Consts.GRID_BASE;
			}
		}
		
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
				{
					rallyval = Math.max(rallyval,adjgrid.readValue(gridRallyBase)+Consts.GRID_DECAY);
					rageval = Math.max(rageval,adjgrid.readValue(gridRageBase)+Consts.GRID_DECAY);
				}

				// and cycle on to the next connected component
				adjgrid.nextComponent();
			}			
		}
		
		grid.writeValue(gridRallyBase, rallyval);
		grid.writeValue(gridRageBase, rageval);
		//System.out.println("Rage/rally time: " + (Clock.getBytecodeNum()-bc));
		
		return true;
	}
	
	static boolean gridOffenseDefense() throws GameActionException
	{
		int bc = Clock.getBytecodeNum();

		GridComponent grid = new GridComponent(rc.readBroadcast(gridODPtrChan));
		if (!grid.isValid()) grid.initialize();
		
		// check if we made it all the way around
		if (grid.isFirst())
		{
			int curround = Clock.getRoundNum();
			int lastup = rc.readBroadcast(gridLastODChan);
			// recently updated, don't run again
			if (curround - lastup < GRID_OD_FREQ)
				return false;

			// broadcast the start of the last update
			rc.broadcast(gridLastODChan,curround);
			System.out.println("Offense/Defense update @ " + curround);
		}
		
		
		// advance the pointer
		rc.broadcast(gridODPtrChan,grid.nextCCPointer());		
				
		// ok, get the base value for this grid square
		int defval = grid.readValue(gridDefenseBase)+Consts.GRID_DECAY;
		int offval = grid.readValue(gridOffenseBase)+Consts.GRID_DECAY;
		
		// make sure the values are initialized
		if (defval == Consts.GRID_DECAY)
		{
			MapLocation gridcenter = grid.getCenter();
			int disttohq = Math.abs(gridcenter.x-myHQ.x)/5+Math.abs(gridcenter.y-myHQ.y)/5;
			defval = Consts.GRID_INIT_BASE-disttohq;
		}
		if (offval == Consts.GRID_DECAY)
		{
			MapLocation gridcenter = grid.getCenter();
			int disttohq = Math.abs(gridcenter.x-enemyHQ.x)/5+Math.abs(gridcenter.y-enemyHQ.y)/5;
			offval = Consts.GRID_INIT_BASE-disttohq;
		}
		
		// check offensive tower/HQ target
		int offensetarget = rc.readBroadcast(offenseBuildingChan);
		if (offensetarget > enemyTowers.length)
			offensetarget = enemyTowers.length;
		MapLocation offenseloc = (offensetarget==0)?enemyHQ:enemyTowers[offensetarget-1];

		// get our square's pathable U unknown, and see what it connects to...
		int pathable = grid.getMaybes();
		
		// first, are we set to HQ? then just check...
		if (offensetarget == 0 && GridComponent.indexFromLocation(enemyHQ) == grid.gridIndex && grid.isInMaybe(enemyHQ))
		{
			offval = Consts.GRID_BASE;
		}
		else if (grid.getCenter().distanceSquaredTo(offenseloc) <= 55)
		{
			// is this possibly in range of this grid?

			// now check masking, briefly, relaxed
			int towermask = relaxGrid(grid.readProperty(gridTowerBase),GRID_MASK);
			// any norm/unknown squares connecting to it? then set as offensive target
			if ((towermask&grid.getFastMaybes()) > 0 || towermask == GRID_MASK)
			{
				offval = Consts.GRID_BASE;
				debug_drawGridMask(grid.getCenter(),towermask,255,0,255);
			}
		}
		
		if (GridComponent.indexFromLocation(myHQ) == grid.gridIndex && grid.isInMaybe(myHQ))
		{
			defval = Consts.GRID_BASE;
		}
		
		
		// now, let's loop through each adjacent direction & their gridids
		for (int dir=0; dir<4; dir++)
		{
			GridComponent adjgrid = grid.offsetTo(dir);

			while (adjgrid.isValid())
			{
				int edges = adjgrid.readValue(edgeChans[dir]);
				
				// possibly connected?
				if ((edges&pathable&bitEdge[dir]) > 0) // keep largest value
				{
					defval = Math.max(defval,adjgrid.readValue(gridDefenseBase)+Consts.GRID_DECAY);
					offval = Math.max(offval,adjgrid.readValue(gridOffenseBase)+Consts.GRID_DECAY);
				}

				// and cycle on to the next connected component
				adjgrid.nextComponent();
			}			
		}
		
		grid.writeValue(gridDefenseBase, defval);
		grid.writeValue(gridOffenseBase, offval);
		//System.out.println("Offense/defense time: " + (Clock.getBytecodeNum()-bc));
		
		return true;
	}
	
	static boolean gridConnectivity() throws GameActionException
	{
		int bc = Clock.getBytecodeNum();
		
		int ptr = rc.readBroadcast(gridConnectivityPtrChan);
		// start off the grid at the first connected component for this function
		GridComponent grid = new GridComponent(ptr&65535);
		if (!grid.isValid()) grid.initialize();
		
		int atomicStartRound = Clock.getRoundNum();
		
		// go on to the next connected component, so we don't double-count things
		rc.broadcast(gridConnectivityPtrChan,grid.nextIndexPointer());
		
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
					rc.broadcast(gridConnectivityPtrChan,grid.gridIndex);
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
			rc.broadcast(gridConnectivityPtrChan,grid.nextCCPointer());

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
	
	// request supplies and post supply urgency
	static double requestSupplies() throws GameActionException
	{
		double supplyUpkeepMultiplier = 100;
		double supplyUrgency = (rc.getType().supplyUpkeep*supplyUpkeepMultiplier)/(1+rc.getSupplyLevel()); // 1/supplyFactor
		double supplyFactor = 1/supplyUrgency;
		double nearbyUrgency = supplyUrgency;
		double globalMaxUrgency = rc.readBroadcast(supplyUrgencyChan);
		MapLocation myLocation = rc.getLocation();
		int supplyLookupRadiusSquared = 49;

		// only request supplies if you have less than 10 turns of supply.
		if (supplyUrgency > (rc.getType().supplyUpkeep/10))
		{

			RobotInfo[] friendlyRobots = rc.senseNearbyRobots(supplyLookupRadiusSquared, myTeam);
			for (RobotInfo b: friendlyRobots)
			{
				nearbyUrgency += (b.type.supplyUpkeep*supplyUpkeepMultiplier)/(1+b.supplyLevel);
			}

			if (nearbyUrgency > globalMaxUrgency){
				rc.broadcast(supplyUrgencyChan,(int)nearbyUrgency);
				rc.setIndicatorDot(myLocation, 0, 0, 255); // put blue dot at location
				rc.broadcast(supplyRequestID, rc.getID()); // my ID
			}

			rc.setIndicatorString(2, "supply factor = " + supplyFactor + " nearby supply urgency = " + nearbyUrgency);
		}
		return supplyFactor;
	}
	
	// Supply Transfer Protocol
	static void transferSupplies(double supplyFactor) throws GameActionException
	{
		RobotInfo[] nearbyAllies = rc.senseNearbyRobots(rc.getLocation(),GameConstants.SUPPLY_TRANSFER_RADIUS_SQUARED,myTeam);
		double mySupply = rc.getSupplyLevel();
		double lowestSupplyRating = mySupply*supplyFactor;
		double transferAmount = 0;
		MapLocation suppliesToThisLocation = null;
		for(RobotInfo ri:nearbyAllies){ // only transfer to bots with less supply
			if(ri.supplyLevel<lowestSupplyRating){
				lowestSupplyRating = ri.supplyLevel;
				transferAmount = mySupply*supplyFactor/(supplyFactor + 1);
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

	
	static boolean isSafeDirection(MapLocation myLocation, Direction dir) throws GameActionException
	{
		// checks if the facing direction is safe from towers
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
	static boolean attackSomething() throws GameActionException
	{
		// if we can attack squad target (eg. tower), so do
		if (!rc.isWeaponReady())
			return false;

		RobotInfo[] enemies = rc.senseNearbyRobots(myType.attackRadiusSquared, enemyTeam);
		double minhealth = 1000;
		
		if (enemies.length == 0)
			return false;

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
		{
			rc.attackLocation(minloc);
			return true;
		}
		return false;
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
	/*static void aggMove() throws GameActionException {

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
    }*/
	
	
	// Potential field move
		static void movePotential() throws GameActionException {

			float forceX = 0.0f;
			float forceY = 0.0f;

			float fDest = 10.0f;
			float fBored = 0.0f; // off for now
			float fFormation = 3.0f;
			float qFriendly = 0.0f; // off for now repel, goes as 1/r

			float fSticky = 0.0f; //off for now. goes as 1/r
			float fBinding = 1.0f;
			float fNoiseMax = 0.0f;// off for now
			float fEnemySighted = 10.0f;
			float qEnemyInRange = -15.0f; //is multiplied by attack strength
			//float aggCoef = 0.8f;
			float fNoise = 0.0f;
			
			float strengthBal = 0;
			
			/*
			// get list of nearby robots
			RobotInfo[] friendlyRobots = rc.senseNearbyRobots(
					myType.sensorRadiusSquared, myTeam);
			RobotInfo[] enemyRobots = rc.senseNearbyRobots(
					myType.sensorRadiusSquared, myTeam.opponent());
			for (RobotInfo bot : friendlyRobots) strengthBal += unitVal[bot.type.ordinal()];
			for (RobotInfo bot : enemyRobots) strengthBal -= unitVal[bot.type.ordinal()];
			//rc.setIndicatorString(2, "StrengthBal = " + strengthBal);
			*/
			
			// attracted to squad target, far away, const fDest
			if (myTarget !=null){
				int destX = (myTarget.x - myLocation.x);
				int destY = (myTarget.y - myLocation.y);

				float d2dest = (float) Math.sqrt(destX * destX + destY * destY);


				forceX =  destX / d2dest;
				forceY =  destY / d2dest;
			}

			/*
				int engageDirOrd = rc.readBroadcast(engageDirChan); 

				//rotate basis to point along engageDir
				float forceEx = Consts.cos[engageDirOrd]*forceX - Consts.sin[engageDirOrd]*forceY;
				float forceEy = Consts.sin[engageDirOrd]*forceX + Consts.cos[engageDirOrd]*forceY;

				// elongate along Ey
				forceEx *= 50;

				//rotate back to XY coordinates
				forceX = Consts.cos[engageDirOrd]*forceEx + Consts.sin[engageDirOrd]*forceEy;
				forceY = -Consts.sin[engageDirOrd]*forceEx + Consts.cos[engageDirOrd]*forceEy;
			*/
			/*
			for (RobotInfo bot : friendlyRobots) {
				// doesn't apply to towers and HQ
				if (bot.type == RobotType.TOWER || bot.type == RobotType.HQ)
					continue;

				int vecx = bot.location.x - myLocation.x;
				int vecy = bot.location.y - myLocation.y;
				int d2 = bot.location.distanceSquaredTo(myLocation);
				float id = invSqrt[d2];

				// weakly attract, constant normalized to unitVal 
				forceX += unitVal[bot.type.ordinal()] * fSticky *id * vecx; // constant
				forceY += unitVal[bot.type.ordinal()] * fSticky *id* vecy;	
		
				// don't get too close, at close range, as -1/r
				forceX += qFriendly * id * id*vecx;
				forceY += qFriendly * id * id*vecy;	
			}

			for (RobotInfo bot : enemyRobots) {
				// dangerous ones, dealt with in tryMove
				if (bot.type == RobotType.TOWER || bot.type == RobotType.HQ)
					continue;

				int vecx = bot.location.x - myLocation.x;
				int vecy = bot.location.y - myLocation.y;
				int d2 = bot.location.distanceSquaredTo(myLocation);


				float id = invSqrt[d2];

				// attract to enemy units, adjusted for strength balance, constant
				forceX += strengthBal * fEnemySighted * unitAtt[bot.type.ordinal()] * id  * vecx ; // constant 
				forceY += strengthBal * fEnemySighted * unitAtt[bot.type.ordinal()] * id  * vecy ;


				// enemies in range, 1/r
				forceX += unitVal[bot.type.ordinal()] * qEnemyInRange * id*id * vecx; // constant
				forceY += unitVal[bot.type.ordinal()] * qEnemyInRange * id *id * vecy;	
		
			}
				*/

			// get direction of force
			
			Direction dir = myLocation.directionTo(myLocation.add((int)(10*forceX),(int)(10*forceY)));


			// only move if nonzero force
			if(forceX*forceX + forceY*forceY > 0){ 
				tryMove(dir,0,myAggression);
			}		

			rc.setIndicatorLine(myLocation, myLocation.add((int)forceX,(int)forceY), 0,
					255, 255); // indicator line along preferred direction




			return;

		}
	
	// this method will attempt to move towards d, preferentially using bitmoves, using specified aggression
		static boolean tryMove(Direction d, int bitmoves, UnitAggression agg) throws GameActionException
		{
			// can't move, don't do anything
			if (!rc.isCoreReady())
				return false;

			// take into account enemies of particular types
			int safemoves = 0;
			
			switch (agg)
			{
			case CHARGE:
			case NO_RESTRICTIONS:
				// they're all safe
				safemoves = 0x1FF;
				break;
			case NO_TOWERS:
				safemoves = 0x1FF&(~getTowerMoves());
				break;
			case NEVER_MOVE_INTO_RANGE:
				safemoves = ~getTowerMoves();
				safemoves &= ~getEnemyMoves();
				safemoves &= 0x1FF;
				break;
			}

			// now mask it by moves we deem safe
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
				// that we specified with bitmoves
				// so we just pick a random safe direction instead, if possible
				bitmoves = (canmoves&safemoves);
				if (bitmoves == 0)
					return false;
			}

			// now pick by starting in direction d and moving out
			int dirint = d.ordinal();
			
			// do we check preferred directions in the other way?
			boolean tryopposite = rand.nextBoolean();
			
			for (int i=0; i<8; i++)
			{
				int dir = tryopposite?(dirint+Consts.dirOffsets[i])&7:
					(dirint+16-Consts.dirOffsets[i])&7;
				// can we move here/do we want to move here?
				if ((bitmoves&(1<<dir))==0)
					continue;
				
				// ok, so now we can and do want to, double check and then do it
				Direction dd = Direction.values()[dir];

				// already checked this with canmoves, but best to double-check...
				if (!rc.canMove(dd))
					continue;
				
				rc.move(dd);
				return true;
			}
			
			return false;
		}

		static int getTowerMoves()
		{
			MapLocation myloc = rc.getLocation();
			
			long bitmoves = 0;
			
			if (Math.abs(myloc.x-enemyHQ.x)<7 && Math.abs(myloc.y-enemyHQ.y)<7)
			{
				int ind = (enemyHQ.x-myloc.x+6) + 13*(enemyHQ.y-myloc.y+6);
				if (enemyTowers.length>1) // use range 35
					bitmoves |= (Consts.attackMask[ind]>>(9*Consts.unitRanges[0]));
				else // use 24
					bitmoves |= (Consts.attackMask[ind]>>(9*Consts.unitRanges[1]));;
			}
			
			for (MapLocation t : enemyTowers)
			{
				if (Math.abs(myloc.x-t.x)>=6 || Math.abs(myloc.y-t.y)>=6)
					continue;
					
				int ind = (t.x-myloc.x+6) + 13*(t.y-myloc.y+6);
				bitmoves |= (Consts.attackMask[ind]>>(9*Consts.unitRanges[1]));;
			}
			
			/*for (int i=0; i<8; i++)
			{
				if ((bitmoves&(1<<i)) > 0)
					rc.setIndicatorDot(myloc.add(Direction.values()[i]), 255, 0, 255);
			}*/
			
			return (int)(bitmoves&0x1FF);
		}
		
		static int getEnemyMoves()
		{
			RobotInfo[] bots = rc.senseNearbyRobots(myType.sensorRadiusSquared, enemyTeam);
			MapLocation myloc = rc.getLocation();
			
			// 0...8 bits determine which squares are dangerous
			long bitmoves = 0;
			
			// by definition, if inside sensor radius, will be on array
			for (RobotInfo ri : bots)
			{
				int ind = (ri.location.x-myloc.x+6) + 13*(ri.location.y-myloc.y+6);
				bitmoves |= (Consts.attackMask[ind]>>(9*Consts.unitRanges[ri.type.ordinal()]));
			}
			
			/*for (int i=0; i<8; i++)
			{
				if ((bitmoves&(1<<i)) > 0)
					rc.setIndicatorDot(myloc.add(Direction.values()[i]), 255, 255, 255);
			}*/
			
			// and return lowest 9 bits
			return (int)(bitmoves&0x1FF);
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
	
	static boolean buildUnitParity(RobotType type) throws GameActionException
	{
		boolean spaceToBuild = true; 
		if(rc.getTeamOre()>type.oreCost && rc.isCoreReady())
		{
			spaceToBuild = false; //if doesn't build it was bc no space
			Direction buildDir = myLocation.directionTo(myHQ);
			for (int i=0; i<8; i++)
			{
				MapLocation buildLoc = myLocation.add(buildDir);
				int sameX = (myHQ.x + buildLoc.x) % 2; // 0 if same
				int sameY = (myHQ.y + buildLoc.y) % 2; // 0 if same
				
				int okParity = (sameX+sameY)%2; // 0 if ok
				
				if(rc.canBuild(buildDir, type) && okParity == 0)
				{
					rc.build(buildDir, type);
					spaceToBuild = true;
					break;
				}
				buildDir = buildDir.rotateLeft();
			}	
			
		}
		return spaceToBuild;
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
			int bestval = grid.readValue(channel);
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
						int adjval = adjgrid.readValue(channel);
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
						int adjval = adjgrid.readValue(channel);
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
				
				if (Clock.getRoundNum()%50 < 15)
					debug_drawConnections(grid);
				
				debug_drawBestDirection(grid,gridRageBase);
				//debug_drawBestDirection(grid,gridDefenseBase);
				//debug_drawBestDirection(grid,gridRallyBase);
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
					debug_drawValue(grid.getCenter().add(i,0),(grid.readValue(gridOffenseBase)-Consts.GRID_BASE)*10);
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
	
	// clears all connected components of a grid cell
	public void reset() throws GameActionException
	{
		// keep most of the flags
		// but delete gridID and # of CC, so it seems like it's an empty square
		gridInfo &= 65535;
		gridInfo &= ~RobotPlayer.GRID_CC_MASK;
		gridInfo &= ~RobotPlayer.STATUS_PATHED;
		writeProperty(RobotPlayer.gridInfoBase,gridInfo);
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
		
		// if we didn't find any, just set it to the first component, we're clearly just confused
		firstComponent();
		
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