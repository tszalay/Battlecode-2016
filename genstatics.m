%% generate sense locations arrays
senseLocs = [];
for x=-5:5
    for y=-5:5
        r2 = x^2+y^2;
        senseLocs(end+1,1:3) = [x y sqrt(r2)];
    end
end

senseLocs = sortrows(senseLocs,3);
% limit it to sight range 35 (full)
senseLocs = senseLocs(senseLocs(:,3) < sqrt(35),:);

%{
scatter(senseLocs(:,1),senseLocs(:,2))
xlim([-6 6]);
ylim([-6 6]);
%}

% now count short and long sensor ranges
nshort = sum(senseLocs(:,3) < sqrt(24));
nlong = size(senseLocs,1);

% and output text file
fid = fopen('senseLocs.txt','w');
fprintf(fid,'static int senseLocsShort = %d;\n',nshort);
fprintf(fid,'static int senseLocsLong = %d;\n',nlong);
% and the array
fprintf(fid,'static int[] senseLocsX = {');
for i=1:size(senseLocs,1)
    fprintf(fid,'%d',senseLocs(i,1));
    if i < size(senseLocs,1)
        fprintf(fid,',');
    end
end
fprintf(fid,'};\n');
fprintf(fid,'static int[] senseLocsY = {');
for i=1:size(senseLocs,1)
    fprintf(fid,'%d',senseLocs(i,2));
    if i < size(senseLocs,1)
        fprintf(fid,',');
    end
end
fprintf(fid,'};\n');
fprintf(fid,'static int[] senseLocsR = {');
for i=1:size(senseLocs,1)
    fprintf(fid,'%d',floor(10*senseLocs(i,3)));
    if i < size(senseLocs,1)
        fprintf(fid,',');
    end
end
fprintf(fid,'};\n');

% now output precomputed inverse square roots
% (up to max sight range of anything + a bit)
sqmax = 81;
sqrts = sqrt(0:sqmax);
invsqrts = 1./sqrt(0:sqmax);
invsqrts(1) = 0;

fprintf(fid,'static float[] sqrt = {');
for i=1:numel(sqrts)
    fprintf(fid,'%ff',sqrts(i));
    if i < numel(sqrts)
        fprintf(fid,',');
    end
end
fprintf(fid,'};\n');

fprintf(fid,'static float[] invSqrt = {');
for i=1:numel(invsqrts)
    fprintf(fid,'%ff',invsqrts(i));
    if i < numel(invsqrts)
        fprintf(fid,',');
    end
end
fprintf(fid,'};\n');
%
% now generate our 25 adjacency integers
adjs = zeros(25,1);
bitedge = zeros(4,1);
for y=-2:2
    for x=-2:2
        ind = (x+2)+5*(y+2);
        adj = bitshift(1,ind);
        for dx=-1:1
            for dy=-1:1
                curx = x+dx;
                cury = y+dy;
                di = 5*dy+dx;
                if curx>=-2 && curx <=2 && cury>=-2 && cury<=2
                    adj = bitor(adj,bitshift(1,ind+di));
                end
            end
        end
        adjs(ind+1) = adj;
    end
end
for i=1:5
    bitedge(1) = bitor(bitedge(1),bitshift(1,i-1));
    bitedge(2) = bitor(bitedge(2),bitshift(1,(i-1)*5+4));
    bitedge(3) = bitor(bitedge(3),bitshift(1,(i-1)*5));
    bitedge(4) = bitor(bitedge(4),bitshift(1,i-1+20));
end
%{
pos = 4;
for i=1:numel(bitedge)
    imagesc(reshape(bitget(bitedge(i),1:25),5,5)')
    %pause
    bs = find(bitget(pos,1:25));
    for b=bs
        pos = bitor(pos,adjs(b));
    end
end
%}
fprintf(fid,'static int[] bitAdjacency = {');
for i=1:numel(adjs)
    fprintf(fid,'%d',adjs(i));
    if i < numel(adjs)
        fprintf(fid,',');
    end
end
fprintf(fid,'};\n');

fprintf(fid,'static int[] bitEdge = {');
for i=1:numel(bitedge)
    fprintf(fid,'%d',bitedge(i));
    if i < numel(bitedge)
        fprintf(fid,',');
    end
end
fprintf(fid,'};\n');


% unit hard-shell repulsion bit-twiddling
attackbits = zeros(13,13);
ranges = [8 15 24 35];
xs = [0 1 1 1 0 -1 -1 -1 0];
ys = -[1 1 0 -1 -1 -1 0 1 0];
for i=1:numel(xs)
    for x=-6:6
        for y=-6:6
            % can unit hit this location?
            for r=1:numel(ranges)
                if (xs(i)-x)^2+(ys(i)-y)^2 <= ranges(r)
                    attackbits(x+7,y+7) = bitor(attackbits(x+7,y+7),bitshift(1,(i-1)+(r-1)*numel(xs)));
                end
            end
        end
    end
end

fprintf(fid,'static long[] attackMask = {');
for i=1:numel(attackbits)
    fprintf(fid,'%d',attackbits(i));
    if i < numel(attackbits)
        fprintf(fid,',');
    end
end
fprintf(fid,'};\n');

imagesc(bitand(attackbits,bitshift(1,14)))

fclose(fid);
