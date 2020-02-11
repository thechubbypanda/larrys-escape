package net.thechubbypanda.larrysadventure.map;

public class Cell {

	public int x, y;
	public boolean visited = false;
	public boolean top = true, left = true, bottom = true, right = true;

	public Cell(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
