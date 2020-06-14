package net.thechubbypanda.larrysescape;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import net.thechubbypanda.larrysescape.components.*;
import net.thechubbypanda.larrysescape.map.*;

import java.util.ArrayList;
import java.util.Random;

import static net.thechubbypanda.larrysescape.Globals.PPM;

public class LevelManager {

	private final ComponentMapper<PhysicsComponent> phcm = ComponentMapper.getFor(PhysicsComponent.class);
	private final ComponentMapper<HealthComponent> hcm = ComponentMapper.getFor(HealthComponent.class);

	private final Engine engine;
	private final World world;
	private final InputProcessor inputListener;

	private int currentLevel;
	private Entity currentMap;
	private CellMap currentCellMap;
	private final ImmutableArray<Entity> players;

	private boolean changing = false;

	private final Random random = new Random();

	private final Music defaultGame = Gdx.audio.newMusic(Gdx.files.internal("sounds/defaultGame.mp3"));
	private final Music safeGame = Gdx.audio.newMusic(Gdx.files.internal("sounds/safeGame.mp3"));
	private final Sound teleport = Gdx.audio.newSound(Gdx.files.internal("sounds/teleport.wav"));

	public LevelManager(Engine engine, World world, InputProcessor inputListener) {
		this.engine = engine;
		this.world = world;
		this.inputListener = inputListener;

		players = engine.getEntitiesFor(Family.all(PlayerComponent.class).get());

		currentLevel = 0;
		generateLevel(currentLevel);
		Globals.HUD.setLevel(currentLevel);

		defaultGame.setLooping(true);
	}

	private void setLevel(int level) {
		if (!changing) {
			currentLevel = level;
			((Game) Gdx.app.getApplicationListener()).fade(new FadeListener() {
				@Override
				public void fadeStart() {
					Gdx.input.setInputProcessor(null);
					changing = true;
					teleport.play();
				}

				@Override
				public void atMiddleOfFade() {
					Globals.HUD.setLevel(level);
					destroyLevel();
					generateLevel(level);
				}

				@Override
				public void fadeEnd() {
					changing = false;
					Gdx.input.setInputProcessor(inputListener);
				}
			});
		}
	}

	public void reset() {
		setLevel(0);
		players.forEach(p -> hcm.get(p).reset());
		Globals.reset();
	}

	public void bumpLevel() {
		setLevel(currentLevel + 1);
	}

	public void destroyLevel() {
		if (currentMap != null) {
			currentMap.getComponent(TileMapComponent.class).removeBodies(world);
		}
		ImmutableArray<Entity> keep = engine.getEntitiesFor(Family.one(CameraComponent.class, PlayerComponent.class).get());
		for (int i = 0; i < 4; i++) {
			for (Entity e : engine.getEntities()) {
				if (!keep.contains(e, true)) {
					engine.removeEntity(e);
				}
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
		// Player
		for (Entity p : players) {
			phcm.get(p).setPosition(Vector2.Zero);
		}

		switch (level) {
			case 0:
				elderLevel(new String[]{
								"Press SPACE to advance dialog.",
								"Use WASD to move and QE to rotate.",
								"I hear you wish to leave the farm, Larry.",
								"The road ahead is a treacherous one!",
								"You can use the portals to get to the next level",
								"Take these weapons to aid you on your way.",
								"Move your mouse and click to shoot."},
						() -> Globals.CAN_SHOOT = true);
				break;
			case 3:
				elderLevel(new String[]{
								"Ah I see you too have made it this far, well done!",
								"I was unable to continue on to freedom after I lost my leg to those god awful robots.",
								"Take this upgrade I salvaged from my own weapon, perhaps you will stand a better chance with it.",
								"Good luck!",
								"You can now shoot faster."},
						() -> Globals.FAST_FIRING = true);
				break;
			case 6:
				elderLevel(new String[]{
								"Hello! So rare someone passes through here anymore.",
								"I no longer have the strength to continue this war.",
								"Thank you for talking to me, young man...",
								"Perhaps my armour can show my appreciation.",
								"Your max health has doubled."},
						() -> {
							//Globals.HEALTH = 200;
							//players.forEach(p -> hcm.get(p).setMax(Globals.HEALTH));
						});
				break;
			case 9:
				elderLevel(new String[]{
						"Beware!",
						"None who have passed here have ever returned!",
						"Beware!",
						"Thank you for playing!",
						"The game is infinite from here on out."}, null);
				break;
			default:
				// Map
				currentCellMap = new RecursiveCellMap(level + 5);

				currentMap = new Entity();
				currentMap.add(new TileMapComponent(world, currentCellMap));
				currentMap.add(new TransformComponent(0, 0, -1));
				engine.addEntity(currentMap);

				ArrayList<Cell> deadEnds = currentCellMap.getDeadEnds();
				deadEnds.remove(currentCellMap.getMap()[0][0]);

				// Level Exit
				Cell exitCell = deadEnds.get(random.nextInt(deadEnds.size()));
				engine.addEntity(EntityFactory.levelExit(world, new Vector2(exitCell.x, exitCell.y).scl(128)));
				deadEnds.remove(exitCell);

				// Patrol Routes
				ArrayList<ArrayList<Vector2>> routes = new ArrayList<>();
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
					Drop drop;
					float r = random.nextFloat();
					if (r < 0.2f) {
						drop = Drop.health;
					} else if (r < 0.4f) {
						drop = Drop.ammo;
					} else {
						drop = Drop.crystal;
					}
					engine.addEntity(EntityFactory.enemy(world, route, drop));
				}
		}

		if (currentCellMap instanceof RecursiveCellMap) {
			safeGame.stop();
			if (!defaultGame.isPlaying()) {
				defaultGame.play();
			}
		} else {
			defaultGame.stop();
			safeGame.play();
		}
	}

	private void elderLevel(String[] lines, Runnable action) {
		currentCellMap = new CustomCellMap(3, 1);
		currentMap = new Entity();
		currentMap.add(new TileMapComponent(world, currentCellMap));
		currentMap.add(new TransformComponent(0, 0, -1));
		engine.addEntity(currentMap);

		engine.addEntity(EntityFactory.levelExit(world, new Vector2(currentCellMap.getMap()[2][0].x, currentCellMap.getMap()[2][0].y).scl(128)));

		engine.addEntity(EntityFactory.elder(lines, currentLevel, action, new Vector2(currentCellMap.getMap()[1][0].x, currentCellMap.getMap()[1][0].y).scl(128)));
	}

	public int getLevel() {
		return currentLevel;
	}
}
