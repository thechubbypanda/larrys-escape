package net.thechubbypanda.larrysadventure.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import static net.thechubbypanda.larrysadventure.Constants.PPM;

public class PhysicsComponent implements Component {

	public Body body;

	public PhysicsComponent(Body body) {
		this.body = body;
	}

	public Vector2 getPosition() {
		return body.getPosition().scl(PPM);
	}
}
