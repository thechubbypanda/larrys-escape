package net.thechubbypanda.larrysadventure.systems;

import box2dLight.RayHandler;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.World;
import net.thechubbypanda.larrysadventure.Globals;

public class LightRenderSystem extends EntitySystem {

	private RayHandler rayHandler;

	public LightRenderSystem(World world, Camera camera) {
		super(Globals.SystemPriority.LIGHT_RENDER);
		rayHandler = new RayHandler(world);
		rayHandler.setCombinedMatrix((OrthographicCamera)camera);
	}

	@Override
	public void update(float deltaTime) {
		rayHandler.update();
		rayHandler.render();
	}

	@Override
	public void removedFromEngine(Engine engine) {
		rayHandler.dispose();
	}
}
