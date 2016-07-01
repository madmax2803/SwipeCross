package com.swapnil.leveleditor.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;

public class Boundary {


    Body lower, left, right, upper;

    public Boundary(World world) {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        BodyDef bodyDef2 = new BodyDef();
        bodyDef2.type = BodyDef.BodyType.StaticBody;
        bodyDef2.position.set(0,0);
        FixtureDef fixtureDef2 = new FixtureDef();

        EdgeShape Shape = new EdgeShape();
        Shape.set(0,0,w,0);
        fixtureDef2.shape = Shape;

        lower = world.createBody(bodyDef2);
        lower.createFixture(fixtureDef2);

        Shape.set(0,0,0,h);
        fixtureDef2.shape = Shape;

        left = world.createBody(bodyDef2);
        left.createFixture(fixtureDef2);

        Shape.set(w,0,w,h);
        fixtureDef2.shape = Shape;

        right= world.createBody(bodyDef2);
        right.createFixture(fixtureDef2);

        Shape.set(0,h,w,h);
        fixtureDef2.shape = Shape;

        upper = world.createBody(bodyDef2);
        upper.createFixture(fixtureDef2);

        Shape.dispose();
    }
}
