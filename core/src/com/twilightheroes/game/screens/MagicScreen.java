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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.twilightheroes.game.TwilightHeroes;
import com.twilightheroes.game.ecs.components.spells.Spell;
import com.twilightheroes.game.ecs.components.spells.SpellComponent;
import com.twilightheroes.game.ecs.components.spells.SpellList;
import com.twilightheroes.game.ecs.components.spells.SpellVFX;
import com.twilightheroes.game.tools.Mappers;
import com.twilightheroes.game.tools.WidgetContainer;

public class MagicScreen implements Screen {

    private final Stage stage;
    private final TwilightHeroes parent;
    private final ImageButton btnSpell1;
    private final ImageButton btnSpell2;
    private final Array<ImageButton> btnSpells = new Array<>();
    public ImageButton selectedSlot;
    public ImageButton lastSelected;
    public Label name;
    public Label description;

    public MagicScreen(TwilightHeroes twilightHeroes) {
        parent = twilightHeroes;
        stage = new Stage(new StretchViewport(1920, 1080));
        Skin skin = new Skin(Gdx.files.internal("skin.json"));
        JsonValue language = parent.jsonMultilanguage.get("magic");


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
        TextButton btnMenuHechizos = createTextButton(language.get("spells").asString(), textButtonStyle);
        btnMenuHechizos.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                parent.changeScreen(TwilightHeroes.APPLICATION);
            }
        });
        btnMenuHechizos.setName("spells");
        WidgetContainer widgetContainer = new WidgetContainer();
        widgetContainer.widgets.add(btnMenuHechizos);

        TextButton btnMenuOpciones = createTextButton(language.get("options").asString(), textButtonStyle);
        btnMenuOpciones.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                parent.changeScreen(TwilightHeroes.OPTIONS);
            }
        });
        btnMenuOpciones.setName("options");
        widgetContainer.widgets.add(btnMenuOpciones);

        mainTable.add(btnMenuHechizos).padBottom(50.0f).align(Align.left).padLeft(100f);
        mainTable.add(btnMenuOpciones).padBottom(50.0f).align(Align.right).padRight(100f).row();

        // Adding two buttons in the middle
        Skin skin3 = new Skin();
        skin3.add("attack", parent.assMan.manager.get("hud/attack.png"));
        skin3.add("dodge", parent.assMan.manager.get("hud/dodge.png"));

        ImageButton.ImageButtonStyle imageButtonStyle = new ImageButton.ImageButtonStyle();
        imageButtonStyle.up = skin3.getDrawable("attack");
        imageButtonStyle.checked = skin3.getDrawable("dodge");

        btnSpell1 = new ImageButton(imageButtonStyle);
        btnSpell1.setName("Button1");
        btnSpell1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                changeSelectedSlot(btnSpell1);
            }
        });

        btnSpell2 = new ImageButton(imageButtonStyle);
        btnSpell2.setName("Button2");
        btnSpell2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                changeSelectedSlot(btnSpell2);
            }
        });

        // Create a Table for buttons on the top
        Table topButtonTable = new Table();
        topButtonTable.add(btnSpell1).center().padRight(100.0f).size(250);
        topButtonTable.add(btnSpell2).center().size(250).row();

        // Add the topButtonTable to mainTable
        mainTable.add(topButtonTable).colspan(2).center().padBottom(20.0f).row();


        // Adding a scrollable table with 50 buttons below
        Table buttonTable = new Table();
        buttonTable.setDebug(true);


        JsonValue json = new JsonReader().parse(Gdx.files.internal("config/spells.json"));

        for (int j = 0; j < json.size; j++) {
            final ImageButton aux = createImageButton(json.get(j).get("spellId").asString());
            aux.setName(json.get(j).get("spellId").asString());
            buttonTable.add(aux).padBottom(15.0f).size(150f).padLeft(30f).padBottom(30f);
            btnSpells.add(aux);
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
        Label nameLabel = new Label(language.get("name").asString(), labelStyle);
        name = new Label("", labelStyle);
        nameLabel.setName("name");
        widgetContainer.widgets.add(nameLabel);

        Label descriptionLabel = new Label(language.get("description").asString(), labelStyle);
        description = new Label("", labelStyle);
        descriptionLabel.setName("description");
        widgetContainer.widgets.add(descriptionLabel);


        rightTable.add(nameLabel).align(Align.left).padLeft(20.0f).padBottom(20.0f).row();
        rightTable.add(name).align(Align.left).padLeft(20.0f).padBottom(20.0f).row();

        rightTable.add(descriptionLabel).align(Align.left).padLeft(20.0f).padBottom(20.0f).row();
        rightTable.add(description).align(Align.left).padLeft(20.0f).padBottom(20.0f).row();

        // Add the rightTable to mainTable
        mainTable.add(rightTable).padLeft(20.0f).align(Align.top);

        stack.addActor(mainTable);

        table.add(stack).grow();
        stage.addActor(table);

        widgetContainer.nameScreen = "magic";
        parent.widgets.add(widgetContainer);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        for (ImageButton btn : btnSpells) {
            if (btn.getName().equals(parent.playerSettings.spell1)) {
                changeSelectedSlot(btnSpell1);
                changeSelectedSpell(btn);
            }
            if (btn.getName().equals(parent.playerSettings.spell2)) {
                changeSelectedSlot(btnSpell2);
                changeSelectedSpell(btn);
            }

        }

    }

    private void changeSelectedSlot(ImageButton selectedSlot) {
        if (this.selectedSlot != null) {
            this.selectedSlot.setChecked(false);
        }
        this.selectedSlot = selectedSlot;
        selectedSlot.setChecked(true);

    }

    private void changeSelectedSpell(ImageButton selectedSpell) {
        JsonValue json = new JsonReader().parse(Gdx.files.internal("config/spells.json"));
        JsonValue jsonSpell = json.get(selectedSpell.getName());

        if (lastSelected != null) {
            lastSelected.setChecked(false);
        }
        lastSelected = selectedSpell;
        if (selectedSlot != null) {
            if (selectedSlot.getStyle() != selectedSpell.getStyle()) {
                selectedSlot.setStyle(selectedSpell.getStyle());
                selectedSlot.setChecked(false);
            }

        }
        name.setText(jsonSpell.get("spellName" + parent.language).asString());
        description.setText(jsonSpell.get("description" + parent.language).asString());


        SpellComponent spellComponent = Mappers.spellCom.get(parent.mainScreen.playerEntity);


        if (selectedSlot.getName().equals("Button1")) {
            spellComponent.spell1 = new Spell(SpellList.spells.valueOf(jsonSpell.get("spellId").asString()).ordinal(), jsonSpell.get("manaCost").asInt(), jsonSpell.get("castingTime").asFloat(), new SpellVFX(9, 9));
            parent.playerSettings.spell1 = jsonSpell.get("spellId").asString();
        } else {
            spellComponent.spell2 = new Spell(SpellList.spells.valueOf(jsonSpell.get("spellId").asString()).ordinal(), jsonSpell.get("manaCost").asInt(), jsonSpell.get("castingTime").asFloat(), new SpellVFX(9, 9));
            parent.playerSettings.spell2 = jsonSpell.get("spellId").asString();

        }

        parent.mainScreen.actualizarBotones();
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
        stage.getViewport().update(width, height, true);

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
        // Se puede implementar si es necesario
    }
}
