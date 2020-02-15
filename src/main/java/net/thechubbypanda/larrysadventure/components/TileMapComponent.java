package net.thechubbypanda.larrysadventure.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import net.thechubbypanda.larrysadventure.map.Cell;
import net.thechubbypanda.larrysadventure.map.CellMap;
import net.thechubbypanda.larrysadventure.map.Tile;

public class TileMapComponent implements Component {

	private Tile[][] map;
	private final int size;

	public TileMapComponent(World world, CellMap cellMap) {
		Cell[][] cMap = cellMap.getMap();
		size = cMap.length;
		map = new Tile[size][size];
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				map[x][y] = new Tile(world, cMap[x][y]);
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

	public void removeBodies(World world) {
		for (Tile[] tiles : map) {
			for(Tile tile : tiles) {
				tile.removeBody(world);
			}
		}
	}
}
