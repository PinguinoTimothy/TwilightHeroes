package com.twilightheroes.game.tools;

/**
 * The type Room size.
 */
public class RoomSize {

    /**
     * The Room width.
     */
    public float roomWidth;
    /**
     * The Room height.
     */
    public float roomHeight;
    /**
     * The Room start x.
     */
    public float roomStartX;
    /**
     * The Room start y.
     */
    public float roomStartY;

    /**
     * Instantiates a new Room size.
     *
     * @param roomWidth  the room width
     * @param roomHeight the room height
     * @param roomStartX the room start x
     * @param roomStartY the room start y
     */
    public RoomSize(float roomWidth, float roomHeight, float roomStartX, float roomStartY) {
        this.roomWidth = roomWidth;
        this.roomHeight = roomHeight;
        this.roomStartX = roomStartX;
        this.roomStartY = roomStartY;

    }
}
