package net.thechubbypanda.larrysescape.components;

import com.badlogic.ashley.core.Component;

import static com.badlogic.gdx.math.MathUtils.clamp;

public class PlayerComponent implements Component {

	public static final long SHOOT_INTERVAL = 300;
	public static final long FAST_SHOOT_INTERVAL = 150;
	public static final int MAX_AMMO = 50;

	private int ammo = MAX_AMMO;

	public int getAmmo() {
		return ammo;
	}

	public void addAmmo(int delta) {
		ammo += delta;
		ammo = clamp(ammo, 0, MAX_AMMO);
	}
}
