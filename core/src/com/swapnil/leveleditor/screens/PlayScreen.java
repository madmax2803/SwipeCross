package com.swapnil.leveleditor.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.sun.org.apache.bcel.internal.generic.FLOAD;
import com.swapnil.leveleditor.GameData;
import com.swapnil.leveleditor.SwipeCross;
import com.swapnil.leveleditor.item.*;
import com.swapnil.leveleditor.listener.CollisionListener;
import com.swapnil.leveleditor.util.Boundary;

import java.io.IOException;

public class PlayScreen implements Screen, InputProcessor {

    private GameData gameData;

    private Array<Item> itemList = new Array<>();
    private Stage stage;
    private World world;
    private Player player;
    private TextField textField;
    private float angle;

    private Vector2 p1 = new Vector2(), p2 = new Vector2(), collision = new Vector2(), normal = new Vector2(), temp = new Vector2(), deflection = new Vector2();
    private ShapeRenderer trajectoryRay = new ShapeRenderer();
    private boolean showTrajectoryRay = false;

    private final float PIXELS_TO_METRES = 100f;

    private RayCastCallback callback;

    private Matrix4 matrix4;
    private Box2DDebugRenderer debugRenderer;
    private Vector2 temp1 = new Vector2();
    private Vector2 temp2 = new Vector2();

    public PlayScreen(GameData gameData) {

        this.gameData = gameData;
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("UI/atlas.pack"));
        Skin skin = new Skin(Gdx.files.internal("Skins/MenuSkin.json"), atlas);

        textField = new TextField("", skin);
        textField.setPosition(0, 0);
        stage = new Stage();


        try {
            XmlReader.Element element = new XmlReader().parse(new FileHandle(gameData.getLevelFile()));
            String itemName;
            for(int i=0;i<element.getChildCount();i++) {
                itemName = element.getChild(i).getName();
                switch (itemName) {
                    case "Player":
                        player = new Player().loadFromXml(element.getChild(i));
                        player.setGameData(gameData);
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

    private void addBoundaryWalls() {
        Wall lower, left, right, upper;

        lower = new Wall(0, 0, Gdx.graphics.getWidth()*PIXELS_TO_METRES, 1, 0f);

        left = new Wall(0, 0, 1, Gdx.graphics.getHeight()*PIXELS_TO_METRES, 0f);

        upper = new Wall(0, Gdx.graphics.getHeight(), Gdx.graphics.getWidth()*PIXELS_TO_METRES, 1, 0f);

        right = new Wall(Gdx.graphics.getWidth(), 0, 1, Gdx.graphics.getHeight()*PIXELS_TO_METRES, 0f);

        lower.createBody(world);
        left.createBody(world);
        upper.createBody(world);
        right.createBody(world);
    }

    @Override
    public void show() {
        init();
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(inputMultiplexer);

        world.setContactListener(new CollisionListener());
    }

    private void init() {
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("UI/atlas.pack"));
        Skin skin = new Skin(Gdx.files.internal("Skins/MenuSkin.json"), atlas);

        world = new World(new Vector2(0f, 0f), true);

        //new Boundary(PIXELS_TO_METRES,world);
        addBoundaryWalls();

        callback = (fixture, point, normal1, fraction) -> {
            collision.set(point.x*PIXELS_TO_METRES, point.y*PIXELS_TO_METRES);
            PlayScreen.this.normal.set(normal1.x*PIXELS_TO_METRES, normal1.y*PIXELS_TO_METRES).
                    add(point.x*PIXELS_TO_METRES, point.y*PIXELS_TO_METRES);
            angle = collision.angle(normal);
            deflection.setLength(p1.len());
            deflection.rotate(60);
            return fraction;
        };

        TextButton editor = new TextButton("Editor", skin);
        editor.setPosition(0, Gdx.graphics.getHeight() - editor.getHeight());
        textField.setPosition(editor.getX() + editor.getWidth(), editor.getY());
        editor.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameData.getGame().setScreen(new LevelEditor(gameData));
            }
        });

        for(int i=0;i<itemList.size;i++) {
            itemList.get(i).createBody(world);
        }

        matrix4 = stage.getBatch().getProjectionMatrix().cpy().scale(PIXELS_TO_METRES, PIXELS_TO_METRES, 0);
        debugRenderer = new Box2DDebugRenderer();

        stage.addActor(editor);
        stage.addActor(textField);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        p1.set(player.getX(), player.getY());
        deflection.set(collision).add(100,100);

        world.step(1f/60f, 6, 2);

        textField.setText(Double.toString(Math.toDegrees(angle)));

        for(int i= 0;i<itemList.size;i++)
            itemList.get(i).updatePlay();

        stage.act();
        stage.draw();

        if(showTrajectoryRay) {

            trajectoryRay.begin(ShapeRenderer.ShapeType.Line);
            trajectoryRay.setColor(Color.RED);
            trajectoryRay.line(p1, p2);
            temp1.set(p1.x/PIXELS_TO_METRES, p1.y/PIXELS_TO_METRES);
            temp2.set(p2.x/PIXELS_TO_METRES, p2.y/PIXELS_TO_METRES);
            world.rayCast(callback, temp1, temp2);
            trajectoryRay.setColor(Color.BLACK);
            trajectoryRay.line(collision, normal);
            trajectoryRay.setColor(Color.BLUE);
            trajectoryRay.line(collision, deflection);
            trajectoryRay.end();
        }

//        debugRenderer.render(world, matrix4);
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
        temp.set(screenX, screenY);

        player.getBody().setAngularVelocity(0f);
        player.getBody().setLinearVelocity(0f, 0f);

        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        player.setNewPoint(screenX, screenY);

        showTrajectoryRay = false;
        collision.set(0f,0f);
        normal.set(0f, 0f);

        player.getBody().setAngularVelocity(0f);
        player.getBody().setLinearVelocity(0f, 0f);

        player.getBody().applyForceToCenter(player.getNewPoint().distanceX(player.getOldPoint().getX())/PIXELS_TO_METRES,
                -player.getNewPoint().distanceY(player.getOldPoint().getY())/PIXELS_TO_METRES, true);
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        p2.set(p1.x + (screenX - temp.x),p1.y - (screenY - temp.y));

        showTrajectoryRay = true;
        return true;
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

