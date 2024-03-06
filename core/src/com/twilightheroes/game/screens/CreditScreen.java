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
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.twilightheroes.game.TwilightHeroes;


/**
 * The type Credit screen.
 */
public class CreditScreen implements Screen {

    /**
     * The Stack.
     */
    final Stack stack = new Stack();
    /**
     * The Json.
     */
    final JsonValue json = new JsonReader().parse(Gdx.files.internal("config/credits.json"));
    private final Stage stage;
    private final ScrollPane scrollPane;
    private final BitmapFont bigFont;
    private final Table mainTable;
    private final TwilightHeroes parent;
    private final ImageButton backButton;


    /**
     * Instantiates a new Credit screen.
     *
     * @param parent the parent
     */
    public CreditScreen(final TwilightHeroes parent) {
        this.parent = parent;
        stage = new Stage(new StretchViewport(1920, 1080)); // Utilizando StretchViewport
        this.mainTable = new Table();
        this.scrollPane = new ScrollPane(mainTable);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("KarmaFuture.ttf"));
        final FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 50;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 2f;
        BitmapFont fontBig = generator.generateFont(parameter);
        parameter.size = 40;
        generator.dispose();

        this.bigFont = fontBig;

        // Configurar la tabla principal y el scroll
        mainTable.padLeft(200).padTop(50).align(Align.left);

        backButton = new ImageButton(new TextureRegionDrawable(parent.assMan.manager.get("hud/backButton.png", Texture.class)));
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                parent.changeScreen(TwilightHeroes.MENU);

            }
        });

        backButton.setBounds(50, stage.getHeight() - 150, 100, 100);

    }

    /**
     * Called when this screen becomes the current screen for the game.
     */
    @Override
    public void show() {
        stage.clear();
        stack.clear();

        mainTable.clear();
        // Configurar la apariencia de la tabla principal (opcional)
        Image image = new Image(parent.assMan.manager.get("backgrounds/book.png", Texture.class));
        image.setScaling(Scaling.fillY);
        stack.addActor(image);


        String[] tipes = new String[]{"Sprites", "Music"};
        for (String tipe : tipes) {
            agregarFila(tipe);

        }


        // Configurar el scroll pane
        scrollPane.setFillParent(true);

        // Agregar el scroll pane al escenario
        stack.addActor(scrollPane);
        stack.setFillParent(true);
        stage.addActor(stack);
        stage.addActor(backButton);

        // Configurar el input para el escenario
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        // Limpiar la pantalla
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Actualizar y dibujar el escenario
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    // Método para agregar una fila a la tabla principal con la textura, nombre y cantidad
    private void agregarFila(String tipo) {
        JsonValue jsonTipo = json.get(tipo);
        for (int i = 0; i < jsonTipo.size; i++) {
            String name = String.valueOf(jsonTipo.get(i));
            Label labelNombre = new Label(tipo + " : " + name, new Label.LabelStyle(bigFont, Color.WHITE));
            mainTable.add(labelNombre);
            mainTable.row();
        }


    }

    @Override
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

    @Override
    public void dispose() {
        stage.dispose();
    }
}




