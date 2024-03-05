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
import com.twilightheroes.game.tools.KillCounter;


/**
 * The type Bestiary screen.
 */
public class BestiaryScreen implements Screen {

    private final Stage stage;
    private final ScrollPane scrollPane;
    private final BitmapFont bigFont;
    private final BitmapFont smallFont;


    private final Table mainTable;
    private final Table enemigoInfoTable;
    private final TwilightHeroes parent;
    private final ImageButton backButton;
    /**
     * The Stack.
     */
    Stack stack = new Stack();
    /**
     * The Json.
     */
    JsonValue json = new JsonReader().parse(Gdx.files.internal("config/enemiesES.json"));


    /**
     * Instantiates a new Bestiary screen.
     *
     * @param parent the parent
     */
    public BestiaryScreen(final TwilightHeroes parent) {
        this.parent = parent;
        stage = new Stage(new StretchViewport(1920, 1080)); // Utilizando StretchViewport
        this.mainTable = new Table();
        this.enemigoInfoTable = new Table();
        this.scrollPane = new ScrollPane(mainTable);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("KarmaFuture.ttf"));
        final FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 50;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 2f;
        BitmapFont fontBig = generator.generateFont(parameter);
        parameter.size = 40;
        BitmapFont fontSmall = generator.generateFont(parameter);
        generator.dispose();

        this.bigFont = fontBig;
        this.smallFont = fontSmall;

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

   /** Called when this screen becomes the current screen for the game. */
 @Override
    public void show() {
        stage.clear();
        stack.clear();

        mainTable.clear();
        // Configurar la apariencia de la tabla principal (opcional)
        Image image = new Image(parent.assMan.manager.get("backgrounds/book.png", Texture.class));
        image.setScaling(Scaling.fillY);
        stack.addActor(image);


        // Agregar la textura del sprite del enemigo y la cantidad de veces que se ha matado
        for (KillCounter kc : parent.playerSettings.killCounter) {
            agregarFila(kc.enemyName, kc.killCount);
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

    /** Called when the screen should render itself.
	 * @param delta The time in seconds since the last render. */
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
    private void agregarFila(String nombre, int cantidad) {
        JsonValue jsonEnemy = json.get(nombre);
        String name = jsonEnemy.getString("name");


        Texture texture = parent.assMan.manager.get("enemies/enemySample/" + nombre + ".png", Texture.class);
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        Image imagen = new Image(texture);

        Label labelNombre = new Label(name, new Label.LabelStyle(bigFont, Color.WHITE));

        // Agregar la fila a la tabla principal y configurar el evento de clic
        mainTable.add(imagen).size(250f, 250f).padBottom(50);
        mainTable.add(labelNombre);
        mainTable.row();
        agregarListener(imagen, nombre, cantidad);
    }

    // Método para agregar un listener de clic a una imagen (enemigo)
    private void agregarListener(final Image imagen, final String nombre, final int cantidad) {
        imagen.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Limpiar la tabla de información del enemigo
                enemigoInfoTable.clear();

                JsonValue jsonEnemy = json.get(nombre);
                String name = jsonEnemy.getString("name");
                String description = jsonEnemy.getString("description");

                // Agregar nueva información detallada del enemigo seleccionado
                Label infoLabel = new Label(name, new Label.LabelStyle(smallFont, Color.WHITE));
                Label cantidadLabel = new Label(cantidad + " killed", new Label.LabelStyle(smallFont, Color.WHITE));

                // Agregar la imagen grande del enemigo
                Texture texture = parent.assMan.manager.get("enemies/enemySample/" + nombre + ".png", Texture.class);
                texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
                Image imagen = new Image(texture);
                // Agregar descripción (personaliza según tus necesidades)
                Label descripcionLabel = new Label(description, new Label.LabelStyle(smallFont, Color.WHITE));

                // Agregar labels a la tabla de información del enemigo
                enemigoInfoTable.add(infoLabel).padBottom(10);
                enemigoInfoTable.row();
                enemigoInfoTable.add(imagen).padBottom(10).size(250f);
                enemigoInfoTable.row();
                enemigoInfoTable.add(descripcionLabel).padBottom(10);
                enemigoInfoTable.row();
                enemigoInfoTable.add(cantidadLabel).padBottom(10);

                // Configurar la posición y tamaño de la tabla de información del enemigo
                enemigoInfoTable.setBounds(stage.getWidth() / 2f - 50, 0, stage.getWidth() / 2f, stage.getHeight());

                // Añadir la tabla de información del enemigo al escenario
                stage.addActor(enemigoInfoTable);
            }
        });
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




