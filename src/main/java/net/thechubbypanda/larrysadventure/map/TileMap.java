package net.thechubbypanda.larrysadventure.map;

import com.badlogic.gdx.graphics.Texture;
import net.thechubbypanda.larrysadventure.Globals;

public class TileMap {

	private final Texture tile;

	public TileMap(CellMap cellMap) {
		tile = Globals.assets.get(Globals.Textures.GRASS);
	}
}
