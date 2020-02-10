package net.thechubbypanda.larrysadventure;

import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.assets.AssetManager;
import net.thechubbypanda.larrysadventure.signals.InputSignal;
import net.thechubbypanda.larrysadventure.signals.ResizeSignal;

public class Globals {

	public static final String TITLE = "Larry's Adventure";

	// Pixels per meter, Box2D conversion value
	public static final float PPM = 100;

	public static class SystemPriority {
		public static final int VIEWPORT = 1;
		public static final int GL_INIT = 2;
		public static final int MAIN_RENDER = 3;
		public static final int PLAYER_RENDER = 4;
		public static final int LIGHT_RENDER = 5;
		public static final int DEBUG_RENDER = 6;
	}

	public static final Signal<ResizeSignal> resizeSignal = new Signal<>();
	public static final Signal<InputSignal> inputSignal = new Signal<>();

	public static boolean SOUND = true;

	public static boolean DEBUG = false;

	public static AssetManager assets;

	public static class Textures {
		public static final String GRASS = "grass.png";
	}

	private Globals() {
	}
}
