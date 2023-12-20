package com.twilightheroes.game.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;

public class B2dBodyComponent implements Component {
    public Body body;
    public float width;
    public float height;
    public float startX;
    public float startY;

}
