package net.thechubbypanda.larrysadventure.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import net.thechubbypanda.larrysadventure.CollisionSignal;
import net.thechubbypanda.larrysadventure.components.AmmoDropComponent;
import net.thechubbypanda.larrysadventure.components.HealthDropComponent;
import net.thechubbypanda.larrysadventure.components.PlayerComponent;

import java.util.ArrayList;

public class DropSystem extends IteratingSystem implements Listener<CollisionSignal> {

	private final ArrayList<Entity> toRemove = new ArrayList<>();
	private ImmutableArray<Entity> players;

	public DropSystem() {
		super(Family.one(HealthDropComponent.class, AmmoDropComponent.class).get());
	}

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		players = engine.getEntitiesFor(Family.all(PlayerComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		for (Entity e : toRemove) {
			getEngine().removeEntity(e);
		}
	}

	@Override
	public void receive(Signal<CollisionSignal> signal, CollisionSignal object) {
		if (getEntities().contains(object.entityA, true) && players.contains((Entity) object.objectB, true)) {
			toRemove.add(object.entityA);
		}
	}
}
