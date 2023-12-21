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
import com.badlogic.gdx.physics.box2d.Fixture;
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
import com.twilightheroes.game.ecs.components.AttackComponent;
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

    private PooledEngine engine;

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
        map = mapLoader.load("maps/nivelPlataformas.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map,1/TwilightHeroes.PPM);

        gameCam.position.set(viewport.getWorldWidth()/2 ,viewport.getWorldHeight()/2,0);
        engine = new PooledEngine();
        new B2WorldCreator(world,map,engine,atlas);



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


    }

    public void crearTouchpad(float x, float y){
        System.out.println("Crear touchpad en " + x + "  " + y);
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
        hud.update();

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
