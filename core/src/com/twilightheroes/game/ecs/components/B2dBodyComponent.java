package com.twilightheroes.game.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool;

public class B2dBodyComponent implements Component, Pool.Poolable {
    public Body body;
    public float width;
    public float height;
    public float startX;
    public float startY;

    public boolean isDead = false;

    @Override
    public void reset() {
        body = null;
        isDead = false;
    }

}
