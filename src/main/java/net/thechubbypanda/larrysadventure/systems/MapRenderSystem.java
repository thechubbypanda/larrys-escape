package net.thechubbypanda.larrysadventure.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.thechubbypanda.larrysadventure.Globals;
import net.thechubbypanda.larrysadventure.components.TileMapComponent;

public class MapRenderSystem extends IteratingSystem {

	private ComponentMapper<TileMapComponent> tmm = ComponentMapper.getFor(TileMapComponent.class);

	private SpriteBatch batch;
	private Camera camera;

	public MapRenderSystem(OrthographicCamera camera) {
		super(Family.all(TileMapComponent.class).get(), Globals.SystemPriority.MAP_RENDER);
		this.camera = camera;
		batch = new SpriteBatch();
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		tmm.get(entity).render(batch);
		batch.end();
	}

	@Override
	public void removedFromEngine(Engine engine) {
		super.removedFromEngine(engine);
		batch.dispose();
	}
}
