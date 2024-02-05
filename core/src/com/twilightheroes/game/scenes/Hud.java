package com.twilightheroes.game.scenes;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.twilightheroes.game.TwilightHeroes;
import com.twilightheroes.game.tools.Mappers;

public class Hud implements Disposable {
    public Stage stage;
    private Viewport viewport;

    private Integer worldTimer;
    private Float timeCount;
    private Integer score;

    Label countdownLabel;
    Label scoreLabel;


    ProgressBar healthOrb;
    ProgressBar manaOrb;


    Texture hbTexture;
    private static final int FRAME_COLS = 3, FRAME_ROWS = 2;
    TextureRegion[] healthBarImages = new TextureRegion[FRAME_COLS * FRAME_ROWS];
    private AssetManager manager;
    private int vidas;


    public Hud(SpriteBatch sb,AssetManager manager){
    worldTimer = 300;
    timeCount = 0f;
    score = 0;

    this.manager = manager;
    viewport = new FitViewport(TwilightHeroes.V_WIDTH,TwilightHeroes.V_HEIGHT,new OrthographicCamera());
    stage = new Stage(viewport,sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);


        scoreLabel =  new Label(String.format("%06d",score),new Label.LabelStyle(new BitmapFont(), Color.WHITE));



        hbTexture = manager.get("hud/healthBar.png", Texture.class);

        // Use the split utility method to create a 2D array of TextureRegions. This is
        // possible because this sprite sheet contains frames of equal size and they are
        // all aligned.
        TextureRegion[][] tmp = TextureRegion.split(hbTexture,
                hbTexture.getWidth() / FRAME_COLS,
                hbTexture.getHeight() / FRAME_ROWS);

        // Place the regions into a 1D array in the correct order, starting from the top
        // left, going across first. The Animation constructor requires a 1D array.
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                if (i == 1 && j == 2){
                }else{
                    healthBarImages[index++] = tmp[i][j];

                }

            }
        }

        Skin progressBarSkin = manager.get("hud/orbeVidaPequeño.json", Skin.class);

        healthOrb = new ProgressBar(0,100,.1f,true,progressBarSkin);
        healthOrb.setValue(100);
        table.add(healthOrb).size(37,37).left().padTop(10);

        progressBarSkin = manager.get("hud/orbeManaPequeño.json", Skin.class);
        manaOrb = new ProgressBar(0,100,.1f,true,progressBarSkin);
        table.add(manaOrb).size(37,29).expandX().left().padTop(10);
        ImageButton btnPause = new ImageButton(new TextureRegionDrawable(new TextureRegion(manager.get("hud/pauseButton.png", Texture.class))));



        stage.addActor(table);




        }


        public void update(Entity playerEntity){


        healthOrb.setValue(Mappers.statsCom.get(playerEntity).hp);
            manaOrb.setValue(Mappers.playerCom.get(playerEntity).mana);


        }



    @Override
    public void dispose() {
        stage.dispose();
    }
}
