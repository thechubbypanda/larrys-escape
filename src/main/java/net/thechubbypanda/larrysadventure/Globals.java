package net.thechubbypanda.larrysadventure;

public class Globals {

	public static final String TITLE = "Larry's Adventure";

	// Pixels per meter, Box2D conversion value
	public static final float PPM = 100;

	public static boolean SOUND = true;

	public static boolean DEBUG = true;

	public class SystemPriority {
		public static final int CAMERA = 1;
		public static final int GLINIT = 2;
		public static final int RENDER = 3;
	}

	private Globals() {
	}
}
