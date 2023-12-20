package com.twilightheroes.game.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.twilightheroes.game.ecs.components.B2dBodyComponent;
import com.twilightheroes.game.ecs.components.CollisionComponent;
import com.twilightheroes.game.ecs.components.PlayerComponent;
import com.twilightheroes.game.ecs.components.TypeComponent;
import com.twilightheroes.game.tools.B2dContactListener;

public class CollisionSystem extends IteratingSystem {
    ComponentMapper<CollisionComponent> cm;
    ComponentMapper<PlayerComponent> pm;
    RenderingSystem renderingSystem;

    public CollisionSystem(RenderingSystem renderingSystem) {
        // only need to worry about player collisions
        super(Family.all(CollisionComponent.class,PlayerComponent.class).get());

        cm = ComponentMapper.getFor(CollisionComponent.class);
        pm = ComponentMapper.getFor(PlayerComponent.class);
        this.renderingSystem = renderingSystem;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        // get player collision component
        CollisionComponent cc = cm.get(entity);

        Entity collidedEntity = cc.collisionEntity;
        if(collidedEntity != null){
            TypeComponent type = collidedEntity.getComponent(TypeComponent.class);
            if(type != null){
                switch(type.type){
                    case TypeComponent.ENEMY:
                        //do player hit enemy thing
                        System.out.println("player hit enemy");
                        break;
                    case TypeComponent.SCENERY:
                        //do player hit scenery thing
                        System.out.println("player switch scenery");
                        ComponentMapper<B2dBodyComponent> bm;
                        bm = ComponentMapper.getFor(B2dBodyComponent.class);

                        renderingSystem.updateRoom(bm.get(collidedEntity).width,bm.get(collidedEntity).height,bm.get(collidedEntity).startX,bm.get(collidedEntity).startY);
                        break;
                    case TypeComponent.OTHER:
                        //do player hit other thing
                        System.out.println("player hit other");
                        break; //technically this isn't needed
                }
                cc.collisionEntity = null; // collision handled reset component
            }
        }

    }

}
