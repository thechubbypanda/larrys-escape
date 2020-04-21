package net.thechubbypanda.larrysadventure.components;

import com.badlogic.ashley.core.Component;

/**
 * Defines how much damage an entity does
 */
public class DamageComponent implements Component {

	private final int damage;

	public DamageComponent(int damage) {
		this.damage = damage;
	}

	public int getDamage() {
		return damage;
	}
}
