package net.thechubbypanda.larrysadventure.screens;

import box2dLight.RayHandler;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import net.thechubbypanda.larrysadventure.*;
import net.thechubbypanda.larrysadventure.components.CameraComponent;
import net.thechubbypanda.larrysadventure.components.EnemyComponent;
import net.thechubbypanda.larrysadventure.components.LightComponent;
import net.thechubbypanda.larrysadventure.components.PhysicsComponent;
import net.thechubbypanda.larrysadventure.entityListeners.EnemyListener;
import net.thechubbypanda.larrysadventure.entityListeners.WorldListener;
import net.thechubbypanda.larrysadventure.signals.InputSignal;
import net.thechubbypanda.larrysadventure.signals.ResizeSignal;
import net.thechubbypanda.larrysadventure.systems.*;

import static net.thechubbypanda.larrysadventure.Globals.AMBIENT_COLOR;
import static net.thechubbypanda.larrysadventure.Globals.PPM;

public class Play implements Screen, InputProcessor, ContactListener {

	private final Signal<ResizeSignal> resizeSignal = new Signal<>();
	private final Signal<InputSignal> inputSignal = new Signal<>();
	private final Signal<CollisionSignal> collisionSignal = new Signal<>();

	private final Engine engine;
	private final World world;
	private final RayHandler rayHandler;
	private final EnemyListener enemyListener;

	public Play() {
		engine = new Engine();

		// World
		world = new World(Vector2.Zero, true);
		world.setContactListener(this);

		Globals.HUD = new HUD();

		// Lights
		RayHandler.useDiffuseLight(true);
		rayHandler = new RayHandler(world);
		rayHandler.setAmbientLight(AMBIENT_COLOR);

		LevelManager levelManager = new LevelManager(engine, world, 0);

		// Cameras
		// Main viewport and camera
		CameraComponent mcc = new CameraComponent(new ExtendViewport(Gdx.graphics.getWidth() / 1.5f, Gdx.graphics.getHeight() / 1.5f), 0, Gdx.graphics.getHeight() / 8f, 0, 1f);
		mcc.setMainCameraComponent();
		engine.addEntity(new Entity().add(mcc));

		// B2d viewport and camera
		CameraComponent b2dcc = new CameraComponent(new ExtendViewport(Gdx.graphics.getWidth() / PPM / 1.5f, Gdx.graphics.getHeight() / PPM / 1.5f), 0, Gdx.graphics.getHeight() / 8f, 0, 1f / PPM);
		engine.addEntity(new Entity().add(b2dcc));

		engine.addEntity(EntityFactory.player(world, rayHandler));

		// Engine systems
		HealthSystem hs = new HealthSystem();
		collisionSignal.add(hs);
		engine.addSystem(hs);

		BulletSystem bs = new BulletSystem();
		collisionSignal.add(bs);
		engine.addSystem(bs);

		DropSystem ds = new DropSystem();
		collisionSignal.add(ds);
		engine.addSystem(ds);

		LevelExitSystem les = new LevelExitSystem(levelManager);
		collisionSignal.add(les);
		engine.addSystem(les);

		CameraSystem cs = new CameraSystem();
		resizeSignal.add(cs);
		engine.addSystem(cs);

		engine.addSystem(new PhysicsSystem(world));
		engine.addSystem(new PlayerSystem(world, rayHandler, cs, collisionSignal, inputSignal));
		engine.addSystem(new EnemySystem(world));
		engine.addSystem(new MainMovementSystem());
		engine.addSystem(new AliveTimeSystem());
		engine.addSystem(new AnimationSystem());

		engine.addSystem(new RenderSystem(rayHandler, b2dcc.getCamera()));
		engine.addSystem(new DebugRenderSystem(world, b2dcc.getCamera()));

		// Entity listeners
		engine.addEntityListener(Family.one(PhysicsComponent.class, LightComponent.class).get(), new WorldListener(world));
		enemyListener = new EnemyListener(engine, world);
		engine.addEntityListener(Family.all(EnemyComponent.class, PhysicsComponent.class).get(), enemyListener);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render(float delta) {
		world.step(delta, 3, 6);
		engine.update(delta);
		enemyListener.render(delta);
		Globals.HUD.render();
	}

	@Override
	public void resize(int width, int height) {
		resizeSignal.dispatch(new ResizeSignal(width, height));
		Globals.HUD.resize(width, height);
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
			collisionSignal.dispatch(new CollisionSignal((Entity) a, b));
		}
		if (b instanceof Entity) {
			//collisions.add(new Collision((Entity)b, a));
			collisionSignal.dispatch(new CollisionSignal((Entity) b, a));
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
		if (getSignalPosition(screenX, screenY, s)) return false;
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
		if (getSignalPosition(screenX, screenY, s)) return false;
		s.button = button;
		inputSignal.dispatch(s);
		return true;
	}

	/**
	 * @param screenX Screen position of pointer
	 * @param screenY Screen position of pointer
	 * @param s       Signal to set world position of
	 * @return True on failure
	 */
	private boolean getSignalPosition(int screenX, int screenY, InputSignal s) {
		Vector3 v = new Vector3(screenX, screenY, 0);
		CameraComponent cc = CameraComponent.getMainCameraComponent();
		if (cc == null) {
			return true;
		}
		v = cc.getCamera().unproject(v);
		s.x = (int) v.x;
		s.y = (int) v.y;
		return false;
	}

	@Override
	public void dispose() {
		rayHandler.dispose();
		world.dispose();
		Globals.HUD.dispose();
	}
}
