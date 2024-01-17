package com.twilightheroes.game.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Pool;

public class EnemyComponent implements Component, Pool.Poolable {

    public static enum Type {
        Goblin,
        Nightborne
    }
    public boolean isDead = false;
    public float xPosCenter = -1;
    public boolean playerGoingLeft = false;
    public boolean viewingPlayer = false;

    public float viewDistance;
    public float attackDistance;
    public float attackCooldown;
    public int attackFrame;

    @Override
    public void reset() {

        isDead = false;

    }
}
