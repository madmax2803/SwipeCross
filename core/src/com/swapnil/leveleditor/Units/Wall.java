package com.swapnil.leveleditor.Units;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class Wall extends Item{

    private Sprite sprite;
    private float length, width;

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }
}
