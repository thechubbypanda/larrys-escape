package net.thechubbypanda.larrysadventure.components;

import com.badlogic.ashley.core.Component;

public class PlayerComponent implements Component {

	public static final long SHOOT_INTERVAL = 300;
	public static final int MAX_AMMO = 50;

	public int ammo = MAX_AMMO;
}
