package com.twilightheroes.game.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.twilightheroes.game.TwilightHeroes;
import com.twilightheroes.game.ecs.components.B2dBodyComponent;
import com.twilightheroes.game.ecs.components.TextureComponent;
import com.twilightheroes.game.screens.MainScreen;
import com.twilightheroes.game.tools.Mappers;

/**
 * The system that handles the physics of the world
 */
public class PhysicsSystem extends IteratingSystem {

    /**
     * The world
     */
    private final World world;
    /**
     * The bodies waiting to process
     */
    private final Array<Entity> bodiesQueue;
    /**
     * the engine
     */
    private final PooledEngine engine;

    /**
     * the Mainscreen
     */
    private final MainScreen screen;
    /**
     * Entities to remove from engine
     */
    private final Array<Entity> entitiesToRemove = new Array<>();

    /**
     * Instantiates a new Physics system.
     *
     * @param world  the world
     * @param engine the engine
     * @param screen the MainScreen
     */
    public PhysicsSystem(World world, PooledEngine engine, MainScreen screen) {
        super(Family.all(B2dBodyComponent.class, TextureComponent.class).get());
        this.world = world;
        this.bodiesQueue = new Array<>();
        this.engine = engine;
        this.screen = screen;
    }

    /**
     * Update the system
     *
     * @param deltaTime The time passed since last frame in seconds.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        world.step(1 / 61f, 6, 2);


        //Entity Queue
        for (Entity entity : bodiesQueue) {

            B2dBodyComponent bodyComp = Mappers.b2dCom.get(entity);
            TextureComponent textureComponent = Mappers.texCom.get(entity);


            float offsetX = bodyComp.body.getPosition().x - textureComponent.sprite.getWidth() / 2;

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
            if (bodyComp.isDead) {
                screen.parent.assMan.manager.get("sfx/enemyDeath.wav", Sound.class).play();
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

    /**
     * This method is called on every entity on every update call of the EntitySystem.
     *
     * @param entity    The current Entity being processed
     * @param deltaTime The delta time between the last and current frame
     */
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        bodiesQueue.add(entity);
    }
}
