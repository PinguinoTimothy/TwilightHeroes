package com.twilightheroes.game.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.twilightheroes.game.tools.Collisions;

public class CollisionComponent implements Component, Pool.Poolable {
    public Entity collisionEntity;

   public Array<Collisions> collisionEntities = new Array<>();

    @Override
    public void reset() {
        collisionEntity = null;

    }
    }
