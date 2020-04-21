package net.thechubbypanda.larrysadventure.signals;

public class InputSignal {

	public enum Type {
		keyDown, keyUp, mouseDragged, mouseMoved
	}

	public Type type;

	public int keycode = -1;

	public int x = -1, y = -1, mouse = -1;
}
