package net.thechubbypanda.larrysadventure.screens;

import box2dLight.RayHandler;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import net.thechubbypanda.larrysadventure.Collision;
import net.thechubbypanda.larrysadventure.LevelManager;
import net.thechubbypanda.larrysadventure.components.CameraComponent;
import net.thechubbypanda.larrysadventure.components.PhysicsComponent;
import net.thechubbypanda.larrysadventure.components.PlayerComponent;
import net.thechubbypanda.larrysadventure.entityListeners.WorldListener;
import net.thechubbypanda.larrysadventure.signals.InputSignal;
import net.thechubbypanda.larrysadventure.signals.ResizeSignal;
import net.thechubbypanda.larrysadventure.systems.*;

import static net.thechubbypanda.larrysadventure.Globals.AMBIENT_COLOR;
import static net.thechubbypanda.larrysadventure.Globals.PPM;

public class Play implements Screen, InputProcessor, ContactListener {

	public final Signal<ResizeSignal> resizeSignal = new Signal<>();
	public final Signal<InputSignal> inputSignal = new Signal<>();
	private final Signal<Collision> collisionSignal = new Signal<>();

	private final Engine engine;
	private final World world;
	private final LevelManager levelManager;
	private final RayHandler rayHandler;

	private final OrthographicCamera mainCamera;

	public Play() {
		engine = new Engine();

		// World
		world = new World(Vector2.Zero, true);
		world.setContactListener(this);

		// Lights
		RayHandler.useDiffuseLight(true);
		rayHandler = new RayHandler(world);
		rayHandler.setAmbientLight(AMBIENT_COLOR);

		levelManager = new LevelManager(engine, world, rayHandler, 0);

		// Cameras
		// Main viewport and camera
		CameraComponent mcc = new CameraComponent(new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), 1f);
		mcc.follow(engine.getEntitiesFor(Family.all(PlayerComponent.class).get()).get(0), 0, Gdx.graphics.getHeight() / 8f, 0);
		resizeSignal.add(mcc);
		engine.addEntity(new Entity().add(mcc));
		mainCamera = mcc.getCamera();

		// B2d viewport and camera
		CameraComponent b2dcc = new CameraComponent(new ExtendViewport(Gdx.graphics.getWidth() / PPM, Gdx.graphics.getHeight() / PPM), 1f / PPM);
		b2dcc.follow(engine.getEntitiesFor(Family.all(PlayerComponent.class).get()).get(0), 0, Gdx.graphics.getHeight() / 8f, 0);
		resizeSignal.add(b2dcc);
		engine.addEntity(new Entity().add(b2dcc));

		// Engine systems
		PlayerSystem ps = new PlayerSystem(world, rayHandler, mainCamera);
		inputSignal.add(ps);

		HealthSystem hs = new HealthSystem();
		collisionSignal.add(hs);

		BulletSystem bs = new BulletSystem();
		collisionSignal.add(bs);

		LevelExitSystem les = new LevelExitSystem(levelManager);
		collisionSignal.add(les);

		engine.addSystem(new MainMovementSystem());
		engine.addSystem(new AliveTimeSystem());
		engine.addSystem(new AnimationSystem());
		engine.addSystem(ps);
		engine.addSystem(new EnemySystem(world));
		engine.addSystem(hs);
		engine.addSystem(bs);
		engine.addSystem(les);

		engine.addSystem(new GLInitSystem());
		engine.addSystem(new CameraSystem());
		engine.addSystem(new MapRenderSystem(mainCamera));
		engine.addSystem(new MainRenderSystem(mainCamera));
		engine.addSystem(new PlayerRenderSystem(mainCamera));
		engine.addSystem(new LightRenderSystem(rayHandler, b2dcc.getCamera()));
		engine.addSystem(new DebugRenderSystem(world, b2dcc.getCamera()));

		// Entity listeners
		engine.addEntityListener(Family.all(PhysicsComponent.class).get(), new WorldListener(world));
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
		resizeSignal.dispatch(new ResizeSignal(width, height));
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void beginContact(Contact contact) {
		Object a = contact.getFixtureA().getBody().getUserData();
		Object b = contact.getFixtureB().getBody().getUserData();
		if (a instanceof Entity) {
			//collisions.add(new Collision((Entity)a, b));
			collisionSignal.dispatch(new Collision((Entity) a, b));
		}
		if (b instanceof Entity) {
			//collisions.add(new Collision((Entity)b, a));
			collisionSignal.dispatch(new Collision((Entity) b, a));
		}
	}

	@Override
	public void endContact(Contact contact) {

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
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
		InputSignal s = new InputSignal();
		s.type = InputSignal.Type.keyUp;
		s.keycode = keycode;
		inputSignal.dispatch(s);
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		InputSignal s = new InputSignal();
		s.type = InputSignal.Type.mouseDown;
		return touchSignal(screenX, screenY, button, s);
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		InputSignal s = new InputSignal();
		s.type = InputSignal.Type.mouseUp;
		return touchSignal(screenX, screenY, button, s);
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
		s.x = (int) v.x;
		s.y = (int) v.y;
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

	private boolean touchSignal(int screenX, int screenY, int button, InputSignal s) {
		Vector3 v = new Vector3(screenX, screenY, 0);
		v = mainCamera.unproject(v);
		s.x = (int) v.x;
		s.y = (int) v.y;
		s.button = button;
		inputSignal.dispatch(s);
		return true;
	}

	@Override
	public void dispose() {
		rayHandler.dispose();
		world.dispose();
	}
}
