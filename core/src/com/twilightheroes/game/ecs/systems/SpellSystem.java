package com.twilightheroes.game.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.twilightheroes.game.TwilightHeroes;
import com.twilightheroes.game.ecs.components.AttackComponent;
import com.twilightheroes.game.ecs.components.B2dBodyComponent;
import com.twilightheroes.game.ecs.components.PlayerComponent;
import com.twilightheroes.game.ecs.components.TextureComponent;
import com.twilightheroes.game.ecs.components.spells.SpellComponent;
import com.twilightheroes.game.tools.Mappers;
import com.twilightheroes.game.ecs.components.spells.SpellList;

import java.util.Map;

public class SpellSystem extends IteratingSystem {
    public SpellSystem() {
        super(Family.all(SpellComponent.class).get());

    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        SpellComponent spellComponent = Mappers.spellCom.get(entity);
        TextureComponent texture= Mappers.texCom.get(entity);
        B2dBodyComponent b2body = Mappers.b2dCom.get(entity);
        AttackComponent attackComponent = Mappers.atkCom.get(entity);

        PlayerComponent playerComponent = Mappers.playerCom.get(entity);
        boolean player;
        if (playerComponent != null){
            player = true;
        }else{
            player = false;
        }

        if (spellComponent.spellToCast != null){
            switch (spellComponent.spellToCast.id){

                case SpellList.shockingGrasp:
                    createAttackFixture(texture,b2body,attackComponent,20f,6f,16f, 0f,player);
                    break;


            }
        }
        spellComponent.spellToCast = null;
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
