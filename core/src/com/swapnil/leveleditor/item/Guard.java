package com.swapnil.leveleditor.item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.swapnil.leveleditor.util.Point;
import com.swapnil.leveleditor.util.forms.GuardForm;

public class Guard extends Item {

    private Point selectedEdge;

    private final float width = new Texture(Gdx.files.internal("unitTexture/GuardEditor1.png")).getWidth();
    private final float height = new Texture(Gdx.files.internal("unitTexture/GuardEditor1.png")).getHeight();

    public void setSprite(Sprite sprite1, Sprite sprite2) {
        this.sprite1 = sprite1;
        this.sprite2 = sprite2;
    }

    private Sprite sprite1, sprite2;
    private Point p1;
    private Point p2;

    public Point getP2() {
        return p2;
    }

    public void setP2(Point p2) {
        this.p2 = p2;
    }

    public Point getP1() {
        return p1;
    }

    public void setP1(Point p1) {
        this.p1 = p1;
    }

    private boolean move;

    private GuardForm guardForm;

    public boolean isMove() {
        return move;
    }

    public void setMove(boolean move) {
        this.move = move;
    }

    public Guard() {
        this.actor = new Actor() {
            @Override
            public void draw(Batch batch, float parentAlpha) {
                super.draw(batch, parentAlpha);
                sprite1.draw(batch);
                sprite2.draw(batch);
            }
        };
        p1 = new Point();
        p2 = new Point();
        selectedEdge = new Point();
        guardForm = new GuardForm(this);
        guardForm.getForm().setVisible(false);
    }

    @Override
    public Table getForm() {
        return guardForm.getForm();
    }

    @Override
    public boolean contains(int screenX, int screenY) {
        sprite1.setBounds(sprite1.getX(),sprite1.getY(),
                sprite1.getWidth(), sprite1.getHeight());

        sprite2.setBounds(sprite2.getX(),sprite2.getY(),
                sprite2.getWidth(), sprite2.getHeight());

        if(sprite1.getBoundingRectangle().contains(screenX, screenY)) {
            selectedEdge = p1;
            return true;
        }
        else if(sprite2.getBoundingRectangle().contains(screenX, screenY)) {
            selectedEdge = p2;
            return true;
        }
        return false;

    }

    @Override
    public void writeToXml(XmlWriter xmlWriter) {

        try {
            xmlWriter.element("Guard")
                        .element("Position")
                            .attribute("X", getX())
                            .attribute("Y", getY())
                        .pop()
                        .element("Angle")
                            .attribute("Value", getAngle())
                        .pop()
                        .element("Point1")
                            .attribute("X1", p1.getX())
                            .attribute("Y1", p1.getY())
                        .pop()
                        .element("Point2")
                            .attribute("X2", p2.getX())
                            .attribute("Y2", p2.getY())
                        .pop()
                    .pop();
        }
        catch (Exception e) {
            System.out.println("XML WRITE FAILED");
        }
    }

    @Override
    public Item loadFromXml(XmlReader.Element element) {

        Guard guard = new Guard();
        guard.setSprite(new Sprite(new Texture("unitTexture/GuardEditor2.png")),new Sprite(new Texture("unitTexture/GuardEditor1.png")));
        guard.setX(element.getChildByName("Position").getFloat("X"));
        guard.setY(element.getChildByName("Position").getFloat("Y"));
        guard.setAngle(element.getChildByName("Angle").getFloat("Value"));
        guard.getP1().setX(element.getChildByName("Point1").getFloat("X1"));
        guard.getP1().setY(element.getChildByName("Point1").getFloat("Y1"));
        guard.getP2().setX(element.getChildByName("Point2").getFloat("X2"));
        guard.getP2().setY(element.getChildByName("Point2").getFloat("Y2"));
        guardForm.setDefaults(p1, p2);
        return guard;

    }

    @Override
    public void createBody(World world) {

    }

    @Override
    public void updateEditor() {

        guardForm.setDefaults(p1, p2);

        selectedEdge.setX(getX());
        selectedEdge.setY(Gdx.graphics.getHeight() - getY());

        sprite1.setPosition(p1.getX(),p1.getY());
        sprite2.setPosition(p2.getX(),p2.getY());


        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(new Color(0f, 0f, 0f, 0.2f));
        shapeRenderer.line(p1.getX() + width/2,p1.getY() + height/2, 0, p2.getX() + width/2,p2.getY() + height/2, 0);
        shapeRenderer.end();
        shapeRenderer.dispose();

        actor.setWidth(width);
        actor.setHeight(height);
        actor.setX(getX() + width/2);
        actor.setY(getY() + height/2);

    }

    @Override
    public void updatePlay() {

    }

}
