package net.thechubbypanda.larrysadventure;

import box2dLight.RayHandler;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import net.thechubbypanda.larrysadventure.components.*;
import net.thechubbypanda.larrysadventure.entityListeners.LightEntityListener;
import net.thechubbypanda.larrysadventure.entityListeners.PhysicsEntityListener;
import net.thechubbypanda.larrysadventure.signals.Resize;
import net.thechubbypanda.larrysadventure.systems.*;

import static net.thechubbypanda.larrysadventure.Globals.PPM;
import static net.thechubbypanda.larrysadventure.Globals.TITLE;

public class Main extends ApplicationAdapter {

	private AssetManager assets;

	private Engine engine;
	private Signal<Resize> resizeSignal;

	private World world;

	@Override
	public void create() {

		// Other
		engine = new Engine();
		world = new World(Vector2.Zero, true);

		// Lights
		RayHandler.useDiffuseLight(true);

		// Assets
		assets = new AssetManager();
		// TODO: Load initial files
		assets.finishLoading();

		// Signals
		resizeSignal = new Signal<>();

		// Player
		Entity player = new Entity();
		BodyDef bdef = new BodyDef();
		bdef.type = BodyDef.BodyType.KinematicBody;
		bdef.fixedRotation = true;

		Shape shape = new CircleShape();
		shape.setRadius(16 / Globals.PPM);

		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;

		Body body = world.createBody(bdef);
		body.createFixture(fdef);

		player.add(new PlayerComponent());
		player.add(new PhysicsComponent(body));
		player.add(new SpriteComponent(new Texture("icon.png")));
		//player.add(new LightComponent(rayHandler, 150f, body));

		engine.addEntity(player);

		// Cameras
		// Main viewport and camera
		CameraComponent mcc = new CameraComponent(new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), 1f);
		mcc.follow(player, 0, Gdx.graphics.getHeight() / 8f, 0);
		resizeSignal.add(mcc);
		engine.addEntity(new Entity().add(mcc));

		// B2d viewport and camera
		CameraComponent b2dcc = new CameraComponent(new ExtendViewport(Gdx.graphics.getWidth() / PPM, Gdx.graphics.getHeight() / PPM), 1f / PPM);
		resizeSignal.add(b2dcc);
		engine.addEntity(new Entity().add(b2dcc));

		// Engine systems
		engine.addSystem(new MovementSystem());
		engine.addSystem(new AliveTimeSystem());
		engine.addSystem(new LightSystem());
		engine.addSystem(new AnimationSystem());
		engine.addSystem(new PlayerControlSystem());
		engine.addSystem(new GLInitSystem());
		engine.addSystem(new CameraSystem());
		engine.addSystem(new MainRenderSystem(mcc.getCamera()));
		engine.addSystem(new DebugRenderSystem(world, b2dcc.getCamera()));

		// Entity listeners
		engine.addEntityListener(Family.all(PhysicsComponent.class).get(), new PhysicsEntityListener(world));
		engine.addEntityListener(Family.all(LightComponent.class).get(), new LightEntityListener());

		Entity a = new Entity();
		a.add(new SpriteComponent(new Texture("explosionSprite.png")));
		engine.addEntity(a);
	}

	@Override
	public void render() {
		world.step(Gdx.graphics.getDeltaTime(), 3, 6);
		engine.update(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void resize(int width, int height) {
		resizeSignal.dispatch(new Resize(width, height));
	}

	@Override
	public void dispose() {
		world.dispose();
		assets.dispose();
	}

	public static void main(String[] args) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setMaximized(true);
		config.setMaximizedMonitor(Lwjgl3ApplicationConfiguration.getPrimaryMonitor());
		config.setTitle(TITLE);
		config.setWindowPosition(-1, -1);
		config.setWindowIcon("icon.png");
		new Lwjgl3Application(new Main(), config);
	}
}
