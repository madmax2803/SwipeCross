package com.swapnil.leveleditor.listener;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.swapnil.leveleditor.item.Item;

public class CollisionListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {

        Item itemB = ((Item) contact.getFixtureB().getBody().getUserData());
        Item itemA = ((Item) contact.getFixtureA().getBody().getUserData());

        itemA.beginContactWith(itemB);
        itemB.beginContactWith(itemA);
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
