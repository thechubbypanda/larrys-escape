package net.thechubbypanda.larrysadventure.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import net.thechubbypanda.larrysadventure.Globals;

import static net.thechubbypanda.larrysadventure.Globals.DEBUG;

public class DebugRenderSystem extends EntitySystem {

	private final Box2DDebugRenderer debugRenderer;
	private final Camera camera;
	private final World world;

	public DebugRenderSystem(World world, Camera camera) {
		super(Globals.SystemPriority.POST_RENDER);
		debugRenderer = new Box2DDebugRenderer();
		debugRenderer.setDrawVelocities(true);
		this.camera = camera;
		this.world = world;
	}

	public void update(float deltatime) {
		if (DEBUG) debugRenderer.render(world, camera.combined);
	}
}
