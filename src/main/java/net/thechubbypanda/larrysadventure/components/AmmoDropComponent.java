package net.thechubbypanda.larrysadventure.components;

import com.badlogic.ashley.core.Component;

import java.util.Random;

public class AmmoDropComponent implements Component {

	private static final Random random = new Random();

	public final int ammo = random.nextInt(20) + 10; // Between 10 and 30 ammo
}
