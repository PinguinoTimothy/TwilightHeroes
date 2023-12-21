package com.twilightheroes.game.tools;

import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.twilightheroes.game.ecs.components.CollisionComponent;

public class B2dContactListener implements ContactListener {

    public B2dContactListener(){
    }

    @Override
    public void beginContact(Contact contact) {
        System.out.println("Contact");
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();
        System.out.println(fa.getBody().getType()+" has hit "+ fb.getBody().getType());

        boolean isHitbox = false;
        if(fa.getBody().getUserData() instanceof Entity){
            Entity ent = (Entity) fa.getBody().getUserData();
            if ("playerAttackSensor".equals(fa.getUserData())){
                isHitbox = true;
            }
            entityCollision(ent,fb,true,isHitbox);

        }else if(fb.getBody().getUserData() instanceof Entity){
            Entity ent = (Entity) fb.getBody().getUserData();
            if ("playerAttackSensor".equals(fa.getUserData())){
                isHitbox = true;
            }
            entityCollision(ent,fa,true,isHitbox);

        }
    }

    public void collide(boolean collision){

    }
    private void entityCollision(Entity ent, Fixture fb, boolean touching, boolean hitbox) {
        if (fb.getBody().getUserData() instanceof Entity) {
            Entity colEnt = (Entity) fb.getBody().getUserData();

            CollisionComponent col = ent.getComponent(CollisionComponent.class);
            CollisionComponent colb = colEnt.getComponent(CollisionComponent.class);

            if (col != null) {
                col.collisionEntity = colEnt;
                col.isTouching = touching;
                col.isAttackHitbox = hitbox;
            }
            if (colb != null) {
                colb.collisionEntity = ent;
                col.isTouching = touching;
                col.isAttackHitbox = hitbox;
            }
        }
    }

    @Override
    public void endContact(Contact contact) {

        System.out.println("Contact end");
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();
        System.out.println(fa.getBody().getType()+" no longer hits "+ fb.getBody().getType());
        boolean isHitbox = false;
        if(fa.getBody().getUserData() instanceof Entity){
            Entity ent = (Entity) fa.getBody().getUserData();
            if ("playerAttackSensor".equals(fa.getUserData())){
                isHitbox = true;
            }
            entityCollision(ent,fb,false,isHitbox);

        }else if(fb.getBody().getUserData() instanceof Entity){
            Entity ent = (Entity) fb.getBody().getUserData();
            if ("playerAttackSensor".equals(fa.getUserData())){
                isHitbox = true;
            }
            entityCollision(ent,fa,false,isHitbox);

        }
    }
    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }
    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

}
