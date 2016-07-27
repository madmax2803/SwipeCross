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

public class Destination extends Item {

    private Sprite sprite;
    private Body body;

    private final float width = new Texture(Gdx.files.internal("unitTexture/Destination.png")).getWidth();
    private final float height = new Texture(Gdx.files.internal("unitTexture/Destination.png")).getHeight();

    public Destination() {
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
        return(sprite.getBoundingRectangle().contains(screenX, screenY));
    }

    @Override
    public void writeToXml(XmlWriter xmlWriter) {
        try {
            xmlWriter.element("Destination")
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
    public Destination loadFromXml(XmlReader.Element element) {
        Destination destination = new Destination();
        destination.setSprite(new Sprite(new Texture("unitTexture/Destination.png")));
        destination.setX(element.getChildByName("Position").getFloat("X"));
        destination.setY(element.getChildByName("Position").getFloat("Y"));
        destination.setAngle(element.getChildByName("Angle").getFloat("Value"));
        return destination;
    }

    @Override
    public void createBody(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x/PIXELS_TO_METRES, Gdx.graphics.getHeight()/PIXELS_TO_METRES - y/PIXELS_TO_METRES);
        bodyDef.angle = getAngle() * MathUtils.degRad;

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/2/PIXELS_TO_METRES, height/2/PIXELS_TO_METRES);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.restitution = 0.5f;
        fixtureDef.density = 0.1f;
        body.createFixture(fixtureDef);

        shape.dispose();
    }

    @Override
    public void updateEditor() {

        if (body!=null) {
            sprite.setPosition(body.getPosition().x * PIXELS_TO_METRES - width/2,
                    Gdx.graphics.getHeight() - body.getPosition().y * PIXELS_TO_METRES - height/2);
        }else{
            sprite.setPosition(x - width/2,
                    Gdx.graphics.getHeight() -  y - height/2);

        }
        sprite.setRotation(getAngle());


        sprite.setBounds(x - width/2,Gdx.graphics.getHeight() -  y - height/2,
                width, height);
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

    }

}
