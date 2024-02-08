package com.twilightheroes.game.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class StateComponent implements Component , Pool.Poolable{
    public static final int STATE_IDLE = 0;
    public static final int STATE_JUMPING = 1;
    public static final int STATE_FALLING = 2;
    public static final int STATE_MOVING = 3;

    public static final int STATE_HIT = 4;
    public static final int STATE_ATTACK01 = 5;
    public static final int STATE_ATTACK02 = 6;
    public static final int STATE_ATTACK03 = 7;
    public static final int STATE_DAMAGED = 8;

    public static final int STATE_ENEMY_ATTACK = 9;
    public static final int STATE_CHASING = 10;
    public static final int STATE_DODGING = 11;
    public static final int STATE_HABILIDAD1 = 12;
    public static final int STATE_SPELL_STARTING = 13;
    public static final int STATE_SPELL_GOING = 14;
    public static final int STATE_SPELL_ENDING = 15;
    public static final int STATE_CASTING = 16;
    public static final int STATE_VFX = 17;





    private int state = 0;
    public int previousState = 0;

    public float time = 0.0f;
    public boolean isLooping = true;


    public void set(int newState){
        previousState = state;
        state = newState;


    }

    public int get(){
        return state;
    }

    @Override
    public void reset() {

    }
}
