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

scatter(senseLocs(:,1),senseLocs(:,2))
xlim([-6 6]);
ylim([-6 6]);

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
fclose(fid);