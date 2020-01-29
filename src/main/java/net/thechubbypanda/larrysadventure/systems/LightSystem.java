package net.thechubbypanda.larrysadventure.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import net.thechubbypanda.larrysadventure.components.AliveTimeComponent;
import net.thechubbypanda.larrysadventure.components.LightComponent;

public class LightSystem extends IteratingSystem {

	private ComponentMapper<LightComponent> lightMapper = ComponentMapper.getFor(LightComponent.class);
	private ComponentMapper<AliveTimeComponent> aliveTimeMapper = ComponentMapper.getFor(AliveTimeComponent.class);

	public LightSystem() {
		super(Family.all(LightComponent.class, AliveTimeComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		if (aliveTimeMapper.get(entity).currentLifeLeft <= 0f) return;

		lightMapper.get(entity).light.setDistance(lightMapper.get(entity).initialDistance * aliveTimeMapper.get(entity).currentLifeLeft / aliveTimeMapper.get(entity).initialLifeTime);
	}
}
