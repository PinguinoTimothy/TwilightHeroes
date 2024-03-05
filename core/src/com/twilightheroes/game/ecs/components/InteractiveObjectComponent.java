package com.twilightheroes.game.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;


/**
 * The type Interactive object component.
 */
public class InteractiveObjectComponent implements Component, Pool.Poolable {


    /**
     * The Id.
     */
    public int id;
    /**
     * The type of object.
     */
    public int tipoObjeto;

    /**
     * Resets the object for reuse.
     */
    @Override
    public void reset() {

        id = -1;
        tipoObjeto = -1;
    }
}
