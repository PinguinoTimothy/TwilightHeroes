package com.twilightheroes.game.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.twilightheroes.game.TwilightHeroes;
import com.twilightheroes.game.ecs.components.AnimationComponent;
import com.twilightheroes.game.ecs.components.AttackComponent;
import com.twilightheroes.game.ecs.components.B2dBodyComponent;
import com.twilightheroes.game.ecs.components.BulletComponent;
import com.twilightheroes.game.ecs.components.EnemyComponent;
import com.twilightheroes.game.ecs.components.StateComponent;
import com.twilightheroes.game.ecs.components.StatsComponent;
import com.twilightheroes.game.ecs.components.TextureComponent;
import com.twilightheroes.game.ecs.components.spells.SpellComponent;
import com.twilightheroes.game.screens.MainScreen;
import com.twilightheroes.game.tools.KillCounter;
import com.twilightheroes.game.tools.Mappers;

import java.util.Random;

public class EnemySystem extends IteratingSystem {


    private final MainScreen screen;

    public EnemySystem(MainScreen screen) {
        super(Family.all(EnemyComponent.class).get());
        this.screen = screen;
    }

    private Vector2 calculateDistance(Entity enemigo) {
        Vector2 distanceVector = new Vector2();

        if (screen.playerEntity != null) {
            B2dBodyComponent playerBody = screen.playerEntity.getComponent(B2dBodyComponent.class);
            B2dBodyComponent enemigoBody = enemigo.getComponent(B2dBodyComponent.class);

            if (enemigoBody != null && playerBody != null) {
                float x1 = playerBody.body.getPosition().x;
                float y1 = playerBody.body.getPosition().y;

                float x2 = enemigoBody.body.getPosition().x;
                float y2 = enemigoBody.body.getPosition().y;

                distanceVector.set(x2 - x1, y2 - y1);
            }
        }

        return distanceVector;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        EnemyComponent enemyCom = Mappers.enemyCom.get(entity);        // get EnemyComponent
        B2dBodyComponent bodyCom = Mappers.b2dCom.get(entity);    // get B2dBodyComponent
        StateComponent enemyStateComponent = Mappers.stateCom.get(entity);
        TextureComponent textureComponent = Mappers.texCom.get(entity);
        AttackComponent attackComponent = Mappers.atkCom.get(entity);
        AnimationComponent animationComponent = Mappers.animCom.get(entity);
        StatsComponent statsComponent = Mappers.statsCom.get(entity);
        SpellComponent spellComponent = Mappers.spellCom.get(entity);

        if (statsComponent.hp <= 0) {
            bodyCom.isDead = true;

            boolean enemyRegistered = false;
            for (KillCounter kc : screen.parent.playerSettings.killCounter) {
                if (kc.enemyName.equals(enemyCom.name)) {
                    kc.killCount++;
                    enemyRegistered = true;
                }
            }

            if (!enemyRegistered) {
                KillCounter killCounter = new KillCounter();
                killCounter.enemyName = enemyCom.name;
                killCounter.killCount = 1;
                screen.parent.playerSettings.killCounter.add(killCounter);
            }

        } else {


            Vector2 distanceToPlayer = calculateDistance(entity);


                for (int i = 0; i < attackComponent.lifetimes.size; i++) {
                    float lifetime = attackComponent.lifetimes.get(i);
                    lifetime += deltaTime;
                    attackComponent.lifetimes.set(i, lifetime);

                    if (lifetime >= 0.1f) {
                        Fixture atkFix = attackComponent.attackFixtures.get(i);
                        bodyCom.body.destroyFixture(atkFix);
                        attackComponent.attackFixtures.removeIndex(i);
                        attackComponent.lifetimes.removeIndex(i);
                        i--;  // Ajusta el índice después de eliminar un elemento
                    }
                }


            if (enemyStateComponent.get() == StateComponent.STATE_ENEMY_ATTACK && !animationComponent.animations.get(enemyStateComponent.get()).isAnimationFinished(enemyStateComponent.time)) {
                if (animationComponent.currentFrame == enemyCom.attackFrame && attackComponent.performAttack) {
                    switch (enemyCom.attackMethod) {
                        case "melee":
                            createAttackFixture(textureComponent, bodyCom, attackComponent);
                            break;
                        case "range":
                            float xVel = textureComponent.runningRight ? 1f : -1f;

                            screen.b2WorldCreator.createBullet(bodyCom.body.getPosition().x, bodyCom.body.getPosition().y, xVel, BulletComponent.Owner.ENEMY, textureComponent.runningRight, statsComponent.damage, "frostSpear");
                            break;
                        case "spellCaster":
                            spellComponent.spellToCast = enemyCom.spells[new Random().nextInt(enemyCom.spells.length)];
                            break;
                    }
                    attackComponent.performAttack = false;

                }
            } else {
                float auxDistance = Math.abs(distanceToPlayer.x * 10);
                if (Math.abs(distanceToPlayer.y) < 10) {
                    if (auxDistance >= 0 && auxDistance <= enemyCom.attackDistance && Math.abs(distanceToPlayer.y) < 0.5) {
                        enemyStateComponent.set(StateComponent.STATE_ENEMY_ATTACK);
                    } else if (auxDistance > 0 && auxDistance <= enemyCom.viewDistance) {
                        enemyStateComponent.set(StateComponent.STATE_CHASING);
                    } else {
                        enemyStateComponent.set(StateComponent.STATE_IDLE);
                    }
                }
                //enemyStateComponent.set(StateComponent.STATE_IDLE);
                switch (enemyStateComponent.get()) {

                    case StateComponent.STATE_IDLE:

                        break;

                    case StateComponent.STATE_CHASING:
                        if (distanceToPlayer.x > 0.001f || distanceToPlayer.x < -0.001f) {


                            float speed = distanceToPlayer.x < 0 ? statsComponent.speed : -statsComponent.speed;
                            bodyCom.body.setLinearVelocity(new Vector2(speed, bodyCom.body.getLinearVelocity().y));
                        } else {
                            enemyStateComponent.set(StateComponent.STATE_IDLE);

                        }
                        break;

                    case StateComponent.STATE_ENEMY_ATTACK:
                        if (enemyStateComponent.get() == StateComponent.STATE_ENEMY_ATTACK && !animationComponent.animations.get(enemyStateComponent.get()).isAnimationFinished(enemyStateComponent.time)) {
                            if (animationComponent.currentFrame == enemyCom.attackFrame && attackComponent.performAttack) {

                                createAttackFixture(textureComponent, bodyCom, attackComponent);
                                attackComponent.performAttack = false;
                            }
                        } else {

                            if (enemyCom.attackCooldown <= 0) {
                                enemyCom.attackCooldown = 1.5f;
                                enemyStateComponent.time = 0f;
                                attackComponent.performAttack = true;
                            } else {
                                enemyCom.attackCooldown -= deltaTime;
                                enemyStateComponent.set(StateComponent.STATE_IDLE);

                            }
                        }

                        break;
                }

                if (distanceToPlayer.x < 0 && !textureComponent.runningRight) {
                    textureComponent.runningRight = true;

                } else if (distanceToPlayer.x > 0 && textureComponent.runningRight) {
                    textureComponent.runningRight = false;

                }
            }

        }
    }


    private void createAttackFixture(TextureComponent texture, B2dBodyComponent b2dbody, AttackComponent attackComponent) {
        PolygonShape attackShape = new PolygonShape();
        float offsetX = texture.runningRight ? 16 / TwilightHeroes.PPM : -16 / TwilightHeroes.PPM;
        attackShape.setAsBox(16 / TwilightHeroes.PPM, 8 / TwilightHeroes.PPM, new Vector2(offsetX, 0), 0);
        FixtureDef attackFixtureDef = new FixtureDef();
        attackFixtureDef.shape = attackShape;
        attackFixtureDef.isSensor = true; // Configurar la fixture como un sensor
        Fixture fix = b2dbody.body.createFixture(attackFixtureDef);
        fix.setUserData("enemyAttackSensor");
        attackComponent.attackFixtures.add(fix);
        attackComponent.lifetimes.add(0f);
        // Liberar los recursos del shape
        attackShape.dispose();
        b2dbody.body.applyForce(new Vector2((texture.runningRight ? 1f : -1f), 0f), b2dbody.body.getWorldCenter(), true);


    }
}
