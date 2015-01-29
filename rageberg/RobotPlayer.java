package rageberg;

import battlecode.common.*;

import java.util.*;

/* Sensing location defines etc */
class RobotConsts
{
	long[] attackMask = {0L,0L,17179869184L,17314086912L,17582522368L,17582522368L,17582522368L,17582522368L,17582522368L,402653184L,268435456L,0L,0L,0L,17179869184L,25904021504L,60565749760L,61102882816L,61103407104L,61103407104L,61103407104L,61069852672L,52479655936L,939524096L,268435456L,0L,17179869184L,25904021504L,64860717056L,67562176512L,68703552000L,68704601600L,68704601600L,68704601600L,68687758848L,64292127744L,53553397760L,939524096L,268435456L,25769803776L,64592281600L,67562176512L,68711973504L,68717348481L,68719447683L,68719447683L,68719447683L,68711026179L,68689858050L,64292127744L,36373528576L,805306368L,30064771072L,66756542464L,68711448576L,68717363904L,68719472577L,68719476679L,68719476679L,68719476679L,68719460103L,68710964742L,68656303104L,38522060800L,1879048192L,30064771072L,66764931072L,68715659264L,68719469280L,68719476721L,68719476735L,68719476735L,68719476735L,68719476511L,68719361550L,68660501504L,38524157952L,1879048192L,30064771072L,66764931072L,68715659264L,68719469280L,68719476721L,68719476735L,68719476735L,68719476735L,68719476511L,68719361550L,68660501504L,38524157952L,1879048192L,30064771072L,66764931072L,68715659264L,68719469280L,68719476721L,68719476735L,68719476735L,68719476735L,68719476511L,68719361550L,68660501504L,38524157952L,1879048192L,30064771072L,66731376640L,68715331584L,68718944352L,68719475568L,68719476604L,68719476604L,68719476604L,68719410972L,68685806604L,68660238336L,38523633664L,1879048192L,12884901888L,66580381696L,68346200064L,68717436960L,68718882864L,68719409208L,68719409208L,68719409208L,68685821976L,68668635144L,51412733952L,38388367360L,1610612736L,4294967296L,15032385536L,67654123520L,68346200064L,68683849728L,68684902400L,68684902400L,68684902400L,68668108800L,51412733952L,42683334656L,3758096384L,1073741824L,0L,4294967296L,15032385536L,50474254336L,51015319552L,51017416704L,51017416704L,51017416704L,51009028096L,42414899200L,3758096384L,1073741824L,0L,0L,0L,4294967296L,6442450944L,7516192768L,7516192768L,7516192768L,7516192768L,7516192768L,3221225472L,1073741824L,0L,0L};
	int[] dirOffsets = {0,7,1,6,2,5,3,4};
	
	static int[] directionLocsX = {0, 1, 1, 1, 0, -1, -1, -1, 0, 0};
	static int[] directionLocsY = {-1, -1, 0, 1, 1, 1, 0, -1, 0, 0};

	// maps position offsets in our 5x5 bit-grid to direction flags
	int[] gridToDir = { 0,   0,   0,   0,   0,
					    0,1<<7,1<<0,1<<1,   0,
					    0,1<<6,   0,1<<2,   0,
					    0,1<<5,1<<4,1<<3,   0,
					    0,   0,   0,   0,   0 };

	/* Grid weight values */
	int MINING_DECAY = -20; // additive factor for neighboring grid squares
	int MINING_GRID_MAX = 500; // all squares in 5x5 grid having 20 ore
	int MINING_OFFSET = 1000;
	int WEIGHT_ORE_MINER = -50;
	int WEIGHT_ORE_BEAVER = -30;
	
	int GRID_DECAY = -1;
	int GRID_BASE = 1000;
	int GRID_INIT_BASE = 500;
	
	int HQ_TOWER_THRESH = 3;
	
	// # of units per supply depot
	double DEPOT_UNIT_RATIO = 15;
}

public class RobotPlayer {
	
	static RobotController rc;
	
	// Game-given values
	static Team myTeam;
	static Team enemyTeam;
	static RobotType myType;
	
	static int myTarget;
	
	// did we move since last turn?
	static boolean justMoved;
	
	static int curRound;
	
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
	static final int GRID_EDGE_CENTERS = (1<<2)|(1<<22)|(1<<10)|(1<<14)|(1<<0)|(1<<4)|(1<<24)|(1<<20);
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
	// contains a tower or HQ
	static final int STATUS_TARGET = (1<<7);
	// started connectivity calc
	static final int STATUS_UPDATING = (1<<8);
	
	static final int STATUS_NORTH=0;
	static final int STATUS_SOUTH=3;
	static final int STATUS_EAST=1;
	static final int STATUS_WEST=2;
	static final int STATUS_NE=4;
	static final int STATUS_SE=5;
	static final int STATUS_SW=6;
	static final int STATUS_NW=7;
	
	static int curChan = 0;
	
	static final int unitNum = curChan++;
		
	/* Map info */
	// encodes known/unknown map symmetry
	static final int mapSymmetryChan = curChan++;
	// packed grid min/max values in each direction
	static final int gridExtentsChan = curChan++;

	/* Grid info */
	
	
	// update frequencies
	static final int NUM_GRIDS = 3;
	
	static int GRID_ORE_FREQ = 15;
	static int GRID_MINING_FREQ = 15;
	static int GRID_TARGET_FREQ = 2;
	
	
	// map array of basic grid square status
	static final int gridInfoBase = curChan; static {curChan+=GRID_NUM;}
	// map list of next grid IDs
	static final int gridNextIDBase = curChan; static {curChan+=GRID_NUM;}
	static final int gridNextIDChan = curChan++;	

	// ORE GRID TOTALS
	static final int gridLastOreChan = curChan++;
	static final int gridOrePtrChan = curChan++;
	// AND THEIR VALUES
	static final int gridOreBase = curChan; static {curChan+=GRID_NUM;}
	//static final int gridEnemyBase = curChan; static {curChan+=GRID_NUM;}
	
	
	// MINING-ORE GRID
	static final int gridLastMiningChan = curChan++;
	static final int gridMiningPtrChan = curChan++;
	static final int gridMiningBase = curChan; static {curChan+=GRID_NUM;}
	
	// OFFENSIVE/DEFENSIVE GRIDS
	static final int gridLastTargetChan = curChan++;
	static final int gridTargetPtrChan = curChan++;
	static final int gridTargetValidChan = curChan++;
	
	// and targets
	static final int gridTargetXBase = curChan; static {curChan+=7;}
	static final int gridTargetYBase = curChan; static {curChan+=7;}
	
	static final int gridTarget0Base = curChan; static {curChan+=GRID_NUM;}
	static final int gridTarget1Base = curChan; static {curChan+=GRID_NUM;}
	static final int gridTarget2Base = curChan; static {curChan+=GRID_NUM;}
	static final int gridTarget3Base = curChan; static {curChan+=GRID_NUM;}
	static final int gridTarget4Base = curChan; static {curChan+=GRID_NUM;}
	static final int gridTarget5Base = curChan; static {curChan+=GRID_NUM;}
	static final int gridTarget6Base = curChan; static {curChan+=GRID_NUM;}
	
	static final int[] gridTargetBases = {gridTarget0Base,gridTarget1Base,gridTarget2Base,gridTarget3Base,gridTarget4Base,gridTarget5Base,gridTarget6Base};
	
	// THE CONNECTIVITY GRID
	// these ones are indexed by location "properties"
	static final int gridNormalBase = curChan; static {curChan+=GRID_NUM;}
	static final int gridKnownBase = curChan; static {curChan+=GRID_NUM;}
	static final int gridVoidBase = curChan; static {curChan+=GRID_NUM;}
	// and these ones are indexed by ID "values"
	static final int gridPathableBase = curChan; static {curChan+=GRID_NUM;}
	static final int gridEdgesNSBase = curChan; static {curChan+=GRID_NUM;}
	static final int gridEdgesEWBase = curChan; static {curChan+=GRID_NUM;}
	static final int gridUpdateBase = curChan; static {curChan+=GRID_NUM;}
	
	
	static final int mapMinXChan = curChan++;
	static final int mapMinYChan = curChan++;
	static final int mapMaxXChan = curChan++;
	static final int mapMaxYChan = curChan++;
	
	static final int hqExceptionChan = curChan++;
	
	static MapLocation[] myTargetLocs = null;

	//===============================================================================================================
	
	
	// build order chans
	static int firstBeaverChan = curChan++;
	static int nextSpawnChan = curChan++;
	
	//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

	// enemy tower bookkeeping
	static MapLocation[] enemyTowers = new MapLocation[0];
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
	//static int minerRageTarget = curChan++;
	//static int minerRageTarget2 = curChan++;
	
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


	static enum UnitAggression
	{
		NO_TOWERS,
		CHARGE
	}

	static UnitAggression myAggression = UnitAggression.NO_TOWERS;

	
	// Adjustable parameters
    static int numBeavers = 2;
    static int maxMiners = 30;
	static int numTanks = 999;
	static int numComputers = 10;
	
	static RobotConsts Consts = new RobotConsts();
	
	static MapLocation[] enemyBuildings;
	
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
	
	public static void run(RobotController robotc)
	{
		rc = robotc;
		
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
				rc.broadcast(gridOrePtrChan, ptr);
				rc.broadcast(gridMiningPtrChan, ptr);
				rc.broadcast(gridTargetPtrChan, ptr);
				
				// initialize to empty, so we fill it and do stuff
				enemyBuildings = new MapLocation[0];
				break;
			case BEAVER:
				facing = rc.getLocation().directionTo(myHQ).opposite();
				break;
			case MINER:
				minerState = MinerState.SEARCHING; // initially miners are in supply chain
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
			case MINERFACTORY:
				doMinerFactory();
				break;
			case MINER:
				doMiner();
				break;
			case BEAVER:
				doBeaver();
				break;
			case BARRACKS:
				doBarracks();
				break;
			case TANKFACTORY:
				doTankFactory();
				break;
			case TECHNOLOGYINSTITUTE:
				doTech();
				break;
			case COMPUTER:
				doComputer();
				break;

			case TANK:
			case SOLDIER:
				doUnit();
				break;
			}
			
			try 
			{
				transferSupplies();
			}
			catch (Exception e)
			{
			}
			
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
		}
	}
	
	// what to do in extra cycles
	static void spare() throws GameActionException
	{
		// not tanks
		if (myType == RobotType.TANK)
			return;
		// which grids still require computing?
		int validgrids = (1<<NUM_GRIDS)-1;
		
		while (Clock.getBytecodesLeft() > 2000 && Clock.getRoundNum() == curRound)
		{
			int ngrids = Integer.bitCount(validgrids);
			
			if (ngrids == 0)
				break;
			
			int gridindex = rand.nextInt(ngrids);
			int gridbits = validgrids;
			for (int i=0; i<gridindex; i++)
				gridbits &= gridbits-1;
			gridindex = Integer.numberOfTrailingZeros(gridbits);
			
			switch (gridindex)
			{
			case 0:
				if (!gridOre())
					validgrids &= ~(1<<gridindex);
				break;
			case 1:
				if (!gridMining())
					validgrids &= ~(1<<gridindex);
				break;
			case 2:
				if (!gridTargets())
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
		
		// calculate center of map, as defined for everyone
		myHQ = rc.senseHQLocation();
		enemyHQ = rc.senseEnemyHQLocation();
		
		center = new MapLocation((myHQ.x+enemyHQ.x)/2,(myHQ.y+enemyHQ.y)/2);

		facing = getRandomDirection();
		
		myTarget = 0;
		
		myTargetLocs = new MapLocation[7];
		for (int i=0; i<7; i++)
			myTargetLocs[i] = new MapLocation(rc.readBroadcast(gridTargetXBase+i),rc.readBroadcast(gridTargetYBase+i));
		
		if (myType == RobotType.TANK || myType == RobotType.SOLDIER)
		{
			int nunits = rc.readBroadcast(unitNum);
			if (nunits < 7)
			{
				// send the first few guys out and about
				myTarget = nunits;
			}
			else
			{
				// later, focus on HQ etc.
				int validTargets = rc.readBroadcast(gridTargetValidChan);
				if (Integer.bitCount(validTargets) > 4) // more than 3 towers?
				{
					myTarget = 0;
					// pick the closest tower and go for that
					for (int i=1; i<7; i++)
					{
						if ((validTargets&(1<<i)) == 0)
							continue;
						if (myTarget == 0 || rc.getLocation().distanceSquaredTo(myTargetLocs[myTarget]) < rc.getLocation().distanceSquaredTo(myTargetLocs[myTarget]))
							myTarget = i;
					}
				}
			}
				
			rc.broadcast(unitNum, nunits+1);
		}
		
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
			
			int startdir = rand.nextInt(8);
			for (int dir=0; dir<8; dir++)
			{
				if (Clock.getRoundNum() != curRound)
					break;
				gridConnectivity(myGrid.offsetTo((dir+startdir)&7));
			}
		}
		
		// and every once in a while, update enemy tower locations
		MapLocation[] towers = rc.senseEnemyTowerLocations();
		if (towers.length != lastTowerCount)
		{
			// globally update tower list/validity list
			if (myType == RobotType.HQ)
			{
				List<MapLocation> towerlist = Arrays.asList(towers);
				int isvalid = 1; // HQ always valid
				for (int i=1; i<7; i++)
					if (towerlist.contains(myTargetLocs[i]))
						isvalid |= (1<<i);
				rc.broadcast(gridTargetValidChan, isvalid);
			}
			enemyTowers = towers;
			if (enemyTowers == null) enemyTowers = new MapLocation[0];
			lastTowerCount = enemyTowers.length;
		}
	}
	
	
	static void mapHQ() throws GameActionException
	{
		// get each set of buildings
		MapLocation[] myblds = getBuildings(myTeam);
		MapLocation[] enblds = getBuildings(myTeam.opponent());
		
		// set target locations
		int targetValid = 0;
		for (int i=0; i<enblds.length; i++)
		{
			targetValid |= (1<<i);
			// set X and Y
			rc.broadcast(gridTargetXBase+i, enblds[i].x);
			rc.broadcast(gridTargetYBase+i, enblds[i].y);
			// and flag it as possible target
			new GridComponent(enblds[i]).setFlag(STATUS_TARGET);
		}
		
		rc.broadcast(gridTargetValidChan, targetValid);
		
		myTargetLocs = new MapLocation[7];
		for (int i=0; i<7; i++)
			myTargetLocs[i] = new MapLocation(rc.readBroadcast(gridTargetXBase+i),rc.readBroadcast(gridTargetYBase+i));
		
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
		//System.out.println("Grid size: " + gridNum);

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
			
			RobotInfo[] ourTeam = rc.senseNearbyRobots(10000, rc.getTeam());
			int n = 0; // current number of beavers
			int mf = 0; // current number of miner factories
			for(RobotInfo ri: ourTeam){ // count up beavers
				if(ri.type==RobotType.BEAVER){
					n++;
				}
				if(ri.type==RobotType.MINERFACTORY){
					mf++;
				}
			}
			if(n !=1 && n<numBeavers || (n==1 && mf>0)){ // in the beginning, spawn 'numBeavers' beavers and send them out in all directions
				// build mine fact before 2nd beaver
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
			if (rc.isWeaponReady())
				attackSomething();

		} catch (Exception e) {
			System.out.println("Tower Exception");
			e.printStackTrace();
		}
	}

	static void doTech()
	{
		try {
			RobotInfo[] units = rc.senseNearbyRobots(10000,myTeam);
			int d = 0;
			for(RobotInfo b: units)
			{
				if(b.type==RobotType.COMPUTER)
					d++;
			}
			if(d<numComputers)
				spawnUnit(RobotType.COMPUTER);
			
		} catch (Exception e) {
			System.out.println("Helipad Exception");
			e.printStackTrace();
		}
	}
	
	static void doMinerFactory()
	{
		try {
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

	
	static void doComputer()
	{
		try {
			if (myLocation.distanceSquaredTo(myHQ) > 81)
				tryMove(myLocation.directionTo(myHQ),0,myAggression);
			else if (rand.nextInt(20) < 3)
				tryMove(myLocation.directionTo(enemyHQ).opposite(),0,myAggression);
		} catch (Exception e) {
			System.out.println("Basher Exception");
			e.printStackTrace();
		}
	}

	static void doBarracks()
	{
		try {
			RobotInfo[] bots = rc.senseNearbyRobots(99999, myTeam);
			int tanks = 0;
			int soldiers = 0;
			for (RobotInfo b: bots)
			{
				if (b.type == RobotType.TANK)
					tanks++;
				else if (b.type == RobotType.SOLDIER)
					soldiers++;
			}
			
			if (tanks < 5 && soldiers < 5)
				trySpawn(facing,RobotType.SOLDIER);
			
		} catch (Exception e) {
			System.out.println("Tank Factory Exception");
			e.printStackTrace();
		}
	}
	
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
	
	static void doBeaver()
	{
		try {
			// move off of directly diag squares to HQ
			int distSq = myLocation.distanceSquaredTo(myHQ);
	
			if (distSq == 2)
			{
				Direction moveDir = myHQ.directionTo(myLocation).rotateRight();	
				tryMove(moveDir,0,myAggression);
			}

			// read build order instructions
			int toBuild = rc.readBroadcast(firstBeaverChan);
			rc.setIndicatorString(1, "toBuild = " + toBuild);
			
			if (toBuild == 0) // 1st beaver to move thinks, deciding builds
				decideNextBuilds(); // ALL BUILDS ENTERED IN THIS FUNCTION
			
			
			
			else if (toBuild > 0 && toBuild != 1)
			{
				boolean spaceToBuild = buildUnitParity(RobotType.values()[toBuild]);
				//if (rand.nextDouble()< 0.3 && spaceToBuild && rc.getTeamOre()>RobotType.values()[toBuild].oreCost && rc.isCoreReady())
				if (rc.isBuildingSomething())
					rc.broadcast(firstBeaverChan,-toBuild); // flag it as done
				else if (!spaceToBuild) // if no space to build, try going farther away.
					tryMove(myHQ.directionTo(myLocation),0,myAggression);
			}
			else
			{ 
				// otherwise, try to get close to HQ
				tryMove(myLocation.directionTo(myHQ),0,myAggression);
			}

			// mine in place
			if(rc.isCoreReady()&&rc.canMine())
			{
				rc.mine();
			}

		} catch (Exception e) {
			System.out.println("Beaver Exception");
			e.printStackTrace();
		}
	}

	
	static void decideNextBuilds() throws GameActionException
	{
		double[] numUnits = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		int toBuild = 0;
		int numNonBuildings = 0;
		// count up all units
		RobotInfo[] ourTeam = rc.senseNearbyRobots(100000, rc.getTeam());
		for(RobotInfo ri: ourTeam)
		{
			numUnits[ri.type.ordinal()] += 1;
			if (ri.builder == null)
				numUnits[ri.type.ordinal()] += .001; // this tells us a building is done!
			if (ri.type.ordinal() > RobotType.AEROSPACELAB.ordinal())
				numNonBuildings++;
		}
		
		rc.setIndicatorString(1, "numUnits = " + Arrays.toString(numUnits));

		// 0 - HQ 
		// 1 - TOWER
		// 2 - SUPPLYDEPOT
		// 3 - TECHNOLOGYINSTITUTE
		// 4 - BARRACKS
		// 5 - HELIPAD
		// 6 - TRAININGFIELD
		// 7 - TANKFACTORY
		// 8 - MINERFACTORY
		// 9 - HANDWASHSTATION
		// 10 - AEROSPACELAB

		int[] buildOrder = {8, 4, 7, 3, 2, 7, 2, 2, 7, 1};
		double[] spawnPriorities = {}; // initialize spawn priorities

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
		
		// if build complete, build a supply depot if we need one, else build a tank factory
		if (buildComplete)
		{
			if (numUnits[RobotType.SUPPLYDEPOT.ordinal()]*Consts.DEPOT_UNIT_RATIO < numNonBuildings)
			{
				toBuild = 2;
				rc.broadcast(firstBeaverChan,toBuild);
			}
			else if (rc.getTeamOre() > RobotType.TANKFACTORY.oreCost+1)
			{
				toBuild = 7;
				rc.broadcast(firstBeaverChan,toBuild);
			}	
		}
		
		rc.setIndicatorString(2, "buildTotals = " + Arrays.toString(buildTotals));


		// build next building, give other beavers a chance to do it
		if (rand.nextDouble()< 0.3 && !buildComplete && toBuild != 1)
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
	
//===========================================================================================
	
	static void doMiner()
	{
		try {
			miningDuties();			
		} catch (Exception e) {
			System.out.println("Miner Exception");
			e.printStackTrace();
		}
	}

	static void miningDuties() throws GameActionException
	{
		if (!rc.isCoreReady())
			return;
		
		double oreHere = rc.senseOre(myLocation);
		int oreMiningCriterion = rc.readBroadcast(bestOrePatchAvg);

		double[] localOre = mineScore();
		double mineScore = localOre[2];
		
		// depending on what state the miner is in, execute functionality
		switch (minerState)
		{
		case LEADING:
			rc.setIndicatorDot(myLocation.add(1,0), 0,255,0); // green dots on front line miners
			if(mineScore < 3*oreMiningCriterion && oreHere < 4) // less ore and can't max out
				minerState = MinerState.SEARCHING; // switch to searching, this will be reported to HQ next round
			else // we've been sitting still and mining
				mineAndMoveStraight(oreMiningCriterion);
			break;
		case SEARCHING:
			rc.setIndicatorDot(myLocation.add(1,0), 255,255,0); // yellow dots on searchers
			mineLocally(localOre, oreMiningCriterion);
			if(mineScore > 4*oreMiningCriterion && oreHere > oreMiningCriterion/2)
				minerState = MinerState.LEADING; // switch to a leader, this will be reported to HQ next round
			break;
		case EXPLORING:
			rc.setIndicatorDot(myLocation.add(1,0), 255,255,255); // white dots on explorers
			if(rc.senseOre(myLocation)>=oreMiningCriterion || rc.senseOre(myLocation)>=10)
			{
				if(rc.canMine())
					rc.mine();
				minerState = MinerState.SEARCHING;
			}
			else if(mineScore>5*oreMiningCriterion)
			{
				minerState = MinerState.SEARCHING;
				mineLocally(localOre, oreMiningCriterion);
			}
			else
			{
				int bitdir = gridPathfind(myLocation, null, gridMiningBase);
				tryMove(Direction.OMNI, bitdir, UnitAggression.NO_TOWERS);
			}
			break;
		}
		return;
	}

	static double[] mineScore() throws GameActionException {
		final int[] x = {0,-1, 0, 0, 1,-1,-1, 1, 1,-2, 0, 0, 2,-2,-2, 2, 2};
		final int[] y = {0, 0,-1, 1, 0,-1, 1,-1, 1, 0,-2, 2, 0,-2, 2,-2, 2};
		double[] localOre = vectorSumOre(x,y);
		return localOre;
	}
	
	static void mineAndMoveStraight(int oreMiningCriterion) throws GameActionException {
		double oreHere = rc.senseOre(myLocation);
		if(oreHere>=5 || oreHere>=oreMiningCriterion/2){ //there is plenty of ore, so try to mine
			if(rand.nextDouble()<0.5){ // mine 95% of time we can collect max
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
			if(rand.nextDouble()<0.75){ // mine 98% of time we can collect max
				if(rc.isCoreReady()&&rc.canMine()){
					rc.mine();
					rc.setIndicatorString(1, "Mining good stuff");
				}
			}else{
				facing = minerLocalOreDirection(localOre, oreMiningCriterion);
				tryMove(facing, 0, UnitAggression.NO_TOWERS);
			}
			
		}else if(oreHere>0.8){ //there is a bit of ore, so maybe try to mine, maybe move on (suppliers don't mine)
			if(rand.nextDouble()<0.4){ // mine
				if(rc.isCoreReady()&&rc.canMine()){
					rc.mine();
					rc.setIndicatorString(1, "Mining suboptimal area");
				}
			}else{ // look for more ore
				facing = minerLocalOreDirection(localOre, oreMiningCriterion);
				tryMove(facing, 0, UnitAggression.NO_TOWERS);
			}
		}else if(localOre[2]>5*oreMiningCriterion){ // still look locally
			facing = minerLocalOreDirection(localOre, oreMiningCriterion);
			tryMove(facing, 0, UnitAggression.NO_TOWERS);
		}else{ //no ore, so look for more
			minersCounter = 0;
			minerState = MinerState.EXPLORING;
			int bitdir = gridPathfind(myLocation, null, gridMiningBase);
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
			sensingRegion[a] = loc;
		}
		double ore = 0;
		int i=0;
		double potentialX = 0;
		double potentialY = 0;
		double mineScore = 0;
		
		// go through and look at ore
		for(MapLocation m: sensingRegion)
		{
			if(m!=null) // put in to neglect spots we can't go
			{
				ore = rc.senseOre(m);
				mineScore += ore;
				double d2 = Math.max(1, here.distanceSquaredTo(m));
				potentialX += ore*(double)x[i]/d2;
				potentialY += ore*(double)y[i]/d2;
			}
			i++;
		}
		double potential[] = {potentialX, potentialY, mineScore};
		return potential;
	}

	//=============================================================================================

	static void doUnit()
	{
		try {
			int targetValid = rc.readBroadcast(gridTargetValidChan);
			while ((targetValid&(1<<myTarget)) == 0)
				myTarget = rand.nextInt(7);
			
			// check if we're close to any valid targets
			for (int i=0; i<7; i++)
			{
				if ((targetValid&(1<<i))==0)
					continue;
				
				if (myLocation.distanceSquaredTo(myTargetLocs[i]) <= 64)
				{
					myTarget = i;
					break;
				}
			}
			
			rc.setIndicatorString(0,"My target (" + myTarget + "): " + myTargetLocs[myTarget] + ", value: " + myGrid.readValue(gridTargetBases[myTarget]));

			// rush if there's a lot of units close by
			int nunits = rc.senseNearbyRobots(myTargetLocs[myTarget],90,myTeam).length;
			nunits = Math.max(nunits,rc.senseNearbyRobots(myLocation,15,myTeam).length);
			UnitAggression agg = UnitAggression.NO_TOWERS;
			if (nunits > 4) agg = UnitAggression.CHARGE;
			
			// prioritize movement over attack, aka rush
			if (rc.isCoreReady())
			{
				int bitstowards = gridPathfind(myLocation, myTargetLocs[myTarget], gridTargetBases[myTarget]);
				
				int d2t = myLocation.distanceSquaredTo(myTargetLocs[myTarget]);
				
				if (d2t <= myType.attackRadiusSquared)
				{
					// if we're within shooting range, only move ~1/2 the time
					if (rand.nextInt(10) < 3)
						tryMove(Direction.OMNI,bitstowards,agg);
				}
				else
				{
					// if we're far, we just try to move towards the target however, whatever
					tryMove(Direction.OMNI,bitstowards,agg);
				}
			}
			if (rc.isWeaponReady())
			{
				attackSomething();
			}

		} catch (Exception e) {
			System.out.println("Tank Exception");
			e.printStackTrace();
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
		
		// mark adjacent squares as "SEEN", meaning do connectivity processing
		// (if it's inside gridMinX, etc), but not yet "VISITED"
		for (int dir=0;dir<8;dir++)
		{
			GridComponent adjgrid = grid.offsetTo(dir);
			if (!adjgrid.getFlag(STATUS_SEEN))
				adjgrid.setFlag(STATUS_SEEN);
		}
	}
	
	static int gridPathfind(MapLocation loc, MapLocation dest, int gridChannel) throws GameActionException
	{
		GridComponent grid = new GridComponent(loc);
		// and figure out which connected component we're on
		int pathind = grid.getPathInd(loc);
		int pathindex = Integer.numberOfTrailingZeros(pathind);
		int pathable = grid.findComponent(pathind);
		
		// if for some reason this didn't work, abandon ship
		if ((pathind&pathable) == 0)
		{
			//System.out.println("Pathable is 0");
			return 0;
		}
		
		// otherwise, quick check if we're in the same grid index
		if (dest != null && grid.gridIndex == GridComponent.indexFromLocation(dest))
		{
			GridComponent destgrid = new GridComponent(dest);
			int destpath = destgrid.getPathInd(dest);
			destgrid.findComponent(destpath);
			if ((destpath&pathable)>0)
			{
				// we can get to there from here
				int norms = grid.readProperty(gridNormalBase);
				for (int i=0; i<GRID_N; i++)
				{
					int path = relaxGrid(destpath,norms);
					// now set newpath to difference between old and new
					if ((path&pathind)>0)
						break;
					// or we didn't change anything, but still didn't reach us, somehow?
					// return error
					if (destpath==path)
						return 0;
					
					destpath = path;
				}
				
				// now, if we got to here, pathable is one step before we hit pathind, so we find the possible directions by masking it
				destpath &= bitAdjacency[pathindex];

				// now let's shift it to the center
				if (pathindex>12) destpath >>= (pathindex-12);
				else if (pathindex<12) destpath <<= (12-pathindex);

				int bitdirs = 0;
				while (destpath > 0)
				{
					int setbit = Integer.numberOfTrailingZeros(destpath);
					bitdirs |= Consts.gridToDir[setbit]; 
					// and drop off last bit
					destpath &= destpath-1;
				}
				return bitdirs;
			}
		}
		
		// ok, so now we're on the right subgrid/connected component
		// find the min-value neighbor and corresponding connected edge
		// now, let's loop through each adjacent direction & their gridids
		
		// and stored values of best
		int bestedge = 0;
		int bestval = grid.readValue(gridChannel);
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
					int adjval = adjgrid.readValue(gridChannel);
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
					int adjval = adjgrid.readValue(gridChannel);
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
				//System.out.println("Path unreachable");
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
			//System.out.println("Ore update @ " + curround);
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
			//System.out.println("Mining update @ " + curround);
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
	
	static boolean gridTargets() throws GameActionException
	{
		int bc = Clock.getBytecodeNum();

		GridComponent grid = new GridComponent(rc.readBroadcast(gridTargetPtrChan));
		if (!grid.isValid()) grid.initialize();
		
		if (grid.readProperty(gridUpdateBase)+10 < Clock.getRoundNum() && grid.getFlag(STATUS_UPDATING))
		{
			grid.unsetFlag(STATUS_UPDATING);
			grid.firstComponent();
			gridConnectivity(grid);
			return false;
		}
		
		// check if we made it all the way around
		if (grid.isFirst())
		{
			int curround = Clock.getRoundNum();
			int lastup = rc.readBroadcast(gridLastTargetChan);
			// recently updated, don't run again
			if (curround - lastup < GRID_TARGET_FREQ)
				return false;

			// broadcast the start of the last update
			rc.broadcast(gridLastTargetChan,curround);
			//System.out.println("Target update @ " + curround);
		}
		
		
		// advance the pointer
		rc.broadcast(gridTargetPtrChan,grid.nextCCPointer());		
				
		int[] targetval = new int[7];
		int targetValid = rc.readBroadcast(gridTargetValidChan);
		
		int maxtarget = 0;
		for (int i=0; i<7; i++)
		{
			if ((targetValid&(1<<i))>0)
			{
				targetval[i] = grid.readValue(gridTargetBases[i]);
				maxtarget = i+1;
				// make sure the values are initialized
				/*if (targetval[i] == 0 && !grid.getFlag(STATUS_SEEN))
				{
					System.out.println("Grid intialization for " + i);
					MapLocation gridcenter = grid.getCenter();
					int disttotarget = Math.abs(gridcenter.x-myTargetLocs[i].x)/5+Math.abs(gridcenter.y-myTargetLocs[i].y)/5;
					targetval[i] = Consts.GRID_INIT_BASE-disttotarget;
				}*/
				targetval[i] += Consts.GRID_DECAY;
			}
		}

		// get our square's pathable U unknown, and see what it connects to...
		int pathable = grid.getMaybes();
		
		// is there a target in this grid cell?
		if (grid.getFlag(STATUS_TARGET))
		{
			for (int i=0; i<7; i++)
			{
				if ((targetValid&(1<<i))==0)
					continue;
					
				int x = rc.readBroadcast(gridTargetXBase+i);
				int y = rc.readBroadcast(gridTargetYBase+i);
				
				MapLocation loc = new MapLocation(x,y);
				
				if (grid.isInMaybe(loc))
					targetval[i] = Consts.GRID_BASE;
			}
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
					for (int i=0; i<maxtarget; i++)
						if ((targetValid&(1<<i))>0)
							targetval[i] = Math.max(targetval[i],adjgrid.readValue(gridTargetBases[i])+Consts.GRID_DECAY);

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
					for (int i=0; i<maxtarget; i++)
						if ((targetValid&(1<<i))>0)
							targetval[i] = Math.max(targetval[i],adjgrid.readValue(gridTargetBases[i])+Consts.GRID_DECAY);

				}
				adjgrid.nextComponent();
			}
		}
		
		for (int i=0; i<maxtarget; i++)
			if ((targetValid&(1<<i))>0)// && targetval[i] > 0)
				grid.writeValue(gridTargetBases[i], targetval[i]);

		//System.out.println("Target time: " + (Clock.getBytecodeNum()-bc));
		
		return true;
	}
	
	static boolean gridConnectivity(GridComponent grid) throws GameActionException
	{
		int bc = Clock.getBytecodeNum();
		
		if (!grid.isValid()) grid.initialize();

		// now, if we haven't seen it at all, do nothing
		if (!grid.getFlag(STATUS_SEEN))
			return true;

		if (grid.readProperty(gridUpdateBase)+10 < Clock.getRoundNum() && grid.getFlag(STATUS_UPDATING))
			grid.unsetFlag(STATUS_UPDATING);

		// or if we're updating it
		if (grid.getFlag(STATUS_UPDATING))
			return true;
		
		grid.setFlag(STATUS_UPDATING);
		grid.writeProperty(gridUpdateBase, Clock.getRoundNum());
		
		// CHECKPOINT #1: CHECK IF WE ARE ADDING ANY NEW TERRAIN TILES
		MapLocation loc = grid.getCenter();
		
		// get previously loaded values for this grid cell
		int norms = grid.readProperty(gridNormalBase);
		int voids = grid.readProperty(gridVoidBase);
		int known = grid.readProperty(gridKnownBase);
		
		int oldknown = known;
		
		// do we know all the tiles?
		// if we don't, try finding some new ones		
		if (known != GRID_MASK)
		{
			// first, get the terrain tiles
			int unknown = GRID_MASK&(~known);
			
			// set unknown to known extended by 1, if we have too many unknowns
			if (known>0)
			{
				unknown = relaxGrid(known,GRID_MASK);
				// and mask it off
				unknown = unknown & (~known);
			}
			else
			{
				unknown = GRID_EDGE_CENTERS;
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
					break;
				case UNKNOWN:
					break;
				case VOID:
				case OFF_MAP:
					voids |= z;
					known |= z;
					break;
				}
				// limit how many times we add things just to slow it down a bit to 1000-bc-size chunks
			}
			
			// then write them back if it changed
			if (known != oldknown)
			{
				grid.writeProperty(gridNormalBase, norms);
				grid.writeProperty(gridVoidBase, voids);
				grid.writeProperty(gridKnownBase, known);
				grid.unsetFlag(STATUS_PATHED);
			}
		}
		
		// if we have pathed and also know everything...
		if (grid.getFlag(STATUS_PATHED) && known == GRID_MASK)
		{
			grid.unsetFlag(STATUS_UPDATING);
			return false;
		}
		
		// CHECKPOINT #3: ADD UP PATHABLES UP TO THIS CONNECTED COMPONENT INDEX
		// note that this is where gridid gets advanced to current grid cell
		int prevpathable = 0;
		
		// if it's connected to unknown
		int maybe = relaxGrid((~known)&GRID_MASK,(~voids)&GRID_MASK);

		while (grid.isValid())
		{
			int pathable = grid.readValue(gridPathableBase);
	
			// now flood-fill the current connected component as far as we can
			
			// if pathable is 0, it's a fresh connected component (or first connected component)
			// which means we *know* it's not connected to prevpathable, if we did our math right
			// so we can just set it to any single norm that isn't part of prevpathable, if there are any
			if (pathable == 0)
				pathable = Integer.lowestOneBit(norms & ~(prevpathable));
			
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
			
			grid.nextComponent();
		}
		
		//System.out.println("Connectivity time: " + (Clock.getBytecodeNum()-bc));
		grid.unsetFlag(STATUS_UPDATING);
		
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
			// they're all safe
			safemoves = 0x1FF;
			break;
		case NO_TOWERS:
			// only towers *and outrange*
			safemoves = ~getTowerMoves();
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

	// this method will attempt to move towards d, preferentially using bitmoves, using specified aggression
	// but it'll only move if it can move directly towards the target
	static boolean tryMoveTowards(Direction d, int bitmoves, UnitAggression agg) throws GameActionException
	{
		// can't move, don't do anything
		if (!rc.isCoreReady())
			return false;

		// take into account enemies of particular types
		int safemoves = 0;
		
		switch (agg)
		{
		case CHARGE:
			// they're all safe
			safemoves = 0x1FF;
			break;
		case NO_TOWERS:
			// only towers *and outrange*
			safemoves = ~getTowerMoves();
			safemoves &= 0x1FF;
			break;
		}

		// now mask it by moves we deem safe
		bitmoves &= safemoves;
		
		// check which directions we can move in
		int canmoves = 0x100;
		for (int i=0; i<8; i++)
		{
			if (rc.canMove(Direction.values()[i]))
				canmoves |= (1<<i);
		}
		
		// now only move in directions we can move
		bitmoves &= canmoves;
		
		if (bitmoves == 0)
		{
			return false;
			// if we're here, it means we said to move, but we can't move anywhere
			// that we specified with bitmoves
			// so we just pick a random safe direction instead, if possible
			/*bitmoves = (canmoves&safemoves);
			if (bitmoves == 0)
				return false;*/
		}

		// now pick by starting in direction d and moving out
		int dirint = d.ordinal();
		
		// do we check preferred directions in the other way?
		boolean tryopposite = rand.nextBoolean();
		
		// this only tests directions towards enemy
		int dirmax = 3;
		// but if we somehow ended up on an unsafe square, we want to move away
		if ((safemoves&0x100)==0)
			dirmax = 8;
		
		for (int i=0; i<dirmax; i++)
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
				bitmoves |= (Consts.attackMask[ind]>>(9*3));
			else // use 24
				bitmoves |= (Consts.attackMask[ind]>>(9*2));;
		}
		
		for (MapLocation t : enemyTowers)
		{
			if (Math.abs(myloc.x-t.x)>=6 || Math.abs(myloc.y-t.y)>=6)
				continue;
				
			int ind = (t.x-myloc.x+6) + 13*(t.y-myloc.y+6);
			bitmoves |= (Consts.attackMask[ind]>>(9*2));;
		}
		
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
	
	static void transferSupplies() throws GameActionException
	{
		if (myType == RobotType.MINER || myType == RobotType.TANK)
			return;
		
		// buildings transfer to buildings or to miners and tanks
		RobotInfo[] robots = rc.senseNearbyRobots(myLocation,GameConstants.SUPPLY_TRANSFER_RADIUS_SQUARED,myTeam);
		
		
		
		for (RobotInfo ri : robots)
		{
			// transfer max supply to tanks
			if (ri.type == RobotType.TANK)
			{
				rc.transferSupplies((int)rc.getSupplyLevel(), ri.location);
				return;
			}
			if (ri.type == RobotType.MINER)
			{
				int supplymissing = 300-(int)ri.supplyLevel;
				if (supplymissing > 0)
				{
					rc.transferSupplies(supplymissing, ri.location);
					return;
				}
			}
			// building-to-building transfer, just diffuse
			if (ri.type.ordinal() <= RobotType.AEROSPACELAB.ordinal() && rand.nextInt(10) < 3)
			{
				double supplydelta = (rc.getSupplyLevel() - ri.supplyLevel)/2;
				if (supplydelta > 0)
				{
					rc.transferSupplies((int)supplydelta, ri.location);
					return;
				}
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
		//gridInfo &= 65535;
		//gridInfo &= ~RobotPlayer.GRID_CC_MASK;
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
			//System.out.println("Initial gridID set to " + nextid + " for " + gridIndex);
			//RobotPlayer.debug_assert((gridInfo>>16)==0,"New block on bad cell");
			if ((gridInfo>>16)>0)
				return;
			
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
		MapLocation mid = getCenter();
		if (Math.abs(mid.x-loc.x)>RobotPlayer.GRID_SPC/2 || Math.abs(mid.y-loc.y)>RobotPlayer.GRID_SPC/2)
			return false;
		int maybe = getFastMaybes();
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