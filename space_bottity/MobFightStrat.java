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
			Action.tryAttackSomeone();
			break;
		
		default:
			// if i am overpowered, kite retreat taking pot-shots
			// Debug.setStringSJF("allies = " + Micro.getNearbyAllies().length + ", hostiles = " + Micro.getNearbyHostiles().length + ", overpowered: " + Micro.amOverpowered());
			if (Action.tryAttackSomeone())
				return;
			
			if (Micro.amOverpowered())
			{
				// pot-shot when you can get away without getting hit, retreat when unable to
				if (Micro.getRoundsUntilDanger() > Micro.getRoundsUntilShootAndMove() + 1)
					Action.tryAttackSomeone();
				else
					Action.tryRetreatOrShootIfStuck();
				
				return;
			}
			
			// not overpowered.  we don't see anyone.  listen for calls for reinforcements, and move to help
			if (Message.getClosestAllyUnderAttack() != null)
			{
				if (!Action.tryAdjacentSafeMoveToward(Message.getClosestAllyUnderAttack()))
				{
					Action.tryMove(here.directionTo(Message.getClosestAllyUnderAttack()));
				}
			}
			break;
		}
	}
}
