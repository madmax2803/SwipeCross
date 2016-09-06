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
import com.swapnil.leveleditor.util.forms.WallForm;

public class Wall extends Item{

    private Sprite sprite;

    private float width, height;
    private Body body;

    private WallForm wallForm;

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;

        setAngle(sprite.getRotation());

        setWidth(this.sprite.getWidth());
        setHeight(this.sprite.getHeight());
        this.wallForm.setDefaults(getWidth(), getHeight());

    }

    public Wall() {
        this.actor = new Actor() {
            @Override
            public void draw(Batch batch, float parentAlpha) {
                super.draw(batch, parentAlpha);
                sprite.draw(batch);

            }
        };
        wallForm = new WallForm(this);
        wallForm.getForm().setVisible(false);
    }

    @Override
    public Table getForm() {
        return wallForm.getForm();
    }

    @Override
    public boolean contains(int screenX, int screenY) {
        return(sprite.getBoundingRectangle().contains(screenX, screenY));
    }

    @Override
    public void writeToXml(XmlWriter xmlWriter) {
        try {
            xmlWriter.element("Wall")
                        .element("Position")
                            .attribute("X", getX())
                            .attribute("Y", getY())
                        .pop()
                        .element("Angle")
                            .attribute("Value", getAngle())
                        .pop()
                        .element("Size")
                            .attribute("Width", getWidth())
                            .attribute("Height", getHeight())
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
        wall.setWidth(element.getChildByName("Size").getFloat("Width"));
        wall.setHeight(element.getChildByName("Size").getFloat("Height"));
        wall.wallForm.setDefaults(wall.getWidth(), wall.getHeight());
        return wall;
    }

    @Override
    public void createBody(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(getX()/PIXELS_TO_METRES,Gdx.graphics.getHeight()/PIXELS_TO_METRES - getY()/PIXELS_TO_METRES);
        bodyDef.angle = getAngle() * MathUtils.degRad;

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox( getWidth()/2/PIXELS_TO_METRES, getHeight()/2/PIXELS_TO_METRES);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.restitution = 0.5f;
        fixtureDef.density = 0.1f;
        body.createFixture(fixtureDef);

        shape.dispose();
    }

    @Override
    public void updateEditor() {


        sprite.setPosition(x - getWidth()/2, Gdx.graphics.getHeight() -  y - getHeight()/2);
        sprite.setRotation(getAngle());
        sprite.setSize(getWidth(), getHeight());
        sprite.setBounds(x - getWidth()/2,Gdx.graphics.getHeight() - y - getHeight()/2,
                getWidth(), getHeight());
        sprite.setOriginCenter();

        actor.setOrigin(sprite.getOriginX(), sprite.getOriginY());
        actor.setWidth(this.getWidth());
        actor.setHeight(this.getHeight());
        actor.setX(getX() - this.getWidth()/2);
        actor.setY(Gdx.graphics.getHeight() - getY() - this.getHeight()/2);
        actor.setRotation(getAngle());

    }

    @Override
    public void updatePlay() {
        sprite.setPosition(body.getPosition().x * PIXELS_TO_METRES - width/2,
                body.getPosition().y * PIXELS_TO_METRES - height/2);
        sprite.setOriginCenter();
        sprite.setRotation(getAngle());
        sprite.setSize(getWidth(), getHeight());
        sprite.setBounds(x - getWidth()/2,Gdx.graphics.getHeight() - y - getHeight()/2,
                getWidth(), getHeight());
        sprite.setOriginCenter();

        actor.setOrigin(sprite.getOriginX(), sprite.getOriginY());
        actor.setWidth(width);
        actor.setHeight(height);
        actor.setX(getX() - width/2);
        actor.setY(Gdx.graphics.getHeight() - getY() - height/2);
        actor.setRotation(getAngle());
    }
}
