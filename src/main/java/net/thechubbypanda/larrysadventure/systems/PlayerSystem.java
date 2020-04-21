package net.thechubbypanda.larrysadventure.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import net.thechubbypanda.larrysadventure.Globals;
import net.thechubbypanda.larrysadventure.components.ConeLightComponent;
import net.thechubbypanda.larrysadventure.components.PhysicsComponent;
import net.thechubbypanda.larrysadventure.components.SpriteComponent;
import net.thechubbypanda.larrysadventure.signals.InputSignal;

public class PlayerSystem extends EntitySystem implements Listener<InputSignal> {

	private final OrthographicCamera camera;

	private final PhysicsComponent pc;
	private final SpriteComponent sc;
	private final ConeLightComponent clc;

	private final Vector2 vel = new Vector2();

	private float targetRotation = 0;
	private float lerpPercent = 0;

	public PlayerSystem(Entity player, OrthographicCamera camera) {
		pc = player.getComponent(PhysicsComponent.class);
		sc = player.getComponent(SpriteComponent.class);
		clc = player.getComponent(ConeLightComponent.class);
		Globals.inputSignal.add(this);
		this.camera = camera;
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

		Vector3 mousePos = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
		float diffX = mousePos.x - pc.getPosition().x;
		float diffY = mousePos.y - pc.getPosition().y;
		float angle = (float) Math.atan2(diffY, diffX);
		clc.setBodyAngleOffset(angle * MathUtils.radiansToDegrees);
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
