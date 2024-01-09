package com.twilightheroes.game.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class ExitComponent implements Component, Pool.Poolable {
    public int exitToRoom;

    @Override
    public void reset() {
        exitToRoom = -1;
    }
}
