package net.thechubbypanda.larrysadventure.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import net.thechubbypanda.larrysadventure.Globals;
import net.thechubbypanda.larrysadventure.components.CameraComponent;
import net.thechubbypanda.larrysadventure.components.PhysicsComponent;
import net.thechubbypanda.larrysadventure.components.PlayerComponent;
import net.thechubbypanda.larrysadventure.signals.ResizeSignal;

public class CameraSystem extends IteratingSystem implements Listener<ResizeSignal> {

	private final ComponentMapper<CameraComponent> ccm = ComponentMapper.getFor(CameraComponent.class);
	private final ComponentMapper<PhysicsComponent> pcm = ComponentMapper.getFor(PhysicsComponent.class);

	private ImmutableArray<Entity> players;

	public CameraSystem() {
		super(Family.all(CameraComponent.class).get(), Globals.SystemPriority.VIEWPORT);
	}

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		players = engine.getEntitiesFor(Family.all(PlayerComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		CameraComponent c = ccm.get(entity);
		for (Entity p : players) {
			final Vector2 vec = pcm.get(p).getPosition();
			float angle = pcm.get(p).getRotation();
			final float cos = MathUtils.cos(angle);
			final float sin = MathUtils.sin(angle);
			final float dX = c.getPosOffset().x * cos - c.getPosOffset().y * sin;
			final float dY = c.getPosOffset().x * sin + c.getPosOffset().y * cos;
			c.setPosition(vec.add(new Vector2(dX, dY)));
			c.setRotation(-MathUtils.radiansToDegrees * (c.getRotationOffset() + angle));
		}
	}

	@Override
	public void receive(Signal<ResizeSignal> signal, ResizeSignal resizeSignal) {
		for (Entity e : getEntities()) {
			ccm.get(e).getViewport().update(resizeSignal.width, resizeSignal.height);
		}
	}
}
