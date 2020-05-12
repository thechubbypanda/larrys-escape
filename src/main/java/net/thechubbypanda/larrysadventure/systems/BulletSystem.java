package net.thechubbypanda.larrysadventure.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.ashley.systems.IteratingSystem;
import net.thechubbypanda.larrysadventure.CollisionSignal;
import net.thechubbypanda.larrysadventure.components.BulletComponent;

import java.util.ArrayList;

public class BulletSystem extends IteratingSystem implements Listener<CollisionSignal> {

	private final ArrayList<Entity> toRemove = new ArrayList<>();

	public BulletSystem() {
		super(Family.all(BulletComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		for (Entity e : toRemove) {
			getEngine().removeEntity(e);
		}
	}

	@Override
	public void receive(Signal<CollisionSignal> signal, CollisionSignal collision) {
		if (getEntities().contains(collision.entityA, true)) {
			if (collision.colliding) {
				toRemove.add(collision.entityA);
			}
		}
	}
}
