package com.swapnil.leveleditor.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.swapnil.leveleditor.SwipeCross;
import com.swapnil.leveleditor.item.Destination;
import com.swapnil.leveleditor.item.Item;
import com.swapnil.leveleditor.item.Player;
import com.swapnil.leveleditor.item.Wall;
import com.swapnil.leveleditor.util.Boundary;

import java.io.IOException;

public class PlayScreen implements Screen {

    private Array<Item> itemList = new Array<>();
    private Stage stage;
    private World world;
    private SwipeCross game;

    public PlayScreen(String levelFile, SwipeCross game) {
        this.game = game;

        stage = new Stage();

        try {
            XmlReader.Element element = new XmlReader().parse(new FileHandle(levelFile));
            String itemName;
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

    @Override
    public void show() {

        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("UI/atlas.pack"));
        Skin skin = new Skin(Gdx.files.internal("Skins/MenuSkin.json"), atlas);

        world = new World(new Vector2(0f, 100f), true);

        new Boundary(world);

        TextButton editor = new TextButton("Editor", skin);
        editor.setPosition(Gdx.graphics.getWidth() - editor.getWidth(), Gdx.graphics.getHeight() - editor.getHeight());

        for(int i=0;i<itemList.size;i++) {
            itemList.get(i).createBody(world);
        }

        stage.addActor(editor);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        world.step(1f/60f, 6, 2);

        stage.act();
        stage.setDebugAll(true);
        stage.draw();
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
}
