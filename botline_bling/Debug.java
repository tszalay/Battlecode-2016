package botline_bling;

public class Debug extends RobotPlayer
{
	static String[] auStrings = new String[4];
	
	public static void setStringAK(String s)
	{
		auStrings[0] = s;
		update();
	}
	
	public static void setStringSJF(String s)
	{
		auStrings[1] = s;
		update();
	}
	
	public static void setStringTS(String s)
	{
		auStrings[2] = s;
		update();
	}
	
	public static void setStringRR(String s)
	{
		auStrings[3] = s;
		update();
	}
	
	static void update()
	{
		rc.setIndicatorString(0, "AK: " + auStrings[0]);
		rc.setIndicatorString(1, "SJF: " + auStrings[1]);
		rc.setIndicatorString(2, "TS: " + auStrings[2] + " || RR: " + auStrings[3]);		
	}
}
