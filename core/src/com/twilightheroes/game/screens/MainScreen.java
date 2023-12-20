package com.twilightheroes.game.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.twilightheroes.game.TwilightHeroes;
import com.twilightheroes.game.ecs.components.AnimationComponent;
import com.twilightheroes.game.ecs.components.B2dBodyComponent;
import com.twilightheroes.game.ecs.components.CollisionComponent;
import com.twilightheroes.game.ecs.components.PlayerComponent;
import com.twilightheroes.game.ecs.components.StateComponent;
import com.twilightheroes.game.ecs.components.TextureComponent;
import com.twilightheroes.game.ecs.components.TypeComponent;
import com.twilightheroes.game.ecs.systems.AnimationSystem;
import com.twilightheroes.game.ecs.systems.CollisionSystem;
import com.twilightheroes.game.ecs.systems.PhysicsDebugSystem;
import com.twilightheroes.game.ecs.systems.PhysicsSystem;
import com.twilightheroes.game.ecs.systems.PlayerControlSystem;
import com.twilightheroes.game.ecs.systems.RenderingSystem;
import com.twilightheroes.game.scenes.Hud;
import com.twilightheroes.game.tools.AnimationMaker;
import com.twilightheroes.game.tools.B2WorldCreator;
import com.twilightheroes.game.tools.B2dContactListener;
import com.twilightheroes.game.tools.BodyFactory;
import com.twilightheroes.game.tools.KeyboardController;
import com.twilightheroes.game.tools.MyInputProcessor;


public class MainScreen implements Screen {

    private TwilightHeroes parent;
    private KeyboardController controller;
    private World world;
    private BodyFactory bodyFactory;
    private TextureAtlas atlas;
    private SpriteBatch sb;

    private Engine engine;

   // private Viewport viewport;

 //   private float viewportWidth,viewportHeight;

    private Hud hud;

    private Touchpad touchpad;
    private Skin touchpadSkin;

    private  RenderingSystem renderingSystem;
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    private Viewport viewport;
private     float viewportWidth,viewportHeight;
    private OrthographicCamera gameCam;

    private float dt;
    private     Skin btnJumpSkin;
    private Button btnJump;

    private  Skin btnAttackSkin;
    private Button btnAttack;

    public MyInputProcessor myInputProcessor;

    public MainScreen(TwilightHeroes twilightHeroes){
        parent = twilightHeroes;
        controller = new KeyboardController();
        world = new World(new Vector2(0,-9.8f), true);
        world.setContactListener(new B2dContactListener());
        bodyFactory = BodyFactory.getInstance(world);



        atlas = new TextureAtlas(Gdx.files.internal("jugador.atlas"));

        gameCam = new OrthographicCamera();
        viewportWidth=12*16;
        viewportHeight=10*16;
        viewport = new ExtendViewport(viewportWidth/TwilightHeroes.PPM,viewportHeight/TwilightHeroes.PPM,gameCam);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("mapa2.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map,1/TwilightHeroes.PPM);

        gameCam.position.set(viewport.getWorldWidth()/2 ,viewport.getWorldHeight()/2,0);
        engine = new PooledEngine();
        new B2WorldCreator(world,map,engine);



        sb = new SpriteBatch();

        hud = new Hud(sb);

        touchpadSkin = new Skin();
        touchpadSkin.add("touchBackground", new Texture("Joystick.png"));
        touchpadSkin.add("touchKnob", new Texture("SmallHandleFilled.png"));

        Touchpad.TouchpadStyle  touchpadStyle = new Touchpad.TouchpadStyle();
        touchpadStyle.background = touchpadSkin.getDrawable("touchBackground");
        touchpadStyle.knob = touchpadSkin.getDrawable("touchKnob");
        touchpadStyle.knob.setMinWidth(10f);
        touchpadStyle.knob.setMinHeight(10f);
        touchpad = new Touchpad(1f, touchpadStyle);

        touchpad.setBounds(15, 15, 50, 50);
        touchpad.setScale(1.2f,1.2f);
        touchpad.setOrigin(Align.center);
        hud.stage.addActor(touchpad);

         myInputProcessor = new MyInputProcessor(viewport.getScreenWidth(), viewport.getScreenHeight(),this);
        Gdx.input.setInputProcessor(hud.stage);

        // Create our new rendering system
        renderingSystem = new RenderingSystem(sb,gameCam,viewport);
        gameCam = renderingSystem.getCamera();
        sb.setProjectionMatrix(gameCam.combined);
       // viewportWidth=12*16;
      // viewportHeight=10*16;
     //   viewport = new ExtendViewport(viewportWidth/TwilightHeroes.PPM,viewportHeight/TwilightHeroes.PPM,cam);
        //create a pooled engineHud

        // Boton Salto
        btnJumpSkin = new Skin();
        btnJumpSkin.add("jump", new Texture("jump.png"));
        btnJump = new ImageButton(btnJumpSkin.getDrawable("jump"));
        btnJump.setScale(1.5f,1.5f);
        btnJump.setBounds(350, 15, 30, 30);
        hud.stage.addActor(btnJump);

// Boton Ataque
        btnAttackSkin = new Skin();
        btnAttackSkin.add("attack", new Texture("attack.png"));
        btnAttack = new ImageButton(btnAttackSkin.getDrawable("attack"));
btnAttack.setScale(1.5f,1.5f);
        btnAttack.setBounds(300, 15, 30, 30);
        hud.stage.addActor(btnAttack);
hud.stage.setDebugAll(true);



        // add all the relevant systems our engine should run
        engine.addSystem(new AnimationSystem());
        engine.addSystem(renderingSystem);
        engine.addSystem(new PhysicsSystem(world));
        engine.addSystem(new PhysicsDebugSystem(world, renderingSystem.getCamera()));
        engine.addSystem(new CollisionSystem(renderingSystem));
        engine.addSystem(new PlayerControlSystem(touchpad,btnJump,btnAttack));

        // create some game objects
        createPlayer();

    }

    public void crearTouchpad(float x, float y){
        System.out.println("Crear touchpad en " + x + "  " + y);
    }

    public void createPlayer(){
        // Create the Entity and all the components that will go in the entity
        Entity entity = engine.createEntity();
        B2dBodyComponent b2dbody = engine.createComponent(B2dBodyComponent.class);
      //  TransformComponent position = engine.createComponent(TransformComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        PlayerComponent player = engine.createComponent(PlayerComponent.class);
        CollisionComponent colComp = engine.createComponent(CollisionComponent.class);
        TypeComponent type = engine.createComponent(TypeComponent.class);
        StateComponent stateCom = engine.createComponent(StateComponent.class);
        AnimationComponent animCom = engine.createComponent(AnimationComponent.class);

        // create the data for the components and add them to the components
        texture.sprite.setRegion(atlas.findRegion("Idle-Sheet"));

        texture.sprite.setBounds(2, 1,35/TwilightHeroes.PPM,47/TwilightHeroes.PPM);

        type.type = TypeComponent.PLAYER;
        stateCom.set(StateComponent.STATE_NORMAL);
        stateCom.isLooping = true;
        colComp.collisionEntity = entity;

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(texture.sprite.getX(),texture.sprite.getY());
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        b2dbody.body = world.createBody(bodyDef);
        b2dbody.body.setFixedRotation(true);

        // Define la hitbox rectangular
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(4 / TwilightHeroes.PPM, 8 / TwilightHeroes.PPM); // Tamaño de la hitbox

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;

        b2dbody.body.createFixture(fixtureDef);

        FixtureDef fdef2 = new FixtureDef();
        EdgeShape feet = new EdgeShape();
        feet.set(new Vector2(-4 / TwilightHeroes.PPM, -12 / TwilightHeroes.PPM), new Vector2(4 / TwilightHeroes.PPM, -12 / TwilightHeroes.PPM));
        fdef2.shape = feet;
        b2dbody.body.createFixture(fdef2);

        // Libera los recursos del shape
        shape.dispose();

        /*
        b2dbody.body = bodyFactory.makeBoxPolyBody(
                texture.sprite.getX(), position.position.y,
                4/TwilightHeroes.PPM,  // Ancho escalado
                8/TwilightHeroes.PPM,  // Altura escalada
                BodyFactory.STONE, BodyDef.BodyType.DynamicBody
        );

         */
        b2dbody.body.setUserData(entity);

        // Create the animation and add it to AnimationComponent

        TextureAtlas.AtlasRegion atlasRegion = atlas.findRegion("Idle-Sheet");

        TextureRegion[][] tmpIdle = atlasRegion.split(atlasRegion.getRegionWidth()/4,atlasRegion.getRegionHeight());
        TextureRegion[] idleFrames = new TextureRegion[4];
        for (int i = 0; i < 4; i++) {
            idleFrames[i] = tmpIdle[0][i];
        }
        animCom.animations.put(StateComponent.STATE_NORMAL, AnimationMaker.crearAnimacion(atlas,"Idle-Sheet",4));
        animCom.animations.put(StateComponent.STATE_MOVING, AnimationMaker.crearAnimacion(atlas,"Run-Sheet",8));
        animCom.animations.put(StateComponent.STATE_ATTACK, AnimationMaker.crearAnimacion(atlas,"ataque1",4));
        /*
        runSheet = new Texture("Run-Sheet.png");
        idleSheet = new Texture("Idle-Sheet.png");
        jumpSheet = new Texture("Jump-Start-Sheet.png");
        attackSheet = new Texture("attack-sheet.png");

        TextureRegion[][] tmpRun = TextureRegion.split(runSheet,runSheet.getWidth() / 8,runSheet.getHeight());

        TextureRegion[] runFrames = new TextureRegion[8];
        for (int i = 0; i < 8; i++) {
            runFrames[i] = tmpRun[0][i];
        }
        therionRun = new Animation<TextureRegion>(1f/8f,runFrames);

        TextureRegion[][] tmpIdle = TextureRegion.split(idleSheet,idleSheet.getWidth() / 4,idleSheet.getHeight());
        TextureRegion[] idleFrames = new TextureRegion[4];
        for (int i = 0; i < 4; i++) {
            idleFrames[i] = tmpIdle[0][i];
        }
        therionIdle = new Animation<TextureRegion>(1f/4f,idleFrames);


        TextureRegion[][] tmpJump = TextureRegion.split(jumpSheet,jumpSheet.getWidth() / 4,jumpSheet.getHeight());
        TextureRegion[] jumpFrames = new TextureRegion[4];
        for (int i = 0; i < 4; i++) {
            jumpFrames[i] = tmpJump[0][i];
        }
        therionJump = new Animation<TextureRegion>(1f/4f,jumpFrames);


        TextureRegion[][] tmpAttack = TextureRegion.split(attackSheet,attackSheet.getWidth() / 8,attackSheet.getHeight());
        TextureRegion[] attackFrames = new TextureRegion[8];
        for (int i = 0; i < 8; i++) {
            attackFrames[i] = tmpAttack[0][i];
        }
        therionAttack = new Animation<TextureRegion>(1f/8f,attackFrames);


         */
        entity.add(b2dbody);
        entity.add(texture);
        entity.add(player);  // Add PlayerComponent to the entity
        entity.add(colComp);
        entity.add(type);
        entity.add(stateCom);
        entity.add(animCom);


// add the entity to the engine
        engine.addEntity(entity);

    }



    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render the background (tiled map) first
        mapRenderer.setView(gameCam);
        mapRenderer.render();

        // Update and render the game entities
dt = delta;
        engine.update(delta);

        // Render the HUD on top
        hud.stage.act(delta);
        hud.stage.draw();

    }

    @Override
    public void resize(int width, int height) {
   renderingSystem.resize(width,height);
   myInputProcessor.resize(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        hud.dispose();
        map.dispose();
        mapRenderer.dispose();
    }
}
