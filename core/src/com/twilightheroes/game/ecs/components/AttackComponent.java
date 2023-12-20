package com.twilightheroes.game.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Fixture;

public class AttackComponent implements Component {
    public boolean isAttacking = false;
    public Fixture attackFixture;
}
