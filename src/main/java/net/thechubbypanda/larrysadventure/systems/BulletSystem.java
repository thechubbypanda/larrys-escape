package net.thechubbypanda.larrysadventure.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.ashley.systems.IteratingSystem;
import net.thechubbypanda.larrysadventure.Collision;
import net.thechubbypanda.larrysadventure.components.BulletComponent;

import java.util.ArrayList;

public class BulletSystem extends IteratingSystem implements Listener<Collision> {

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
	public void receive(Signal<Collision> signal, Collision object) {
		if (getEntities().contains(object.entity, true)) {
			toRemove.add(object.entity);
		}
	}
}
