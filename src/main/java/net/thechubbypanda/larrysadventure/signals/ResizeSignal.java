package net.thechubbypanda.larrysadventure.signals;

public class ResizeSignal {

	public final int width, height;

	public ResizeSignal(int width, int height) {
		this.width = width;
		this.height = height;
	}
}
