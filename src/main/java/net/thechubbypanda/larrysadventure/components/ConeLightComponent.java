package net.thechubbypanda.larrysadventure.components;

import box2dLight.ConeLight;
import box2dLight.RayHandler;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;

public class ConeLightComponent extends ConeLight implements Component {

	/**
	 * Creates light shaped as a circle's sector with given radius, direction and arc angle
	 *
	 * @param rayHandler      not {@code null} instance of RayHandler
	 * @param rays            number of rays - more rays make light to look more realistic
	 *                        but will decrease performance, can't be less than MIN_RAYS
	 * @param color           color, set to {@code null} to use the default color
	 * @param distance        distance of cone light, soft shadow length is set to distance * 0.1f
	 * @param x               axis position
	 * @param y               axis position
	 * @param directionDegree direction of cone light
	 * @param coneDegree      half-size of cone light, centered over direction
	 */
	public ConeLightComponent(RayHandler rayHandler, int rays, Color color, float distance, float x, float y, float directionDegree, float coneDegree) {
		super(rayHandler, rays, color, distance, x, y, directionDegree, coneDegree);
	}

	public void setBodyAngleOffset(float angle) {
		bodyAngleOffset = angle;
	}
}
