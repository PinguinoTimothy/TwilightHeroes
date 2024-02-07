package com.twilightheroes.game.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.twilightheroes.game.TwilightHeroes;
import com.twilightheroes.game.ecs.components.B2dBodyComponent;
import com.twilightheroes.game.ecs.components.TextureComponent;
import com.twilightheroes.game.screens.MainScreen;
import com.twilightheroes.game.tools.Mappers;

import java.util.Map;

public class PhysicsSystem extends IteratingSystem {

    private World world;
    private Array<Entity> bodiesQueue;
    private PooledEngine engine;

    private MainScreen screen;

    public PhysicsSystem(World world, PooledEngine engine, MainScreen screen) {
        super(Family.all(B2dBodyComponent.class, TextureComponent.class).get());
        this.world = world;
        this.bodiesQueue = new Array<Entity>();
        this.engine = engine;
        this.screen = screen;
    }
    private Array<Entity> entitiesToRemove = new Array<Entity>();
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float frameTime = Math.min(deltaTime, 0.25f);
            world.step(1/61f, 6, 2);


            //Entity Queue
            for (Entity entity : bodiesQueue) {

                B2dBodyComponent bodyComp = Mappers.b2dCom.get(entity);
                TextureComponent textureComponent = Mappers.texCom.get(entity);


                Float offsetX = bodyComp.body.getPosition().x - textureComponent.sprite.getWidth() / 2;

                if (Mappers.playerCom.get(entity) != null) {
                    if (textureComponent.runningRight) {
                        offsetX += 5 / TwilightHeroes.PPM;
                    } else {
                        offsetX -= 5 / TwilightHeroes.PPM;
                    }
                }
                textureComponent.sprite.setPosition(
                       offsetX,
                        bodyComp.body.getPosition().y - textureComponent.sprite.getHeight() / 2
                );
                if (bodyComp.isDead){
                    entitiesToRemove.add(entity);
                }
            }

        for (Entity entity : entitiesToRemove) {
            B2dBodyComponent bodyComp = Mappers.b2dCom.get(entity);
            world.destroyBody(bodyComp.body);
            screen.bodies.removeValue(bodyComp.body, true);
            engine.removeEntity(entity);
        }

        entitiesToRemove.clear();


        bodiesQueue.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        bodiesQueue.add(entity);
    }
}
