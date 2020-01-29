package net.thechubbypanda.larrysadventure.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import net.thechubbypanda.larrysadventure.components.AliveTimeComponent;

public class AliveTimeSystem extends IteratingSystem {

	private Engine engine;
	private ComponentMapper<AliveTimeComponent> aliveTimeMapper = ComponentMapper.getFor(AliveTimeComponent.class);

	public AliveTimeSystem() {
		super(Family.all(AliveTimeComponent.class).get());
	}

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		this.engine = engine;
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		if (aliveTimeMapper.get(entity).currentLifeLeft > 0f) {
			aliveTimeMapper.get(entity).currentLifeLeft -= deltaTime;
		} else {
			engine.removeEntity(entity);
		}
	}
}
