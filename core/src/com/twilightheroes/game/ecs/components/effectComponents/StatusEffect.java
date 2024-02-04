package com.twilightheroes.game.ecs.components.effectComponents;

import com.badlogic.ashley.core.Entity;

public class StatusEffect {

    public int type;
    public boolean buffOrDebuff;

    private float timeRemaining;
    private float value;
    private boolean used;
    public Entity particleEffect;

    public StatusEffect(int type, boolean buffOrDebuff, float timeRemaining, float value) {
        this.type = type;
        this.buffOrDebuff = buffOrDebuff;
        setTimeRemaining(timeRemaining);
        setValue(value);
    }


    public float getTimeRemaining() {
        return timeRemaining;
    }


    public void setTimeRemaining(float timeRemaining) {
        this.timeRemaining = timeRemaining;
    }


    public boolean getUsed() {
        return used;
    }


    public void setUsed(boolean used) {
        this.used = used;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
