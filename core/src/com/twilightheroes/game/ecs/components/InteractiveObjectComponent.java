package com.twilightheroes.game.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;


public class InteractiveObjectComponent implements Component, Pool.Poolable {


    public int id;
    public int tipoObjeto;

    @Override
    public void reset() {

        id = -1;
        tipoObjeto = -1;
    }
}
