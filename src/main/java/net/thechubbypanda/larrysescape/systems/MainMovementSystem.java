package net.thechubbypanda.larrysescape.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import net.thechubbypanda.larrysescape.components.AnimationComponent;
import net.thechubbypanda.larrysescape.components.PlayerComponent;
import net.thechubbypanda.larrysescape.components.SpriteComponent;
import net.thechubbypanda.larrysescape.components.TransformComponent;

public class MainMovementSystem extends IteratingSystem {

	private final ComponentMapper<TransformComponent> tcm = ComponentMapper.getFor(TransformComponent.class);
	private final ComponentMapper<SpriteComponent> scm = ComponentMapper.getFor(SpriteComponent.class);
	private final ComponentMapper<AnimationComponent> acm = ComponentMapper.getFor(AnimationComponent.class);

	public MainMovementSystem() {
		super(Family.all(TransformComponent.class).one(SpriteComponent.class, AnimationComponent.class).exclude(PlayerComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		Vector2 position = tcm.get(entity).getPosition();
		float rotation = tcm.get(entity).getRotation();
		if (scm.has(entity)) {
			scm.get(entity).setPosition(position);
			scm.get(entity).setRotation(rotation);
		}
		if (acm.has(entity)) {
			acm.get(entity).setPosition(position);
			acm.get(entity).setRotation(rotation);
		}
	}
}
