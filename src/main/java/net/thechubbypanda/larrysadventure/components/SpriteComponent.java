package net.thechubbypanda.larrysadventure.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class SpriteComponent implements Component {

	public Sprite sprite;

	public SpriteComponent(Texture texture) {
		sprite = new Sprite(texture);
		sprite.setCenter(0, 0);
	}

	public SpriteComponent(TextureRegion textureRegion) {
		sprite = new Sprite(textureRegion);
		sprite.setCenter(0, 0);
	}

	public void setPosition(Vector2 position) {
		sprite.setCenter(position.x, position.y);
	}
}
