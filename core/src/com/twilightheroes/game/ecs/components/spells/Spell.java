package com.twilightheroes.game.ecs.components.spells;

/**
 * The type Spell.
 */
public class Spell {

    /**
     * The Id.
     */
    public int id;
    /**
     * The Mana cost.
     */
    public float manaCost;
    /**
     * The Casting time.
     */
    public float castingTime;
    /**
     * The Duration.
     */
    public float duration;
    /**
     * The Value.
     */
    public float value;

    /**
     * The Vfx.
     */
    public SpellVFX vfx;

    /**
     * Instantiates a new Spell.
     *
     * @param id          the id
     * @param manaCost    the mana cost
     * @param castingTime the casting time
     * @param spellVFX    the spell vfx
     * @param duration    the duration
     * @param value       the value
     */
    public Spell(int id, float manaCost, float castingTime, SpellVFX spellVFX, float duration,float value) {

        this.id = id;
        this.manaCost = manaCost;
        this.castingTime = castingTime;
        this.vfx = spellVFX;
        this.duration =duration;
        this.value = value;

    }
}
