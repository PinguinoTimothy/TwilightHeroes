package com.twilightheroes.game.ecs.components;

import com.badlogic.ashley.core.Component;

public class EnemyComponent implements Component {
    public boolean isDead = false;
    public float xPosCenter = -1;
    public boolean isGoingLeft = false;
}
