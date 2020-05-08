package net.thechubbypanda.larrysadventure.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class SpriteComponent extends Sprite implements Component {

	public SpriteComponent(Texture texture) {
		super(texture);
		setCenter(0, 0);
		setOrigin(getWidth() / 2, getHeight() / 2);
	}

	public SpriteComponent(TextureRegion textureRegion) {
		super(textureRegion);
		setCenter(0, 0);
	}

	public Vector2 getPosition() {
		return new Vector2(getX() + getWidth() / 2, getY() + getHeight() / 2);
	}

	public void setPosition(Vector2 position) {
		setCenter(position.x, position.y);
	}
}
