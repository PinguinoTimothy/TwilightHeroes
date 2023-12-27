package com.twilightheroes.game.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Queue;
import com.twilightheroes.game.TwilightHeroes;
import com.twilightheroes.game.ecs.components.AnimationComponent;
import com.twilightheroes.game.ecs.components.AttackComponent;
import com.twilightheroes.game.ecs.components.B2dBodyComponent;
import com.twilightheroes.game.ecs.components.EnemyComponent;
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
    ComponentMapper<AttackComponent> atkm;


    Touchpad touchpad;
    Button btnSaltar;
    Button btnAtacar;
    B2dBodyComponent b2body;
    StateComponent state;
    AttackComponent attackComponent;

    AnimationComponent animation;

    private int numAtaqueActual=0;
    private int numAtaquesDeseados = 0;
    private int numAtaquesLimite = 0;
    private boolean knockback;
    private float coyoteTime = 0f;
    private float dequeueTime = 0f;
    private Queue<Integer> inputBuffer = new Queue<>();
    private boolean atacando = false;
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
        atkm = ComponentMapper.getFor(AttackComponent.class);

        // Initialize the button listeners here
        btnSaltar.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                inputBuffer.addFirst(StateComponent.STATE_JUMPING);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                b2body.body.setGravityScale(2f);
            }
        });
        /*
        btnSaltar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Lógica para el salto aquí



                /*
                if ((b2body.body.getLinearVelocity().y == 0 || coyoteTime < 0.1f) && !knockback) {
                    coyoteTime += 1f;
                    b2body.body.setLinearVelocity(b2body.body.getLinearVelocity().x, 0);
                    b2body.body.applyLinearImpulse(new Vector2(0, 3f), b2body.body.getWorldCenter(), true);
                }



            }
        });

         */

        btnAtacar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {


                if (numAtaquesLimite<3 && !knockback){
                    numAtaquesLimite++;
                    numAtaquesDeseados++;
                }



            }
        });
    }



    private float speed;
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        b2body = bodm.get(entity);
        state = sm.get(entity);
        TextureComponent texture = tm.get(entity);
        animation = am.get(entity);
        attackComponent = atkm.get(entity);
        PlayerComponent playerComponent = pm.get(entity);
        knockback = playerComponent.knockback;
        speed = playerComponent.speed;

if (knockback){

    state.set(StateComponent.STATE_DAMAGED);
    numAtaqueActual = 0;
    numAtaquesLimite = 0;
    numAtaquesDeseados = 0;
    if (b2body.body.getLinearVelocity().y == 0){
        playerComponent.knockback = false;
    }
}else{
        if ((state.get() == StateComponent.STATE_ATTACK01 || state.get() == StateComponent.STATE_ATTACK02 || state.get() == StateComponent.STATE_ATTACK03) && !animation.animations.get(state.get()).isAnimationFinished(state.time)) {

        } else {

            if (attackComponent.attackFixture != null) {
                // Si la fixture de ataque ya existe, la eliminamos antes de recrearla
                b2body.body.destroyFixture(attackComponent.attackFixture);
                attackComponent.attackFixture = null;
            }


            // Crear una nueva fixture de ataque
            if (numAtaquesDeseados > 0) {
                numAtaqueActual++;
                switch (numAtaqueActual) {
                    case 1:
                        state.set(StateComponent.STATE_ATTACK01);
                        createAttackFixture(texture, b2body, attackComponent);
                        break;
                    case 2:
                        state.set(StateComponent.STATE_ATTACK02);
                        createAttackFixture(texture, b2body, attackComponent);
                        break;
                    case 3:
                        state.set(StateComponent.STATE_ATTACK03);
                        createAttackFixture(texture, b2body, attackComponent);

                        break;
                }
                state.time = 0f;

                if (numAtaqueActual >= numAtaquesDeseados){
                    numAtaquesDeseados = 0;
                }

            }else{


                numAtaqueActual = 0;
                numAtaquesLimite = 0;
                numAtaquesDeseados = 0;

                if (b2body.body.getLinearVelocity().y < 0) {
                    coyoteTime += deltaTime;
                    state.set(StateComponent.STATE_FALLING);
                    if (state.previousState == StateComponent.STATE_JUMPING){
                        b2body.body.setGravityScale(2f);
                    }
                } else if (b2body.body.getLinearVelocity().y == 0 && state.get() != StateComponent.STATE_JUMPING) {
                    coyoteTime = 0f;
                    b2body.body.setGravityScale(1f);

                    if (!inputBuffer.isEmpty()) {
                        if (inputBuffer.first() == StateComponent.STATE_JUMPING) {
                            b2body.body.setLinearVelocity(b2body.body.getLinearVelocity().x, 0);
                            b2body.body.applyLinearImpulse(new Vector2(0, playerComponent.jumpPower * deltaTime), b2body.body.getWorldCenter(), true);
                            inputBuffer.removeFirst();
                            state.set(StateComponent.STATE_JUMPING);
                        }
                    }
                    if (b2body.body.getLinearVelocity().x != 0 && state.get() != StateComponent.STATE_JUMPING) {
                        state.set(StateComponent.STATE_MOVING);


                    } else if (state.get() != StateComponent.STATE_JUMPING){
                        state.set(StateComponent.STATE_NORMAL);
                    }
                }


                // Actualiza la dirección del sprite según la velocidad
                if (b2body.body.getLinearVelocity().x > 0 && !texture.runningRight) {
                    texture.runningRight = true;

                    /*
                    if (attackComponent.attackFixture != null) {
                        // Si la fixture de ataque ya existe, la eliminamos antes de recrearla
                        b2body.body.destroyFixture(attackComponent.attackFixture);
                        attackComponent.attackFixture = null;
                    }


                    // Crear una nueva fixture de ataque
                    createAttackFixture(texture, b2body, attackComponent);


                     */
                } else if (b2body.body.getLinearVelocity().x < 0 && texture.runningRight) {
                    texture.runningRight = false;

                    /*
                    if (attackComponent.attackFixture != null) {
                        // Si la fixture de ataque ya existe, la eliminamos antes de recrearla
                        b2body.body.destroyFixture(attackComponent.attackFixture);
                        attackComponent.attackFixture = null;
                    }

                    // Crear una nueva fixture de ataque
                    createAttackFixture(texture, b2body, attackComponent);


                     */
                }


            }
        }
        }



       // state.time =  state.get() == state.previousState ? state.time + deltaTime : 0f;

if (!knockback) {


    if (touchpad.isTouched()) {


        float knobPercentX = touchpad.getKnobPercentX();

        // Puedes ajustar la velocidad según tus necesidades
        float velocityX = knobPercentX * speed * deltaTime;

        // Aplica la velocidad al cuerpo del jugador
        if (state.get() != StateComponent.STATE_ATTACK01 || state.get() != StateComponent.STATE_ATTACK02 || state.get() != StateComponent.STATE_ATTACK03) {
            b2body.body.setLinearVelocity(new Vector2(velocityX, b2body.body.getLinearVelocity().y));
        }

    }else{
        b2body.body.setLinearVelocity(new Vector2(0, b2body.body.getLinearVelocity().y));

    }

}
        dequeueTime += deltaTime;
        while (dequeueTime >= 0.2f){
            if (!inputBuffer.isEmpty()){
                inputBuffer.removeFirst();
            }
            dequeueTime -= 0.2f;
            if (dequeueTime < 0f){
                dequeueTime = 0f;
            }
        }
    }

    private void createAttackFixture(TextureComponent texture, B2dBodyComponent b2dbody,AttackComponent attackComponent) {
        PolygonShape attackShape = new PolygonShape();
        float offsetX = texture.runningRight ? 16 / TwilightHeroes.PPM : -16 / TwilightHeroes.PPM;
        attackShape.setAsBox(12 / TwilightHeroes.PPM, 8 / TwilightHeroes.PPM, new Vector2(offsetX, 0), 0);
        FixtureDef attackFixtureDef = new FixtureDef();
        attackFixtureDef.shape = attackShape;
        attackFixtureDef.isSensor = true; // Configurar la fixture como un sensor
        attackComponent.attackFixture = b2dbody.body.createFixture(attackFixtureDef);
        attackComponent.attackFixture.setUserData("playerAttackSensor");
        // Liberar los recursos del shape
        attackShape.dispose();
        b2body.body.applyForce(new Vector2((texture.runningRight ? 100f : -100f),0f),b2body.body.getWorldCenter(),true);


        }

    }

