package com.twilightheroes.game.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Queue;
import com.twilightheroes.game.TwilightHeroes;
import com.twilightheroes.game.ecs.components.AnimationComponent;
import com.twilightheroes.game.ecs.components.AttackComponent;
import com.twilightheroes.game.ecs.components.B2dBodyComponent;
import com.twilightheroes.game.ecs.components.PlayerComponent;
import com.twilightheroes.game.ecs.components.StateComponent;
import com.twilightheroes.game.ecs.components.TextureComponent;
import com.twilightheroes.game.tools.Mappers;

public class PlayerControlSystem extends IteratingSystem {



    Touchpad touchpad;
    Button btnSaltar;
    Button btnAtacar;
    Button btnDodge;

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
    public boolean dodging = false;
    public boolean makeDodge = false;
    public boolean canDodge = true;
    public float dodgeTime = 0f;
    public float dodgeCooldown = 0f;
    private Filter inmuneFilter = new Filter();
    private Filter playerFilter = new Filter();
    private float accelY;

    public PlayerControlSystem(Touchpad touchpad, Button btnSaltar, Button btnAtacar,Button btnDodge) {
        super(Family.all(PlayerComponent.class).get());
        this.touchpad = touchpad;
        this.btnSaltar = btnSaltar;
        this.btnAtacar = btnAtacar;
        this.btnDodge = btnDodge;


        // Initialize the button listeners here
        /*
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


         */
        btnSaltar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Lógica para el salto aquí


saltar();





            }
        });



        btnAtacar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {


                if (numAtaquesLimite<3 && !knockback && attackCooldown <= 0f){
                    numAtaquesLimite++;
                    numAtaquesDeseados++;
                    attacking = true;
                }



            }
        });



        btnDodge.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (dodgeCooldown <= 0 && !knockback){
                    makeDodge = true;

                    dodgeCooldown = 0.5f;
                }

            }
        });

        inmuneFilter.categoryBits = TwilightHeroes.INMUNE_BIT;
        playerFilter.categoryBits = TwilightHeroes.PLAYER_BIT;




    }

    public void saltar(){
        if ((b2body.body.getLinearVelocity().y == 0 || coyoteTime < 0.1f) && !knockback) {
            coyoteTime += 1f;
            b2body.body.setLinearVelocity(b2body.body.getLinearVelocity().x, 0);
            b2body.body.applyLinearImpulse(new Vector2(0, 3f), b2body.body.getWorldCenter(), true);
        }
    }


    private boolean attacking = false;
    private float attackCooldown = 0f;
    private float speed;

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        b2body = Mappers.b2dCom.get(entity);
        state = Mappers.stateCom.get(entity);
        TextureComponent texture = Mappers.texCom.get(entity);
        animation = Mappers.animCom.get(entity);
        attackComponent = Mappers.atkCom.get(entity);
        PlayerComponent playerComponent = Mappers.playerCom.get(entity);
        knockback = playerComponent.knockback;
        speed = playerComponent.speed;

        accelY = Gdx.input.getAccelerometerZ();
        if (accelY > 20){
            saltar();
        }

if (makeDodge){

    playerComponent.inmune = true;
    dodging = true;
    playerComponent.inmuneTime = 0.4f;
    dodgeTime = 0.3f;
    state.set(StateComponent.STATE_DODGING);
    float dodgeForceX;
    if (touchpad.getKnobPercentX() > 0){
        dodgeForceX = 3f;
    } else if (touchpad.getKnobPercentX() < 0) {
        dodgeForceX = -3f;
    } else{
        dodgeForceX = texture.runningRight ? 3f : -3f;
    }
    b2body.body.setLinearVelocity(0,0);
    b2body.body.setGravityScale(0f);
    b2body.body.applyLinearImpulse(new Vector2(dodgeForceX, 0f), b2body.body.getWorldCenter(), true);
    b2body.body.getFixtureList().get(0).setFilterData(inmuneFilter);
    makeDodge = false;

}
        if (playerComponent.inmune){

            playerComponent.inmuneTime-= deltaTime;
            if ( playerComponent.inmuneTime <= 0){
                playerComponent.inmuneTime = 0f;
                playerComponent.inmune = false;
                dodging = false;
                b2body.body.getFixtureList().get(0).setFilterData(playerFilter);
            }
        }

        if (dodging){
            dodgeTime -= deltaTime;
            if (dodgeTime<= 0){
                dodging = false;

            }
        }else{
            b2body.body.setGravityScale(1f);
            dodgeCooldown -= deltaTime;
            if (knockback) {

                state.set(StateComponent.STATE_DAMAGED);

                playerComponent.knockBackTime -= deltaTime;
                if (playerComponent.knockBackTime <= 0) {
                    b2body.body.setLinearVelocity(new Vector2(0,0));
                    playerComponent.knockback = false;
                    state.set(StateComponent.STATE_IDLE);
                }
            } else {
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
                                attackCooldown = 0.2f;

                                break;
                        }
                        state.time = 0f;

                        if (numAtaqueActual >= numAtaquesDeseados) {
                            numAtaquesDeseados = 0;
                        }

                    } else {

                        if (numAtaqueActual > 0) {
                            attackCooldown = 0.2f;
                        }
                        if (attackCooldown > 0) {
                            attackCooldown -= deltaTime;
                        }
                        numAtaqueActual = 0;
                        numAtaquesLimite = 0;
                        numAtaquesDeseados = 0;
                        attacking = false;
                        if (b2body.body.getLinearVelocity().y < 0) {
                            coyoteTime += deltaTime;
                            state.set(StateComponent.STATE_FALLING);
                            if (state.previousState == StateComponent.STATE_JUMPING) {
                                b2body.body.setGravityScale(2f);
                            }
                        } else if (b2body.body.getLinearVelocity().y == 0 && state.get() != StateComponent.STATE_JUMPING) {
                            coyoteTime = 0f;
                            b2body.body.setGravityScale(1f);

                    /*
                    if (!inputBuffer.isEmpty()) {
                        if (inputBuffer.first() == StateComponent.STATE_JUMPING) {
                            b2body.body.setLinearVelocity(b2body.body.getLinearVelocity().x, 0);
                            b2body.body.applyLinearImpulse(new Vector2(0, playerComponent.jumpPower * deltaTime), b2body.body.getWorldCenter(), true);
                            inputBuffer.removeFirst();
                            state.set(StateComponent.STATE_JUMPING);
                        }
                    }

                     */
                            if (b2body.body.getLinearVelocity().x != 0 && state.get() != StateComponent.STATE_JUMPING) {
                                state.set(StateComponent.STATE_MOVING);


                            } else if (state.get() != StateComponent.STATE_JUMPING) {
                                state.set(StateComponent.STATE_IDLE);
                            }
                        }


                        // Actualiza la dirección del sprite según la velocidad
                        if (!knockback) {
                            if (b2body.body.getLinearVelocity().x > 0 && !texture.runningRight) {
                                texture.runningRight = true;

                            } else if (b2body.body.getLinearVelocity().x < 0 && texture.runningRight) {
                                texture.runningRight = false;

                            }
                        }


                    }
                }
            }


        }

if (!knockback && !dodging) {


    if (touchpad.isTouched() && !attacking) {


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
        float offsetX;
        if (touchpad.getKnobPercentX() > 0){
            texture.runningRight = true;
            offsetX = 16f;
        } else if (touchpad.getKnobPercentX() < 0) {
            texture.runningRight = false;
            offsetX = -16f;
        } else{
            offsetX = texture.runningRight ? 16f : -16f;
        }
        attackShape.setAsBox(12 / TwilightHeroes.PPM, 8 / TwilightHeroes.PPM, new Vector2(offsetX/TwilightHeroes.PPM, 0), 0);
        FixtureDef attackFixtureDef = new FixtureDef();
        attackFixtureDef.shape = attackShape;
attackFixtureDef.filter.categoryBits = TwilightHeroes.HITBOX_BIT;
        attackFixtureDef.isSensor = true; // Configurar la fixture como un sensor
        attackComponent.attackFixture = b2dbody.body.createFixture(attackFixtureDef);
        attackComponent.attackFixture.setUserData("playerAttackSensor");
        // Liberar los recursos del shape
        attackShape.dispose();
        b2body.body.applyForce(new Vector2((texture.runningRight ? 1f : -1f),0f),b2body.body.getWorldCenter(),true);


        }

    }
