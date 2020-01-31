package net.thechubbypanda.larrysadventure.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import net.thechubbypanda.larrysadventure.components.PhysicsComponent;
import net.thechubbypanda.larrysadventure.components.PlayerComponent;

public class PlayerControlSystem extends IteratingSystem {

	private ComponentMapper<PlayerComponent> playerMapper = ComponentMapper.getFor(PlayerComponent.class);
	private ComponentMapper<PhysicsComponent> physicsMapper = ComponentMapper.getFor(PhysicsComponent.class);

	private final Vector2 vel = new Vector2();

	public PlayerControlSystem() {
		super(Family.all(PlayerComponent.class, PhysicsComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
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

		physicsMapper.get(entity).body.setLinearVelocity(vel.nor());
	}
}
