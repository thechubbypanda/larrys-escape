package net.thechubbypanda.larrysadventure.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

public interface ScriptComponent extends Component {

	void update(Entity entity);
}
