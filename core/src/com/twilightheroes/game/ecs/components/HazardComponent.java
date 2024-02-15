package com.twilightheroes.game.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class HazardComponent implements Component, Pool.Poolable {

   public int damage;
    public boolean alive;

    @Override
    public void reset() {
damage = 0;
alive = false;
    }
}
