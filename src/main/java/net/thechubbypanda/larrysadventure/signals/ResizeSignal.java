package net.thechubbypanda.larrysadventure.signals;

import com.badlogic.ashley.signals.Signal;

public class ResizeSignal {

	public int width, height;

	public ResizeSignal(int width, int height) {
		this.width = width;
		this.height = height;
	}
}
