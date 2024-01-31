
        package com.twilightheroes.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.twilightheroes.game.TwilightHeroes;
import com.twilightheroes.game.ecs.components.spells.Spell;
import com.twilightheroes.game.ecs.components.spells.SpellComponent;
import com.twilightheroes.game.tools.Mappers;

        public class MagicScreen implements Screen {

    private Skin skin;
    private Stage stage;
    private TwilightHeroes parent;

    public ImageButton selectedSlot;
    public ImageButton lastSelected;

    public Label name;
    public Label description;

    public MagicScreen(TwilightHeroes twilightHeroes) {
        parent = twilightHeroes;
    }

    @Override
    public void show() {
        stage = new Stage(new StretchViewport(1920,1080));
        skin = new Skin(Gdx.files.internal("skin.json"));
        Gdx.input.setInputProcessor(stage);


        final Table table = new Table();
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
        parameter.size = 75;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 2f;
        BitmapFont fontBig = generator.generateFont(parameter);
        parameter.size = 40;
        BitmapFont fontSmall = generator.generateFont(parameter);
        generator.dispose();
        skin = new Skin();
        skin.add("font", fontBig);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = fontBig;

        // Adding Hechizos and Opciones buttons to the top
        TextButton btnMenuHechizos = createTextButton("Hechizos", textButtonStyle);
        btnMenuHechizos.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
            parent.changeScreen(TwilightHeroes.APPLICATION);
            }
        });

        TextButton btnMenuOpciones = createTextButton("Opciones", textButtonStyle);
        btnMenuOpciones.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                parent.changeScreen(TwilightHeroes.OPTIONS);
            }
        });

        mainTable.add(btnMenuHechizos).padBottom(50.0f).align(Align.left).padLeft(100f);
        mainTable.add(btnMenuOpciones).padBottom(50.0f).align(Align.right).padRight(100f).row();

        // Adding two buttons in the middle
        Skin skin3 = new Skin();
        skin3.add("attack", parent.assMan.manager.get("hud/attack.png"));
        skin3.add("dodge", parent.assMan.manager.get("hud/dodge.png"));

        ImageButton.ImageButtonStyle imageButtonStyle = new ImageButton.ImageButtonStyle();
        imageButtonStyle.up = skin3.getDrawable("attack");
        imageButtonStyle.checked = skin3.getDrawable("dodge");

        final ImageButton btn1 = new ImageButton(imageButtonStyle);
        btn1.setName("Button1");
        btn1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                changeSelectedSlot(btn1);
            }
        });

        final ImageButton btn2 = new ImageButton(imageButtonStyle);
        btn1.setName("Button2");
        btn2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x,float y) {
                super.clicked(event, x, y);
                changeSelectedSlot(btn2);
            }
        });

        // Create a Table for buttons on the top
        Table topButtonTable = new Table();
        topButtonTable.add(btn1).center().padRight(100.0f).size(250);
        topButtonTable.add(btn2).center().size(250).row();

        // Add the topButtonTable to mainTable
        mainTable.add(topButtonTable).colspan(2).center().padBottom(20.0f).row();



        // Adding a scrollable table with 50 buttons below
        Table buttonTable = new Table();
        buttonTable.setDebug(true);



        JsonValue json = new JsonReader().parse(Gdx.files.internal("variety/spells.json"));

            for (int j = 0; j < json.size; j++) {
                final ImageButton aux = createImageButton(json.get(j).get("spellId").asString());
                aux.setName(json.get(j).get("spellId").asString());
                buttonTable.add(aux).padBottom(15.0f).size(150f).padLeft(30f).padBottom(30f);
                final int finalJ = j;
                aux.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        super.clicked(event, x, y);
                    changeSelectedSpell(aux);
                    }
                });
            }
            buttonTable.row();


        ScrollPane scrollPane = new ScrollPane(buttonTable);
        mainTable.add(scrollPane).center().padTop(20.0f).grow();

        // Create a Table for information on the right
        Table rightTable = new Table();
        rightTable.setDebug(true);

        // Add information labels to the rightTable
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = fontSmall;
        Label nameLabel = new Label("Spell Name:", labelStyle);
         name = new Label("", labelStyle);

        Label descriptionLabel = new Label("Spell Description:", labelStyle);
         description = new Label("", labelStyle);

        rightTable.add(nameLabel).align(Align.left).padLeft(20.0f).padBottom(20.0f).row();
        rightTable.add(name).align(Align.left).padLeft(20.0f).padBottom(20.0f).row();

        rightTable.add(descriptionLabel).align(Align.left).padLeft(20.0f).padBottom(20.0f).row();
        rightTable.add(description).align(Align.left).padLeft(20.0f).padBottom(20.0f).row();

        // Add the rightTable to mainTable
        mainTable.add(rightTable).padLeft(20.0f).align(Align.top);

        stack.addActor(mainTable);

        table.add(stack).grow();
        stage.addActor(table);
    }

    private void changeSelectedSlot(ImageButton selectedSlot) {
        if (this.selectedSlot != null) {
            this.selectedSlot.setChecked(false);
        }
        this.selectedSlot = selectedSlot;
        selectedSlot.setChecked(true);

    }

    private void changeSelectedSpell(ImageButton selectedSpell){
        JsonValue json = new JsonReader().parse(Gdx.files.internal("variety/spells.json"));
        JsonValue jsonSpell = json.get(selectedSpell.getName());

        if (lastSelected != null){
            lastSelected.setChecked(false);
        }
        lastSelected = selectedSpell;
        if (selectedSlot != null) {
            selectedSlot.setStyle(selectedSpell.getStyle());
            selectedSlot.setChecked(false);
            selectedSlot = null;

        }
        name.setText(jsonSpell.get("spellName").asString());
        description.setText(jsonSpell.get("description").asString());


        SpellComponent spellComponent = Mappers.spellCom.get(parent.mainScreen.playerEntity);

/*

        if (selectedSpell.getName().equals("Button1")){
            spellComponent.spell1 = new Spell()
        }else{
            spellComponent.spell2
        }

 */
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
        skin2.add(imageName, parent.assMan.manager.get("spells/" + imageName + ".png"));
        skin2.add(imageName + "checked", parent.assMan.manager.get("spells/" + imageName + "Checked.png"));

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
        stage.getViewport().update(width, height, true);

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
        // Se puede implementar si es necesario
    }
}
