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
		for (DamageComponent dc : hcm.get(entity).beingHitBy.keySet()) {
			if (dc.hitInterval == -1) {
				hcm.get(entity).addHealth(-dc.damage);
			} else {
				if (hcm.get(entity).beingHitBy.get(dc) <= System.currentTimeMillis() - dc.hitInterval) {
					hcm.get(entity).addHealth(-dc.damage);
					hcm.get(entity).beingHitBy.put(dc, System.currentTimeMillis());
				}
			}
		}
	}

	@Override
	public void receive(Signal<CollisionSignal> signal, CollisionSignal collision) {
		if (getEntities().contains(collision.entityA, true)) {
			if (collision.objectB instanceof Entity) {
				Entity entityHit = (Entity) collision.objectB;
				if (dcm.has(entityHit)) {
					if (collision.colliding) {
						hcm.get(collision.entityA).beingHitBy.put(dcm.get(entityHit), 0l);
					} else {
						hcm.get(collision.entityA).beingHitBy.remove(dcm.get(entityHit));
					}
				}
			}
		}
	}
}
