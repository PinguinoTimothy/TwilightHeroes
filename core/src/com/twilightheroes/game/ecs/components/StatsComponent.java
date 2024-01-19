package com.twilightheroes.game.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class StatsComponent implements Component, Pool.Poolable {
    public float damage;
    public float speed;
    public float hp;
    public float atkSpeed;

    public float damageReduction;

    public float hpRegen;

    public float lifeSteal;

    @Override
    public void reset() {
        hp = 100;
    }
}
