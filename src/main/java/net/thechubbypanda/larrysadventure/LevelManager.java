package net.thechubbypanda.larrysadventure;

import box2dLight.RayHandler;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import net.thechubbypanda.larrysadventure.components.CameraComponent;
import net.thechubbypanda.larrysadventure.components.TileMapComponent;
import net.thechubbypanda.larrysadventure.map.Cell;
import net.thechubbypanda.larrysadventure.map.CellMap;
import net.thechubbypanda.larrysadventure.map.Tile;

import java.util.ArrayList;
import java.util.Random;

public class LevelManager {

	private final Engine engine;
	private final World world;
	private final RayHandler rayHandler;

	private int currentLevel;
	private Entity currentMap;

	public LevelManager(Engine engine, World world, RayHandler rayHandler, int initialLevel) {
		this.engine = engine;
		this.world = world;
		this.rayHandler = rayHandler;

		setLevel(initialLevel);
	}

	public void setLevel(int level) {
		currentLevel = level;
		generateLevel(level);
	}

	public void bumpLevel() {
		setLevel(currentLevel + 1);
	}

	private void generateLevel(int level) {
		// Destroy old
		if (currentMap != null) {
			currentMap.getComponent(TileMapComponent.class).removeBodies(world);
		}
		ImmutableArray<Entity> keep = engine.getEntitiesFor(Family.one(CameraComponent.class).get());
		while (engine.getEntities().size() > keep.size()) {
			for (Entity e : engine.getEntities()) {
				if (!keep.contains(e, true)) {
					engine.removeEntity(e);
				}
			}
		}

		// New
		// Map
		CellMap cellMap = new CellMap(level + 5);

		currentMap = new Entity().add(new TileMapComponent(world, cellMap));
		engine.addEntity(currentMap);

		// Player
		Entity player = EntityFactory.player(world, rayHandler);
		engine.addEntity(player);

		for (Entity e : engine.getEntitiesFor(Family.all(CameraComponent.class).get())) {
			e.getComponent(CameraComponent.class).follow(player, 0, 0, 0);
		}

		// Enemies
		ArrayList<Cell> containingEnemies = new ArrayList<>();
		Random random = new Random();
		int numEnemies = cellMap.getSize() * cellMap.getSize() / 8;
		for (int i = 0; i < numEnemies; i++) {
			Cell cell;
			do {
				cell = cellMap.getMap()[random.nextInt(cellMap.getSize())][random.nextInt(cellMap.getSize())];
			} while (containingEnemies.contains(cell) || cellMap.getMap()[0][0] == cell);
			containingEnemies.add(cell);
		}

		final Vector2 pos = new Vector2();
		for (Cell cell : containingEnemies) {
			pos.set(cell.x * Tile.SIZE, cell.y * Tile.SIZE);
			engine.addEntity(EntityFactory.enemy(world, pos));
		}

		ArrayList<Cell> deadEnds = cellMap.getDeadEnds();
		Cell exitCell;
		do {
			exitCell = deadEnds.get(random.nextInt(deadEnds.size()));
		} while (exitCell == cellMap.getMap()[0][0]);
		engine.addEntity(EntityFactory.levelExit(this, world, new Vector2(exitCell.x, exitCell.y).scl(128)));
	}
}
