package com.twilightheroes.game.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.twilightheroes.game.ecs.components.AnimationComponent;
import com.twilightheroes.game.ecs.components.StateComponent;
import com.twilightheroes.game.ecs.components.TextureComponent;
import com.twilightheroes.game.tools.Mappers;

/**
 * The system that handles all the animations
 */
public class AnimationSystem extends IteratingSystem {


    /**
     * Instantiates a new Animation system.
     */
    public AnimationSystem() {
        super(Family.all(TextureComponent.class,
                AnimationComponent.class,
                StateComponent.class).get());


    }

    /**
     * This method is called on every entity on every update call of the EntitySystem.
     *
     * @param entity    The current Entity being processed
     * @param deltaTime The delta time between the last and current frame
     */
    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        AnimationComponent ani = Mappers.animCom.get(entity);
        StateComponent state = Mappers.stateCom.get(entity);

        if (ani.animations.containsKey(state.get())) {
            TextureComponent tex = Mappers.texCom.get(entity);
            tex.sprite.setRegion(ani.animations.get(state.get()).getKeyFrame(state.time, state.isLooping));
            ani.currentFrame = ani.animations.get(state.get()).getKeyFrameIndex(state.time);
        }

        if (state.get() == StateComponent.STATE_VFX && ani.animations.get(state.get()).isAnimationFinished(state.time)) {
            getEngine().removeEntity(entity);
        }

        state.time += deltaTime;

    }
}
