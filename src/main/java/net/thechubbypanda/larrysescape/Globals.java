package net.thechubbypanda.larrysescape;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;

public class Globals {

	public static final String TITLE = "Larry's Escape";

	// Pixels per meter, Box2D conversion value
	public static final float PPM = 128;

	public static final float AMBIENT_INTENSITY = 0.13f;
	public static final Color AMBIENT_COLOR = new Color(AMBIENT_INTENSITY, AMBIENT_INTENSITY, AMBIENT_INTENSITY, 1f);

	public static boolean CAN_SHOOT = false;
	public static boolean FAST_FIRING = false;
	public static int HEALTH = 100;

	public static void reset() {
		CAN_SHOOT = false;
		FAST_FIRING = false;
		HEALTH = 100;
	}

	public static class SystemPriority {
		public static final int PRE_UPDATE = -1;
		public static final int UPDATE = 0;
		public static final int POST_UPDATE = 1;

		public static final int PRE_RENDER = 10;
		public static final int RENDER = 11;
		public static final int POST_RENDER = 12;
	}

	public static boolean DEBUG = false;

	public static final AssetManager assets = new AssetManager();

	public static class Textures {
		public static final String GRASS = "grass.png";
		public static final String WALL_VERT = "wallVert.png";
		public static final String WALL_HORIZ = "wallHoriz.png";
		public static final String WALL_CORNER = "wallCorner.png";
		public static final String LARRY = "tempLarry2.png";
		public static final String ENEMY = "tempEnemy.png";
	}

	public static HUD HUD;

	private Globals() {
	}
}
