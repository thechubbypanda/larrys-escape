package net.thechubbypanda.larrysadventure.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import net.thechubbypanda.larrysadventure.Globals;
import net.thechubbypanda.larrysadventure.components.SpriteComponent;
import net.thechubbypanda.larrysadventure.signals.InputSignal;

public class PlayerRenderSystem extends EntitySystem {

	private Batch batch;
	private OrthographicCamera camera;
	private SpriteComponent sc;

	public PlayerRenderSystem(OrthographicCamera camera, Entity player) {
		super(Globals.SystemPriority.PLAYER_RENDER);
		this.camera = camera;
		sc = player.getComponent(SpriteComponent.class);
		batch = new SpriteBatch();
	}

	@Override
	public void update(float deltaTime) {
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		sc.sprite.draw(batch);
		batch.end();
	}

	@Override
	public void removedFromEngine(Engine engine) {
		super.removedFromEngine(engine);
		batch.dispose();
	}
}
