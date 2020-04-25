package net.thechubbypanda.larrysadventure.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

public class CameraComponent implements Component {

	private static CameraComponent mainCameraComponent = null;

	public final Viewport viewport;
	public final float scale;

	public final Vector2 posOffset;
	public final float rotationOffset;

	public final Vector2 shakeOffset = new Vector2();
	public float shakeMagnitude = 0f;
	public float shakeInitialDuration = 0f;
	public float shakeDuration = 0f;

	public CameraComponent(Viewport viewport, float offsetX, float offsetY, float rotationOffset, float scale) {
		this.viewport = viewport;
		this.posOffset = new Vector2(offsetX, offsetY);
		this.rotationOffset = rotationOffset;
		this.scale = scale;
	}

	public static CameraComponent getMainCameraComponent() {
		return mainCameraComponent;
	}

	public void setMainCameraComponent() {
		mainCameraComponent = this;
	}

	public OrthographicCamera getCamera() {
		return (OrthographicCamera) viewport.getCamera();
	}

	public void setPosition(Vector2 xy) {
		getCamera().position.set(xy.scl(scale), 0);
		viewport.apply();
	}

	public void setRotation(float r) {
		getCamera().up.set(0, 1, 0);
		getCamera().rotate(r);
		viewport.apply();
	}

	public Vector2 getPosOffset() {
		return posOffset;
	}

	public float getRotationOffset() {
		return rotationOffset;
	}
}
