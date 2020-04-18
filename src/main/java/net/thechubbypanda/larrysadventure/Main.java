package net.thechubbypanda.larrysadventure;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import net.thechubbypanda.larrysadventure.screens.Play;
import net.thechubbypanda.larrysadventure.signals.ResizeSignal;

import static net.thechubbypanda.larrysadventure.Globals.*;

public class Main extends Game {

	@Override
	public void create() {
		setScreen(new Play());
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		resizeSignal.dispatch(new ResizeSignal(width, height));
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
}
