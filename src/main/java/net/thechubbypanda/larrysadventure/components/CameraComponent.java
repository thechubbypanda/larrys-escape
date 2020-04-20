package net.thechubbypanda.larrysadventure.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import net.thechubbypanda.larrysadventure.signals.ResizeSignal;

public class CameraComponent implements Component, Listener<ResizeSignal> {

	private Viewport vp;

	private boolean following = false;
	private PhysicsComponent follow;
	private Vector2 posOffset;
	private float rotationOffset;

	public final float scale;

	public CameraComponent(Viewport viewport, float scale) {
		this.vp = viewport;
		this.vp.setCamera(new OrthographicCamera());
		this.scale = scale;
	}

	public void follow(Entity follow, float offsetX, float offsetY, float rotationOffset) {
		following = true;
		this.follow = follow.getComponent(PhysicsComponent.class);
		posOffset = new Vector2(offsetX, offsetY);
		this.rotationOffset = rotationOffset;
	}

	@Override
	public void receive(Signal<ResizeSignal> signal, ResizeSignal resizeSignal) {
		vp.update(resizeSignal.width, resizeSignal.height);
	}

	public boolean isFollowing() {
		return following;
	}

	public Vector2 getFollowPosition() {
		return follow.getPosition();
	}

	public float getFollowRotation() {
		return follow.getRotation();
	}

	public Vector2 getPosOffset() {
		return posOffset;
	}

	public float getRotationOffset() {
		return rotationOffset;
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
}
