package net.thechubbypanda.larrysadventure.entityListeners;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import net.thechubbypanda.larrysadventure.components.ConeLightComponent;

public class LightEntityListener implements EntityListener {

	private ComponentMapper<ConeLightComponent> lightMapper = ComponentMapper.getFor(ConeLightComponent.class);

	@Override
	public void entityAdded(Entity entity) {

	}

	@Override
	public void entityRemoved(Entity entity) {
		lightMapper.get(entity).remove();
	}
}
