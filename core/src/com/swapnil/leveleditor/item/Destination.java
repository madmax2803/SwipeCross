package com.swapnil.leveleditor.item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;

public class Destination extends Item {

    private Sprite sprite;
    private Body body;

    public Destination() {
        this.actor=new Actor(){
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
                sprite.draw(batch);
                actor.setX(x - sprite.getWidth()/2);
                actor.setY(Gdx.graphics.getHeight() - y - sprite.getHeight()/2);

            }
        };
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
        actor.setWidth(sprite.getWidth());
        actor.setHeight(sprite.getHeight());
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
            xmlWriter.element("Destination")
                    .element("Position")
                    .attribute("X", sprite.getX() + sprite.getWidth()/2)
                    .attribute("Y", Gdx.graphics.getHeight() - sprite.getY() - sprite.getHeight()/2)
                    .pop()
                .pop();
        }
        catch (Exception e) {
            Gdx.app.log("XML WRITE FAILED", e.getMessage());
        }
    }

    @Override
    public Destination loadFromXml(XmlReader.Element element) {
        Destination destination = new Destination();
        destination.setSprite(new Sprite(new Texture("unitTexture/Destination.png")));
        destination.setX(element.getChildByName("Position").getFloat("X"));
        destination.setY(element.getChildByName("Position").getFloat("Y"));
        return destination;
    }

    @Override
    public void createBody(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
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

}
