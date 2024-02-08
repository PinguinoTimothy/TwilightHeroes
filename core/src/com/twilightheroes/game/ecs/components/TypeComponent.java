package com.twilightheroes.game.ecs.components;

import com.badlogic.ashley.core.Component;

public class TypeComponent implements Component {
    public static final int PLAYER = 0;
    public static final int ENEMY = 1;
    public static final int FLOOR = 2;
    public static final int WALL = 3;
    public static  final int EXIT = 4;
    public static  final int BULLET = 5;
    public static final int ROOM = 6;
    public static final int HAZARD = 7;
    public static final int INTERACTABLE = 8;



    public int type = WALL;
}
