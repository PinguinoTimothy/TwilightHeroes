package com.twilightheroes.game.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class DialogueComponent implements Component, Pool.Poolable {


    public Array<String> dialogueTexts = new Array<>();
    public boolean active = false;
    public boolean touchDown = false;



    @Override
    public void reset() {
        active = false;
        touchDown = false;
    }
}


