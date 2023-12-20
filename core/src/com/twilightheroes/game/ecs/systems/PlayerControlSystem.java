package com.twilightheroes.game.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.twilightheroes.game.ecs.components.AnimationComponent;
import com.twilightheroes.game.ecs.components.B2dBodyComponent;
import com.twilightheroes.game.ecs.components.PlayerComponent;
import com.twilightheroes.game.ecs.components.StateComponent;
import com.twilightheroes.game.ecs.components.TextureComponent;
import com.twilightheroes.game.tools.KeyboardController;

public class PlayerControlSystem extends IteratingSystem {
    ComponentMapper<PlayerComponent> pm;
    ComponentMapper<B2dBodyComponent> bodm;
    ComponentMapper<StateComponent> sm;
    ComponentMapper<TextureComponent> tm;

    ComponentMapper<AnimationComponent> am;

    Touchpad touchpad;
    Button btnSaltar;
    Button btnAtacar;
    B2dBodyComponent b2body;
    StateComponent state;

    AnimationComponent animation;


    public PlayerControlSystem(Touchpad touchpad, Button btnSaltar, Button btnAtacar) {
        super(Family.all(PlayerComponent.class).get());
        this.touchpad = touchpad;
        this.btnSaltar = btnSaltar;
        this.btnAtacar = btnAtacar;
        pm = ComponentMapper.getFor(PlayerComponent.class);
        bodm = ComponentMapper.getFor(B2dBodyComponent.class);
        sm = ComponentMapper.getFor(StateComponent.class);
        tm = ComponentMapper.getFor(TextureComponent.class);
        am = ComponentMapper.getFor(AnimationComponent.class);
    }
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
         b2body = bodm.get(entity);
         state = sm.get(entity);
        TextureComponent texture = tm.get(entity);
        animation = am.get(entity);

        texture.sprite.setPosition(
                b2body.body.getPosition().x - texture.sprite.getWidth() / 2,
                b2body.body.getPosition().y - texture.sprite.getHeight() / 2
        );

        if (state.get() == StateComponent.STATE_ATTACK && !animation.animations.get(state.get()).isAnimationFinished(state.time)){
            state.set(StateComponent.STATE_ATTACK);
        }else {

            if (b2body.body.getLinearVelocity().y > 0) {
                state.set(StateComponent.STATE_FALLING);
            }

            if (b2body.body.getLinearVelocity().y == 0) {
                if (state.get() == StateComponent.STATE_FALLING) {
                    state.set(StateComponent.STATE_NORMAL);
                }
                if (b2body.body.getLinearVelocity().x != 0) {
                    state.set(StateComponent.STATE_MOVING);


                } else {
                    state.set(StateComponent.STATE_NORMAL);
                }
            }
            // Actualiza la dirección del sprite según la velocidad
            texture.runningRight = b2body.body.getLinearVelocity().x > 0;
        }

        state.time = state.get() == state.previousState ? state.time + deltaTime : 0f;
        if (touchpad.isTouched()) {
            float knobPercentX = touchpad.getKnobPercentX();
            float velocityX = knobPercentX * 2.0f;

            if (state.get() != StateComponent.STATE_ATTACK) {
                b2body.body.setLinearVelocity(new Vector2(velocityX, b2body.body.getLinearVelocity().y));
            }
        }




        state.time =  state.get() == state.previousState ? state.time + deltaTime : 0f;
        if (touchpad.isTouched()) {


            float knobPercentX = touchpad.getKnobPercentX();

            // Puedes ajustar la velocidad según tus necesidades
            float velocityX = knobPercentX * 2.0f;

            // Aplica la velocidad al cuerpo del jugador
            if (state.get() != StateComponent.STATE_ATTACK) {
                b2body.body.setLinearVelocity(new Vector2(velocityX, b2body.body.getLinearVelocity().y));
            }

        }

        btnSaltar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Lógica para el salto aquí
                if (b2body.body.getLinearVelocity().y == 0) {


                    b2body.body.setLinearVelocity(b2body.body.getLinearVelocity().x, 0); // Restablece la velocidad vertical a cero
                    b2body.body.applyLinearImpulse(new Vector2(0, 3f), b2body.body.getWorldCenter(), true);
                }
            }
            });


        btnAtacar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                if (state.get() != StateComponent.STATE_ATTACK){
                    if (b2body.body.getLinearVelocity().y == 0){
                        b2body.body.setLinearVelocity(new Vector2(0f,b2body.body.getLinearVelocity().y));
                    }
                    state.set(StateComponent.STATE_ATTACK);
                    state.time = 0f;
                }

            }
        });

    }
}
