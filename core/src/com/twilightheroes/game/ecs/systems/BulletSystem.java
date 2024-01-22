package com.twilightheroes.game.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.twilightheroes.game.ecs.components.B2dBodyComponent;
import com.twilightheroes.game.ecs.components.BulletComponent;
import com.twilightheroes.game.screens.MainScreen;
import com.twilightheroes.game.tools.B2WorldCreator;
import com.twilightheroes.game.tools.Mappers;

public class BulletSystem extends IteratingSystem {
    private MainScreen parent;
    public BulletSystem(MainScreen parent) {
        super(Family.all(BulletComponent.class).get());
        this.parent =  parent;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        //get box 2d body and bullet components
        B2dBodyComponent b2body = Mappers.b2dCom.get(entity);
        BulletComponent bullet = Mappers.bullCom.get(entity);

        // apply bullet velocity to bullet body
        b2body.body.setLinearVelocity(bullet.xVel,0f);

        // get player pos
        B2dBodyComponent playerBodyComp = Mappers.b2dCom.get(parent.playerEntity);
        float px = playerBodyComp.body.getPosition().x;
        float py = playerBodyComp.body.getPosition().y;

        //get bullet pos
        float bx = b2body.body.getPosition().x;
        float by = b2body.body.getPosition().y;

        // if bullet is 20 units away from player on any axis then it is probably off screen
        if(bx - px > 3 || by - py > 5){
            bullet.isDead = true;
        }

        //check if bullet is dead
        if(bullet.isDead){
            System.out.println("Bullet died");
            b2body.isDead = true;
        }
    }
}
