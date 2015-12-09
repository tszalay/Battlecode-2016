function Asight = sightMask(M, loc, radius)

    [XX, YY] = meshgrid(0:size(M,1)-1);

    Asight = ((XX-loc(1)).^2+(YY-loc(2)).^2) < radius^2;
    
end

