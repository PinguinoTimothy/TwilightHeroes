package com.twilightheroes.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.twilightheroes.game.ecs.components.AttackComponent;
import com.twilightheroes.game.ecs.components.B2dBodyComponent;
import com.twilightheroes.game.ecs.components.PlayerComponent;
import com.twilightheroes.game.ecs.components.TextureComponent;
import com.twilightheroes.game.tools.Mappers;

public class Spells {

    public static final int SHOCKING_GRASP = 0;
    public static final int HEAL = 1;
    public static final int FURY = 2;

    PlayerComponent playerComponent;
    public Spells(PlayerComponent playerComponent){
        this.playerComponent = playerComponent;
    }

    public void castSpell(int spell){
        switch (spell){
            case Spells.SHOCKING_GRASP:
         //   createAttackFixture(touchpad, player,Mappers.b2dCom.get(player),Mappers.atkCom.get(player),true,12f,8f,16f, 0f,75f);
                break;
        }
    }

    private void createAttackFixture(Touchpad touchpad, TextureComponent texture, B2dBodyComponent b2dbody, AttackComponent attackComponent,boolean area, float hx, float hy, float offsetX, float offsetY, float attackDamage) {
        PolygonShape attackShape = new PolygonShape();
        float auxOffsetX = offsetX;
        float auxOffsetY = offsetY;
        float auxHx = hx;
        float auxHy = hy;
        if (touchpad.getKnobPercentY() < -0.3f){
            auxHx = hy;
            auxHy = hx;
            auxOffsetX = offsetY;
            auxOffsetY = -offsetX;
        }else if (touchpad.getKnobPercentY() > 0.3f) {
            auxHx = hy;
            auxHy = hx;
            auxOffsetX = offsetY;
            auxOffsetY = offsetX;
        }else{
            if (touchpad.getKnobPercentX() > 0) {
                texture.runningRight = true;
                auxOffsetX = offsetX;
            } else if (touchpad.getKnobPercentX() < 0) {
                texture.runningRight = false;
                auxOffsetX = -offsetX;
            } else {
                auxOffsetX = texture.runningRight ? offsetX : -offsetX;
            }
        }
        attackShape.setAsBox(auxHx / TwilightHeroes.PPM, auxHy / TwilightHeroes.PPM, new Vector2(auxOffsetX/TwilightHeroes.PPM, auxOffsetY / TwilightHeroes.PPM), 0);
        FixtureDef attackFixtureDef = new FixtureDef();
        attackFixtureDef.shape = attackShape;
        attackFixtureDef.filter.categoryBits = TwilightHeroes.HITBOX_BIT;
        attackFixtureDef.isSensor = true; // Configurar la fixture como un sensor
        attackComponent.attackFixture = b2dbody.body.createFixture(attackFixtureDef);
        attackComponent.attackFixture.setUserData("spell");
        // Liberar los recursos del shape
        attackShape.dispose();


        b2dbody.body.setLinearVelocity(new Vector2((texture.runningRight ? 1f : -1f),0f));

    }
}
