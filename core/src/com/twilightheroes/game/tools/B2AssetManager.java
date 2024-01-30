package com.twilightheroes.game.tools;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class B2AssetManager {
    public final AssetManager manager = new AssetManager();




    public void loadImages(){
        manager.load("enemies/caballero.atlas", TextureAtlas.class);
        manager.load("enemies/enemy.png", Texture.class);
        manager.load("enemies/skeleton.atlas", TextureAtlas.class);
        manager.load("enemies/caballero.atlas", TextureAtlas.class);

        manager.load("player/player.atlas", TextureAtlas.class);
        manager.load("player/player.png", Texture.class);

        manager.load("hud/attack.png",Texture.class);
        manager.load("hud/attackchecked.png",Texture.class);

        manager.load("hud/dodge.png",Texture.class);
        manager.load("hud/healthBar.png",Texture.class);
        manager.load("hud/Joystick.png",Texture.class);
        manager.load("hud/SmallHandleFilled.png",Texture.class);
        manager.load("hud/jump.png",Texture.class);

        manager.load("hud/habilidad1.png",Texture.class);


        manager.load("background.png",Texture.class);
        manager.load("hud/play01.png",Texture.class);
        manager.load("hud/healthBar.png",Texture.class);
        manager.load("hud/knob.png",Texture.class);

        manager.load("arrow.atlas", TextureAtlas.class);
        manager.load("arrow.png", Texture.class);
        manager.load("pantallaNegra.jpg", Texture.class);
        manager.load("scrollbar-vertical.png", Texture.class);

         String skin	= "hud/neonui/neon-ui.json";
        SkinLoader.SkinParameter params = new SkinLoader.SkinParameter("hud/neonui/neon-ui.atlas");
        manager.load(skin, Skin.class,params);


    }




}
