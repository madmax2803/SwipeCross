package com.swapnil.leveleditor.item;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.swapnil.leveleditor.GameData;
import com.swapnil.leveleditor.util.Point;

public class Laser extends Item {

    private Sprite sprite;
    private Body body;

    private Point point;


    private final float width = new Texture(Gdx.files.internal("unitTexture/Laser.png")).getWidth();
    private final float height = new Texture(Gdx.files.internal("unitTexture/Laser.png")).getHeight();

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    @Override
    public Table getForm() {
        return null;
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
            xmlWriter.element("Laser")
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
    public Item loadFromXml(XmlReader.Element element) {
        Laser laser = new Laser();
        laser.setSprite(new Sprite(new Texture("unitTexture/Laser.png")));
        laser.setX(element.getChildByName("Position").getFloat("X"));
        laser.setY(element.getChildByName("Position").getFloat("Y"));
        laser.setAngle(element.getChildByName("Angle").getFloat("Value"));
        return laser;

    }

    public Laser() {
        this.actor = new Actor() {
            public void draw(Batch batch, float parentAlpha) {
                super.draw(batch, parentAlpha);
                sprite.draw(batch);

            }
        };
        point = new Point();
    }

    @Override

    public void createBody(World world) {

    }

    @Override
    public void updateEditor() {

        float yIntercept = (Gdx.graphics.getHeight() - getY()) - (float) (Math.tan(MathUtils.degreesToRadians*getAngle())*(getX()));
        point.setX(0);
        point.setY(yIntercept);

        sprite.setPosition(x - width/2, Gdx.graphics.getHeight() -  y - height/2);

        sprite.setRotation(getAngle());

        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(new Color(0f, 0f, 0f, 0.2f));
        shapeRenderer.line(getX(),Gdx.graphics.getHeight() - getY(), 0, point.getX(), point.getY(), 0);
        shapeRenderer.end();
        shapeRenderer.dispose();


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

        actor.setOrigin(sprite.getOriginX(), sprite.getOriginY());
        actor.setWidth(width);
        actor.setHeight(height);
        actor.setX(getX() - width/2);
        actor.setY(Gdx.graphics.getHeight() - getY() - height/2);
        actor.setRotation(getAngle());
    }

    @Override
    public void beginContactWith(Item itemB) {

    }


}
