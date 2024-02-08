package com.twilightheroes.game.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class BulletComponent implements Component, Pool.Poolable {

    public float xVel = 0;
    public float yVel = 0;
    public float damage;
    public boolean isDead = false;
    public Owner owner = Owner.NONE;

    @Override
    public void reset() {
        owner = Owner.NONE;
        xVel = 0;
        yVel = 0;
        isDead = false;
    }


    public enum Owner {ENEMY, PLAYER, SCENERY, NONE}
}
