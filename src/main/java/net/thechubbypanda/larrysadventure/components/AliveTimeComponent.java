package net.thechubbypanda.larrysadventure.components;

import com.badlogic.ashley.core.Component;

public class AliveTimeComponent implements Component {

	public final float initialLifeTime;
	public float currentLifeLeft;

	/**
	 * @param time Time in seconds that the entity will live for
	 */
	public AliveTimeComponent(float time) {
		initialLifeTime = time;
		currentLifeLeft = initialLifeTime;
	}
}
