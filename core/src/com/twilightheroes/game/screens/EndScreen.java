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
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.twilightheroes.game.TwilightHeroes;

public class EndScreen implements Screen {

    private final TwilightHeroes parent;
    private Stage stage;

    public EndScreen(TwilightHeroes twilightHeroes) {
        parent = twilightHeroes;
    }

    @Override
    public void show() {
        // get skin
        Skin skin = new Skin();
        skin.add("play", parent.assMan.manager.get("hud/play01.png"));
        Texture background = parent.assMan.manager.get("background.png");


        // create stage and set it as input processor
        stage = new Stage(new ExtendViewport(1920, 1080));
        Gdx.input.setInputProcessor(stage);

        // create table to layout iutems we will add
        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(true);
        table.setBackground(new TextureRegionDrawable(new TextureRegion(background)));

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("KarmaFuture.ttf"));
        final FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 120;
        BitmapFont font12 = generator.generateFont(parameter); // font size 12 pixels
        generator.dispose(); // don't forget to dispose to avoid memory leaks!


        Label.LabelStyle label1Style = new Label.LabelStyle();
        label1Style.font = font12;
        label1Style.fontColor = Color.RED;


        Label deadLabel = new Label("Has muerto", label1Style);

        Button btn1 = new Button();

        // Create a ButtonStyle and set its 'up' field with a Drawable
        Button.ButtonStyle btnStyle = new Button.ButtonStyle();
        btnStyle.up = new TextureRegionDrawable(new TextureRegion(parent.assMan.manager.get("hud/play01.png", Texture.class)));
        btn1.setStyle(btnStyle);
        btn1.setSkin(skin);
        btn1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                parent.restart();
            }
        });

        // add items to table
        table.add(deadLabel).colspan(2);
        table.row().padTop(10);
        table.add(btn1).colspan(2);
        table.row().padTop(10);
        table.add(btn1).uniformX().align(Align.left);
        table.add(btn1).uniformX().align(Align.left);
        table.row().padTop(10);
        table.add(btn1).uniformX().align(Align.left);
        table.add(btn1).uniformX().align(Align.left);
        table.row().padTop(50);
        table.add(btn1).colspan(2);

        //add table to stage
        stage.addActor(table);

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
