package com.twilightheroes.game.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
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

public class SpellSystem extends IteratingSystem {
    MainScreen screen;
    SpellList.spells spell;
    AssetManager manager;


    public SpellSystem(MainScreen screen) {
        super(Family.all(SpellComponent.class).get());
        this.screen = screen;
        manager = screen.parent.assMan.manager;
    }

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
                stateComponent.set(StateComponent.STATE_CASTING);
                stateComponent.time = 0f;
                spellComponent.casting = true;
                stateComponent.isLooping = false;
            }
            if (spellComponent.castingTime <= 0) {
                switch (spell) {

                    case shockingGrasp:
                        createAttackFixture(texture, b2body, attackComponent, 20f, 6f, 16f, 0f, player);
                        createVFX(spellComponent.spellToCast, entity,1);

                        break;



                    case healingSigil:
                        statsComponent.hp += 25;


                        // bul.particleEffect = makeParticleEffect(ParticleEffectManager.FIRE,b2dbody);

                        break;

                    case fury:
                        statusComponent.effects.add(new StatusEffect(StatusType.DAMAGE, true, 10, 25));
                        break;

                    case frostSpear:
                        float xVel = texture.runningRight ? 0.5f : -0.5f;  // set the speed of the bullet
                        float shooterX = b2body.body.getPosition().x; // get player location
                        float shooterY = b2body.body.getPosition().y; // get player location
                        screen.b2WorldCreator.createBullet(shooterX, shooterY, xVel, player ? BulletComponent.Owner.PLAYER : BulletComponent.Owner.ENEMY, texture.runningRight, 25, "frostSpear");
                        break;

                    case earthSpike:

                        for (int i = 0; i < 5; i++) {
                            float delay = i * 0.5f; // Ajusta el valor del retraso según tus necesidades
                            createDelayedAttackAndVFX(texture, b2body, attackComponent, 10f, 5f, 16f * (i+1), 0, player, spellComponent, entity, delay, i+1);
                        }
                        break;
                }
                spellComponent.casting = false;
                spellComponent.spellToCast = null;


            }
        }
    }
    public Spell spellToCast;
    private void createDelayedAttackAndVFX(final TextureComponent texture, final B2dBodyComponent b2dbody, final AttackComponent attackComponent, final float hx, final float hy, final float offsetX, final float offsetY, final boolean player, final SpellComponent spellComponent, final Entity casterEntity, float delay,final int i) {
         spellToCast = spellComponent.spellToCast;

        Timer.Task task = new Timer.Task() {
            @Override
            public void run() {
                createAttackAndVFX(texture, b2dbody, attackComponent, hx, hy, offsetX, offsetY, player, spellToCast, casterEntity,i);
            }
        };

        screen.timer.scheduleTask(task, delay);
    }



    private void createAttackAndVFX(final TextureComponent texture, final B2dBodyComponent b2dbody, final AttackComponent attackComponent, final float hx, final float hy, final float offsetX, final float offsetY, final boolean player, final Spell spell, final Entity casterEntity, int i) {
        createAttackFixture(texture, b2dbody, attackComponent, hx, hy, offsetX, offsetY, player);
        createVFX(spell, casterEntity, i);
    }



    private void createVFX(Spell spell, Entity casterEntity,int i) {
        if (spell.vfx != null) {
            Entity entityVFX = getEngine().createEntity();
            TextureComponent texture = getEngine().createComponent(TextureComponent.class);
            AnimationComponent animationComponent = getEngine().createComponent(AnimationComponent.class);
            StateComponent stateComponent = getEngine().createComponent(StateComponent.class);

            TextureComponent textureCaster = Mappers.texCom.get(casterEntity);
            B2dBodyComponent b2body = Mappers.b2dCom.get(casterEntity);



            texture.sprite.setTexture(manager.get("spells/spellsVFX/" + SpellList.spells.values()[spell.id].name() + ".png", Texture.class));
            float offsetX = textureCaster.runningRight ? 16f / TwilightHeroes.PPM : -16f / TwilightHeroes.PPM;
            offsetX *= i;
            texture.sprite.setSize(20/TwilightHeroes.PPM,20/TwilightHeroes.PPM);
            texture.sprite.setPosition(b2body.body.getPosition().x - b2body.width/TwilightHeroes.PPM + offsetX , b2body.body.getPosition().y - 10/TwilightHeroes.PPM);
            animationComponent.animations.put(StateComponent.STATE_VFX, AnimationMaker.crearAnimacion(manager.get("spells/spellsVFX/spells.atlas", TextureAtlas.class), SpellList.spells.values()[spell.id].name(), spell.vfx.nFrames, spell.vfx.frameDuration));
            stateComponent.set(StateComponent.STATE_VFX);

            stateComponent.time = 0;
            entityVFX.add(texture);
            entityVFX.add(animationComponent);
            entityVFX.add(stateComponent);
            getEngine().addEntity(entityVFX);

        }
    }

    private void createAttackFixture(TextureComponent texture, B2dBodyComponent b2dbody, AttackComponent attackComponent, float hx, float hy, float offsetX, float offsetY, boolean player) {
        PolygonShape attackShape = new PolygonShape();
        float auxOffsetX = texture.runningRight ? offsetX : -offsetX;


        attackShape.setAsBox(hx / TwilightHeroes.PPM, hy     / TwilightHeroes.PPM, new Vector2(auxOffsetX / TwilightHeroes.PPM, offsetY / TwilightHeroes.PPM), 0);
        FixtureDef attackFixtureDef = new FixtureDef();
        attackFixtureDef.shape = attackShape;
        attackFixtureDef.filter.categoryBits = TwilightHeroes.HITBOX_BIT;
        attackFixtureDef.isSensor = true;
        Fixture fix = b2dbody.body.createFixture(attackFixtureDef);
        if (player) {
            fix.setUserData("playerAttackSensor");
        } else {
            fix.setUserData("enemyAttackSensor");
        }

       attackComponent.attackFixtures.add(fix); // Almacenar la fixture en la lista
        attackComponent.lifetimes.add(0f);  // Inicializa el tiempo de vida para esta fixture a 0

        attackShape.dispose();
        b2dbody.body.applyLinearImpulse(new Vector2(0f, -0.1f), b2dbody.body.getWorldCenter(), true);
    }



}
