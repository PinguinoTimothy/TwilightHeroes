package com.twilightheroes.game.tools;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.I18NBundleLoader;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Json;

import javax.swing.JEditorPane;

public class B2AssetManager {
    public final AssetManager manager = new AssetManager();




    public void loadImages(){
        manager.load("enemies/caballero.atlas", TextureAtlas.class);
        manager.load("enemies/enemy.png", Texture.class);
        manager.load("enemies/skeleton.atlas", TextureAtlas.class);

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

        manager.load("backgrounds/mainMenuBackground.png",Texture.class);

        manager.load("variety/checkboxOff.png",Texture.class);
        manager.load("variety/checked.png",Texture.class);

        manager.load("spells/spells.atlas",TextureAtlas.class);

        manager.load("spells/healingSpell.png",Texture.class);
        manager.load("spells/healingSpellChecked.png",Texture.class);
        manager.load("spells/frostSpear.png",Texture.class);
        manager.load("spells/frostSpearChecked.png",Texture.class);

        manager.load("arrow.atlas", TextureAtlas.class);
        manager.load("arrow.png", Texture.class);
        manager.load("pantallaNegra.jpg", Texture.class);
        manager.load("scrollbar-vertical.png", Texture.class);

         String skin	= "hud/neonui/neon-ui.json";
        SkinLoader.SkinParameter params = new SkinLoader.SkinParameter("hud/neonui/neon-ui.atlas");
        manager.load(skin, Skin.class,params);



    }




}
