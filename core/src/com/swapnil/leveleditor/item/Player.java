package com.swapnil.leveleditor.item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.swapnil.leveleditor.GameData;
import com.swapnil.leveleditor.screens.GameOverScreen;
import com.swapnil.leveleditor.util.Point;

public class Player extends Item {

    private Sprite sprite;
    private Body body;

    private Point oldPoint;
    private Point newPoint;

    public void setGameData(GameData gameData) {
        this.gameData = gameData;
    }

    private GameData gameData;

    public Point getNewPoint() {
        return newPoint;
    }

    public void setNewPoint(float x, float y) {
        newPoint = new Point();
        newPoint.setX(Math.abs(x));
        newPoint.setY(Math.abs(y));
    }

    public Point getOldPoint() {
        return oldPoint;
    }

    public void setOldPoint(float x, float y) {
        oldPoint = new Point();
        oldPoint.setX(Math.abs(x));
        oldPoint.setY(Math.abs(y));
    }

    private final float width = new Texture(Gdx.files.internal("unitTexture/Player.png")).getWidth();
    private final float height = new Texture(Gdx.files.internal("unitTexture/Player.png")).getHeight();

    public Body getBody() {
        return body;
    }

    public Player() {
        this.actor=new Actor(){
            @Override
            public void draw(Batch batch, float parentAlpha) {
                super.draw(batch, parentAlpha);
                sprite.draw(batch);
            }
        };
    }

    @Override
    public Table getForm() {
        return null;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
        setAngle(sprite.getRotation());
    }

    @Override
    public boolean contains(int screenX, int screenY) {
        sprite.setBounds(sprite.getX(),sprite.getY(),
                sprite.getWidth(), sprite.getHeight());

        return(sprite.getBoundingRectangle().contains(screenX, screenY));
    }

    @Override
    public void writeToXml(XmlWriter xmlWriter) {
        try {
            xmlWriter.element("Player")
                        .element("Position")
                            .attribute("X", getX())
                            .attribute("Y", getY())
                        .pop()
                        .element("Angle")
                            .attribute("Value", getAngle())
                        .pop()
                    .pop();
        }
        catch (Exception e) {
            System.out.println("XML WRITE FAILED");
        }
    }

    @Override
    public Player loadFromXml(XmlReader.Element element) {
        Player player = new Player();
        player.setSprite(new Sprite(new Texture("unitTexture/Player.png")));
        player.setX(element.getChildByName("Position").getFloat("X"));
        player.setY(element.getChildByName("Position").getFloat("Y"));
        player.setAngle(element.getChildByName("Angle").getFloat("Value"));
        return player;
    }

    @Override
    public void createBody(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x/PIXELS_TO_METRES,Gdx.graphics.getHeight()/PIXELS_TO_METRES -  y/PIXELS_TO_METRES);
        bodyDef.angle = getAngle() * MathUtils.degRad;

        body = world.createBody(bodyDef);
        body.setUserData(this);

        CircleShape circle = new CircleShape();
        circle.setRadius(width/2/PIXELS_TO_METRES);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.restitution = 0.5f;
        fixtureDef.density = 0.1f;
        body.createFixture(fixtureDef);

        circle.dispose();
    }

    @Override
    public void updateEditor() {
        sprite.setPosition(x - width/2, Gdx.graphics.getHeight() -  y - height/2);
        sprite.setRotation(getAngle());
        sprite.setOriginCenter();

        actor.setOrigin(sprite.getOriginX(), sprite.getOriginY());
        actor.setWidth(width);
        actor.setHeight(height);
        actor.setX(getX() - width/2);
        actor.setY(Gdx.graphics.getHeight() - getY() - height/2);
        actor.setRotation(getAngle());

    }

    @Override
    public void updatePlay() {
        sprite.setPosition(body.getPosition().x * PIXELS_TO_METRES - width/2,
                body.getPosition().y * PIXELS_TO_METRES - height/2);
        sprite.setOriginCenter();
        sprite.setRotation(getAngle());

        setX(body.getPosition().x * PIXELS_TO_METRES);
        setY(body.getPosition().y * PIXELS_TO_METRES);

        actor.setOrigin(sprite.getOriginX(), sprite.getOriginY());
        actor.setWidth(width);
        actor.setHeight(height);
        actor.setX(getX() - width/2);
        actor.setY(Gdx.graphics.getHeight() - getY() - height/2);
        actor.setRotation(getAngle());
    }

    @Override
    public void beginContactWith(Item itemB) {
        if(itemB.getClass()==Camera.class) {
            gameData.getGame().setScreen(new GameOverScreen(gameData, false));
        }
        else if(itemB.getClass() == Guard.class) {
            gameData.getGame().setScreen(new GameOverScreen(gameData, false));
        }
        else if(itemB.getClass() == Destination.class) {
            gameData.getGame().setScreen(new GameOverScreen(gameData, true));
        }
        else if(itemB.getClass() == Wall.class) {
            //DO NOTHING
        }

    }

}
