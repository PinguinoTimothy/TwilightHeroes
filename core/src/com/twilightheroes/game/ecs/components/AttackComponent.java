package com.twilightheroes.game.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Fixture;

public class AttackComponent implements Component {
    public Fixture attackFixture;
    public float attackCooldown = 0f;

    public int attackFrame = 5;
    public boolean performAttack = false;
}
