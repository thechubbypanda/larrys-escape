package net.thechubbypanda.larrysadventure;

import box2dLight.ConeLight;
import box2dLight.RayHandler;
import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import net.thechubbypanda.larrysadventure.components.*;

import static net.thechubbypanda.larrysadventure.Globals.PPM;

public final class EntityFactory {

	public static Entity player(World world, RayHandler rayHandler) {
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
		body.createFixture(fdef);

		player.add(new PlayerComponent());
		player.add(new PhysicsComponent(player, body));
		player.add(new SpriteComponent(new Texture("icon.png")));
		player.add(new HealthComponent(100));

		ConeLight light = new ConeLightComponent(rayHandler, 64, Color.WHITE, 400 / PPM, 0, 0, 0, 45);
		light.attachToBody(body, 0, 0, 90);
		light.setIgnoreAttachedBody(true);
		player.add((Component) light);

		return player;
	}

	public static Entity enemy(World world, Vector2 position) {
		Entity enemy = new Entity();

		BodyDef bdef = new BodyDef();
		bdef.type = BodyDef.BodyType.DynamicBody;
		bdef.fixedRotation = true;

		Shape shape = new CircleShape();
		shape.setRadius(16 / Globals.PPM);

		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;

		bdef.position.set(position.scl(1/PPM));

		Body body = world.createBody(bdef);

		body.createFixture(fdef);

		enemy.add(new EnemyComponent());
		enemy.add(new PhysicsComponent(enemy, body));
		enemy.add(new SpriteComponent(new Texture("icon.png")));
		enemy.add(new HealthComponent(20));
		enemy.add(new DamageComponent(10));

		return enemy;
	}

	private static final Color BULLET_LIGHT_COLOR = new Color(1, 1, 0.9f, 1);
	private static final BodyDef BULLET_BDEF = new BodyDef();
	private static final CircleShape BULLET_SHAPE = new CircleShape();
	private static final FixtureDef BULLET_FDEF = new FixtureDef();

	static {
		BULLET_BDEF.type = BodyDef.BodyType.DynamicBody;
		BULLET_BDEF.bullet = true;
		BULLET_BDEF.fixedRotation = true;
		BULLET_SHAPE.setRadius(2/PPM);
	}

	public static Entity bullet(World world, RayHandler rayHandler, Vector2 position, Vector2 direction) {
		Entity bullet = new Entity();

		BULLET_BDEF.position.set(position.scl(1/PPM));
		BULLET_BDEF.linearVelocity.set(direction.nor().scl(2));

		Body body = world.createBody(BULLET_BDEF);

		body.createFixture(BULLET_FDEF);

		PointLightComponent light = new PointLightComponent(rayHandler, 64, BULLET_LIGHT_COLOR, 16 / PPM, 0, 0);
		light.attachToBody(body);
		light.setIgnoreAttachedBody(true);

		bullet.add(new DamageComponent(10));
		bullet.add(new PhysicsComponent(bullet, body));
		bullet.add(light);

		return bullet;
	}

	private EntityFactory() {
	}
}
