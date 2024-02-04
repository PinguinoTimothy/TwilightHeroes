package com.twilightheroes.game.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;

public class BulletComponent implements Component, Pool.Poolable {

    public static enum Owner { ENEMY,PLAYER,SCENERY,NONE }

    public float xVel = 0;
    public float yVel = 0;
    public float damage;
    public boolean isDead = false;
    public Owner owner = Owner.NONE;

    public Entity particleEffect;


    @Override
    public void reset() {
        owner = Owner.NONE;
        xVel = 0;
        yVel = 0;
        isDead = false;
        particleEffect = null;
    }
}
