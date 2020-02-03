package net.thechubbypanda.larrysadventure.signals;

import com.badlogic.ashley.signals.Signal;

public class Resize {

	public int width, height;

	public Resize(int width, int height) {
		this.width = width;
		this.height = height;
	}
}
