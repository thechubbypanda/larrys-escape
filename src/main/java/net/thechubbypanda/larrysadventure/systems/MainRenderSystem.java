package net.thechubbypanda.larrysadventure.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.thechubbypanda.larrysadventure.Globals;
import net.thechubbypanda.larrysadventure.components.AnimationComponent;
import net.thechubbypanda.larrysadventure.components.SpriteComponent;

public class MainRenderSystem extends IteratingSystem {

	private Batch batch;
	private Camera camera;
	private ComponentMapper<SpriteComponent> spriteMapper = ComponentMapper.getFor(SpriteComponent.class);
	private ComponentMapper<AnimationComponent> animationMapper = ComponentMapper.getFor(AnimationComponent.class);

	public MainRenderSystem(Camera camera) {
		super(Family.one(SpriteComponent.class, AnimationComponent.class).get(), Globals.SystemPriority.MAIN_RENDER);
		this.camera = camera;
		batch = new SpriteBatch();
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		if (spriteMapper.has(entity)) {
			spriteMapper.get(entity).sprite.draw(batch);
		}
		if (animationMapper.has(entity)) {
			animationMapper.get(entity).draw(batch);
		}
		batch.end();
	}

	@Override
	public void removedFromEngine(Engine engine) {
		super.removedFromEngine(engine);
		batch.dispose();
	}
}
