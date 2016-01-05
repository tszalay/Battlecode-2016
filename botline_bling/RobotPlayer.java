package botline_bling;

import battlecode.common.*;

import java.util.Random;

public class RobotPlayer
{
	public static RobotController rc;
	public static Random rand;
	public static Team ourTeam;
	public static Team theirTeam;
	
	@SuppressWarnings("unused")

	// BC Engine -> RobotPlayer.run -> RoboXXX.run
    public static void run(RobotController robotc)
	{
		// globals in our class
		RobotPlayer.rc = robotc;
		RobotPlayer.rand = new Random(rc.getID());
		RobotPlayer.ourTeam = rc.getTeam();
		RobotPlayer.theirTeam = ourTeam.opponent();
		
		Debug.setStringTS("Tamas");
		Debug.setStringAK("A-aron");
		Debug.setStringSJF("Stephen J. Fry");
		Debug.setStringRR("Ryan");
		
		switch (robotc.getType())
		{
		case ARCHON:
			RoboArchon.run();
			break;
		case GUARD:
			RoboGuard.run();
			break;
		case SCOUT:
			RoboScout.run();
			break;
		case SOLDIER:
			RoboSoldier.run();
			break;
		case TTM:
			RoboTTM.run();
			break;
		case TURRET:
			RoboTurret.run();
			break;
		case VIPER:
			RoboViper.run();
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
}
