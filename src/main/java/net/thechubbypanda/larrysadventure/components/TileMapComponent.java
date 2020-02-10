package net.thechubbypanda.larrysadventure.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.thechubbypanda.larrysadventure.map.Cell;
import net.thechubbypanda.larrysadventure.map.CellMap;
import net.thechubbypanda.larrysadventure.map.Tile;

public class TileMapComponent implements Component {

	private Tile[][] map;
	private final int size;

	public TileMapComponent(CellMap cellMap) {
		Cell[][] cMap = cellMap.getMap();
		size = cMap.length;
		map = new Tile[size][size];
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				map[x][y] = new Tile(cMap[x][y]);
			}
		}
	}

	public void render(SpriteBatch batch) {
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				map[x][y].render(batch);
			}
		}
	}
}
