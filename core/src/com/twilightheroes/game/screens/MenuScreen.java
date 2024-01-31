package com.twilightheroes.game.screens;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.twilightheroes.game.TwilightHeroes;

import java.awt.GradientPaint;

import jdk.tools.jmod.Main;

public class MenuScreen implements Screen {
    private Skin skin;

    private Stage stage;
    TwilightHeroes parent;

   public MenuScreen(TwilightHeroes parent){
       this.parent = parent;
   }

    @Override
    public void show() {
        stage = new Stage(new StretchViewport(1920,1080));
        skin = new Skin(Gdx.files.internal("skin.json"));
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("KarmaFuture.ttf"));
        final FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 50;
        parameter.color = Color.CYAN;
        parameter.borderColor = Color.valueOf("fea973");
        parameter.borderWidth = 1f;
        BitmapFont fontButtons = generator.generateFont(parameter);

        parameter.size = 70;
        parameter.color = Color.valueOf("731116");
        parameter.borderWidth = 0;
        BitmapFont fontTitle = generator.generateFont(parameter);

        generator.dispose();
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = fontButtons;

        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);

        Stack stack = new Stack();

        Image image = new Image(parent.assMan.manager.get("backgrounds/mainMenuBackground.png", Texture.class));
        image.setScaling(Scaling.fill);
        stack.addActor(image);

        Table table1 = new Table();

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = fontTitle;
        Label lblTitle = new Label("Twilight Heroes", labelStyle);
        table1.add(lblTitle).padTop(-450f).row();

        TextButton textButton = new TextButton("Jugar", textButtonStyle);
        textButton.setName("play");
        table1.add(textButton).padBottom(30.0f);
textButton.addListener(new ClickListener()
{
    @Override
    public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);
        parent.restart();
    }
});

        table1.row();
        textButton = new TextButton("Opciones", textButtonStyle);
        textButton.setName("options");
        table1.add(textButton).padBottom(30.0f);
textButton.addListener(new ClickListener(){
    @Override
    public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);
        parent.changeScreen(TwilightHeroes.OPTIONS);
    }
});


        table1.row();
        textButton = new TextButton("Logros", textButtonStyle);
        textButton.setName("logres");
        table1.add(textButton).padBottom(30.0f);

        table1.row();
        textButton = new TextButton("Creditos", textButtonStyle);
        textButton.setName("credits");
        table1.add(textButton).padBottom(30.0f);

        table1.row();
        textButton = new TextButton("Controles", textButtonStyle);
        textButton.setName("controls");
        table1.add(textButton);
        stack.addActor(table1);
        table.add(stack).grow();
        stage.addActor(table);


    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    public void resize(int width, int height) {
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

    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
