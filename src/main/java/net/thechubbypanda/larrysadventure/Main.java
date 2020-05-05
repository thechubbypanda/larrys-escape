package net.thechubbypanda.larrysadventure;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import net.thechubbypanda.larrysadventure.screens.MainMenu;

import static net.thechubbypanda.larrysadventure.Globals.*;

public class Main extends Game {

	@Override
	public void create() {
		//Graphics
		Gdx.gl.glClearColor(.1f, .1f, .1f, 1);

		// Load initial files
		assets.load(Globals.Textures.GRASS, Texture.class);
		assets.load(Globals.Textures.WALL_CORNER, Texture.class);
		assets.load(Globals.Textures.WALL_VERT, Texture.class);
		assets.load(Globals.Textures.WALL_HORIZ, Texture.class);
		assets.load("flatearthui/flat-earth-ui.json", Skin.class);
		assets.load("levelExit.png", Texture.class);
		assets.load("bullet.png", Texture.class);
		assets.load("health.png", Texture.class);
		assets.finishLoading();

		setScreen(new MainMenu());
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

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

		new Lwjgl3Application(new Main(), config);
	}

	@Override
	public void dispose() {
		super.dispose();
		assets.dispose();
	}
}
