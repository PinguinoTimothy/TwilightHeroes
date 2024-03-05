package com.twilightheroes.game.ecs.components.spells;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * The type Spell component.
 */
public class SpellComponent implements Component, Pool.Poolable {

    /**
     * The Spell 1.
     */
    public Spell spell1;
    /**
     * The Spell 2.
     */
    public Spell spell2;

    /**
     * The Spell to cast.
     */
    public Spell spellToCast = null;

    /**
     * The Casting.
     */
    public boolean casting = false;
    /**
     * The Casting time.
     */
    public float castingTime = 0f;
    /**
     * The Duration.
     */
    public float duration = 0f;
    /**
     * The Value.
     */
    public float value = 0f;



    /**
     * Resets the object for reuse.
     */
    @Override
    public void reset() {
        spell1 = null;
        spell2 = null;
        spellToCast = null;
        casting = false;
        castingTime = 0f;
        duration = 0f;
        value = 0f;
    }
}
