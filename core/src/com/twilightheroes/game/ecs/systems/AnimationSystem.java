package com.twilightheroes.game.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.twilightheroes.game.ecs.components.AnimationComponent;
import com.twilightheroes.game.ecs.components.StateComponent;
import com.twilightheroes.game.ecs.components.TextureComponent;
import com.twilightheroes.game.tools.Mappers;

public class AnimationSystem extends IteratingSystem {




    public AnimationSystem(){
        super(Family.all(TextureComponent.class,
                AnimationComponent.class,
                StateComponent.class).get());


    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        AnimationComponent ani = Mappers.animCom.get(entity);
        StateComponent state = Mappers.stateCom.get(entity);

        if(ani.animations.containsKey(state.get())){
            TextureComponent tex = Mappers.texCom.get(entity);
            tex.sprite.setRegion(ani.animations.get(state.get()).getKeyFrame(state.time, state.isLooping));
        ani.currentFrame = ani.animations.get(state.get()).getKeyFrameIndex(state.time);
        }
        state.time += deltaTime;
    }
}
