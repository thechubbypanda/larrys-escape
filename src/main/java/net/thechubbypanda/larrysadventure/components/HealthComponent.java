package net.thechubbypanda.larrysadventure.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;

public class HealthComponent implements Component {

	private final ComponentMapper<DamageComponent> dcm = ComponentMapper.getFor(DamageComponent.class);
	private int health;

	public HealthComponent(int initialHealth) {
		this.health = initialHealth;
	}

	public void hit(Object e) {
		if (e != null && e instanceof Entity) {
			if (dcm.has((Entity)e)) {
				health -= dcm.get((Entity)e).getDamage();
			}
		}
	}

	public int getHealth() {
		return health;
	}
}
