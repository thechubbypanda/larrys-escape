package net.thechubbypanda.larrysadventure;

import box2dLight.RayHandler;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import net.thechubbypanda.larrysadventure.components.AnimationComponent;
import net.thechubbypanda.larrysadventure.components.LightComponent;
import net.thechubbypanda.larrysadventure.components.PhysicsComponent;
import net.thechubbypanda.larrysadventure.components.SpriteComponent;
import net.thechubbypanda.larrysadventure.entityListeners.LightEntityListener;
import net.thechubbypanda.larrysadventure.entityListeners.PhysicsEntityListener;
import net.thechubbypanda.larrysadventure.systems.*;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static net.thechubbypanda.larrysadventure.Constants.*;
import static org.lwjgl.opengl.GL11.glClear;

public class Main extends ApplicationAdapter {

	private SpriteBatch batch;
	private AssetManager assets;

	private OrthographicCamera camera, b2dCamera, hudCamera;
	private Viewport viewport, b2dViewport, hudViewport;

	private Engine engine;
	private World world;
	private RayHandler rayHandler;

	//private Level currentLevel;
	private Entity player;

	private ComponentMapper<SpriteComponent> spriteMapper;
	private ComponentMapper<AnimationComponent> animationMapper;

	private Box2DDebugRenderer debugRenderer;

	@Override
	public void create() {
		batch = new SpriteBatch();

		camera = new OrthographicCamera();
		b2dCamera = new OrthographicCamera();
		hudCamera = new OrthographicCamera();

		viewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
		b2dViewport = new ExtendViewport(Gdx.graphics.getWidth() / PPM, Gdx.graphics.getHeight() / PPM, b2dCamera);
		hudViewport = new ScreenViewport(); // TODO

		debugRenderer = new Box2DDebugRenderer();

		// Engine and rendering related component mappers
		engine = new Engine();
		world = new World(Vector2.Zero, true);
		rayHandler = new RayHandler(world);

		spriteMapper = ComponentMapper.getFor(SpriteComponent.class);
		animationMapper = ComponentMapper.getFor(AnimationComponent.class);

		// Lights
		RayHandler.useDiffuseLight(true);

		assets = new AssetManager();
		// TODO: Load initial files
		assets.finishLoading();

		// Initialize player
		player = new Entity();
		BodyDef bdef = new BodyDef();
		bdef.type = BodyDef.BodyType.KinematicBody;
		bdef.fixedRotation = true;

		Shape shape = new CircleShape();
		shape.setRadius(20f / Constants.PPM);

		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;

		Body body = world.createBody(bdef);
		body.createFixture(fdef);

		player.add(new PhysicsComponent(body));

		//player.add(new LightComponent(rayHandler, 150f, body));
		engine.addEntity(player);

		// Move cameras to player
		camera.position.set(player.getComponent(PhysicsComponent.class).getPosition(), 0);
		b2dCamera.position.set(player.getComponent(PhysicsComponent.class).getPosition().scl(1f / PPM), 0);

		// Setup engine systems
		engine.addSystem(new MovementSystem());
		engine.addSystem(new AliveTimeSystem());
		engine.addSystem(new LightSystem());
		engine.addSystem(new AnimationSystem());
		InputSystem inputSystem = new InputSystem(b2dCamera, world, rayHandler, player);
		Gdx.input.setInputProcessor(inputSystem);
		engine.addSystem(inputSystem);

		// Setup entity listeners
		engine.addEntityListener(Family.all(PhysicsComponent.class).get(), new PhysicsEntityListener(world));
		engine.addEntityListener(Family.all(LightComponent.class).get(), new LightEntityListener());
	}

	@Override
	public void render() {
		world.step(Gdx.graphics.getDeltaTime(), 4, 8);

		engine.update(Gdx.graphics.getDeltaTime());

		Vector2 playerPosition = player.getComponent(PhysicsComponent.class).getPosition();
		camera.position.lerp(new Vector3(playerPosition, 0), 5f * Gdx.graphics.getDeltaTime());
		b2dCamera.position.lerp(new Vector3(playerPosition.scl(1f / PPM), 0), 5f * Gdx.graphics.getDeltaTime());

		viewport.apply();
		b2dViewport.apply();

		rayHandler.setCombinedMatrix(b2dCamera);
		rayHandler.update();

		glClear(GL_COLOR_BUFFER_BIT);
		//currentLevel.render();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		for (Entity e : engine.getEntitiesFor(Family.all(SpriteComponent.class).get())) {
			spriteMapper.get(e).sprite.draw(batch);
		}
		for (Entity e : engine.getEntitiesFor(Family.all(AnimationComponent.class).get())) {
			animationMapper.get(e).draw(batch);
		}
		batch.end();
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
		//rayHandler.dispose();
		batch.dispose();
		assets.dispose();
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
