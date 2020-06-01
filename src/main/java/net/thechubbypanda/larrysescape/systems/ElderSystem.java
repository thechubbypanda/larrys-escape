package net.thechubbypanda.larrysescape.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import net.thechubbypanda.larrysescape.Globals;
import net.thechubbypanda.larrysescape.components.ElderComponent;
import net.thechubbypanda.larrysescape.signals.InputSignal;
import net.thechubbypanda.larrysescape.signals.ResizeSignal;

import static net.thechubbypanda.larrysescape.Globals.assets;

public class ElderSystem extends IteratingSystem {

	private final ComponentMapper<ElderComponent> ecm = ComponentMapper.getFor(ElderComponent.class);
	private final Stage stage;
	private final Label speech;

	public ElderSystem(Signal<ResizeSignal> resizeSignal, Signal<InputSignal> inputSignal) {
		super(Family.all(ElderComponent.class).get(), Globals.SystemPriority.UPDATE);
		stage = new Stage(new ExtendViewport(1000, 1000));

		Skin skin = assets.get("flatearthui/flat-earth-ui.json");

		Table window = new Table();
		window.setFillParent(true);
		stage.addActor(window);

		speech = new Label("", skin);
		window.add(speech).colspan(2).expandX();

		resizeSignal.add(new ResizeImpl());
		inputSignal.add(new InputImpl());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {

	}

	public void setText(String s) {
		speech.setText(s);
	}

	private class InputImpl implements Listener<InputSignal> {
		@Override
		public void receive(Signal<InputSignal> signal, InputSignal object) {
			if (object.type == InputSignal.Type.keyDown) {
				if (object.keycode == Input.Keys.SPACE) {
					getEntities().forEach(e -> speech.setText(ecm.get(e).get()));
				}
			}
		}
	}

	class ResizeImpl implements Listener<ResizeSignal> {
		@Override
		public void receive(Signal<ResizeSignal> signal, ResizeSignal object) {
			stage.getViewport().update(object.width, object.height, true);
		}
	}

	public void draw() {
		stage.act();
		stage.draw();
	}
}
