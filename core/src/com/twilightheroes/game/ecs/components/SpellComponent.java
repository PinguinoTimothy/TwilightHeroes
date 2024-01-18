package com.twilightheroes.game.ecs.components;

import com.badlogic.ashley.core.Component;

public class SpellComponent implements Component {

    public Spell spell1 = new Spell("Shocking Grasp",SpellList.HEAL,25,true);
    public Spell spell2 = new Spell("Fury",SpellList.FURY,50,true);

}
