package com.twilightheroes.game.ecs.components;

import com.badlogic.ashley.core.Component;

public class TypeComponent implements Component {
    public static final int PLAYER = 0;
    public static final int ENEMY = 1;
    public static final int FLOOR = 2;
    public static final int WALL = 3;
    public static  final int EXIT = 4;

    public int type = WALL;
}
