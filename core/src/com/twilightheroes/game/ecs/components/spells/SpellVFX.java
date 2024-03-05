package com.twilightheroes.game.ecs.components.spells;

/**
 * The type Spell vfx.
 */
public class SpellVFX {

    /**
     * The number of frames.
     */
    public int nFrames;
    /**
     * The Frame duration.
     */
    public int frameDuration;

    /**
     * Instantiates a new Spell vfx.
     *
     * @param nFrames       the number of frames
     * @param frameDuration the frame duration
     */
    public SpellVFX(int nFrames, int frameDuration) {

        this.nFrames = nFrames;
        this.frameDuration = frameDuration;
    }
}
