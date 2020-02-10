package net.thechubbypanda.larrysadventure.map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.thechubbypanda.larrysadventure.Globals;

public class Tile {

	public static final int SIZE = 128;

	private static final Texture tile, wallVert, wallHoriz, wallCorner;
	private static TextureRegion wallVertLeft, wallVertRight, wallHorizTop, wallHorizBottom,
			wallCornerTopLeft, wallCornerTopRight, wallCornerBottomLeft, wallCornerBottomRight;

	static {
		tile = Globals.assets.get(Globals.Textures.GRASS);

		wallVert = Globals.assets.get(Globals.Textures.WALL_VERT);
		wallHoriz = Globals.assets.get(Globals.Textures.WALL_HORIZ);
		wallCorner = Globals.assets.get(Globals.Textures.WALL_CORNER);

		wallVertLeft = new TextureRegion(wallVert, 12, 0, 12, 128 - 24);
		wallVertRight = new TextureRegion(wallVert, 0, 0, 12, 128 - 24);
		wallHorizTop = new TextureRegion(wallHoriz, 0, 12, 128-24, 12);
		wallHorizBottom = new TextureRegion(wallHoriz, 0, 0, 128-24, 12);

		wallCornerTopLeft = new TextureRegion(wallCorner, 0, 12, 12, 12);
		wallCornerTopRight = new TextureRegion(wallCorner, 12, 12, 12, 12);
		wallCornerBottomLeft = new TextureRegion(wallCorner, 0, 0, 12, 12);
		wallCornerBottomRight = new TextureRegion(wallCorner, 12, 0, 12, 12);
	}

	private Cell cell;
	private float x, y;

	public Tile(Cell cell) {
		this.cell = cell;
		x = cell.x * SIZE - SIZE / 2f;
		y = cell.y * SIZE - SIZE / 2f;
	}

	public void render(SpriteBatch batch) {
		batch.draw(tile, x, y);
		if (cell.wallLeft)
			batch.draw(wallVertLeft, x, y + 12);
		if (cell.wallRight)
			batch.draw(wallVertRight, x+(128-12), y + 12);
		if (cell.wallTop)
			batch.draw(wallHorizTop, x + 12, y + (128-12));
		if (cell.wallBottom)
			batch.draw(wallHorizBottom, x + 12, y);
	}
}
