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
    public TextureAtlas atlas;
    /**
     * The Name.
     */
    public String name;
    /**
     * The Width.
     */
    public int width;
    /**
     * The Height.
     */
    public int height;
    /**
     * The Hitbox x.
     */
    public int hitboxX;
    /**
     * The Hitbox y.
     */
    public int hitboxY;
    /**
     * The Hp.
     */
    public int hp;
    /**
     * The Idle frames.
     */
    public int idleFrames;
    /**
     * The Walk frames.
     */
    public int walkFrames;
    /**
     * The Attack frames.
     */
    public int attackFrames;

    /**
     * The View distance.
     */
    public float viewDistance;
    /**
     * The Attack distance.
     */
    public float attackDistance;
    /**
     * The Attack cooldown.
     */
    public float attackCooldown;
    /**
     * The Speed.
     */
    public float speed;
    /**
     * The Attack frame.
     */
    public int attackFrame;
    /**
     * The Attack damage.
     */
    public int attackDamage;
    /**
     * The Attack method.
     */
    public String attackMethod;

    /**
     * The Spells.
     */
    public Spell[] spells;


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
