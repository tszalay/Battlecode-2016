function runPathfinding(mapname, nav)

    % load map
    if ischar(mapname)
        mf = matfile(mapname);
        M = mf.M;
    else
        M = mapname;
    end
    N = size(M,1);
    
    % create visibility mask
    visMask = 0*M;
    
    % get spawn location
    baseLocations = find(M==2);
    [I,J] = ind2sub(size(M),baseLocations);
    spawnLocation = [I(1),J(1)];
    enemyLocation = [I(2),J(2)];
    
    % positions of units
    unitLocations = [];
    % nav functions of units
    unitFunctions = {};
    
    
    % game constants
    spawnDelay = 10;
    sightRadius = 8;
    
    lastSpawn = -100;
    
    for roundNum=1:1000
        % check if we spawn
        if (roundNum-lastSpawn) >= spawnDelay
            unitLocations(end+1,:) = spawnLocation;
            unitFunctions{end+1} = nav('new');
            lastSpawn = roundNum;
        end
        
        % move units
        data = [];
        data.M = M;
        for i=1:size(unitLocations,1)
            newloc = unitFunctions{i}(unitLocations(i,:),enemyLocation,data);
            if (M(newloc(1),newloc(2)) == 0)
                unitLocations(i,:) = newloc;
            end
        end
        
        % update sight masks
        for i=1:size(unitLocations,1)
            visMask = visMask | sightMask(M,unitLocations(i,:),sightRadius);
        end
        
        % redraw
        M1 = M;
        for i=1:size(unitLocations,1)
            M1(unitLocations(i,1),unitLocations(i,2)) = 1.5;
        end
        fig = figure(1);
        subplot(121)
        imagesc(M1);
        daspect([1 1 1])
        
        subplot(122)
        M1 = M1+0.5;
        M1(~visMask) = 0;
        imagesc(M1);
        daspect([1 1 1])
        %colormap(gca,hot(5))
        
        pause(0.1)
    end
end

