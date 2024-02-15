package com.twilightheroes.game.tools;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class B2AssetManager {
    public final AssetManager manager = new AssetManager();


    public void loadImages() {
        manager.load("enemies/caballero.atlas", TextureAtlas.class);
        manager.load("enemies/enemy.png", Texture.class);
        manager.load("enemies/skeleton.atlas", TextureAtlas.class);

        manager.load("enemies/nightborne.atlas", TextureAtlas.class);


        manager.load("player/player.atlas", TextureAtlas.class);
        manager.load("player/player.png", Texture.class);

        manager.load("hud/attack.png", Texture.class);
        manager.load("hud/attackchecked.png", Texture.class);

        manager.load("hud/dodge.png", Texture.class);
        manager.load("hud/healthBar.png", Texture.class);
        manager.load("hud/Joystick.png", Texture.class);
        manager.load("hud/SmallHandleFilled.png", Texture.class);
        manager.load("hud/jump.png", Texture.class);

        manager.load("hud/habilidad1.png", Texture.class);
        manager.load("hud/pauseButton.png", Texture.class);



        manager.load("background.png", Texture.class);
        manager.load("hud/play01.png", Texture.class);
        manager.load("hud/healthBar.png", Texture.class);
        manager.load("hud/knob.png", Texture.class);

        manager.load("backgrounds/mainMenuBackground.png", Texture.class);

        manager.load("variety/checkboxOff.png", Texture.class);
        manager.load("variety/checked.png", Texture.class);
        manager.load("variety/obelisk.atlas", TextureAtlas.class);


        manager.load("spells/spells.atlas", TextureAtlas.class);

        manager.load("spells/healingSigil.png", Texture.class);
        manager.load("spells/healingSigilChecked.png", Texture.class);
        manager.load("spells/frostSpear.png", Texture.class);
        manager.load("spells/frostSpearChecked.png", Texture.class);
        manager.load("spells/shockingGrasp.png", Texture.class);
        manager.load("spells/shockingGraspChecked.png", Texture.class);
        manager.load("spells/spellsVFX/spells.atlas", TextureAtlas.class);
        manager.load("spells/spellsVFX/shockingGrasp.png", Texture.class);
        manager.load("spells/spellsVFX/earthSpike.png", Texture.class);

        manager.load("spells/earthSpike.png", Texture.class);
        manager.load("spells/earthSpikeChecked.png", Texture.class);

        manager.load("arrow.atlas", TextureAtlas.class);
        manager.load("arrow.png", Texture.class);
        manager.load("pantallaNegra.jpg", Texture.class);
        manager.load("scrollbar-vertical.png", Texture.class);


        manager.load("backgrounds/book.png", Texture.class);
        manager.load("skele.png", Texture.class);

        manager.load("enemies/enemySample/caballero.png", Texture.class);
        manager.load("enemies/enemySample/skeleton.png", Texture.class);
        manager.load("enemies/enemySample/nightborne.png", Texture.class);

        manager.load("hud/backButton.png", Texture.class);

        String skin = "hud/neonui/neon-ui.json";
        SkinLoader.SkinParameter params = new SkinLoader.SkinParameter("hud/neonui/neon-ui.atlas");
        manager.load(skin, Skin.class, params);

        String skin2 = "hud/orbeVidaPequeño.json";
        SkinLoader.SkinParameter params2 = new SkinLoader.SkinParameter("hud/orbeVidaPequeño.atlas");
        manager.load(skin2, Skin.class, params2);

        String skinMana = "hud/orbeManaPequeño.json";
        SkinLoader.SkinParameter params3 = new SkinLoader.SkinParameter("hud/orbeManaPequeño.atlas");
        manager.load(skinMana, Skin.class, params3);

        String skinDialogo = "hud/dialogSkin.json";
        SkinLoader.SkinParameter params4 = new SkinLoader.SkinParameter("hud/dialogSkin.atlas");
        manager.load(skinDialogo, Skin.class, params4);

    }


}
