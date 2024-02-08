package com.twilightheroes.game.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Pool;

public class AttackComponent implements Component, Pool.Poolable {
    public Fixture attackFixture;

    public boolean performAttack = false;

    @Override
    public void reset() {
        attackFixture = null;

    }
}
