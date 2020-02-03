package net.thechubbypanda.larrysadventure.systems;

import com.badlogic.ashley.core.EntitySystem;
import net.thechubbypanda.larrysadventure.Globals;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

public class GLInitSystem extends EntitySystem {

	public GLInitSystem() {
		super(Globals.SystemPriority.GL_INIT);
	}

	@Override
	public void update(float deltaTime) {
		glClear(GL_COLOR_BUFFER_BIT);
	}
}
