package net.thechubbypanda.larrysadventure.components;

import com.badlogic.ashley.core.Component;

import static com.badlogic.gdx.math.MathUtils.clamp;

public class HealthComponent implements Component {

	private final int maxHealth;
	private int health;

	public HealthComponent(int maxHealth) {
		this(maxHealth, maxHealth);
	}

	public HealthComponent(int maxHealth, int initialHealth) {
		this.maxHealth = maxHealth;
		this.health = initialHealth;
	}

	public int getHealth() {
		return health;
	}

	public void addHealth(int delta) {
		health += delta;
		health = clamp(health, 0, maxHealth);
	}

	public void reset() {
		health = maxHealth;
	}
}
