package net.thechubbypanda.larrysadventure.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import net.thechubbypanda.larrysadventure.Globals;
import net.thechubbypanda.larrysadventure.components.CameraComponent;
import net.thechubbypanda.larrysadventure.signals.ResizeSignal;

public class CameraSystem extends IteratingSystem implements Listener<ResizeSignal> {

	//private ComponentMapper<PhysicsComponent> physicsMapper = ComponentMapper.getFor(PhysicsComponent.class);
	private final ComponentMapper<CameraComponent> ccm = ComponentMapper.getFor(CameraComponent.class);

	public CameraSystem() {
		super(Family.all(CameraComponent.class).get(), Globals.SystemPriority.VIEWPORT);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		CameraComponent c = ccm.get(entity);
		if (c.isFollowing()) {
			final Vector2 vec = c.getFollowPosition();
			float angle = c.getFollowRotation();
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
