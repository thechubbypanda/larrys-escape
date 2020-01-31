package net.thechubbypanda.larrysadventure.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import net.thechubbypanda.larrysadventure.Globals;
import net.thechubbypanda.larrysadventure.components.AnimationComponent;
import net.thechubbypanda.larrysadventure.components.SpriteComponent;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

public class RenderSystem extends IteratingSystem {

	private Batch batch;
	private Camera camera;
	private ComponentMapper<SpriteComponent> spriteMapper = ComponentMapper.getFor(SpriteComponent.class);
	private ComponentMapper<AnimationComponent> animationMapper = ComponentMapper.getFor(AnimationComponent.class);

	public RenderSystem(Batch batch, Camera camera) {
		super(Family.one(SpriteComponent.class, AnimationComponent.class).get(), Globals.SystemPriority.RENDER);
		this.camera = camera;
		this.batch = batch;
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		glClear(GL_COLOR_BUFFER_BIT);
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
}
