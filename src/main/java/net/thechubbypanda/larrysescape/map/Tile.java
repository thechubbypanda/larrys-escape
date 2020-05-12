package net.thechubbypanda.larrysescape.map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import net.thechubbypanda.larrysescape.Globals;

import static net.thechubbypanda.larrysescape.Globals.PPM;

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

	private static final BodyDef bdef = new BodyDef();
	private static final ChainShape sTopLeftCorner, sBottomLeftCorner, sBottomRightCorner, sTopRightCorner, sLeft, sRight, sTop, sBottom;
	private static final FixtureDef fTopLeftCorner, fBottomLeftCorner, fBottomRightCorner, fTopRightCorner, fLeft, fRight, fTop, fBottom;

	static{
		bdef.type = BodyDef.BodyType.StaticBody;

		Vector2[] cornerPoints = {
				new Vector2(),
				new Vector2(),
				new Vector2()
		};

		Vector2[] edgePoints = {
				new Vector2(),
				new Vector2()
		};

		sTopLeftCorner = new ChainShape();
		cornerPoints[0].set(-SIZE / 2f / PPM, SIZE / 2f / PPM - WALL_SIZE / PPM);
		cornerPoints[1].set(-SIZE / 2f / PPM + WALL_SIZE / PPM, SIZE / 2f / PPM - WALL_SIZE / PPM);
		cornerPoints[2].set(-SIZE / 2f / PPM + WALL_SIZE / PPM, SIZE / 2f / PPM);
		sTopLeftCorner.createChain(cornerPoints);
		fTopLeftCorner = new FixtureDef();
		fTopLeftCorner.shape = sTopLeftCorner;

		sBottomLeftCorner = new ChainShape();
		cornerPoints[0].set(-SIZE / 2f / PPM, -SIZE / 2f / PPM + WALL_SIZE / PPM);
		cornerPoints[1].set(-SIZE / 2f / PPM + WALL_SIZE / PPM, -SIZE / 2f / PPM + WALL_SIZE / PPM);
		cornerPoints[2].set(-SIZE / 2f / PPM + WALL_SIZE / PPM, -SIZE / 2f / PPM);
		sBottomLeftCorner.createChain(cornerPoints);
		fBottomLeftCorner = new FixtureDef();
		fBottomLeftCorner.shape = sBottomLeftCorner;


		sBottomRightCorner = new ChainShape();
		cornerPoints[0].set(SIZE / 2f / PPM, -SIZE / 2f / PPM + WALL_SIZE / PPM);
		cornerPoints[1].set(SIZE / 2f / PPM - WALL_SIZE / PPM, -SIZE / 2f / PPM + WALL_SIZE / PPM);
		cornerPoints[2].set(SIZE / 2f / PPM - WALL_SIZE / PPM, -SIZE / 2f / PPM);
		sBottomRightCorner.createChain(cornerPoints);
		fBottomRightCorner = new FixtureDef();
		fBottomRightCorner.shape = sBottomRightCorner;

		sTopRightCorner = new ChainShape();
		cornerPoints[0].set(SIZE / 2f / PPM, SIZE / 2f / PPM - WALL_SIZE / PPM);
		cornerPoints[1].set(SIZE / 2f / PPM - WALL_SIZE / PPM, SIZE / 2f / PPM - WALL_SIZE / PPM);
		cornerPoints[2].set(SIZE / 2f / PPM - WALL_SIZE / PPM, SIZE / 2f / PPM);
		sTopRightCorner.createChain(cornerPoints);
		fTopRightCorner = new FixtureDef();
		fTopRightCorner.shape = sTopRightCorner;

		sTop = new ChainShape();
		edgePoints[0].set(-SIZE / 2f / PPM + WALL_SIZE / PPM, SIZE / 2f / PPM - WALL_SIZE / PPM);
		edgePoints[1].set(SIZE / 2f / PPM - WALL_SIZE / PPM, SIZE / 2f / PPM - WALL_SIZE / PPM);
		sTop.createChain(edgePoints);
		fTop = new FixtureDef();
		fTop.shape = sTop;

		sLeft = new ChainShape();
		edgePoints[0].set(-SIZE / 2f / PPM + WALL_SIZE / PPM, SIZE / 2f / PPM - WALL_SIZE / PPM);
		edgePoints[1].set(-SIZE / 2f / PPM + WALL_SIZE / PPM, -SIZE / 2f / PPM + WALL_SIZE / PPM);
		sLeft.createChain(edgePoints);
		fLeft = new FixtureDef();
		fLeft.shape = sLeft;

		sBottom = new ChainShape();
		edgePoints[0].set(-SIZE / 2f / PPM + WALL_SIZE / PPM, -SIZE / 2f / PPM + WALL_SIZE / PPM);
		edgePoints[1].set(SIZE / 2f / PPM - WALL_SIZE / PPM, -SIZE / 2f / PPM + WALL_SIZE / PPM);
		sBottom.createChain(edgePoints);
		fBottom = new FixtureDef();
		fBottom.shape = sBottom;

		sRight = new ChainShape();
		edgePoints[0].set(SIZE / 2f / PPM - WALL_SIZE / PPM, SIZE / 2f / PPM - WALL_SIZE / PPM);
		edgePoints[1].set(SIZE / 2f / PPM - WALL_SIZE / PPM, -SIZE / 2f / PPM + WALL_SIZE / PPM);
		sRight.createChain(edgePoints);
		fRight = new FixtureDef();
		fRight.shape = sRight;
	}

	public Cell cell;
	private final float x;
	private final float y;
	private final Body body;

	public Tile(World world, Cell cell) {
		this.cell = cell;
		x = cell.x * SIZE - SIZE / 2f;
		y = cell.y * SIZE - SIZE / 2f;

		bdef.position.set(cell.x * SIZE / PPM, cell.y * SIZE / PPM);
		body = world.createBody(bdef);
		body.createFixture(fTopLeftCorner);
		body.createFixture(fBottomLeftCorner);
		body.createFixture(fBottomRightCorner);
		body.createFixture(fTopRightCorner);
		if (cell.up == null)
			body.createFixture(fTop);
		if (cell.left == null)
			body.createFixture(fLeft);
		if (cell.down == null)
			body.createFixture(fBottom);
		if (cell.right == null)
			body.createFixture(fRight);
	}

	public void render(Batch batch) {
		batch.draw(tile, x, y);
		if (cell.left == null)
			batch.draw(wallVertLeft, x, y + WALL_SIZE);
		if (cell.right == null)
			batch.draw(wallVertRight, x + (SIZE - WALL_SIZE), y + WALL_SIZE);
		if (cell.up == null)
			batch.draw(wallHorizTop, x + WALL_SIZE, y + (SIZE - WALL_SIZE));
		if (cell.down == null)
			batch.draw(wallHorizBottom, x + WALL_SIZE, y);

		if (cell.up == null) {
			if (cell.left == null) {
				batch.draw(wallCornerBottomLeft, x, y + (SIZE - WALL_SIZE));
			} else {
				batch.draw(wallCornerBottomRight, x, y + (SIZE - WALL_SIZE));
			}
			if (cell.right == null) {
				batch.draw(wallCornerBottomLeft, x + (SIZE - WALL_SIZE), y + (SIZE - WALL_SIZE));
			} else {
				batch.draw(wallCornerBottomRight, x + (SIZE - WALL_SIZE), y + (SIZE - WALL_SIZE));
			}
		}
		if (cell.left == null) {
			if (cell.up != null) {
				batch.draw(wallCornerTopRight, x, y + (SIZE - WALL_SIZE));
			}
			if (cell.down != null) {
				batch.draw(wallCornerTopRight, x, y);
			}
		} else {
			if (cell.up != null) {
				batch.draw(wallCornerTopLeft, x + WALL_SIZE, y + (SIZE - WALL_SIZE) + WALL_SIZE, -WALL_SIZE, -WALL_SIZE);
			}
			if (cell.down != null) {
				batch.draw(wallCornerTopLeft, x + WALL_SIZE, y, -WALL_SIZE, WALL_SIZE);
			}
		}
		if (cell.down == null) {
			if (cell.left == null) {
				batch.draw(wallCornerBottomLeft, x, y);
			} else {
				batch.draw(wallCornerBottomRight, x, y + WALL_SIZE, WALL_SIZE, -WALL_SIZE);
			}
			if (cell.right == null) {
				batch.draw(wallCornerBottomLeft, x + (SIZE - WALL_SIZE), y);
			} else {
				batch.draw(wallCornerBottomRight, x + (SIZE - WALL_SIZE), y + WALL_SIZE, WALL_SIZE, -WALL_SIZE);
			}
		}
		if (cell.right == null) {
			if (cell.up != null) {
				batch.draw(wallCornerTopRight, x + (SIZE - WALL_SIZE) + WALL_SIZE, y + (SIZE - WALL_SIZE), -WALL_SIZE, WALL_SIZE);
			}
			if (cell.down != null) {
				batch.draw(wallCornerTopRight, x + (SIZE - WALL_SIZE) + WALL_SIZE, y, -WALL_SIZE, WALL_SIZE);
			}
		} else {
			if (cell.up != null) {
				batch.draw(wallCornerTopLeft, x + (SIZE - WALL_SIZE), y + (SIZE - WALL_SIZE) + WALL_SIZE, WALL_SIZE, -WALL_SIZE);
			}
			if (cell.down != null) {
				batch.draw(wallCornerTopLeft, x + (SIZE - WALL_SIZE), y);
			}
		}
	}

	public void removeBody(World world) {
		world.destroyBody(body);
	}
}
