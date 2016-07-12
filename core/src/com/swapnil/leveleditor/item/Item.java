package com.swapnil.leveleditor.item;

import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;

public abstract class Item {

    protected float x;
    protected float y;
    private float angle;
    protected Actor actor=new Actor();
    protected final float PIXELS_TO_METRES = 100f;

    public Item() {

    }

    public abstract Table getForm();

    public Actor getActor() {
        return actor;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public abstract boolean contains(int screenX, int screenY);

    public abstract void writeToXml(XmlWriter xmlWriter);

    public abstract Item loadFromXml(XmlReader.Element element);

    public abstract void createBody(World world);

    public abstract void update();
}
