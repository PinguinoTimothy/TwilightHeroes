package com.twilightheroes.game.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Pool;

/**
 * The type Player component.
 */
public class PlayerComponent implements Component, Pool.Poolable {

    /**
     * The Mana.
     */
    public float mana = 100;


    /**
     * If is in knockback.
     */
    public boolean knockback = false;
    /**
     * The Knock back time.
     */
    public float knockBackTime;
    /**
     * If the player is immune.
     */
    public boolean immune = false;
    /**
     * How much more time the player is immune.
     */
    public float immuneTime;
    /**
     * If the player is dead
     */
    public boolean isDead = false;
    /**
     * If the player can dodge.
     */
    public boolean canDodge = true;
    /**
     * If the player can jump.
     */
    public boolean canJump = true;
    /**
     * If the player can attack.
     */
    public boolean canAttack = true;
    /**
     * The Coyote time.
     */
    public float coyoteTime;
    /**
     * The Interact fixture.
     */
    public Fixture interactFixture;

    /**
     * If the player ended the game.
     */
    public boolean end = false;


    /**
     * Resets the object for reuse.
     */
    @Override
    public void reset() {
        knockback = false;
        isDead = false;
        canJump = true;
        canDodge = true;
        canAttack = true;
        immune = false;
        end = false;
    }
}
