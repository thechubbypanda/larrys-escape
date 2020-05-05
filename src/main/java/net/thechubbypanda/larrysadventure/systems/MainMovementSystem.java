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

	private final ComponentMapper<PhysicsComponent> pcm = ComponentMapper.getFor(PhysicsComponent.class);
	private final ComponentMapper<SpriteComponent> scm = ComponentMapper.getFor(SpriteComponent.class);
	private final ComponentMapper<AnimationComponent> acm = ComponentMapper.getFor(AnimationComponent.class);

	public MainMovementSystem() {
		super(Family.all(PhysicsComponent.class).one(SpriteComponent.class, AnimationComponent.class).exclude(PlayerComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		Vector2 position = pcm.get(entity).getPosition();
		float rotation = pcm.get(entity).getRotation();
		if (scm.has(entity)) {
			scm.get(entity).setPosition(position);
			scm.get(entity).sprite.setRotation(rotation * MathUtils.radiansToDegrees);
		}
		if (acm.has(entity)) {
			acm.get(entity).setPosition(position);
			acm.get(entity).setRotation(rotation);
		}
	}
}
