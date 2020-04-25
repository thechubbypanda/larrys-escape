package net.thechubbypanda.larrysadventure.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

public class CameraComponent implements Component {

	private static CameraComponent mainCameraComponent = null;

	private final Viewport vp;
	private final float scale;

	private final Vector2 posOffset;
	private final float rotationOffset;

	public CameraComponent(Viewport viewport, float offsetX, float offsetY, float rotationOffset, float scale) {
		this.vp = viewport;
		this.vp.setCamera(new OrthographicCamera());
		posOffset = new Vector2(offsetX, offsetY);
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
		return (OrthographicCamera) vp.getCamera();
	}

	public void setPosition(Vector2 xy) {
		getCamera().position.set(xy.scl(scale), 0);
		vp.apply();
	}

	public void setRotation(float r) {
		getCamera().up.set(0, 1, 0);
		getCamera().rotate(r);
		vp.apply();
	}

	public Viewport getViewport() {
		return vp;
	}

	public Vector2 getPosOffset() {
		return posOffset;
	}

	public float getRotationOffset() {
		return rotationOffset;
	}
}
