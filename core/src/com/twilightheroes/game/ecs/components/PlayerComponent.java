package com.twilightheroes.game.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Pool;

public class PlayerComponent implements Component, Pool.Poolable {

    public float mana = 100;


    public boolean knockback = false;
    public float knockBackTime;
    public boolean inmune = false;
    public float inmuneTime;
    public boolean isDead = false;
    public boolean canDodge = true;
    public boolean canJump = true;
    public boolean canAttack = true;
    public float coyoteTime;
    public Fixture interactFixture;

    public boolean end = false;


    @Override
    public void reset() {
        knockback = false;
        isDead = false;
        canJump = true;
        canDodge = true;
        canAttack = true;
        inmune = false;
end =  false;
    }
}
