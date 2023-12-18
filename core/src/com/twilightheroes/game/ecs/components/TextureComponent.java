package com.twilightheroes.game.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class TextureComponent implements Component {
    public Sprite sprite = new Sprite();
    public boolean runningRight = false; // Nueva propiedad para la dirección
}
