package net.thechubbypanda.larrysescape.map;

import com.badlogic.gdx.math.MathUtils;
import net.thechubbypanda.larrysescape.Globals;

import java.util.Stack;

public class RecursiveCellMap extends CellMap {

	private final int size;

	/**
	 * Uses recursive back-tracker to generate a maze
	 */
	public RecursiveCellMap(int size) {
		this.size = size;

		map = new Cell[size][size];

		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				map[x][y] = new Cell(x, y);
			}
		}

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

	public int getSize() {
		return size;
	}
}
