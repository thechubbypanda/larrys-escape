package net.thechubbypanda.larrysadventure.map;

import com.badlogic.gdx.graphics.Texture;
import net.thechubbypanda.larrysadventure.Globals;

public class Tile {

	public static final int SIZE = 128;
	public static final Texture TEXTURE = Globals.assets.get(Globals.Textures.GRASS);

	public int x, y;

	public Tile(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void render() {

	}
}
