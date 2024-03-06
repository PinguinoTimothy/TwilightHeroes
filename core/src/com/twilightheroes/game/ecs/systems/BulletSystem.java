package com.twilightheroes.game.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.twilightheroes.game.ecs.components.AnimationComponent;
import com.twilightheroes.game.ecs.components.B2dBodyComponent;
import com.twilightheroes.game.ecs.components.BulletComponent;
import com.twilightheroes.game.ecs.components.StateComponent;
import com.twilightheroes.game.screens.MainScreen;
import com.twilightheroes.game.tools.Mappers;

/**
 * The system that handles all the bullets
 */
public class BulletSystem extends IteratingSystem {
    private final MainScreen screen;

    /**
     * Instantiates a new Bullet system.
     *
     * @param screen the screen
     */
    public BulletSystem(MainScreen screen) {
        super(Family.all(BulletComponent.class).get());
        this.screen = screen;
    }

    /**
     * This method is called on every entity on every update call of the EntitySystem.
     *
     * @param entity    The current Entity being processed
     * @param deltaTime The delta time between the last and current frame
     */
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        //get box 2d body and bullet components
        B2dBodyComponent b2body = Mappers.b2dCom.get(entity);
        BulletComponent bullet = Mappers.bullCom.get(entity);

        // apply bullet velocity to bullet body
        if (!bullet.isDead) {
            b2body.body.setLinearVelocity(bullet.xVel, 0f);


            // get player pos
            B2dBodyComponent playerBodyComp = Mappers.b2dCom.get(screen.playerEntity);
            float px = playerBodyComp.body.getPosition().x;
            float py = playerBodyComp.body.getPosition().y;

            //get bullet pos
            float bx = b2body.body.getPosition().x;
            float by = b2body.body.getPosition().y;

            // if bullet is away from player on any axis then it is probably off screen
            if (bx - px > 3 || by - py > 5) {
                bullet.isDead = true;
            }
        } else {
            b2body.body.setLinearVelocity(0, 0f);


        }
        //
        StateComponent stateComponent = Mappers.stateCom.get(entity);
        AnimationComponent animationComponent = Mappers.animCom.get(entity);
        if (stateComponent.get() == StateComponent.STATE_SPELL_STARTING && animationComponent.animations.get(stateComponent.get()).isAnimationFinished(stateComponent.time)) {
            stateComponent.set(StateComponent.STATE_SPELL_GOING);
        } else if (stateComponent.get() == StateComponent.STATE_SPELL_ENDING && animationComponent.animations.get(stateComponent.get()).isAnimationFinished(stateComponent.time)) {
            b2body.isDead = true;
        }


        //check if bullet is dead
        if (bullet.isDead && stateComponent.get() != StateComponent.STATE_SPELL_ENDING) {
            stateComponent.set(StateComponent.STATE_SPELL_ENDING);
            stateComponent.time = 0f;
        }
    }
}
