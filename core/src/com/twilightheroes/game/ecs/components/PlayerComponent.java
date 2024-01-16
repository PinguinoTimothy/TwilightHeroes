package com.twilightheroes.game.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import java.util.HashMap;

public class PlayerComponent implements Component,Pool.Poolable {
    public float speed = 100;
    public float jumpPower = 200;
    public float hp = 100;
    public float mana = 100;
    public float damage;

    public boolean knockback = false;
    public float knockBackTime;
    public boolean inmune = false;
    public float inmuneTime;
    public boolean isDead = false;
    public boolean canDodge = true;
    public boolean canJump = true;
    public float coyoteTime;

    public float attackBuff = 0f;
    public HashMap<Float,Float> damageBuffs = new HashMap<>();

    @Override
    public void reset() {
        knockback = false;

    }
}
