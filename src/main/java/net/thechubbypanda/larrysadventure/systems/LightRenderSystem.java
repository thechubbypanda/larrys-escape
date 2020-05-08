package net.thechubbypanda.larrysadventure.systems;

import box2dLight.RayHandler;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import net.thechubbypanda.larrysadventure.Globals;

public class LightRenderSystem extends EntitySystem {

	private final RayHandler rayHandler;
	private final OrthographicCamera camera;

	public LightRenderSystem(RayHandler rayHandler, OrthographicCamera camera) {
		super(Globals.SystemPriority.POST_RENDER);
		this.rayHandler = rayHandler;
		this.camera = camera;
		this.rayHandler.setCombinedMatrix(camera);
	}

	@Override
	public void update(float deltaTime) {
		rayHandler.setCombinedMatrix(camera);
		rayHandler.update();
		rayHandler.render();
	}

	@Override
	public void removedFromEngine(Engine engine) {
		rayHandler.dispose();
	}
}
