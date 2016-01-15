package space_bottity;

import battlecode.common.*;

import java.util.*;

public class MobFightStrat extends RobotPlayer
{
	private static RobotType type;
	
	public MobFightStrat(RobotType type)
	{
		this.type = type;
	}
	
	public static void doTurn() throws GameActionException
	{
		switch (type)
		{
		case TURRET:
			Behavior.tryAttackSomeone();
			break;
		
		default:
			// if i am overpowered, kite retreat taking pot-shots
			Debug.setStringSJF("allies = " + Micro.getNearbyAllies().length + ", hostiles = " + Micro.getNearbyHostiles().length + ", overpowered: " + Micro.amOverpowered());
			if (Behavior.tryAttackSomeone())
				return;
			
			if (Micro.amOverpowered())
			{
				// pot-shot when you can get away without getting hit, retreat when unable to
				if (Micro.getRoundsUntilDanger() > Micro.getRoundsUntilShootAndMove())
					Behavior.tryAttackSomeone();
				else
					Behavior.tryRetreatOrShootIfStuck();
				
				return;
			}
			
			// not overpowered.  if we see anyone, shoot
			if (Micro.getNearbyHostiles().length > 0)
			{
				Behavior.tryAttackSomeone();
				return;
			}
			
			// not overpowered.  we don't see anyone.  listen for calls for reinforcements, and move to help
			if (Message.getClosestAllyUnderAttack() != null)
			{
				Behavior.tryAdjacentSafeMoveToward(Message.getClosestAllyUnderAttack());
			}
			break;
		}
	}
}
