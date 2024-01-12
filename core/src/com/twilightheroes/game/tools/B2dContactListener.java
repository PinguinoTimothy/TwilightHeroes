package com.twilightheroes.game.tools;

import com.badlogic.gdx.ai.utils.Collision;
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
        boolean isEnemyHitbox = false;
        if ("playerAttackSensor".equals(fa.getUserData()) || "playerAttackSensor".equals(fb.getUserData())){
            isHitbox = true;
        }
        if ("enemyAttackSensor".equals(fa.getUserData()) || "enemyAttackSensor".equals(fb.getUserData())){
            isEnemyHitbox = true;
        }
        if(fa.getBody().getUserData() instanceof Entity){
            Entity ent = (Entity) fa.getBody().getUserData();
            entityCollision(ent,fb,true,isHitbox,isEnemyHitbox);
        }else if(fb.getBody().getUserData() instanceof Entity){
            Entity ent = (Entity) fb.getBody().getUserData();
            entityCollision(ent,fa,true,isHitbox,isEnemyHitbox);

        }
    }

    private void entityCollision(Entity ent, Fixture fb, boolean touching, boolean isHitbox, boolean isEnemyHitbox) {
        if (fb.getBody().getUserData() instanceof Entity) {
            Entity colEnt = (Entity) fb.getBody().getUserData();

            CollisionComponent col = ent.getComponent(CollisionComponent.class);
            CollisionComponent colb = colEnt.getComponent(CollisionComponent.class);

            if (col != null) {
                Collisions collisionsAux = new Collisions();  // Nueva instancia para cada colisión
                collisionsAux.collisionEntity = colEnt;
                collisionsAux.isAttackHitbox = isHitbox;
                collisionsAux.isEnemyHitbox = isEnemyHitbox;

                if (touching) {
                    // Si las entidades están en contacto, añade la entidad a la lista de colisiones
                    col.collisionEntities.add(collisionsAux);
                } else {
                    // Si las entidades ya no están en contacto, elimina la entidad de la lista de colisiones
                    col.collisionEntities.removeValue(collisionsAux, true);
                }


            }
            if (colb != null) {
                Collisions collisionsAux = new Collisions();  // Nueva instancia para cada colisión
                collisionsAux.collisionEntity = ent;
                collisionsAux.isAttackHitbox = isHitbox;
                collisionsAux.isEnemyHitbox = isEnemyHitbox;

                if (touching) {
                    colb.collisionEntities.add(collisionsAux);
                } else {
                    colb.collisionEntities.removeValue(collisionsAux, true);
                }
            }
        }
    }
    @Override
    public void endContact(Contact contact) {
/*
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
            entityCollision(ent,fb,false,isHitbox,);

        }else if(fb.getBody().getUserData() instanceof Entity){
            Entity ent = (Entity) fb.getBody().getUserData();
            if ("playerAttackSensor".equals(fa.getUserData())){
                isHitbox = true;
            }
            entityCollision(ent,fa,false,isHitbox);

        }

 */
    }
    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }
    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

}
