package net.thechubbypanda.larrysadventure;

import box2dLight.RayHandler;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import net.thechubbypanda.larrysadventure.components.LightComponent;
import net.thechubbypanda.larrysadventure.components.PhysicsComponent;
import net.thechubbypanda.larrysadventure.components.PlayerComponent;
import net.thechubbypanda.larrysadventure.entityListeners.LightEntityListener;
import net.thechubbypanda.larrysadventure.entityListeners.PhysicsEntityListener;
import net.thechubbypanda.larrysadventure.systems.*;

import static net.thechubbypanda.larrysadventure.Globals.*;

public class Main extends ApplicationAdapter {

	private SpriteBatch sb;
	private AssetManager assets;

	private OrthographicCamera camera, b2dCamera, hudCamera;
	private Viewport viewport, b2dViewport, hudViewport;

	private Engine engine;
	private World world;
	private RayHandler rayHandler;

	//private Level currentLevel;

	private Box2DDebugRenderer debugRenderer;

	@Override
	public void create() {
		sb = new SpriteBatch();

		// Cameras and Viewports
		camera = new OrthographicCamera();
		b2dCamera = new OrthographicCamera();
		hudCamera = new OrthographicCamera();

		viewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
		b2dViewport = new ExtendViewport(Gdx.graphics.getWidth() / PPM, Gdx.graphics.getHeight() / PPM, b2dCamera);
		hudViewport = new ScreenViewport(); // TODO

		// Other
		debugRenderer = new Box2DDebugRenderer();
		engine = new Engine();
		world = new World(Vector2.Zero, true);

		// Lights
		RayHandler.useDiffuseLight(true);
		rayHandler = new RayHandler(world);

		// Assets
		assets = new AssetManager();
		// TODO: Load initial files
		assets.finishLoading();

		// Engine systems
		engine.addSystem(new MovementSystem());
		engine.addSystem(new AliveTimeSystem());
		engine.addSystem(new LightSystem());
		engine.addSystem(new AnimationSystem());
		engine.addSystem(new PlayerControlSystem());
		engine.addSystem(new GLInitSystem());
		engine.addSystem(new CameraSystem(camera, b2dCamera));
		engine.addSystem(new RenderSystem(sb, camera));

		// Entity listeners
		engine.addEntityListener(Family.all(PhysicsComponent.class).get(), new PhysicsEntityListener(world));
		engine.addEntityListener(Family.all(LightComponent.class).get(), new LightEntityListener());

		initializePlayer();
	}

	private void initializePlayer() {
		Entity player = new Entity();
		BodyDef bdef = new BodyDef();
		bdef.type = BodyDef.BodyType.KinematicBody;
		bdef.fixedRotation = true;

		Shape shape = new CircleShape();
		shape.setRadius(20f / Globals.PPM);

		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;

		Body body = world.createBody(bdef);
		body.createFixture(fdef);

		player.add(new PhysicsComponent(body));
		player.add(new PlayerComponent());

		//player.add(new LightComponent(rayHandler, 150f, body));
		engine.addEntity(player);

		// Move cameras to player
		camera.position.set(player.getComponent(PhysicsComponent.class).getPosition(), 0);
		b2dCamera.position.set(player.getComponent(PhysicsComponent.class).getPosition().scl(1f / PPM), 0);
	}

	@Override
	public void render() {
		world.step(Gdx.graphics.getDeltaTime(), 4, 8);

		viewport.apply();
		b2dViewport.apply();

		rayHandler.setCombinedMatrix(b2dCamera);
		rayHandler.update();

		//currentLevel.render();
		engine.update(Gdx.graphics.getDeltaTime());
		//rayHandler.render();
		if (DEBUG) debugRenderer.render(world, b2dCamera.combined);
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
		b2dViewport.update(width, height);
		hudViewport.update(width, height);
	}

	@Override
	public void dispose() {
		world.dispose();
		rayHandler.dispose();
		assets.dispose();
		sb.dispose();
		//currentLevel.dispose();
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
