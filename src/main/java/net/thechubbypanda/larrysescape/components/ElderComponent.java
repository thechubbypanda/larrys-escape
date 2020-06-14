package net.thechubbypanda.larrysescape.components;

import com.badlogic.ashley.core.Component;

public class ElderComponent implements Component {

	private String[] lines;
	private int i = 0;
	private Runnable action;

	public ElderComponent(String[] lines, Runnable action) {
		this.lines = lines;
		this.action = action;
	}

	public void act() {
		if (action != null) {
			action.run();
			action = null;
		}
	}

	public String get() {
		if (i < lines.length) {
			String x = lines[i];
			i++;
			if (i == lines.length) {
				act();
			}
			return x;
		}
		return "";
	}
}
