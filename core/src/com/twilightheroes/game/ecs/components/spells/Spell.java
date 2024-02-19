package com.twilightheroes.game.ecs.components.spells;

public class Spell {

    public int id;
    public float manaCost;
    public float castingTime;
    public float duration;
    public float value;

    public SpellVFX vfx;

    public Spell(int id, float manaCost, float castingTime, SpellVFX spellVFX, float duration,float value) {

        this.id = id;
        this.manaCost = manaCost;
        this.castingTime = castingTime;
        this.vfx = spellVFX;
        this.duration =duration;
        this.value = value;

    }
}
