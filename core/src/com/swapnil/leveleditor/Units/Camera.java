package com.swapnil.leveleditor.Units;


import com.badlogic.gdx.graphics.g2d.Sprite;

public class Camera extends Item{

    private Sprite sprite;
    private float deviationAngle;

    public float getDeviationAngle() {
        return deviationAngle;
    }

    public void setDeviationAngle(float deviationAngle) {
        this.deviationAngle = deviationAngle;
    }
}
