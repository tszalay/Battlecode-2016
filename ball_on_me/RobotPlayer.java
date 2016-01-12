package ball_on_me;

import battlecode.common.*;

import java.util.*;

public class RobotPlayer
{
	public static RobotController rc;
	public static Random rand;
	public static Team ourTeam;
	public static Team theirTeam;
	public static MapLocation here;
	
	public static RobotInfo myArchon = null;
	public static int myArchonSenseRound = 0;
	
	public static MicroBase Micro = null;
	
	@SuppressWarnings("unused")
	// BC Engine -> RobotPlayer.run -> RoboXXX.run
    public static void run(RobotController robotc)
	{
		// globals in our class
		RobotPlayer.rc = robotc;
		RobotPlayer.rand = new Random(rc.getID());
		RobotPlayer.ourTeam = rc.getTeam();
		RobotPlayer.theirTeam = ourTeam.opponent();
		RobotPlayer.here = rc.getLocation();
		
		Debug.setStringTS("Tamas");
		Debug.setStringAK("A-aron");
		Debug.setStringSJF("Stephen J. Fry");
		Debug.setStringRR("Ryan");
				
		// look for an archon close by, if we aren't an Archon
		if (rc.getType() != RobotType.ARCHON)
		{
			RobotInfo[] nearbyFriends;
			// look at a larger distance if we started early in the game
			if (rc.getRoundNum() < 2)
				nearbyFriends = rc.senseNearbyRobots(rc.getType().sensorRadiusSquared, ourTeam);
			else
				nearbyFriends = rc.senseNearbyRobots(2, ourTeam);
			
			// find closest Archon
			for (RobotInfo ri : nearbyFriends)
			{
				if (ri.type == RobotType.ARCHON)
				{
					if (myArchon == null || here.distanceSquaredTo(myArchon.location) > here.distanceSquaredTo(ri.location))
						myArchon = ri;
				}
			}
			if (myArchon != null) myArchonSenseRound = rc.getRoundNum();
		}
		
		try
		{
			switch (robotc.getType())
			{
			case ARCHON:
				RoboArchon.init();
				break;
			case GUARD:
				RoboGuard.init();
				break;
			case SCOUT:
				RoboScout.init();
				break;
			case SOLDIER:
				RoboSoldier.init();
				break;
			case TTM:
			case TURRET:
				RoboTurret.init();
				break;
			case VIPER:
				RoboViper.init();
				break;
				
			case ZOMBIEDEN:
			case BIGZOMBIE:
			case FASTZOMBIE:
			case RANGEDZOMBIE:
			case STANDARDZOMBIE:
			default:
				System.out.println("WTF IS GOING ON!!!111");
				rc.disintegrate();
				break;
			}
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
            e.printStackTrace();
		}
		
		// doing it like this so we can add robot-wide behaviors easily
		try
		{
			while (true)
			{
				RobotPlayer.here = rc.getLocation();
				// clear all outstanding micro stuff
				Micro = new MicroBase();
				// process incoming messages
				Message.readSignalQueue();
				// update stuff with sensing
				MapInfo.updateLocalWaypoints();
				
				if (rc.getHealth() == rc.getType().maxHealth)
				{
					int attackRound = rc.getRoundNum() + Micro.getRoundsUntilDanger();
					System.out.println("estimated attack round = " + attackRound);
					Debug.setStringSJF("estimated attack round = " + attackRound);
				}
				else
				{
					System.out.println("got attacked!");
					rc.disintegrate();
				}
				
				Clock.yield();
			}
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
            e.printStackTrace();
		}

    }
}
