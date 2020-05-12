package net.thechubbypanda.larrysescape.map;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import net.thechubbypanda.larrysescape.Globals;

import java.util.ArrayList;
import java.util.Stack;

public class CellMap {

	private final Cell[][] map;
	private final int size;

	public CellMap(int size) {
		this.size = size;

		map = new Cell[size][size];

		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				map[x][y] = new Cell(x, y);
			}
		}

		generate();
	}

	/**
	 * Uses recursive back-tracker to generate a maze
	 */
	private void generate() {
		Stack<Cell> stack = new Stack<>();
		Cell[] neighbours = new Cell[4];
		Cell currentCell = map[0][0];
		currentCell.visited = true;
		stack.push(currentCell);
		Cell nextCell = null;

		while (!stack.empty()) {
			currentCell = stack.pop();
			getNeighbours(currentCell, neighbours);
			if (containsUnvisited(neighbours)) {
				stack.push(currentCell);
				int index;
				do {
					index = MathUtils.random(neighbours.length - 1);
					nextCell = neighbours[index];
				} while (nextCell == null || nextCell.visited);
				if (index == 0) { // top
					currentCell.up = nextCell;
					nextCell.down = currentCell;
				} else if (index == 1) { // left
					currentCell.left = nextCell;
					nextCell.right = currentCell;
				} else if (index == 2) { // bottom
					currentCell.down = nextCell;
					nextCell.up = currentCell;
				} else { // right
					currentCell.right = nextCell;
					nextCell.left = currentCell;
				}
				nextCell.visited = true;
				stack.push(nextCell);
			}

			if (Globals.DEBUG) {
				for (int y = size - 1; y >= 0; y--) {
					for (int x = 0; x < size; x++) {
						System.out.print(map[x][y].up == null ? " _ " : "   ");
					}
					System.out.println();
					for (int x = 0; x < size; x++) {
						System.out.print((map[x][y].left == null ? "|" : " ") + (currentCell == map[x][y] ? "+" : (nextCell == map[x][y] ? "*" : " ")) + (map[x][y].right == null ? "|" : " "));
					}
					System.out.println();
					for (int x = 0; x < size; x++) {
						System.out.print(map[x][y].down == null ? " - " : "   ");
					}
					System.out.println();
				}
				System.out.println();
			}
		}
	}

	// TODO: Something wrong with determining whether there are cells available
	// Can't see any issues?
	private void getNeighbours(Cell cell, Cell[] neighbours) {
		Cell top = null, left = null, bottom = null, right = null;
		if (cell.y != size - 1)
			top = map[cell.x][cell.y + 1];
		if (cell.x != 0)
			left = map[cell.x - 1][cell.y];
		if (cell.y != 0)
			bottom = map[cell.x][cell.y - 1];
		if (cell.x != size - 1)
			right = map[cell.x + 1][cell.y];

		neighbours[0] = top;
		neighbours[1] = left;
		neighbours[2] = bottom;
		neighbours[3] = right;
	}

	private static boolean containsUnvisited(Cell[] cells) {
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

	public int getSize() {
		return size;
	}
}