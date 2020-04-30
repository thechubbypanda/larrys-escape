package net.thechubbypanda.larrysadventure.entityListeners;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.gdx.physics.box2d.World;
import net.thechubbypanda.larrysadventure.components.LightComponent;
import net.thechubbypanda.larrysadventure.components.PhysicsComponent;

public class WorldListener implements EntityListener {

	private final ComponentMapper<PhysicsComponent> pcm = ComponentMapper.getFor(PhysicsComponent.class);
	private final ComponentMapper<LightComponent> lcm = ComponentMapper.getFor(LightComponent.class);

	private final World world;

	public WorldListener(World world) {
		this.world = world;
	}

	@Override
	public void entityAdded(Entity entity) {

	}

	@Override
	public void entityRemoved(Entity entity) {
		if (pcm.has(entity)) {
			pcm.get(entity).dispose(world);
		}
		if (lcm.has(entity)) {
			lcm.get(entity).dispose();
		}
	}
}
