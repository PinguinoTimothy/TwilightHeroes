package com.twilightheroes.game.ecs.components.spells;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class SpellComponent implements Component, Pool.Poolable {

    public Spell spell1;
    public Spell spell2;

    public Spell spellToCast = null;

    public boolean casting = false;
    public float castingTime = 0f;


    @Override
    public void reset() {
        spell1 = null;
        spell2 = null;
        spellToCast = null;
        casting = false;
        castingTime = 0f;
    }
}
