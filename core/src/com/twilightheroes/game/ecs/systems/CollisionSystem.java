package com.twilightheroes.game.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;
import com.twilightheroes.game.TwilightHeroes;
import com.twilightheroes.game.ecs.components.B2dBodyComponent;
import com.twilightheroes.game.ecs.components.CollisionComponent;
import com.twilightheroes.game.ecs.components.ExitComponent;
import com.twilightheroes.game.ecs.components.PlayerComponent;
import com.twilightheroes.game.ecs.components.TypeComponent;
import com.twilightheroes.game.screens.MainScreen;
import com.twilightheroes.game.tools.Collisions;
import com.twilightheroes.game.tools.Mappers;

public class CollisionSystem extends IteratingSystem {

    RenderingSystem renderingSystem;
    MainScreen screen;

    private Filter inmuneFilter = new Filter();

    public CollisionSystem(RenderingSystem renderingSystem, MainScreen screen) {
        // only need to worry about player collisions;
        super(Family.all(CollisionComponent.class).get());


        this.renderingSystem = renderingSystem;
        this.screen = screen;
        inmuneFilter.categoryBits = TwilightHeroes.INMUNE_BIT;

    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

            // get collision for this entity
            CollisionComponent cc = Mappers.collisionCom.get(entity);

            for (int i = cc.collisionEntities.size - 1; i >= 0; i--) {

                Collisions auxCollision = cc.collisionEntities.get(i);

                //get collided entity
                Entity collidedEntity = auxCollision.collisionEntity;
                boolean isHitbox = auxCollision.isAttackHitbox;


                TypeComponent thisType = Mappers.typeCom.get(entity);

                if (thisType.type == TypeComponent.PLAYER) {
                    if (collidedEntity != null) {
                        TypeComponent type = Mappers.typeCom.get(collidedEntity);
                        if (type != null) {
                            switch (type.type) {
                                case TypeComponent.ENEMY:
                                    //do player hit enemy thing


                                    if (isHitbox) {
                                        Mappers.enemyCom.get(collidedEntity).hp -= 25;
                                        if (Mappers.enemyCom.get(collidedEntity).hp <= 0) {
                                            Mappers.b2dCom.get(collidedEntity).isDead = true;

                                        }
                                    }


                                    break;
                                case TypeComponent.SCENERY:
                                    //do player hit scenery thing

                                    renderingSystem.updateRoom(Mappers.b2dCom.get(collidedEntity).width, Mappers.b2dCom.get(collidedEntity).height, Mappers.b2dCom.get(collidedEntity).startX, Mappers.b2dCom.get(collidedEntity).startY);
                                    break;
                                case TypeComponent.EXIT:

                                    if (!screen.change && screen.auxChangeMap == 0) {
                                        ExitComponent exitComponent = Mappers.exitCom.get(collidedEntity);

                                        screen.change = true;
                                        screen.newMap = exitComponent.exitToRoom;


                                    }

                                    break;
                                case TypeComponent.OTHER:
                                    //do player hit other thing
                                    break; //technically this isn't needed
                            }
                            cc.collisionEntities.removeIndex(i); // collision handled reset component

                        }
                    }
                } else if (thisType.type == TypeComponent.ENEMY) {
                    if (collidedEntity != null) {
                        TypeComponent type = Mappers.typeCom.get(collidedEntity);
                        if (type != null) {
                            switch (type.type) {
                                case TypeComponent.PLAYER:
                                    PlayerComponent player = Mappers.playerCom.get(collidedEntity);
                                    B2dBodyComponent bodyPlayer = Mappers.b2dCom.get(collidedEntity);
                                    B2dBodyComponent bodyEnemy = Mappers.b2dCom.get(entity);


                                    if (!isHitbox && !player.inmune) {
                                        System.out.println("enemy hit player");
                                        float xForce = 2.5f;
                                        if (bodyPlayer.body.getPosition().x < bodyEnemy.body.getPosition().x) {
                                            xForce = -2.5f;
                                        }
                                        bodyPlayer.body.getFixtureList().get(0).setFilterData(inmuneFilter);
                                        player.inmune = true;
                                        player.inmuneTime = 0.3f;
                                        bodyPlayer.body.setLinearVelocity(new Vector2(0f,0f));
                                        bodyPlayer.body.setLinearVelocity(new Vector2(xForce, 0f));
                                        player.knockback = true;
                                        player.knockBackTime = 0.5f;
                                    }
                                    break;
                            }
                            cc.collisionEntities.removeIndex(i); // collision handled reset component
                        }
                    }
                }



        }
    }
}
