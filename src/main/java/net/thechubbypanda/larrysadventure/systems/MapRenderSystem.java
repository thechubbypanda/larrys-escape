package net.thechubbypanda.larrysadventure.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.thechubbypanda.larrysadventure.Globals;
import net.thechubbypanda.larrysadventure.components.CameraComponent;
import net.thechubbypanda.larrysadventure.components.TileMapComponent;

public class MapRenderSystem extends IteratingSystem {

	private final ComponentMapper<TileMapComponent> tmm = ComponentMapper.getFor(TileMapComponent.class);

	private final SpriteBatch batch;

	public MapRenderSystem() {
		super(Family.all(TileMapComponent.class).get(), Globals.SystemPriority.MAP_RENDER);
		batch = new SpriteBatch();
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		CameraComponent cc = CameraComponent.getMainCameraComponent();
		if (cc != null) {
			batch.setProjectionMatrix(cc.getCamera().combined);
		}
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
