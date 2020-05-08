package net.thechubbypanda.larrysadventure.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import net.thechubbypanda.larrysadventure.components.*;
import net.thechubbypanda.larrysadventure.map.Cell;
import net.thechubbypanda.larrysadventure.map.CellMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import static net.thechubbypanda.larrysadventure.Globals.PPM;
import static net.thechubbypanda.larrysadventure.components.EnemyComponent.State.*;

public class EnemySystem extends IteratingSystem {

	private final World world;
	private final ComponentMapper<PhysicsComponent> phcm = ComponentMapper.getFor(PhysicsComponent.class);
	private final ComponentMapper<TransformComponent> tcm = ComponentMapper.getFor(TransformComponent.class);
	private final ComponentMapper<EnemyComponent> ecm = ComponentMapper.getFor(EnemyComponent.class);
	private final ComponentMapper<TileMapComponent> tmcm = ComponentMapper.getFor(TileMapComponent.class);

	private ImmutableArray<Entity> players;
	private ImmutableArray<Entity> maps;

	public EnemySystem(World world) {
		super(Family.all(EnemyComponent.class, PhysicsComponent.class).get());

		this.world = world;
	}

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		players = engine.getEntitiesFor(Family.all(PlayerComponent.class).get());
		maps = engine.getEntitiesFor(Family.all(TileMapComponent.class).get());
	}

	// Finds the best path from one cell to another (A* algorithm)
	public static ArrayList<Cell> findPath(Cell start, Cell target) {
		// Contains cells that have been fully checked
		ArrayList<Cell> closedSet = new ArrayList<>();

		// Contains cells that are being checked
		ArrayList<Cell> openSet = new ArrayList<>();

		// Add the staring cell to the open set
		openSet.add(start);

		// While there are cells in the open set
		while (openSet.size() > 0) {
			Cell currentCell = openSet.get(0);

			// Find the cell with the lowest cost to the target
			for (int i = 1; i < openSet.size(); i++) {

				// If the fcost (total cost from start to goal) of the next cell is < the
				// current cell's
				if (openSet.get(i).fcost() < currentCell.fcost()) {
					// Change to the next cell
					currentCell = openSet.get(i);
				}

				// If the fcost of the next cell is equal to the current cell's fcost and
				// the next cell's hcost (heuristic from cell to target) is < the current cell's
				// hcost
				else if (openSet.get(i).fcost() == currentCell.fcost() && openSet.get(i).hcost < currentCell.hcost) {
					// Chageg to the next cell
					currentCell = openSet.get(i);
				}
			}

			// We will be done with the current cell after this so move it into the closed
			// set
			openSet.remove(currentCell);
			closedSet.add(currentCell);

			// If we have reached the target, calculate and return the path
			if (currentCell == target) {
				ArrayList<Cell> path = new ArrayList<>();
				currentCell = target;

				while (currentCell != start) {
					path.add(currentCell);
					currentCell = currentCell.parent;
				}

				Collections.reverse(path);
				return path;
			}

			// Get the neighbours of the current cell and choose the cell with the lowest
			// cost of moving to (gscore)
			for (Cell neighbour : currentCell.getLinkedNeighbours()) {
				// Skip it if we can't walk on it or we've disregarded it
				if (closedSet.contains(neighbour)) {
					continue;
				}

				// The new total path cost if we go to the next cell
				int movementCost = currentCell.gcost + 1;

				// Discover this fast route or if we know of it and the movement cost is more
				// than the neighbour's gcost then leave it
				if (!openSet.contains(neighbour)) {
					openSet.add(neighbour);
				} else if (movementCost >= neighbour.gcost) {
					continue;
				}

				// Set the neighbours costs and parent for later use since this is the best
				// route
				neighbour.gcost = movementCost;
				neighbour.hcost = (int) new Vector2(neighbour.x, neighbour.y).dst(target.x, target.y);
				neighbour.parent = currentCell;
			}
		}

		// Something went wrong and a path could not be found
		return null;
	}

	private ArrayList<Entity> raycast(Vector2 start, Vector2 end) {
		ArrayList<Entity> entities = new ArrayList<>();
		world.rayCast((fixture, point, normal, fraction) -> {
			entities.add((Entity) fixture.getBody().getUserData());
			return 1;
		}, start, end);
		return entities;
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		EnemyComponent ec = ecm.get(entity);

		// TODO: Potentially slow
		for (Entity player : players) {

			ArrayList<Entity> entities = raycast(phcm.get(entity).getPosition(), phcm.get(player).getPosition());

			switch (ec.state) {
				case chasing:
					if (entities.contains(player) && !entities.contains(null)) {
						phcm.get(entity).setLinearVelocity(phcm.get(player).getPosition().sub(phcm.get(entity).getPosition()).nor().scl(1));
						phcm.get(entity).setRotation(phcm.get(entity).getVelocity().angleRad());
					} else {
						ec.state = calculateReturn;
					}
					break;
				case calculateReturn:
					if (entities.contains(player) && !entities.contains(null)) {
						ec.state = chasing;
						break;
					}
					phcm.get(entity).setLinearVelocity(Vector2.Zero);
					if (raycast(phcm.get(entity).getPosition(), ec.patrolPoints.get(ec.nextPatrolPoint)).size() == 0) {
						ec.state = patrolling;
					} else {
						ec.returnPoints.clear();
						for (Entity e : maps) {
							CellMap cellMap = tmcm.get(e).getCellMap();
							ArrayList<Cell> path = findPath(cellMap.getClosestCell(tcm.get(entity).getPosition()), cellMap.getClosestCell(new Vector2(ec.patrolPoints.get(ec.nextPatrolPoint)).scl(PPM)));
							for (Cell cell : Objects.requireNonNull(path)) {
								ec.returnPoints.add(new Vector2(cell.x, cell.y).scl(128 / PPM));
							}
						}
						ec.state = returning;
						ec.nextReturnPoint = 0;
					}
					ec.percent = 0;
					ec.startPosition.set(phcm.get(entity).getPosition());
					break;
				case returning:
					if (entities.contains(player) && !entities.contains(null)) {
						ec.state = chasing;
						break;
					}
					phcm.get(entity).setLinearVelocity(Vector2.Zero);
					if (ec.percent >= 1) {
						if (ec.returnPoints.size() > 1) {
							ec.nextReturnPoint++;
							if (ec.nextReturnPoint == ec.returnPoints.size()) {
								ec.state = patrolling;
								break;
							}
						}
						ec.percent = 0;
						ec.startPosition.set(phcm.get(entity).getPosition());
						phcm.get(entity).setRotation(new Vector2(ec.returnPoints.get(ec.nextReturnPoint)).sub(phcm.get(entity).getPosition()).angleRad());
					}

					phcm.get(entity).setPosition(new Vector2(ec.startPosition).lerp(ec.returnPoints.get(ec.nextReturnPoint), (ec.percent += (deltaTime / 2f))));
					break;
				case patrolling:
					if (entities.contains(player) && !entities.contains(null)) {
						ec.state = chasing;
						break;
					}
					phcm.get(entity).setLinearVelocity(Vector2.Zero);
					if (ec.percent >= 1) {
						if (ec.patrolPoints.size() > 1) {
							if (ec.reverse) {
								ec.nextPatrolPoint--;
							} else {
								ec.nextPatrolPoint++;
							}
							if (ec.nextPatrolPoint == ec.patrolPoints.size()) {
								ec.reverse = true;
								ec.nextPatrolPoint = ec.patrolPoints.size() - 2;
							} else if (ec.nextPatrolPoint < 0) {
								ec.reverse = false;
								ec.nextPatrolPoint = 1;
							}
						}
						ec.percent = 0;
						ec.startPosition.set(phcm.get(entity).getPosition());
						phcm.get(entity).setRotation(new Vector2(ec.patrolPoints.get(ec.nextPatrolPoint)).sub(phcm.get(entity).getPosition()).angleRad());
					}

					phcm.get(entity).setPosition(new Vector2(ec.startPosition).lerp(ec.patrolPoints.get(ec.nextPatrolPoint), (ec.percent += (deltaTime / 2f))));
					break;
			}
		}
	}
}
