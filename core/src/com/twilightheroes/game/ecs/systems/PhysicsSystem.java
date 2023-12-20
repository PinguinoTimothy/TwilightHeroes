package com.twilightheroes.game.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.twilightheroes.game.ecs.components.B2dBodyComponent;

public class PhysicsSystem extends IteratingSystem {

    private World world;
    private Array<Entity> bodiesQueue;

    private ComponentMapper<B2dBodyComponent> bm = ComponentMapper.getFor(B2dBodyComponent.class);

    public PhysicsSystem(World world) {
        super(Family.all(B2dBodyComponent.class).get());
        this.world = world;
        this.bodiesQueue = new Array<Entity>();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float frameTime = Math.min(deltaTime, 0.25f);
            world.step(1/60f, 6, 2);


            //Entity Queue
            for (Entity entity : bodiesQueue) {

                B2dBodyComponent bodyComp = bm.get(entity);

            }

        bodiesQueue.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        bodiesQueue.add(entity);
    }
}
