package net.thechubbypanda.larrysadventure.components;

import com.badlogic.ashley.core.Component;
import net.thechubbypanda.larrysadventure.LevelManager;

public class LevelExitComponent implements Component {

	private final LevelManager lm;

	public LevelExitComponent(LevelManager lm) {
		this.lm = lm;
	}

	public void nextLevel() {
		lm.bumpLevel();
	}
}
