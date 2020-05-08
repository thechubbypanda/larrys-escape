package net.thechubbypanda.larrysadventure.entityListeners;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.thechubbypanda.larrysadventure.components.CameraComponent;
import net.thechubbypanda.larrysadventure.components.TransformComponent;

import java.util.ArrayList;

public class EnemyListener implements EntityListener {

	private final ComponentMapper<TransformComponent> tcm = ComponentMapper.getFor(TransformComponent.class);

	private final ArrayList<ParticleEffect> running = new ArrayList<>();
	private final ArrayList<ParticleEffect> free = new ArrayList<>();
	private final SpriteBatch batch = new SpriteBatch();

	public EnemyListener() {
		for (int i = 0; i < 10; i++) {
			ParticleEffect pe = new ParticleEffect();
			pe.load(Gdx.files.internal("explosion.p"), Gdx.files.internal(""));
			free.add(pe);
		}
	}

	public void render(float delta) {
		batch.setProjectionMatrix(CameraComponent.getMainCameraComponent().getCamera().combined);
		batch.begin();
		for (int i = running.size() - 1; i >= 0; i--) {
			running.get(i).update(delta);
			running.get(i).draw(batch, delta);
			if (running.get(i).isComplete()) {
				free.add(running.get(i));
				running.remove(running.get(i));
			}
		}
		batch.end();
	}

	@Override
	public void entityAdded(Entity entity) {

	}

	@Override
	public void entityRemoved(Entity entity) {
		ParticleEffect pe = free.get(0);
		free.remove(pe);
		running.add(pe);
		pe.reset();
		pe.getEmitters().first().setPosition(tcm.get(entity).getPosition().x, tcm.get(entity).getPosition().y);
		pe.start();
	}
}
