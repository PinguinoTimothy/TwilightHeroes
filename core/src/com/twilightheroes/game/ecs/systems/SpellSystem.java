package com.twilightheroes.game.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Timer;
import com.twilightheroes.game.TwilightHeroes;
import com.twilightheroes.game.ecs.components.AnimationComponent;
import com.twilightheroes.game.ecs.components.AttackComponent;
import com.twilightheroes.game.ecs.components.B2dBodyComponent;
import com.twilightheroes.game.ecs.components.BulletComponent;
import com.twilightheroes.game.ecs.components.PlayerComponent;
import com.twilightheroes.game.ecs.components.StateComponent;
import com.twilightheroes.game.ecs.components.StatsComponent;
import com.twilightheroes.game.ecs.components.TextureComponent;
import com.twilightheroes.game.ecs.components.effectComponents.StatusComponent;
import com.twilightheroes.game.ecs.components.effectComponents.StatusEffect;
import com.twilightheroes.game.ecs.components.effectComponents.StatusType;
import com.twilightheroes.game.ecs.components.spells.Spell;
import com.twilightheroes.game.ecs.components.spells.SpellComponent;
import com.twilightheroes.game.ecs.components.spells.SpellList;
import com.twilightheroes.game.screens.MainScreen;
import com.twilightheroes.game.tools.AnimationMaker;
import com.twilightheroes.game.tools.Mappers;

/**
 * A simple EntitySystem that iterates over each spell entity and calls processEntity() for each entity every time the EntitySystem is
 * updated.
 */
public class SpellSystem extends IteratingSystem {
    /**
     * The MainScren.
     */
    MainScreen screen;
    /**
     * The List of spells.
     */
    SpellList.spells spell;
    /**
     * The AssetManager.
     */
    AssetManager manager;


    /**
     * Instantiates a new Spell system.
     *
     * @param screen the MainScreen
     */
    public SpellSystem(MainScreen screen) {
        super(Family.all(SpellComponent.class).get());
        this.screen = screen;
        manager = screen.parent.assMan.manager;
    }
    /**
     * This method is called on every entity on every update call of the EntitySystem.
     * @param entity The current Entity being processed
     * @param deltaTime The delta time between the last and current frame
     */
    @Override
    protected void processEntity(final Entity entity, float deltaTime) {

       final SpellComponent spellComponent = Mappers.spellCom.get(entity);
        TextureComponent texture = Mappers.texCom.get(entity);
        B2dBodyComponent b2body = Mappers.b2dCom.get(entity);
        AttackComponent attackComponent = Mappers.atkCom.get(entity);
        StatsComponent statsComponent = Mappers.statsCom.get(entity);
        StatusComponent statusComponent = Mappers.statusCom.get(entity);
        StateComponent stateComponent = Mappers.stateCom.get(entity);

        PlayerComponent playerComponent = Mappers.playerCom.get(entity);
        boolean player;
        player = playerComponent != null;

        if (spellComponent.castingTime > 0) {
            spellComponent.castingTime -= deltaTime;
        }

        if (spellComponent.spellToCast != null) {
            if (!spellComponent.casting) {
                spell = SpellList.spells.values()[spellComponent.spellToCast.id];
                spellComponent.castingTime = spellComponent.spellToCast.castingTime;
                spellComponent.duration = spellComponent.spellToCast.duration;
                spellComponent.value = spellComponent.spellToCast.value;

                stateComponent.set(StateComponent.STATE_CASTING);
                stateComponent.time = 0f;
                spellComponent.casting = true;
                stateComponent.isLooping = false;
            }

            if (spellComponent.castingTime <= 0) {
                switch (spell) {

                    case shockingGrasp:
                        createAttackFixture(texture, b2body, attackComponent, 20f, 6f, 16f, 0f, player);
                        createVFX(spellComponent, entity);

                        break;



                    case healingSigil:
                        statsComponent.hp += spellComponent.value;


                        // bul.particleEffect = makeParticleEffect(ParticleEffectManager.FIRE,b2dbody);

                        break;

                    case fury:
                        statusComponent.effects.add(new StatusEffect(StatusType.DAMAGE, true, spellComponent.duration, spellComponent.value));
                        break;

                    case frostSpear:
                        float xVel = texture.runningRight ? 0.5f : -0.5f;  // set the speed of the bullet
                        float shooterX = b2body.body.getPosition().x; // get player location
                        float shooterY = b2body.body.getPosition().y; // get player location
                        screen.b2WorldCreator.createBullet(shooterX, shooterY, xVel, player ? BulletComponent.Owner.PLAYER : BulletComponent.Owner.ENEMY, texture.runningRight, 25, "frostSpear");
                        break;

                }
                spellComponent.casting = false;
                spellComponent.spellToCast = null;


            }
        }
    }


    /**
     * Create the VFX for the spell
     * @param spellComponent the spell
     * @param casterEntity the caster
     */
    private void createVFX(SpellComponent spellComponent, Entity casterEntity) {
        if (spellComponent.spellToCast.vfx != null) {
            Entity entityVFX = getEngine().createEntity();
            TextureComponent texture = getEngine().createComponent(TextureComponent.class);
            AnimationComponent animationComponent = getEngine().createComponent(AnimationComponent.class);
            StateComponent stateComponent = getEngine().createComponent(StateComponent.class);


            TextureComponent textureCaster = Mappers.texCom.get(casterEntity);
            B2dBodyComponent b2body = Mappers.b2dCom.get(casterEntity);


            texture.sprite.setTexture(manager.get("spells/spellsVFX/" + SpellList.spells.values()[spellComponent.spellToCast.id].name() + ".png", Texture.class));
            float offsetX = textureCaster.runningRight ? 20f / TwilightHeroes.PPM : -20f / TwilightHeroes.PPM;
            texture.sprite.setBounds(b2body.body.getPosition().x + offsetX, b2body.body.getPosition().y, 20 / TwilightHeroes.PPM, 20 / TwilightHeroes.PPM);
            animationComponent.animations.put(StateComponent.STATE_VFX, AnimationMaker.crearAnimacion(manager.get("spells/spellsVFX/spells.atlas", TextureAtlas.class), SpellList.spells.values()[spellComponent.spellToCast.id].name(), spellComponent.spellToCast.vfx.nFrames, spellComponent.spellToCast.vfx.frameDuration));
            stateComponent.set(StateComponent.STATE_VFX);

            stateComponent.time = 0;
            entityVFX.add(texture);
            entityVFX.add(animationComponent);
            entityVFX.add(stateComponent);
            getEngine().addEntity(entityVFX);

        }
    }

    /**
     * Create the attackFixture for the spell based on his caster
     * @param texture Texture of his caster
     * @param b2dbody Body of his caster
     * @param attackComponent AttackComponent of his caster
     * @param hx Width of the attackFixture
     * @param hy Height of the attackFixture
     * @param offsetX OffsetX of the attackFixture
     * @param offsetY OffsetY of the attackFixture
     * @param player True indicates that the caster is a player false an enemy
     */
    private void createAttackFixture(TextureComponent texture, B2dBodyComponent b2dbody, AttackComponent attackComponent, float hx, float hy, float offsetX, float offsetY, boolean player) {
        PolygonShape attackShape = new PolygonShape();
        float auxOffsetX = texture.runningRight ? offsetX : -offsetX;


        attackShape.setAsBox(hx / TwilightHeroes.PPM, hy / TwilightHeroes.PPM, new Vector2(auxOffsetX / TwilightHeroes.PPM, offsetY / TwilightHeroes.PPM), 0);
        FixtureDef attackFixtureDef = new FixtureDef();
        attackFixtureDef.shape = attackShape;
        attackFixtureDef.filter.categoryBits = TwilightHeroes.HITBOX_BIT;
        attackFixtureDef.isSensor = true;
        attackComponent.attackFixture = b2dbody.body.createFixture(attackFixtureDef);
        if (player) {
            attackComponent.attackFixture.setUserData("playerAttackSensor");
        } else {
            attackComponent.attackFixture.setUserData("enemyAttackSensor");

        }
        // Liberar los recursos del shape
        attackShape.dispose();


        b2dbody.body.setLinearVelocity(new Vector2((texture.runningRight ? 12f : -1f), 0f));

    }
}
