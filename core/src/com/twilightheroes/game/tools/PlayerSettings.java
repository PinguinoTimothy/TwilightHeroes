package com.twilightheroes.game.tools;


import com.badlogic.gdx.utils.Array;

/**
 * The type Player settings.
 */
public class PlayerSettings {

    /**
     * The Doors opened.
     */
    public final boolean[] doorsOpened = new boolean[100];
    /**
     * The Kill counter.
     */
    public final Array<KillCounter> killCounter = new Array<>();
    /**
     * The Spell 1.
     */
    public String spell1;
    /**
     * The Spell 2.
     */
    public String spell2;
    /**
     * The Money.
     */
    public int money;
    /**
     * The Level.
     */
    public int level;
    /**
     * The Last level.
     */
    public int lastLevel;


}
