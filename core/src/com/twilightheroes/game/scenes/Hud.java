package com.twilightheroes.game.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.twilightheroes.game.TwilightHeroes;

public class Hud implements Disposable {
    public Stage stage;
    private Viewport viewport;

    private Integer worldTimer;
    private Float timeCount;
    private Integer score;

    Label countdownLabel;
    Label scoreLabel;
    Label timeLabel;
    Label levelLabel;
    Label worldLabel;
    Label marioLabel;

    Image healthBar;

    Texture hbTexture;
    private static final int FRAME_COLS = 3, FRAME_ROWS = 2;
    TextureRegion[] healthBarImages = new TextureRegion[FRAME_COLS * FRAME_ROWS];


    public Hud(SpriteBatch sb){
    worldTimer = 300;
    timeCount = 0f;
    score = 0;

    viewport = new FitViewport(TwilightHeroes.V_WIDTH,TwilightHeroes.V_HEIGHT,new OrthographicCamera());
    stage = new Stage(viewport,sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        countdownLabel = new Label(String.format("%03d",worldTimer),new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel =  new Label(String.format("%06d",score),new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel =  new Label("TIME",new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        levelLabel =  new Label("1-1",new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        worldLabel =  new Label("WORLD",new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        marioLabel =  new Label("MARIO",new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        Skin skin = new Skin();
        skin.add("logo", new Texture("prueba1.png"));

        Texture logo = skin.get("logo", Texture.class);

        Drawable D_background = skin.getDrawable("logo");
        hbTexture = new Texture("healthBar.png");

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

        ProgressBar.ProgressBarStyle pb = new ProgressBar.ProgressBarStyle();
        pb.background = D_background;
        healthBar = new Image(healthBarImages[0]);


        table.add(healthBar).expandX().padTop(10);
        table.add(worldLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        table.row();
        table.add(scoreLabel).expandX().padTop(10);
        table.add(levelLabel).expandX().padTop(10);
        table.add(countdownLabel).expandX().padTop(10);



        stage.addActor(table);




        }

        public void crearBotones(Touchpad touchpad, ImageButton btnSalto, ImageButton btnAtaque, ImageButton btnDash, ImageButton btnHabilidad1, ImageButton btnHabilidad2) {
        }
int vidas = 5;
        public void update(float dt){
        timeCount += dt;
        if (timeCount >= 1){
            worldTimer--;
            countdownLabel.setText(String.format("%03d",worldTimer));
            timeCount = 0f;
        }


        //Creo que al actualizarse demasiado rapido, peta
            if (worldTimer == 295){
                vidas--;

                Skin skin2 = new Skin();
                skin2.add("logo", healthBarImages[vidas]);


                Drawable vid = skin2.getDrawable("logo");
                healthBar.setDrawable(vid);
            }


        }


    @Override
    public void dispose() {
        stage.dispose();
    }
}
