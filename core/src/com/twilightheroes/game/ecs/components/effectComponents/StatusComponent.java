package com.twilightheroes.game.ecs.components.effectComponents;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

/**
 * Component of status effect
 */
public class StatusComponent implements Component, Pool.Poolable {
    /**
     * The Effects.
     */
    public final Array<StatusEffect> effects = new Array<>();


    /**
     * Resets the object for reuse.
     */
    @Override
    public void reset() {
        effects.clear();
    }
}
