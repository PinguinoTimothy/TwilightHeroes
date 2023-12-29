package com.twilightheroes.game.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Fixture;

public class EnemyComponent implements Component {
    public boolean isDead = false;
    public float xPosCenter = -1;
    public boolean playerGoingLeft = false;
    public boolean viewingPlayer = false;

    public float viewDistance = 12.5f;
    public float attackDistance = 2.5f;



}
