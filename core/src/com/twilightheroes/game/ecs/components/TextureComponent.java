package com.twilightheroes.game.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;


public class TextureComponent implements Component , Pool.Poolable{
    public Sprite sprite = new Sprite();
    public boolean runningRight = false; // Nueva propiedad para la dirección

    @Override
    public void reset() {
    sprite = new Sprite();
    runningRight = false;
    }
}
