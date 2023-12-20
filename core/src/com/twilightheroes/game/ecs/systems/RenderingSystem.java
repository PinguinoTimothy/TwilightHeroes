        package com.twilightheroes.game.ecs.systems;

        import com.badlogic.ashley.core.ComponentMapper;
        import com.badlogic.ashley.core.Entity;
        import com.badlogic.ashley.core.Family;
        import com.badlogic.ashley.systems.IteratingSystem;
        import com.badlogic.gdx.Gdx;
        import com.badlogic.gdx.graphics.OrthographicCamera;
        import com.badlogic.gdx.graphics.g2d.SpriteBatch;
        import com.badlogic.gdx.math.MathUtils;
        import com.badlogic.gdx.math.Vector2;
        import com.badlogic.gdx.math.Vector3;
        import com.badlogic.gdx.utils.Array;
        import com.badlogic.gdx.utils.viewport.Viewport;
        import com.twilightheroes.game.ecs.components.PlayerComponent;
        import com.twilightheroes.game.ecs.components.TextureComponent;

        public class RenderingSystem extends IteratingSystem {

            public static final float PPM = 100f;


            public static final float PIXELS_TO_METRES = 1.0f / PPM;

            private static Vector2 meterDimensions = new Vector2();
            private static Vector2 pixelDimensions = new Vector2();



            public static Vector2 getScreenSizeInMeters(){
                meterDimensions.set(Gdx.graphics.getWidth()*PIXELS_TO_METRES,
                        Gdx.graphics.getHeight()*PIXELS_TO_METRES);
                return meterDimensions;
            }

            public static Vector2 getScreenSizeInPixesl(){
                pixelDimensions.set(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                return pixelDimensions;
            }

            public static float PixelsToMeters(float pixelValue){
                return pixelValue * PIXELS_TO_METRES;
            }

            private SpriteBatch batch; // a reference to our spritebatch
            private Array<Entity> renderQueue; // an array used to allow sorting of images allowing us to draw images on top of each other
            private OrthographicCamera cam; // a reference to our camera

            // component mappers to get components from entities
            private ComponentMapper<TextureComponent> textureM;

            private Viewport viewport;

            private float roomWidth;
            private float roomHeight;
    private float roomStartX;
    private  float roomStartY;
            public RenderingSystem(SpriteBatch batch,OrthographicCamera cam, Viewport viewport) {
                super(Family.all(TextureComponent.class).get());

                textureM = ComponentMapper.getFor(TextureComponent.class);

                renderQueue = new Array<Entity>();

                this.batch = batch;
            this.viewport = viewport;
            this.cam = cam;


            }

            public void updateRoom(float roomWidth, float roomHeight, float roomStartX, float roomStartY){
                this.roomWidth = roomWidth;
                this.roomHeight = roomHeight;
                this.roomStartX = roomStartX;
                this.roomStartY = roomStartY;

            }

            public void resize(int width, int height) {
                viewport.update(width,height,false);
                viewport.getCamera().position.set(viewport.getWorldWidth()/2f,viewport.getWorldHeight()/2f,0);
                viewport.getCamera().update();
            }

            @Override
            public void update(float deltaTime) {
                super.update(deltaTime);

                // Actualiza la cámara y el sprite batch
                cam.update();
                batch.setProjectionMatrix(cam.combined);
                batch.enableBlending();
                batch.begin();

                for (Entity entity : renderQueue) {
                    TextureComponent tex = textureM.get(entity);

                    if (entity.getComponents().contains(ComponentMapper.getFor(PlayerComponent.class).get(entity),true)){

                        float lerp = 0.1f; // Ajusta este valor según sea necesario
                        float targetX = MathUtils.clamp(tex.sprite.getX(),  PixelsToMeters(roomStartX) + cam.viewportWidth / 2, PixelsToMeters(roomStartX) +PixelsToMeters(roomWidth) - cam.viewportWidth / 2);
                        float targetY = MathUtils.clamp(tex.sprite.getY(),  PixelsToMeters(roomStartY) + cam.viewportHeight / 2, PixelsToMeters(roomStartY) +PixelsToMeters(roomHeight) - cam.viewportHeight / 2);

                        cam.position.lerp(new Vector3(targetX, targetY, 0), lerp);

                    }
                    if (tex.sprite == null) {
                        continue;
                    }



                    tex.sprite.draw(batch);


                }


                batch.end();
                renderQueue.clear();
            }



            @Override
            public void processEntity(Entity entity, float deltaTime) {
                renderQueue.add(entity);

                TextureComponent tex = textureM.get(entity);
                if (tex.sprite == null) {
                    return;
                }

                // Invierte el sprite si el jugador se está moviendo hacia la izquierda
                tex.sprite.setFlip(!tex.runningRight, false);
            }


            // convenience method to get camera
            public OrthographicCamera getCamera() {
                return cam;
            }
        }
