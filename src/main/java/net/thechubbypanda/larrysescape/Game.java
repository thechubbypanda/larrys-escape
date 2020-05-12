package net.thechubbypanda.larrysescape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import net.thechubbypanda.larrysescape.screens.Menu;
import net.thechubbypanda.larrysescape.screens.Pause;
import net.thechubbypanda.larrysescape.screens.Play;

import java.util.HashMap;

import static net.thechubbypanda.larrysescape.Globals.*;

public class Game extends com.badlogic.gdx.Game {

	private ShapeRenderer sr;
	private float fadeAlpha = 0;
	private Screens nextScreen;
	private boolean fadeIn = true;
	private FadeListener fadeListener;

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

		sr = new ShapeRenderer();
		sr.setColor(0, 0, 0, 0);
		Gdx.gl.glBlendFunc(Gdx.gl.GL_SRC_ALPHA, Gdx.gl.GL_ONE_MINUS_SRC_ALPHA);

		setScreen(Screens.menu);
	}

	public void fadeScreen(Screens screen) {
		fadeIn = false;
		nextScreen = screen;
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

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	public void setScreen(Screens screen) {
		setScreen(screens.get(screen));
	}

	public void fade(FadeListener fadeListener) {
		fadeIn = false;
		this.fadeListener = fadeListener;
	}

	@Override
	public void render() {
		if (screen != null) screen.render(Gdx.graphics.getDeltaTime());
		if (fadeIn) {
			fadeAlpha = MathUtils.clamp(fadeAlpha - Gdx.graphics.getDeltaTime(), 0, 1);
		} else {
			fadeAlpha = MathUtils.clamp(fadeAlpha + Gdx.graphics.getDeltaTime(), 0, 1);
			if (fadeAlpha == 1) {
				fadeIn = true;
				if (nextScreen != null) {
					setScreen(nextScreen);
					nextScreen = null;
				}
				if (fadeListener != null) {
					fadeListener.atMiddleOfFade();
					fadeListener = null;
				}
			}
		}
		Gdx.gl.glEnable(Gdx.gl.GL_BLEND);
		sr.setColor(0, 0, 0, fadeAlpha);
		sr.begin(ShapeRenderer.ShapeType.Filled);
		sr.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		sr.end();
		Gdx.gl.glDisable(Gdx.gl.GL_BLEND);
	}

	public enum Screens {
		menu, play, pause
	}

	@Override
	public void dispose() {
		super.dispose();
		assets.dispose();
	}
}
