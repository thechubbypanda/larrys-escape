package net.thechubbypanda.larrysadventure.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import net.thechubbypanda.larrysadventure.Globals;
import net.thechubbypanda.larrysadventure.components.PhysicsComponent;
import net.thechubbypanda.larrysadventure.components.SpriteComponent;
import net.thechubbypanda.larrysadventure.signals.InputSignal;

public class PlayerSystem extends EntitySystem implements Listener<InputSignal> {

	private PhysicsComponent pc;
	private SpriteComponent sc;

	private final Vector2 vel = new Vector2();

	private float targetRotation = 0;

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

		pc.setRotation(MathUtils.lerpAngle(pc.getRotation(), targetRotation, 8f * deltaTime));
		pc.setLinearVelocity(vel.rotateRad(pc.getRotation()).nor());

		sc.setPosition(pc.getPosition());
	}

	@Override
	public void receive(Signal<InputSignal> signal, InputSignal o) {
		if (o.type == InputSignal.Type.keyDown) {
			if (o.keycode == Input.Keys.Q) {
				targetRotation += Math.PI / 2f;
			}
			if (o.keycode == Input.Keys.E) {
				targetRotation -= Math.PI / 2f;
			}
			if (targetRotation <= -Math.PI * 2) {
				targetRotation += Math.PI * 2;
			}
		}

		if (o.type == InputSignal.Type.mouseDragged || o.type == InputSignal.Type.mouseMoved) {
			//Vector3 v = camera.unproject(new Vector3(o.x, o.y, 0));

			System.out.println(sc.getPosition().angle(new Vector2(o.x, o.y)));
			sc.sprite.setRotation(sc.getPosition().angle(new Vector2(o.x, o.y)));
		}
	}
}
