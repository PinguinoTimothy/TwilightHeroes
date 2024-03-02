package com.twilightheroes.game.ecs.components.effectComponents;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class StatusComponent implements Component, Pool.Poolable {
    public Array<StatusEffect> effects = new Array<>();

    
    @Override
    public void reset() {
        effects.clear();
    }
}
