package net.thechubbypanda.larrysadventure.map;

import java.util.ArrayList;

public class Cell {

	public final int x, y;
	public boolean visited = false;

	/**
	 * Null if wall, Cell for cell
	 */
	public Cell up = null, left = null, down = null, right = null;

	public Cell(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public ArrayList<Cell> getLinkedNeighbours() {
		ArrayList<Cell> neighbours = new ArrayList<>();
		if (up != null) neighbours.add(up);
		if (left != null) neighbours.add(left);
		if (down != null) neighbours.add(down);
		if (right != null) neighbours.add(right);

		return neighbours;
	}
}
