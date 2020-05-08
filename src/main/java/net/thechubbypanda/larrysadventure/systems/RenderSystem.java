package net.thechubbypanda.larrysadventure.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.thechubbypanda.larrysadventure.Globals;
import net.thechubbypanda.larrysadventure.components.AnimationComponent;
import net.thechubbypanda.larrysadventure.components.CameraComponent;
import net.thechubbypanda.larrysadventure.components.SpriteComponent;
import net.thechubbypanda.larrysadventure.components.TransformComponent;

import java.util.Comparator;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

public class RenderSystem extends SortedIteratingSystem {

	private static final ComponentMapper<TransformComponent> tcm = ComponentMapper.getFor(TransformComponent.class);

	private final ComponentMapper<SpriteComponent> scm = ComponentMapper.getFor(SpriteComponent.class);
	private final ComponentMapper<AnimationComponent> acm = ComponentMapper.getFor(AnimationComponent.class);

	private final Batch batch;

	public RenderSystem() {
		super(Family.all(TransformComponent.class).one(SpriteComponent.class, AnimationComponent.class).get(), new Comparator<Entity>() {
			@Override
			public int compare(Entity o1, Entity o2) {
				return (int) Math.signum(tcm.get(o1).getZ() - tcm.get(o2).getZ());
			}
		}, Globals.SystemPriority.RENDER);
		batch = new SpriteBatch();
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
	}

	@Override
	public void update(float deltaTime) {
		glClear(GL_COLOR_BUFFER_BIT);
		batch.begin();
		super.update(deltaTime);
		batch.flush();
		batch.end();
	}

	@Override
	public void removedFromEngine(Engine engine) {
		super.removedFromEngine(engine);
		batch.dispose();
	}
}
