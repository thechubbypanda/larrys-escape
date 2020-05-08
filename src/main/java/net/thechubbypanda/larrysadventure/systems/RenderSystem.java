package net.thechubbypanda.larrysadventure.systems;

import box2dLight.RayHandler;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.thechubbypanda.larrysadventure.Globals;
import net.thechubbypanda.larrysadventure.components.*;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

public class RenderSystem extends SortedIteratingSystem {

	private static final ComponentMapper<TransformComponent> tcm = ComponentMapper.getFor(TransformComponent.class);

	private final ComponentMapper<SpriteComponent> scm = ComponentMapper.getFor(SpriteComponent.class);
	private final ComponentMapper<AnimationComponent> acm = ComponentMapper.getFor(AnimationComponent.class);
	private final ComponentMapper<TileMapComponent> tmcm = ComponentMapper.getFor(TileMapComponent.class);

	private final Batch batch;

	private final RayHandler rayHandler;
	private final OrthographicCamera lightCamera;

	public RenderSystem(RayHandler rayHandler, OrthographicCamera lightCamera) {
		super(
				Family.all(TransformComponent.class).one(SpriteComponent.class, AnimationComponent.class, TileMapComponent.class).get(),
				(e1, e2) -> (int) Math.signum(tcm.get(e1).getZ() - tcm.get(e2).getZ()),
				Globals.SystemPriority.RENDER
		);

		batch = new SpriteBatch();
		this.rayHandler = rayHandler;
		this.lightCamera = lightCamera;
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		CameraComponent cc = CameraComponent.getMainCameraComponent();
		if (cc != null) {
			batch.setProjectionMatrix(cc.getCamera().combined);
		}
		if (scm.has(entity)) {
			scm.get(entity).draw(batch);
		}
		if (acm.has(entity)) {
			acm.get(entity).draw(batch);
		}
		if (tmcm.has(entity)) {
			tmcm.get(entity).draw(batch);
		}
	}

	@Override
	public void update(float deltaTime) {
		glClear(GL_COLOR_BUFFER_BIT);
		batch.begin();
		super.update(deltaTime);
		batch.flush();
		batch.end();
		rayHandler.setCombinedMatrix(lightCamera);
		rayHandler.updateAndRender();
	}

	@Override
	public void removedFromEngine(Engine engine) {
		super.removedFromEngine(engine);
		batch.dispose();
	}
}
