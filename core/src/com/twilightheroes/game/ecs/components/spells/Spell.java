package com.twilightheroes.game.ecs.components.spells;

public class Spell {

    public String name;
    public int id;
    public float manaCost;

    public boolean unlocked;


    public Spell(String name, int id, float manaCost, boolean unlocked) {
        this.name = name;
        this.id = id;
        this.manaCost = manaCost;
        this.unlocked = unlocked;
    }
}
