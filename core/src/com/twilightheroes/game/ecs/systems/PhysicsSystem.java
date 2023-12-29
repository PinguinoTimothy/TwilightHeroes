package com.twilightheroes.game.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.twilightheroes.game.ecs.components.B2dBodyComponent;
import com.twilightheroes.game.ecs.components.TextureComponent;
import com.twilightheroes.game.tools.Mappers;

import java.util.Map;

public class PhysicsSystem extends IteratingSystem {

    private World world;
    private Array<Entity> bodiesQueue;
    private PooledEngine engine;


    public PhysicsSystem(World world, PooledEngine engine) {
        super(Family.all(B2dBodyComponent.class, TextureComponent.class).get());
        this.world = world;
        this.bodiesQueue = new Array<Entity>();
        this.engine = engine;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float frameTime = Math.min(deltaTime, 0.25f);
            world.step(1/61f, 6, 2);


            //Entity Queue
            for (Entity entity : bodiesQueue) {

                B2dBodyComponent bodyComp = Mappers.b2dCom.get(entity);
                TextureComponent textureComponent = Mappers.texCom.get(entity);

                textureComponent.sprite.setPosition(
                        bodyComp.body.getPosition().x - textureComponent.sprite.getWidth() / 2,
                        bodyComp.body.getPosition().y - textureComponent.sprite.getHeight() / 2
                );
                if (bodyComp.isDead){
                    world.destroyBody(bodyComp.body);
                    engine.removeEntity(entity);
                }
            }

        bodiesQueue.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        bodiesQueue.add(entity);
    }
}
