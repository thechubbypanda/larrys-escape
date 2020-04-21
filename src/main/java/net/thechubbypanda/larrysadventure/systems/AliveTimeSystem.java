package net.thechubbypanda.larrysadventure.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import net.thechubbypanda.larrysadventure.components.AliveTimeComponent;

public class AliveTimeSystem extends IteratingSystem {

	private final ComponentMapper<AliveTimeComponent> aliveTimeMapper = ComponentMapper.getFor(AliveTimeComponent.class);

	public AliveTimeSystem() {
		super(Family.all(AliveTimeComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		if (aliveTimeMapper.get(entity).currentLifeLeft > 0f) {
			aliveTimeMapper.get(entity).currentLifeLeft -= deltaTime;
		} else {
			getEngine().removeEntity(entity);
		}
	}
}
