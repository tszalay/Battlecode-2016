function M = makeMap(N, name)

    if nargin < 2
        name = 'foo';
    end
    if nargin < 1
        N = 40;
    end

    M = zeros(N);
    M(10,10) = 2;
    M(N-10,N-10) = 2;
    M([1 end],:) = 1;
    M(:,[1 end]) = 1;
    
    function mouseDown(src,~)
        val = strcmp(src.SelectionType,'normal');
        src.WindowButtonMotionFcn = @(~,~) mouseMove(val);
    end
    function mouseUp(src,~)
        src.WindowButtonMotionFcn = '';
    end
    function mouseMove(val)
        % do this thing
        x = floor(ah.CurrentPoint(1,1));
        y = floor(ah.CurrentPoint(1,2));
        M(y,x) = val;
        % and reflect around the center
        y = N-y;
        x = N-x;
        M(y,x) = val;
        imagesc(M);
        daspect([1 1 1])
%        ah.YDir = 'reverse';
    end
    
    fig = figure(1);
    fig.WindowButtonDownFcn = @mouseDown;
    fig.WindowButtonUpFcn = @mouseUp;

    ah = axes();
    imagesc(M);
    daspect([1 1 1])
%    ah.YDir = 'reverse';
        
    while ishandle(fig)
        pause(0.1);
    end
    
    mf = matfile(name,'Writable',true);
    mf.M = M;
end

