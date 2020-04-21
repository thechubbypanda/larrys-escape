package net.thechubbypanda.larrysadventure.entityListeners;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.gdx.physics.box2d.World;
import net.thechubbypanda.larrysadventure.components.PhysicsComponent;

public class PhysicsEntityListener implements EntityListener {

	private final ComponentMapper<PhysicsComponent> physicsMapper = ComponentMapper.getFor(PhysicsComponent.class);

	private final World world;

	public PhysicsEntityListener(World world) {
		this.world = world;
	}

	@Override
	public void entityAdded(Entity entity) {

	}

	@Override
	public void entityRemoved(Entity entity) {
		physicsMapper.get(entity).removeBody(world);
	}
}
