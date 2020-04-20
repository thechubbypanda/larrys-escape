package net.thechubbypanda.larrysadventure.screens;

import box2dLight.ConeLight;
import box2dLight.RayHandler;
import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import net.thechubbypanda.larrysadventure.Globals;
import net.thechubbypanda.larrysadventure.components.*;
import net.thechubbypanda.larrysadventure.entityListeners.LightEntityListener;
import net.thechubbypanda.larrysadventure.entityListeners.PhysicsEntityListener;
import net.thechubbypanda.larrysadventure.map.CellMap;
import net.thechubbypanda.larrysadventure.signals.InputSignal;
import net.thechubbypanda.larrysadventure.systems.*;

import static net.thechubbypanda.larrysadventure.Globals.*;

public class Play extends ScreenAdapter implements InputProcessor {

	private final Engine engine;

	private final World world;
	private final RayHandler rayHandler;

	private final OrthographicCamera mainCamera;//, b2dCamera;

	public Play() {
		engine = new Engine();
		world = new World(Vector2.Zero, true);

		// Lights
		RayHandler.useDiffuseLight(true);
		rayHandler = new RayHandler(world);
		rayHandler.setAmbientLight(AMBIENT_COLOR);

		// Load initial files
		assets.load(Globals.Textures.GRASS, Texture.class);
		assets.load(Globals.Textures.WALL_CORNER, Texture.class);
		assets.load(Globals.Textures.WALL_VERT, Texture.class);
		assets.load(Globals.Textures.WALL_HORIZ, Texture.class);
		assets.finishLoading();

		// Player
		Entity player = new Entity();
		BodyDef bdef = new BodyDef();
		bdef.type = BodyDef.BodyType.DynamicBody;
		bdef.fixedRotation = true;

		Shape shape = new CircleShape();
		shape.setRadius(16 / Globals.PPM);

		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;

		Body body = world.createBody(bdef);
		Fixture x = body.createFixture(fdef);
		x.setUserData(player);

		player.add(new PlayerComponent());
		player.add(new PhysicsComponent(body));
		player.add(new SpriteComponent(new Texture("icon.png")));

		ConeLight light = new ConeLightComponent(rayHandler, 64, Color.WHITE, 400 / PPM, 0, 0, 0, 45);
		light.attachToBody(body, 0,0, 90);
		light.setIgnoreAttachedBody(true);
		player.add((Component) light);

		engine.addEntity(player);

		// Cameras
		// Main viewport and camera
		CameraComponent mcc = new CameraComponent(new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), 1f);
		mcc.follow(player, 0, Gdx.graphics.getHeight() / 8f, 0);
		resizeSignal.add(mcc);
		engine.addEntity(new Entity().add(mcc));
		mainCamera = mcc.getCamera();

		// B2d viewport and camera
		CameraComponent b2dcc = new CameraComponent(new ExtendViewport(Gdx.graphics.getWidth() / PPM, Gdx.graphics.getHeight() / PPM), 1f / PPM);
		b2dcc.follow(player, 0, Gdx.graphics.getHeight() / 8f, 0);
		resizeSignal.add(b2dcc);
		engine.addEntity(new Entity().add(b2dcc));

		// Map
		CellMap cellMap = new CellMap(5);
		engine.addEntity(new Entity().add(new TileMapComponent(world, cellMap)));

		// Enemies
		Entity enemy = new Entity();

		bdef.position.set(128f / PPM, 128f / PPM);

		body = world.createBody(bdef);

		body.createFixture(fdef);

		enemy.add(new PhysicsComponent(body));
		enemy.add(new EnemyComponent());
		enemy.add(new SpriteComponent(new Texture("icon.png")));
		engine.addEntity(enemy);

		// Engine systems
		engine.addSystem(new MainMovementSystem());
		engine.addSystem(new AliveTimeSystem());
		engine.addSystem(new AnimationSystem());
		engine.addSystem(new PlayerSystem(player, mainCamera));
		engine.addSystem(new EnemySystem(world, player));
		engine.addSystem(new GLInitSystem());
		engine.addSystem(new CameraSystem());
		engine.addSystem(new MapRenderSystem(mainCamera));
		engine.addSystem(new MainRenderSystem(mainCamera));
		engine.addSystem(new PlayerRenderSystem(mainCamera, player));
		engine.addSystem(new LightRenderSystem(world,rayHandler, b2dcc.getCamera()));
		engine.addSystem(new DebugRenderSystem(world, b2dcc.getCamera()));

		// Entity listeners
		engine.addEntityListener(Family.all(PhysicsComponent.class).get(), new PhysicsEntityListener(world));
		engine.addEntityListener(Family.all(ConeLightComponent.class).get(), new LightEntityListener());
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render(float delta) {
		world.step(Gdx.graphics.getDeltaTime(), 3, 6);
		engine.update(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {
		rayHandler.dispose();
		world.dispose();
		assets.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		InputSignal s = new InputSignal();
		s.type = InputSignal.Type.keyDown;
		s.keycode = keycode;
		inputSignal.dispatch(s);
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		InputSignal s = new InputSignal();
		if (pointer == -1) {
			s.type = InputSignal.Type.mouseMoved;
		} else {
			s.type = InputSignal.Type.mouseDragged;
		}
		Vector3 v = new Vector3(screenX, screenY, 0);
		v = mainCamera.unproject(v);
		s.x = (int)v.x;
		s.y = (int)v.y;
		s.mouse = pointer;
		inputSignal.dispatch(s);
		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return touchDragged(screenX, screenY, -1);
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}