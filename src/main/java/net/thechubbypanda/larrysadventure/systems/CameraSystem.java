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
import net.thechubbypanda.larrysadventure.components.TransformComponent;
import net.thechubbypanda.larrysadventure.signals.ResizeSignal;

import java.util.Random;

public class CameraSystem extends IteratingSystem implements Listener<ResizeSignal> {

	private final ComponentMapper<CameraComponent> ccm = ComponentMapper.getFor(CameraComponent.class);
	private final ComponentMapper<TransformComponent> tcm = ComponentMapper.getFor(TransformComponent.class);
	private final ComponentMapper<PhysicsComponent> phcm = ComponentMapper.getFor(PhysicsComponent.class);

	private ImmutableArray<Entity> players;

	private Random random = new Random();

	public CameraSystem() {
		super(Family.all(CameraComponent.class).get(), Globals.SystemPriority.POST_UPDATE);
	}

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		players = engine.getEntitiesFor(Family.all(PlayerComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		CameraComponent cc = ccm.get(entity);
		Vector2 shake = new Vector2(random.nextFloat(), random.nextFloat());
		for (Entity p : players) {
			final Vector2 vec = tcm.get(p).getPosition();
			float angle = phcm.get(p).getRotation();
			final float cos = MathUtils.cos(angle);
			final float sin = MathUtils.sin(angle);
			final float dX = cc.getPosOffset().x * cos - cc.getPosOffset().y * sin;
			final float dY = cc.getPosOffset().x * sin + cc.getPosOffset().y * cos;
			cc.setRotation(-MathUtils.radiansToDegrees * (cc.getRotationOffset() + angle));
			vec.add(new Vector2(dX, dY));
			if (cc.shakeDuration > 0f) {
				vec.add(new Vector2(shake).nor().scl(cc.shakeMagnitude).scl(1 / cc.shakeInitialDuration).scl(cc.shakeDuration));
				cc.shakeDuration -= deltaTime;
			}
			cc.setPosition(vec);
		}
	}

	public void shake(float shakeDuration, float shakeMagnitude) {
		for (Entity c : getEntities()) {
			ccm.get(c).shakeDuration = shakeDuration;
			ccm.get(c).shakeInitialDuration = shakeDuration;
			ccm.get(c).shakeMagnitude = shakeMagnitude;
		}
	}

	@Override
	public void receive(Signal<ResizeSignal> signal, ResizeSignal resizeSignal) {
		for (Entity e : getEntities()) {
			ccm.get(e).viewport.update(resizeSignal.width, resizeSignal.height);
		}
	}
}
