package com.swapnil.leveleditor.item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;

import java.io.StringWriter;

public class Wall extends Item{

    private Sprite sprite;
    private float length, width;
    private Body body;

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
        actor.setWidth(this.sprite.getWidth());
        actor.setHeight(this.sprite.getHeight());
        setX(sprite.getX());
        setY(sprite.getY());
        setAngle(sprite.getRotation());

    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public Wall() {
        this.actor = new Actor() {
            @Override
            public void draw(Batch batch, float parentAlpha) {
                super.draw(batch, parentAlpha);
                if (body!=null) {
                    sprite.setPosition(body.getPosition().x - sprite.getWidth()/2,
                            Gdx.graphics.getHeight() -  body.getPosition().y - sprite.getHeight()/2);
                }else{
                    sprite.setPosition(x - sprite.getWidth()/2,
                            Gdx.graphics.getHeight() -  y - sprite.getHeight()/2);
                }
                sprite.setRotation(getAngle());
                setAngle(sprite.getRotation());
                update();
                sprite.draw(batch);

            }
        };
    }

    @Override
    public boolean contains(int screenX, int screenY) {
        sprite.setBounds(x - sprite.getWidth()/2,Gdx.graphics.getHeight() - y - sprite.getHeight()/2,
                sprite.getWidth(), sprite.getHeight());
        return(sprite.getBoundingRectangle().contains(screenX, screenY));
    }

    @Override
    public void writeToXml(XmlWriter xmlWriter) {
        try {
            xmlWriter.element("Wall")
                    .element("Position")
                    .attribute("X", sprite.getX() + sprite.getWidth())
                    .attribute("Y", Gdx.graphics.getHeight() - sprite.getY() - sprite.getHeight()/2)
                    .pop()
                    .element("Angle")
                    .attribute("Value", sprite.getRotation())
                    .pop()
                    .pop();
        }
        catch (Exception e) {
            Gdx.app.log("XML WRITE FAILED", e.getMessage());
        }
    }

    @Override
    public Wall loadFromXml(XmlReader.Element element) {
        Wall wall = new Wall();
        wall.setSprite(new Sprite(new Texture("unitTexture/Wall.png")));
        wall.setX(element.getChildByName("Position").getFloat("X"));
        wall.setY(element.getChildByName("Position").getFloat("Y"));
        wall.setAngle(element.getChildByName("Angle").getFloat("Value"));
        return wall;
    }

    @Override
    public void createBody(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x, y);

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sprite.getWidth()/2, sprite.getHeight()/2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.restitution = 0.5f;
        fixtureDef.density = 0.1f;
        body.createFixture(fixtureDef);

        shape.dispose();
    }

    @Override
    public void update() {

        actor.setX(getX());
        actor.setY(getY());
        actor.setRotation(getAngle());


    }
}
