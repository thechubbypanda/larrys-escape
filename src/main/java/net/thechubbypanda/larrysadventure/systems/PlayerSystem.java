package net.thechubbypanda.larrysadventure.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import net.thechubbypanda.larrysadventure.Globals;
import net.thechubbypanda.larrysadventure.components.PhysicsComponent;
import net.thechubbypanda.larrysadventure.components.SpriteComponent;
import net.thechubbypanda.larrysadventure.signals.InputSignal;

public class PlayerSystem extends EntitySystem implements Listener<InputSignal> {

	private PhysicsComponent pc;
	private SpriteComponent sc;

	private final Vector2 vel = new Vector2();

	private float targetRotation = 0;
	private float lerpPercent = 0;

	public PlayerSystem(Entity player) {
		pc = player.getComponent(PhysicsComponent.class);
		sc = player.getComponent(SpriteComponent.class);
		Globals.inputSignal.add(this);
	}

	@Override
	public void update(float deltaTime) {
		vel.setZero();

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

		pc.setRotation(MathUtils.lerpAngle(pc.getRotation(), targetRotation, Math.min(1f, MathUtils.clamp(lerpPercent += deltaTime, 0, 1))));
		pc.setLinearVelocity(vel.rotateRad(pc.getRotation()).nor());

		sc.sprite.setRotation(sc.sprite.getRotation());
		sc.setPosition(pc.getPosition());
	}

	@Override
	public void receive(Signal<InputSignal> signal, InputSignal o) {
		if (o.type == InputSignal.Type.keyDown) {
			if (o.keycode == Input.Keys.Q) {
				targetRotation += MathUtils.PI / 2f;
				lerpPercent = 0;
				while (targetRotation > MathUtils.PI2) {
					targetRotation -= MathUtils.PI2;
				}
			}
			if (o.keycode == Input.Keys.E) {
				targetRotation -= MathUtils.PI / 2f;
				lerpPercent = 0;
				while (targetRotation < -MathUtils.PI2) {
					targetRotation += MathUtils.PI2;
				}
			}
		}
	}
}
