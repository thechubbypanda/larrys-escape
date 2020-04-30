package net.thechubbypanda.larrysadventure.components;

import com.badlogic.ashley.core.Component;

public class HealthComponent implements Component {

	private int health;

	public HealthComponent(int initialHealth) {
		this.health = initialHealth;
	}

	public int getHealth() {
		return health;
	}

	public void addHealth(int delta) {
		this.health += delta;
	}
}
