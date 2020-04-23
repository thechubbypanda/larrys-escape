package net.thechubbypanda.larrysadventure.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import net.thechubbypanda.larrysadventure.components.BulletComponent;
import net.thechubbypanda.larrysadventure.components.HealthComponent;
import net.thechubbypanda.larrysadventure.components.LevelExitComponent;
import net.thechubbypanda.larrysadventure.components.PlayerComponent;

public class CollisionSystem extends EntitySystem implements ContactListener {

	private final ComponentMapper<HealthComponent> hcm = ComponentMapper.getFor(HealthComponent.class);
	private final ComponentMapper<BulletComponent> bcm = ComponentMapper.getFor(BulletComponent.class);
	private final ComponentMapper<LevelExitComponent> lecm = ComponentMapper.getFor(LevelExitComponent.class);

	private void dealWithContact(Entity a, Object b) {
		if (hcm.has(a)) {
			getEngine().getSystem(HealthSystem.class).hit(a, b);
		}
		if (bcm.has(a)) {
			Gdx.app.postRunnable(() -> getEngine().removeEntity(a));
		}
		if (lecm.has(a) && b instanceof Entity && ((Entity) b).getComponent(PlayerComponent.class) != null) {
			Gdx.app.postRunnable(() -> lecm.get(a).nextLevel());
		}
	}

	@Override
	public void beginContact(Contact contact) {
		Object a = contact.getFixtureA().getBody().getUserData();
		Object b = contact.getFixtureB().getBody().getUserData();
		if (a instanceof Entity) {
			dealWithContact((Entity) a, b);
		}
		if (b instanceof Entity) {
			dealWithContact((Entity) b, a);
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
		Object a = contact.getFixtureA().getBody().getUserData();
		Object b = contact.getFixtureB().getBody().getUserData();
		if (a instanceof Entity) {
			// Run functions of entity components etc
		} else if (b instanceof Entity) {
			// Run functions of entity components etc
		}
	}
}
