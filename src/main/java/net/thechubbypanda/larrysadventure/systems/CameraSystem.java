package net.thechubbypanda.larrysadventure.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import net.thechubbypanda.larrysadventure.Globals;
import net.thechubbypanda.larrysadventure.components.PhysicsComponent;
import net.thechubbypanda.larrysadventure.components.PlayerComponent;

import static net.thechubbypanda.larrysadventure.Globals.PPM;

public class CameraSystem extends IteratingSystem {

	private ComponentMapper<PhysicsComponent> physicsMapper = ComponentMapper.getFor(PhysicsComponent.class);
	private Camera camera, b2dCamera;

	public CameraSystem(Camera camera, Camera b2dCamera) {
		super(Family.all(PlayerComponent.class, PhysicsComponent.class).get(), Globals.SystemPriority.CAMERA);
		this.camera = camera;
		this.b2dCamera = b2dCamera;
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		Vector2 playerPosition = physicsMapper.get(entity).getPosition();
		camera.position.lerp(new Vector3(playerPosition, 0), 5f * Gdx.graphics.getDeltaTime());
		b2dCamera.position.lerp(new Vector3(playerPosition.scl(1f / PPM), 0), 5f * Gdx.graphics.getDeltaTime());
	}
}
