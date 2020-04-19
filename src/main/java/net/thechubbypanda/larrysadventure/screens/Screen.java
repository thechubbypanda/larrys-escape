package net.thechubbypanda.larrysadventure.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.ScreenAdapter;

public abstract class Screen extends ScreenAdapter {

	protected Game game;

	public Screen(Game game) {
		this.game = game;
	}

	protected void setScreen(com.badlogic.gdx.Screen screen) {
		game.setScreen(screen);
	}

	protected com.badlogic.gdx.Screen getScreen() {
		return game.getScreen();
	}
}
