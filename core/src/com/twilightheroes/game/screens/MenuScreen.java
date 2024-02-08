package com.twilightheroes.game.screens;

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
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.twilightheroes.game.TwilightHeroes;
import com.twilightheroes.game.tools.WidgetContainer;

public class MenuScreen implements Screen {
    private final Skin skin;

    private final Stage stage;
    TwilightHeroes parent;
    JsonValue language;

    public MenuScreen(TwilightHeroes TH) {
        this.parent = TH;
        language = TH.jsonMultilanguage.get("menu");


        stage = new Stage(new StretchViewport(1920, 1080));
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

        TextButton textButton = new TextButton(language.get("play").asString(), textButtonStyle);
        textButton.setName("play");
        WidgetContainer widgetContainer = new WidgetContainer();
        widgetContainer.widgets.add(textButton);
        table1.add(textButton).padBottom(30.0f);
        textButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                parent.restart();
            }
        });

        table1.row();
        textButton = new TextButton(language.get("options").asString(), textButtonStyle);
        textButton.setName("options");
        widgetContainer.widgets.add(textButton);
        table1.add(textButton).padBottom(30.0f);
        textButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                parent.changeScreen(TwilightHeroes.OPTIONS);
            }
        });


        table1.row();
        textButton = new TextButton(language.get("bestiary").asString(), textButtonStyle);
        textButton.setName("bestiary");
        widgetContainer.widgets.add(textButton);
        table1.add(textButton).padBottom(30.0f);
        textButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                parent.changeScreen(TwilightHeroes.BESTIARY);
            }
        });


        table1.row();
        textButton = new TextButton(language.get("credits").asString(), textButtonStyle);
        textButton.setName("credits");
        widgetContainer.widgets.add(textButton);
        table1.add(textButton).padBottom(30.0f);

        table1.row();
        textButton = new TextButton(language.get("controls").asString(), textButtonStyle);
        textButton.setName("controls");
        widgetContainer.widgets.add(textButton);

        table1.add(textButton);

        stack.addActor(table1);
        table.add(stack).grow();
        stage.addActor(table);

        widgetContainer.nameScreen = "menu";
        parent.widgets.add(widgetContainer);

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

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
