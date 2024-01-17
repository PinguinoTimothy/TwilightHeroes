package com.twilightheroes.game.ecs.components;

import com.badlogic.ashley.core.Component;

public class StatsComponent implements Component {
    public float damage;
    public float speed;
    public float hp;
    public float atkSpeed;

    public float damageReduction;

    public float hpRegen;

    public float lifeSteal;

}
