package com.twilightheroes.game.ecs.components.effectComponents;

public interface EffectComponent {
    public float getTimeRemaining();

    public void setTimeRemaining(float timeRemaining);

    float timeRemaining = 0;

}
