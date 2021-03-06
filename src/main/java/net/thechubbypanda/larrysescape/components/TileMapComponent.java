package net.thechubbypanda.larrysescape.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.World;
import net.thechubbypanda.larrysescape.map.Cell;
import net.thechubbypanda.larrysescape.map.CellMap;
import net.thechubbypanda.larrysescape.map.Tile;

public class TileMapComponent implements Component {

	private final CellMap cellMap;
	private final Tile[][] map;

	public TileMapComponent(World world, CellMap cellMap) {
		Cell[][] cMap = cellMap.getMap();
		this.cellMap = cellMap;
		map = new Tile[cMap.length][cMap[0].length];
		for (int x = 0; x < cMap.length; x++) {
			for (int y = 0; y < cMap[0].length; y++) {
				map[x][y] = new Tile(world, cMap[x][y]);
			}
		}
	}

	public void draw(Batch batch) {
		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map[0].length; y++) {
				map[x][y].render(batch);
			}
		}
	}

	public void removeBodies(World world) {
		for (Tile[] tiles : map) {
			for (Tile tile : tiles) {
				tile.removeBody(world);
			}
		}
	}

	public CellMap getCellMap() {
		return cellMap;
	}
}
