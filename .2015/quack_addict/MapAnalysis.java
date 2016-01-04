package quack_addict;

import battlecode.common.*;

class BorderFollowResult {
    Direction wallHit = null;
    int numSquaresAdded = 0;
}

public class MapAnalysis extends Bot {
    static MapLocation[] ourTowers;
    static MapLocation[] enemyTowers;

    static int mapMinX;
    static int mapMaxX;
    static int mapMinY;
    static int mapMaxY;

    public static double estimatedProtectedOre;
    public static double minProtectedMapFraction;
    public static double maxProtectedMapFraction;
    
    public static void analyze() throws GameActionException {
        ourTowers = rc.senseTowerLocations();
        enemyTowers = rc.senseEnemyTowerLocations();

        MeasureMapSize.runInitialCheckForMapEdgesFromHQ(ourTowers);
        mapMinX = MessageBoard.MAP_MIN_X.readInt();
        mapMaxX = MessageBoard.MAP_MAX_X.readInt();
        mapMinY = MessageBoard.MAP_MIN_Y.readInt();
        mapMaxY = MessageBoard.MAP_MAX_Y.readInt();

//        Debug.indicate("analyze", 0, "symmetry = " + MeasureMapSize.mapSymmetry.toString() + "; " + mapMinX + " <= x <= " + mapMaxX + "; " + mapMinY
//                + " <= y <= " + mapMaxY);

        // simulate a soldier coming from the enemy HQ and bugging around our towers
        MapLocation startLoc = theirHQ;
        MapLocation[] border = new MapLocation[1000];
        int numBorderSquares;

        while (true) {
            MapLocation borderStart = advanceToOurHQOrToJustOutsideOurTowerRange(startLoc);

            numBorderSquares = findBorderOfOurTowerRange(borderStart, border);

            MapLocation closestToHQ = borderStart;
            int minDistSq = borderStart.distanceSquaredTo(ourHQ);
            for (int i = 0; i < numBorderSquares; i++) {
                int distSq = border[i].distanceSquaredTo(ourHQ);
                if (distSq < minDistSq) {
                    minDistSq = distSq;
                    closestToHQ = border[i];
                }
            }

            if (closestToHQ.equals(startLoc)) break;
            else startLoc = closestToHQ;
        }

        MapLocation[] interior = new MapLocation[GameConstants.MAP_MAX_WIDTH * GameConstants.MAP_MAX_HEIGHT];
        int numInteriorSquares = fillBorder(interior, border, numBorderSquares);
        
        analyzeProtectedRegion(interior, numInteriorSquares);
    }

    // TODO: put safeties in all while(true)'s

    private static void analyzeProtectedRegion(MapLocation[] interior, int numInteriorSquares) throws GameActionException {
        int numVisibleInteriorSquares = 0;
        double totalVisibleOre = 0;
        
        for(int i = 0; i < numInteriorSquares; i++) {
            if(rc.canSenseLocation(interior[i])) {
                numVisibleInteriorSquares++;
                totalVisibleOre += rc.senseOre(interior[i]);
            }
        }
        
        int minPossibleMapSize = (mapMaxX - mapMinX + 1) * (mapMaxY - mapMinY + 1);

        int actualMapMinX = MessageBoard.MAP_MIN_X.readInt();
        int actualMapMaxX = MessageBoard.MAP_MAX_X.readInt();
        int actualMapMinY = MessageBoard.MAP_MIN_Y.readInt();
        int actualMapMaxY = MessageBoard.MAP_MAX_Y.readInt();
        int maxPossibleMapWidth = GameConstants.MAP_MAX_WIDTH;
        int maxPossibleMapHeight = GameConstants.MAP_MAX_HEIGHT;
        if(actualMapMinX != -MeasureMapSize.COORD_UNKNOWN && actualMapMaxX != MeasureMapSize.COORD_UNKNOWN) {
            maxPossibleMapWidth = actualMapMaxX - actualMapMinX + 1;
        }
        if(actualMapMinY != -MeasureMapSize.COORD_UNKNOWN && actualMapMaxY != MeasureMapSize.COORD_UNKNOWN) {
            maxPossibleMapHeight = actualMapMaxY - actualMapMinY + 1;
        }
        int maxPossibleMapSize = maxPossibleMapWidth * maxPossibleMapHeight;
        
        estimatedProtectedOre = numInteriorSquares * (totalVisibleOre / numVisibleInteriorSquares);
        
//        Debug.indicate("analyze", 0, "# interior squares = " + numInteriorSquares + "; # visible = " + numVisibleInteriorSquares + "; visible ore = " + totalVisibleOre + "; estimated protected ore = " + estimatedProtectedOre);
//        Debug.indicate("analyze", 1, "min possible map size = " + (mapMaxX - mapMinX + 1) + " x " + (mapMaxY - mapMinY + 1) + "; max possible map size = " + maxPossibleMapWidth + " x " + maxPossibleMapHeight);

        minProtectedMapFraction = numInteriorSquares / (double)maxPossibleMapSize;
        maxProtectedMapFraction = numInteriorSquares / (double)minPossibleMapSize;
//        Debug.indicate("analyze", 2, "minProtectedMapFraction = " + minProtectedMapFraction + "; maxProtectedMapFraction = " + maxProtectedMapFraction);
    }
    
    private static MapLocation advanceToOurHQOrToJustOutsideOurTowerRange(MapLocation loc) {
//        rc.setIndicatorDot(loc, 0, 255, 0);
        while (true) {
            MapLocation nextLoc = loc.add(loc.directionTo(ourHQ));
            if (inOurTowerOrHQRange(nextLoc)) return loc;
            if (nextLoc.equals(ourHQ)) return ourHQ;
            loc = nextLoc;
//            rc.setIndicatorDot(loc, 0, 255, 0);
        }
    }

    private static int findBorderOfOurTowerRange(MapLocation start, MapLocation[] border) {
        int numBorderSquares = 0;
        border[numBorderSquares++] = start;

        BorderFollowResult result = followBorderOfOurTowerRangeToWall(border, numBorderSquares, start, true);
        numBorderSquares += result.numSquaresAdded;
//        Debug.indicate("analyze", 1, "wall hit = " + (result.wallHit == null ? "null" : result.wallHit.toString()));

        if (result.wallHit != null) {
            result = followBorderOfOurTowerRangeToWall(border, numBorderSquares, start, false);
            numBorderSquares += result.numSquaresAdded;
//            Debug.indicate("analyze", 2, "also hit = " + (result.wallHit == null ? "null" : result.wallHit.toString()));
        }

        return numBorderSquares;
    }

    private static BorderFollowResult followBorderOfOurTowerRangeToWall(MapLocation[] border, int numBorderSquares, MapLocation start, boolean wallOnLeft) {
        int initialNumBorderSquares = numBorderSquares;

        BorderFollowResult ret = new BorderFollowResult();

        Direction lookDir = start.directionTo(ourHQ);
        MapLocation borderLoc = start;
        while (true) {
//            if (!borderLoc.equals(start)) rc.setIndicatorDot(borderLoc, 255, 0, 0);

            MapLocation nextBorderLoc = borderLoc.add(lookDir);
            while (inOurTowerOrHQRange(nextBorderLoc)) {
                lookDir = wallOnLeft ? lookDir.rotateRight() : lookDir.rotateLeft();
                nextBorderLoc = borderLoc.add(lookDir);
            }
            if (isOffMap(nextBorderLoc)) {
                if (nextBorderLoc.x < mapMinX) ret.wallHit = Direction.WEST;
                if (nextBorderLoc.x > mapMaxX) ret.wallHit = Direction.EAST;
                if (nextBorderLoc.y < mapMinY) ret.wallHit = Direction.NORTH;
                if (nextBorderLoc.y > mapMaxY) ret.wallHit = Direction.SOUTH;
                break;
            }

            borderLoc = nextBorderLoc;
            if (wallOnLeft) lookDir = lookDir.rotateLeft().rotateLeft().rotateLeft();
            else lookDir = lookDir.rotateRight().rotateRight().rotateRight();

            if (borderLoc.equals(start)) break;
            border[numBorderSquares++] = borderLoc;
        }

        ret.numSquaresAdded = numBorderSquares - initialNumBorderSquares;
        return ret;
    }

    private static void cropMapBordersAsSmallAsPossible() {
        if (mapMinX == -MeasureMapSize.COORD_UNKNOWN) {
            int minBuildingX = Math.min(ourHQ.x, theirHQ.x);
            for (MapLocation ourTower : ourTowers) {
                if (ourTower.x < minBuildingX) minBuildingX = ourTower.x;
            }
            for (MapLocation enemyTower : enemyTowers) {
                if (enemyTower.x < minBuildingX) minBuildingX = enemyTower.x;
            }
            mapMinX = minBuildingX - 5;
        }

        if (mapMaxX == MeasureMapSize.COORD_UNKNOWN) {
            int maxBuildingX = Math.max(ourHQ.x, theirHQ.x);
            for (MapLocation ourTower : ourTowers) {
                if (ourTower.x > maxBuildingX) maxBuildingX = ourTower.x;
            }
            for (MapLocation enemyTower : enemyTowers) {
                if (enemyTower.x > maxBuildingX) maxBuildingX = enemyTower.x;
            }
            mapMaxX = maxBuildingX + 5;
        }

        if (mapMinY == -MeasureMapSize.COORD_UNKNOWN) {
            int minBuildingY = Math.min(ourHQ.y, theirHQ.y);
            for (MapLocation ourTower : ourTowers) {
                if (ourTower.y < minBuildingY) minBuildingY = ourTower.y;
            }
            for (MapLocation enemyTower : enemyTowers) {
                if (enemyTower.y < minBuildingY) minBuildingY = enemyTower.y;
            }
            mapMinY = minBuildingY - 5;
        }

        if (mapMaxY == MeasureMapSize.COORD_UNKNOWN) {
            int maxBuildingY = Math.max(ourHQ.y, theirHQ.y);
            for (MapLocation ourTower : ourTowers) {
                if (ourTower.y > maxBuildingY) maxBuildingY = ourTower.y;
            }
            for (MapLocation enemyTower : enemyTowers) {
                if (enemyTower.y > maxBuildingY) maxBuildingY = enemyTower.y;
            }

            mapMaxY = maxBuildingY + 5;
        }
    }

    private static int modX(int x) {
        int ret = x % GameConstants.MAP_MAX_WIDTH;
        if (ret < 0) ret += GameConstants.MAP_MAX_WIDTH;
        return ret;
    }

    private static int modY(int y) {
        int ret = y % GameConstants.MAP_MAX_HEIGHT;
        if (ret < 0) ret += GameConstants.MAP_MAX_HEIGHT;
        return ret;
    }

    private static int fillBorder(MapLocation[] interior, MapLocation[] border, int numBorderSquares) {
        int numInteriorSquares = 0;
        
        cropMapBordersAsSmallAsPossible();

        boolean[][] isBorder = new boolean[GameConstants.MAP_MAX_WIDTH][GameConstants.MAP_MAX_HEIGHT];
        for (int i = 0; i < numBorderSquares; i++) {
            MapLocation borderLoc = border[i];
            isBorder[modX(borderLoc.x)][modY(borderLoc.y)] = true;
        }

        boolean[][] alreadyFilled = new boolean[GameConstants.MAP_MAX_WIDTH][GameConstants.MAP_MAX_HEIGHT];
        MapLocation[] queue = new MapLocation[GameConstants.MAP_MAX_WIDTH * GameConstants.MAP_MAX_HEIGHT];
        int queueHead = 0;
        int queueTail = 0;

        // push HQ onto queue
        queue[queueTail++] = ourHQ;
        alreadyFilled[modX(ourHQ.x)][modY(ourHQ.y)] = true;

        Direction[] dirs = new Direction[] { Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST };

        while (queueHead != queueTail) {
            // pop something off the queue
            MapLocation loc = queue[queueHead++];
            interior[numInteriorSquares++] = loc;
//            rc.setIndicatorDot(loc, 0, 0, 255);
            
            // add neighbors to the queue, but stop at border squares or map edges
            for(Direction dir : dirs) {
                MapLocation newLoc = loc.add(dir);
                int mx = modX(newLoc.x);
                int my = modY(newLoc.y);
                if(alreadyFilled[mx][my]) continue;
                if(isBorder[mx][my]) continue;
                if(isOffMap(newLoc)) continue;
                
                // push new loc onto queue
                queue[queueTail++] = newLoc;
                alreadyFilled[mx][my] = true;
            }
        }
        
        return numInteriorSquares;
    }

    private static boolean inOurTowerOrHQRange(MapLocation loc) {
        if (loc.distanceSquaredTo(ourHQ) <= 52) {
            switch (ourTowers.length) {
                case 6:
                case 5:
                    // enemy HQ has range of 35 and splash
                    if (loc.add(loc.directionTo(ourHQ)).distanceSquaredTo(ourHQ) <= 35) return true;
                    break;

                case 4:
                case 3:
                case 2:
                    // enemy HQ has range of 35 and no splash
                    if (loc.distanceSquaredTo(ourHQ) <= 35) return true;
                    break;

                case 1:
                case 0:
                default:
                    // enemyHQ has range of 24;
                    if (loc.distanceSquaredTo(ourHQ) <= 24) return true;
                    break;
            }
        }

        for (MapLocation tower : ourTowers) {
            if (loc.distanceSquaredTo(tower) <= RobotType.TOWER.attackRadiusSquared) return true;
        }

        return false;
    }

    private static boolean isOffMap(MapLocation loc) {
        return loc.x < mapMinX || loc.x > mapMaxX || loc.y < mapMinY || loc.y > mapMaxY;
    }
    
    
	private static MapLocation[] getBuildings(Team team)
	{
		MapLocation[] buildings;
		if (team == us)
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

    
	// 0 - rotation/diag
	// 1 - rotation/horiz flip
	// 2 - rotation/vert flip
	// 3 - rotation
	// 4 - h flip
	// 5 - v flip
	// 6 - diag
    public static void analyzeSymmetry() throws GameActionException
	{
		int mapSymmetry = 0;
		
		// 0 is HQ location
		// check horizontal/vertical first, easy
		if (ourHQ.x == theirHQ.x)
		{
			mapSymmetry = 1;
		}
		else if (ourHQ.y == theirHQ.y)
		{
			mapSymmetry = 2;
		}
		else // diag or rotation
		{
			if (Math.abs(theirHQ.x - ourHQ.x) != Math.abs(theirHQ.y - ourHQ.y))
				mapSymmetry = 3; // has to be rotational
			else
				mapSymmetry = 0; // diagonal/rotational still
		}
		
		// now go through enemy buildings and check if there's stuff there
		MapLocation[] enblds = getBuildings(us.opponent());
		
		MapLocation hqsum = ourHQ.add(theirHQ.x,theirHQ.y);

		for (MapLocation loc : enblds)
		{
			// symmetry solved, return
			if (mapSymmetry > 2)
				break;
			
			MapLocation loc_horiz = new MapLocation(loc.x, hqsum.y-loc.y);
			MapLocation loc_vert = new MapLocation(hqsum.x-loc.x, loc.y);
			MapLocation loc_rot = new MapLocation(loc_vert.x,loc_horiz.y);
			
			boolean is_horiz = rc.canSenseLocation(loc_horiz) && (rc.senseRobotAtLocation(loc_horiz) != null);
			boolean is_vert = rc.canSenseLocation(loc_vert) && (rc.senseRobotAtLocation(loc_vert) != null);
			boolean is_rot = rc.canSenseLocation(loc_rot) && (rc.senseRobotAtLocation(loc_rot) != null);
			
			switch (mapSymmetry)
			{
			case 0:
				// if it isn't rotational, it's definitely diagonal
				if (!is_rot)
					mapSymmetry = 6;
				break;
			case 1: // horiz or rotation
				// nothing at horiz, must be rotation
				if (is_horiz)
					mapSymmetry = 3;
				if (is_rot)
					mapSymmetry = 4;
				break;
			case 2: // vert or rotation
				if (is_vert)
					mapSymmetry = 3;
				if (is_rot)
					mapSymmetry = 5;
				break;
			default:
				break;
			}
		}
		
		// if it's probably rotational, we'll just call it that
		if (mapSymmetry == 0)
			mapSymmetry = 3;
		
		// and save it, why not
		MessageBoard.MAP_SYMMETRY_CHAN.writeInt(mapSymmetry);
		System.out.println("Map symmetry: " + mapSymmetry);
	}
    
    public static MapLocation mapSymmetricLocation(MapLocation loc) throws GameActionException
    {
    	int mapSymmetry = MessageBoard.MAP_SYMMETRY_CHAN.readInt();
    	MapLocation hqsum = ourHQ.add(theirHQ.x,theirHQ.y);

    	MapLocation loc_sym = null;
    	
    	switch (mapSymmetry)
    	{
    	case 3:
    		loc_sym = new MapLocation(hqsum.x-loc.x,hqsum.y-loc.y);
    		break;
    	case 4:
    		loc_sym = new MapLocation(loc.x, hqsum.y-loc.y);
    		break;
    	case 5:
    		loc_sym = new MapLocation(hqsum.x-loc.x, loc.y);
    		break;
		default:
			break;
    	}
    	
    	return loc_sym;
    }
}
