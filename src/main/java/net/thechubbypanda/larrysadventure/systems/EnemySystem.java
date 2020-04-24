package net.thechubbypanda.larrysadventure.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import net.thechubbypanda.larrysadventure.components.EnemyComponent;
import net.thechubbypanda.larrysadventure.components.PhysicsComponent;
import net.thechubbypanda.larrysadventure.components.PlayerComponent;

public class EnemySystem extends IteratingSystem {

	private final World world;
	private final ComponentMapper<PhysicsComponent> epc = ComponentMapper.getFor(PhysicsComponent.class);
	private ImmutableArray<Entity> players;
	private Fixture last = null;

	public EnemySystem(World world) {
		super(Family.all(EnemyComponent.class).get());

		this.world = world;
	}

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		players = engine.getEntitiesFor(Family.all(PlayerComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		// TODO: Potentially slow
		for (Entity player : players) {
			PhysicsComponent ppc = epc.get(player);
			if (epc.has(entity)) {
				PhysicsComponent pc = epc.get(entity);
				last = null;
				world.rayCast((fixture, point, normal, fraction) -> {
					last = fixture;
					return fraction;
				}, pc.getBodyPosition(), ppc.getBodyPosition());
				if (last != null && last.getBody().getUserData() == player) {
					pc.setLinearVelocity(ppc.getBodyPosition().sub(pc.getBodyPosition()).nor().scl(0.5f));
				} else {
					pc.setLinearVelocity(Vector2.Zero);
				}
			}
		}
	}
}
