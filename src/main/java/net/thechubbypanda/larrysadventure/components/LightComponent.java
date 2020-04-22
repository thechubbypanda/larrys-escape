package net.thechubbypanda.larrysadventure.components;

import box2dLight.PositionalLight;
import com.badlogic.ashley.core.Component;

public class LightComponent implements Component {

	private final PositionalLight light;

	public LightComponent(PositionalLight light) {
		this.light = light;
	}

	public void setBodyAngleOffset(float angle) {
		light.setBodyAngleOffset(angle);
	}

	public void remove() {
		light.remove();
	}
}
