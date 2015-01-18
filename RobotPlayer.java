package grid366;

import battlecode.common.*;

import java.util.*;

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
	
	static MapLocation squadTarget;
	
	static MapLocation myLocation;
	static Direction facing;
	static int myGridX;
	static int myGridY;
	static int myGridInd;
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
	static int mapMinX;
	static int mapMinY;
	static int mapMaxX;
	static int mapMaxY;
	// minimum and maximum x/y coords of grid elements
	static int gridMinX;
	static int gridMinY;
	static int gridMaxX;
	static int gridMaxY;
	// and the minimum number of grid cells we may potentially have
	static int gridMinNum;
	
	static Random rand;
	static Direction[] directions = {Direction.NORTH, Direction.NORTH_EAST, Direction.EAST, Direction.SOUTH_EAST, Direction.SOUTH, Direction.SOUTH_WEST, Direction.WEST, Direction.NORTH_WEST};
	static int myRounds = 0;
	
	
	// sizes of all of our arrays
	static final int MAX_SQUADS = 16;
	
	static final int CID_NUM = 4000;
	static final int GAMEID_NUM = 60000;
	
	// grid, not map
	// how many map elements per grid element?
	static final int GRID_SPC = 9;
	// and what is required to sense the whole grid?
	static final int GRID_SENSE = 32;
	// max possible extents in one direction
	// needs to be at least 240/GRID_SPC
	static final int GRID_DIM = 252/GRID_SPC;
	// and how many elements in base grid. 784 total, of which we will use at most 196
	static final int GRID_NUM = GRID_DIM*GRID_DIM;
	// offset to get to upper-right corner of grid
	static final int GRID_OFFSET = GRID_DIM*GRID_SPC/2;
	
	// is this in the grid list?
	static final int STATUS_USING = (1<<0);
	// have we been there?
	static final int STATUS_SEEN = (1<<9);
	// which directions is this cell connected to others?
	static final int STATUS_UNCONN_NORTH = (1<<1);
	static final int STATUS_UNCONN_SOUTH = (1<<2);
	static final int STATUS_UNCONN_EAST = (1<<3);
	static final int STATUS_UNCONN_WEST = (1<<4);
	static final int STATUS_KNOW_NORTH = (1<<5);
	static final int STATUS_KNOW_SOUTH = (1<<6);
	static final int STATUS_KNOW_EAST = (1<<7);
	static final int STATUS_KNOW_WEST = (1<<8);
	static final int STATUS_KNOW_ALL = STATUS_KNOW_NORTH|STATUS_KNOW_SOUTH|STATUS_KNOW_EAST|STATUS_KNOW_WEST;
	
	static int curChan = 0;
	
	/* Contig ID counting, to use for enemies and friends */
	static final int nextCIDChan = curChan++;
	static final int cidBase = curChan; static {curChan += CID_NUM;}
		
	/* Map info */
	static final int mapSymmetryChan = curChan++;
	static final int mapExtentsChan = curChan++;
	
	/* Grid info */
	// list of grid indices that we have explored
	static final int gridListCountChan = curChan++;
	static final int gridListBase = curChan; static {curChan+=GRID_NUM/4;}
	// map list of basic grid square status
	static final int gridStatusBase = curChan; static {curChan+=GRID_NUM;}
	// lower 16 bits are current accumulation round
	// upper 16 are when the values in the accumulators below were saved
	//static final int gridTickBase = curChan; static {curChan+=GRID_NUM;}
	
	// these are calculated in a distributed fashion 
	static final int gridFriendBase = curChan; static {curChan+=GRID_NUM;}
	static final int gridEnemyBase = curChan; static {curChan+=GRID_NUM;}
	// diffusion values for frienemy potential
	// upper bits are "distance from friendly unit"
	static final int gridPotentialBase = curChan; static {curChan+=GRID_NUM;}

	// pointer to which grid cells we're calculating the above
	static final int gridUpdatePtrChan = curChan++;
	static final int gridPotentialPtrChan = curChan++;
	static final int gridConnectivityPtrChan = curChan++;
	
	
	static final int gridOreBase = curChan; static {curChan+=GRID_NUM;}
	
	//static MapLocation[] gridCenters = new MapLocation[GRID_NUM];
	static int[] foo = new int[8000];
	
	/* Squad task defines */
	static final int squadTaskBase = curChan; static {curChan+=MAX_SQUADS;}
	
	/* Squad target defines */
	static final int squadTargetBase = curChan; static {curChan+=MAX_SQUADS;}
	
	/* Squad unit counts */
	static final int squadUnitsBase = curChan; static {curChan+=MAX_SQUADS;}
	
	/* Next squad channel */
	static final int nextSquadChan = curChan; static {curChan+=MAX_SQUADS;}
	
	
	static int bestMineScoreChan = curChan++;
	static int bestMineXChan = curChan++;
	static int bestMineYChan = curChan++;

	// Adjustable parameters
    static int numBeavers = 8;
    static int numMinerFactories = 20;
    static int numMiners = 100;
    static int numBarracks = 0;
    static int numSoldiers = 0;
    static int numHelipads = 15;
	static int numSupplyDepots = 3;
	static int numTankFactories = 1;    
	
	
	static RobotConsts Consts = new RobotConsts();
	
	// HQ-specific
	static enum SquadState
	{
		RALLY,		// fewer than SQUAD_NUM units, defensive
		ATTACK,		// target a thing and kill it
		HARASS		// move around the back of the map and pick off targets
	}
	static MapLocation[] squadTargets = new MapLocation[MAX_SQUADS];
	static SquadState[] squadStates = new SquadState[MAX_SQUADS];
	static int[] squadCounts = new int[MAX_SQUADS];
	static int lastTowers = 0;
	
	static MapLocation[] enemyBuildings;
	static double[] enemyHP;
	
	static final int SQUAD_UNITS = 16;
	static final int HARASS_UNITS = 4;
	static final int HARASS_SQUADS = 2;

	static double lastOre = 0;
	static double curOre = 0;
	
	
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
	}
	

	public static void run(RobotController robotc)
	{
		rc = robotc;
		
		//while (lastTowers == 0)
		//	rc.yield();
		
		// Initialization code
		try {
			
			init();
			
			switch (myType)
			{
			case HQ:
				System.out.println(curChan + " channels in use");
				analyzeMap();
				
				Arrays.fill(squadStates, SquadState.RALLY);
				enemyBuildings = getBuildings(myTeam.opponent());
				enemyHP = new double[enemyBuildings.length];
				for (int i=0; i<enemyHP.length; i++)
					enemyHP[i] = 1200 - 1.0/myHQ.distanceSquaredTo(enemyBuildings[i]);
				break;
			case BEAVER:
				facing = rc.getLocation().directionTo(myHQ).opposite();
				break;
			case MINER:
				break;
			case DRONE:
				// get the next squad, as assigned by the HQ
				int nextSquad = rc.readBroadcast(nextSquadChan);
				// save the lowest byte
				mySquad = nextSquad & 255;
				// and write it back
				//rc.broadcast(nextSquadChan, nextSquad >> 8);
				break;
			}
		} catch (Exception e) {
			System.out.println("Initialization exception");
			e.printStackTrace();
		}
		
		while(true) {
			
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
			}
			
			lastOre = curOre;
			
			try {
				int clockcyc = Clock.getRoundNum() % 3;
				switch (clockcyc)
				{
				case 0:
					while (Clock.getBytecodesLeft() > 1500)
						gridUpdate();
					break;
				case 1:
					while (Clock.getBytecodesLeft() > 1000)
						gridConnectivity();
					break;
				case 2:
					while (Clock.getBytecodesLeft() > 400)
						gridDiffuse();
					break;
				}
			} catch (Exception e) {
				System.out.println("Grid exception");
				e.printStackTrace();
			}
			
			if (Clock.getBytecodesLeft() < 600)
			{
				rc.yield();
				continue;
			}
			
			try {
				transferSupplies();
			} catch (Exception e) {
				System.out.println("Supply exception");
				//e.printStackTrace();
			}
			
			rc.yield();
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

		curOre = rc.getTeamOre();
		
		if (justMoved)
		{			
			// update internal grid coordinate values
			myGridX = (GRID_OFFSET+myLocation.x-center.x+GRID_SPC/2)/GRID_SPC;
			myGridY = (GRID_OFFSET+myLocation.y-center.y+GRID_SPC/2)/GRID_SPC;
			myGridInd = myGridY*GRID_DIM + myGridX;
			
			if (myGridInd != lastGridInd)
			{
			
				int gridstatus = rc.readBroadcast(gridStatusBase+myGridInd);
				// has this grid cell been added yet?
				if (gridstatus == 0)
				{
					// only add it if array has been initialized
					// no double adding...
					int gridcount = rc.readBroadcast(gridListCountChan);
					if (gridcount >= gridMinNum)
					{
						gridstatus |= STATUS_USING|STATUS_SEEN;
						// write that we have entered it
						rc.broadcast(gridStatusBase+myGridInd, gridstatus);
						// and add it to global grid list
						rc.broadcast(gridListBase+gridcount,myGridInd);
						rc.broadcast(gridListCountChan,gridcount+1);
					}
				}
				// or we just haven't been here yet
				if ((gridstatus&STATUS_SEEN) == 0)
				{
					rc.broadcast(gridStatusBase+myGridInd, gridstatus|STATUS_SEEN);
				}
				
				myGridCenter = gridCenter(myGridInd);
				// both coords are inclusive
				myGridUL = myGridCenter.add(-GRID_SPC/2,-GRID_SPC/2);
				myGridBR = myGridCenter.add(GRID_SPC/2,GRID_SPC/2);
				
				lastGridInd = myGridInd;
			}			
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
	static void analyzeMap() throws GameActionException
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
		
		mapMinX = Math.min(myblds[0].x,enblds[0].x);
		mapMaxX = Math.max(myblds[myblds.length-1].x,enblds[enblds.length-1].x);
		
		Arrays.sort(myblds, new Comparator<MapLocation>() {
		    public int compare(MapLocation ml1, MapLocation ml2) {
	 	      return Integer.compare(ml1.y,ml2.y);}});
		Arrays.sort(enblds, new Comparator<MapLocation>() {
		    public int compare(MapLocation ml1, MapLocation ml2) {
	 	      return Integer.compare(ml1.y,ml2.y);}});

		mapMinY = Math.min(myblds[0].y,enblds[0].y);
		mapMaxY = Math.max(myblds[myblds.length-1].y,enblds[enblds.length-1].y);

		// and pack it into map extents broadcast
		mapMinX -= center.x;
		mapMaxX -= center.x;
		mapMinY -= center.y;
		mapMaxY -= center.y;
		int mapextents = (mapMinX&255) | ((mapMaxX&255)<<8) | ((mapMinY&255)<<16) | ((mapMaxY&255)<<24);
		rc.broadcast(mapExtentsChan, mapextents);
		
		getExtents();
		
		
		// now check each tower on a case-by-case basis
		/*
		switch (mapSymmetry)
		{
		case 0:
			// any not diagonally symmetric?
			// (aka x-coord diff != y-coord diff)
			// so sort by diff of x and y coords
			// (+1, +1) diagonal reflection test
			if ((myblds[0].x - enblds[0].x) == (myblds[0].y - enblds[0].y))
			{
				Arrays.sort(myblds, new Comparator<MapLocation>() {
				    public int compare(MapLocation ml1, MapLocation ml2) {
			 	      return Integer.compare(ml1.x+ml1.y,ml2.x+ml2.y);}});
				Arrays.sort(enblds, new Comparator<MapLocation>() {
				    public int compare(MapLocation ml1, MapLocation ml2) {
			 	      return Integer.compare(ml1.x+ml1.y,ml2.x+ml2.y);}});
				for (int i=1; i<myblds.length; i++)
				{
					// rotation test
					if (myblds[i].x != enblds[i].x)
					{
						mapSymmetry = 3;
						break;
					}
				}
			}
			else
			{
				Arrays.sort(myblds, new Comparator<MapLocation>() {
				    public int compare(MapLocation ml1, MapLocation ml2) {
			 	      return Integer.compare(ml1.x+ml1.y,ml2.x+ml2.y);}});
				Arrays.sort(enblds, new Comparator<MapLocation>() {
				    public int compare(MapLocation ml1, MapLocation ml2) {
			 	      return Integer.compare(ml1.x+ml1.y,ml2.x+ml2.y);}});
				for (int i=1; i<myblds.length; i++)
				{
					// rotation test
					if (myblds[i].x != enblds[i].x)
					{
						mapSymmetry = 3;
						break;
					}
				}
			}

			break;
		case 1:
			// any not horizontally symmetric?
			// (aka any x-coords not the same?)
			// so sort by x coords and compare
			Arrays.sort(myblds, new Comparator<MapLocation>() {
			    public int compare(MapLocation ml1, MapLocation ml2) {
		 	      return Integer.compare(ml1.x,ml2.x);}});
			Arrays.sort(enblds, new Comparator<MapLocation>() {
			    public int compare(MapLocation ml1, MapLocation ml2) {
		 	      return Integer.compare(ml1.x,ml2.x);}});

			
			for (int i=1; i<myblds.length; i++)
			{
				// rotation test
				if (myblds[i].x != enblds[i].x)
				{
					mapSymmetry = 3;
					break;
				}
			}
			break;
		case 2:
			// any not vertically symmetric?
			// (aka any y-coords not the same?)
			// so sort by y coords and compare
			Arrays.sort(myblds, new Comparator<MapLocation>() {
			    public int compare(MapLocation ml1, MapLocation ml2) {
		 	      return Integer.compare(ml1.y,ml2.y);}});
			Arrays.sort(enblds, new Comparator<MapLocation>() {
			    public int compare(MapLocation ml1, MapLocation ml2) {
		 	      return Integer.compare(ml1.y,ml2.y);}});

			
			for (int i=1; i<myblds.length; i++)
			{
				// rotation test
				if (myblds[i].y != enblds[i].y)
				{
					mapSymmetry = 3;
					break;
				}
			}
			break;
		}
		*/
	}
	
	static void getExtents() throws GameActionException
	{
		// unpack map extents
		int mapextents = rc.readBroadcast(mapExtentsChan);
		mapMinX = (int)(byte)((mapextents)&255);
		mapMaxX = (int)(byte)((mapextents>>8)&255);
		mapMinY = (int)(byte)((mapextents>>16)&255);
		mapMaxY = (int)(byte)((mapextents>>24)&255);
		
		// grid extents x/y:
		gridMinX = (mapMinX+GRID_OFFSET+GRID_SPC/2)/GRID_SPC;
		gridMinY = (mapMinY+GRID_OFFSET+GRID_SPC/2)/GRID_SPC;
		gridMaxX = (mapMaxX+GRID_OFFSET+GRID_SPC/2)/GRID_SPC;
		gridMaxY = (mapMaxY+GRID_OFFSET+GRID_SPC/2)/GRID_SPC;
		// and minimum number in order to populate whole map
		gridMinNum = (gridMaxX-gridMinX+1)*(gridMaxY-gridMinY+1);
		System.out.println("Minimum grid size: " + gridMinNum);
		//System.out.println("Map extents: "+mapMinX+","+mapMaxX+","+mapMinY+","+mapMaxY);
		
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

			int nextSquad = -1;
			// figure out squad state updates
			for (int i=0; i<MAX_SQUADS; i++)
			{
				int squadcount = rc.readBroadcast(squadUnitsBase + i);
				
				if (squadcount >> 16 == Clock.getRoundNum() - 1)
				{
					// units alive, get number of units
					squadcount = squadcount & 255;
					squadCounts[i] = squadcount;
					// if we are rallying and full
					int targetNum = SQUAD_UNITS;
					if (i<HARASS_SQUADS) targetNum = HARASS_UNITS;
					
					if (squadStates[i] == SquadState.RALLY)
					{
						if (squadCounts[i] >= targetNum)
						{
							if (((rc.readBroadcast(squadTaskBase+i) >> 8) & 255) >= targetNum) {
								// this squad is now live, and is all near target, don't need more units
								squadStates[i] = (i<HARASS_SQUADS)?SquadState.HARASS:SquadState.ATTACK;
								doSquadTarget(i);
							}
						}
						else
						{
							// it needs more units, target stays, but only if we're rallying
							if (nextSquad == -1) nextSquad = i;
						}
					}
					else // see if other types reached their target
					{
						if (rc.canSenseLocation(squadTargets[i]))
						{
							RobotInfo ri = rc.senseRobotAtLocation(squadTargets[i]);
							if (ri != null && ri.team == myTeam)
								doSquadTarget(i);
						}
					}
				}
				else
				{
					// no units reporting, all are dead
					squadCounts[i] = 0;
					squadStates[i] = SquadState.RALLY;
					if (nextSquad == -1) nextSquad = i;
					doSquadTarget(i);
				}
			}

			// and send out which squad to build to next
			if (nextSquad == -1)
				nextSquad = 0;
			rc.broadcast(nextSquadChan, nextSquad);
			
			
			
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
	
	// returns total (differential) HP near a location
	// (this is too slow, just replacing with building health for now)
	static double getHPnear(MapLocation loc) throws GameActionException
	{
		double HP = 0.0;
		
		//RobotInfo[] bots = rc.senseNearbyRobots(loc, 15, null);
		
		//for (RobotInfo b: bots)
		//	HP += (b.team == myTeam)?-b.health:b.health;
		
		if (!rc.canSenseLocation(loc))
			return 0;
		
		RobotInfo ri = rc.senseRobotAtLocation(loc);
		HP = ri.health;
		if (ri.supplyLevel > ri.type.supplyUpkeep*10)
			HP *= 2;
		
		return HP;
	}
	
	static MapLocation getRallyTarget() throws GameActionException
	{
		MapLocation[] buildings = getBuildings(myTeam);
		MapValue[] mvs = new MapValue[buildings.length];
		for (int i=0; i<buildings.length; i++)
			mvs[i] = new MapValue(buildings[i].x,buildings[i].y,getHPnear(buildings[i]));
		Arrays.sort(mvs);
		return new MapLocation(mvs[0].x,mvs[0].y);
	}
	
	static MapLocation updateEnemyBuildingHP() throws GameActionException
	{
		MapLocation[] buildings = getBuildings(myTeam.opponent());
		
		// check if any were destroyed
		if (buildings.length < enemyBuildings.length)
		{
			MapLocation[] oldbldgs = enemyBuildings;
			enemyBuildings = buildings;
			double[] newHP = new double[buildings.length];
			// and find which one(s), and remove it
			int j = 0;
			for (int i=0; i<enemyBuildings.length; i++)
			{
				if (!buildings[j].equals(enemyBuildings[i]))
					continue;
				newHP[j] = enemyHP[i];
				j++;
			}
			enemyHP = newHP;
		}
		
		// now see if we can sense any of them
		MapValue[] mvs = new MapValue[buildings.length];
		for (int i=0; i<buildings.length; i++)
		{
			//if (rc.canSenseLocation(buildings[i]))
			//	enemyHP[i] = getHPnear(buildings[i]);
			mvs[i] = new MapValue(buildings[i].x,buildings[i].y,enemyHP[i]);
		}
		Arrays.sort(mvs);
		
		return new MapLocation(mvs[0].x,mvs[0].y);
	}
	
	static void setSquadTarget(int squad, MapLocation target) throws GameActionException
	{
		int loc = ((target.y-center.y) << 16) + ((target.x-center.x) & 65535);
		rc.broadcast(squadTargetBase + squad, loc);
		squadTargets[squad] = target;
	}
	
	static MapLocation getHarassTarget()
	{
		return rc.senseEnemyHQLocation();
	}

	static void doSquadTarget(int squad) throws GameActionException
	{
		switch (squadStates[squad])
		{
		case RALLY:
			// go to the friendly tower with the least HP
			if (squad < HARASS_SQUADS)
				setSquadTarget(squad,rc.senseHQLocation());
			else
				setSquadTarget(squad,getRallyTarget());
			break;
		case HARASS:
			setSquadTarget(squad,getHarassTarget());
			break;
		case ATTACK:
			setSquadTarget(squad,updateEnemyBuildingHP());
			break;
		}
	}
	
	static void doTower()
	{
		try {
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
			spawnUnit(RobotType.SOLDIER);
		} catch (Exception e) {
			System.out.println("Barracks Exception");
			e.printStackTrace();
		}
	}

	
	static void doDrone()
	{
		try {
			updateSquadInfo();
			attackSomething();
			calcPotential();
			rc.setIndicatorString(0, "Drone: squad " + mySquad + ", target " + squadTarget);
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
			attackSomething();
			if (rc.isCoreReady()) {
				aggMove();
			}
		} catch (Exception e) {
			System.out.println("Soldier Exception");
			e.printStackTrace();
		}
	}

	static void doBeaver()
	{
		try {
			if(Clock.getRoundNum() < 50)
			{
				moveStraight();
			}
			else
			{
				RobotInfo[] ourTeam = rc.senseNearbyRobots(100000, rc.getTeam());
				int n = 0; // current number of miner factories
				int m = 0; // current number of barracks
				int o = 0; // current number of helipads
				int s = 0; // current number of supply depots
				int tf = 0;
				for(RobotInfo ri: ourTeam){ // count up miner factories
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
					}
				}
				// only build additional miner factories if we have more than 1
				// only build additional miner factories if we have more than 1
				if(s<numSupplyDepots && s < o && s < n)
				{
					buildUnit(RobotType.SUPPLYDEPOT);
				}
				else if(n < numMinerFactories && (n == o)) 
				{
					buildUnit(RobotType.MINERFACTORY);
				} 
				else if(m<numBarracks)
				{
					buildUnit(RobotType.BARRACKS);
				}
				else if(tf<1 && m>0){
					buildUnit(RobotType.TANKFACTORY);
				}
				else if(o<numHelipads)
				{
					buildUnit(RobotType.HELIPAD);
				}
				else if(tf<numTankFactories  && m>0)
				{
					buildUnit(RobotType.TANKFACTORY);
				}
				attackSomething();
				mineAndMove();
			}
		} catch (Exception e) {
			System.out.println("Beaver Exception");
			e.printStackTrace();
		}
	}
	
	static void doMiner()
	{
		try {
			attackSomething();
			mineAndMove();
		} catch (Exception e) {
			System.out.println("Miner Exception");
			e.printStackTrace();
		}
	}
	

	// This method updates all squad-specific info
	static void updateSquadInfo() throws GameActionException
	{
		// accumulate squad unit counts
		int squadUnits = rc.readBroadcast(squadUnitsBase + mySquad);
		if (Clock.getRoundNum() != (squadUnits >> 16))
			squadUnits = Clock.getRoundNum() << 16;
		squadUnits++;
		rc.broadcast(squadUnitsBase + mySquad, squadUnits);
		
		// and update squad targets
		int st = rc.readBroadcast(squadTargetBase + mySquad);
		squadTarget = new MapLocation(((int)(short)st) + center.x, (st >> 16) + center.y);
		//System.out.println(mySquad + ", " + squadTarget + " " + center + " " + (int)(st & 65535) + " " + ((st >>> 16) - MAP_OFFSET));
		
		int squadTask = rc.readBroadcast(squadTaskBase + mySquad);
		if (Clock.getRoundNum() != (squadTask >> 16))
			squadTask = (Clock.getRoundNum() << 16) + ((squadTask & 255) << 8);
		
		if (myLocation.distanceSquaredTo(squadTarget) < 81)
			squadTask++;
		
		rc.broadcast(squadTaskBase + mySquad, squadTask);
	}
		
	static MapLocation gridCenter(int gridX, int gridY)
	{
		return new MapLocation(gridX*GRID_SPC+center.x-GRID_OFFSET,gridY*GRID_SPC+center.y-GRID_OFFSET);
	}
	
	static MapLocation gridCenter(int gridInd)
	{
		return new MapLocation((gridInd%GRID_DIM)*GRID_SPC+center.x-GRID_OFFSET,(gridInd/GRID_DIM)*GRID_SPC+center.y-GRID_OFFSET);
	}
	
	static void gridUpdate() throws GameActionException
	{
		// 100 bytecodes for preamble
		// 100 bytecodes for senseRobots
		// 100 bytecodes for broadcasts
		
		int bc = Clock.getBytecodeNum();

		int ptr = rc.readBroadcast(gridUpdatePtrChan);
		int gridn = rc.readBroadcast(gridListCountChan);
		int gridind = rc.readBroadcast(gridListBase+ptr);
		
		if (ptr == 0)
			System.out.println("Grid update @ " + Clock.getRoundNum() + ", Grid Size: " + gridn);
		
		// check if we've added all of the map-required grid elements yet or not
		// if not, only do that, and nothing else
		if (gridn < gridMinNum)
		{
			// just add one, then carry on
			// horizontal dimension of minimum grid
			int gridd = (gridMaxX-gridMinX+1);
			// get actual x,y coords
			int gridy = (gridn/gridd)+gridMinY;
			int gridx = (gridn%gridd)+gridMinX;
			// and add them
			gridind = gridx+gridy*GRID_DIM;
			
			// write that we are using it
			int gridstatus = rc.readBroadcast(gridStatusBase+gridind); 
			rc.broadcast(gridStatusBase+gridind, gridstatus|STATUS_USING);
			// and add it to global grid list
			int gridcount = rc.readBroadcast(gridListCountChan);
			rc.broadcast(gridListBase+gridcount,gridind);
			rc.broadcast(gridListCountChan,gridcount+1);
			return;
		}
		
		MapLocation loc = gridCenter(gridind);
		MapLocation ul = loc.add(-GRID_SPC/2,-GRID_SPC/2);
		MapLocation br = loc.add(GRID_SPC/2,GRID_SPC/2);
		
		// now sense frienemies in all grid cells
		RobotInfo[] bots = rc.senseNearbyRobots(loc,GRID_SENSE,null);
		
		int prevFriend = rc.readBroadcast(gridFriendBase+gridind);
		int prevEnemy = rc.readBroadcast(gridEnemyBase+gridind);
		
		int gridFriend = 0;
		int gridEnemy = 0;
		
		for (RobotInfo ri : bots)
		{
			// not in this grid cell?
			if ((ri.location.x < ul.x) || (ri.location.x > br.x) ||
					(ri.location.y < ul.y) || (ri.location.y > br.y))
				continue;
			
			int strength = getRobotStrength(ri.health,ri.supplyLevel,ri.type);
			if (ri.team == myTeam)
				gridFriend += strength;
			else
				gridEnemy += strength;
		}
		
		// decay enemy, if new sighting is lower
		if (prevEnemy > gridEnemy)
			gridEnemy = Math.max(0, prevEnemy-1);
		
		if (gridFriend != prevFriend)
			rc.broadcast(gridFriendBase+gridind,gridFriend);
		if (gridEnemy != prevEnemy)
			rc.broadcast(gridEnemyBase+gridind,gridEnemy);
		
		rc.broadcast(gridUpdatePtrChan,(ptr+1)%gridn);
		
		//System.out.println("Update time (" + bots.length + "): " + (Clock.getBytecodeNum()-bc));
	}
	
	static void gridDiffuse() throws GameActionException
	{
		int bc = Clock.getBytecodeNum();
		int ptr = rc.readBroadcast(gridPotentialPtrChan);
		int gridn = rc.readBroadcast(gridListCountChan);
		int gridind = rc.readBroadcast(gridListBase+ptr);
		
		if (gridn == 0)
			return;
		
		if (ptr == 0)
			System.out.println("Diffuse update @ " + Clock.getRoundNum());
		
		int gridX = gridind%GRID_DIM;
		int gridY = gridind/GRID_DIM;
		
		// contains connectivity information
		int gridstatus = rc.readBroadcast(gridStatusBase+gridind);
		
		
		// # of grid squares to closest friendly unit
		int frienddist;
		
		// source term for potential is difference between friend and enemy squares
		int source = rc.readBroadcast(gridFriendBase+gridind);
		if (source > 0)
		{
			frienddist = -1;
			// there are friendly units, we need lots of guys here
			source = source - 4*rc.readBroadcast(gridEnemyBase+gridind);
		}
		else
		{
			frienddist = 1000;
			// no friendly units
			source = source - rc.readBroadcast(gridEnemyBase+gridind);
		}
		
		int gridval = rc.readBroadcast(gridPotentialBase+gridind);
		
		// new value, computed from average of surrounding squares
		int newval = source;
		
		if (gridX>0)
		{
			int val = rc.readBroadcast(gridPotentialBase+gridind-1);
//			frienddist = Math.min(frienddist, val >>> 16);
			if ((gridstatus&STATUS_UNCONN_WEST)==0)
				newval += val;
			else
				newval += gridval;
		}
		else
		{
			newval += gridval;
		}
		
		if (gridX<GRID_DIM-1)
		{
			int val = rc.readBroadcast(gridPotentialBase+gridind+1);
//			frienddist = Math.min(frienddist, val >>> 16);
			if ((gridstatus&STATUS_UNCONN_EAST)==0)
				newval += val;
			else
				newval += gridval;
		}
		else
		{
			newval += gridval;
		}
		
		if (gridY>0)
		{
			int val = rc.readBroadcast(gridPotentialBase+gridind-GRID_DIM);
//			frienddist = Math.min(frienddist, val >>> 16);
			if ((gridstatus&STATUS_UNCONN_NORTH)==0)
				newval += val;
			else
				newval += gridval;
		}
		else
		{
			newval += gridval;
		}
		
		if (gridY<GRID_DIM-1)
		{
			int val = rc.readBroadcast(gridPotentialBase+gridind+GRID_DIM);
//			frienddist = Math.min(frienddist, val >>> 16);
			if ((gridstatus&STATUS_UNCONN_SOUTH)==0)
				newval += val;
			else
				newval += gridval;
		}
		else
		{
			newval += gridval;
		}
		
		// clear the upper bits first
		newval = newval/4;
		// increment this to denote our increased distance (or goes to 0 if we have friendlies)
//		frienddist++;
		
//		rc.broadcast(gridPotentialBase+gridind, newval | (frienddist<<16));
		rc.broadcast(gridPotentialBase+gridind, newval);
		rc.broadcast(gridPotentialPtrChan,(ptr+1)%gridn);
		//System.out.println("Diffuse time: " + (Clock.getBytecodeNum()-bc));
	}
	
	static void gridConnectivity() throws GameActionException
	{
		int bc = Clock.getBytecodeNum();
		
		int ptr = rc.readBroadcast(gridConnectivityPtrChan);
		int gridn = rc.readBroadcast(gridListCountChan);
		int gridind = rc.readBroadcast(gridListBase+ptr);
		int gridstatus = rc.readBroadcast(gridStatusBase+gridind);
		
		if (gridn == 0)
			return;
		
		if (ptr == 0)
			System.out.println("Connectivity update @ " + Clock.getRoundNum());
		
		// have we visited? if not, don't bother
		if ((gridstatus&STATUS_SEEN) == 0 || (gridstatus&STATUS_KNOW_ALL) == STATUS_KNOW_ALL)
		{
			// move on to the next one
			rc.broadcast(gridConnectivityPtrChan,(ptr+1)%gridn);
			return;
		}
		
		int gridX = gridind%GRID_DIM;
		int gridY = gridind/GRID_DIM;
		
		MapLocation loc = gridCenter(gridind);
		// go through and check each ordinal direction and if we have sensed them
		int dir = rand.nextInt(4);
		// pick one we haven't figured out yet
		while ((gridstatus&(STATUS_KNOW_NORTH<<dir))>0)
			dir = rand.nextInt(4);

		int notconn = 0;
		boolean connected = false;

		switch (dir)
		{
		case 0:
			// north
			for (int i=-GRID_SPC/2; i<=GRID_SPC/2; i++)
			{
				TerrainTile a = rc.senseTerrainTile(loc.add(i,-GRID_SPC/2));
				TerrainTile b = rc.senseTerrainTile(loc.add(i,-GRID_SPC/2-1));
				if (a == TerrainTile.NORMAL && b == TerrainTile.NORMAL)
				{
					// definitely connected
					connected = true;
					break;
				}
				if(a == TerrainTile.VOID || a == TerrainTile.OFF_MAP || b == TerrainTile.VOID || b == TerrainTile.OFF_MAP)
				{
					notconn++;
				}
			}
			// we decided whether it's connected one way or another
			if (connected==true || notconn==GRID_SPC)
			{
				rc.broadcast(gridStatusBase+gridind,gridstatus|STATUS_KNOW_NORTH|(connected?0:STATUS_UNCONN_NORTH));
				// and update the partner's link
				if (gridY>0)
					rc.broadcast(gridStatusBase+gridind-GRID_DIM,
							rc.readBroadcast(gridStatusBase+gridind-GRID_DIM)|STATUS_KNOW_SOUTH|(connected?0:STATUS_UNCONN_SOUTH));
			}
			break;
		case 1:
			// south
			for (int i=-GRID_SPC/2; i<=GRID_SPC/2; i++)
			{
				TerrainTile a = rc.senseTerrainTile(loc.add(i,GRID_SPC/2));
				TerrainTile b = rc.senseTerrainTile(loc.add(i,GRID_SPC/2+1));
				if (a == TerrainTile.NORMAL && b == TerrainTile.NORMAL)
				{
					// definitely connected
					connected = true;
					break;
				}
				if(a == TerrainTile.VOID || a == TerrainTile.OFF_MAP || b == TerrainTile.VOID || b == TerrainTile.OFF_MAP)
				{
					notconn++;
				}
			}
			// we decided whether it's connected one way or another
			if (connected==true || notconn==GRID_SPC)
			{
				rc.broadcast(gridStatusBase+gridind,gridstatus|STATUS_KNOW_SOUTH|(connected?0:STATUS_UNCONN_SOUTH));
				// and update the partner's link
				if (gridY<GRID_DIM-1)
					rc.broadcast(gridStatusBase+gridind+GRID_DIM,
							rc.readBroadcast(gridStatusBase+gridind+GRID_DIM)|STATUS_KNOW_NORTH|(connected?0:STATUS_UNCONN_NORTH));
			}
			break;
		case 2:
			// east
			for (int i=-GRID_SPC/2; i<=GRID_SPC/2; i++)
			{
				TerrainTile a = rc.senseTerrainTile(loc.add(GRID_SPC/2,i));
				TerrainTile b = rc.senseTerrainTile(loc.add(GRID_SPC/2+1,i));
				if (a == TerrainTile.NORMAL && b == TerrainTile.NORMAL)
				{
					// definitely connected
					connected = true;
					break;
				}
				if(a == TerrainTile.VOID || a == TerrainTile.OFF_MAP || b == TerrainTile.VOID || b == TerrainTile.OFF_MAP)
				{
					notconn++;
				}
			}
			// we decided whether it's connected one way or another
			if (connected==true || notconn==GRID_SPC)
			{
				rc.broadcast(gridStatusBase+gridind,gridstatus|STATUS_KNOW_EAST|(connected?0:STATUS_UNCONN_EAST));
				// and update the partner's link
				if (gridX<GRID_DIM-1)
					rc.broadcast(gridStatusBase+gridind+1,
							rc.readBroadcast(gridStatusBase+gridind+1)|STATUS_KNOW_WEST|(connected?0:STATUS_UNCONN_WEST));
			}
			break;
		case 3:
			// west
			for (int i=-GRID_SPC/2; i<=GRID_SPC/2; i++)
			{
				TerrainTile a = rc.senseTerrainTile(loc.add(-GRID_SPC/2,i));
				TerrainTile b = rc.senseTerrainTile(loc.add(-GRID_SPC/2+1,i));
				if (a == TerrainTile.NORMAL && b == TerrainTile.NORMAL)
				{
					// definitely connected
					connected = true;
					break;
				}
				if(a == TerrainTile.VOID || a == TerrainTile.OFF_MAP || b == TerrainTile.VOID || b == TerrainTile.OFF_MAP)
				{
					notconn++;
				}
			}
			// we decided whether it's connected one way or another
			if (connected==true || notconn==GRID_SPC)
			{
				rc.broadcast(gridStatusBase+gridind,gridstatus|STATUS_KNOW_WEST|(connected?0:STATUS_UNCONN_WEST));
				// and update the partner's link
				if (gridX>0)
					rc.broadcast(gridStatusBase+gridind-1,
							rc.readBroadcast(gridStatusBase+gridind-1)|STATUS_KNOW_EAST|(connected?0:STATUS_UNCONN_EAST));

			}
			break;
		}
		
		rc.broadcast(gridConnectivityPtrChan,(ptr+1)%gridn);
		
		//System.out.println("Connectivity time: " + (Clock.getBytecodeNum()-bc));
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
		if (squadTarget != null && rc.canAttackLocation(squadTarget))
		{
			RobotInfo ri = rc.senseRobotAtLocation(squadTarget);
			if (ri != null && ri.team == myTeam.opponent())
			{
				rc.attackLocation(squadTarget);
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
			TerrainTile tile = rc.senseTerrainTile(m);
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

        facing = rc.getLocation().directionTo(squadTarget);
        
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
	static void calcPotential() throws GameActionException
	{
		float forceX = 0.0f;
		float forceY = 0.0f;
		
		// get dem robots
		RobotInfo[] friendlyRobots = rc.senseNearbyRobots(myType.sensorRadiusSquared,myTeam);
		RobotInfo[] enemyRobots = rc.senseNearbyRobots(myType.sensorRadiusSquared,myTeam.opponent());
		
		// attracted to squad target, far away
		forceX = (squadTarget.x - myLocation.x);
		forceY = (squadTarget.y - myLocation.y);
		
		float f = (float)Math.sqrt(forceX*forceX + forceY*forceY);
		forceX /= f;
		forceY /= f;
		
		// forceXY is now normalized distance to squadTarget
		
		double friendlyHP = 0;
		
		// don't get too close to friendly things
		for (RobotInfo bot : friendlyRobots)
		{
			friendlyHP += bot.health;
			//if (bot.type.attackRadiusSquared > 0)
			//	continue;
			
			int vecx = bot.location.x - myLocation.x;
			int vecy = bot.location.y - myLocation.y;
			int d2 = bot.location.distanceSquaredTo(myLocation);
			float id = Consts.invSqrt[d2];
			
			float kRepel = 0.1f;
			
			// just repel based on distance, at close range
			forceX += -kRepel*id*id*vecx;
			forceY += -kRepel*id*id*vecy;
		}
				
		for (RobotInfo bot : enemyRobots)
		{
			// dangerous ones, dealt with below
			if (bot.type == RobotType.TOWER || bot.type == RobotType.HQ)
				continue;
			
			int vecx = bot.location.x - myLocation.x;
			int vecy = bot.location.y - myLocation.y;
			int d2 = bot.location.distanceSquaredTo(myLocation);
			
			float id = Consts.invSqrt[d2];
			
			float kRepel = -8.0f;
			
			// attract if it can't fight back
			if (bot.type.attackRadiusSquared == 0)
				kRepel = 5.0f;
			
			// within attack range, repel
			// (difference in distances)
			float dattack = Consts.sqrt[bot.type.attackRadiusSquared] - Consts.sqrt[d2] + 2.0f;
			if (dattack > 0)
			{
				kRepel /= (dattack*dattack);
			}
			else if (mySquad < HARASS_SQUADS)
			{
				// outside of attack range, weakly attrack (if harassing)
				if (bot.type.attackRadiusSquared < myType.attackRadiusSquared + 5)
				{
					kRepel = 5.0f;
				}
			}

			forceX += kRepel*id*vecx;
			forceY += kRepel*id*vecy;
		}
		
		boolean hasSquad = friendlyRobots.length > 8 || ((rc.readBroadcast(squadTaskBase+mySquad) >> 8) & 255) > 8;
		
		MapLocation[] towers = getBuildings(myTeam.opponent());
		
		for (MapLocation tower : towers)
		{
			int d2 = tower.distanceSquaredTo(myLocation);
			// check if it's in range
			if (d2 > RobotType.TOWER.attackRadiusSquared + 20)
				continue;
			
			float id = Consts.invSqrt[d2];

			int vecx = tower.x - myLocation.x;
			int vecy = tower.y - myLocation.y;
			
			float kRepel = -20.0f;

			// if we can kill it easily, we attract
			if (rc.canSenseLocation(tower) && tower.equals(squadTarget))
			{
				RobotInfo ti = rc.senseRobotAtLocation(tower);
				if (ti.health < friendlyHP*10)
					kRepel = 50;
			}
			
			// otherwise, only get attracted to target tower, remain repelled from others
			if (hasSquad && tower.equals(squadTarget))
				kRepel = 50;
			
			// within attack range, repel
			// (difference in distances)
			float offset = 1.5f;
			// steer very clear of towers
			if (mySquad < HARASS_SQUADS) offset = 5.5f;
			float dattack = Consts.sqrt[RobotType.TOWER.attackRadiusSquared] - Consts.sqrt[d2] + offset;
			if (kRepel > 0)
			{// this is attractive, actually
				dattack = kRepel*id;
				rc.setIndicatorString(1,"Tower attraction");
			}
			else // and this means we are being repelled
			{
				dattack = kRepel*id/Math.max(dattack,1);
				rc.setIndicatorString(1,"Tower repulsion: " + dattack);
			}
			forceX += dattack*vecx;
			forceY += dattack*vecy;
		}
		// get direction of force
		MapValue[] mvs = new MapValue[9];

		for (int i=0; i<9; i++)
			mvs[i] = new MapValue(Consts.senseLocsX[i],Consts.senseLocsY[i],forceX*Consts.senseLocsX[i] + forceY*Consts.senseLocsY[i]);
		
		//rc.setIndicatorLine(myLocation, new MapLocation(myLocation.x + (int)(forceX*10), myLocation.y + (int)(forceY*10)), 0, 255, 255);

		Arrays.sort(mvs);
		
		for (int i=8; i>0; i--)
		{
			// facing the wrong way, don't move at all
			if (mvs[i].value < 0)
				break;
			Direction newdir = myLocation.directionTo(mvs[i].offsetFrom(myLocation));
			if(rc.isCoreReady()&&rc.canMove(newdir))
			{
				rc.move(newdir);
			}
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
	
	static void buildUnit(RobotType type) throws GameActionException {
		if(rc.getTeamOre()>type.oreCost && rc.isCoreReady()) {
			
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

	
	static void spawnUnit(RobotType type) throws GameActionException {
		Direction randomDir = getRandomDirection();

		if(rc.isCoreReady() && rc.canSpawn(randomDir, type))
		{
			rc.spawn(randomDir, type);
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
	
	
	
	static void debug_drawGridVals() throws GameActionException
	{
		for (int x=0; x<GRID_DIM; x++)
		{
			for (int y=0;y<GRID_DIM;y++)
			{
				int gridind = y*GRID_DIM+x;
				
				// not mapped yet
				int gridstatus = rc.readBroadcast(gridStatusBase+gridind); 
				if ((gridstatus&STATUS_USING)==0)
				//if ((gridstatus&STATUS_SEEN)==0)
					continue;
				
				MapLocation loc = gridCenter(gridind);
				int val = (rc.readBroadcast(gridPotentialBase+gridind));
				//int dist2friends = val >> 16;
				/*if (dist2friends > 2)
				{
					rc.setIndicatorDot(loc,255,255,0);
				}
				else*/
				if (val > 0)
				{
					// friendly-controlled, set blue
					if (val>255) val = 255;
					rc.setIndicatorDot(loc,0,0,val);
				}
				else
				{
					// enemy-controlled, set red
					val = -val;
					if (val>255) val = 255;
					rc.setIndicatorDot(loc,val,0,0);
				}
				
				// check connectivities
				if ((gridstatus&STATUS_KNOW_NORTH)>0)
				{
					if ((gridstatus&STATUS_UNCONN_NORTH)==0)
						rc.setIndicatorLine(loc,loc.add(0,-GRID_SPC),255,255,0);
					else
						rc.setIndicatorDot(loc.add(0,-1),255,255,0);
				}
				if ((gridstatus&STATUS_KNOW_SOUTH)>0)
				{
					if ((gridstatus&STATUS_UNCONN_SOUTH)==0)
						rc.setIndicatorLine(loc,loc.add(0,GRID_SPC),255,255,0);
					else
						rc.setIndicatorDot(loc.add(0,1),255,255,0);
				}
				if ((gridstatus&STATUS_KNOW_EAST)>0)
				{
					if ((gridstatus&STATUS_UNCONN_EAST)==0)
						rc.setIndicatorLine(loc,loc.add(GRID_SPC,0),255,255,0);
					else
						rc.setIndicatorDot(loc.add(1,0),255,255,0);
				}
				if ((gridstatus&STATUS_KNOW_WEST)>0)
				{
					if ((gridstatus&STATUS_UNCONN_WEST)==0)
						rc.setIndicatorLine(loc,loc.add(-GRID_SPC,0),255,255,0);
					else
						rc.setIndicatorDot(loc.add(-1,0),255,255,0);
				}

				
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
