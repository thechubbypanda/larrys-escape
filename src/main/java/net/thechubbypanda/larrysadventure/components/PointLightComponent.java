package net.thechubbypanda.larrysadventure.components;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;

public class PointLightComponent extends PointLight implements Component {

	/**
	 * Creates light shaped as a circle with given radius
	 *
	 * @param rayHandler
	 *            not {@code null} instance of RayHandler
	 * @param rays
	 *            number of rays - more rays make light to look more realistic
	 *            but will decrease performance, can't be less than MIN_RAYS
	 * @param color
	 *            color, set to {@code null} to use the default color
	 * @param distance
	 *            distance of light, soft shadow length is set to distance * 0.1f
	 * @param x
	 *            horizontal position in world coordinates
	 * @param y
	 *            vertical position in world coordinates
	 */
	public PointLightComponent(RayHandler rayHandler, int rays, Color color, float distance, float x, float y) {
		super(rayHandler, rays, color, distance, x, y);
	}
}
