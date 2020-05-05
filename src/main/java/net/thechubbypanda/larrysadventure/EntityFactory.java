package net.thechubbypanda.larrysadventure;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import net.thechubbypanda.larrysadventure.components.*;

import java.util.ArrayList;

import static net.thechubbypanda.larrysadventure.Globals.PPM;
import static net.thechubbypanda.larrysadventure.Globals.assets;

public final class EntityFactory {

	private static final BodyDef ENEMY_BDEF = new BodyDef();
	private static final FixtureDef ENEMY_FDEF = new FixtureDef();
	private static final Color BULLET_LIGHT_COLOR = new Color(1, 1, 0.8f, 1);
	private static final BodyDef BULLET_BDEF = new BodyDef();
	private static final FixtureDef BULLET_FDEF = new FixtureDef();

	static {
		BULLET_BDEF.type = BodyDef.BodyType.DynamicBody;
		BULLET_BDEF.bullet = true;
		BULLET_BDEF.fixedRotation = true;
		CircleShape shape = new CircleShape();
		shape.setRadius(3 / PPM);
		BULLET_FDEF.shape = shape;
		BULLET_FDEF.isSensor = true;
		BULLET_FDEF.filter.categoryBits = CollisionBit.bullet.bits;
		BULLET_FDEF.filter.maskBits = CollisionBit.other.bits;
	}

	static {
		ENEMY_BDEF.type = BodyDef.BodyType.DynamicBody;
		ENEMY_BDEF.fixedRotation = true;

		Shape shape = new CircleShape();
		shape.setRadius(16 / Globals.PPM);

		ENEMY_FDEF.shape = shape;
	}

	private static final BodyDef HEALTH_BDEF = new BodyDef();

	public static Entity player(World world, RayHandler rayHandler) {
		// Player
		Entity player = new Entity();

		player.add(new PlayerComponent());
		player.add(new SpriteComponent(new Texture("icon.png")));
		player.add(new HealthComponent(100));

		BodyDef bdef = new BodyDef();
		bdef.type = BodyDef.BodyType.DynamicBody;
		bdef.fixedRotation = true;

		Shape shape = new CircleShape();
		shape.setRadius(16 / Globals.PPM);

		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.filter.categoryBits = CollisionBit.player.bits;
		fdef.filter.maskBits = (short) (CollisionBit.other.bits | CollisionBit.levelExit.bits);

		Body body = world.createBody(bdef);
		body.createFixture(fdef);

		player.add(new PhysicsComponent(player, body));

		Filter filter = new Filter();
		filter.maskBits = CollisionBit.other.bits;

		ConeLight light = new ConeLight(rayHandler, 64, Color.WHITE, 400 / PPM, 0, 0, 0, 45);
		light.attachToBody(body, 0, 0, 90);
		light.setIgnoreAttachedBody(true);
		light.setContactFilter(filter);

		PointLight light2 = new PointLight(rayHandler, 16, Color.WHITE, 48 / PPM, 0, 0);
		light2.attachToBody(body);
		light.setIgnoreAttachedBody(true);
		light.setContactFilter(filter);

		player.add(new LightComponent().add(light).add(light2));

		return player;
	}

	public static Entity bullet(World world, RayHandler rayHandler, Vector2 position, Vector2 direction) {
		Entity bullet = new Entity();

		bullet.add(new BulletComponent());
		bullet.add(new DamageComponent(10));

		BULLET_BDEF.position.set(position.scl(1 / PPM));
		BULLET_BDEF.linearVelocity.set(direction.nor().scl(5));
		BULLET_BDEF.angle = direction.angleRad() - MathUtils.PI / 2f;

		Body body = world.createBody(BULLET_BDEF);

		body.createFixture(BULLET_FDEF);

		bullet.add(new PhysicsComponent(bullet, body));

		PointLight light = new PointLight(rayHandler, 64, BULLET_LIGHT_COLOR, 16 / PPM, 0, 0);
		light.attachToBody(body);
		light.setIgnoreAttachedBody(true);

		bullet.add(new LightComponent().add(light));

		bullet.add(new SpriteComponent(assets.get("bullet.png", Texture.class)));

		return bullet;
	}

	public static Entity levelExit(World world, Vector2 position) {
		Entity levelExit = new Entity();

		BodyDef bdef = new BodyDef();
		bdef.type = BodyDef.BodyType.StaticBody;
		bdef.position.set(position.scl(1 / PPM));
		bdef.fixedRotation = true;

		CircleShape shape = new CircleShape();
		shape.setRadius(0.5f / PPM);

		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.isSensor = true;
		fdef.filter.categoryBits = CollisionBit.levelExit.bits;
		fdef.filter.maskBits = CollisionBit.player.bits;

		Body body = world.createBody(bdef);
		body.createFixture(fdef);

		levelExit.add(new PhysicsComponent(levelExit, body));
		levelExit.add(new SpriteComponent(assets.get("levelExit.png", Texture.class)));
		levelExit.add(new LevelExitComponent());

		return levelExit;
	}

	private static final FixtureDef HEALTH_FDEF = new FixtureDef();

	static {
		HEALTH_BDEF.type = BodyDef.BodyType.StaticBody;
		HEALTH_BDEF.fixedRotation = true;
		CircleShape shape = new CircleShape();
		shape.setRadius(8 / PPM);
		HEALTH_FDEF.shape = shape;
		HEALTH_FDEF.isSensor = true;
		HEALTH_FDEF.filter.maskBits = CollisionBit.player.bits;
	}

	public static Entity enemy(World world, ArrayList<Vector2> patrolRoute, Drop drop) {
		Entity enemy = new Entity();

		ENEMY_BDEF.position.set(new Vector2(patrolRoute.get(0)));

		Body body = world.createBody(ENEMY_BDEF);

		body.createFixture(ENEMY_FDEF);

		enemy.add(new EnemyComponent(patrolRoute, drop));
		enemy.add(new PhysicsComponent(enemy, body));
		enemy.add(new SpriteComponent(new Texture("icon.png")));
		enemy.add(new HealthComponent(20));
		enemy.add(new DamageComponent(10));

		return enemy;
	}

	public static final Entity healthPack(World world, Vector2 position) {
		Entity health = new Entity();

		HEALTH_BDEF.position.set(new Vector2(position).scl(1 / PPM));

		Body b = world.createBody(HEALTH_BDEF);

		b.createFixture(HEALTH_FDEF);

		health.add(new PhysicsComponent(health, b));

		health.add(new HealthDropComponent());
		health.add(new SpriteComponent(assets.get("health.png", Texture.class)));

		return health;
	}

	private EntityFactory() {
	}
}
