package net.thechubbypanda.larrysescape.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public class PhysicsComponent implements Component {

	private final Body body;

	public PhysicsComponent(Entity e, Body body) {
		this.body = body;
		this.body.setUserData(e);
	}

	public Vector2 getVelocity() {
		return body.getLinearVelocity();
	}

	/**
	 * Returns the unscaled box2D position of the body
	 */
	public Vector2 getPosition() {
		return body.getPosition();
	}

	/**
	 * Returns the angle of the body in radians
	 */
	public float getRotation() {
		return body.getAngle();
	}

	public void setLinearVelocity(Vector2 vel) {
		body.setLinearVelocity(vel);
	}

	/**
	 * Sets the unscaled box2D position of the body
	 */
	public void setPosition(Vector2 position) {
		body.setTransform(position.x, position.y, getRotation());
	}

	/**
	 * Sets the body's angle
	 *
	 * @param angle in radians
	 */
	public void setRotation(float angle) {
		body.setAngularVelocity(0);
		body.setTransform(body.getPosition(), angle);
		body.setAngularVelocity(0);
	}

	public void dispose(World world) {
		world.destroyBody(body);
	}
}
