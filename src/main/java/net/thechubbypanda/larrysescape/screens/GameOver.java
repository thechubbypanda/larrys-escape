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

import static net.thechubbypanda.larrysescape.Globals.assets;

public class GameOver extends ScreenAdapter {

	private final Stage stage;
	private final Label level;

	public GameOver() {
		stage = new Stage(new ExtendViewport(1000, 1000));

		Skin skin = assets.get("flatearthui/flat-earth-ui.json");

		Table window = new Table();
		window.setFillParent(true);
		stage.addActor(window);

		Label label = new Label("Game Over", skin, "title");
		window.add(label).colspan(2).padBottom(10.0f).expandX();

		window.row();
		level = new Label("You reached level: ", skin);
		window.add(level).colspan(2).padBottom(10f).expandX();

		window.row();
		Table rowTable = new Table();
		window.add(rowTable).growX().colspan(2);

		Table table = new Table();
		table.defaults().growX();
		rowTable.add(table).width(200.0f).right();

		TextButton textButton = new TextButton("Main Menu", skin);
		textButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				((Game) Gdx.app.getApplicationListener()).reset();
				((Game) Gdx.app.getApplicationListener()).fadeScreen(Game.Screens.menu);
			}
		});
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
		int level = ((Play) ((Game) Gdx.app.getApplicationListener()).getScreen(Game.Screens.play)).getLevel();
		this.level.setText("You reached level " + level);
	}

	@Override
	public void render(float delta) {
		Gdx.input.setInputProcessor(stage);
		Gdx.gl.glClearColor(20f / 255f, 26f / 255f, 20f / 255f, 1);
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
