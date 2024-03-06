package com.twilightheroes.game.tools;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.twilightheroes.game.ecs.components.spells.Spell;

/**
 * The type Enemy prototype.
 */
public class EnemyPrototype {
    /**
     * The Atlas.
     */
    public final TextureAtlas atlas;
    /**
     * The Name.
     */
    public final String name;
    /**
     * The Width.
     */
    public final int width;
    /**
     * The Height.
     */
    public final int height;
    /**
     * The Hitbox x.
     */
    public final int hitboxX;
    /**
     * The Hitbox y.
     */
    public final int hitboxY;
    /**
     * The Hp.
     */
    public final int hp;
    /**
     * The Idle frames.
     */
    public final int idleFrames;
    /**
     * The Walk frames.
     */
    public final int walkFrames;
    /**
     * The Attack frames.
     */
    public final int attackFrames;

    /**
     * The View distance.
     */
    public final float viewDistance;
    /**
     * The Attack distance.
     */
    public final float attackDistance;
    /**
     * The Attack cooldown.
     */
    public final float attackCooldown;
    /**
     * The Speed.
     */
    public final float speed;
    /**
     * The Attack frame.
     */
    public final int attackFrame;
    /**
     * The Attack damage.
     */
    public final int attackDamage;
    /**
     * The Attack method.
     */
    public final String attackMethod;

    /**
     * The Spells.
     */
    public final Spell[] spells;


    /**
     * Instantiates a new Enemy prototype.
     *
     * @param atlas          the atlas
     * @param name           the name
     * @param width          the width
     * @param height         the height
     * @param hitboxX        the hitbox x
     * @param hitboxY        the hitbox y
     * @param hp             the hp
     * @param idleFrames     the idle frames
     * @param walkFrames     the walk frames
     * @param attackFrames   the attack frames
     * @param viewDistance   the view distance
     * @param attackDistance the attack distance
     * @param attackCooldown the attack cooldown
     * @param speed          the speed
     * @param attackFrame    the attack frame
     * @param attackDamage   the attack damage
     * @param attackMethod   the attack method
     * @param spells         the spells
     */
    public EnemyPrototype(TextureAtlas atlas, String name, int width, int height, int hitboxX, int hitboxY, int hp, int idleFrames, int walkFrames, int attackFrames, float viewDistance, float attackDistance, float attackCooldown, float speed, int attackFrame, int attackDamage, String attackMethod, Spell[] spells) {
        this.atlas = atlas;
        this.name = name;
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
        this.attackDamage = attackDamage;
        this.attackMethod = attackMethod;
        this.spells = spells;
    }
}
