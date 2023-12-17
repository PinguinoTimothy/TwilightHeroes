package com.twilightheroes.game.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.IntMap;

public class AnimationComponent implements Component {
    public IntMap<Animation<TextureRegion>> animations = new IntMap<Animation<TextureRegion>>();

    public void reset() {
        animations = new IntMap<Animation<TextureRegion>>();
    }
}
