package net.thechubbypanda.larrysadventure.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.thechubbypanda.larrysadventure.Globals;
import net.thechubbypanda.larrysadventure.components.AnimationComponent;
import net.thechubbypanda.larrysadventure.components.CameraComponent;
import net.thechubbypanda.larrysadventure.components.PlayerComponent;
import net.thechubbypanda.larrysadventure.components.SpriteComponent;

public class MainRenderSystem extends IteratingSystem {

	private final Batch batch;
	private final ComponentMapper<SpriteComponent> spriteMapper = ComponentMapper.getFor(SpriteComponent.class);
	private final ComponentMapper<AnimationComponent> animationMapper = ComponentMapper.getFor(AnimationComponent.class);

	public MainRenderSystem() {
		super(Family.one(SpriteComponent.class, AnimationComponent.class).exclude(PlayerComponent.class).get(), Globals.SystemPriority.MAIN_RENDER);
		batch = new SpriteBatch();
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		CameraComponent cc = CameraComponent.getMainCameraComponent();
		if (cc != null) {
			batch.setProjectionMatrix(cc.getCamera().combined);
		}
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
