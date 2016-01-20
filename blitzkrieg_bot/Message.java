package blitzkrieg_bot;

import battlecode.common.*;

import java.util.*;

// enum for encoding the type of a message in the contents
enum MessageType
{
	UNDER_ATTACK, // int free signals always mean under attack
	ZOMBIE_DEN,
	SIGHT_TARGET,
	SPAM,
	FREE_BEER,
	MAP_EDGE,
	NEW_STRATEGY,
	LOTSA_FRIENDLIES
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

class SignalRound extends RobotPlayer
{
	public Signal sig;
	public int round;
	private final int timeout;
	
	public SignalRound(int timeout)
	{
		sig = null;
		round = -1000000;
		this.timeout = timeout;
	}
	
	public boolean update(Signal newsig)
	{
		if (roundsSince(round) > timeout || 
				here.distanceSquaredTo(newsig.getLocation()) < 
				here.distanceSquaredTo(sig.getLocation()))
		{
			sig = newsig;
			round = rc.getRoundNum();
			return true;
		}
		return false;
	}
	
	public boolean isRecent()
	{
		return roundsSince(round) < timeout;
	}
}

public class Message extends RobotPlayer
{	
	// bookkeeping stuff
	public static final int FULL_MAP_DIST_SQ = GameConstants.MAP_MAX_HEIGHT*GameConstants.MAP_MAX_HEIGHT +
											   GameConstants.MAP_MAX_WIDTH*GameConstants.MAP_MAX_WIDTH;
	
	// get set by MapInfo... awkward
	public static int MAP_OFF_X = 0;
	public static int MAP_OFF_Y = 0;
	
	private static SignalRound	recentAllyAttacked = new SignalRound(30);
	private static SignalRound	recentArchonAttacked = new SignalRound(80);
	private static SignalRound	recentFriendlySignal = new SignalRound(300);
	
	// closest recent friendly signal
	
	
	// and for any transmitted enemy messages, only keep the latest received
	public static MapLocation recentEnemySignal = null;
	
	// and other things
	public static Strategy.Type recentStrategySignal = null;
	
	public static void readSignalQueue()
	{
		Signal[] sigs = rc.emptySignalQueue();
		
		for (Signal sig : sigs)
		{
			// skip enemy signals for now
			if (sig.getTeam() != ourTeam)
			{
				recentEnemySignal = sig.getLocation();
				continue;
			}
			
			int[] vals = sig.getMessage();
			MessageType type;
			if (vals == null)
				type = MessageType.UNDER_ATTACK;
			else
				type = readType(vals[0]);
			
			switch (type)
			{
			case UNDER_ATTACK:
				if (vals == null)
					recentAllyAttacked.update(sig);
				else
					recentArchonAttacked.update(sig);
				break;
			case FREE_BEER:
				break;
			case SPAM:
				break;
			case SIGHT_TARGET:
				Sighting.addSightedTarget(readLocation(vals[0]),
						RobotType.values()[readByte(vals[1],0)],
						rc.getRoundNum()-1);
				break;
			case ZOMBIE_DEN:
				MapInfo.updateZombieDens(readLocation(vals[0]),readLocation(vals[1]));
				break;
			case MAP_EDGE:
				MapInfo.updateMapEdges(readLocation(vals[0]), readLocation(vals[1]));
				break;
			case NEW_STRATEGY:
				recentStrategySignal = Strategy.Type.values()[vals[1]];
				break;
			case LOTSA_FRIENDLIES:
				
				break;
			default:
				break;
			}
		}
	}
	
	// to be called by Archon
	public static void sendBuiltMessage(Strategy.Type strat) throws GameActionException
	{
		// send all four map edges in one go
		Message.sendMessageSignal(9, MessageType.MAP_EDGE, MapInfo.mapMin, MapInfo.mapMax);
		// and a strategy to use
		Message.sendMessageSignal(9, MessageType.NEW_STRATEGY, strat.ordinal());
	}
	
	// Basic signals always only done when under attack
	public static MapLocation getClosestAllyUnderAttack() throws GameActionException
	{
		// timeout for old signals
		if (recentArchonAttacked.isRecent())
			return recentArchonAttacked.sig.getLocation();
		
		if (recentAllyAttacked.isRecent())
			return recentAllyAttacked.sig.getLocation();
		
		return null;
	}
	
	// Have we heard from any friendly units close by recently?
	public static MapLocation getRecentFriendlyLocation() throws GameActionException
	{
		if (recentFriendlySignal.sig != null)
			return recentFriendlySignal.sig.getLocation();
		return null;
	}
	
	
	// low-level functions past here
	
	
	private static int readByte(int val, int ind)
	{
		return (val>>>(ind*8))&255;
	}
	
	private static MapLocation readLocation(int val)
	{
		return new MapLocation(readByte(val,0)+MAP_OFF_X, readByte(val,1)+MAP_OFF_Y);
	}
	
	// writes a particular byte
	// (gotta | all the bytes together to make a message)
	public static int writeByte(int val, int ind)
	{
		return (val << (ind*8));
	}
	
	// writes a maplocation to the lowest two bytes of ints n stuff
	public static int writeLocation(MapLocation loc)
	{
		return writeByte(loc.x-MAP_OFF_X,0) | writeByte(loc.y-MAP_OFF_Y,1);
	}
	
	// and the type byte
	public static int writeType(MessageType type)
	{
		return writeByte(type.ordinal(), 3);
	}
	
	public static MessageType readType(int val)
	{
		return MessageType.values()[readByte(val, 3)];
	}
	
	public static void sendMessageSignal(int sq_distance, MessageType type, int val) throws GameActionException
	{
		sendMessageSignal(sq_distance, writeType(type), val);
	}
	
	public static void sendMessageSignal(int sq_distance, MessageType type, MapLocation loc1, MapLocation loc2) throws GameActionException
	{
		sendMessageSignal(sq_distance, writeType(type) | writeLocation(loc1), writeLocation(loc2));
	}
	
	public static void sendMessageSignal(int sq_distance, MessageType type, MapLocation loc) throws GameActionException
	{
		sendMessageSignal(sq_distance, writeType(type) | writeLocation(loc), 0);
	}

	private static void sendMessageSignal(int sq_distance, int v1, int v2) throws GameActionException
	{
		rc.broadcastMessageSignal(v1,v2,sq_distance);
	}
	
	// sends a message-free signal
	public static void sendSignal(int sq_distance) throws GameActionException
	{
		rc.broadcastSignal(sq_distance);
	}
}
