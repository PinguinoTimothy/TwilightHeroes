package com.twilightheroes.game.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.twilightheroes.game.ecs.components.ParticleEffectComponent;
import com.twilightheroes.game.tools.Mappers;

public class ParticleEffectSystem extends IteratingSystem {

    private static final boolean shouldRender = true;

    private Array<Entity> renderQueue;
    private SpriteBatch batch;
    private OrthographicCamera camera;


    public ParticleEffectSystem(SpriteBatch sb, OrthographicCamera cam) {
        super(Family.all(ParticleEffectComponent.class).get());
        renderQueue = new Array<Entity>();
        batch = sb;
        camera = cam;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        batch.setProjectionMatrix(camera.combined);
        batch.enableBlending();
        // Render PE
        if(shouldRender){
            batch.begin();
            for (Entity entity : renderQueue) {
                ParticleEffectComponent pec = Mappers.partCom.get(entity);
                pec.particleEffect.draw(batch, deltaTime);
            }
            batch.end();
        }
        renderQueue.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ParticleEffectComponent pec = Mappers.partCom.get(entity);

        // Move PE if attached
        if(pec.isattached){
            pec.particleEffect.setPosition(
                    pec.attachedBody.getPosition().x + pec.xOffset,
                    pec.attachedBody.getPosition().y + pec.yOffset);
        }
        // free PE if completed
        if(pec.particleEffect.isComplete() && pec.isDead){
            getEngine().removeEntity(entity);
        }else{
            renderQueue.add(entity);
        }
    }
}
