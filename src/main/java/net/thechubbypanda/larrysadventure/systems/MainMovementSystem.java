package net.thechubbypanda.larrysadventure.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import net.thechubbypanda.larrysadventure.components.AnimationComponent;
import net.thechubbypanda.larrysadventure.components.PhysicsComponent;
import net.thechubbypanda.larrysadventure.components.PlayerComponent;
import net.thechubbypanda.larrysadventure.components.SpriteComponent;

public class MainMovementSystem extends IteratingSystem {

	private ComponentMapper<PhysicsComponent> physicsMapper = ComponentMapper.getFor(PhysicsComponent.class);
	private ComponentMapper<SpriteComponent> spriteMapper = ComponentMapper.getFor(SpriteComponent.class);
	private ComponentMapper<AnimationComponent> animationMapper = ComponentMapper.getFor(AnimationComponent.class);

	public MainMovementSystem() {
		super(Family.all(PhysicsComponent.class).exclude(PlayerComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		Vector2 position = physicsMapper.get(entity).getPosition();
		float rotation = physicsMapper.get(entity).getRotation();
		if (spriteMapper.has(entity)) {
			spriteMapper.get(entity).setPosition(position);
			spriteMapper.get(entity).sprite.setRotation(rotation * MathUtils.radiansToDegrees);
		}
		if (animationMapper.has(entity)) {
			animationMapper.get(entity).setPosition(position);
			animationMapper.get(entity).setRotation(rotation);
		}
	}
}
