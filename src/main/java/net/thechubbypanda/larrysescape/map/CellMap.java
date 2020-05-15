package net.thechubbypanda.larrysescape.map;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class CellMap {

	protected Cell[][] map;

	// TODO: Something wrong with determining whether there are cells available
	// Can't see any issues?
	protected void getNeighbours(Cell cell, Cell[] neighbours) {
		Cell top = null, left = null, bottom = null, right = null;
		if (cell.y != map[0].length - 1)
			top = map[cell.x][cell.y + 1];
		if (cell.x != 0)
			left = map[cell.x - 1][cell.y];
		if (cell.y != 0)
			bottom = map[cell.x][cell.y - 1];
		if (cell.x != map.length - 1)
			right = map[cell.x + 1][cell.y];

		neighbours[0] = top;
		neighbours[1] = left;
		neighbours[2] = bottom;
		neighbours[3] = right;
	}

	protected static boolean containsUnvisited(Cell[] cells) {
		for (Cell cell : cells) {
			if (cell != null && !cell.visited)
				return true;
		}
		return false;
	}

	/**
	 * Gets all the dead ends of the maze
	 *
	 * @return Dead end cells
	 */
	public ArrayList<Cell> getDeadEnds() {
		ArrayList<Cell> deadEnds = new ArrayList<>();

		for (Cell[] cells : map) {
			for (Cell cell : cells) {
				int count = 0;
				if (cell.up == null) count++;
				if (cell.left == null) count++;
				if (cell.down == null) count++;
				if (cell.right == null) count++;

				if (count == 3) deadEnds.add(cell);
			}
		}

		return deadEnds;
	}

	public Cell getClosestCell(Vector2 position) {
		position.scl(1 / 128f);
		return map[Math.round(position.x)][Math.round(position.y)];
	}

	public Cell[][] getMap() {
		return map;
	}
}