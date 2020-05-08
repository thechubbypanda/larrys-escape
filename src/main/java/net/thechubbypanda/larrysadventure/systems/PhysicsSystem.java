package net.thechubbypanda.larrysadventure.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import net.thechubbypanda.larrysadventure.Globals;
import net.thechubbypanda.larrysadventure.components.PhysicsComponent;
import net.thechubbypanda.larrysadventure.components.TransformComponent;

import static net.thechubbypanda.larrysadventure.Globals.PPM;

public class PhysicsSystem extends IteratingSystem {

	private final ComponentMapper<PhysicsComponent> phcm = ComponentMapper.getFor(PhysicsComponent.class);
	private final ComponentMapper<TransformComponent> pocm = ComponentMapper.getFor(TransformComponent.class);

	public PhysicsSystem() {
		super(Family.all(PhysicsComponent.class, TransformComponent.class).get(), Globals.SystemPriority.PHYSICS);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		pocm.get(entity).setPosition(new Vector2(phcm.get(entity).getPosition()).scl(PPM));
		pocm.get(entity).setRotation(phcm.get(entity).getRotation() * MathUtils.radiansToDegrees);
	}
}
