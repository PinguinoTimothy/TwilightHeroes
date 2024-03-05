package com.twilightheroes.game.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Pool;

/**
 * The type Attack component.
 */
public class AttackComponent implements Component, Pool.Poolable {
    /**
     * The Attack fixture.
     */
    public Fixture attackFixture;
    /**
     * The Perform attack.
     */
    public boolean performAttack = false;


    /**
     * Resets the object for reuse.
     */
    @Override
    public void reset() {
        attackFixture = null;

    }
}
