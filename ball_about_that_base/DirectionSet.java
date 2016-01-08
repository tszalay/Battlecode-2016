package ball_about_that_base;

import battlecode.common.*;

// uses bitmasks to keep track of a list of directions
// (including NONE)
// --- only extending RobotPlayer for rand(), not "here" or anything like that!!!
public class DirectionSet extends RobotPlayer
{
	static final int[] dirOffsets = {1,7,2,6,3,5,4};
	
	
	int dirs = 0;
	
	// don't really need to do any construction
	public DirectionSet()
	{
	}
	
	private DirectionSet(int ds)
	{
		this.dirs = ds;
	}
	
	public DirectionSet and(DirectionSet d)
	{
		return new DirectionSet(dirs & d.dirs);
	}
	
	public DirectionSet or(DirectionSet d)
	{
		return new DirectionSet(dirs | d.dirs);
	}
	
	public boolean any()
	{
		return dirs != 0;
	}
	
	public boolean isValid(Direction d)
	{
		return ((1 << d.ordinal()) & dirs) > 0;
	}
	
	public Direction getRandomValid()
	{
		if (!any())
			return null;
		
		int nbits = Integer.bitCount(dirs);
		int dir_bit = 1+rand.nextInt(nbits);

		int a = 1;
		
		for (int i=0; i<9; i++)
		{
			if ( (a&dirs) > 0 )
				dir_bit--;
			
			if (dir_bit == 0)
				return Direction.values()[i];
			
			a <<= 1;
		}
		
		return null;
	}
	
	public Direction getDirectionTowards(MapLocation from, MapLocation to)
	{
		// if they're just straight up null
		if (to == null || from == null)
			return getRandomValid();
		
		Direction best = from.directionTo(to);
		
		// first handle base case
		if (isValid(best))
			return best;
		
		// otherwise let's go through offsets
		int i0 = best.ordinal();
		
		for (int i=0; i<dirOffsets.length; i++)
		{
			int d = (i0 + dirOffsets[i]) & 7;
			if ( (dirs & (1<<d)) > 0 )
				return Direction.values()[d];

		}
		
		return null;
	}
	
	public void add(Direction d)
	{
		dirs |= (1<<d.ordinal());
	}
	
	public DirectionSet getOddSquares(MapLocation currentLoc)
	{
		// return the adjacent DirectionSet of squares (from currentLoc) that are odd
		
		if (currentLoc == null)
			return null;
		
		DirectionSet odds = new DirectionSet();
		
		if ( ((currentLoc.x + currentLoc.y) % 2) == 0)  // currentLoc is even
		{
			odds.add(Direction.NORTH);
			odds.add(Direction.SOUTH);
			odds.add(Direction.EAST);
			odds.add(Direction.WEST);
		}
		else // currentLoc is odd
		{
			odds.add(Direction.NORTH_EAST);
			odds.add(Direction.SOUTH_EAST);
			odds.add(Direction.SOUTH_WEST);
			odds.add(Direction.NORTH_WEST);
		}
		
		return odds;
	}
	
	public DirectionSet getEvenSquares(MapLocation currentLoc)
	{
		// return the adjacent DirectionSet of squares (from currentLoc) that are even
		
		if (currentLoc == null)
			return null;
		
		DirectionSet evens = new DirectionSet();
		
		if ( ((currentLoc.x + currentLoc.y) % 2) == 0)  // currentLoc is even
		{
			evens.add(Direction.NORTH_EAST);
			evens.add(Direction.SOUTH_EAST);
			evens.add(Direction.SOUTH_WEST);
			evens.add(Direction.NORTH_WEST);
		}
		else // currentLoc is odd
		{
			evens.add(Direction.NORTH);
			evens.add(Direction.SOUTH);
			evens.add(Direction.EAST);
			evens.add(Direction.WEST);
		}
		
		return evens;
	}
}
