package botline_bling;

import battlecode.common.*;

// sq. distance formula: sightRadius*(2+x)
// so x = sqd / sight - 2

public class Message extends RobotPlayer
{
	// enum for encoding the type of a message in the contents
	public enum MessageType
	{
		NONE,
		SPAWN,
		DEN,
		ENEMY,
		SPAM,
		FREE_BEER
	}
	
	MessageType type;
	int val1;
	int val2;
	
	private static final int TYPE_BITS = 4;
	private static final int SHIFT_BITS = 32-TYPE_BITS;
	private static final int LOC_OFFSET = 17000;
	
	// general constructor with two ints
	public Message(MessageType myType, int v1, int v2)
	{
		this.type = myType;
		
		this.val1 = v1 | (this.type.ordinal() << SHIFT_BITS);
		this.val2 = v2;
	}
	
	public Message(MessageType myType, MapLocation loc)
	{
		this(myType, loc.x + LOC_OFFSET, loc.y + LOC_OFFSET);
	}
	
	public Message(Signal sig)
	{
		int[] vals = sig.getMessage();
		
		// check if it's a message-less signal first
		if (vals == null)
		{
			this.type = MessageType.NONE;
			return;
		}
		
		this.type = MessageType.values()[vals[0] >>> SHIFT_BITS];
		this.val1 = vals[0] & ((1 << SHIFT_BITS)-1);
		this.val2 = vals[1];
	}
	
	public MapLocation readLocation()
	{
		return new MapLocation(this.val1-LOC_OFFSET, this.val2-LOC_OFFSET);
	}
	
	public void send(int lin_distance) throws GameActionException
	{
		Message.sendMessageSignal(lin_distance, this.val1, this.val2);
	}
	
	public static void sendMessageSignal(int lin_distance, int v1, int v2) throws GameActionException
	{
		int x = lin_distance*lin_distance/rc.getType().sensorRadiusSquared - 2;
		if (x<0) x=0;
		
		rc.broadcastMessageSignal(v1,v2,x);
	}
	
	// sends a message-free signal
	public static void sendSignal(int lin_distance) throws GameActionException
	{
		int x = lin_distance*lin_distance/rc.getType().sensorRadiusSquared - 2;
		if (x<0) x=0;
		
		rc.broadcastSignal(x);
	}
}
