package com.twilightheroes.game.ecs.components.spells;

public class Spell {

    public int id;
    public float manaCost;
    public float castingTime;
    public SpellVFX vfx;

    public Spell(int id, float manaCost, float castingTime, SpellVFX spellVFX) {

        this.id = id;
        this.manaCost = manaCost;
        this.castingTime = castingTime;
        this.vfx = spellVFX;

    }
}
