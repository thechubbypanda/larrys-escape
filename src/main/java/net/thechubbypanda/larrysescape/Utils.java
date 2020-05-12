package net.thechubbypanda.larrysescape;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;

public class Utils {

	/**
	 * Produces
	 * @param assets The {@link AssetManager asset manager} to load with
	 * @param parentDirectory The directory containing all the frame files
	 * @param name The name of the animation e.g. for "player0.png", name would be "player"
	 * @param numberOfFrames Number of frames in the animation
	 * @param frameDuration Time each frame is displayed
	 * @param playMode See {@link Animation.PlayMode PlayMode}
	 * @return The requested {@link Animation Animation}
	 */
	public static Animation<Texture> loadAnimation(AssetManager assets, String parentDirectory, String name, int numberOfFrames, float frameDuration, Animation.PlayMode playMode) {
		Array<Texture> frames = new Array<>();
		for (int i = 0; i < numberOfFrames; i++) {
			String path = parentDirectory + "/" + name + "/" + i + ".png";
			assets.load(path, Texture.class);
			assets.finishLoadingAsset(path);
			frames.add(assets.get(path));
		}
		return new Animation<>(frameDuration, frames, playMode);
	}
}
