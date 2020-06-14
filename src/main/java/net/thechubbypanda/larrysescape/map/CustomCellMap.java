package net.thechubbypanda.larrysescape.map;

public class CustomCellMap extends CellMap {

	public CustomCellMap(int width, int height) {
		map = new Cell[width][height];

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				map[x][y] = new Cell(x, y);
			}
		}

		Cell[] neighbours;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				getNeighbours(map[x][y], neighbours = new Cell[4]);
				map[x][y].up = neighbours[0];
				map[x][y].left = neighbours[1];
				map[x][y].down = neighbours[2];
				map[x][y].right = neighbours[3];
			}
		}
	}
}
