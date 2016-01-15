package space_bottity;

import battlecode.common.*;
import java.util.*;

// enum for encoding the type of a message in the contents
enum MessageType
{
	UNDER_ATTACK,//int free signals always mean under attack
	SPAWN,
	ZOMBIE_DEN,
	SIGHT_TARGET,
	SPAM,
	FREE_BEER,
	GOOD_PARTS,
	RALLY_LOCATION,
	MAP_EDGE,
	REMOVE_WAYPOINT
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
	private static final int TYPE_BITS = 8;
	private static final int SHIFT_BITS = 32-TYPE_BITS;
	private static final int SHIFT_MASK = ((1 << SHIFT_BITS)-1);
	private static final int LOC_OFFSET = 20000;
	public static final int FULL_MAP_DIST_SQ = GameConstants.MAP_MAX_HEIGHT*GameConstants.MAP_MAX_HEIGHT +
											   GameConstants.MAP_MAX_WIDTH*GameConstants.MAP_MAX_WIDTH;
	
	// storage for received/accumulated message info
	public static ArrayList<SignalLocation> archonLocs = new ArrayList<SignalLocation>();
	public static ArrayList<SignalLocation> underAttackLocs = new ArrayList<SignalLocation>();
	
	// and for any transmitted enemy messages, only keep the latest received
	public static MapLocation recentEnemySignal = null;
	
	// and other things
	public static MapLocation rallyLocation = null;
	public static MapLocation closestAllyUnderAttackLocation = null;
	
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
				type = MessageType.values()[readByte(vals[0],3)];
			
			if (sig.getID() == BallMove.ballTargetID)
				BallMove.updateBallLocation(sig.getLocation());

			switch (type)
			{
			case UNDER_ATTACK:
				underAttackLocs.add(new SignalLocation(sig,sig.getLocation()));
				break;
			case SPAWN:
				archonLocs.add(new SignalLocation(sig,readLocation(vals)));
				break;
			case FREE_BEER:
				break;
			case SPAM:
				break;
			case SIGHT_TARGET:
				Sighting.addSightedTarget(readLocation(vals),
						RobotType.values()[readByte(vals[1],3)],
						rc.getRoundNum()-1);
				break;
			case ZOMBIE_DEN:
				MapInfo.updateZombieDens(readLocation(vals),false);
				break;
			case RALLY_LOCATION:
				rallyLocation = readLocation(vals);
				break;
			case MAP_EDGE:
				MapLocation ml = readLocation(vals);
				MapInfo.updateMapEdge(Direction.values()[ml.x],ml.y,false);
				break;
			case GOOD_PARTS:
				MapInfo.updateParts(readLocation(vals),false);				
				break;
			}
		}
	}
	
	private static int readByte(int val, int ind)
	{
		return (val>>>(ind*8))&255;
	}
	
	private static MapLocation readLocation(int[] vals)
	{
		return new MapLocation((vals[0]&SHIFT_MASK)-LOC_OFFSET, vals[1]-LOC_OFFSET);
	}
	
	// to be called by Archon
	public static void sendBuiltMessage() throws GameActionException
	{
		// send all four map edge signals
		Message.sendMessageSignal(2, MessageType.MAP_EDGE,
				new MapLocation(Direction.NORTH.ordinal(), MapInfo.mapMin.y));
		Message.sendMessageSignal(2, MessageType.MAP_EDGE,
				new MapLocation(Direction.EAST.ordinal(), MapInfo.mapMax.x));
		Message.sendMessageSignal(2, MessageType.MAP_EDGE,
				new MapLocation(Direction.SOUTH.ordinal(), MapInfo.mapMax.y));
		Message.sendMessageSignal(2, MessageType.MAP_EDGE,
				new MapLocation(Direction.WEST.ordinal(), MapInfo.mapMin.x));
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

	//Basic signals always only done when under attack
	public static MapLocation getClosestAllyUnderAttack()
	{
		if (closestAllyUnderAttackLocation != null)
			return closestAllyUnderAttackLocation;
		
		int distToClosestAlly = 1000000;
		MapLocation bestLoc = null;
		for (SignalLocation sm : Message.underAttackLocs )
		{
			int distToThisSignalOrigin = here.distanceSquaredTo(sm.loc);
			if (distToThisSignalOrigin < distToClosestAlly)
			{
				distToClosestAlly = distToThisSignalOrigin;
				bestLoc = sm.loc;
			}
		}
		
		closestAllyUnderAttackLocation = bestLoc;
		return closestAllyUnderAttackLocation;
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
