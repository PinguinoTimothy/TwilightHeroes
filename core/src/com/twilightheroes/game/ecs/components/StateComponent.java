package com.twilightheroes.game.ecs.components;

import com.badlogic.ashley.core.Component;

public class StateComponent implements Component {
    public static final int STATE_NORMAL = 0;
    public static final int STATE_JUMPING = 1;
    public static final int STATE_FALLING = 2;
    public static final int STATE_MOVING = 3;
    public static final int STATE_HIT = 4;
    public static final int STATE_ATTACK = 5;


    private int state = 0;
    public int previousState = 0;

    public float time = 0.0f;
    public boolean isLooping = false;


    public void set(int newState){
        previousState = state;
        state = newState;


    }

    public int get(){
        return state;
    }
}
