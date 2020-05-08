package net.thechubbypanda.larrysadventure;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;

public class Globals {

	public static final String TITLE = "Larry's Adventure";

	// Pixels per meter, Box2D conversion value
	public static final float PPM = 128;

	public static final float AMBIENT_INTENSITY = 0.13f;
	public static final Color AMBIENT_COLOR = new Color(AMBIENT_INTENSITY, AMBIENT_INTENSITY, AMBIENT_INTENSITY, 1f);

	public static class SystemPriority {
		public static final int VIEWPORT = 1;
		public static final int GL_INIT = 2;
		public static final int MAP_RENDER = 3;
		public static final int MAIN_RENDER = 4;
		public static final int PLAYER_RENDER = 5;
		public static final int LIGHT_RENDER = 6;
		public static final int DEBUG_RENDER = 7;
	}

	public static boolean DEBUG = false;

	public static final AssetManager assets = new AssetManager();

	public static class Textures {
		public static final String GRASS = "grass.png";
		public static final String WALL_VERT = "wallVert.png";
		public static final String WALL_HORIZ = "wallHoriz.png";
		public static final String WALL_CORNER = "wallCorner.png";
	}

	public enum Screens {
		mainMenu, play;

	}

	private Globals() {
	}
}
