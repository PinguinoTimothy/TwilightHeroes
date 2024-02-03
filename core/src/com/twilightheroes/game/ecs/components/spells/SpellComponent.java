package com.twilightheroes.game.ecs.components.spells;

import com.badlogic.ashley.core.Component;

public class SpellComponent implements Component {

   public Spell spell1;
  public Spell spell2;

    public Spell spellToCast = null;

    public boolean casting = false;
    public float castingTime = 0f;



}
