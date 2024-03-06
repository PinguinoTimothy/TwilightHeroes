package com.twilightheroes.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.twilightheroes.game.TwilightHeroes;


/**
 * The type Help screen.
 */
public class HelpScreen implements Screen {

    /**
     * The Stack.
     */
    final Stack stack = new Stack();
    private final Stage stage;
    private final Table mainTable;
    private final TwilightHeroes parent;
    private final ImageButton backButton;
    private final ImageButton rightArrow;
    private final ImageButton leftArrow;
    private final String[] menus = new String[]{"principales.jpeg", "hechizos.jpeg"};
    private int imgActual = 0;

    /**
     * Instantiates a new Help screen.
     *
     * @param parent the parent
     */
    public HelpScreen(final TwilightHeroes parent) {
        this.parent = parent;
        stage = new Stage(new StretchViewport(1920, 1080)); // Utilizando StretchViewport
        this.mainTable = new Table();


        mainTable.padLeft(400f).padBottom(100f);


        // Configurar la tabla principal y el scroll

        backButton = new ImageButton(new TextureRegionDrawable(parent.assMan.manager.get("hud/backButton.png", Texture.class)));
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                parent.changeScreen(TwilightHeroes.MENU);

            }
        });

        rightArrow = new ImageButton(new TextureRegionDrawable(parent.assMan.manager.get("help/flechaDerecha.png", Texture.class)));
        rightArrow.getImage().setScale(10f);
        rightArrow.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (imgActual < menus.length - 1) {
                    imgActual++;
                    cambiarImagen();
                }

            }
        });

        leftArrow = new ImageButton(new TextureRegionDrawable(parent.assMan.manager.get("help/flechaIzquierda.png", Texture.class)));
        leftArrow.getImage().setScale(10f);
        leftArrow.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (imgActual > 0) {

                    imgActual--;
                    cambiarImagen();
                }

            }
        });


        backButton.setBounds(50, stage.getHeight() - 150, 100, 100);
        leftArrow.setBounds(50, stage.getHeight() / 2 - 100, 100, 100);
        rightArrow.setBounds(stage.getWidth() - 400, stage.getHeight() / 2 - 100, 100, 100);
        mainTable.setBounds(100, 0, 100, 100);
        stack.addActor(mainTable);

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


        // Agregar el scroll pane al escenario
        stack.setFillParent(true);
        stage.addActor(stack);
        stage.addActor(backButton);
        stage.addActor(leftArrow);
        stage.addActor(rightArrow);
        cambiarImagen();
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
    private void cambiarImagen() {

        mainTable.clear();
        Texture texture = parent.assMan.manager.get("help/" + menus[imgActual], Texture.class);
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        Image imagen = new Image(texture);
        imagen.setScale(0.7f);

        // Agregar la fila a la tabla principal y configurar el evento de clic
        mainTable.add(imagen);
        stack.addActor(mainTable);
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




