package ball_about_that_base;

import battlecode.common.*;

// uses bitmasks to keep track of a list of directions
// (including NONE)
// --- only extending RobotPlayer for rand(), not "here" or anything like that!!!
public class DirectionSet extends RobotPlayer
{
	static final int[] dirOffsets = {0,1,7,2,6,3,5,4};
	
	
	int dirs = 0;
	
	// don't really need to do any construction
	public DirectionSet()
	{
	}
	
	private DirectionSet(int ds)
	{
		dirs = ds;
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
		Direction best = from.directionTo(to);
		
		// first handle base case of NONE if from and to are equal
		if (isValid(best))
			return best;
		if (to == null || from == null)
			return getRandomValid();
		
		// otherwise let's go through offsets
		int i0 = best.ordinal();
		
		for (int i=0; i<dirOffsets.length; i++)
		{
			int d = (i0 + dirOffsets[i]) & 7;
			if ( (dirs & (1<<d)) > 0 )
				return Direction.values()[d];

		}
		
		return best;
	}
	
	public void add(Direction d)
	{
		dirs |= (1<<d.ordinal());
	}
}
