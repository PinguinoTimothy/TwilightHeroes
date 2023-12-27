package com.twilightheroes.game.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.twilightheroes.game.tools.Collisions;

public class CollisionComponent implements Component {
    public Entity collisionEntity;
    public boolean isTouching;
    public boolean isAttackHitbox;

   public Array<Collisions> collisionEntities = new Array<>();
    }
