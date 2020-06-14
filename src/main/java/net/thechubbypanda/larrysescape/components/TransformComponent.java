package net.thechubbypanda.larrysescape.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class TransformComponent implements Component {

	private final Vector3 position3 = new Vector3();
	private final Vector2 position2 = new Vector2();
	private float rotation = 0f;

	public TransformComponent(float x, float y, float z) {
		position3.set(x, y, z);
	}

	public TransformComponent(Vector2 xy, float z) {
		position3.set(xy, z);
	}

	public TransformComponent(float z) {
		position3.set(Vector2.Zero, z);
	}

	/**
	 * Returns the same Vector2 every time
	 *
	 * @return Position
	 */
	public Vector2 getPosition() {
		return position2.set(position3.x, position3.y);
	}

	public void setPosition(Vector2 position) {
		position3.x = position.x;
		position3.y = position.y;
	}

	public float getZ() {
		return position3.z;
	}

	public void setZ(float z) {
		position3.z = z;
	}

	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}
}
