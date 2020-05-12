package net.thechubbypanda.larrysescape.components;

import com.badlogic.ashley.core.Component;

import java.util.HashMap;

import static com.badlogic.gdx.math.MathUtils.clamp;

public class HealthComponent implements Component {

	private final int maxHealth;
	private int health;

	public final HashMap<DamageComponent, Long> beingHitBy = new HashMap<>();

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
