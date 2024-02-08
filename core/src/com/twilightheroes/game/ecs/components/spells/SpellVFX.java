package com.twilightheroes.game.ecs.components.spells;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SpellVFX {

    public int nFrames;
    public int frameDuration;

    public SpellVFX( int nFrames, int frameDuration) {

        this.nFrames = nFrames;
        this.frameDuration = frameDuration;
    }
}
