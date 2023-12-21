package com.twilightheroes.game.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.twilightheroes.game.ecs.components.B2dBodyComponent;
import com.twilightheroes.game.ecs.components.CollisionComponent;
import com.twilightheroes.game.ecs.components.EnemyComponent;
import com.twilightheroes.game.ecs.components.ExitComponent;
import com.twilightheroes.game.ecs.components.PlayerComponent;
import com.twilightheroes.game.ecs.components.TextureComponent;
import com.twilightheroes.game.ecs.components.TypeComponent;
import com.twilightheroes.game.screens.MainScreen;
import com.twilightheroes.game.tools.B2dContactListener;

public class CollisionSystem extends IteratingSystem {
    ComponentMapper<CollisionComponent> cm;
    ComponentMapper<PlayerComponent> pm;
    RenderingSystem renderingSystem;
    MainScreen screen;

    public CollisionSystem(RenderingSystem renderingSystem, MainScreen screen) {
        // only need to worry about player collisions;
        super(Family.all(CollisionComponent.class).get());

        cm = ComponentMapper.getFor(CollisionComponent.class);
        pm = ComponentMapper.getFor(PlayerComponent.class);
        this.renderingSystem = renderingSystem;
        this.screen = screen;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        // get collision for this entity
        CollisionComponent cc = cm.get(entity);
        //get collided entity
        Entity collidedEntity = cc.collisionEntity;

        TypeComponent thisType = entity.getComponent(TypeComponent.class);

        if (thisType.type == TypeComponent.PLAYER) {
            if (collidedEntity != null) {
                TypeComponent type = collidedEntity.getComponent(TypeComponent.class);
                if (type != null) {
                    switch (type.type) {
                        case TypeComponent.ENEMY:
                            //do player hit enemy thing

                            PlayerComponent player = pm.get(entity);

                            if (cc.isTouching) {
                                player.enemigosEnRango.add(collidedEntity);
                                System.out.println("player hit enemy");
                            } else {
                                player.enemigosEnRango.removeValue(collidedEntity, true);
                                System.out.println("player no longer hits enemy");
                            }


                            break;
                        case TypeComponent.SCENERY:
                            //do player hit scenery thing
                            System.out.println("player switch scenery");
                            ComponentMapper<B2dBodyComponent> bm;
                            bm = ComponentMapper.getFor(B2dBodyComponent.class);
                            renderingSystem.updateRoom(bm.get(collidedEntity).width, bm.get(collidedEntity).height, bm.get(collidedEntity).startX, bm.get(collidedEntity).startY);
                            break;
                        case TypeComponent.EXIT:
                            ComponentMapper<ExitComponent> ex;
                            ex = ComponentMapper.getFor(ExitComponent.class);
                            ExitComponent exitComponent = ex.get(collidedEntity);
                            screen.change = true;
                            screen.newMap = (exitComponent.exitToRoom);
                            break;
                        case TypeComponent.OTHER:
                            //do player hit other thing
                            System.out.println("player hit other");
                            break; //technically this isn't needed
                    }
                    cc.collisionEntity = null; // collision handled reset component
                }
            }
        } else if (thisType.type == TypeComponent.ENEMY) {
            if (collidedEntity != null) {
                TypeComponent type = collidedEntity.getComponent(TypeComponent.class);
                if (type != null) {
                    switch (type.type) {
                        case TypeComponent.PLAYER:
                            System.out.println("enemy hit player");
                            PlayerComponent player = pm.get(collidedEntity);
                            B2dBodyComponent body = collidedEntity.getComponent(B2dBodyComponent.class);
                            TextureComponent text = entity.getComponent(TextureComponent.class);
                           CollisionComponent cca = cm.get(collidedEntity);
                            if (!cca.isAttackHitbox) {
                                float xForce = 1f;
                                if (!text.runningRight) {
                                    xForce *= -1;
                                }
                                body.body.applyLinearImpulse(new Vector2(xForce, 0.6f), body.body.getWorldCenter(), true);
                                player.knockback = true;
                            }
                            break;
                    }
                    cc.collisionEntity = null; // collision handled reset component
                } else {
                    System.out.println("Enemy: collidedEntity.type == null");
                }
            }

        }

    }
}
