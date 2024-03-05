package com.twilightheroes.game.tools;


import com.badlogic.gdx.utils.Array;

/**
 * The type Player settings.
 */
public class PlayerSettings {

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
     * The Last obelisk.
     */
    public int lastObelisk;

    /**
     * The Doors opened.
     */
    public boolean[] doorsOpened = new boolean[100];

    /**
     * The Kill counter.
     */
    public Array<KillCounter> killCounter = new Array<>();


}
