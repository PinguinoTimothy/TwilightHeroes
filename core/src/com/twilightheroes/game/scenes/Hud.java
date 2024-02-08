package com.twilightheroes.game.scenes;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.twilightheroes.game.TwilightHeroes;
import com.twilightheroes.game.tools.Mappers;

public class Hud implements Disposable {
    public Stage stage;


    ProgressBar healthOrb;
    ProgressBar manaOrb;


    Texture hbTexture;


    public Hud(SpriteBatch sb, AssetManager manager) {

        Viewport viewport = new FitViewport(TwilightHeroes.V_WIDTH, TwilightHeroes.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);


        hbTexture = manager.get("hud/healthBar.png", Texture.class);


        Skin progressBarSkin = manager.get("hud/orbeVidaPequeño.json", Skin.class);

        healthOrb = new ProgressBar(0, 100, .1f, true, progressBarSkin);
        healthOrb.setValue(100);
        table.add(healthOrb).size(37, 37).left().padTop(10).padLeft(25);

        progressBarSkin = manager.get("hud/orbeManaPequeño.json", Skin.class);
        manaOrb = new ProgressBar(0, 100, .1f, true, progressBarSkin);
        table.add(manaOrb).size(37, 29).expandX().left().padTop(10).padLeft(15);


        stage.addActor(table);



    }

    public void hideHudElements() {


        for (Actor act: stage.getActors()) {
            act.setVisible(false);
        }
    }

    public void showHudElements() {
        for (Actor act: stage.getActors()) {
            act.setVisible(true);
        }
    }


    public void update(Entity playerEntity) {


        healthOrb.setValue(Mappers.statsCom.get(playerEntity).hp);
        manaOrb.setValue(Mappers.playerCom.get(playerEntity).mana);


    }


    @Override
    public void dispose() {
        stage.dispose();
    }
}
