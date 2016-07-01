package com.swapnil.leveleditor.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.swapnil.leveleditor.SwipeCross;
import com.swapnil.leveleditor.item.*;
import com.swapnil.leveleditor.listener.SaveListener;
import com.swapnil.leveleditor.tool.DeleteTool;
import com.swapnil.leveleditor.tool.ItemTool;
import com.swapnil.leveleditor.tool.SelectTool;
import com.swapnil.leveleditor.tool.Tool;

import java.io.IOException;

public class LevelEditor extends Game implements InputProcessor, Screen {

	private Stage stage;
	private String levelFile;
	private SwipeCross game;

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
	private FileHandle handle;


	public FileHandle getHandle() {
		return handle;
	}

	public LevelEditor(String levelFile,SwipeCross game) {
		this.levelFile = levelFile;
		this.game = game;

		handle = new FileHandle(levelFile);
		selectTool = new SelectTool(this);
		deleteTool = new DeleteTool(this);
		wallTool = new ItemTool(this, ItemType.WALL);
		playerTool = new ItemTool(this, ItemType.PLAYER);
		destinationTool = new ItemTool(this, ItemType.DESTINATION);

		activeTool = selectTool;

		init();

	}

	private void layoutSetup() {
		String itemName;
		try{
			XmlReader.Element element = new XmlReader().parse(handle);
			for(int i=0;i<element.getChildCount();i++) {
				itemName = element.getChild(i).getName();
				switch (itemName) {
					case "Player":
						addToStage(new Player().loadFromXml(element.getChild(i)));
						break;
					case "Destination":
						addToStage(new Destination().loadFromXml(element.getChild(i)));
						break;
					case "Wall":
						addToStage(new Wall().loadFromXml(element.getChild(i)));
						break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	public void setActiveTool(Tool tool) {
		activeTool = tool;
	}

	public Table getMenu() {
		return menu;
	}

	@Override
	public void create() {


	}

	private void init() {
		stage = new Stage();

		if(handle.exists()) {
			layoutSetup();
		}

//		setScreen(new PlayScreen("LevelLayout.xml"));

		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("UI/atlas.pack"));
		Skin skin = new Skin(Gdx.files.internal("Skins/MenuSkin.json"), atlas);

		TextButton select = new TextButton("Select", skin);
		TextButton delete = new TextButton("Delete", skin);
		TextButton player = new TextButton("Player", skin);
		TextButton wall = new TextButton("Wall", skin);
		TextButton destination = new TextButton("Destination", skin);
		TextButton save = new TextButton("Save", skin);

		menu = new Table(skin);
		menu.add(select).width(destination.getWidth()).height(destination.getHeight()).row();
		menu.add(delete).width(destination.getWidth()).height(destination.getHeight()).row();
		menu.add(player).width(destination.getWidth()).height(destination.getHeight()).row();
		menu.add(destination).width(destination.getWidth()).height(destination.getHeight()).row();
		menu.add(wall).width(destination.getWidth()).height(destination.getHeight()).row();
		menu.add(save).width(destination.getWidth()).height(destination.getHeight()).row();
		menu.pack();

		menu.setVisible(false);

		select.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Gdx.app.log("debug","selection tool");
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
		save.addListener(new SaveListener(this));

		TextButton play = new TextButton("Play", skin);
		play.setPosition(Gdx.graphics.getWidth() - play.getWidth(), Gdx.graphics.getHeight() - play.getHeight());
		play.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.setScreen(new PlayScreen(levelFile,game));
			}
		});


		stage.addActor(play);
		stage.addActor(menu);

		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(stage);
		inputMultiplexer.addProcessor(this);
		Gdx.input.setInputProcessor(inputMultiplexer);
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

			menu.setX((float) screenX);
			menu.setY(Gdx.graphics.getHeight() - screenY - menu.getHeight());
			menu.setVisible(true);

		} else if (button == Input.Buttons.LEFT) {

			menu.setVisible(false);
			activeTool.onPress(screenX, screenY);

		}

		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (selectedItem!=null) {
			selectedItem.setX(screenX);
			selectedItem.setY(screenY);
		}
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

	@Override
	public void show() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act();
		stage.setDebugAll(true);
		stage.draw();

		if (drawSelectionRectangle) {
			ShapeRenderer shapeRenderer = new ShapeRenderer();
			shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
			shapeRenderer.setColor(0f, 0f, 0f, 0.2f);
			shapeRenderer.rect(selectedItem.getX() - selectedItem.getActor().getWidth() / 2 - 5,
					Gdx.graphics.getHeight() - selectedItem.getY() - selectedItem.getActor().getHeight() / 2 - 5,
					selectedItem.getActor().getWidth() + 10, selectedItem.getActor().getHeight() + 10);
			shapeRenderer.end();

		}
	}
}
