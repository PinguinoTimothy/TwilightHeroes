package com.twilightheroes.game.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.twilightheroes.game.tools.Collisions;

/**
 * The type Collision component.
 */
public class CollisionComponent implements Component, Pool.Poolable {


    /**
     * The Collision entities.
     */
    public Array<Collisions> collisionEntities = new Array<>();


    /**
     * Resets the object for reuse.
     */
    @Override
    public void reset() {

        collisionEntities.clear();
    }
}
