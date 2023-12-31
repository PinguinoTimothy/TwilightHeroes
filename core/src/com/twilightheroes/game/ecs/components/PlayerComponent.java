package com.twilightheroes.game.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class PlayerComponent implements Component,Pool.Poolable {
    public Array<Entity> enemigosEnRango = new Array<>();
    public float speed = 100;
    public float jumpPower = 200;

    public boolean knockback = false;

    @Override
    public void reset() {
        knockback = false;
    }
}
