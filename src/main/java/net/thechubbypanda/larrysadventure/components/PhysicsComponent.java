package net.thechubbypanda.larrysadventure.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import static net.thechubbypanda.larrysadventure.Globals.PPM;

public class PhysicsComponent implements Component {

	private Body body;

	public PhysicsComponent(Body body) {
		this.body = body;
	}

	public Vector2 getPosition() {
		return body.getPosition().scl(PPM);
	}

	public float getRotation() {
		return body.getAngle();
	}

	public void setLinearVelocity(Vector2 vel) {
		body.setLinearVelocity(vel);
	}

	public void setRotationalVelocity(float vel) {
		body.setAngularVelocity(vel);
	}

	public void removeBody(World world) {
		world.destroyBody(body);
	}
}
