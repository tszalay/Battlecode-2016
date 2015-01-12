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


fclose(fid);

