package com.twilightheroes.game.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * The type Hazard component.
 */
public class HazardComponent implements Component, Pool.Poolable {

    /**
     * The Damage.
     */
    public int damage;
    /**
     * If the hazard is active.
     */
    public boolean alive;

    /**
     * Resets the object for reuse.
     */
    @Override
    public void reset() {
        damage = 0;
        alive = false;
    }
}
