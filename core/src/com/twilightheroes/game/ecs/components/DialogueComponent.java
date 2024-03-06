package com.twilightheroes.game.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

/**
 * The type Dialogue component.
 */
public class DialogueComponent implements Component, Pool.Poolable {


    /**
     * The Dialogue texts.
     */
    public final Array<String> dialogueTexts = new Array<>();
    /**
     * If the dialogue is active.
     */
    public boolean active = false;


    /**
     * Resets the object for reuse.
     */
    @Override
    public void reset() {
        active = false;
    }
}


