package com.twilightheroes.game.ecs.components.spells;

public class Spell {

    public int id;
    public float manaCost;

    public float castingTime;


    public Spell( int id, float manaCost, float castingTime) {

        this.id = id;
        this.manaCost = manaCost;
        this.castingTime = castingTime;

    }
}
