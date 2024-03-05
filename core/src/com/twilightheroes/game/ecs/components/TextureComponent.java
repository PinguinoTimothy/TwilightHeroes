package com.twilightheroes.game.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Pool;


/**
 * The type Texture component.
 */
public class TextureComponent implements Component, Pool.Poolable {
    /**
     * The Sprite.
     */
    public Sprite sprite = new Sprite();
    /**
     * If is running right or left.
     */
    public boolean runningRight = false; // Nueva propiedad para la dirección

    /**
     * Resets the object for reuse.
     */
    @Override
    public void reset() {
        sprite = new Sprite();
        runningRight = false;
    }
}
