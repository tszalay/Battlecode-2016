package botline_bling;

import battlecode.common.*;
import java.util.*;

// enum for encoding the type of a message in the contents
enum MessageType
{
	NONE,
	SPAWN,
	ZOMBIE_DEN,
	ZOMBIE,
	SPAM,
	FREE_BEER
}

class SignalLocation
{
	public MapLocation loc;
	public Signal sig;
	
	public SignalLocation (Signal s, MapLocation ml)
	{
		sig = s;
		loc = ml;
	}
}

public class Message extends RobotPlayer
{	
	// bookkeeping stuff
	private static final int TYPE_BITS = 4;
	private static final int SHIFT_BITS = 32-TYPE_BITS;
	private static final int LOC_OFFSET = 17000;
	
	// storage for received/accumulated message info
	public static ArrayList<SignalLocation> archonLocs = new ArrayList<SignalLocation>();
	public static ArrayList<SignalLocation> zombieDenLocs = new ArrayList<SignalLocation>();
	public static ArrayList<SignalLocation> zombieLocs = new ArrayList<SignalLocation>();
	
	//List<Signal> enemySignals;
	
	public static void readSignalQueue()
	{
		Signal[] sigs = rc.emptySignalQueue();
		
		for (Signal sig : sigs)
		{
			// skip enemy signals for now
			if (sig.getTeam() != ourTeam)
				continue;
			
			int[] vals = sig.getMessage();
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
			case SPAWN:
				archonLocs.add(new SignalLocation(sig,readLocation(vals)));
				break;
			case FREE_BEER:
				break;
			case NONE:
				// logic for basic signaling goes here
				break;
			case SPAM:
				break;
			case ZOMBIE:
				break;
			case ZOMBIE_DEN:
				zombieDenLocs.add(new SignalLocation(sig,readLocation(vals)));
				break;
			}
		}
	}

	private static MapLocation readLocation(int[] vals)
	{
		return new MapLocation(vals[0]-LOC_OFFSET, vals[1]-LOC_OFFSET);
	}
	
	public static void sendMessageSignal(int lin_distance, MessageType type, MapLocation loc) throws GameActionException
	{
		int v1 = (type.ordinal() << SHIFT_BITS);
		sendMessageSignal(lin_distance, v1 | (loc.x+LOC_OFFSET), (loc.y+LOC_OFFSET));
	}

	
	private static void sendMessageSignal(int lin_distance, int v1, int v2) throws GameActionException
	{
		int x = lin_distance*lin_distance/rc.getType().sensorRadiusSquared - 2;
		if (x<0) x=0;
		
		//rc.broadcastMessageSignal(v1,v2,x);
		rc.broadcastMessageSignal(v1,v2,lin_distance*lin_distance);
	}
	
	// sends a message-free signal
	public static void sendSignal(int lin_distance) throws GameActionException
	{
		int x = lin_distance*lin_distance/rc.getType().sensorRadiusSquared - 2;
		if (x<0) x=0;
		
		rc.broadcastSignal(x);
	}
}
