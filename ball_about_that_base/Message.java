package ball_about_that_base;

import battlecode.common.*;
import java.util.*;

// enum for encoding the type of a message in the contents
enum MessageType
{
	NONE,
	SPAWN,
	ZOMBIE_DEN,
	SIGHT_TARGET,
	ENEMY_TURRET,
	SPAM,
	FREE_BEER,
	RALLY_LOCATION,
	MAP_MIN,
	MAP_MAX
}

class SignalLocation extends RobotPlayer
{
	public MapLocation loc;
	public Signal sig;
	public int round;
	
	public SignalLocation (Signal s, MapLocation ml)
	{
		sig = s;
		loc = ml;
		round = rc.getRoundNum();
	}
}

public class Message extends RobotPlayer
{	
	// bookkeeping stuff
	private static final int TYPE_BITS = 5;
	private static final int SHIFT_BITS = 32-TYPE_BITS;
	private static final int LOC_OFFSET = 20000;
	public static final int FULL_MAP_DIST_SQ = GameConstants.MAP_MAX_HEIGHT*GameConstants.MAP_MAX_HEIGHT +
											   GameConstants.MAP_MAX_WIDTH*GameConstants.MAP_MAX_WIDTH;
	
	// storage for received/accumulated message info
	public static ArrayList<SignalLocation> archonLocs = new ArrayList<SignalLocation>();
	public static ArrayList<SignalLocation> sightLocs = new ArrayList<SignalLocation>();
	public static ArrayList<SignalLocation> enemyTurretLocs = new ArrayList<SignalLocation>();
	
	// and for any transmitted enemy messages, only keep recents (300 rounds)
	public static ArrayList<Signal> enemySignals = new ArrayList<Signal>();
	
	// and other things
	public static MapLocation rallyLocation = null;
	
	public static void readSignalQueue()
	{
		Signal[] sigs = rc.emptySignalQueue();
		
		for (Signal sig : sigs)
		{
			// skip enemy signals for now
			if (sig.getTeam() != ourTeam)
			{
				//enemySignals.add(sig);
				continue;
			}
			
			int[] vals = sig.getMessage().clone();
			MessageType type;
			if (vals == null)
			{
				type = MessageType.NONE;
			}
			else
			{
				type = MessageType.values()[vals[0] >>> SHIFT_BITS];
				vals[0] &= ((1 << SHIFT_BITS)-1);
			}

			switch (type)
			{
			case NONE:
				// logic for basic signaling goes here
				break;
			case SPAWN:
				archonLocs.add(new SignalLocation(sig,readLocation(vals)));
				break;
			case FREE_BEER:
				break;
			case SPAM:
				break;
			case SIGHT_TARGET:
				sightLocs.add(new SignalLocation(sig,readLocation(vals)));
				break;
			case ENEMY_TURRET:
				enemyTurretLocs.add(new SignalLocation(sig,readLocation(vals)));
				break;
			case ZOMBIE_DEN:
				MapInfo.updateZombieDens(readLocation(vals),false);
				break;
			case RALLY_LOCATION:
				rallyLocation = readLocation(vals);
				break;
			case MAP_MIN:
				MapInfo.updateMapEdges(readLocation(vals),false);
				break;
			case MAP_MAX:
				MapInfo.updateMapEdges(readLocation(vals),false);
				break;
			}
		}
	}
	

	private static MapLocation readLocation(int[] vals)
	{
		return new MapLocation(vals[0]-LOC_OFFSET, vals[1]-LOC_OFFSET);
	}
	
	
	// to be called by Archon
	public static void sendBuiltMessage() throws GameActionException
	{
		// our rally location should have been set by the Archon already
		if (Message.rallyLocation != null)
			sendMessageSignal(2, MessageType.RALLY_LOCATION, Message.rallyLocation);
	}
	
	// also by archon
	public static void calcRallyLocation()
	{
		int bestID = 1000000;
		MapLocation bestloc = null;

		for (SignalLocation sm : Message.archonLocs)
		{
			if (sm.sig.getID() < bestID)
			{
				bestloc = sm.loc;
				bestID = sm.sig.getID();
			}
		}
		
		rallyLocation = bestloc;
	}
	
	
	public static void sendMessageSignal(int sq_distance, MessageType type, MapLocation loc) throws GameActionException
	{
		int v1 = (type.ordinal() << SHIFT_BITS);
		sendMessageSignal(sq_distance, v1 | (loc.x+LOC_OFFSET), (loc.y+LOC_OFFSET));
	}

	
	private static void sendMessageSignal(int sq_distance, int v1, int v2) throws GameActionException
	{
		rc.broadcastMessageSignal(v1,v2,sq_distance*sq_distance);
	}
	
	// sends a message-free signal
	public static void sendSignal(int sq_distance) throws GameActionException
	{
		rc.broadcastSignal(sq_distance);
	}
}
