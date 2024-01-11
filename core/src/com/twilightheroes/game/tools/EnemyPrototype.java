package com.twilightheroes.game.tools;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class EnemyPrototype {
    public TextureAtlas atlas;
    public int width;
    public int height;
    public int hitboxX;
    public int hitboxY;
    public int hp;
    public int idleFrames;
    public int walkFrames;
    public int attackFrames;

    public float viewDistance;
    public float attackDistance;
    public float attackCooldown;
    public float speed;
    public int attackFrame;


    public EnemyPrototype(TextureAtlas atlas, int width, int height, int hitboxX, int hitboxY, int hp, int idleFrames, int walkFrames, int attackFrames, float viewDistance, float attackDistance, float attackCooldown, float speed, int attackFrame) {
        this.atlas = atlas;
        this.width = width;
        this.height = height;
        this.hitboxX = hitboxX;
        this.hitboxY = hitboxY;
        this.hp = hp;
        this.idleFrames = idleFrames;
        this.walkFrames = walkFrames;
        this.attackFrames = attackFrames;
        this.viewDistance = viewDistance;
        this.attackDistance = attackDistance;
        this.attackCooldown = attackCooldown;
        this.speed = speed;
        this.attackFrame = attackFrame;
    }
}