package net.thechubbypanda.larrysadventure.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.ashley.systems.IteratingSystem;
import net.thechubbypanda.larrysadventure.CollisionSignal;
import net.thechubbypanda.larrysadventure.components.DamageComponent;
import net.thechubbypanda.larrysadventure.components.HealthComponent;

public class HealthSystem extends IteratingSystem implements Listener<CollisionSignal> {

	private final ComponentMapper<HealthComponent> hcm = ComponentMapper.getFor(HealthComponent.class);
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

	@Override
	public void receive(Signal<CollisionSignal> signal, CollisionSignal object) {
		if (getEntities().contains(object.entity, true)) {
			if (object.object instanceof Entity) {
				Entity entityHit = (Entity) object.object;
				if (dcm.has(entityHit)) {
					hcm.get(object.entity).addHealth(-dcm.get(entityHit).getDamage());
				}
			}
		}
	}
}
