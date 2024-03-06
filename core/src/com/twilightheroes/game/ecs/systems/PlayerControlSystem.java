package com.twilightheroes.game.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.twilightheroes.game.TwilightHeroes;
import com.twilightheroes.game.ecs.components.AnimationComponent;
import com.twilightheroes.game.ecs.components.AttackComponent;
import com.twilightheroes.game.ecs.components.B2dBodyComponent;
import com.twilightheroes.game.ecs.components.PlayerComponent;
import com.twilightheroes.game.ecs.components.StateComponent;
import com.twilightheroes.game.ecs.components.StatsComponent;
import com.twilightheroes.game.ecs.components.TextureComponent;
import com.twilightheroes.game.ecs.components.spells.SpellComponent;
import com.twilightheroes.game.screens.MainScreen;
import com.twilightheroes.game.tools.Mappers;

/**
 * The system that handles the player controls
 */
public class PlayerControlSystem extends IteratingSystem {

    /**
     * The Touchpad.
     */
    final Touchpad touchpad;
    /**
     * The Jump Button.
     */
    final Button btnSaltar;
    /**
     * The Attack Button.
     */
    final Button btnAtacar;
    /**
     * The Dodge Button.
     */
    final Button btnDodge;
    /**
     * A filter that sets the player immune
     */
    private final Filter inmuneFilter = new Filter();
    /**
     * A filter that sets the player as a player
     */
    private final Filter playerFilter = new Filter();
    /**
     * the Mainscreen
     */
    private final MainScreen screen;
    private final Sound sound;
    private final long soundId;
    /**
     * If the player can dodge.
     */
    public boolean dodging = false;
    /**
     * If the player request a dodge.
     */
    public boolean makeDodge = false;
    /**
     * The Dodge time.
     */
    public float dodgeTime = 0f;
    /**
     * The Dodge cooldown.
     */
    public float dodgeCooldown = 0f;
    /**
     * The B2BodyComponent of the player.
     */
    B2dBodyComponent b2body;
    /**
     * The StateComponent of the player.
     */
    StateComponent state;
    /**
     * The Attack component of the player.
     */
    AttackComponent attackComponent;
    /**
     * The Animation component of the player.
     */
    AnimationComponent animation;
    /**
     * The player component of the player.
     */
    private PlayerComponent playerComponent;
    /**
     * The spell component of the player
     */
    private SpellComponent spellComponent;
    /**
     * the number of the actual attack
     */
    private int numAtaqueActual = 0;
    /**
     * how many attacks are requested
     */
    private int numAtaquesDeseados = 0;
    /**
     * the limit of attacks a player can do in a single combo
     */
    private int numAtaquesLimite = 0;
    /**
     * number of attacks of the current combo
     */
    private int numAtaquesCompletados = 0;
    /**
     * if the player is in knockback
     */
    private boolean knockback;
    /**
     * if the player is attacking
     */
    private boolean attacking = false;
    /**
     * the cooldown between attacks
     */
    private float attackCooldown = 0f;
    /**
     * the cooldoown between interactions
     */
    private float interactCooldown = 0f;


    /**
     * Instantiates a new Player control system.
     *
     * @param touchpad       the touchpad
     * @param btnSaltar      the btn saltar
     * @param btnAtacar      the btn atacar
     * @param btnDodge       the btn dodge
     * @param btnHabilidad1  the btn habilidad 1
     * @param btnHabilidad2  the btn habilidad 2
     * @param interactButton the interact button
     * @param btnPause       the btn pause
     * @param screen         the screen
     */
    public PlayerControlSystem(Touchpad touchpad, Button btnSaltar, Button btnAtacar, Button btnDodge, Button btnHabilidad1, Button btnHabilidad2, Button interactButton, Button btnPause, final MainScreen screen) {
        super(Family.all(PlayerComponent.class).get());
        this.touchpad = touchpad;
        this.btnSaltar = btnSaltar;
        this.btnAtacar = btnAtacar;
        this.btnDodge = btnDodge;
        this.screen = screen;


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

                if (playerComponent.canAttack) {

                    if (numAtaquesLimite < 3 && !knockback && attackCooldown <= 0f) {

                        numAtaquesLimite++;
                        numAtaquesDeseados++;
                        attacking = true;

                    }
                }


            }
        });


        btnDodge.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (dodgeCooldown <= 0 && !knockback && playerComponent.canDodge && !attacking) {
                    makeDodge = true;
                    playerComponent.canAttack = false;
                    playerComponent.canDodge = false;
                    playerComponent.canJump = false;
                    dodgeCooldown = 0.7f;

                }

            }
        });


        inmuneFilter.categoryBits = TwilightHeroes.INMUNE_BIT;
        playerFilter.categoryBits = TwilightHeroes.PLAYER_BIT;


        btnHabilidad1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {


                if (playerComponent.mana >= spellComponent.spell1.manaCost && spellComponent.spellToCast == null) {
                    playerComponent.mana -= spellComponent.spell1.manaCost;


                    spellComponent.spellToCast = spellComponent.spell1;
                    playerComponent.canAttack = false;
                    playerComponent.canDodge = false;
                    playerComponent.canJump = false;
                }


            }


        });

        btnHabilidad2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                if (playerComponent.mana >= spellComponent.spell2.manaCost && spellComponent.spellToCast == null) {
                    playerComponent.mana -= spellComponent.spell2.manaCost;
                    spellComponent.spellToCast = spellComponent.spell2;
                    playerComponent.canAttack = false;
                    playerComponent.canDodge = false;
                    playerComponent.canJump = false;

                }


            }
        });

        btnPause.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                screen.pause();
                screen.parent.changeScreen(TwilightHeroes.MAGIC);
            }
        });

        interactButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                createInteractFixture(b2body, 20f, 8, 0f, 0f);

            }
        });

        sound = screen.parent.assMan.manager.get("sfx/walk.wav", Sound.class);
        soundId = sound.play();
        sound.setLooping(soundId, true);
        sound.pause(soundId);
    }

    /**
     * The functions that handles the player jump.
     */
    public void saltar() {
        if ((playerComponent.canJump || playerComponent.coyoteTime < 0.1f) && !knockback && !attacking) {
            playerComponent.canJump = false;
            playerComponent.coyoteTime += 1f;
            b2body.body.setLinearVelocity(b2body.body.getLinearVelocity().x, 0);
            b2body.body.applyLinearImpulse(new Vector2(0, 3.2f), b2body.body.getWorldCenter(), true);
            screen.parent.assMan.manager.get("sfx/jump.wav", Sound.class).play();
        }
    }

    /**
     * This method is called on every entity on every update call of the EntitySystem.
     *
     * @param entity    The current Entity being processed
     * @param deltaTime The delta time between the last and current frame
     */
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        b2body = Mappers.b2dCom.get(entity);
        state = Mappers.stateCom.get(entity);
        TextureComponent texture = Mappers.texCom.get(entity);
        animation = Mappers.animCom.get(entity);
        attackComponent = Mappers.atkCom.get(entity);
        playerComponent = Mappers.playerCom.get(entity);
        StatsComponent stats = Mappers.statsCom.get(entity);
        spellComponent = Mappers.spellCom.get(entity);

        if (stats.hp <= 0) {
            playerComponent.isDead = true;
        }

        if (stats.hp > 100) {
            stats.hp = 100;
        }
        if (playerComponent.mana > 100) {
            playerComponent.mana = 100;
        }
        if (interactCooldown <= 0) {

            if (playerComponent.interactFixture != null) {
                b2body.body.destroyFixture(playerComponent.interactFixture);
                playerComponent.interactFixture = null;
            }
        } else {
            interactCooldown -= deltaTime;
        }

        knockback = playerComponent.knockback;
        float speed = stats.speed;

        if (screen.parent.accelerometerOn) {


            float accelY = Gdx.input.getAccelerometerZ();
            if (accelY > 20) {
                saltar();
            }

        }
        if (makeDodge) {

            playerComponent.immune = true;
            dodging = true;
            playerComponent.immuneTime = 0.5f;
            dodgeTime = 0.1f;
            float dodgeForceX;
            if (touchpad.getKnobPercentX() > 0) {
                dodgeForceX = 3f;
            } else if (touchpad.getKnobPercentX() < 0) {
                dodgeForceX = -3f;
            } else {
                dodgeForceX = texture.runningRight ? 3f : -3f;
            }

            b2body.body.setLinearVelocity(0, 0);
            b2body.body.setGravityScale(0f);
            b2body.body.applyLinearImpulse(new Vector2(dodgeForceX, 0f), b2body.body.getWorldCenter(), true);
            b2body.body.getFixtureList().get(0).setFilterData(inmuneFilter);
            makeDodge = false;
            texture.sprite.setAlpha(0);
            screen.parent.assMan.manager.get("sfx/dash.wav", Sound.class).play();

        }

        if (playerComponent.immune) {

            playerComponent.immuneTime -= deltaTime;
            if (playerComponent.immuneTime <= 0) {
                playerComponent.immuneTime = 0f;
                playerComponent.immune = false;
                dodging = false;
                b2body.body.getFixtureList().get(0).setFilterData(playerFilter);
            }
        }

        if (dodging) {
            dodgeTime -= deltaTime;
            if (dodgeTime <= 0) {
                dodging = false;

            }
        } else {
            dodgeCooldown -= deltaTime;
            if (dodgeCooldown <= 0) {
                playerComponent.canDodge = true;

            }
            texture.sprite.setAlpha(1);
            if (knockback) {

                state.set(StateComponent.STATE_DAMAGED);

                playerComponent.knockBackTime -= deltaTime;
                if (playerComponent.knockBackTime <= 0) {
                    b2body.body.setLinearVelocity(new Vector2(0, 0));
                    playerComponent.knockback = false;
                    state.set(StateComponent.STATE_IDLE);
                }
            } else {
                if ((state.get() == StateComponent.STATE_ATTACK01 || state.get() == StateComponent.STATE_ATTACK02 || state.get() == StateComponent.STATE_ATTACK03) && !animation.animations.get(state.get()).isAnimationFinished(state.time)) {
                    playerComponent.canJump = false;
                } else {
                    if (spellComponent.castingTime <= 0) {
                        state.isLooping = true;

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
                                    createAttackFixture(texture, b2body, attackComponent, 12f, 8, 16f, 0f);
                                    screen.parent.assMan.manager.get("sfx/sword.mp3", Sound.class).play();
                                    break;
                                case 2:
                                    state.set(StateComponent.STATE_ATTACK02);
                                    createAttackFixture(texture, b2body, attackComponent, 12f, 8, 16f, 0f);
                                    screen.parent.assMan.manager.get("sfx/sword.mp3", Sound.class).play();
                                    break;
                                case 3:
                                    state.set(StateComponent.STATE_ATTACK03);
                                    createAttackFixture(texture, b2body, attackComponent, 12f, 8, 16f, 0f);
                                    screen.parent.assMan.manager.get("sfx/sword.mp3", Sound.class).play();
                                    attackCooldown = 0.5f;
                                    break;
                            }
                            state.time = 0f;

                            if (numAtaqueActual >= numAtaquesDeseados) {
                                numAtaquesCompletados++;
                                if (numAtaquesCompletados >= numAtaquesDeseados) {
                                    // Restablecer las variables después de completar todos los ataques
                                    numAtaquesDeseados = 0;
                                    numAtaquesCompletados = 0;
                                }
                            }

                        } else {
                            b2body.body.setGravityScale(1f);
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
                            playerComponent.canAttack = true;

                            if (b2body.body.getLinearVelocity().y < 0) {
                                playerComponent.coyoteTime += deltaTime;
                                state.set(StateComponent.STATE_FALLING);


                            } else if (b2body.body.getLinearVelocity().y == 0 && state.get() != StateComponent.STATE_JUMPING) {


                                if (b2body.body.getLinearVelocity().x != 0 && state.get() != StateComponent.STATE_JUMPING) {
                                    state.set(StateComponent.STATE_MOVING);


                                } else if (state.get() != StateComponent.STATE_JUMPING) {
                                    state.set(StateComponent.STATE_IDLE);
                                }
                            }


                            // Actualiza la dirección del sprite según la velocidad
                            if (!knockback && !dodging) {
                                if (b2body.body.getLinearVelocity().x > 0 && !texture.runningRight) {
                                    texture.runningRight = true;

                                } else if (b2body.body.getLinearVelocity().x < 0 && texture.runningRight) {
                                    texture.runningRight = false;

                                }
                            }


                        }
                    } else {
                        playerComponent.canJump = false;
                        playerComponent.canDodge = false;

                    }
                }


            }

            if (!knockback && !dodging) {


                if (touchpad.isTouched() && !attacking) {


                    float knobPercentX = touchpad.getKnobPercentX();

                    // Puedes ajustar la velocidad según tus necesidades
                    float velocityX = knobPercentX * speed * deltaTime;

                    // Aplica la velocidad al cuerpo del jugador
                    if (state.get() != StateComponent.STATE_ATTACK01 && state.get() != StateComponent.STATE_ATTACK02 && state.get() != StateComponent.STATE_ATTACK03 && state.get() != StateComponent.STATE_CASTING) {
                        b2body.body.setLinearVelocity(new Vector2(velocityX, b2body.body.getLinearVelocity().y));
                    }

                } else {
                    b2body.body.setLinearVelocity(new Vector2(0, b2body.body.getLinearVelocity().y));
                    sound.pause(soundId);

                }

            }

        }
    }


    /**
     * Create the attackFixture for the player
     *
     * @param texture         Texture of the player
     * @param b2dbody         Body of the player
     * @param attackComponent AttackComponent of the player
     * @param hx              Width of the attackFixture
     * @param hy              Height of the attackFixture
     * @param offsetX         OffsetX of the attackFixture
     * @param offsetY         OffsetY of the attackFixture
     */
    private void createAttackFixture(TextureComponent texture, B2dBodyComponent b2dbody, AttackComponent attackComponent, float hx, float hy, float offsetX, float offsetY) {
        PolygonShape attackShape = new PolygonShape();
        float auxOffsetX;

        if (touchpad.getKnobPercentX() > 0) {
            texture.runningRight = true;
            auxOffsetX = offsetX;
        } else if (touchpad.getKnobPercentX() < 0) {
            texture.runningRight = false;
            auxOffsetX = -offsetX;
        } else {
            auxOffsetX = texture.runningRight ? offsetX : -offsetX;
        }

        attackShape.setAsBox(hx / TwilightHeroes.PPM, hy / TwilightHeroes.PPM, new Vector2(auxOffsetX / TwilightHeroes.PPM, offsetY / TwilightHeroes.PPM), 0);
        FixtureDef attackFixtureDef = new FixtureDef();
        attackFixtureDef.shape = attackShape;
        attackFixtureDef.filter.categoryBits = TwilightHeroes.HITBOX_BIT;
        attackFixtureDef.isSensor = true; // Configurar la fixture como un sensor
        attackComponent.attackFixture = b2dbody.body.createFixture(attackFixtureDef);
        attackComponent.attackFixture.setUserData("playerAttackSensor");
        // Liberar los recursos del shape
        attackShape.dispose();


        b2body.body.applyLinearImpulse(new Vector2(0.001f, 0), b2body.body.getWorldCenter(), true);

    }

    /**
     * Create the interact fixture for the player
     *
     * @param b2dbody Body of the player
     * @param hx      Width of the interactFixture
     * @param hy      Height of the interactFixture
     * @param offsetX OffsetX of the interactFixture
     * @param offsetY OffsetY of the interactFixture
     */

    private void createInteractFixture(B2dBodyComponent b2dbody, float hx, float hy, float offsetX, float offsetY) {
        PolygonShape interactShape = new PolygonShape();


        interactShape.setAsBox(hx / TwilightHeroes.PPM, hy / TwilightHeroes.PPM, new Vector2(offsetX / TwilightHeroes.PPM, offsetY / TwilightHeroes.PPM), 0);
        FixtureDef interactFixture = new FixtureDef();
        interactFixture.shape = interactShape;
        interactFixture.filter.categoryBits = TwilightHeroes.HITBOX_BIT;
        interactFixture.isSensor = true; // Configurar la fixture como un sensor
        playerComponent.interactFixture = b2dbody.body.createFixture(interactFixture);
        playerComponent.interactFixture.setUserData("playerInteractSensor");
        // Liberar los recursos del shape
        interactShape.dispose();

        interactCooldown = 0.001f;

        b2body.body.applyLinearImpulse(new Vector2(0f, -0.1f), b2body.body.getWorldCenter(), true);


    }


}
