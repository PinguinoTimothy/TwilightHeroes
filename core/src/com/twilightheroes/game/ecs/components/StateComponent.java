package com.twilightheroes.game.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * The type State component.
 */
public class StateComponent implements Component, Pool.Poolable {
    /**
     * The constant STATE_IDLE.
     */
    public static final int STATE_IDLE = 0;
    /**
     * The constant STATE_JUMPING.
     */
    public static final int STATE_JUMPING = 1;
    /**
     * The constant STATE_FALLING.
     */
    public static final int STATE_FALLING = 2;
    /**
     * The constant STATE_MOVING.
     */
    public static final int STATE_MOVING = 3;

    /**
     * The constant STATE_HIT.
     */
    public static final int STATE_HIT = 4;
    /**
     * The constant STATE_ATTACK01.
     */
    public static final int STATE_ATTACK01 = 5;
    /**
     * The constant STATE_ATTACK02.
     */
    public static final int STATE_ATTACK02 = 6;
    /**
     * The constant STATE_ATTACK03.
     */
    public static final int STATE_ATTACK03 = 7;
    /**
     * The constant STATE_DAMAGED.
     */
    public static final int STATE_DAMAGED = 8;

    /**
     * The constant STATE_ENEMY_ATTACK.
     */
    public static final int STATE_ENEMY_ATTACK = 9;
    /**
     * The constant STATE_CHASING.
     */
    public static final int STATE_CHASING = 10;
    /**
     * The constant STATE_DODGING.
     */
    public static final int STATE_DODGING = 11;
    /**
     * The constant STATE_HABILIDAD1.
     */
    public static final int STATE_HABILIDAD1 = 12;
    /**
     * The constant STATE_SPELL_STARTING.
     */
    public static final int STATE_SPELL_STARTING = 13;
    /**
     * The constant STATE_SPELL_GOING.
     */
    public static final int STATE_SPELL_GOING = 14;
    /**
     * The constant STATE_SPELL_ENDING.
     */
    public static final int STATE_SPELL_ENDING = 15;
    /**
     * The constant STATE_CASTING.
     */
    public static final int STATE_CASTING = 16;
    /**
     * The constant STATE_VFX.
     */
    public static final int STATE_VFX = 17;
    /**
     * The constant STATE_OBELISK_IDLE.
     */
    public static final int STATE_OBELISK_IDLE = 18;
    /**
     * The constant STATE_OBELISK_ANI.
     */
    public static final int STATE_OBELISK_ANI = 19;

    /**
     * The Previous state.
     */
    public int previousState = 0;
    /**
     * The Time in the given state.
     */
    public float time = 0.0f;
    /**
     * if the state is looping.
     */
    public boolean isLooping = true;
    /**
     * current state
     */
    private int state = 0;

    /**
     * Set the state.
     *
     * @param newState the new state
     */
    public void set(int newState) {
        previousState = state;
        state = newState;


    }

    /**
     * Get state.
     *
     * @return the state
     */
    public int get() {
        return state;
    }

    /**
     * Resets the object for reuse.
     */
    @Override
    public void reset() {

    }
}
