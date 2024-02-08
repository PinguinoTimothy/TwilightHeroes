package com.twilightheroes.game.tools;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationMaker {

    public static Animation<TextureRegion> crearAnimacion(TextureAtlas atlas, String regionName, int nFrames, int frameDuration) {
        TextureAtlas.AtlasRegion atlasRegion = atlas.findRegion(regionName);

        TextureRegion[][] tmpIdle = atlasRegion.split(atlasRegion.getRegionWidth() / nFrames, atlasRegion.getRegionHeight());

        return new Animation<>(1f / frameDuration, tmpIdle[0]);
    }
}
