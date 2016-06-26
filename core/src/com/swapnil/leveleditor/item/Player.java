package com.swapnil.leveleditor.item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Player extends Item {

    private Sprite sprite;

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
        actor.setWidth(this.sprite.getWidth());
        actor.setHeight(this.sprite.getHeight());
        actor.setX(this.sprite.getX());
        actor.setY(this.sprite.getY());
    }

    public Player() {
        this.actor = new Actor() {
            @Override
            public void draw(Batch batch, float parentAlpha) {
                super.draw(batch, parentAlpha);
                sprite.setPosition(x - sprite.getWidth()/2, Gdx.graphics.getHeight() -  y - sprite.getHeight()/2);
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
}
