package net.thechubbypanda.larrysadventure.components;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Body;
import net.thechubbypanda.larrysadventure.Constants;

public class LightComponent implements Component {

	public PointLight light;
	public float initialDistance;

	public LightComponent(RayHandler rayHandler, float distance, Body body) {
		initialDistance = distance / Constants.PPM;
		light = new PointLight(rayHandler, (int) distance / 2, Color.WHITE, initialDistance, 0, 0);
		light.attachToBody(body);
		light.setIgnoreAttachedBody(true);
		// TODO: use filtering
		light.setXray(true);
	}

	public LightComponent(RayHandler rayHandler, float distance, float x, float y) {
		initialDistance = distance / Constants.PPM;
		light = new PointLight(rayHandler, (int) distance / 2, Color.WHITE, initialDistance, x / Constants.PPM, y / Constants.PPM);
		light.setStaticLight(true);
		// TODO: use filtering
		light.setXray(true);
	}
}
