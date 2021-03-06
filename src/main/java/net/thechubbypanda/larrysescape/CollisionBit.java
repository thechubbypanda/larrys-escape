package net.thechubbypanda.larrysescape;

public enum CollisionBit {
	other(0b1),
	player(0b10),
	bullet(0b1000),
	levelExit(0b10000);

	public final short bits;

	CollisionBit(int bits) {
		this.bits = (short) bits;
	}
}
