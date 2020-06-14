package net.thechubbypanda.larrysescape;

import com.badlogic.ashley.core.Entity;

public class CollisionSignal {

	public final Entity entityA;
	public final Object objectB;
	public final boolean colliding;

	public CollisionSignal(Entity entityA, Object objectB, boolean colliding) {
		this.entityA = entityA;
		this.objectB = objectB;
		this.colliding = colliding;
	}
}
