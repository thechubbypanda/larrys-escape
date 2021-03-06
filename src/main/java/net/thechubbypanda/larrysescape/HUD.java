package net.thechubbypanda.larrysescape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import net.thechubbypanda.larrysescape.components.PlayerComponent;

import static net.thechubbypanda.larrysescape.Globals.assets;

public class HUD {

	private final Stage stage;
	private final ProgressBar health;
	private final Label enemies, ammo, level;

	public HUD() {
		Skin skin = assets.get("flatearthui/flat-earth-ui.json");

		stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera()));
		{
			Table table = new Table();
			table.top();
			table.setFillParent(true);
			{
				Table left = new Table();
				{
					Table innerLeft = new Table();
					{
						Label healthTag = new Label("Health: ", skin);
						innerLeft.add(healthTag);

						health = new ProgressBar(0, Globals.HEALTH, 1, false, skin);
						health.setWidth(Gdx.graphics.getWidth());
						innerLeft.add(health);
					}
					left.add(innerLeft);
				}
				left.row();
				{
					ammo = new Label("69", skin);
					left.add(ammo).padTop(10).align(Align.left);
				}
				table.add(left).expandX().align(Align.left).pad(10);
			}
			{
				Table right = new Table();
				{
					enemies = new Label("69", skin);
					right.add(enemies).align(Align.right);
				}
				right.row();
				{
					level = new Label("69", skin);
					right.add(level).align(Align.right).padTop(10);
				}
				table.add(right).expandX().align(Align.topRight).pad(10);
			}

			stage.addActor(table);
		}
	}

	public void setHealth(int health) {
		this.health.setValue(health);
	}

	public void setAmmo(int ammo) {
		this.ammo.setText("Ammo: " + ammo + "/" + PlayerComponent.MAX_AMMO);
	}

	public void render() {
		stage.act();
		stage.draw();
	}

	public void resize(int width, int height) {
		stage.getViewport().update(width, height);
	}

	public void dispose() {
		stage.dispose();
	}

	public void setEnemiesLeft(int size) {
		enemies.setText("Enemies left: " + size);
	}

	public void setLevel(int level) {
		this.level.setText("Level: " + level);
	}
}