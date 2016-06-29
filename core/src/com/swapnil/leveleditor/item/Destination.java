package com.swapnil.leveleditor.item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;

public class Destination extends Item {

    private Sprite sprite;

    public Destination() {
        this.actor=new Actor(){
            @Override
            public void draw(Batch batch, float parentAlpha) {
                super.draw(batch, parentAlpha);
                sprite.setPosition(x - sprite.getWidth()/2, Gdx.graphics.getHeight() -  y - sprite.getHeight()/2);
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
}
