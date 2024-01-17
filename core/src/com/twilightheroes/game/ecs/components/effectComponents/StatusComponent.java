package com.twilightheroes.game.ecs.components.effectComponents;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Array;

public class StatusComponent implements Component {
   public Array<StatusEffect> effects = new Array<>();
}
