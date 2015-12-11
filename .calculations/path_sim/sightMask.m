function Asight = sightMask(M, loc, radius)

    [XX, YY] = meshgrid(1:size(M,1));

    Asight = ((XX-loc(2)).^2+(YY-loc(1)).^2) < radius^2;
    
end

