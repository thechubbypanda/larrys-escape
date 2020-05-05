package net.thechubbypanda.larrysadventure;

import com.badlogic.ashley.core.Entity;

public class CollisionSignal {

	public Entity entity;
	public Object object;

	public CollisionSignal(Entity entity, Object object) {
		this.entity = entity;
		this.object = object;
	}
}
