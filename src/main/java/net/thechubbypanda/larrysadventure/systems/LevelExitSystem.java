package net.thechubbypanda.larrysadventure.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.ashley.systems.IteratingSystem;
import net.thechubbypanda.larrysadventure.CollisionSignal;
import net.thechubbypanda.larrysadventure.LevelManager;
import net.thechubbypanda.larrysadventure.components.LevelExitComponent;
import net.thechubbypanda.larrysadventure.components.PlayerComponent;

public class LevelExitSystem extends IteratingSystem implements Listener<CollisionSignal> {

	private final LevelManager lm;
	private final ComponentMapper<PlayerComponent> pcm = ComponentMapper.getFor(PlayerComponent.class);

	private boolean toBump = false;

	public LevelExitSystem(LevelManager lm) {
		super(Family.all(LevelExitComponent.class).get());

		this.lm = lm;
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		if (toBump) {
			lm.bumpLevel();
			toBump = false;
		}
	}

	@Override
	public void receive(Signal<CollisionSignal> signal, CollisionSignal object) {
		if (getEntities().contains(object.entity, true)) {
			if (object.object instanceof Entity) {
				if (pcm.has((Entity) object.object)) {
					toBump = true;
				}
			}
		}
	}
}
