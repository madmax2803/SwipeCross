package com.swapnil.leveleditor.item;


import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;

public class Laser extends Item {

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

    }


}
