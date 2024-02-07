package com.twilightheroes.game.screens;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.twilightheroes.game.TwilightHeroes;
import com.twilightheroes.game.ecs.components.AnimationComponent;
import com.twilightheroes.game.ecs.components.PlayerComponent;
import com.twilightheroes.game.ecs.systems.AnimationSystem;
import com.twilightheroes.game.ecs.systems.BulletSystem;
import com.twilightheroes.game.ecs.systems.CollisionSystem;
import com.twilightheroes.game.ecs.systems.EffectSystem;
import com.twilightheroes.game.ecs.systems.EnemySystem;
import com.twilightheroes.game.ecs.systems.PhysicsDebugSystem;
import com.twilightheroes.game.ecs.systems.PhysicsSystem;
import com.twilightheroes.game.ecs.systems.PlayerControlSystem;
import com.twilightheroes.game.ecs.systems.RenderingSystem;
import com.twilightheroes.game.ecs.systems.SpellSystem;
import com.twilightheroes.game.scenes.Hud;
import com.twilightheroes.game.tools.B2WorldCreator;
import com.twilightheroes.game.tools.B2dContactListener;
import com.twilightheroes.game.tools.BodyFactory;
import com.twilightheroes.game.tools.Mappers;
import com.twilightheroes.game.tools.RoomSize;
import com.twilightheroes.game.tools.WidgetContainer;

import sun.security.util.Debug;


public class MainScreen implements Screen {

    public TwilightHeroes parent;
    private World world;
    private BodyFactory bodyFactory;
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

    private Skin btnDodgeSkin;
    private Button btnDodge;

    private ImageButton.ImageButtonStyle btnSpell1Style = new ImageButton.ImageButtonStyle();
    private ImageButton.ImageButtonStyle btnSpell2Style = new ImageButton.ImageButtonStyle();

    private Button btnSpell1;

    private Button btnSpell2;
    private Button btnPause;

    public B2WorldCreator b2WorldCreator;

    public Entity playerEntity;
    private AssetManager manager;
    Image pantallaNegro;

    private WidgetContainer widgetContainer = new WidgetContainer();

    public MainScreen(TwilightHeroes twilightHeroes){
        parent = twilightHeroes;
        world = new World(new Vector2(0,-9.8f), true);
        world.setContactListener(new B2dContactListener());
        bodyFactory = BodyFactory.getInstance(world);
        manager = parent.assMan.manager;


        gameCam = new OrthographicCamera();
        viewportWidth=12*16;
        viewportHeight=10*16;
        viewport = new ExtendViewport(viewportWidth/TwilightHeroes.PPM,viewportHeight/TwilightHeroes.PPM,gameCam);

        mapLoader = new TmxMapLoader();
        mapRenderer = new OrthogonalTiledMapRenderer(map,1/TwilightHeroes.PPM);

        gameCam.position.set(viewport.getWorldWidth()/2 ,viewport.getWorldHeight()/2,0);
        engine = new PooledEngine();



        sb = new SpriteBatch();

        hud = new Hud(sb,parent.assMan.manager);

        touchpadSkin = new Skin();
        touchpadSkin.add("touchBackground", manager.get("hud/Joystick.png"));
        touchpadSkin.add("touchKnob", manager.get("hud/SmallHandleFilled.png"));

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
        btnJumpSkin.add("jump",manager.get("hud/jump.png"));
        btnJump = new ImageButton(btnJumpSkin.getDrawable("jump"));
        btnJump.setScale(1.5f,1.5f);
        btnJump.setBounds(350, 15, 30, 30);
        hud.stage.addActor(btnJump);

// Boton Ataque
        btnAttackSkin = new Skin();
        btnAttackSkin.add("attack",manager.get("hud/attack.png"));
        btnAttack = new ImageButton(btnAttackSkin.getDrawable("attack"));
btnAttack.setScale(1.5f,1.5f);
        btnAttack.setBounds(300, 15, 30, 30);
        hud.stage.addActor(btnAttack);

hud.stage.setDebugAll(true);

        // Boton Dodge
        btnDodgeSkin = new Skin();
        btnDodgeSkin.add("dodge",manager.get("hud/dodge.png"));
        btnDodge = new ImageButton(btnDodgeSkin.getDrawable("dodge"));
        btnDodge.setScale(1.5f,1.5f);
        btnDodge.setBounds(350, 60, 30, 30);
        hud.stage.addActor(btnDodge);


        actualizarBotones();
        // Boton Habilidad 1
        btnSpell1 = new ImageButton(btnSpell1Style);
        btnSpell1.setScale(1.5f,1.5f);
        btnSpell1.setBounds(300, 120, 30, 30);
        hud.stage.addActor(btnSpell1);

        // Boton Habilidad 2
        btnSpell2 = new ImageButton(btnSpell2Style);
        btnSpell2.setScale(1.5f,1.5f);
        btnSpell2.setBounds(350, 120, 30, 30);
        hud.stage.addActor(btnSpell2);

        // Boton Pause 2

        btnPause = new ImageButton(new TextureRegionDrawable(new TextureRegion(manager.get("hud/pauseButton.png", Texture.class))));
        btnPause.setScale(1.5f,1.5f);
        btnPause.setBounds(350, 170, 30, 30);
        hud.stage.addActor(btnPause);




        // add all the relevant systems our engine should run
        engine.addSystem(new AnimationSystem());
        engine.addSystem(renderingSystem);
        engine.addSystem(new PhysicsSystem(world,engine,this));
        engine.addSystem(new PhysicsDebugSystem(world, renderingSystem.getCamera()));
        engine.addSystem(new CollisionSystem(renderingSystem,this));
        engine.addSystem(new PlayerControlSystem(touchpad,btnJump,btnAttack,btnDodge, btnSpell1, btnSpell2,btnPause,this));
        engine.addSystem(new EnemySystem(this));
        engine.addSystem(new EffectSystem());
        engine.addSystem(new BulletSystem(this));
        engine.addSystem(new SpellSystem(this));


        b2WorldCreator = new B2WorldCreator(world,engine,this,manager);
        // create some game objects
        Drawable drawable = new TextureRegionDrawable(new TextureRegion(manager.get("pantallaNegra.jpg", Texture.class)));
        pantallaNegro = new Image();
        pantallaNegro.setDrawable(drawable);
        pantallaNegro.setBounds(0,0, viewportWidth*TwilightHeroes.PPM,viewportHeight*TwilightHeroes.PPM);
        pantallaNegro.setColor(0,0,0,0f);
        pantallaNegro.setVisible(false);
        hud.stage.addActor(pantallaNegro);

        newMap = 0;
changeMap();
        parent.widgets.add(widgetContainer);


    }




    @Override
    public void show() {
        Gdx.input.setInputProcessor(hud.stage);
        newMap = parent.playerSettings.level;

    }

    public void actualizarBotones(){


        btnSpell1Style.up = new TextureRegionDrawable((Texture) parent.assMan.manager.get("spells/" + parent.playerSettings.spell1 + ".png"));
        btnSpell2Style.up = new TextureRegionDrawable((Texture) parent.assMan.manager.get("spells/" + parent.playerSettings.spell2 + ".png"));

        if (btnSpell1 != null){
            btnSpell1.setStyle(btnSpell1Style);
            btnSpell2.setStyle(btnSpell2Style);
        }

    }

    private String[] maps = {"map0","mapa1","mapa2","mapa3"};
    public Array<Body> bodies = new Array<>();
    public boolean change = false;
    public int newMap;
    // Once this reaches 1.0f the next scene is shown
    private float alpha = 0;
    // true if fade in, false if fade out
    private boolean fadeDirection = true;

    private RoomSize roomSize;

    private void changeMap(){
        auxChangeMap = 5;
       change = false;
        if (map != null){
            map.dispose();
        }
        for (int i = bodies.size - 1; i >= 0; i--) {
            world.destroyBody(bodies.get(i));
        }


        engine.clearPools();
        Array<Entity> entitiesToRemove = new Array<>();
        for (Entity ent: engine.getEntities())
        {
            if (Mappers.playerCom.get(ent) == null){
                entitiesToRemove.add(ent);
            }
        }
        for (Entity ent: entitiesToRemove)
        {
            engine.removeEntity(ent);
        }
        entitiesToRemove.clear();
        bodies.clear();



            map = mapLoader.load(new String("maps/"+maps[newMap] + ".tmx"));
          roomSize =  b2WorldCreator.generateLevel(map,playerEntity);
            renderingSystem.updateRoom(roomSize);
            mapRenderer.setMap(map);

            parent.playerSettings.level = newMap;



    }

    public  int auxChangeMap = 0;

    @Override
    public void render(float delta) {
        //check if player is dead. if so show end screen
        PlayerComponent pc = (playerEntity.getComponent(PlayerComponent.class));
        if(pc.isDead){
            parent.changeScreen(TwilightHeroes.ENDGAME);
        }

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
        hud.update(playerEntity);
        if (auxChangeMap > 0){
            auxChangeMap--;
        }

        if (change){
            changeMap();
            transitionTime = 1f;
        }
        if (transitionTime>0f){
            pantallaNegro.setColor(0,0,0,transitionTime);
            transitionTime -= delta;
            pantallaNegro.setVisible(true);

        }else{
            pantallaNegro.setVisible(false);

        }


    }
    private float transitionTime = 0f;

    @Override
    public void resize(int width, int height) {
   renderingSystem.resize(width,height);

    }



    @Override
    public void pause() {

    }

    @Override
    public void resume() {
        hud.stage.unfocusAll();

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        engine.clearPools();
        engine.removeAllEntities();
        map.dispose();
        mapRenderer.dispose();


    }


}
