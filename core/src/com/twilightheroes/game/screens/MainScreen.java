package com.twilightheroes.game.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.twilightheroes.game.TwilightHeroes;
import com.twilightheroes.game.ecs.components.AnimationComponent;
import com.twilightheroes.game.ecs.components.B2dBodyComponent;
import com.twilightheroes.game.ecs.components.CollisionComponent;
import com.twilightheroes.game.ecs.components.PlayerComponent;
import com.twilightheroes.game.ecs.components.StateComponent;
import com.twilightheroes.game.ecs.components.TextureComponent;
import com.twilightheroes.game.ecs.components.TransformComponent;
import com.twilightheroes.game.ecs.components.TypeComponent;
import com.twilightheroes.game.ecs.systems.AnimationSystem;
import com.twilightheroes.game.ecs.systems.CollisionSystem;
import com.twilightheroes.game.ecs.systems.PhysicsDebugSystem;
import com.twilightheroes.game.ecs.systems.PhysicsSystem;
import com.twilightheroes.game.ecs.systems.PlayerControlSystem;
import com.twilightheroes.game.ecs.systems.RenderingSystem;
import com.twilightheroes.game.tools.BodyFactory;
import com.twilightheroes.game.tools.KeyboardController;


public class MainScreen implements Screen {

    private TwilightHeroes parent;
    private KeyboardController controller;
    private World world;
    private BodyFactory bodyFactory;
    private TextureAtlas atlas;
    private SpriteBatch sb;

    private OrthographicCamera cam;
    private Engine engine;

    private Viewport viewport;

    private float viewportWidth,viewportHeight;



    public MainScreen(TwilightHeroes twilightHeroes){
        parent = twilightHeroes;
        controller = new KeyboardController();
        world = new World(new Vector2(0,0f), true);
        bodyFactory = BodyFactory.getInstance(world);

        atlas = new TextureAtlas(Gdx.files.internal("jugador.atlas"));



        sb = new SpriteBatch();
        // Create our new rendering system
        RenderingSystem renderingSystem = new RenderingSystem(sb);
        cam = renderingSystem.getCamera();
        sb.setProjectionMatrix(cam.combined);
        viewportWidth=12*16;
        viewportHeight=10*16;
        viewport = new ExtendViewport(viewportWidth/TwilightHeroes.PPM,viewportHeight/TwilightHeroes.PPM,cam);
        //create a pooled engine
        engine = new PooledEngine();

        // add all the relevant systems our engine should run
        engine.addSystem(new AnimationSystem());
        engine.addSystem(renderingSystem);
        engine.addSystem(new PhysicsSystem(world));
        engine.addSystem(new PhysicsDebugSystem(world, renderingSystem.getCamera()));
        engine.addSystem(new CollisionSystem());
        engine.addSystem(new PlayerControlSystem(controller));

        // create some game objects
        createPlayer();

    }

    public void createPlayer(){
        // Create the Entity and all the components that will go in the entity
        Entity entity = engine.createEntity();
        B2dBodyComponent b2dbody = engine.createComponent(B2dBodyComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        PlayerComponent player = engine.createComponent(PlayerComponent.class);
        CollisionComponent colComp = engine.createComponent(CollisionComponent.class);
        TypeComponent type = engine.createComponent(TypeComponent.class);
        StateComponent stateCom = engine.createComponent(StateComponent.class);
        AnimationComponent animCom = engine.createComponent(AnimationComponent.class);

        // create the data for the components and add them to the components
        // set object position (x,y,z) z used to define draw order 0 first drawn
        position.position.set(0.12f, 0.64f);
        position.scale.set(0.1f, 0.1f); // Ajusta estos valores según tus necesidades
        texture.region = atlas.findRegion("Idle-Sheet");
        type.type = TypeComponent.PLAYER;
        stateCom.set(StateComponent.STATE_NORMAL);
        stateCom.isLooping = true;
        b2dbody.body = bodyFactory.makeBoxPolyBody(
                position.position.x, position.position.y,
                4 * position.scale.x / TwilightHeroes.PPM,  // Ancho escalado
                8 * position.scale.y / TwilightHeroes.PPM,  // Altura escalada
                BodyFactory.STONE, BodyDef.BodyType.DynamicBody
        );
        b2dbody.body.setUserData(entity);

        // Create the animation and add it to AnimationComponent

        TextureAtlas.AtlasRegion atlasRegion = atlas.findRegion("Idle-Sheet");

        TextureRegion[][] tmpIdle = atlasRegion.split(atlasRegion.getRegionWidth()/4,atlasRegion.getRegionHeight());
        TextureRegion[] idleFrames = new TextureRegion[4];
        for (int i = 0; i < 4; i++) {
            idleFrames[i] = tmpIdle[0][i];
        }
        animCom.animations.put(StateComponent.STATE_NORMAL,new Animation<TextureRegion>(1f/4f,idleFrames));


        // add the components to the entity
        entity.add(b2dbody);
        entity.add(position);
        entity.add(texture);
        entity.add(player);
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

        engine.update(delta);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height,false);
        viewport.getCamera().position.set(viewportWidth/2f/TwilightHeroes.PPM,viewportHeight/2f/TwilightHeroes.PPM,0);
        viewport.getCamera().update();
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

    }
}
