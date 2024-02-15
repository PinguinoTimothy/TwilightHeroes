package com.twilightheroes.game.tools;


import com.badlogic.gdx.utils.Array;

public class PlayerSettings {

    public String spell1;
    public String spell2;
    public int money;
    public int level;

    public int lastObelisk;

    public boolean[] doorsOpened = new boolean[100];

    public Array<KillCounter> killCounter = new Array<>();


}
