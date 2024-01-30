
    package com.twilightheroes.game.screens;

    import com.badlogic.gdx.Gdx;
    import com.badlogic.gdx.Screen;
    import com.badlogic.gdx.graphics.GL20;
    import com.badlogic.gdx.graphics.g2d.BitmapFont;
    import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
    import com.badlogic.gdx.scenes.scene2d.Actor;
    import com.badlogic.gdx.scenes.scene2d.InputEvent;
    import com.badlogic.gdx.scenes.scene2d.Stage;
    import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
    import com.badlogic.gdx.scenes.scene2d.ui.Image;
    import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
    import com.badlogic.gdx.scenes.scene2d.ui.Label;
    import com.badlogic.gdx.scenes.scene2d.ui.List;
    import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
    import com.badlogic.gdx.scenes.scene2d.ui.Skin;
    import com.badlogic.gdx.scenes.scene2d.ui.Slider;
    import com.badlogic.gdx.scenes.scene2d.ui.Stack;
    import com.badlogic.gdx.scenes.scene2d.ui.Table;
    import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
    import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
    import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
    import com.badlogic.gdx.utils.Align;
    import com.badlogic.gdx.utils.Scaling;
    import com.badlogic.gdx.utils.viewport.ExtendViewport;
    import com.badlogic.gdx.utils.viewport.FillViewport;
    import com.badlogic.gdx.utils.viewport.FitViewport;
    import com.badlogic.gdx.utils.viewport.ScreenViewport;
    import com.badlogic.gdx.utils.viewport.StretchViewport;
    import com.twilightheroes.game.TwilightHeroes;

    public class OptionScreen implements Screen {

        private Skin skin;
        private Stage stage;
        private TwilightHeroes parent;

        public Slider sliderVolume;

        CheckBox chkAccelerometer;
        CheckBox chkVibrator;
        SelectBox<String> languageSelectBox;

        public Label name;
        public Label description;

        public OptionScreen(TwilightHeroes twilightHeroes) {
            parent = twilightHeroes;
        }

        @Override
        public void show() {
            stage = new Stage(new ExtendViewport(720,480));
            skin = new Skin(Gdx.files.internal("skin.json"));
            Gdx.input.setInputProcessor(stage);

            Table table = new Table();
            table.setFillParent(true);

            Stack stack = new Stack();

            Image image = new Image(skin, "background");
            image.setScaling(Scaling.fill);
            stack.addActor(image);

            Table mainTable = new Table();
            mainTable.setDebug(true);
            mainTable.setFillParent(true);
            mainTable.top().padTop(20.0f);

            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("KarmaFuture.ttf"));
            final FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameter.size = 45;
            BitmapFont font12 = generator.generateFont(parameter);
            parameter.size = 30;
            BitmapFont font123 = generator.generateFont(parameter);
            parameter.size = 15;
            BitmapFont font1234 = generator.generateFont(parameter);

            generator.dispose();
            skin = new Skin();
            skin.add("font", font12);

            TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
            textButtonStyle.font = font12;

            // Alinea los botones "Hechizos" y "Opciones" en la parte superior y a los lados
            TextButton btnMenuHechizos = createTextButton("Hechizos", textButtonStyle);
            btnMenuHechizos.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    parent.changeScreen(TwilightHeroes.MAGIC);
                }
            });

            TextButton btnMenuOpciones = createTextButton("Opciones", textButtonStyle);
            btnMenuOpciones.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    parent.changeScreen(TwilightHeroes.APPLICATION);
                }
            });

            mainTable.add(btnMenuHechizos).padTop(20.0f).align(Align.left).padLeft(20.0f);
            mainTable.add().expandX(); // Espacio en blanco para centrar entre "Hechizos" y "Opciones"
            mainTable.add(btnMenuOpciones).padTop(20.0f).align(Align.right).padRight(20.0f).row();


            // Crear widgets para configuraciones de opciones
            Label.LabelStyle labelStyle = new Label.LabelStyle();
            labelStyle.font = font123;
            Label lblVolume = new Label("Volumen:", labelStyle);

            skin = parent.assMan.manager.get("hud/neonui/neon-ui.json", Skin.class);
            sliderVolume = new Slider(0.0f, 1.0f, 0.1f, false, skin);
            sliderVolume.getStyle().background.setMinHeight(50f);
            sliderVolume.getStyle().knob.setMinHeight(25f);


            chkAccelerometer = new CheckBox("Desactivar Acelerómetro", skin);
            chkAccelerometer.getStyle().font = font123;

            chkVibrator = new CheckBox("Desactivar Vibrador", skin);
            chkVibrator.getStyle().font = font123;

            Label lblLanguage = new Label("Idioma:", labelStyle);

            languageSelectBox = new SelectBox<>(skin);
            languageSelectBox.getStyle().font = font1234;

            languageSelectBox.setItems("Espanol","English");


            // Agregar listeners para manejar cambios en las configuraciones
            sliderVolume.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    float volume = sliderVolume.getValue();
                    // Implementa la lógica para ajustar el volumen
                }
            });

            chkAccelerometer.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    boolean accelerometerEnabled = !chkAccelerometer.isChecked();
                    // Implementa la lógica para activar/desactivar el acelerómetro
                }
            });

            chkVibrator.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    boolean vibratorEnabled = !chkVibrator.isChecked();
                    // Implementa la lógica para activar/desactivar el vibrador
                }
            });

            languageSelectBox.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    String selectedLanguage = languageSelectBox.getSelected();
                    // Implementa la lógica para cambiar el idioma
                }
            });

            // Ajustar el tamaño de los botones y controles según sea necesario
            mainTable.add(lblVolume).padLeft(150f).padBottom(20.0f).colspan(2).center().row();
            mainTable.add(sliderVolume).padLeft(150f).padBottom(20.0f).colspan(2).center().row();
            mainTable.add(lblLanguage).padLeft(150f).padBottom(20.0f).colspan(2).center();
            mainTable.add(languageSelectBox).padBottom(20.0f).colspan(2).center().row();
            mainTable.add(chkAccelerometer).padLeft(150f).padBottom(20.0f).colspan(2).center().row();
            mainTable.add(chkVibrator).padLeft(150f).padBottom(20.0f).colspan(2).center().row();




            stack.add(mainTable);
            table.add(stack).grow();
            stage.addActor(table);
        }



        // Helper method to create TextButton
        private TextButton createTextButton(String text, TextButton.TextButtonStyle style) {
            TextButton textButton = new TextButton(text, style);
            textButton.setName(text.toLowerCase()); // Set the name to lowercase for consistency
            return textButton;
        }

        // Helper method to create ImageButton
        private ImageButton createImageButton(String imageName) {
            Skin skin2 = new Skin();
            skin2.add(imageName, parent.assMan.manager.get("hud/" + imageName + ".png"));
            skin2.add(imageName + "checked", parent.assMan.manager.get("hud/" + imageName + "checked.png"));

            ImageButton.ImageButtonStyle imageButtonStyle = new ImageButton.ImageButtonStyle();
            imageButtonStyle.up = skin2.getDrawable(imageName);
            imageButtonStyle.checked = skin2.getDrawable(imageName + "checked");

            return new ImageButton(imageButtonStyle);
        }

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
            Gdx.input.setInputProcessor(stage);

        }

        @Override
        public void pause() {
            // Se puede implementar si es necesario
        }

        @Override
        public void resume() {
            // Se puede implementar si es necesario
            Gdx.input.setInputProcessor(stage);

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
