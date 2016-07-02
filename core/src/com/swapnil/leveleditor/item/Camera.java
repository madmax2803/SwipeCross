package com.swapnil.leveleditor.item;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;

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

    @Override
    public void writeToXml(XmlWriter xmlWriter) {

    }

    @Override
    public Item loadFromXml(XmlReader.Element element) {
        return null;
    }

    @Override
    public void createBody(World world) {

    }

    @Override
    public void update() {

        setX(sprite.getX() - sprite.getWidth()/2);
        setY(Gdx.graphics.getHeight() - sprite.getY() - sprite.getHeight()/2);
        setAngle(sprite.getRotation());

        actor.setX(getX());
        actor.setY(getY());
        actor.setRotation(getAngle());


    }

}
