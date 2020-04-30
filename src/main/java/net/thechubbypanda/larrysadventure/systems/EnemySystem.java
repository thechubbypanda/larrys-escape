package net.thechubbypanda.larrysadventure.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import net.thechubbypanda.larrysadventure.components.*;

import java.util.ArrayList;

public class EnemySystem extends IteratingSystem {

	private final World world;
	private final ComponentMapper<PhysicsComponent> pcm = ComponentMapper.getFor(PhysicsComponent.class);
	private final ComponentMapper<EnemyComponent> ecm = ComponentMapper.getFor(EnemyComponent.class);
	private final ComponentMapper<BulletComponent> bcm = ComponentMapper.getFor(BulletComponent.class);
	private final ComponentMapper<TileMapComponent> tmcm = ComponentMapper.getFor(TileMapComponent.class);

	private ImmutableArray<Entity> players;
	private ImmutableArray<Entity> maps;

	public EnemySystem(World world) {
		super(Family.all(EnemyComponent.class).get(), 10000);

		this.world = world;
	}

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		players = engine.getEntitiesFor(Family.all(PlayerComponent.class).get());
		maps = engine.getEntitiesFor(Family.all(TileMapComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		EnemyComponent ec = ecm.get(entity);
		PhysicsComponent epc = pcm.get(entity);
		// TODO: Potentially slow
		for (Entity player : players) {
			PhysicsComponent ppc = pcm.get(player);

			ArrayList<Entity> entities = raycast(epc.getBodyPosition(), ppc.getBodyPosition());

			if (entities.contains(player) && !entities.contains(null)) {
				ec.tryToReturn = true;
				epc.setLinearVelocity(ppc.getBodyPosition().sub(epc.getBodyPosition()).nor().scl(0.5f));
			} else if (ec.tryToReturn) {
				epc.setLinearVelocity(Vector2.Zero);
				if (raycast(epc.getBodyPosition(), ec.patrolPoints.get(ec.nextPoint)).size() == 0) {
					ec.tryToReturn = false;
					ec.percent = 0;
					ec.startPosition.set(epc.getBodyPosition());
				}
			} else {
				epc.setLinearVelocity(Vector2.Zero);
				if (ec.patrolPoints.size() > 1) {
					if (ec.percent >= 1) {
						if (ec.reverse) {
							ec.nextPoint--;
						} else {
							ec.nextPoint++;
						}
						if (ec.nextPoint == ec.patrolPoints.size()) {
							ec.reverse = true;
							ec.nextPoint = ec.patrolPoints.size() - 2;
						} else if (ec.nextPoint < 0) {
							ec.reverse = false;
							ec.nextPoint = 1;
						}
						ec.percent = 0;
						ec.startPosition.set(epc.getBodyPosition());
					}

					epc.setPosition(new Vector2(ec.startPosition).interpolate(ec.patrolPoints.get(ec.nextPoint), (ec.percent += (deltaTime / 2f)), Interpolation.linear));
				}
			}
		}
	}

	private ArrayList<Entity> raycast(Vector2 start, Vector2 end) {
		ArrayList<Entity> entities = new ArrayList<>();
		world.rayCast((fixture, point, normal, fraction) -> {
			entities.add((Entity) fixture.getBody().getUserData());
			return 1;
		}, start, end);
		return entities;
	}
}
