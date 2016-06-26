package com.swapnil.leveleditor;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.swapnil.leveleditor.item.*;
import com.swapnil.leveleditor.tool.DeleteTool;
import com.swapnil.leveleditor.tool.ItemTool;
import com.swapnil.leveleditor.tool.SelectTool;
import com.swapnil.leveleditor.tool.Tool;

public class LevelEditor extends ApplicationAdapter implements InputProcessor {

	private Stage stage;

	public void setDrawSelectionRectangle(boolean drawSelectionRectangle) {
		this.drawSelectionRectangle = drawSelectionRectangle;
	}

	private boolean drawSelectionRectangle = false;
	public Array<Item> getItemList() {
		return itemList;
	}

	private Array<Item> itemList = new Array<>();
	private Tool activeTool;

	public void setSelectedItem(Item selectedItem) {
		this.selectedItem = selectedItem;
		this.selectedItem.getActor();
		drawSelectionRectangle = true;

	}

	public Item selectedItem = null;
	private Table menu;
	private SelectTool selectTool;
	private DeleteTool deleteTool;
	private ItemTool playerTool, wallTool, destinationTool;


	public LevelEditor() {

		selectTool = new SelectTool(this);
		deleteTool = new DeleteTool(this);
		wallTool = new ItemTool(this, ItemType.WALL);
		playerTool = new ItemTool(this, ItemType.PLAYER);
		destinationTool = new ItemTool(this, ItemType.DESTINATION);

		activeTool = selectTool;

	}

	public void setActiveTool(Tool tool) {
		activeTool = tool;
	}

	@Override
	public void create() {

		new LevelEditor();

		stage = new Stage();

		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("UI/atlas.pack"));
		Skin skin = new Skin(Gdx.files.internal("Skins/MenuSkin.json"), atlas);

		TextButton select = new TextButton("Select", skin);
		TextButton delete = new TextButton("Delete", skin);
		TextButton player = new TextButton("Player", skin);
		TextButton wall = new TextButton("Wall", skin);
		TextButton destination = new TextButton("Destination", skin);


		menu = new Table(skin);
		menu.add(select).width(destination.getWidth()).height(destination.getHeight()).row();
		menu.add(delete).width(destination.getWidth()).height(destination.getHeight()).row();
		menu.add(player).width(destination.getWidth()).height(destination.getHeight()).row();
		menu.add(destination).width(destination.getWidth()).height(destination.getHeight()).row();
		menu.add(wall).width(destination.getWidth()).height(destination.getHeight()).row();
		menu.pack();

		menu.setVisible(false);

		select.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {

				setActiveTool(selectTool);
				menu.setVisible(false);
			}
		});
		delete.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				setActiveTool(deleteTool);
				menu.setVisible(false);
			}
		});
		player.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				setActiveTool(playerTool);
				menu.setVisible(false);
			}
		});
		destination.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				setActiveTool(destinationTool);
				menu.setVisible(false);
			}
		});
		wall.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				setActiveTool(wallTool);
				menu.setVisible(false);
			}
		});

		stage.addActor(menu);

		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(stage);
		inputMultiplexer.addProcessor(this);
		Gdx.input.setInputProcessor(inputMultiplexer);

	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act();
		stage.setDebugAll(true);
		stage.draw();

		if(drawSelectionRectangle){
			ShapeRenderer shapeRenderer = new ShapeRenderer();
		 	shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
			shapeRenderer.setColor(0f, 0f, 0f, 0.2f);
			shapeRenderer.rect(selectedItem.getX() - selectedItem.getActor().getWidth()/2 - 5,
					Gdx.graphics.getHeight() - selectedItem.getY() - selectedItem.getActor().getHeight()/2 - 5,
					selectedItem.getActor().getWidth() + 10, selectedItem.getActor().getHeight() + 10);
			shapeRenderer.end();

		}

	}

	public void addToStage(Item item) {

		itemList.add(item);
		stage.addActor(item.getActor());
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

		if (button == Input.Buttons.RIGHT) {

//			Gdx.app.log("RIGHT CLICK", "Right mouse button pressed");
			menu.setX((float) screenX);
			menu.setY(Gdx.graphics.getHeight() - screenY - menu.getHeight());
			menu.setVisible(true);

		} else if (button == Input.Buttons.LEFT) {

//			Gdx.app.log("LEFT CLICK", "Left mouse button pressed");
			menu.setVisible(false);
			activeTool.onPress(screenX, screenY);

		}

		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
//		Gdx.app.log("RELEASE", "Mouse button released");
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
//		Gdx.app.log("MOUSE DRAG", "Mouse dragged");
		selectedItem.setX(screenX);
		selectedItem.setY(screenY);
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
