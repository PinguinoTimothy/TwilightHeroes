package com.twilightheroes.game.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.Pool;

/**
 * The type Animation component.
 */
public class AnimationComponent implements Component, Pool.Poolable {
    /**
     * The Animations.
     */
    public IntMap<Animation<TextureRegion>> animations = new IntMap<>();

    /**
     * The Current frame.
     */
    public int currentFrame = 0;



    /**
     * Resets the object for reuse.
     */
    @Override
    public void reset() {
        animations.clear();
        currentFrame = 0;
    }
}
