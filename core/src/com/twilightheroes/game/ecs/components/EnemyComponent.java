package com.twilightheroes.game.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import com.twilightheroes.game.ecs.components.spells.Spell;

/**
 * The type Enemy component.
 */
public class EnemyComponent implements Component, Pool.Poolable {


    /**
     * The Name.
     */
    public String name = "";
    /**
     * If the enemy is dead.
     */
    public boolean isDead = false;
    /**
     * The View distance.
     */
    public float viewDistance;
    /**
     * The Attack distance.
     */
    public float attackDistance;
    /**
     * The Attack cooldown.
     */
    public float attackCooldown;
    /**
     * The Attack frame.
     */
    public int attackFrame;
    /**
     * The Attack method.
     */
    public String attackMethod;

    /**
     * The Spells the enemy have.
     */
    public Spell[] spells;

    /**
     * Resets the object for reuse.
     */
    @Override
    public void reset() {

        isDead = false;
        spells = null;
        viewDistance = 0;
        attackDistance = 0;
        attackCooldown = 0;
        attackFrame = 0;

    }
}
