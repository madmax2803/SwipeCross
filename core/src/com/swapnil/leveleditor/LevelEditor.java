package com.swapnil.leveleditor;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class LevelEditor extends ApplicationAdapter implements InputProcessor{

	Stage stage;
	ScrollPane menu;

	@Override
	public void create () {

		stage = new Stage();

		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("UI/atlas.pack"));
		Skin skin = new Skin(Gdx.files.internal("Skins/MenuSkin.json"), atlas);
		List list = new List(skin);
		list.setItems(new String[] {"Item 1", "Item 2", "Item 3" });

		menu = new ScrollPane(list, skin);

		menu.setVisible(false);
		stage.addActor(menu);

		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(stage);
		inputMultiplexer.addProcessor(this);
		Gdx.input.setInputProcessor(inputMultiplexer);

	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act();
		stage.setDebugAll(true);
		stage.draw();
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {

		if(button == Input.Buttons.RIGHT) {
			menu.setX((float) screenX);
			menu.setY(Gdx.graphics.getHeight() - screenY - menu.getHeight());
			menu.setVisible(true);
		}
		else if(button == Input.Buttons.LEFT) {
			Rectangle rectangle = new Rectangle(menu.getX(), menu.getY(), menu.getWidth(), menu.getHeight());
			if(!rectangle.contains(screenX, screenY));
				menu.setVisible(false);
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
