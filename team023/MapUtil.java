package team023;

import battlecode.common.MapLocation;

public class MapUtil
{
	// AK check if map location is odd or even.
	public static boolean isLocOdd(MapLocation loc) {
		if ((loc.x+loc.y)%2 == 0){
			Debug.setStringAK("EVEN loc");
			return false;
		}
		else {
			Debug.setStringAK("ODD loc");
			return true;
		}
	}


	// AK check if map location is four-odd or four-even.
	public static boolean isFourOdd(MapLocation loc) {
		if (isLocOdd(loc)) {
			if ((loc.x)%2 == 1) {
				Debug.setStringAK("FourODD loc");
				return true;
			}
			else {			
			Debug.setStringAK("FourEVEN loc");
			return false;
			}
		}
		else {
			Debug.setStringAK("FourEVEN loc");
			return false;
		}
	}
}
