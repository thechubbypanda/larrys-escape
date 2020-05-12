package net.thechubbypanda.larrysescape.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.ashley.systems.IteratingSystem;
import net.thechubbypanda.larrysescape.CollisionSignal;
import net.thechubbypanda.larrysescape.LevelManager;
import net.thechubbypanda.larrysescape.components.LevelExitComponent;
import net.thechubbypanda.larrysescape.components.PlayerComponent;

public class LevelExitSystem extends IteratingSystem implements Listener<CollisionSignal> {

	private final LevelManager lm;
	private final ComponentMapper<PlayerComponent> plcm = ComponentMapper.getFor(PlayerComponent.class);

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
		if (getEntities().contains(object.entityA, true)) {
			if (object.objectB instanceof Entity) {
				if (plcm.has((Entity) object.objectB)) {
					toBump = true;
				}
			}
		}
	}
}
