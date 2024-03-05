package com.twilightheroes.game.tools;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.twilightheroes.game.ecs.components.CollisionComponent;

/**
 * The type B 2 d contact listener.
 */
public class B2dContactListener implements ContactListener {

    /**
     * Instantiates a new B 2 d contact listener.
     */
    public B2dContactListener() {
    }

    @Override
    public void beginContact(Contact contact) {
        System.out.println("Contact");
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();
        System.out.println(fa.getBody().getType() + " has hit " + fb.getBody().getType());

        boolean isHitbox = false;
        boolean isEnemyHitbox = false;
        boolean canBeReduced = false;
        boolean isInteractHitbox = false;
        boolean isSpell = false;

        if ((fa.getUserData() != null && fa.getUserData().toString().contains("playerAttackSensor")) || (fb.getUserData() != null && fb.getUserData().toString().contains("playerAttackSensor"))){
            isHitbox = true;
        }
        if ((fa.getUserData() != null && fa.getUserData().toString().contains("enemyAttackSensor")) || (fb.getUserData() != null && fb.getUserData().toString().contains("enemyAttackSensor"))){
            isEnemyHitbox = true;
        }
        if ((fa.getUserData() != null && fa.getUserData().toString().contains("canBeReduced")) || (fb.getUserData() != null && fb.getUserData().toString().contains("canBeReduced"))){
            canBeReduced = true;
        }
        if ((fa.getUserData() != null && fa.getUserData().toString().contains("playerInteractSensor")) || (fb.getUserData() != null && fb.getUserData().toString().contains("playerInteractSensor"))){
            isInteractHitbox = true;
        }
        if ((fa.getUserData() != null && fa.getUserData().toString().contains("isSpell")) || (fb.getUserData() != null && fb.getUserData().toString().contains("isSpell"))){
            isSpell = true;
        }




        if (fa.getBody().getUserData() instanceof Entity) {
            Entity ent = (Entity) fa.getBody().getUserData();
            entityCollision(ent, fb, true, isHitbox, isEnemyHitbox, canBeReduced, isInteractHitbox,isSpell);
        } else if (fb.getBody().getUserData() instanceof Entity) {
            Entity ent = (Entity) fb.getBody().getUserData();
            entityCollision(ent, fa, true, isHitbox, isEnemyHitbox, canBeReduced, isInteractHitbox,isSpell);

        }
    }

    private void entityCollision(Entity ent, Fixture fb, boolean touching, boolean isHitbox, boolean isEnemyHitbox, boolean canBeReduced, boolean isInteractHitbox,boolean isSpell) {
        if (fb.getBody().getUserData() instanceof Entity) {
            Entity colEnt = (Entity) fb.getBody().getUserData();

            CollisionComponent col = ent.getComponent(CollisionComponent.class);
            CollisionComponent colb = colEnt.getComponent(CollisionComponent.class);

            if (col != null) {
                Collisions collisionsAux = new Collisions();  // Nueva instancia para cada colisión
                collisionsAux.collisionEntity = colEnt;
                collisionsAux.isAttackHitbox = isHitbox;
                collisionsAux.isEnemyHitbox = isEnemyHitbox;
                collisionsAux.canBeReduced = canBeReduced;
                collisionsAux.isInteractHitbox = isInteractHitbox;
                collisionsAux.isSpell = isSpell;

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
                collisionsAux.canBeReduced = canBeReduced;
                collisionsAux.isInteractHitbox = isInteractHitbox;
                collisionsAux.isSpell = isSpell;

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
