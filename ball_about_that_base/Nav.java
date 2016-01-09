package ball_about_that_base;

import battlecode.common.*;

public class Nav extends RobotPlayer
{
    private static MapLocation myDest;
    private static DirectionSet myDirs;

    private enum BugState
    {
        DIRECT, BUG
    }

    public enum WallSide
    {
        LEFT, RIGHT
    }

    private static BugState bugState;
    public static WallSide bugWallSide = WallSide.LEFT;
    private static int bugStartDistSq;
    private static Direction bugLastMoveDir;
    private static Direction bugLookStartDir;
    private static int bugRotationCount;
    private static int bugMovesSinceSeenObstacle = 0;

    private static boolean tryMoveDirect() throws GameActionException
    {
        Direction toDest = myDirs.getDirectionTowards(here, myDest);
        
        if (toDest == null)
        	return false;
        
        return MicroBase.tryMove(toDest);
    }

    private static void startBug() throws GameActionException
    {
        bugStartDistSq = here.distanceSquaredTo(myDest);
        bugLastMoveDir = here.directionTo(myDest);
        bugLookStartDir = here.directionTo(myDest);
        bugRotationCount = 0;
        bugMovesSinceSeenObstacle = 0;

        // try to intelligently choose on which side we will keep the wall
        Direction leftTryDir = bugLastMoveDir.rotateLeft();
        for (int i = 0; i < 3; i++)
        {
            if (!myDirs.isValid(leftTryDir))
            	leftTryDir = leftTryDir.rotateLeft();
            else break;
        }
        Direction rightTryDir = bugLastMoveDir.rotateRight();
        for (int i = 0; i < 3; i++)
        {
            if (!myDirs.isValid(rightTryDir)) rightTryDir = rightTryDir.rotateRight();
            else break;
        }
        if (myDest.distanceSquaredTo(here.add(leftTryDir)) < myDest.distanceSquaredTo(here.add(rightTryDir)))
        {
            bugWallSide = WallSide.RIGHT;
        }
        else
        {
            bugWallSide = WallSide.LEFT;
        }
    }

    private static Direction findBugMoveDir() throws GameActionException
    {
        bugMovesSinceSeenObstacle++;
        Direction dir = bugLookStartDir;
        for (int i = 8; i-- > 0;)
        {
            if (myDirs.isValid(dir)) 
            	return dir;
            dir = (bugWallSide == WallSide.LEFT ? dir.rotateRight() : dir.rotateLeft());
            bugMovesSinceSeenObstacle = 0;
        }
        return null;
    }

    private static int numRightRotations(Direction start, Direction end)
    {
        return (end.ordinal() - start.ordinal() + 8) % 8;
    }

    private static int numLeftRotations(Direction start, Direction end)
    {
        return (-end.ordinal() + start.ordinal() + 8) % 8;
    }

    private static int calculateBugRotation(Direction moveDir)
    {
        if (bugWallSide == WallSide.LEFT)
        {
            return numRightRotations(bugLookStartDir, moveDir) - numRightRotations(bugLookStartDir, bugLastMoveDir);
        }
        else
        {
            return numLeftRotations(bugLookStartDir, moveDir) - numLeftRotations(bugLookStartDir, bugLastMoveDir);
        }
    }

    private static void bugMove(Direction dir) throws GameActionException
    {
        if (MicroBase.tryMove(dir))
        {
            bugRotationCount += calculateBugRotation(dir);
        	bugLastMoveDir = dir;
        	
        	if (bugWallSide == WallSide.LEFT)
        		bugLookStartDir = dir.rotateLeft().rotateLeft();
        	else 
        		bugLookStartDir = dir.rotateRight().rotateRight();
        }
    }

    private static boolean detectBugIntoEdge() throws GameActionException
    {
        if (bugWallSide == WallSide.LEFT)
        {
            return !rc.onTheMap(here.add(bugLastMoveDir.rotateLeft()));
        }
        else
        {
            return !rc.onTheMap(here.add(bugLastMoveDir.rotateRight()));
        }
    }

    private static void reverseBugWallFollowDir() throws GameActionException
    {
        bugWallSide = (bugWallSide == WallSide.LEFT ? WallSide.RIGHT : WallSide.LEFT);
        startBug();
    }

    private static void bugTurn() throws GameActionException
    {
        if (detectBugIntoEdge())
        {
            reverseBugWallFollowDir();
        }
        Direction dir = findBugMoveDir();
        if (dir != null)
        {
            bugMove(dir);
        }
    }

    private static boolean canEndBug()
    {
        if (bugMovesSinceSeenObstacle >= 4) return true;
        return (bugRotationCount <= 0 || bugRotationCount >= 8) && here.distanceSquaredTo(myDest) <= bugStartDistSq;
    }

    private static void bugMove() throws GameActionException
    {

    	// Check if we can stop bugging at the *beginning* of the turn
        if (bugState == BugState.BUG)
        {
            if (canEndBug())
                bugState = BugState.DIRECT;
        }

        // If DIRECT mode, try to go directly to target
        if (bugState == BugState.DIRECT)
        {
            if (!tryMoveDirect())
            {
                bugState = BugState.BUG;
                startBug();
            }
        }

        // If that failed, or if bugging, bug
        if (bugState == BugState.BUG)
            bugTurn();
    }

    public static void goTo(MapLocation dest, DirectionSet dirs) throws GameActionException
    {
    	myDirs = dirs;
    	
        if (!dest.equals(myDest))
        {
            myDest = dest;
            bugState = BugState.DIRECT;
        }
        
        if (here.equals(dest))
        	return;

        bugMove();
    }
}
