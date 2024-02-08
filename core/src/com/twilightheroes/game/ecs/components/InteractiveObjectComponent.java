package com.twilightheroes.game.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class InteractiveObjectComponent implements Component, Pool.Poolable {


    public int id;

    @Override
    public void reset() {

    }
}
