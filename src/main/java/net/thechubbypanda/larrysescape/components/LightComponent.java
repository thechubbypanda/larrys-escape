package net.thechubbypanda.larrysescape.components;

import box2dLight.Light;
import box2dLight.PositionalLight;
import com.badlogic.ashley.core.Component;

import java.util.ArrayList;

public class LightComponent implements Component {

	private final ArrayList<PositionalLight> lights = new ArrayList<>();

	public LightComponent add(PositionalLight light) {
		lights.add(light);
		return this;
	}

	public void setBodyAngleOffset(float angle) {
		lights.forEach(l -> l.setBodyAngleOffset(angle));
	}

	public void dispose() {
		lights.forEach(Light::remove);
	}
}
