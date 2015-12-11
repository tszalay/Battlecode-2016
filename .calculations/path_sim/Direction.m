classdef Direction
    
    properties (Access=public)
        value
    end
    
    methods (Access=public)
        
        % initialize with ordinal value
        function d = Direction(val)
            d.value = val;
        end
        
        function val = ordinal(d)
            val = d.value;
        end
        
        function val = rotateLeft(d)
            newd = d.value - 1;
            if newd < 0
                newd = newd + 8;
            end
            val = Direction(newd);
        end
        
        function val = rotateRight(d)
            newd = d.value + 1;
            if newd >= 8
                newd = newd - 8;
            end
            val = Direction(newd);
        end
        
        function loc = addTo(d, loc)
            switch d.value
                case 0
                    loc = loc + [0, -1];
                case 1
                    loc = loc + [1, -1];
                case 2
                    loc = loc + [1, 0];
                case 3
                    loc = loc + [1, 1];
                case 4
                    loc = loc + [0, 1];
                case 5
                    loc = loc + [-1, 1];
                case 6
                    loc = loc + [-1, 0];
                case 7
                    loc = loc + [-1, -1];
            end
        end
        
    end
    
    methods(Access=public, Static)
        
        function d = directionTo(from, to)
            
            delta = to - from;
            theta = pi - atan2(delta(1),delta(2));
            
            val = round(4*theta/pi);
            if val == 8
                val = 0;
            end
            
            d = Direction(val);
            
        end
        
    end
    
end

