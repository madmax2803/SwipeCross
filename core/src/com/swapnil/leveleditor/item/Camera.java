package com.swapnil.leveleditor.item;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Camera extends Item{

    private Sprite sprite;
    private float deviationAngle;

    public Camera() {
        this.actor = new Actor() {
            @Override
            public void draw(Batch batch, float parentAlpha) {
                super.draw(batch, parentAlpha);
                sprite.setPosition(x - sprite.getWidth()/2, Gdx.graphics.getHeight() -  y - sprite.getHeight()/2);
                sprite.draw(batch);
            }
        };
    }

    public float getDeviationAngle() {
        return deviationAngle;
    }

    public void setDeviationAngle(float deviationAngle) {
        this.deviationAngle = deviationAngle;
    }

    @Override
    public boolean contains(int screenX, int screenY) {
        return false;
    }
}
