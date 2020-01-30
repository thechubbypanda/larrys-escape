package net.thechubbypanda.larrysadventure.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import net.thechubbypanda.larrysadventure.components.ScriptComponent;

public class ScriptSystem extends IteratingSystem {

	private ComponentMapper<ScriptComponent> scriptMapper = ComponentMapper.getFor(ScriptComponent.class);

	public ScriptSystem() {
		super(Family.all(ScriptComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		scriptMapper.get(entity).update(entity);
	}
}
