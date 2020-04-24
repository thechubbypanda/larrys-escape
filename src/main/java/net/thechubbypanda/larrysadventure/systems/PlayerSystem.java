package net.thechubbypanda.larrysadventure.systems;

import box2dLight.RayHandler;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import net.thechubbypanda.larrysadventure.EntityFactory;
import net.thechubbypanda.larrysadventure.Globals;
import net.thechubbypanda.larrysadventure.components.LightComponent;
import net.thechubbypanda.larrysadventure.components.PhysicsComponent;
import net.thechubbypanda.larrysadventure.components.PlayerComponent;
import net.thechubbypanda.larrysadventure.components.SpriteComponent;
import net.thechubbypanda.larrysadventure.signals.InputSignal;

public class PlayerSystem extends IteratingSystem implements Listener<InputSignal> {

	private static final Family family = Family.all(PlayerComponent.class).get();
	private static float speed = 1;

	private final OrthographicCamera camera;

	private final ComponentMapper<PhysicsComponent> pcm = ComponentMapper.getFor(PhysicsComponent.class);
	private final ComponentMapper<SpriteComponent> scm = ComponentMapper.getFor(SpriteComponent.class);
	private final ComponentMapper<LightComponent> lcm = ComponentMapper.getFor(LightComponent.class);

	private final RayHandler rayHandler;
	private final World world;

	private final Vector2 vel = new Vector2();

	private float targetRotation = 0;
	private float lerpPercent = 0;

	public PlayerSystem(World world, RayHandler rayHandler, OrthographicCamera camera) {
		super(Family.all(PlayerComponent.class).get());
		this.camera = camera;
		this.world = world;
		this.rayHandler = rayHandler;
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		vel.setZero();

		if (Globals.DEBUG) {
			speed = 5;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			vel.y += 1;
			//playerAnimationComponent.play("up");
		} else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			vel.y -= 1;
			//playerAnimationComponent.play("down");
		}

		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			vel.x -= 1;
			//playerAnimationComponent.play("left");
		} else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			vel.x += 1;
			//playerAnimationComponent.play("right");
		}

//		if (vel.len2() <= 0) {
//			//playerAnimationComponent.setToInitialFrame();
//		}

		pcm.get(entity).setRotation(MathUtils.lerpAngle(pcm.get(entity).getRotation(), targetRotation, Math.min(1f, MathUtils.clamp(lerpPercent += deltaTime, 0, 1))));
		pcm.get(entity).setLinearVelocity(vel.rotateRad(pcm.get(entity).getRotation()).nor().scl(speed));

		scm.get(entity).sprite.setRotation(scm.get(entity).sprite.getRotation());
		scm.get(entity).setPosition(pcm.get(entity).getPosition());

		Vector3 mousePos = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
		float diffX = mousePos.x - pcm.get(entity).getPosition().x;
		float diffY = mousePos.y - pcm.get(entity).getPosition().y;
		float angle = (float) Math.atan2(diffY, diffX);
		lcm.get(entity).setBodyAngleOffset((angle - targetRotation) * MathUtils.radiansToDegrees);
	}

	@Override
	public void receive(Signal<InputSignal> signal, InputSignal o) {
		if (o.type == InputSignal.Type.keyDown) {
			if (o.keycode == Input.Keys.Q) {
				targetRotation += MathUtils.PI / 2f;
				lerpPercent = 0;
				while (targetRotation > MathUtils.PI2) {
					targetRotation -= MathUtils.PI2;
				}
			}
			if (o.keycode == Input.Keys.E) {
				targetRotation -= MathUtils.PI / 2f;
				lerpPercent = 0;
				while (targetRotation < -MathUtils.PI2) {
					targetRotation += MathUtils.PI2;
				}
			}
		}
		if (o.type == InputSignal.Type.mouseDown) {
			if (o.button == Input.Buttons.LEFT) {
				Vector2 currentPosition = pcm.get(getEngine().getEntitiesFor(family).get(0)).getPosition();
				getEngine().addEntity(EntityFactory.bullet(world, rayHandler, currentPosition, new Vector2(o.x, o.y).sub(currentPosition)));
			}
		}
	}
}
