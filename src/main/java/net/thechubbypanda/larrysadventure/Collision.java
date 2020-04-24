package net.thechubbypanda.larrysadventure;

import com.badlogic.ashley.core.Entity;

public class Collision {

	public Entity entity;
	public Object object;

	public Collision(Entity entity, Object object) {
		this.entity = entity;
		this.object = object;
	}
}
