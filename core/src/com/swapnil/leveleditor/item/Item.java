package com.swapnil.leveleditor.item;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class Item {

    protected float x;
    protected float y;
    private float angle;
    protected Actor actor=new Actor();

    public Item() {
    }

//    @Override
//    public void draw(Batch batch, float parentAlpha) {
//        super.draw(batch, parentAlpha);
//    }


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
}
