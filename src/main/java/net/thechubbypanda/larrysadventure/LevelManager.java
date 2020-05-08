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
import net.thechubbypanda.larrysadventure.components.TransformComponent;
import net.thechubbypanda.larrysadventure.map.Cell;
import net.thechubbypanda.larrysadventure.map.CellMap;
import net.thechubbypanda.larrysadventure.map.Tile;

import java.util.ArrayList;
import java.util.Random;

import static net.thechubbypanda.larrysadventure.Globals.PPM;

public class LevelManager {

	private final Engine engine;
	private final World world;
	private final RayHandler rayHandler;

	private int currentLevel;
	private Entity currentMap;
	public CellMap currentCellMap;
	public ArrayList<ArrayList<Vector2>> routes;

	private Random random = new Random();

	public LevelManager(Engine engine, World world, RayHandler rayHandler, int initialLevel) {
		this.engine = engine;
		this.world = world;
		this.rayHandler = rayHandler;

		setLevel(initialLevel);
	}

	public void setLevel(int level) {
		currentLevel = level;
		destroyLevel();
		generateLevel(level);
	}

	public void bumpLevel() {
		setLevel(currentLevel + 1);
	}

	private void destroyLevel() {
		if (currentMap != null) {
			currentMap.getComponent(TileMapComponent.class).removeBodies(world);
		}
		ImmutableArray<Entity> keep = engine.getEntitiesFor(Family.one(CameraComponent.class).get());
		for (Entity e : engine.getEntities()) {
			if (!keep.contains(e, true)) {
				engine.removeEntity(e);
			}
		}
	}

	private static ArrayList<Cell> getStraightPath(Cell start) {
		ArrayList<Cell> path = new ArrayList<>();
		path.add(start);
		return calcPath(start, path);
	}

	private static ArrayList<Cell> calcPath(Cell cell, ArrayList<Cell> path) {
		ArrayList<Cell> neighbours = cell.getLinkedNeighbours();
		if (neighbours.size() == 2) {
			neighbours.removeIf(path::contains);
			path.add(cell);
			return calcPath(neighbours.get(0), path);
		} else if (neighbours.size() == 1) {
			return calcPath(neighbours.get(0), path);
		} else {
			return path;
		}
	}

	private void generateLevel(int level) {
		// Map
		currentCellMap = new CellMap(level + 5);

		currentMap = new Entity();
		currentMap.add(new TileMapComponent(world, currentCellMap));
		currentMap.add(new TransformComponent(0, 0, -1));
		engine.addEntity(currentMap);

		// Player
		Entity player = EntityFactory.player(world, rayHandler);
		engine.addEntity(player);

		ArrayList<Cell> deadEnds = currentCellMap.getDeadEnds();
		deadEnds.remove(currentCellMap.getMap()[0][0]);

		// Level Exit
		Cell exitCell = deadEnds.get(random.nextInt(deadEnds.size()));
		engine.addEntity(EntityFactory.levelExit(world, new Vector2(exitCell.x, exitCell.y).scl(128)));
		deadEnds.remove(exitCell);

		// Patrol Routes
		routes = new ArrayList<>();
		for (Cell end : deadEnds) {
			ArrayList<Cell> path = getStraightPath(end);
			ArrayList<Vector2> route = new ArrayList<>();
			for (Cell c : path) {
				route.add(new Vector2(c.x, c.y).scl(Tile.SIZE).scl(1 / PPM));
			}
			routes.add(route);
		}

		// Enemies
		for (ArrayList<Vector2> route : routes) {
			engine.addEntity(EntityFactory.enemy(world, route));
		}
	}
}
