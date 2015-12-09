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
    
    % initialize pathfinder
    % pathfind is now a _function_ returned by nav
    % which should get called every step
    pathfind = nav(M);

    % get spawn location
    spawnLocation = find(M==2,1,'first');
    [I,J] = ind2sub(size(M),spawnLocation);
    spawnLocation = [I,J];
    
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
        end
        
        % move units
        for i=1:size(unitLocations,1)
            unitLocations(i,:) = unitLocations(i,:) + [1,1];
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
        
        pause(0.5)
    end
end

