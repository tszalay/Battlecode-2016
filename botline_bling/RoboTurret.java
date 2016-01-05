package botline_bling;

import battlecode.common.*;

public class RoboTurret extends RobotPlayer
{
	public static void run()
	{
		int myAttackRange = 0;
		
		try {
            myAttackRange = rc.getType().attackRadiusSquared;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        while (true) {
            // This is a loop to prevent the run() method from returning. Because of the Clock.yield()
            // at the end of it, the loop will iterate once per game round.
            try {
                // If this robot type can attack, check for enemies within range and attack one
                if (rc.isWeaponReady()) {
                    RobotInfo[] enemiesWithinRange = rc.senseNearbyRobots(myAttackRange, theirTeam);
                    RobotInfo[] zombiesWithinRange = rc.senseNearbyRobots(myAttackRange, Team.ZOMBIE);
                    if (enemiesWithinRange.length > 0) {
                        for (RobotInfo enemy : enemiesWithinRange) {
                            // Check whether the enemy is in a valid attack range (turrets have a minimum range)
                            if (rc.canAttackLocation(enemy.location)) {
                                rc.attackLocation(enemy.location);
                                break;
                            }
                        }
                    } else if (zombiesWithinRange.length > 0) {
                        for (RobotInfo zombie : zombiesWithinRange) {
                            if (rc.canAttackLocation(zombie.location)) {
                                rc.attackLocation(zombie.location);
                                break;
                            }
                        }
                    }
                }

                Clock.yield();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }

	}
}
