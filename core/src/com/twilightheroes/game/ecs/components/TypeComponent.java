package com.twilightheroes.game.ecs.components;

import com.badlogic.ashley.core.Component;

/**
 * The type Type component.
 */
public class TypeComponent implements Component {
    /**
     * The constant PLAYER.
     */
    public static final int PLAYER = 0;
    /**
     * The constant ENEMY.
     */
    public static final int ENEMY = 1;
    /**
     * The constant FLOOR.
     */
    public static final int FLOOR = 2;
    /**
     * The constant WALL.
     */
    public static final int WALL = 3;
    /**
     * The constant EXIT.
     */
    public static final int EXIT = 4;
    /**
     * The constant BULLET.
     */
    public static final int BULLET = 5;
    /**
     * The constant ROOM.
     */
    public static final int ROOM = 6;
    /**
     * The constant HAZARD.
     */
    public static final int HAZARD = 7;
    /**
     * The constant INTERACTABLE.
     */
    public static final int INTERACTABLE = 8;
    /**
     * The constant DOOR.
     */
    public static final int DOOR = 9;


    /**
     * The Type.
     */
    public int type = WALL;
}
