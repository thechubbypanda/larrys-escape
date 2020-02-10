package net.thechubbypanda.larrysadventure.map;

import com.badlogic.gdx.math.MathUtils;

import java.util.Stack;

public class CellMap {

	private Cell[][] map;

	public CellMap(int size) {
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
			getNeighbours(map, currentCell, neighbours);
			if (containsUnvisited(neighbours)) {
				stack.push(currentCell);
				Cell nextCell;
				int index;
				do {
					index = MathUtils.random(neighbours.length);
					nextCell = neighbours[index];
				} while (nextCell == null);
				if (index == 0) { // top
					currentCell.wallTop = false;
					nextCell.wallBottom = false;
				} else if (index == 1) { // left
					currentCell.wallLeft = false;
					nextCell.wallRight = false;
				} else if (index == 2) { // bottom
					currentCell.wallBottom = false;
					currentCell.wallTop = false;
				} else { // right
					currentCell.wallRight = false;
					currentCell.wallLeft = false;
				}
			}
		}
	}

	private static void getNeighbours(Cell[][] map, Cell cell, Cell[] neighbours) {
		Cell top = map[cell.x][cell.y + 1];
		Cell left = map[cell.x - 1][cell.y];
		Cell bottom = map[cell.x][cell.y - 1];
		Cell right = map[cell.x + 1][cell.y];
		if (top != null)
			neighbours[0] = top;
		if (left != null)
			neighbours[1] = left;
		if (bottom != null)
			neighbours[2] = bottom;
		if (right != null)
			neighbours[3] = right;
	}

	private static boolean containsUnvisited(Cell[] cells) {
		for (Cell cell : cells) {
			if (!cell.visited)
				return true;
		}
		return false;
	}

	Cell[][] getMap() {
		return map;
	}
}