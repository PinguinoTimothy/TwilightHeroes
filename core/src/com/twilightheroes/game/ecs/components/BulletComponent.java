package com.twilightheroes.game.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * The type Bullet component.
 */
public class BulletComponent implements Component, Pool.Poolable {

    /**
     * The X velocity.
     */
    public float xVel = 0;
    /**
     * The Y velocity.
     */
    public float yVel = 0;
    /**
     * The Damage.
     */
    public float damage;
    /**
     * If the bullet is dead.
     */
    public boolean isDead = false;
    /**
     * The Owner.
     */
    public Owner owner = Owner.NONE;


    /**
     * Resets the object for reuse.
     */
    @Override
    public void reset() {
        owner = Owner.NONE;
        xVel = 0;
        yVel = 0;
        isDead = false;
    }


    /**
     * The enum that defines how own the bullet.
     */
    public enum Owner {
        /**
         * Enemy .
         */
        ENEMY,
        /**
         * Player .
         */
        PLAYER,
        /**
         * Scenery .
         */
        SCENERY,
        /**
         * None.
         */
        NONE
    }
}
