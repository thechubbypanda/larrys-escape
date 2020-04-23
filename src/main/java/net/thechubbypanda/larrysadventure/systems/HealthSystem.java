package net.thechubbypanda.larrysadventure.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import net.thechubbypanda.larrysadventure.components.DamageComponent;
import net.thechubbypanda.larrysadventure.components.HealthComponent;
import net.thechubbypanda.larrysadventure.components.PlayerComponent;

public class HealthSystem extends IteratingSystem {

	private final ComponentMapper<HealthComponent> hcm = ComponentMapper.getFor(HealthComponent.class);
	private final ComponentMapper<PlayerComponent> pcm = ComponentMapper.getFor(PlayerComponent.class);
	private final ComponentMapper<DamageComponent> dcm = ComponentMapper.getFor(DamageComponent.class);

	public HealthSystem() {
		super(Family.all(HealthComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		if (hcm.get(entity).getHealth() <= 0) {
			getEngine().removeEntity(entity);
		}
	}

	public void hit(Entity e, Object o) {
		if (o instanceof Entity) {
			Entity hit = (Entity) o;
			if (dcm.has(hit)) {
				hcm.get(e).addHealth(-dcm.get(hit).getDamage());
			}
		}
	}
}
