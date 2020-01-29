package net.thechubbypanda.larrysadventure.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import net.thechubbypanda.larrysadventure.Utils;

import java.util.HashMap;

public class AnimationComponent implements Component {

	private HashMap<String, Animation<Texture>> animations;
	private Animation<Texture> currentAnimation;

	public float stateTime;

	private final Vector2 position = new Vector2();

	public AnimationComponent() {
		animations = new HashMap<>();
	}

	public void add(String name, Animation<Texture> animation) {
		animations.put(name, animation);
		if (currentAnimation == null) currentAnimation = animation;
	}

	public void add(AssetManager assets, String parentDirectory, String name, int numberOfFrames, float frameDuration, Animation.PlayMode playMode) {
		add(name, Utils.loadAnimation(assets, parentDirectory, name, numberOfFrames, frameDuration, playMode));
	}

	public void play(String name) {
		currentAnimation = animations.get(name);
	}

	public void setToInitialFrame() {
		stateTime = 0;
	}

	public void draw(Batch batch) {
		Texture texture = currentAnimation.getKeyFrame(stateTime);
		batch.draw(texture, position.x - texture.getWidth()/2f, position.y - texture.getHeight()/2f);
	}

	public void setPosition(Vector2 position) {
		this.position.set(position);
	}
}
