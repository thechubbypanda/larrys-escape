package net.thechubbypanda.larrysescape.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import net.thechubbypanda.larrysescape.components.*;

public class MainMovementSystem extends IteratingSystem {

	private final ComponentMapper<TransformComponent> tcm = ComponentMapper.getFor(TransformComponent.class);
	private final ComponentMapper<SpriteComponent> scm = ComponentMapper.getFor(SpriteComponent.class);
	private final ComponentMapper<AnimationComponent> acm = ComponentMapper.getFor(AnimationComponent.class);
	private final ComponentMapper<EnemyComponent> ecm = ComponentMapper.getFor(EnemyComponent.class);

	public MainMovementSystem() {
		super(Family.all(TransformComponent.class).one(SpriteComponent.class, AnimationComponent.class).exclude(PlayerComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		Vector2 position = tcm.get(entity).getPosition();
		float rotation = tcm.get(entity).getRotation();
		if (scm.has(entity)) {
			scm.get(entity).setPosition(position);
			if (ecm.has(entity)) {
				scm.get(entity).setRotation(rotation - 90);
			} else {
				scm.get(entity).setRotation(rotation);
			}
		}
		if (acm.has(entity)) {
			acm.get(entity).setPosition(position);
			acm.get(entity).setRotation(rotation);
		}
	}
}
