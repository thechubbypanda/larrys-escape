package net.thechubbypanda.larrysadventure.map;

import com.badlogic.gdx.math.MathUtils;
import net.thechubbypanda.larrysadventure.Globals;

import java.util.Stack;

public class CellMap {

	private Cell[][] map;
	private int size;

	public CellMap(int size) {
		this.size = size;
		map = new Cell[size][size];
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				map[x][y] = new Cell(x, y);
			}
		}

		generateMaze();
	}

	private void generateMaze() {
		Stack<Cell> stack = new Stack<>();
		Cell[] neighbours = new Cell[4];
		Cell currentCell = map[0][0];
		currentCell.visited = true;
		stack.push(currentCell);
		while (!stack.empty()) {
			currentCell = stack.pop();
			getNeighbours(currentCell, neighbours);
			if (containsUnvisited(neighbours)) {
				stack.push(currentCell);
				Cell nextCell;
				int index;
				do {
					index = MathUtils.random(neighbours.length - 1);
					nextCell = neighbours[index];
				} while (nextCell == null);
				System.out.println(index+1 + "/" + neighbours.length);
				if (index == 0) { // top
					currentCell.wallTop = false;
					nextCell.wallBottom = false;
				} else if (index == 1) { // left
					currentCell.wallLeft = false;
					nextCell.wallRight = false;
				} else if (index == 2) { // bottom
					currentCell.wallBottom = false;
					nextCell.wallTop = false;
				} else { // right
					currentCell.wallRight = false;
					nextCell.wallLeft = false;
				}
				nextCell.visited = true;
				stack.push(nextCell);
			}

//			for (int y = size - 1; y >= 0; y--) {
//				for (int x = 0; x < size; x++) {
//					System.err.print(map[x][y].wallTop ? " _ " : "   ");
//				}
//				System.err.println();
//				for (int x = 0; x < size; x++) {
//					System.err.print((map[x][y].wallLeft ? "| " : "  ") + (map[x][y].wallRight ? "|" : " "));
//				}
//				System.err.println();
//				for (int x = 0; x < size; x++) {
//					System.err.print(map[x][y].wallBottom ? " - " : "   ");
//				}
//				System.err.println();
//			}
//			System.err.println();
//			System.err.flush();
		}

	}
// TODO: Something wrong with determining whether there are cells available
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

	public Cell[][] getMap() {
		return map;
	}
}