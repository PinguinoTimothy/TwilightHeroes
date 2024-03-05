package com.twilightheroes.game.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.twilightheroes.game.TwilightHeroes;
import com.twilightheroes.game.ecs.components.B2dBodyComponent;
import com.twilightheroes.game.ecs.components.BulletComponent;
import com.twilightheroes.game.ecs.components.CollisionComponent;
import com.twilightheroes.game.ecs.components.DialogueComponent;
import com.twilightheroes.game.ecs.components.ExitComponent;
import com.twilightheroes.game.ecs.components.HazardComponent;
import com.twilightheroes.game.ecs.components.InteractiveObjectComponent;
import com.twilightheroes.game.ecs.components.PlayerComponent;
import com.twilightheroes.game.ecs.components.StatsComponent;
import com.twilightheroes.game.ecs.components.TypeComponent;
import com.twilightheroes.game.screens.MainScreen;
import com.twilightheroes.game.tools.Collisions;
import com.twilightheroes.game.tools.Mappers;
import com.twilightheroes.game.ecs.components.tipoObjetoInteractivo;


/**
 * The system that handles all the collisions between entities
 */
public class CollisionSystem extends IteratingSystem {

    /**
     * The filter that makes the player immune
     */
    private final Filter inmuneFilter = new Filter();
    /**
     * Json that contains the dialogues
     */
    private final JsonValue jsonText = new JsonReader().parse(Gdx.files.internal("config/dialogues.json"));
    /**
     * The Rendering system.
     */
    RenderingSystem renderingSystem;
    /**
     * The MainScreen.
     */
    MainScreen screen;

    /**
     * Instantiates a new Collision system.
     *
     * @param renderingSystem the rendering system
     * @param screen          the screen
     */
    public CollisionSystem(RenderingSystem renderingSystem, MainScreen screen) {
        // only need to worry about player collisions;
        super(Family.all(CollisionComponent.class).get());


        this.renderingSystem = renderingSystem;
        this.screen = screen;
        inmuneFilter.categoryBits = TwilightHeroes.INMUNE_BIT;

    }
    /**
     * This method is called on every entity on every update call of the EntitySystem.
     * @param entity The current Entity being processed
     * @param deltaTime The delta time between the last and current frame
     */
    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        // get collision for this entity
        CollisionComponent cc = Mappers.collisionCom.get(entity);

        for (int i = cc.collisionEntities.size - 1; i >= 0; i--) {

            Collisions auxCollision = cc.collisionEntities.get(i);

            //get collided entity
            Entity collidedEntity = auxCollision.collisionEntity;
            boolean isHitbox = auxCollision.isAttackHitbox;
            boolean isEnemyHitbox = auxCollision.isEnemyHitbox;
            boolean canBeReduced = auxCollision.canBeReduced;
            boolean isInteractHitbox = auxCollision.isInteractHitbox;
            boolean isSpell = auxCollision.isSpell;

            TypeComponent thisType = Mappers.typeCom.get(entity);

            if (thisType.type == TypeComponent.PLAYER) {
                if (collidedEntity != null) {
                    PlayerComponent player = Mappers.playerCom.get(entity);
                    StatsComponent playerStats = Mappers.statsCom.get(entity);
                    TypeComponent type = Mappers.typeCom.get(collidedEntity);
                    if (type != null) {
                        switch (type.type) {
                            case TypeComponent.ENEMY:
                                //do player hit enemy thing
                                StatsComponent enemyStats = Mappers.statsCom.get(collidedEntity);


                                if (isHitbox && !isEnemyHitbox) {
                                    if (isSpell){
                                        enemyStats.hp -= playerStats.spellDamage;

                                    }else {
                                        if (canBeReduced) {
                                            enemyStats.hp -= playerStats.damage - enemyStats.damageReduction;
                                        } else {
                                            enemyStats.hp -= playerStats.damage;
                                        }
                                        player.canDodge = true;
                                        playerStats.hp += playerStats.lifeSteal;
                                        player.mana += 10;
                                    }
                                }


                                break;
                            case TypeComponent.FLOOR:
                                //do player hit scenery thing


                                break;

                            case TypeComponent.HAZARD:
                                HazardComponent hazardComponent = Mappers.hazardCom.get(collidedEntity);
                                B2dBodyComponent bodyPlayer = Mappers.b2dCom.get(entity);
                                B2dBodyComponent bodyHazard = Mappers.b2dCom.get(collidedEntity);

                                if (hazardComponent.alive) {
                                    if (!isHitbox && !player.immune) {
                                        float xForce = 2.5f;
                                        if (bodyPlayer.body.getPosition().x < bodyHazard.body.getPosition().x) {
                                            xForce = -2.5f;
                                        }
                                        playerStats.hp -= hazardComponent.damage;


                                        bodyPlayer.body.getFixtureList().get(0).setFilterData(inmuneFilter);
                                        player.immune = true;
                                        player.immuneTime = 0.5f;
                                        bodyPlayer.body.setLinearVelocity(new Vector2(0f, 0f));
                                        bodyPlayer.body.setLinearVelocity(new Vector2(xForce, 0f));
                                        player.knockback = true;
                                        player.knockBackTime = 0.3f;
                                        if (screen.parent.vibratorOn) {
                                            Gdx.input.vibrate(500);
                                        }
                                    }
                                }
                                break;
                            case TypeComponent.EXIT:


                                if (!screen.change && screen.auxChangeMap == 0) {
                                    ExitComponent exitComponent = Mappers.exitCom.get(collidedEntity);

                                    if (exitComponent.exitToRoom != 4){
                                        screen.change = true;
                                        screen.newMap = exitComponent.exitToRoom;
                                        screen.oldMap = exitComponent.fromRoom;

                                    }else{
player.end = true;
                                    }


                                }


                                break;

                            case TypeComponent.WALL:
                                //do player hit other thing
                                break;
                            case TypeComponent.INTERACTABLE:
                                if (isInteractHitbox) {
                                    InteractiveObjectComponent interactiveObjectComponent = Mappers.interactiveCom.get(collidedEntity);
                                    switch (interactiveObjectComponent.tipoObjeto) {
                                        case tipoObjetoInteractivo.LEVER:
                                            screen.parent.playerSettings.doorsOpened[interactiveObjectComponent.id] = true;
                                            screen.b2WorldCreator.openDoors();
                                            break;
                                        case tipoObjetoInteractivo.DIALOGUE:
                                            DialogueComponent dialogueComponent = Mappers.dialogueCom.get(entity);
                                            dialogueComponent.active = true;

                                            JsonValue text = jsonText.get(interactiveObjectComponent.id);

                                            for (int j = 0; j < text.size; j++) {
                                                dialogueComponent.dialogueTexts.add(text.getString(j));
                                            }
                                            break;
                                        case tipoObjetoInteractivo.OBELISK:
                                            screen.parent.playerSettings.lastObelisk = interactiveObjectComponent.id;
                                            playerStats.hp = 100;
                                            player.mana = 100;
                                            break;
                                    }


                                }
                                break;
                        }
                        cc.collisionEntities.removeIndex(i); // collision handled reset component

                    }
                }
            } else if (thisType.type == TypeComponent.ENEMY) {
                if (collidedEntity != null) {
                    TypeComponent type = Mappers.typeCom.get(collidedEntity);
                    if (type != null) {
                        if (type.type == TypeComponent.PLAYER) {
                            PlayerComponent player = Mappers.playerCom.get(collidedEntity);
                            B2dBodyComponent bodyPlayer = Mappers.b2dCom.get(collidedEntity);
                            B2dBodyComponent bodyEnemy = Mappers.b2dCom.get(entity);
                            StatsComponent playerStats = Mappers.statsCom.get(collidedEntity);
                            StatsComponent enemyStats = Mappers.statsCom.get(entity);
                            if (!isHitbox && !player.immune) {
                                System.out.println("enemy hit player");
                                float xForce = 2.5f;
                                if (bodyPlayer.body.getPosition().x < bodyEnemy.body.getPosition().x) {
                                    xForce = -2.5f;
                                }
                                if (isSpell){
                                    playerStats.hp -= enemyStats.spellDamage;
                                }else{
                                    playerStats.hp -= enemyStats.damage;
                                }


                                bodyPlayer.body.getFixtureList().get(0).setFilterData(inmuneFilter);
                                player.immune = true;
                                player.immuneTime = 0.5f;
                                bodyPlayer.body.setLinearVelocity(new Vector2(0f, 0f));
                                bodyPlayer.body.setLinearVelocity(new Vector2(xForce, 0f));
                                player.knockback = true;
                                player.knockBackTime = 0.3f;
                                if (screen.parent.vibratorOn) {
                                    Gdx.input.vibrate(500);
                                }
                            }
                        }
                        cc.collisionEntities.removeIndex(i); // collision handled reset component
                    }
                }
            } else if (thisType.type == TypeComponent.BULLET) {
                if (collidedEntity != null) {
                    TypeComponent type = Mappers.typeCom.get(collidedEntity);
                    BulletComponent bullet = Mappers.bullCom.get(entity);
                    if (!bullet.isDead) {
                        if (type != null) {
                            switch (type.type) {
                                case TypeComponent.PLAYER:
                                    if (bullet.owner != BulletComponent.Owner.PLAYER) {

                                        PlayerComponent player = Mappers.playerCom.get(collidedEntity);
                                        B2dBodyComponent bodyPlayer = Mappers.b2dCom.get(collidedEntity);
                                        StatsComponent playerStats = Mappers.statsCom.get(collidedEntity);

                                        if (!player.immune && !isHitbox) {

                                            playerStats.hp -= bullet.damage;
                                            bullet.isDead = true;

                                            bodyPlayer.body.getFixtureList().get(0).setFilterData(inmuneFilter);
                                            player.immune = true;
                                            player.immuneTime = 0.5f;
                                            // bodyPlayer.body.setLinearVelocity(new Vector2(0f, 0f));
                                            // bodyPlayer.body.setLinearVelocity(new Vector2(xForce, 0f));
                                            // player.knockback = true;
                                            // player.knockBackTime = 0.3f;
                                            if (screen.parent.vibratorOn) {
                                                Gdx.input.vibrate(500);
                                            }
                                        }
                                    }
                                    break;
                                case TypeComponent.ENEMY:
                                    if (bullet.owner != BulletComponent.Owner.ENEMY) {
                                        //do player hit enemy thing
                                        StatsComponent enemyStats = Mappers.statsCom.get(collidedEntity);


                                        if (!isEnemyHitbox) {
                                            if (canBeReduced) {
                                                enemyStats.hp -= bullet.damage - enemyStats.damageReduction;
                                            } else {
                                                enemyStats.hp -= bullet.damage;
                                            }
                                            bullet.isDead = true;

                                        }
                                    }


                                    break;

                            }
                        }
                    }
                    cc.collisionEntities.removeIndex(i); // collision handled reset component
                }
            }


        }
    }


}
