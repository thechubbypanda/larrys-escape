package net.thechubbypanda.larrysadventure.scripts;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import net.thechubbypanda.larrysadventure.components.PhysicsComponent;
import net.thechubbypanda.larrysadventure.components.ScriptComponent;

public class PlayerScript implements ScriptComponent {

	private final Vector2 vel = new Vector2();

	public void update(Entity entity) {
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

		if (vel.len2() <= 0) {
			//playerAnimationComponent.setToInitialFrame();
		}

		entity.getComponent(PhysicsComponent.class).body.setLinearVelocity(vel.nor());
	}
}
