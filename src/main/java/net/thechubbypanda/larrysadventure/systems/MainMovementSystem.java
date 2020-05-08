package net.thechubbypanda.larrysadventure.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import net.thechubbypanda.larrysadventure.components.*;

public class MainMovementSystem extends IteratingSystem {

	private final ComponentMapper<TransformComponent> positionMapper = ComponentMapper.getFor(TransformComponent.class);
	private final ComponentMapper<SpriteComponent> spriteMapper = ComponentMapper.getFor(SpriteComponent.class);
	private final ComponentMapper<AnimationComponent> animationMapper = ComponentMapper.getFor(AnimationComponent.class);

	public MainMovementSystem() {
		super(Family.all(PhysicsComponent.class).exclude(PlayerComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		Vector2 position = positionMapper.get(entity).getPosition();
		float rotation = positionMapper.get(entity).getRotation();
		if (spriteMapper.has(entity)) {
			spriteMapper.get(entity).setPosition(position);
			spriteMapper.get(entity).sprite.setRotation(rotation);
		}
		if (animationMapper.has(entity)) {
			animationMapper.get(entity).setPosition(position);
			animationMapper.get(entity).setRotation(rotation);
		}
	}
}
