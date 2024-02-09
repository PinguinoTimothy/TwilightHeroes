package com.twilightheroes.game.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class AttackComponent implements Component, Pool.Poolable {
    public Array<Fixture> attackFixtures = new Array<>();
    public Array<Float> lifetimes = new Array<>();  // Almacena el tiempo de vida de cada fixture
    public boolean performAttack = false;

    @Override
    public void reset() {
        attackFixtures.clear();

    }
}
