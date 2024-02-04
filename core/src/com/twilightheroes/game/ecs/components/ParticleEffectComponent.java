package com.twilightheroes.game.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool;

public class ParticleEffectComponent implements Component, Pool.Poolable {


    public ParticleEffectPool.PooledEffect particleEffect;
    public boolean isattached = false;
    public float xOffset = 0;
    public float yOffset = 0;

    public boolean isDead = false;
    public Body attachedBody;

    @Override
    public void reset() {
        particleEffect.free(); // free the pooled effect
        particleEffect = null; // empty this component's particle effect
        xOffset = 0;
        yOffset = 0;
        isattached = false;
        isDead = false;
        attachedBody = null;
    }

}
