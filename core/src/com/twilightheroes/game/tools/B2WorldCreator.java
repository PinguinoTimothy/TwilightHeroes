package com.twilightheroes.game.tools;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.twilightheroes.game.TwilightHeroes;
import com.twilightheroes.game.ecs.components.AnimationComponent;
import com.twilightheroes.game.ecs.components.AttackComponent;
import com.twilightheroes.game.ecs.components.B2dBodyComponent;
import com.twilightheroes.game.ecs.components.BulletComponent;
import com.twilightheroes.game.ecs.components.CollisionComponent;
import com.twilightheroes.game.ecs.components.DialogueComponent;
import com.twilightheroes.game.ecs.components.DoorComponent;
import com.twilightheroes.game.ecs.components.EnemyComponent;
import com.twilightheroes.game.ecs.components.ExitComponent;
import com.twilightheroes.game.ecs.components.HazardComponent;
import com.twilightheroes.game.ecs.components.InteractiveObjectComponent;
import com.twilightheroes.game.ecs.components.PlayerComponent;
import com.twilightheroes.game.ecs.components.StateComponent;
import com.twilightheroes.game.ecs.components.StatsComponent;
import com.twilightheroes.game.ecs.components.TextureComponent;
import com.twilightheroes.game.ecs.components.TypeComponent;
import com.twilightheroes.game.ecs.components.tipoObjetoInteractivo;
import com.twilightheroes.game.ecs.components.effectComponents.StatusComponent;
import com.twilightheroes.game.ecs.components.spells.Spell;
import com.twilightheroes.game.ecs.components.spells.SpellComponent;
import com.twilightheroes.game.ecs.components.spells.SpellList;
import com.twilightheroes.game.ecs.components.spells.SpellVFX;
import com.twilightheroes.game.screens.MainScreen;

import java.util.HashMap;

public class B2WorldCreator {

    private final PooledEngine engine;
    private final World world;

    private final MainScreen screen;
    private final HashMap<String, EnemyPrototype> enemigos = new HashMap<>();
    private final AssetManager manager;
    private final JsonValue jsonSpells = new JsonReader().parse(Gdx.files.internal("config/spells.json"));
    private final JsonValue jsonConfig = new JsonReader().parse(Gdx.files.internal("config/configuraciones.json"));
    public TextureRegion aux;
    private TiledMap map;

    public B2WorldCreator(World world, PooledEngine engine, MainScreen screen, AssetManager manager) {

        this.engine = engine;
        this.world = world;

        this.screen = screen;
        this.manager = manager;

        JsonValue jsonEnemigos = jsonConfig.get("enemigos");
        for (int i = 0; i < jsonEnemigos.size; i++) {
            JsonValue enemigoActual = jsonEnemigos.get(i);
            TextureAtlas atlas = manager.get("enemies/" + enemigoActual.get("atlas").asString());
            String name = enemigoActual.get("name").asString();
            int width = enemigoActual.get("width").asInt();
            int height = enemigoActual.get("height").asInt();
            int hitboxX = enemigoActual.get("hitboxX").asInt();
            int hitboxY = enemigoActual.get("hitboxY").asInt();
            int hp = enemigoActual.get("hp").asInt();
            int idleFrames = enemigoActual.get("animaciones").get("IDLE").asInt();
            int walkFrames = enemigoActual.get("animaciones").get("WALK").asInt();
            int attackFrames = enemigoActual.get("animaciones").get("ATTACK").asInt();

            float viewDistance = enemigoActual.get("viewDistance").asFloat();
            float attackDistance = enemigoActual.get("attackDistance").asFloat();
            float attackCooldown = enemigoActual.get("attackCooldown").asFloat();
            float speed = enemigoActual.get("speed").asFloat();
            int attackFrame = enemigoActual.get("attackFrame").asInt();
            int attackDamage = enemigoActual.get("attackDamage").asInt();
            String attackMethod = enemigoActual.get("attackMethod").asString();
            Spell[] spells = null;
            if (attackMethod.equals("spellCaster")) {
                String[] auxSpellString = enemigoActual.get("spells").asStringArray();
                Spell[] auxSpells = new Spell[auxSpellString.length];
                for (int j = 0; j < auxSpellString.length; j++) {
                    JsonValue jsonSpell = jsonSpells.get(auxSpellString[j]);
                    auxSpells[j] = new Spell(SpellList.spells.valueOf(jsonSpell.get("spellId").asString()).ordinal(), jsonSpell.get("manaCost").asInt(), jsonSpell.get("castingTime").asFloat() , new SpellVFX(4, 4),jsonSpell.get("duration").asInt(),jsonSpell.get("value").asInt());
                }
                spells = auxSpells;
            }

            enemigos.put(enemigoActual.get("name").asString(), new EnemyPrototype(atlas, name, width, height, hitboxX, hitboxY, hp, idleFrames, walkFrames, attackFrames, viewDistance, attackDistance, attackCooldown, speed, attackFrame, attackDamage, attackMethod, spells));
        }


    }

    public RoomSize generateLevel(TiledMap map, Entity playerEntity, int oldMap) {
        if (this.map != null) {
            this.map.dispose();

        }
        this.map = map;

        //Crear el suelo
        crearTerreno();
        crearSalidas();
        crearInteracciones();
        crearPuertas();
        crearPeligros();


        createPlayer(manager.get("player/player.atlas", TextureAtlas.class), playerEntity, oldMap);

        createEnemy();
        Rectangle rectangleRoom = ((RectangleMapObject) map.getLayers().get("room").getObjects().get(0)).getRectangle();
        return new RoomSize(rectangleRoom.width, rectangleRoom.height, rectangleRoom.x, rectangleRoom.y);


    }

    public void crearTerreno() {
        BodyDef bodyDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();

        //Crear el terreno
        for (RectangleMapObject object : map.getLayers().get("ground").getObjects().getByType(RectangleMapObject.class)) {

            // Create the Entity and all the components that will go in the entity
            Entity entity = engine.createEntity();
            B2dBodyComponent b2dbody = engine.createComponent(B2dBodyComponent.class);
            TypeComponent type = engine.createComponent(TypeComponent.class);


            type.type = TypeComponent.FLOOR;

            Rectangle rectangle = object.getRectangle();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rectangle.getX() + rectangle.getWidth() / 2) / TwilightHeroes.PPM, (rectangle.getY() + rectangle.getHeight() / 2) / TwilightHeroes.PPM);

            b2dbody.body = world.createBody(bodyDef);
            shape.setAsBox(rectangle.getWidth() / 2 / TwilightHeroes.PPM, rectangle.getHeight() / 2 / TwilightHeroes.PPM);
            fixtureDef.shape = shape;
            fixtureDef.filter.categoryBits = TwilightHeroes.SOLID_BIT;
            b2dbody.body.createFixture(fixtureDef);
            b2dbody.body.setUserData(entity);


            // add the components to the entity
            entity.add(b2dbody);
            entity.add(type);

            engine.addEntity(entity);
            screen.bodies.add(b2dbody.body);


        }
        shape.dispose();
    }

    public void crearPeligros() {
        BodyDef bodyDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();

        //Crear el terreno
        for (RectangleMapObject object : map.getLayers().get("environmentHazard").getObjects().getByType(RectangleMapObject.class)) {

            // Create the Entity and all the components that will go in the entity
            Entity entity = engine.createEntity();
            B2dBodyComponent b2dbody = engine.createComponent(B2dBodyComponent.class);
            TypeComponent type = engine.createComponent(TypeComponent.class);
            HazardComponent hazardComponent = engine.createComponent(HazardComponent.class);

            type.type = TypeComponent.HAZARD;

            hazardComponent.alive = true;
            hazardComponent.damage = (int) object.getProperties().get("damage");

            Rectangle rectangle = object.getRectangle();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rectangle.getX() + rectangle.getWidth() / 2) / TwilightHeroes.PPM, (rectangle.getY() + rectangle.getHeight() / 2) / TwilightHeroes.PPM);

            b2dbody.body = world.createBody(bodyDef);
            shape.setAsBox(rectangle.getWidth() / 2 / TwilightHeroes.PPM, rectangle.getHeight() / 2 / TwilightHeroes.PPM);
            fixtureDef.shape = shape;
            fixtureDef.filter.categoryBits = TwilightHeroes.SOLID_BIT;
            b2dbody.body.createFixture(fixtureDef);
            b2dbody.body.setUserData(entity);


            // add the components to the entity
            entity.add(b2dbody);
            entity.add(type);
            entity.add(hazardComponent);

            engine.addEntity(entity);
            screen.bodies.add(b2dbody.body);


        }
        shape.dispose();
    }

    public void crearSalidas() {
        BodyDef bodyDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();

        //Crear las habitaciones
        for (RectangleMapObject object : map.getLayers().get("exit").getObjects().getByType(RectangleMapObject.class)) {

            // Create the Entity and all the components that will go in the entity
            Entity entity = engine.createEntity();
            B2dBodyComponent b2dbody = engine.createComponent(B2dBodyComponent.class);
            ExitComponent exitComponent = engine.createComponent(ExitComponent.class);
            //  TransformComponent position = engine.createComponent(TransformComponent.class);


            CollisionComponent colComp = engine.createComponent(CollisionComponent.class);
            TypeComponent type = engine.createComponent(TypeComponent.class);


            type.type = TypeComponent.EXIT;

            Rectangle rectangle = object.getRectangle();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rectangle.getX() + rectangle.getWidth() / 2) / TwilightHeroes.PPM, (rectangle.getY() + rectangle.getHeight() / 2) / TwilightHeroes.PPM);

            b2dbody.body = world.createBody(bodyDef);
            shape.setAsBox(rectangle.getWidth() / 2 / TwilightHeroes.PPM, rectangle.getHeight() / 2 / TwilightHeroes.PPM);
            fixtureDef.shape = shape;
            fixtureDef.isSensor = true;
            fixtureDef.filter.categoryBits = TwilightHeroes.EXIT_BIT;
            fixtureDef.filter.maskBits = TwilightHeroes.PLAYER_BIT | TwilightHeroes.INMUNE_BIT;
            b2dbody.body.createFixture(fixtureDef);
            b2dbody.body.setUserData(entity);
            b2dbody.width = rectangle.getWidth();
            b2dbody.height = rectangle.getHeight();
            b2dbody.startX = rectangle.getX();
            b2dbody.startY = rectangle.getY();

            exitComponent.exitToRoom = (int) object.getProperties().get("toMap");
            exitComponent.fromRoom = (int) object.getProperties().get("fromMap");

            // add the components to the entity
            entity.add(b2dbody);
            entity.add(colComp);
            entity.add(type);
            entity.add(exitComponent);

            engine.addEntity(entity);
            screen.bodies.add(b2dbody.body);


        }
        shape.dispose();
    }

    public void crearInteracciones() {
        BodyDef bodyDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();

        //Crear las habitaciones
        for (RectangleMapObject object : map.getLayers().get("interactiveObject").getObjects().getByType(RectangleMapObject.class)) {

            // Create the Entity and all the components that will go in the entity
            Entity entity = engine.createEntity();
            B2dBodyComponent b2dbody = engine.createComponent(B2dBodyComponent.class);
            InteractiveObjectComponent interactiveObjectComponent = engine.createComponent(InteractiveObjectComponent.class);


            CollisionComponent colComp = engine.createComponent(CollisionComponent.class);
            TypeComponent type = engine.createComponent(TypeComponent.class);


            type.type = TypeComponent.INTERACTABLE;

            Rectangle rectangle = object.getRectangle();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rectangle.getX() + rectangle.getWidth() / 2) / TwilightHeroes.PPM, (rectangle.getY() + rectangle.getHeight() / 2) / TwilightHeroes.PPM);

            b2dbody.body = world.createBody(bodyDef);
            shape.setAsBox(rectangle.getWidth() / 2 / TwilightHeroes.PPM, rectangle.getHeight() / 2 / TwilightHeroes.PPM);
            fixtureDef.shape = shape;
            fixtureDef.isSensor = true;
            fixtureDef.filter.categoryBits = TwilightHeroes.INTERACTIVE_BIT;
            fixtureDef.filter.maskBits = TwilightHeroes.HITBOX_BIT;
            b2dbody.body.createFixture(fixtureDef);
            b2dbody.body.setUserData(entity);
            b2dbody.width = rectangle.getWidth();
            b2dbody.height = rectangle.getHeight();
            b2dbody.startX = rectangle.getX();
            b2dbody.startY = rectangle.getY();


            if (object.getProperties().get("type") != null){
                switch (object.getProperties().get("type").toString()){
                    case "lever":
                        interactiveObjectComponent.tipoObjeto = tipoObjetoInteractivo.LEVER;
                        break;
                    case "dialogue":
                        interactiveObjectComponent.tipoObjeto = tipoObjetoInteractivo.DIALOGUE;
                        break;
                    case "obelisk":
                        interactiveObjectComponent.tipoObjeto = tipoObjetoInteractivo.OBELISK;
                        break;

                }
                interactiveObjectComponent.id = (int) object.getProperties().get("id");
            }

            if (interactiveObjectComponent.tipoObjeto == tipoObjetoInteractivo.OBELISK){
                AnimationComponent animationComponent = new AnimationComponent();
                StateComponent stateComponent = new StateComponent();
                TextureComponent textureComponent = new TextureComponent();


                stateComponent.set(StateComponent.STATE_OBELISK_IDLE);
            textureComponent.sprite.setRegion(manager.get("variety/obelisk.atlas", TextureAtlas.class).findRegion("IDLE"));
                textureComponent.sprite.setPosition((rectangle.getX() + rectangle.getWidth() / 2) / TwilightHeroes.PPM, (rectangle.getY())-1000 / TwilightHeroes.PPM);
            textureComponent.sprite.setSize(0.5f,0.5f);


                animationComponent.animations.put(StateComponent.STATE_OBELISK_IDLE, AnimationMaker.crearAnimacion(manager.get("variety/obelisk.atlas", TextureAtlas.class), "IDLE", 14, 7));
                animationComponent.animations.put(StateComponent.STATE_OBELISK_ANI, AnimationMaker.crearAnimacion(manager.get("variety/obelisk.atlas", TextureAtlas.class), "ANI", 14, 7));
                entity.add(animationComponent);
                entity.add(stateComponent);
                entity.add(textureComponent);
            }

            // add the components to the entity
            entity.add(b2dbody);
            entity.add(colComp);
            entity.add(type);
            entity.add(interactiveObjectComponent);

            engine.addEntity(entity);
            screen.bodies.add(b2dbody.body);


        }
        shape.dispose();
    }

    public void crearPuertas(){
        BodyDef bodyDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();

        //Crear las habitaciones
        for (RectangleMapObject object : map.getLayers().get("door").getObjects().getByType(RectangleMapObject.class)) {

            // Create the Entity and all the components that will go in the entity
            Entity entity = engine.createEntity();
            B2dBodyComponent b2dbody = engine.createComponent(B2dBodyComponent.class);
            TypeComponent type = engine.createComponent(TypeComponent.class);
            DoorComponent doorComponent = engine.createComponent(DoorComponent.class);

            type.type = TypeComponent.DOOR;

            Rectangle rectangle = object.getRectangle();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rectangle.getX() + rectangle.getWidth() / 2) / TwilightHeroes.PPM, (rectangle.getY() + rectangle.getHeight() / 2) / TwilightHeroes.PPM);

            b2dbody.body = world.createBody(bodyDef);
            shape.setAsBox(rectangle.getWidth() / 2 / TwilightHeroes.PPM, rectangle.getHeight() / 2 / TwilightHeroes.PPM);
            fixtureDef.shape = shape;
            fixtureDef.isSensor = false;
            fixtureDef.filter.categoryBits = TwilightHeroes.SOLID_BIT;
            b2dbody.body.createFixture(fixtureDef);
            b2dbody.body.setUserData(entity);
            b2dbody.width = rectangle.getWidth();
            b2dbody.height = rectangle.getHeight();
            b2dbody.startX = rectangle.getX();
            b2dbody.startY = rectangle.getY();

            doorComponent.id = (int) object.getProperties().get("id");



            // add the components to the entity
            entity.add(b2dbody);
            entity.add(doorComponent);
            entity.add(type);

            engine.addEntity(entity);
            screen.bodies.add(b2dbody.body);
            openDoors();

        }
        shape.dispose();
    }

    public void openDoors() {
        ImmutableArray<Entity> doors = engine.getEntitiesFor(Family.all(DoorComponent.class).get());
        for (Entity door : doors) {
            DoorComponent doorComponent = Mappers.doorCom.get(door);

            if (screen.parent.playerSettings.doorsOpened[doorComponent.id]) {
                // La puerta está abierta, elimina la entidad y cambia las tiles
                changeTilesForOpenedDoor(door,true);
                destroyDoor(door);
            }
        }
        ImmutableArray<Entity> levers = engine.getEntitiesFor(Family.all(InteractiveObjectComponent.class).get());
        for (Entity lvr : levers) {
            InteractiveObjectComponent interactiveObjectComponent = Mappers.interactiveCom.get(lvr);

            if (interactiveObjectComponent.tipoObjeto == tipoObjetoInteractivo.LEVER && screen.parent.playerSettings.doorsOpened[interactiveObjectComponent.id]) {
                // La puerta está abierta, elimina la entidad y cambia las tiles
                changeTilesForOpenedDoor(lvr,false);
            }
        }
    }



    private void destroyDoor(Entity door) {
        // Obtén el componente B2dBodyComponent asociado a la entidad
        B2dBodyComponent b2dbody = Mappers.b2dCom.get(door);

        // Elimina el cuerpo del mundo físico y la entidad del motor
        screen.bodies.removeValue(b2dbody.body,true);
        world.destroyBody(b2dbody.body);
        engine.removeEntity(door);
    }

    private void changeTilesForOpenedDoor(Entity entity,Boolean door) {
        B2dBodyComponent b2dbody = Mappers.b2dCom.get(entity);
        TiledMapTileLayer layerToChange;
        TiledMapTile[] newDoorTile = new TiledMapTile[0];
        if (door) {

            layerToChange = (TiledMapTileLayer) map.getLayers().get("doorTexture");
            TiledMapTileSet newTileSets = map.getTileSets().getTileSet("Dungeon Tile Set");
            newDoorTile = new TiledMapTile[]{newTileSets.getTile(10527 + 162), newTileSets.getTile(10527 + 177)};
        }else {
            layerToChange = (TiledMapTileLayer) map.getLayers().get("leverTexture");
        }

        // Calcula las coordenadas de las tiles cubiertas por la puerta abierta
        int tileStartX = (int) (b2dbody.startX / layerToChange.getTileWidth());
        int tileStartY = (int) (b2dbody.startY / layerToChange.getTileHeight());
        int tileEndX = (int) ((b2dbody.startX + b2dbody.width) / layerToChange.getTileWidth());
        int tileEndY = (int) ((b2dbody.startY + b2dbody.height) / layerToChange.getTileHeight());

            int i = 0;

            // Itera sobre todas las tiles dentro del área cubierta por la puerta
            for (int tileX = tileStartX; tileX < tileEndX; tileX++) {
                for (int tileY = tileStartY; tileY < tileEndY; tileY++) {

                    if (door){
                        layerToChange.getCell(tileX, tileY).setTile(newDoorTile[i]);
                        i++;

                    }else{
                        TiledMapTileLayer.Cell cell =  layerToChange.getCell(tileX, tileY);
                        if (cell != null){
                       cell.setFlipHorizontally(true);

                        }

                    }
                }
            }
        }


    public void createPlayer(TextureAtlas atlas, Entity playerEntity, int oldMap) {
        JsonValue jsonPlayer = jsonConfig.get("personaje");

        if (playerEntity == null) {
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
            StatsComponent statsComponent = engine.createComponent(StatsComponent.class);
            StatusComponent statusComponent = engine.createComponent(StatusComponent.class);
            SpellComponent spellComponent = engine.createComponent(SpellComponent.class);
            DialogueComponent dialogueComponent = engine.createComponent(DialogueComponent.class);


            // create the data for the components and add them to the components
            aux = atlas.findRegion("Idle");
            texture.sprite.setRegion(aux);
            Rectangle spawn = null;
            for (RectangleMapObject object : map.getLayers().get("spawn").getObjects().getByType(RectangleMapObject.class)) {
                if ( object.getProperties().get("id",int.class) == oldMap){
                    spawn = object.getRectangle();
                }
            }


                texture.sprite.setBounds(spawn.x / TwilightHeroes.PPM, spawn.y / TwilightHeroes.PPM, jsonPlayer.get("width").asFloat() / TwilightHeroes.PPM, jsonPlayer.get("height").asFloat() / TwilightHeroes.PPM);

            type.type = TypeComponent.PLAYER;
            stateCom.set(StateComponent.STATE_IDLE);
            stateCom.isLooping = true;

            BodyDef bodyDef = new BodyDef();
            bodyDef.position.set(texture.sprite.getX(), texture.sprite.getY());
            bodyDef.type = BodyDef.BodyType.DynamicBody;
            b2dbody.body = world.createBody(bodyDef);
            b2dbody.body.setFixedRotation(true);

            // Define la hitbox rectangular
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(jsonPlayer.get("hitboxX").asFloat() / TwilightHeroes.PPM, jsonPlayer.get("hitboxY").asFloat() / TwilightHeroes.PPM); // Tamaño de la hitbox

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.friction = 0;
            fixtureDef.shape = shape;
            fixtureDef.filter.categoryBits = TwilightHeroes.PLAYER_BIT;
            b2dbody.body.createFixture(fixtureDef);

            FixtureDef fdef2 = new FixtureDef();
            EdgeShape feet = new EdgeShape();

            feet.set(new Vector2(-2 / TwilightHeroes.PPM, -12 / TwilightHeroes.PPM), new Vector2(2 / TwilightHeroes.PPM, -12 / TwilightHeroes.PPM));
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

            Fixture fix = b2dbody.body.createFixture(attackFixtureDef);
            fix.setUserData("playerAttackSensor");
            attackComponent.attackFixtures.add(fix);
            attackComponent.lifetimes.add(0f);  // Inicializa el tiempo de vida para esta fixture a 0
            // Liberar los recursos del shape
            attackShape.dispose();


            b2dbody.body.setUserData(entity);


            screen.bodies.add(b2dbody.body);


            // Create the animation and add it to AnimationComponent


            animCom.animations.put(StateComponent.STATE_IDLE, AnimationMaker.crearAnimacion(atlas, "Idle", 7, 7));
            animCom.animations.put(StateComponent.STATE_MOVING, AnimationMaker.crearAnimacion(atlas, "Run", 8, 8));
            animCom.animations.put(StateComponent.STATE_ATTACK01, AnimationMaker.crearAnimacion(atlas, "Attack_1", 4, 12));
            animCom.animations.put(StateComponent.STATE_ATTACK02, AnimationMaker.crearAnimacion(atlas, "Attack_2", 4, 12));
            animCom.animations.put(StateComponent.STATE_ATTACK03, AnimationMaker.crearAnimacion(atlas, "Attack_3", 4, 12));
            animCom.animations.put(StateComponent.STATE_DAMAGED, AnimationMaker.crearAnimacion(atlas, "Hurt", 4, 4));
            animCom.animations.put(StateComponent.STATE_DODGING, AnimationMaker.crearAnimacion(atlas, "Rest", 7, 14));
            animCom.animations.put(StateComponent.STATE_CASTING, AnimationMaker.crearAnimacion(atlas, "Casting", 8, 8));


            statsComponent.speed = jsonPlayer.get("speed").asFloat();
            statsComponent.hp = jsonPlayer.get("hp").asFloat();
            statsComponent.damage = jsonPlayer.get("attackDamage").asFloat();

            //Selected Spells


            JsonValue auxSpell = jsonSpells.get(screen.parent.playerSettings.spell1);
            spellComponent.spell1 = new Spell(SpellList.spells.valueOf(auxSpell.get("spellId").asString()).ordinal(), auxSpell.get("manaCost").asInt(), auxSpell.get("castingTime").asFloat(), new SpellVFX(16, 16),auxSpell.get("duration").asInt(),auxSpell.get("value").asInt());

            auxSpell = jsonSpells.get(screen.parent.playerSettings.spell2);
            spellComponent.spell2 = new Spell(SpellList.spells.valueOf(auxSpell.get("spellId").asString()).ordinal(), auxSpell.get("manaCost").asInt(), auxSpell.get("castingTime").asFloat(), new SpellVFX(16, 16),auxSpell.get("duration").asInt(),auxSpell.get("value").asInt());


            entity.add(b2dbody);
            entity.add(texture);
            entity.add(player);  // Add PlayerComponent to the entity
            entity.add(colComp);
            entity.add(type);
            entity.add(stateCom);
            entity.add(animCom);
            entity.add(attackComponent);
            entity.add(statsComponent);
            entity.add(statusComponent);
            entity.add(spellComponent);
            entity.add(dialogueComponent);

// add the entity to the engine

            engine.addEntity(entity);
            screen.playerEntity = entity;
        } else {
            playerEntity.remove(B2dBodyComponent.class);
            playerEntity.remove(TextureComponent.class);

            TextureComponent texture = engine.createComponent(TextureComponent.class);
            aux = atlas.findRegion("Idle");
            texture.sprite.setRegion(aux);

            Rectangle spawn = null;
            for (RectangleMapObject object : map.getLayers().get("spawn").getObjects().getByType(RectangleMapObject.class)) {
                if ( (int)object.getProperties().get("id") == oldMap){
                    spawn = object.getRectangle();
                }
            }


            texture.sprite.setBounds(spawn.x / TwilightHeroes.PPM, spawn.y / TwilightHeroes.PPM, jsonPlayer.get("width").asFloat() / TwilightHeroes.PPM, jsonPlayer.get("height").asFloat() / TwilightHeroes.PPM);


            B2dBodyComponent b2dbody = engine.createComponent(B2dBodyComponent.class);
            BodyDef bodyDef = new BodyDef();
            bodyDef.position.set(texture.sprite.getX(), texture.sprite.getY());
            bodyDef.type = BodyDef.BodyType.DynamicBody;
            b2dbody.body = world.createBody(bodyDef);
            b2dbody.body.setFixedRotation(true);

            // Define la hitbox rectangular
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(jsonPlayer.get("hitboxX").asFloat() / TwilightHeroes.PPM, jsonPlayer.get("hitboxY").asFloat() / TwilightHeroes.PPM); // Tamaño de la hitbox

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
            b2dbody.body.setUserData(playerEntity);


            screen.bodies.add(b2dbody.body);
            playerEntity.add(b2dbody);
            playerEntity.add(texture);
        }
    }


    public void createEnemy() {

        if (map.getLayers().get("enemies") != null) {
            for (RectangleMapObject object : map.getLayers().get("enemies").getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rectangle = object.getRectangle();
                String enemyName = (String) object.getProperties().get("enemigo");
                EnemyPrototype enemyPrototype = enemigos.get(enemyName);
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
                StatsComponent statsComponent = engine.createComponent(StatsComponent.class);
                StatusComponent statusComponent = engine.createComponent(StatusComponent.class);
                SpellComponent spellComponent = engine.createComponent(SpellComponent.class);

                // create the data for the components and add them to the components
                texture.sprite.setRegion(enemyPrototype.atlas.findRegion("IDLE"));

                texture.sprite.setBounds(rectangle.getX() / TwilightHeroes.PPM, rectangle.getY() / TwilightHeroes.PPM, enemyPrototype.width / TwilightHeroes.PPM, enemyPrototype.height / TwilightHeroes.PPM);

                type.type = TypeComponent.ENEMY;
                stateCom.set(StateComponent.STATE_IDLE);
                stateCom.isLooping = true;

                BodyDef bodyDef = new BodyDef();
                bodyDef.position.set(texture.sprite.getX(), texture.sprite.getY());
                bodyDef.type = BodyDef.BodyType.DynamicBody;
                b2dbody.body = world.createBody(bodyDef);
                b2dbody.body.setFixedRotation(true);
                // Define la hitbox rectangular
                PolygonShape shape = new PolygonShape();
                shape.setAsBox(enemyPrototype.hitboxX / TwilightHeroes.PPM, enemyPrototype.hitboxY / TwilightHeroes.PPM); // Tamaño de la hitbox

                FixtureDef fixtureDef = new FixtureDef();
                fixtureDef.friction = 0;
                fixtureDef.shape = shape;
                fixtureDef.filter.categoryBits = TwilightHeroes.ENEMY_BIT;
                fixtureDef.filter.maskBits = TwilightHeroes.SOLID_BIT | TwilightHeroes.HITBOX_BIT | TwilightHeroes.PLAYER_BIT | TwilightHeroes.BULLET_BIT;
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


                animCom.animations.put(StateComponent.STATE_IDLE, AnimationMaker.crearAnimacion(enemyPrototype.atlas, "IDLE", enemyPrototype.idleFrames, enemyPrototype.idleFrames));
                animCom.animations.put(StateComponent.STATE_ENEMY_ATTACK, AnimationMaker.crearAnimacion(enemyPrototype.atlas, "ATTACK", enemyPrototype.attackFrames, enemyPrototype.attackFrames));
                animCom.animations.put(StateComponent.STATE_CHASING, AnimationMaker.crearAnimacion(enemyPrototype.atlas, "WALK", enemyPrototype.walkFrames, enemyPrototype.walkFrames));

                statsComponent.damage = enemyPrototype.attackDamage;

                enemyComponent.name = enemyPrototype.name;
                enemyComponent.attackCooldown = enemyPrototype.attackCooldown;
                enemyComponent.attackDistance = enemyPrototype.attackDistance;
                enemyComponent.attackFrame = enemyPrototype.attackFrame;
                enemyComponent.viewDistance = enemyPrototype.viewDistance;
                enemyComponent.attackMethod = enemyPrototype.attackMethod;
                enemyComponent.spells = enemyPrototype.spells;

                statsComponent.speed = enemyPrototype.speed;
                statsComponent.hp = enemyPrototype.hp;

                entity.add(b2dbody);
                entity.add(texture);
                entity.add(enemyComponent);  // Add PlayerComponent to the entity
                entity.add(colComp);
                entity.add(type);
                entity.add(stateCom);
                entity.add(animCom);
                entity.add(attackComponent);
                entity.add(statsComponent);
                entity.add(statusComponent);
                entity.add(spellComponent);

// add the entity to the engine
                engine.addEntity(entity);
            }
        }
    }


    public void createBullet(float x, float y, float xVel, BulletComponent.Owner own, boolean runningRight, float damage, String spellName) {
        Entity entity = engine.createEntity();
        B2dBodyComponent b2dbody = engine.createComponent(B2dBodyComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        TypeComponent type = engine.createComponent(TypeComponent.class);
        CollisionComponent colComp = engine.createComponent(CollisionComponent.class);
        BulletComponent bul = engine.createComponent(BulletComponent.class);
        AnimationComponent animationComponent = engine.createComponent(AnimationComponent.class);
        StateComponent stateComponent = engine.createComponent(StateComponent.class);
        TextureAtlas spellAtlas = manager.get("spells/spells.atlas");


        bul.owner = own;
        bul.damage = damage;

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(x, y);
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        b2dbody.body = world.createBody(bodyDef);
        b2dbody.body.setFixedRotation(true);

        // Define la hitbox rectangular
        CircleShape shape = new CircleShape();
        shape.setRadius(4f / TwilightHeroes.PPM); // Tamaño de la hitbox

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.friction = 0;
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = TwilightHeroes.BULLET_BIT;
        fixtureDef.isSensor = true;
        b2dbody.body.createFixture(fixtureDef);
        shape.dispose();


        JsonValue json = new JsonReader().parse(Gdx.files.internal("config/spells.json"));
        JsonValue jsonSpell = json.get(spellName);


        animationComponent.animations.put(StateComponent.STATE_SPELL_STARTING, AnimationMaker.crearAnimacion(spellAtlas, spellName + "Start", jsonSpell.get("startFrames").asInt(), jsonSpell.get("startFrames").asInt()));
        animationComponent.animations.put(StateComponent.STATE_SPELL_GOING, AnimationMaker.crearAnimacion(spellAtlas, spellName + "Going", jsonSpell.get("goingFrames").asInt(), jsonSpell.get("goingFrames").asInt()));
        animationComponent.animations.put(StateComponent.STATE_SPELL_ENDING, AnimationMaker.crearAnimacion(spellAtlas, spellName + "Ending", jsonSpell.get("endingFrames").asInt(), jsonSpell.get("endingFrames").asInt()));
        stateComponent.set(StateComponent.STATE_SPELL_STARTING);

        texture.sprite.setRegion(animationComponent.animations.get(stateComponent.get()).getKeyFrame(stateComponent.time, stateComponent.isLooping));
        texture.sprite.setBounds(x, y, 10 / TwilightHeroes.PPM, 10 / TwilightHeroes.PPM);
        texture.runningRight = runningRight;

        type.type = TypeComponent.BULLET;
        b2dbody.body.setUserData(entity);
        bul.xVel = xVel;


        entity.add(bul);
        entity.add(colComp);
        entity.add(b2dbody);
        entity.add(texture);
        entity.add(type);
        entity.add(animationComponent);
        entity.add(stateComponent);

        engine.addEntity(entity);
    }



}
