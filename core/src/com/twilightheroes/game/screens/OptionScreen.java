package com.twilightheroes.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.twilightheroes.game.TwilightHeroes;
import com.twilightheroes.game.tools.WidgetContainer;

/**
 * The type Option screen.
 */
public class OptionScreen implements Screen {

    /**
     * The Slider volume.
     */
    public final Slider sliderVolume;
    /**
     * The Btn menu hechizos.
     */
    final TextButton btnMenuHechizos;
    /**
     * The Btn menu opciones.
     */
    final TextButton btnMenuOpciones;
    /**
     * The Chk accelerometer.
     */
    final CheckBox chkAccelerometer;
    /**
     * The Chk vibrator.
     */
    final CheckBox chkVibrator;
    /**
     * The Language select box.
     */
    final SelectBox<String> languageSelectBox;
    /**
     * The Btn volver.
     */
    final TextButton btnVolver;
    /**
     * Boton volver al menu
     */
    final TextButton btnVolverMenu;
    /**
     * The Lbl volume.
     */
    final Label lblVolume;
    /**
     * The Widgets.
     */
    final WidgetContainer widgets = new WidgetContainer();
    private final TwilightHeroes parent;
    private final Table mainTable;
    /**
     * The Name.
     */
    public Label name;
    /**
     * The Lbl language.
     */
    Label lblLanguage;
    private Stage stage;


    /**
     * Instantiates a new Option screen.
     *
     * @param twilightHeroes the twilight heroes
     */
    public OptionScreen(TwilightHeroes twilightHeroes) {
        parent = twilightHeroes;
        JsonValue language = parent.jsonMultilanguage.get("options");

        stage = new Stage(new ExtendViewport(900, 550));
        Skin skin = new Skin(Gdx.files.internal("skin.json"));

        Table table = new Table();
        table.setFillParent(true);

        Stack stack = new Stack();

        Image image = new Image(skin, "background");
        image.setScaling(Scaling.fill);
        stack.addActor(image);

        mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.top().padTop(20.0f);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("KarmaFuture.ttf"));
        final FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 45;
        parameter.borderWidth = 2f;
        parameter.borderColor = Color.BLACK;
        BitmapFont fontBig = generator.generateFont(parameter);
        parameter.size = 30;
        BitmapFont fontMedium = generator.generateFont(parameter);
        parameter.size = 15;
        BitmapFont fontSmall = generator.generateFont(parameter);

        generator.dispose();
        skin = new Skin();
        skin.add("font", fontBig);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = fontBig;

        // Alinea los botones "Hechizos" y "Opciones" en la parte superior y a los lados
        btnMenuHechizos = createTextButton(language.get("spells").asString(), textButtonStyle);
        btnMenuHechizos.setName("spells");
        btnMenuHechizos.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                parent.changeScreen(TwilightHeroes.MAGIC);
            }
        });
        widgets.widgets.add(btnMenuHechizos);

        btnMenuOpciones = createTextButton(language.get("options").asString(), textButtonStyle);
        btnMenuOpciones.setName("options");
        btnMenuOpciones.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (parent.inGame) {
                    parent.changeScreen(TwilightHeroes.APPLICATION);
                }
            }
        });
        widgets.widgets.add(btnMenuOpciones);


        // Crear widgets para configuraciones de opciones
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = fontMedium;
        lblVolume = new Label(language.get("volume").asString(), labelStyle);
        lblVolume.setName("volume");
        widgets.widgets.add(lblVolume);

        skin = parent.assMan.manager.get("hud/neonui/neon-ui.json", Skin.class);
        sliderVolume = new Slider(0.0f, 1.0f, 0.1f, false, skin);
        sliderVolume.getStyle().background.setMinHeight(30f);
        sliderVolume.setValue(parent.musicVolume);

        CheckBox.CheckBoxStyle checkBoxStyle = new CheckBox.CheckBoxStyle();
        checkBoxStyle.checkboxOff = new TextureRegionDrawable(new TextureRegion(parent.assMan.manager.get("variety/checkboxOff.png", Texture.class)));
        checkBoxStyle.checkboxOn = new TextureRegionDrawable(new TextureRegion(parent.assMan.manager.get("variety/checked.png", Texture.class)));
        checkBoxStyle.font = fontMedium;
        chkAccelerometer = new CheckBox(language.get("accelerometer").asString(), checkBoxStyle);
        chkAccelerometer.setChecked(!parent.accelerometerOn);
        chkAccelerometer.setName("accelerometer");
        widgets.widgets.add(chkAccelerometer);

        chkVibrator = new CheckBox(language.get("vibrator").asString(), checkBoxStyle);
        chkVibrator.setChecked(!parent.vibratorOn);
        chkVibrator.setName("vibrator");
        widgets.widgets.add(chkVibrator);

        Label lblLanguage = new Label(language.get("language").asString(), labelStyle);
        lblLanguage.setName("language");
        widgets.widgets.add(lblLanguage);

        languageSelectBox = new SelectBox<>(skin);
        languageSelectBox.getStyle().font = fontSmall;

        languageSelectBox.setItems("Espanol", "English");
        languageSelectBox.setSelectedIndex(parent.language.ordinal());

        btnVolver = createTextButton(language.get("back").asString(), textButtonStyle);
        btnVolver.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                parent.changeScreen(parent.previousScreen);
            }
        });
        btnVolver.setName("back");
        widgets.widgets.add(btnVolver);

        btnVolverMenu = createTextButton(language.get("backMenu").asString(), textButtonStyle);
        btnVolverMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                parent.changeScreen(TwilightHeroes.MENU);
            }
        });
        btnVolverMenu.setName("backMenu");
        widgets.widgets.add(btnVolverMenu);

        // Agregar listeners para manejar cambios en las configuraciones
        sliderVolume.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (parent.music != null) {
                    parent.music.setVolume(sliderVolume.getValue());
                }
                parent.musicVolume = sliderVolume.getValue();
            }
        });

        chkAccelerometer.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.accelerometerOn = !chkAccelerometer.isChecked();
            }
        });

        chkVibrator.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                parent.vibratorOn = !chkVibrator.isChecked();
            }
        });

        languageSelectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.language = TwilightHeroes.languages.values()[languageSelectBox.getSelectedIndex()];
                parent.updateLanguage();
            }
        });


        stack.add(mainTable);
        table.add(stack).grow();
        stage.addActor(table);

        widgets.nameScreen = "options";
        parent.widgets.add(widgets);
    }


    /**
     * Called when this screen becomes the current screen for the game.
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        mainTable.clear();
        if (parent.inGame) {
            mainTable.add(btnMenuHechizos).padBottom(20.0f).align(Align.left).padRight(200f);
            mainTable.add(btnMenuOpciones).padBottom(20.0f).align(Align.right).padLeft(200f).row();

        } else {
            mainTable.removeActor(btnMenuHechizos);
            mainTable.add(btnMenuOpciones).padBottom(20.0f).align(Align.center).padLeft(50f).row();

        }
        mainTable.add(lblVolume).colspan(2).center().row();
        mainTable.add(sliderVolume).colspan(2).center().row();
        mainTable.add(lblLanguage).colspan(2).center().row();
        mainTable.add(languageSelectBox).colspan(2).center().row();
        mainTable.add(chkAccelerometer).colspan(2).center().row();
        mainTable.add(chkVibrator).colspan(2).center().row();
        mainTable.add(btnVolver).colspan(2).center().row();
        mainTable.add(btnVolverMenu).colspan(2).center().row();

    }


    // Helper method to create TextButton
    private TextButton createTextButton(String text, TextButton.TextButtonStyle style) {
        TextButton textButton = new TextButton(text, style);
        textButton.setName(text.toLowerCase()); // Set the name to lowercase for consistency
        return textButton;
    }


    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // Se puede implementar si es necesario

    }

    @Override
    public void pause() {
        // Se puede implementar si es necesario
    }

    @Override
    public void resume() {
        // Se puede implementar si es necesario

    }

    @Override
    public void hide() {
        // Se puede implementar si es necesario
    }

    @Override
    public void dispose() {
        if (stage != null) {
            stage.dispose();
            stage = null; // Asegúrate de establecer la referencia a null después de liberar recursos
        }
    }
}
