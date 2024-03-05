package com.twilightheroes.game.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool;

/**
 * The type B 2 d body component.
 */
public class B2dBodyComponent implements Component, Pool.Poolable {
    /**
     * The Body.
     */
    public Body body;
    /**
     * The Width.
     */
    public float width;
    /**
     * The Height.
     */
    public float height;
    /**
     * The Start x.
     */
    public float startX;
    /**
     * The Start y.
     */
    public float startY;

    /**
     * The Is dead.
     */
    public boolean isDead = false;


    /**
     * Resets the object for reuse.
     */
    @Override
    public void reset() {
        body = null;
        isDead = false;
    }

}
