function output_fn = bugNav(command)

    BUG_LEFT = 0;

    % closures for bug nav
    bugWallOnLeft = 0;
    bugIsBugging = 0;
    bugStartDist = 0;
    bugLastMoveDir = 0;
    bugLookStartDir = 0;
    bugRotationCount = 0;
    bugMovesSinceSeenObstacle = 0;
    here = [];
    mydest = [];
    
    lastdata = [];
    
    function val = move(d)
        here = d.addTo(here);
        val = true;
    end

    function val = canMove(d)
        % logic to say if we can move by that space
        tl = d.addTo(here);
        val = (lastdata.M(tl(1),tl(2)) <= 0);
    end

    function val = tryMoveDirect()
        
        toDest = Direction.directionTo(here,mydest);
        
        if (canMove(toDest))
            move(toDest);
            val = true;
            return
        end

        dirs = {[],[]};
        dirLeft = toDest.rotateLeft();
        dirRight = toDest.rotateRight();
        
        if (norm(dirLeft.addTo(here)-mydest) < norm(dirRight.addTo(here)-mydest))
            dirs{1} = dirLeft;
            dirs{2} = dirRight;
        else
            dirs{1} = dirRight;
            dirs{2} = dirLeft;
        end
        for i=1:2
            d = dirs{i};
            if (canMove(d))
                move(d);
                val = true;
                return
            end
        end
        val = false;
        
    end

    function startBug()
        bugStartDist = norm(here-mydest);
        bugLastMoveDir = Direction.directionTo(here,mydest);
        bugLookStartDir = Direction.directionTo(here,mydest);
        bugRotationCount = 0;
        bugMovesSinceSeenObstacle = 0;

        % try to intelligently choose on which side we will keep the wall
        leftTryDir = bugLastMoveDir.rotateLeft();
        for i=0:2
            if (~canMove(leftTryDir))
                leftTryDir = leftTryDir.rotateLeft();
            else
                break;
            end
        end
        
        rightTryDir = bugLastMoveDir.rotateRight();
        for i=0:2
            if (~canMove(rightTryDir))
                rightTryDir = rightTryDir.rotateRight();
            else
                break;
            end
        end
        
        if (norm(mydest-leftTryDir.addTo(here)) < norm(mydest-rightTryDir.addTo(here)))
            bugWallOnLeft = ~BUG_LEFT;
        else
            bugWallOnLeft = BUG_LEFT;
        end
    end

    function val = findBugMoveDir()
        bugMovesSinceSeenObstacle = bugMovesSinceSeenObstacle + 1;
        d = bugLookStartDir;
        for i=8:-1:1
            if (canMove(d))
                val = d;
                return
            end
            if bugWallOnLeft == BUG_LEFT
                d = d.rotateRight();
            else
                d = d.rotateLeft();
            end
            bugMovesSinceSeenObstacle = 0;
            
        end
        val = [];
    end

    function val = numRightRotations(start, fin)
        val = mod(fin.ordinal() - start.ordinal() + 8,8);
    end

    function val = numLeftRotations(start, fin)
        val = mod(-fin.ordinal() + start.ordinal() + 8,8);
    end

    function val = calculateBugRotation(moveDir)
        if (bugWallOnLeft == BUG_LEFT)
            val = numRightRotations(bugLookStartDir, moveDir) - numRightRotations(bugLookStartDir, bugLastMoveDir);
        else
            val = numLeftRotations(bugLookStartDir, moveDir) - numLeftRotations(bugLookStartDir, bugLastMoveDir);
        end
    end

    function bugMove(d)
        if (move(d))
            bugRotationCount = bugRotationCount + calculateBugRotation(d);
            bugLastMoveDir = d;
            if (bugWallOnLeft == BUG_LEFT)
                bugLookStartDir = d.rotateLeft().rotateLeft();
            else
                bugLookStartDir = d.rotateRight().rotateRight();
            end
        end
    end

    function val = detectBugIntoEdge()
        %{
        if (bugWallOnLeft == BUG_LEFT)
            val = rc.senseTerrainTile(here.add(bugLastMoveDir.rotateLeft())) == TerrainTile.OFF_MAP;
        else
            val = rc.senseTerrainTile(here.add(bugLastMoveDir.rotateRight())) == TerrainTile.OFF_MAP;
        end
        %}
        val = false;
    end

    function reverseBugWallFollowDir()
        bugWallOnLeft = ~bugWallOnLeft;
        startBug();
    end

    function bugTurn()
        if (detectBugIntoEdge())
            reverseBugWallFollowDir();
        end
        d = findBugMoveDir();
        if (~isempty(d))
            bugMove(d);
        end
    end
    
    function val = canEndBug()
        if (bugMovesSinceSeenObstacle >= 4) 
            val = true;
            return
        end
        val = (bugRotationCount <= 0 || bugRotationCount >= 8) && (norm(here-mydest) <= bugStartDist);
    end
    
    function [newloc, data] = movefn(loc,dest,data)
        here = loc;
        mydest = dest;
        lastdata = data;
        
        if (bugIsBugging)
            if canEndBug()
                bugIsBugging = 0;
            end
        else
            if (~tryMoveDirect()) 
                bugIsBugging = 1;
                startBug();
            end
        end

        if (bugIsBugging)
            bugTurn();
        end
        
        newloc = here;
    end

    function data = updatefn(data)
        % do nothing to globals for now
    end

    switch command
        case 'new'
            output_fn = @movefn;
        case 'update'
            output_fn = @updatefn;
        otherwise
            return
    end
end

