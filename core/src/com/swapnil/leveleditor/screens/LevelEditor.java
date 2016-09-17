package com.swapnil.leveleditor.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
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
import com.swapnil.leveleditor.util.CommonMenu;
import com.swapnil.leveleditor.util.Point;

import java.io.IOException;

public class LevelEditor implements InputProcessor, Screen {

	private Stage stage;

	private String levelFile;
	private SwipeCross game;
	private boolean drawSelectionRectangle = false;

	private Array<Item> itemList = new Array<>();

	public Item selectedItem = null;

	private Table menu, itemMenu;

	private SelectTool selectTool;

	private DeleteTool deleteTool;
	private ItemTool playerTool, wallTool, destinationTool, cameraTool, laserTool, guardTool;
	private Tool activeTool;
	private FileHandle handle;

	private CommonMenu commonMenu;

	public Item getSelectedItem() {
		return selectedItem;
	}

	public CommonMenu getCommonMenu() {
		return commonMenu;
	}

	public void setDrawSelectionRectangle(boolean drawSelectionRectangle) {
		this.drawSelectionRectangle = drawSelectionRectangle;
	}

	public Array<Item> getItemList() {
		return itemList;
	}

	public void setSelectedItem(Item selectedItem) {
		this.selectedItem = selectedItem;
		drawSelectionRectangle = true;

	}

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
		cameraTool = new ItemTool(this, ItemType.CAMERA);
		laserTool = new ItemTool(this, ItemType.LASER);
		guardTool = new ItemTool(this,ItemType.GUARD);

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
					case "Camera":
						addToStage(new Camera().loadFromXml(element.getChild(i)));
						break;
					case "Guard":
						addToStage(new Guard().loadFromXml(element.getChild(i)));
						break;
					case "Laser":
						addToStage(new Laser().loadFromXml(element.getChild(i)));
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

	private void init() {
		stage = new Stage();

		if(handle.exists()) {
			layoutSetup();
		}

		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("UI/atlas.pack"));
		Skin skin = new Skin(Gdx.files.internal("Skins/MenuSkin.json"), atlas);

		commonMenu = new CommonMenu(this);
		commonMenu.getMenu().setVisible(false);

		TextButton select = new TextButton("Select", skin);
		TextButton delete = new TextButton("Delete", skin);
		TextButton player = new TextButton("Player", skin);
		TextButton wall = new TextButton("Wall", skin);
		TextButton destination = new TextButton("Destination", skin);
		TextButton guard = new TextButton("Guard", skin);
		TextButton camera = new TextButton("Camera", skin);
		TextButton laser = new TextButton("Laser", skin);
		TextButton save = new TextButton("Save", skin);

		menu = new Table(skin);
		menu.add(select).width(destination.getWidth()).height(destination.getHeight()).row();
		menu.add(delete).width(destination.getWidth()).height(destination.getHeight()).row();
		menu.add(save).width(destination.getWidth()).height(destination.getHeight()).row();
		menu.pack();

		menu.setVisible(false);

		itemMenu = new Table(skin);
		itemMenu.add(player).width(Gdx.graphics.getWidth()/6);
		itemMenu.add(destination).width(Gdx.graphics.getWidth()/6);
		itemMenu.add(wall).width(Gdx.graphics.getWidth()/6);
		itemMenu.add(guard).width(Gdx.graphics.getWidth()/6);
		itemMenu.add(camera).width(Gdx.graphics.getWidth()/6);
		itemMenu.add(laser).width(Gdx.graphics.getWidth()/6);
		itemMenu.pack();

		itemMenu.setX(0);
		itemMenu.setY(0);

		itemMenu.setVisible(false);


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
				if(selectedItem!=null) {
					selectedItem = null;
					drawSelectionRectangle = false;
				}

				setActiveTool(deleteTool);
				menu.setVisible(false);
			}
		});
		save.addListener(new SaveListener(this));
		player.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(selectedItem!=null) {
					selectedItem = null;
					drawSelectionRectangle = false;
				}

				setActiveTool(playerTool);
				menu.setVisible(false);
				itemMenu.setVisible(false);
			}
		});
		wall.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(selectedItem!=null) {
					selectedItem = null;
					drawSelectionRectangle = false;
				}

				setActiveTool(wallTool);
				menu.setVisible(false);
				itemMenu.setVisible(false);
			}
		});
		destination.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(selectedItem!=null) {
					selectedItem = null;
					drawSelectionRectangle = false;
				}

				setActiveTool(destinationTool);
				menu.setVisible(false);
				itemMenu.setVisible(false);
			}
		});
		camera.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(selectedItem!=null) {
					selectedItem = null;
					drawSelectionRectangle = false;
				}

				setActiveTool(cameraTool);
				menu.setVisible(false);
				itemMenu.setVisible(false);
			}
		});
		laser.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(selectedItem!=null) {
					selectedItem = null;
					drawSelectionRectangle = false;
				}

				setActiveTool(laserTool);
				menu.setVisible(false);
				itemMenu.setVisible(false);
			}
		});
		guard.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(selectedItem!=null) {
					selectedItem = null;
					drawSelectionRectangle = false;
				}

				setActiveTool(guardTool);
				menu.setVisible(false);
				itemMenu.setVisible(false);
			}
		});

		TextButton play = new TextButton("Play", skin);
		play.setPosition(0, Gdx.graphics.getHeight() - play.getHeight());
		play.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.setScreen(new PlayScreen(levelFile,game));
			}
		});


		stage.addActor(play);
		stage.addActor(menu);
		stage.addActor(itemMenu);
		stage.addActor(commonMenu.getMenu());

		stage.getRoot().addCaptureListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				if (!(event.getTarget() instanceof TextField)) stage.setKeyboardFocus(null);
				return false;
			}});

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
		if(selectedItem!=null){
			switch (keycode) {
				case Input.Keys.UP:
					selectedItem.setY(selectedItem.getY()-1);
					commonMenu.setDefaults(new Point(selectedItem.getX(), selectedItem.getY()), selectedItem.getAngle());
					break;
				case Input.Keys.DOWN:
					selectedItem.setY(selectedItem.getY()+1);
					commonMenu.setDefaults(new Point(selectedItem.getX(), selectedItem.getY()), selectedItem.getAngle());
					break;
				case Input.Keys.RIGHT:
					selectedItem.setX(selectedItem.getX()+1);
					commonMenu.setDefaults(new Point(selectedItem.getX(), selectedItem.getY()), selectedItem.getAngle());
					break;
				case Input.Keys.LEFT:
					selectedItem.setX(selectedItem.getX()-1);
					commonMenu.setDefaults(new Point(selectedItem.getX(), selectedItem.getY()), selectedItem.getAngle());
					break;
				case Input.Keys.S:
					try {
						stage.addActor(selectedItem.getForm());
						selectedItem.getForm().setPosition(Gdx.graphics.getWidth() - selectedItem.getForm().getWidth(),
								Gdx.graphics.getHeight() - commonMenu.getMenu().getHeight() - selectedItem.getForm().getHeight());
						selectedItem.getForm().setVisible(!selectedItem.getForm().isVisible());
					}catch (NullPointerException e) {
						//DO NOTHING
					}
					break;
			}
		}
		else if(keycode == Input.Keys.SPACE || keycode == Input.Keys.MENU)
			itemMenu.setVisible(!itemMenu.isVisible());
		else if(keycode == Input.Keys.VOLUME_UP)
			scrolled(1);
		else if(keycode == Input.Keys.VOLUME_DOWN)
			scrolled(-1);
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
			commonMenu.setDefaults(new Point(selectedItem.getX(), selectedItem.getY()), selectedItem.getAngle());
		}
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {

		try {
			if(amount == 1)
                selectedItem.setAngle(selectedItem.getAngle()+1);
            else if(amount == -1)
                selectedItem.setAngle(selectedItem.getAngle()-1);
			commonMenu.setDefaults(new Point(selectedItem.getX(), selectedItem.getY()), selectedItem.getAngle());
		} catch (Exception e) {
			//DO NOTHING
		}

		return false;
	}

	@Override
	public void show() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {
		stage.dispose();
		game.dispose();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glEnable(GL20.GL_BLEND);

		for(int i= 0;i<itemList.size;i++)
			itemList.get(i).updateEditor();

		stage.act();
		stage.draw();

		if (drawSelectionRectangle) {
			ShapeRenderer shapeRenderer = new ShapeRenderer();
			shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
			shapeRenderer.setColor(new Color(0f, 0f, 0f, 0.2f));
			shapeRenderer.translate(selectedItem.getX(),Gdx.graphics.getHeight() - selectedItem.getY(), 0);
			shapeRenderer.rotate(0f, 0f, 1f, selectedItem.getAngle());
			shapeRenderer.rect(0f - selectedItem.getActor().getWidth()/2 - 5, 0f - selectedItem.getActor().getHeight()/2 - 5,
					selectedItem.getActor().getWidth() + 10, selectedItem.getActor().getHeight() + 10);
			shapeRenderer.end();
			shapeRenderer.dispose();

		}
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}
}
