package com.twilightheroes.game.tools;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.twilightheroes.game.ecs.components.StateComponent;

public class AnimationMaker {

    public static Animation<TextureRegion> crearAnimacion(TextureAtlas atlas, String regionName, int nFrames){
        TextureAtlas.AtlasRegion atlasRegion = atlas.findRegion(regionName);

        TextureRegion[][] tmpIdle = atlasRegion.split(atlasRegion.getRegionWidth()/nFrames,atlasRegion.getRegionHeight());
        TextureRegion[] idleFrames = new TextureRegion[nFrames];
        for (int i = 0; i < nFrames; i++) {
            idleFrames[i] = tmpIdle[0][i];
        }
        return new Animation<TextureRegion>(1f/nFrames,idleFrames);
    }
}
