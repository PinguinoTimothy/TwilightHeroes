package com.twilightheroes.game.tools;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
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
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.twilightheroes.game.TwilightHeroes;
import com.twilightheroes.game.ecs.components.AnimationComponent;
import com.twilightheroes.game.ecs.components.B2dBodyComponent;
import com.twilightheroes.game.ecs.components.CollisionComponent;
import com.twilightheroes.game.ecs.components.PlayerComponent;
import com.twilightheroes.game.ecs.components.StateComponent;
import com.twilightheroes.game.ecs.components.TextureComponent;
import com.twilightheroes.game.ecs.components.TypeComponent;

public class B2WorldCreator {
    public B2WorldCreator(World world, TiledMap map, Engine engine){
        BodyDef bodyDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        Body body;

        //Crear el suelo
        for(MapObject object : map.getLayers().get(1).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rectangle.getX()+rectangle.getWidth()/2)/ TwilightHeroes.PPM,(rectangle.getY()+rectangle.getHeight()/2)/TwilightHeroes.PPM);

            body = world.createBody(bodyDef);
            shape.setAsBox(rectangle.getWidth()/2/TwilightHeroes.PPM,rectangle.getHeight()/2/TwilightHeroes.PPM);
            fixtureDef.shape = shape;
            body.createFixture(fixtureDef);
        }

        //Crear las habitaciones
        for(MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){

            // Create the Entity and all the components that will go in the entity
            Entity entity = engine.createEntity();
            B2dBodyComponent b2dbody = engine.createComponent(B2dBodyComponent.class);
            //  TransformComponent position = engine.createComponent(TransformComponent.class);


            CollisionComponent colComp = engine.createComponent(CollisionComponent.class);
            TypeComponent type = engine.createComponent(TypeComponent.class);


            type.type = TypeComponent.SCENERY;
            colComp.collisionEntity = entity;

            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rectangle.getX()+rectangle.getWidth()/2)/ TwilightHeroes.PPM,(rectangle.getY()+rectangle.getHeight()/2)/TwilightHeroes.PPM);

            b2dbody.body = world.createBody(bodyDef);
            shape.setAsBox(rectangle.getWidth()/2/TwilightHeroes.PPM,rectangle.getHeight()/2/TwilightHeroes.PPM);
            fixtureDef.shape = shape;
            fixtureDef.isSensor = true;
            b2dbody.body.createFixture(fixtureDef);
            b2dbody.body.setUserData(entity);
            b2dbody.width = rectangle.getWidth();
            b2dbody.height = rectangle.getHeight();
            b2dbody.startX = rectangle.getX();
            b2dbody.startY = rectangle.getY();
            shape.dispose();


            // add the components to the entity
            entity.add(b2dbody);
            entity.add(colComp);
            entity.add(type);

            engine.addEntity(entity);


        }




    }
}
