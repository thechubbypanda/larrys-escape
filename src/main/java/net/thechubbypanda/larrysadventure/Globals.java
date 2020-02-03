package net.thechubbypanda.larrysadventure;

public class Globals {

	public static final String TITLE = "Larry's Adventure";

	// Pixels per meter, Box2D conversion value
	public static final float PPM = 100;

	public static class SystemPriority {
		public static final int VIEWPORT = 1;
		public static final int GL_INIT = 2;
		public static final int MAIN_RENDER = 3;
		public static final int LIGHT_RENDER = 4;
		public static final int DEBUG_RENDER = 5;
	}

	public static boolean SOUND = true;

	public static boolean DEBUG = true;

	private Globals() {
	}
}
