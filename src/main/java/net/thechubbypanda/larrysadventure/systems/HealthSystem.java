package net.thechubbypanda.larrysadventure.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import net.thechubbypanda.larrysadventure.components.HealthComponent;
import net.thechubbypanda.larrysadventure.components.PlayerComponent;

public class HealthSystem extends IteratingSystem {

	private final ComponentMapper<HealthComponent> hcm = ComponentMapper.getFor(HealthComponent.class);
	private final ComponentMapper<PlayerComponent> pcm = ComponentMapper.getFor(PlayerComponent.class);

	public HealthSystem() {
		super(Family.all(HealthComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		if (hcm.get(entity).getHealth() <= 0) {
			if (!pcm.has(entity)) {
				getEngine().removeEntity(entity);
			}
		}
	}
}
