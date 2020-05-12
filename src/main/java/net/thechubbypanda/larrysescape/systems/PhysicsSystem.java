package net.thechubbypanda.larrysescape.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import net.thechubbypanda.larrysescape.Globals;
import net.thechubbypanda.larrysescape.components.PhysicsComponent;
import net.thechubbypanda.larrysescape.components.TransformComponent;

import static net.thechubbypanda.larrysescape.Globals.PPM;

public class PhysicsSystem extends IteratingSystem {

	private final ComponentMapper<PhysicsComponent> phcm = ComponentMapper.getFor(PhysicsComponent.class);
	private final ComponentMapper<TransformComponent> tcm = ComponentMapper.getFor(TransformComponent.class);

	private final World world;

	public PhysicsSystem(World world) {
		super(Family.all(PhysicsComponent.class, TransformComponent.class).get(), Globals.SystemPriority.PRE_UPDATE);
		this.world = world;
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		tcm.get(entity).setPosition(new Vector2(phcm.get(entity).getPosition()).scl(PPM));
		tcm.get(entity).setRotation(phcm.get(entity).getRotation() * MathUtils.radiansToDegrees);
	}
}
