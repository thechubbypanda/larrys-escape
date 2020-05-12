package net.thechubbypanda.larrysadventure.components;

import com.badlogic.ashley.core.Component;

/**
 * Defines how much damage an entity does
 */
public class DamageComponent implements Component {

	public final int damage;
	public final long hitInterval;

	public DamageComponent(int damage) {
		this.damage = damage;
		this.hitInterval = -1;
	}

	public DamageComponent(int damage, long hitInterval) {
		this.damage = damage;
		this.hitInterval = hitInterval;
	}
}
