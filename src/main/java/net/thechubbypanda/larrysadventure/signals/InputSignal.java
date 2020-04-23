package net.thechubbypanda.larrysadventure.signals;

public class InputSignal {

	public enum Type {
		keyDown, keyUp, mouseDragged, mouseDown, mouseUp, mouseMoved
	}

	public Type type;

	public int keycode = -1;

	public int x = -1, y = -1, button = -1;
}
