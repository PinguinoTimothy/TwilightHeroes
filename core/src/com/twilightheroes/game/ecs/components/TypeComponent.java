package com.twilightheroes.game.ecs.components;

import com.badlogic.ashley.core.Component;

public class TypeComponent implements Component {
    public static final int PLAYER = 4;
    public static final int ENEMY = 1;
    public static final int SCENERY = 3;
    public static final int OTHER = 0;
    public static  final int EXIT = 5;

    public int type = OTHER;
}
