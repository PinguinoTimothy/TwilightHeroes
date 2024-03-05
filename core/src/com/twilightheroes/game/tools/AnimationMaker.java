package com.twilightheroes.game.tools;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * The type Animation maker.
 */
public class AnimationMaker {

    /**
     * Crear animacion animation.
     *
     * @param atlas         the atlas
     * @param regionName    the region name
     * @param nFrames       the n frames
     * @param frameDuration the frame duration
     * @return the animation
     */
    public static Animation<TextureRegion> crearAnimacion(TextureAtlas atlas, String regionName, int nFrames, int frameDuration) {
        TextureAtlas.AtlasRegion atlasRegion = atlas.findRegion(regionName);

        TextureRegion[][] tmpIdle = atlasRegion.split(atlasRegion.getRegionWidth() / nFrames, atlasRegion.getRegionHeight());

        return new Animation<>(1f / frameDuration, tmpIdle[0]);
    }
}
