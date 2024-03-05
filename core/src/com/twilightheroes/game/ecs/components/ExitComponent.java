package com.twilightheroes.game.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * The type Exit component.
 */
public class ExitComponent implements Component, Pool.Poolable {
    /**
     * To what room is the exit.
     */
    public int exitToRoom;
    /**
     * Indicates from what room the exit is.
     */
    public int fromRoom;

    /**
     * Resets the object for reuse.
     */
    @Override
    public void reset() {
        exitToRoom = -1;
        fromRoom = 0;
    }
}
