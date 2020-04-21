package net.thechubbypanda.larrysadventure.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import net.thechubbypanda.larrysadventure.components.HealthComponent;

public class CollisionSystem extends EntitySystem implements ContactListener {

	private final ComponentMapper<HealthComponent> hcm = ComponentMapper.getFor(HealthComponent.class);

	@Override
	public void beginContact(Contact contact) {
		Object a = contact.getFixtureA().getBody().getUserData();
		Object b = contact.getFixtureB().getBody().getUserData();
		if (a instanceof Entity) {
			contact((Entity) a, b);
		} else if (b instanceof Entity) {
			contact((Entity) b, a);
		}
	}

	private void contact(Entity a, Object b) {
		if (hcm.has(a)) {
			hcm.get(a).hit(b);
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
