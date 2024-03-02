package com.twilightheroes.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.twilightheroes.game.TwilightHeroes;

public class WinScreen implements Screen {

    private final TwilightHeroes parent;
    private Stage stage;

    public WinScreen(TwilightHeroes twilightHeroes) {
        parent = twilightHeroes;
        // get skin
        Skin skin = new Skin();
        skin.add("play", parent.assMan.manager.get("hud/play01.png"));
        Texture background = parent.assMan.manager.get("backgrounds/winscreenBackground.jpeg");


        // create stage and set it as input processor
        stage = new Stage(new ExtendViewport(1920, 1080));

        // create table to layout iutems we will add
        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(true);
        table.setBackground(new TextureRegionDrawable(new TextureRegion(background)));

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("KarmaFuture.ttf"));
        final FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 120;
        BitmapFont font12 = generator.generateFont(parameter); // font size 12 pixels
        parameter.size = 45;
        parameter.borderWidth = 2f;
        parameter.borderColor = Color.BLACK;
        BitmapFont fontBig = generator.generateFont(parameter);
        generator.dispose(); // don't forget to dispose to avoid memory leaks!


        Label.LabelStyle label1Style = new Label.LabelStyle();
        label1Style.font = font12;
        label1Style.fontColor = Color.RED;


        Label deadLabel = new Label("Continuara...", label1Style);


        // Create a ButtonStyle and set its 'up' field with a Drawable


        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = fontBig;

       TextButton btnVolver = new TextButton(parent.language == TwilightHeroes.languages.ES ? "Volver Al Menu" : "Back To Menu",textButtonStyle);
        btnVolver.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                parent.changeScreen(TwilightHeroes.MENU);
            }
        });

        // add items to table
        table.add(deadLabel).colspan(2);
        table.row().padTop(10);
        table.row().padTop(10);
        table.row().padTop(50);
        table.add(btnVolver).colspan(2);

        //add table to stage
        stage.addActor(table);
    }



    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);


    }

    @Override
    public void render(float delta) {
        // clear the screen ready for next set of images to be drawn
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // change the stage's viewport when teh screen size is changed
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }
}
