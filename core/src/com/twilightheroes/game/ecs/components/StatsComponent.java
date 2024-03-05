package com.twilightheroes.game.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * The type Stats component.
 */
public class StatsComponent implements Component, Pool.Poolable {
    /**
     * The Damage.
     */
    public float damage;
    /**
     * The Spell damage.
     */
    public float spellDamage;

    /**
     * The Speed.
     */
    public float speed;
    /**
     * The Hp.
     */
    public float hp;
    /**
     * The Atk speed.
     */
    public float atkSpeed;

    /**
     * The Damage reduction.
     */
    public float damageReduction;

    /**
     * The Hp regen.
     */
    public float hpRegen;

    /**
     * The Life steal.
     */
    public float lifeSteal;

    /**
     * Resets the object for reuse.
     */
    @Override
    public void reset() {
        damage = 0;
        speed = 0;
        hp = 0;
        atkSpeed = 0;
        damageReduction = 0;
        hpRegen = 0;
        lifeSteal = 0;
        spellDamage =0;
    }
}
