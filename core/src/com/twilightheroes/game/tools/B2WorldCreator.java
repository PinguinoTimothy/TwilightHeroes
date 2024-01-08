package com.twilightheroes.game.tools;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.twilightheroes.game.TwilightHeroes;
import com.twilightheroes.game.ecs.components.AnimationComponent;
import com.twilightheroes.game.ecs.components.AttackComponent;
import com.twilightheroes.game.ecs.components.B2dBodyComponent;
import com.twilightheroes.game.ecs.components.CollisionComponent;
import com.twilightheroes.game.ecs.components.EnemyComponent;
import com.twilightheroes.game.ecs.components.ExitComponent;
import com.twilightheroes.game.ecs.components.PlayerComponent;
import com.twilightheroes.game.ecs.components.StateComponent;
import com.twilightheroes.game.ecs.components.TextureComponent;
import com.twilightheroes.game.ecs.components.TypeComponent;
import com.twilightheroes.game.screens.MainScreen;

public class B2WorldCreator {

    private PooledEngine engine;
    private World world;
    private TiledMap map;
    private BodyFactory bodyFactory;
private MainScreen screen;

    public B2WorldCreator(World world, TiledMap map, PooledEngine engine, TextureAtlas playerAtlas, MainScreen screen){
        BodyDef bodyDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        Body body;
        this.engine = engine;
        this.world = world;
        this.map = map;
        bodyFactory = BodyFactory.getInstance(world);
        this.screen = screen;
        //Crear el suelo
        for(MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rectangle.getX()+rectangle.getWidth()/2)/ TwilightHeroes.PPM,(rectangle.getY()+rectangle.getHeight()/2)/TwilightHeroes.PPM);

            body = world.createBody(bodyDef);
            shape.setAsBox(rectangle.getWidth()/2/TwilightHeroes.PPM,rectangle.getHeight()/2/TwilightHeroes.PPM);
            fixtureDef.shape = shape;
            fixtureDef.filter.categoryBits = TwilightHeroes.SOLID_BIT;
            body.createFixture(fixtureDef);
            screen.bodies.add(body);
        }

shape.dispose();

        createPlayer(playerAtlas);
        TextureAtlas atlasEnemigo = new TextureAtlas(Gdx.files.internal("enemy.atlas"));

        createEnemy(playerAtlas.findRegion("Idle-Sheet"),4f,1,4,8,atlasEnemigo);
     //   createEnemy(playerAtlas.findRegion("Idle-Sheet"),5f,1,4,8,atlasEnemigo);
       // createEnemy(playerAtlas.findRegion("Idle-Sheet"),6f,1,4,8,atlasEnemigo);
        //createEnemy(playerAtlas.findRegion("Idle-Sheet"),7f,1,4,8,atlasEnemigo);

crearSalidas();
    }

    public void crearSalidas(){
        BodyDef bodyDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        Body body;

        //Crear las habitaciones
        for(MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){

            // Create the Entity and all the components that will go in the entity
            Entity entity = engine.createEntity();
            B2dBodyComponent b2dbody = engine.createComponent(B2dBodyComponent.class);
            ExitComponent exitComponent = engine.createComponent(ExitComponent.class);
            //  TransformComponent position = engine.createComponent(TransformComponent.class);


            CollisionComponent colComp = engine.createComponent(CollisionComponent.class);
            TypeComponent type = engine.createComponent(TypeComponent.class);


            type.type = TypeComponent.EXIT;
            colComp.collisionEntity = entity;

            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rectangle.getX()+rectangle.getWidth()/2)/ TwilightHeroes.PPM,(rectangle.getY()+rectangle.getHeight()/2)/TwilightHeroes.PPM);

            b2dbody.body = world.createBody(bodyDef);
            shape.setAsBox(rectangle.getWidth()/2/TwilightHeroes.PPM,rectangle.getHeight()/2/TwilightHeroes.PPM);
            fixtureDef.shape = shape;
            fixtureDef.isSensor = true;
            fixtureDef.filter.categoryBits = TwilightHeroes.EXIT_BIT;
            fixtureDef.filter.maskBits = TwilightHeroes.PLAYER_BIT;
            b2dbody.body.createFixture(fixtureDef);
            b2dbody.body.setUserData(entity);
            b2dbody.width = rectangle.getWidth();
            b2dbody.height = rectangle.getHeight();
            b2dbody.startX = rectangle.getX();
            b2dbody.startY = rectangle.getY();

            exitComponent.exitToRoom = (int) object.getProperties().get("salida");

            // add the components to the entity
            entity.add(b2dbody);
            entity.add(colComp);
            entity.add(type);
            entity.add(exitComponent);

            engine.addEntity(entity);
            screen.bodies.add(b2dbody.body);



        }
    }

    public void createPlayer(TextureAtlas atlas){
        // Create the Entity and all the components that will go in the entity
        Entity entity = engine.createEntity();
        B2dBodyComponent b2dbody = engine.createComponent(B2dBodyComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        PlayerComponent player = engine.createComponent(PlayerComponent.class);
        CollisionComponent colComp = engine.createComponent(CollisionComponent.class);
        TypeComponent type = engine.createComponent(TypeComponent.class);
        StateComponent stateCom = engine.createComponent(StateComponent.class);
        AnimationComponent animCom = engine.createComponent(AnimationComponent.class);
        AttackComponent attackComponent = engine.createComponent(AttackComponent.class);

        // create the data for the components and add them to the components
        texture.sprite.setRegion(atlas.findRegion("Idle-Sheet"));

        texture.sprite.setBounds(2, 1,35/TwilightHeroes.PPM,47/TwilightHeroes.PPM);

        type.type = TypeComponent.PLAYER;
        stateCom.set(StateComponent.STATE_IDLE);
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
        fixtureDef.friction = 0;
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = TwilightHeroes.PLAYER_BIT;
        b2dbody.body.createFixture(fixtureDef);

        FixtureDef fdef2 = new FixtureDef();
        EdgeShape feet = new EdgeShape();
        feet.set(new Vector2(-4 / TwilightHeroes.PPM, -12 / TwilightHeroes.PPM), new Vector2(4 / TwilightHeroes.PPM, -12 / TwilightHeroes.PPM));
        fdef2.shape = feet;
        fdef2.friction = 1;
        b2dbody.body.createFixture(fdef2);

        // Libera los recursos del shape
        shape.dispose();


        //Esto no estoy seguro porque pero sin el no funciona el ataque. No tocar.
        Fixture attackFixture;
        PolygonShape attackShape = new PolygonShape();
        float offsetX = texture.runningRight ? 16 / TwilightHeroes.PPM : -16 / TwilightHeroes.PPM;
        attackShape.setAsBox(12 / TwilightHeroes.PPM, 8 / TwilightHeroes.PPM, new Vector2(offsetX, 0), 0);

        FixtureDef attackFixtureDef = new FixtureDef();
        attackFixtureDef.shape = attackShape;
        attackFixtureDef.isSensor = true; // Configurar la fixture como un sensor
        attackFixtureDef.filter.categoryBits = TwilightHeroes.HITBOX_BIT;

        attackFixtureDef.filter.maskBits = TwilightHeroes.ENEMY_BIT;
        attackFixture = b2dbody.body.createFixture(attackFixtureDef);
        attackFixture.setUserData("playerAttackSensor");
        attackComponent.attackFixture = attackFixture;
        // Liberar los recursos del shape
        attackShape.dispose();




        b2dbody.body.setUserData(entity);


screen.bodies.add(b2dbody.body);



        // Create the animation and add it to AnimationComponent


        animCom.animations.put(StateComponent.STATE_IDLE, AnimationMaker.crearAnimacion(atlas,"Idle-Sheet",4,4));
        animCom.animations.put(StateComponent.STATE_MOVING, AnimationMaker.crearAnimacion(atlas,"Run-Sheet",8,8));
        animCom.animations.put(StateComponent.STATE_ATTACK01, AnimationMaker.crearAnimacion(atlas,"ataque1",4,16));
        animCom.animations.put(StateComponent.STATE_ATTACK02, AnimationMaker.crearAnimacion(atlas,"ataque2",4,16));
        animCom.animations.put(StateComponent.STATE_ATTACK03, AnimationMaker.crearAnimacion(atlas,"ataque3",4,16));

        entity.add(b2dbody);
        entity.add(texture);
        entity.add(player);  // Add PlayerComponent to the entity
        entity.add(colComp);
        entity.add(type);
        entity.add(stateCom);
        entity.add(animCom);
        entity.add(attackComponent);


// add the entity to the engine

        engine.addEntity(entity);
screen.playerEntity = entity;
    }

    public void createEnemy(TextureRegion tex, float x, float y, float width, float height,TextureAtlas atlas){
        // Create the Entity and all the components that will go in the entity
        Entity entity = engine.createEntity();
        B2dBodyComponent b2dbody = engine.createComponent(B2dBodyComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        EnemyComponent enemyComponent = engine.createComponent(EnemyComponent.class);
        CollisionComponent colComp = engine.createComponent(CollisionComponent.class);
        TypeComponent type = engine.createComponent(TypeComponent.class);
        StateComponent stateCom = engine.createComponent(StateComponent.class);
        AnimationComponent animCom = engine.createComponent(AnimationComponent.class);
        AttackComponent attackComponent = engine.createComponent(AttackComponent.class);

        // create the data for the components and add them to the components
        texture.sprite.setRegion(atlas.findRegion("IDLE"));

        texture.sprite.setBounds(x, y,35/TwilightHeroes.PPM,47/TwilightHeroes.PPM);

        type.type = TypeComponent.ENEMY;
        stateCom.set(StateComponent.STATE_IDLE);
        stateCom.isLooping = true;
        colComp.collisionEntity = entity;

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(texture.sprite.getX(),texture.sprite.getY());
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        b2dbody.body = world.createBody(bodyDef);
        b2dbody.body.setFixedRotation(true);
        b2dbody.body.setLinearVelocity(1f,0f);
        // Define la hitbox rectangular
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(4 / TwilightHeroes.PPM, 8 / TwilightHeroes.PPM); // Tamaño de la hitbox

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.friction = 0;
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = TwilightHeroes.ENEMY_BIT;
        fixtureDef.filter.maskBits = TwilightHeroes.SOLID_BIT | TwilightHeroes.HITBOX_BIT | TwilightHeroes.PLAYER_BIT;
        b2dbody.body.createFixture(fixtureDef);

        FixtureDef fdef2 = new FixtureDef();
        EdgeShape feet = new EdgeShape();

        feet.set(new Vector2(-4 / TwilightHeroes.PPM, -12 / TwilightHeroes.PPM), new Vector2(4 / TwilightHeroes.PPM, -12 / TwilightHeroes.PPM));
        fdef2.shape = feet;
        fdef2.friction = 1;
        fdef2.filter.categoryBits = TwilightHeroes.ENEMY_BIT;
        fdef2.filter.maskBits = TwilightHeroes.SOLID_BIT | TwilightHeroes.PLAYER_BIT;
        b2dbody.body.createFixture(fdef2);



        // Libera los recursos del shape
        shape.dispose();
        b2dbody.body.setUserData(entity);




screen.bodies.add(b2dbody.body);

        // Create the animation and add it to AnimationComponent


        animCom.animations.put(StateComponent.STATE_IDLE, AnimationMaker.crearAnimacion(atlas,"IDLE",3,3));
        animCom.animations.put(StateComponent.STATE_ENEMY_ATTACK, AnimationMaker.crearAnimacion(atlas,"ATTACK",7,7));
        animCom.animations.put(StateComponent.STATE_CHASING, AnimationMaker.crearAnimacion(atlas,"WALK",8,8));



        entity.add(b2dbody);
        entity.add(texture);
        entity.add(enemyComponent);  // Add PlayerComponent to the entity
        entity.add(colComp);
        entity.add(type);
        entity.add(stateCom);
        entity.add(animCom);
        entity.add(attackComponent);

// add the entity to the engine
        engine.addEntity(entity);
    }
}
