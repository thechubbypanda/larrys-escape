package net.thechubbypanda.larrysadventure.systems;

import box2dLight.RayHandler;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import net.thechubbypanda.larrysadventure.CollisionSignal;
import net.thechubbypanda.larrysadventure.EntityFactory;
import net.thechubbypanda.larrysadventure.Globals;
import net.thechubbypanda.larrysadventure.components.*;
import net.thechubbypanda.larrysadventure.signals.InputSignal;

public class PlayerSystem extends IteratingSystem {

	private static float speed = 1;

	private final ComponentMapper<TransformComponent> tcm = ComponentMapper.getFor(TransformComponent.class);
	private final ComponentMapper<PhysicsComponent> phcm = ComponentMapper.getFor(PhysicsComponent.class);
	private final ComponentMapper<SpriteComponent> scm = ComponentMapper.getFor(SpriteComponent.class);
	private final ComponentMapper<LightComponent> lcm = ComponentMapper.getFor(LightComponent.class);
	private final ComponentMapper<HealthDropComponent> hdcm = ComponentMapper.getFor(HealthDropComponent.class);
	private final ComponentMapper<HealthComponent> hcm = ComponentMapper.getFor(HealthComponent.class);
	private final ComponentMapper<PlayerComponent> plcm = ComponentMapper.getFor(PlayerComponent.class);

	private final RayHandler rayHandler;
	private final World world;
	private final CameraSystem cs;

	private final Vector2 vel = new Vector2();

	private float targetRotation = 0;
	private float lerpPercent = 0;
	private long lastShootTime = System.currentTimeMillis();

	public PlayerSystem(World world, RayHandler rayHandler, CameraSystem cs, Signal<CollisionSignal> collisionSignal, Signal<InputSignal> inputSignal) {
		super(Family.all(PlayerComponent.class).get());
		this.world = world;
		this.rayHandler = rayHandler;
		this.cs = cs;

		collisionSignal.add(new CollisionImpl());
		inputSignal.add(new InputImpl());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		vel.setZero();

		if (Globals.DEBUG) {
			speed = 5;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			vel.y += 1;
			//playerAnimationComponent.play("up");
		} else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			vel.y -= 1;
			//playerAnimationComponent.play("down");
		}

		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			vel.x -= 1;
			//playerAnimationComponent.play("left");
		} else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			vel.x += 1;
			//playerAnimationComponent.play("right");
		}

//		if (vel.len2() <= 0) {
//			//playerAnimationComponent.setToInitialFrame();
//		}

		phcm.get(entity).setRotation(MathUtils.lerpAngle(phcm.get(entity).getRotation(), targetRotation, Math.min(1f, MathUtils.clamp(lerpPercent += deltaTime, 0, 1))));
		phcm.get(entity).setLinearVelocity(vel.rotateRad(phcm.get(entity).getRotation()).nor().scl(speed));

		scm.get(entity).setPosition(tcm.get(entity).getPosition());

		CameraComponent cc = CameraComponent.getMainCameraComponent();
		if (cc != null) {
			Vector3 mousePos = cc.getCamera().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
			float diffX = mousePos.x - tcm.get(entity).getPosition().x;
			float diffY = mousePos.y - tcm.get(entity).getPosition().y;
			float angle = (float) Math.atan2(diffY, diffX);
			lcm.get(entity).setBodyAngleOffset((angle - phcm.get(entity).getRotation()) * MathUtils.radiansToDegrees);
			scm.get(entity).setRotation((angle) * MathUtils.radiansToDegrees);
		}
	}

	private class CollisionImpl implements Listener<CollisionSignal> {
		@Override
		public void receive(Signal<CollisionSignal> signal, CollisionSignal c) {
			if (getEntities().contains(c.entity, true) && hcm.has(c.entity)) {
				if (c.object instanceof Entity && hdcm.has((Entity) c.object)) {
					hcm.get(c.entity).addHealth(hdcm.get((Entity) c.object).health);
				}
			}
		}
	}

	private class InputImpl implements Listener<InputSignal> {
		@Override
		public void receive(Signal<InputSignal> signal, InputSignal i) {
			if (i.type == InputSignal.Type.keyDown) {
				if (i.keycode == Input.Keys.Q) {
					targetRotation += MathUtils.PI / 2f;
					lerpPercent = 0;
					while (targetRotation > MathUtils.PI2) {
						targetRotation -= MathUtils.PI2;
					}
				}
				if (i.keycode == Input.Keys.E) {
					targetRotation -= MathUtils.PI / 2f;
					lerpPercent = 0;
					while (targetRotation < -MathUtils.PI2) {
						targetRotation += MathUtils.PI2;
					}
				}
			}
			if (i.type == InputSignal.Type.mouseDown) {
				if (i.button == Input.Buttons.LEFT) {
					if (lastShootTime <= System.currentTimeMillis() - PlayerComponent.SHOOT_INTERVAL) {
						for (Entity p : getEntities()) {
							if (plcm.get(p).ammo > 0) {
								plcm.get(p).ammo--;
								Vector2 currentPosition = tcm.get(p).getPosition();
								getEngine().addEntity(EntityFactory.bullet(world, rayHandler, currentPosition, new Vector2(i.x, i.y).sub(currentPosition)));
								cs.shake(0.2f, 4f);
							}
						}
						lastShootTime = System.currentTimeMillis();
					}
				}
			}
		}
	}
}
