package ball_is_life;

import battlecode.common.*;
import java.util.*;

public class FastLocSet
{
    private static final int HASH = Math.max(GameConstants.MAP_MAX_WIDTH, GameConstants.MAP_MAX_HEIGHT);
    private boolean[][] has = new boolean[HASH][HASH];
    private List<MapLocation> locations = new ArrayList<MapLocation>();

    public void add(MapLocation loc)
    {
        int x = loc.x % HASH;
        int y = loc.y % HASH;
        if (!has[x][y])
        {
            has[x][y] = true;
            locations.add(loc);
        }
    }

    public void remove(MapLocation loc)
    {
        int x = loc.x % HASH;
        int y = loc.y % HASH;
        if (has[x][y])
        {
            has[x][y] = false;
            locations.remove(loc);
        }
    }

    public boolean contains(MapLocation loc)
    {
        return has[loc.x % HASH][loc.y % HASH];
    }
    
    public void clear()
    {
        has = new boolean[HASH][HASH];
        locations.clear();
    }
    
    public List<MapLocation> elements()
    {
    	return locations;
    }
}