package net.thechubbypanda.larrysadventure.map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.thechubbypanda.larrysadventure.Globals;

public class Tile {

	public static final int SIZE = 128;
	public static final int WALL_SIZE = 12;
	public static final int WALL_SIZE2 = WALL_SIZE*2;

	private static final Texture tile, wallVert, wallHoriz;
	private static final TextureRegion wallVertLeft, wallVertRight, wallHorizTop, wallHorizBottom;
	private static final Texture wallCorner;
	private static final TextureRegion wallCornerTopLeft, wallCornerTopRight, wallCornerBottomLeft, wallCornerBottomRight;

	static {
		tile = Globals.assets.get(Globals.Textures.GRASS);

		wallVert = Globals.assets.get(Globals.Textures.WALL_VERT);
		wallHoriz = Globals.assets.get(Globals.Textures.WALL_HORIZ);
		wallCorner = Globals.assets.get(Globals.Textures.WALL_CORNER);

		wallVertLeft = new TextureRegion(wallVert, WALL_SIZE, 0, WALL_SIZE, SIZE - WALL_SIZE2);
		wallVertRight = new TextureRegion(wallVert, 0, 0, WALL_SIZE, SIZE - WALL_SIZE2);
		wallHorizTop = new TextureRegion(wallHoriz, 0, WALL_SIZE, SIZE-WALL_SIZE2, WALL_SIZE);
		wallHorizBottom = new TextureRegion(wallHoriz, 0, 0, SIZE-WALL_SIZE2, WALL_SIZE);

		wallCornerTopLeft = new TextureRegion(wallCorner, 0, 0, WALL_SIZE, WALL_SIZE);
		wallCornerTopRight = new TextureRegion(wallCorner, WALL_SIZE, 0, WALL_SIZE, WALL_SIZE);
		wallCornerBottomLeft = new TextureRegion(wallCorner, 0, WALL_SIZE, WALL_SIZE, WALL_SIZE);
		wallCornerBottomRight = new TextureRegion(wallCorner, WALL_SIZE, WALL_SIZE, WALL_SIZE, WALL_SIZE);
	}

	public Cell cell;
	private float x, y;

	public Tile(Cell cell) {
		this.cell = cell;
		x = cell.x * SIZE - SIZE / 2f;
		y = cell.y * SIZE - SIZE / 2f;
	}

	public void render(SpriteBatch batch) {
		batch.draw(tile, x, y);
		if (cell.left)
			batch.draw(wallVertLeft, x, y + WALL_SIZE);
		if (cell.right)
			batch.draw(wallVertRight, x+(SIZE-WALL_SIZE), y + WALL_SIZE);
		if (cell.top)
			batch.draw(wallHorizTop, x + WALL_SIZE, y + (SIZE-WALL_SIZE));
		if (cell.bottom)
			batch.draw(wallHorizBottom, x + WALL_SIZE, y);

		if (cell.top) {
			if (cell.left) {
				batch.draw(wallCornerBottomLeft, x, y + (SIZE-WALL_SIZE));
			} else {
				batch.draw(wallCornerBottomRight, x, y + (SIZE-WALL_SIZE));
			}
			if (cell.right) {
				batch.draw(wallCornerBottomLeft, x + (SIZE-WALL_SIZE), y+(SIZE-WALL_SIZE));
			} else {
				batch.draw(wallCornerBottomRight, x + (SIZE-WALL_SIZE), y + (SIZE-WALL_SIZE));
			}
		}
		if (cell.left) {
			if (!cell.top) {
				batch.draw(wallCornerTopRight, x, y + (SIZE-WALL_SIZE));
			}
			if (!cell.bottom) {
				batch.draw(wallCornerTopRight, x, y);
			}
		} else {
			if (!cell.top) {
				batch.draw(wallCornerTopLeft, x + WALL_SIZE, y + (SIZE-WALL_SIZE) + WALL_SIZE, -WALL_SIZE, -WALL_SIZE);
			}
			if (!cell.bottom) {
				batch.draw(wallCornerTopLeft, x + WALL_SIZE, y, -WALL_SIZE, WALL_SIZE);
			}
		}
		if (cell.bottom) {
			if (cell.left) {
				batch.draw(wallCornerBottomLeft, x, y);
			} else {
				batch.draw(wallCornerBottomRight, x, y+WALL_SIZE, WALL_SIZE, -WALL_SIZE);
			}
			if (cell.right) {
				batch.draw(wallCornerBottomLeft, x+(SIZE-WALL_SIZE), y);
			} else {
				batch.draw(wallCornerBottomRight, x+(SIZE-WALL_SIZE), y+WALL_SIZE, WALL_SIZE, -WALL_SIZE);
			}
		}
		if (cell.right) {
			if (!cell.top) {
				batch.draw(wallCornerTopRight, x + (SIZE-WALL_SIZE) + WALL_SIZE, y + (SIZE-WALL_SIZE), -WALL_SIZE, WALL_SIZE);
			}
			if (!cell.bottom) {
				batch.draw(wallCornerTopRight, x + (SIZE-WALL_SIZE)+WALL_SIZE, y, -WALL_SIZE, WALL_SIZE);
			}
		} else {
			if (!cell.top) {
				batch.draw(wallCornerTopLeft, x+ (SIZE-WALL_SIZE), y+ (SIZE-WALL_SIZE) + WALL_SIZE, WALL_SIZE, -WALL_SIZE);
			}
			if (!cell.bottom) {
				batch.draw(wallCornerTopLeft, x+ (SIZE-WALL_SIZE), y);
			}
		}
	}
}
