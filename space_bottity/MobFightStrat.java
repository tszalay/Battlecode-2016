package space_bottity;

import battlecode.common.*;

import java.util.*;

public class MobFightStrat extends RobotPlayer implements Strategy
{
	private static String stratName;
	private static MapLocation target;
	
	public MobFightStrat()
	{
		this.stratName = "MobFightStrat";
		this.target = null;
	}
	
	public MobFightStrat(MapLocation target)
	{
		this.stratName = "MobFightStrat";
		this.target = target;
	}
	
	public boolean tryTurn() throws GameActionException
	{
		Debug.setStringAK("My Strategy: " + this.stratName);
		
		switch (rc.getType())
		{
		case TURRET:
			return (Action.tryAttackSomeone());
			
		case GUARD:
			// get in their face
			// don't retreat unless you are out-numbered
			// retreating is for the weak
			if (Action.tryAttackSomeone())
				return true;
			
			// we can't attack anyone.  listen for calls for reinforcements, and move to help
			updateTarget();
			if (target != null)
			{
				RobotInfo enemyAttackingAlly = Micro.getClosestUnitTo(Micro.getNearbyHostiles(), target);
				if (enemyAttackingAlly == null)
					return Action.tryMove(here.directionTo(target));
				
				if (!Action.tryAdjacentSafeMoveToward(enemyAttackingAlly.location))
				{
					Direction dir = Micro.getCanFastMoveDirs().getDirectionTowards(here.directionTo(enemyAttackingAlly.location));
					if (!Action.tryMove(dir))
					{
						dir = Micro.getCanMoveDirs().getDirectionTowards(here.directionTo(enemyAttackingAlly.location));
						if (!Action.tryMove(dir))
							return Nav.tryGoTo(enemyAttackingAlly.location, Micro.getCanMoveDirs()); // don't just sit there, FULL SURROUND
						else
							return true;
					}
					else
					{
						return true;
					}
				}
				else
				{
					return true;
				}
			}
			
			// if out of their range and alone, retreat
			if (rc.senseNearbyRobots(2, ourTeam).length < 3)
			{
				return Action.tryRetreatOrShootIfStuck();
			}
			
			// if you are out-ranged, move in
//			RobotInfo rangedHostile = null;
//			for (RobotInfo ri : Micro.getNearbyHostiles())
//			{
//				if (ri.type == RobotType.RANGEDZOMBIE || ri.type == RobotType.VIPER)
//				{
//					rangedHostile = ri;
//					continue;
//				}
//			}
//			if (rangedHostile != null)
//			{
//				Direction dir = Micro.getCanFastMoveDirs().getDirectionTowards(here.directionTo(rangedHostile.location));
//				if (!Action.tryMove(dir))
//				{
//					dir = Micro.getCanMoveDirs().getDirectionTowards(here.directionTo(rangedHostile.location));
//					return Action.tryMove(dir);
//				}
//				else
//				{
//					return true;
//				}
//			}
			
			
			return false;
		
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
			updateTarget();
			if (target != null)
			{
				RobotInfo enemyAttackingAlly = Micro.getClosestUnitTo(Micro.getNearbyHostiles(), target);
				if (enemyAttackingAlly == null)
					return Action.tryMove(here.directionTo(target));
				
				if (!Action.tryAdjacentSafeMoveToward(enemyAttackingAlly.location))
				{
					Direction dir = Micro.getCanFastMoveDirs().getDirectionTowards(here.directionTo(enemyAttackingAlly.location));
					if (!Action.tryMove(dir))
					{
						dir = Micro.getCanMoveDirs().getDirectionTowards(here.directionTo(enemyAttackingAlly.location));
						if (!Action.tryMove(dir))
							return Nav.tryGoTo(enemyAttackingAlly.location, Micro.getCanMoveDirs()); // don't just sit there, FULL SURROUND
						else
							return true;
					}
					else
					{
						return true;
					}
				}
				else
				{
					return true;
				}
			}
			return false;
		}
	}
	
	public void updateTarget() throws GameActionException
	{
		if (target == null)
		{
			target = Message.getClosestAllyUnderAttack();
			return;
		}
		
		if (rc.canSenseLocation(target) && (rc.senseRobotAtLocation(target) == null || rc.senseRobotAtLocation(target).team == ourTeam))
			target = null;
		if (target == null)
			target = Message.getClosestAllyUnderAttack();
	}
}
