package net.thechubbypanda.larrysadventure.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.thechubbypanda.larrysadventure.Globals;
import net.thechubbypanda.larrysadventure.components.CameraComponent;
import net.thechubbypanda.larrysadventure.components.PlayerComponent;
import net.thechubbypanda.larrysadventure.components.SpriteComponent;

public class PlayerRenderSystem extends IteratingSystem {

	private final Batch batch;
	private final ComponentMapper<SpriteComponent> scm = ComponentMapper.getFor(SpriteComponent.class);

	public PlayerRenderSystem() {
		super(Family.all(PlayerComponent.class).get(), Globals.SystemPriority.PLAYER_RENDER);
		batch = new SpriteBatch();
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		CameraComponent cc = CameraComponent.getMainCameraComponent();
		if (cc != null) {
			batch.setProjectionMatrix(cc.getCamera().combined);
		}
		batch.begin();
		scm.get(entity).sprite.draw(batch);
		batch.end();
	}

	@Override
	public void removedFromEngine(Engine engine) {
		super.removedFromEngine(engine);
		batch.dispose();
	}
}
