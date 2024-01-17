package com.twilightheroes.game.ecs.components.effectComponents;

import com.badlogic.ashley.core.Component;

public class DamageBuffComponent implements Component, EffectComponent {

    private float timeRemaining;
    private float damageBuff;
    public DamageBuffComponent(float timeRemaining, float damageBuff) {
        setTimeRemaining(timeRemaining);
        setDamageBuff(damageBuff);
    }

    @Override
    public float getTimeRemaining() {
        return timeRemaining;
    }

    @Override
    public void setTimeRemaining(float timeRemaining) {
        this.timeRemaining = timeRemaining;
    }

    public float getDamageBuff() {
        return damageBuff;
    }

    public void setDamageBuff(float damageBuff) {
        this.damageBuff = damageBuff;
    }


}
