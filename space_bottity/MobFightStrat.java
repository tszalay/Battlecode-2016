package space_bottity;

import battlecode.common.*;

import java.util.*;

public class MobFightStrat extends RobotPlayer implements Strategy
{
	private static RobotType type;
	
	public MobFightStrat(RobotType type)
	{
		this.type = type;
	}
	
	public boolean tryTurn() throws GameActionException
	{
		switch (type)
		{
		case TURRET:
			return (Action.tryAttackSomeone());
		
		default:
			// if i am overpowered, kite retreat taking pot-shots
			Debug.setStringSJF("allies = " + Micro.getNearbyAllies().length + ", hostiles = " + Micro.getNearbyHostiles().length + ", overpowered: " + Micro.amOverpowered());
			if (Action.tryAttackSomeone())
				return true;
			
			if (Micro.amOverpowered())
			{
				// pot-shot when you can get away without getting hit, retreat when unable to
				if (Micro.getRoundsUntilDanger() > Micro.getRoundsUntilShootAndMove() + 1)
				{
					if (!Action.tryAttackSomeone())
						return Action.tryRetreatOrShootIfStuck();
					else
						return true;
				}
				else
				{
					return Action.tryRetreatOrShootIfStuck();
				}
			}
			
			// not overpowered.  we don't see anyone.  listen for calls for reinforcements, and move to help
			MapLocation allyLoc = Message.getClosestAllyUnderAttack();
			if (allyLoc != null)
			{
				RobotInfo enemyAttackingAlly = Micro.getClosestUnitTo(Micro.getNearbyHostiles(), allyLoc);
				if (enemyAttackingAlly == null)
					return Action.tryMove(here.directionTo(allyLoc));
				
				if (!Action.tryAdjacentSafeMoveToward(enemyAttackingAlly.location))
				{
					Direction dir = Micro.getCanFastMoveDirs().getDirectionTowards(here.directionTo(enemyAttackingAlly.location));
					return Action.tryMove(dir);
				}
				else
				{
					return true;
				}
			}
			return false;
		}
	}
}
