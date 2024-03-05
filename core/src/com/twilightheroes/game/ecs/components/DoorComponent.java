package com.twilightheroes.game.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * The type Door component.
 */
public class DoorComponent implements Component, Pool.Poolable {

    /**
     * The Id.
     */
    public int id;
    /**
     * if the door is open.
     */
    public boolean open = false;

    /**
     * Resets the object for reuse.
     */
    @Override
    public void reset() {
id = -1;
open = false;
    }
}
