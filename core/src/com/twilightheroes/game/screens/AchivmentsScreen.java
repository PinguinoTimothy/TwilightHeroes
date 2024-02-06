package com.twilightheroes.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;


public class AchivmentsScreen implements Screen {
    private Stage stage;
    private Table table;
    private ScrollPane scrollPane;
    private BitmapFont font;

    private int enemigosTipo1;
    private int enemigosTipo2;

        private Table mainTable;
        private Table enemigoInfoTable;



    public AchivmentsScreen() {

            this.stage = new Stage(new ScreenViewport());
            this.mainTable = new Table();
            this.enemigoInfoTable = new Table();
            this.scrollPane = new ScrollPane(mainTable);
            this.font = new BitmapFont();

            // Configurar la tabla principal y el scroll
            mainTable.defaults().pad(10).width(200);
            mainTable.add(new Label("Enemigo", new Label.LabelStyle(font, Color.WHITE)));
            mainTable.add(new Label("Cantidad", new Label.LabelStyle(font, Color.WHITE)));
            mainTable.row();

            // Inicializar el conteo de enemigos
            enemigosTipo1 = 0;
            enemigosTipo2 = 0;
        }

        @Override
        public void show() {
            // Configurar la apariencia de la tabla principal (opcional)
            mainTable.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("background.png")))));

            // Agregar la textura del sprite del enemigo y la cantidad de veces que se ha matado
            agregarFila("skele.png", "Enemigo Tipo 1", enemigosTipo1);
            agregarFila("skele.png", "Enemigo Tipo 2", enemigosTipo2);

            // Configurar el scroll pane
            scrollPane.setFillParent(true);

            // Agregar el scroll pane al escenario
            stage.addActor(scrollPane);

            // Configurar el input para el escenario
            Gdx.input.setInputProcessor(stage);
        }

        @Override
        public void render(float delta) {
            // Limpiar la pantalla
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            // Actualizar y dibujar el escenario
            stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
            stage.draw();

            // Dibujar la información detallada del enemigo seleccionado
            stage.getBatch().begin();
            enemigoInfoTable.draw(stage.getBatch(), 1);
            stage.getBatch().end();
        }

        // Métodos de incremento de conteo de enemigos (igual que en el ejemplo anterior)

    // Método para agregar una fila a la tabla principal con la textura, nombre y cantidad
    private void agregarFila(String textura, final String nombre, final int cantidad) {
        Image imagen = new Image(new Texture(Gdx.files.internal(textura)));
        Label labelNombre = new Label(nombre, new Label.LabelStyle(font, Color.WHITE));
        Label labelCantidad = new Label(String.valueOf(cantidad), new Label.LabelStyle(font, Color.WHITE));

        // Agregar la fila a la tabla principal y configurar el evento de clic
        mainTable.add(imagen);
        mainTable.add(labelNombre);
        mainTable.add(labelCantidad).padRight(10);
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

                // Agregar nueva información detallada del enemigo seleccionado
                Label infoLabel = new Label("Detalles de " + nombre, new Label.LabelStyle(font, Color.WHITE));
                Label cantidadLabel = new Label("Cantidad: " + cantidad, new Label.LabelStyle(font, Color.WHITE));

                // Agregar la imagen grande del enemigo
                Image largeImagen = new Image(new Texture(Gdx.files.internal( "skele.png"))); // Asume que las imágenes grandes tienen el prefijo "large_"

                // Agregar descripción (personaliza según tus necesidades)
                Label descripcionLabel = new Label("Descripción del enemigo:\nEste enemigo es peligroso y...", new Label.LabelStyle(font, Color.WHITE));

                // Agregar labels a la tabla de información del enemigo
                enemigoInfoTable.add(infoLabel).padBottom(10).align(Align.left).colspan(2);
                enemigoInfoTable.row();
                enemigoInfoTable.add(largeImagen).colspan(2).padBottom(10);
                enemigoInfoTable.row();
                enemigoInfoTable.add(descripcionLabel).padBottom(10).align(Align.left).colspan(2);
                enemigoInfoTable.row();
                enemigoInfoTable.add(cantidadLabel).padBottom(10).align(Align.left).colspan(2);

                // Configurar la posición y tamaño de la tabla de información del enemigo
                enemigoInfoTable.setBounds(Gdx.graphics.getWidth() / 2f, 0, Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight());

                // Añadir la tabla de información del enemigo al escenario
                stage.addActor(enemigoInfoTable);
            }
        });
    }





        @Override
    public void resize(int width, int height) {

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
