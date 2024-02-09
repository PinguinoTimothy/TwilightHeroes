package com.twilightheroes.game.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class DoorComponent implements Component, Pool.Poolable {

    public int id;
    public boolean open = false;

    @Override
    public void reset() {
id = -1;
open = false;
    }
}
