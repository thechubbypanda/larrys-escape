package net.thechubbypanda.larrysadventure;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import net.thechubbypanda.larrysadventure.screens.Menu;
import net.thechubbypanda.larrysadventure.screens.Pause;
import net.thechubbypanda.larrysadventure.screens.Play;

import java.util.HashMap;

import static net.thechubbypanda.larrysadventure.Globals.*;

public class Game extends com.badlogic.gdx.Game {

	@Override
	public void create() {
		//Graphics
		Gdx.gl.glClearColor(.1f, .1f, .1f, 1);

		// Load initial files
		assets.load(Textures.GRASS, Texture.class);
		assets.load(Textures.WALL_CORNER, Texture.class);
		assets.load(Textures.WALL_VERT, Texture.class);
		assets.load(Textures.WALL_HORIZ, Texture.class);
		assets.load("flatearthui/flat-earth-ui.json", Skin.class);
		assets.load("levelExit.png", Texture.class);
		assets.load("bullet.png", Texture.class);
		assets.load("health.png", Texture.class);
		assets.load("ammo.png", Texture.class);
		assets.finishLoading();

		screens.put(Screens.menu, new Menu());
		screens.put(Screens.play, new Play());
		screens.put(Screens.pause, new Pause());

		setScreen(Screens.menu);
	}

	private final HashMap<Screens, Screen> screens = new HashMap<>();

	public static void main(String[] args) {
		if (args.length > 0) {
			if (args[0].equals("debug")) {
				DEBUG = true;
			}
		}

		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

		config.setMaximized(true);
		config.setMaximizedMonitor(Lwjgl3ApplicationConfiguration.getPrimaryMonitor());
		config.setTitle(TITLE);
		config.setWindowPosition(-1, -1);
		config.setWindowIcon("icon.png");

		new Lwjgl3Application(new Game(), config);
	}

	public void reset() {
		((Play) screens.get(Screens.play)).reset();
	}

	public enum Screens {
		menu, play, pause;
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	public void setScreen(Screens screen) {
		setScreen(screens.get(screen));
	}

	@Override
	public void dispose() {
		super.dispose();
		assets.dispose();
	}
}
