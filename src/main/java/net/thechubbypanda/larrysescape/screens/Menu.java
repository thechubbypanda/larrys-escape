package net.thechubbypanda.larrysescape.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import net.thechubbypanda.larrysescape.Game;
import net.thechubbypanda.larrysescape.Globals;

import static net.thechubbypanda.larrysescape.Globals.assets;

public class Menu extends ScreenAdapter {

	private final Stage stage;

	public Menu() {
		stage = new Stage(new ExtendViewport(1000, 1000));

		Skin skin = assets.get("flatearthui/flat-earth-ui.json");

		Table window = new Table();
		window.setFillParent(true);
		stage.addActor(window);

		Label label = new Label(Globals.TITLE, skin, "title");
		window.add(label).colspan(2).padBottom(10.0f).expandX();

		window.row();
		Table rowTable = new Table();
		window.add(rowTable).growX().colspan(2);

		Table table = new Table();
		table.defaults().growX();
		rowTable.add(table).width(200.0f).right();

		TextButton textButton = new TextButton("Start", skin);
		textButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				((Game) Gdx.app.getApplicationListener()).setScreen(Game.Screens.play);
			}
		});
		table.add(textButton).padTop(10);

		table.row();
		textButton = new TextButton("Config", skin);
		table.add(textButton).padTop(10);

		table.row();
		textButton = new TextButton("Quit", skin);
		textButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			}
		});
		table.add(textButton).padTop(10);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(.1f, .12f, .16f, 1);
		Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);

		stage.act();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {
	}
}
