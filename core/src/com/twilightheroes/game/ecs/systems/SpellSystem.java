package com.twilightheroes.game.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.twilightheroes.game.TwilightHeroes;
import com.twilightheroes.game.ecs.components.AnimationComponent;
import com.twilightheroes.game.ecs.components.AttackComponent;
import com.twilightheroes.game.ecs.components.B2dBodyComponent;
import com.twilightheroes.game.ecs.components.BulletComponent;
import com.twilightheroes.game.ecs.components.CollisionComponent;
import com.twilightheroes.game.ecs.components.PlayerComponent;
import com.twilightheroes.game.ecs.components.StateComponent;
import com.twilightheroes.game.ecs.components.StatsComponent;
import com.twilightheroes.game.ecs.components.TextureComponent;
import com.twilightheroes.game.ecs.components.TypeComponent;
import com.twilightheroes.game.ecs.components.effectComponents.StatusComponent;
import com.twilightheroes.game.ecs.components.effectComponents.StatusEffect;
import com.twilightheroes.game.ecs.components.effectComponents.StatusType;
import com.twilightheroes.game.ecs.components.spells.SpellComponent;
import com.twilightheroes.game.screens.MainScreen;
import com.twilightheroes.game.tools.AnimationMaker;
import com.twilightheroes.game.tools.Mappers;
import com.twilightheroes.game.ecs.components.spells.SpellList;

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
    protected void processEntity(Entity entity, float deltaTime) {

        SpellComponent spellComponent = Mappers.spellCom.get(entity);
        TextureComponent texture= Mappers.texCom.get(entity);
        B2dBodyComponent b2body = Mappers.b2dCom.get(entity);
        AttackComponent attackComponent = Mappers.atkCom.get(entity);
        StatsComponent statsComponent = Mappers.statsCom.get(entity);
        StatusComponent statusComponent = Mappers.statusCom.get(entity);
        StateComponent stateComponent = Mappers.stateCom.get(entity);

        PlayerComponent playerComponent = Mappers.playerCom.get(entity);
        boolean player;
        if (playerComponent != null){
            player = true;
        }else{
            player = false;
        }

        if (spellComponent.castingTime>0){
            spellComponent.castingTime -= deltaTime;
        }

        if (spellComponent.spellToCast != null){
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
                        createVFX(spellComponent,entity);
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
                        screen.b2WorldCreator.createBullet(shooterX, shooterY, xVel,player ? BulletComponent.Owner.PLAYER : BulletComponent.Owner.ENEMY, texture.runningRight, 25, "frostSpear");
                        break;
                }
                spellComponent.casting = false;
                spellComponent.spellToCast = null;

            }
        }
    }

    private void createVFX(SpellComponent spellComponent, Entity casterEntity){
        if (spellComponent.spellToCast.vfx != null){
            Entity entityVFX = getEngine().createEntity();
            TextureComponent texture = getEngine().createComponent(TextureComponent.class);
            AnimationComponent animationComponent = getEngine().createComponent(AnimationComponent.class);
            StateComponent stateComponent = getEngine().createComponent(StateComponent.class);



            TextureComponent textureCaster= Mappers.texCom.get(casterEntity);
            B2dBodyComponent b2body = Mappers.b2dCom.get(casterEntity);




            texture.sprite.setTexture(manager.get("spells/spellsVFX/"+SpellList.spells.values()[spellComponent.spellToCast.id].name()+".png", Texture.class));
            Float offsetX =textureCaster.runningRight ? 20f/TwilightHeroes.PPM : -20f/TwilightHeroes.PPM;
            texture.sprite.setBounds(b2body.body.getPosition().x + offsetX,b2body.body.getPosition().y,20/ TwilightHeroes.PPM,20/TwilightHeroes.PPM);
            animationComponent.animations.put(StateComponent.STATE_VFX, AnimationMaker.crearAnimacion(manager.get("spells/spellsVFX/spellsVFX.atlas", TextureAtlas.class), SpellList.spells.values()[spellComponent.spellToCast.id].name(),spellComponent.spellToCast.vfx.nFrames, spellComponent.spellToCast.vfx.frameDuration));
            stateComponent.set(StateComponent.STATE_VFX);

            stateComponent.time = 0;
            entityVFX.add(texture);
            entityVFX.add(animationComponent);
            entityVFX.add(stateComponent);
            getEngine().addEntity(entityVFX);

        }
    }

    private void createAttackFixture(TextureComponent texture, B2dBodyComponent b2dbody, AttackComponent attackComponent, float hx, float hy, float offsetX, float offsetY,boolean player) {
        PolygonShape attackShape = new PolygonShape();
        float auxOffsetX = texture.runningRight ? offsetX : -offsetX;
        float auxOffsetY = offsetY;
        float auxHx = hx;
        float auxHy = hy;


        attackShape.setAsBox(auxHx / TwilightHeroes.PPM, auxHy / TwilightHeroes.PPM, new Vector2(auxOffsetX / TwilightHeroes.PPM, auxOffsetY / TwilightHeroes.PPM), 0);
        FixtureDef attackFixtureDef = new FixtureDef();
        attackFixtureDef.shape = attackShape;
        attackFixtureDef.filter.categoryBits = TwilightHeroes.HITBOX_BIT;
        attackFixtureDef.isSensor = true;
        attackComponent.attackFixture = b2dbody.body.createFixture(attackFixtureDef);
        if (player){
            attackComponent.attackFixture.setUserData("playerAttackSensor");
        }else{
            attackComponent.attackFixture.setUserData("enemyAttackSensor");

        }
        // Liberar los recursos del shape
        attackShape.dispose();


        b2dbody.body.setLinearVelocity(new Vector2((texture.runningRight ? 1f : -1f), 0f));

    }

}
