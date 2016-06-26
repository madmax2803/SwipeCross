package com.swapnil.leveleditor.util;

/**
 * Generic utility class for storing 2d point
 * Created by swapnilverma on 22/06/16.
 */
public class Point {
    private float x;
    private float y;

    public Point() {
        x=0;
        y=0;
    }

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
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

    public float distanceFrom(Point p){
        return 0;//TODO formula
    }
}
