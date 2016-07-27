package com.swapnil.leveleditor.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.swapnil.leveleditor.SwipeCross;
import com.swapnil.leveleditor.item.*;
import com.swapnil.leveleditor.util.Boundary;

import java.io.IOException;

public class PlayScreen implements Screen, InputProcessor {

    private Array<Item> itemList = new Array<>();
    private Stage stage;
    private World world;
    private String levelFile;
    private SwipeCross game;
    private Player player;

    private final float PIXELS_TO_METRES = 100f;

    private Matrix4 matrix4;
    private Box2DDebugRenderer debugRenderer;

    public PlayScreen(String levelFile, SwipeCross game) {

        this.levelFile = levelFile;
        this.game = game;

        stage = new Stage();

        try {
            XmlReader.Element element = new XmlReader().parse(new FileHandle(levelFile));
            String itemName;
            for(int i=0;i<element.getChildCount();i++) {
                itemName = element.getChild(i).getName();
                switch (itemName) {
                    case "Player":
                        player = new Player().loadFromXml(element.getChild(i));
                        addToStage(player);
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

    @Override
    public void show() {
        init();
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    private void init() {
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("UI/atlas.pack"));
        Skin skin = new Skin(Gdx.files.internal("Skins/MenuSkin.json"), atlas);

        world = new World(new Vector2(0f, 0f), true);

        new Boundary(PIXELS_TO_METRES,world);

        TextButton editor = new TextButton("Editor", skin);
        editor.setPosition(0, Gdx.graphics.getHeight() - editor.getHeight());
        editor.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new LevelEditor(levelFile, game));
            }
        });

        for(int i=0;i<itemList.size;i++) {
            itemList.get(i).createBody(world);
        }

        matrix4 = stage.getBatch().getProjectionMatrix().cpy().scale(PIXELS_TO_METRES, PIXELS_TO_METRES, 0);
        debugRenderer = new Box2DDebugRenderer();

        stage.addActor(editor);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        world.step(1f/60f, 6, 2);

        for(int i= 0;i<itemList.size;i++)
            itemList.get(i).updatePlay();

        stage.act();
        stage.draw();

        debugRenderer.render(world, matrix4);
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

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    private void addToStage(Item item) {
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
        player.setOldPoint(screenX, screenY);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        player.setNewPoint(screenX, screenY);

        player.getBody().applyForceToCenter(-player.getNewPoint().distanceX(player.getOldPoint().getX()),
                player.getNewPoint().distanceY(player.getOldPoint().getY()), true);
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
