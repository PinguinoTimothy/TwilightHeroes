package com.twilightheroes.game.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.twilightheroes.game.TwilightHeroes;
import com.twilightheroes.game.ecs.components.DialogueComponent;
import com.twilightheroes.game.ecs.components.effectComponents.StatusComponent;
import com.twilightheroes.game.screens.MainScreen;
import com.twilightheroes.game.tools.Mappers;

public class DialogueSystem extends IteratingSystem {

    private MainScreen screen;
    private BitmapFont font;
    private boolean touchDown = false;

    private   Label.LabelStyle labelStyle;
    private   Label label;

    public DialogueSystem(MainScreen screen) {
        super(Family.all(DialogueComponent.class).get());
    this.screen = screen;
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Retro Gaming.ttf"));
        final FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = (int)Math.ceil(12);
        parameter.minFilter = Texture.TextureFilter.Nearest;
        parameter.magFilter = Texture.TextureFilter.Nearest;

        generator.scaleForPixelHeight((int)Math.ceil(12));
         font = generator.generateFont(parameter);
         generator.dispose();

      labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.BLACK;
        label = new Label("",labelStyle);

        // Agregar un listener para detectar el toque en la pantalla
        screen.hud.stage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // Marcar que se ha tocado la pantalla

                    touchDown = true;


                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        DialogueComponent dialogueComponent = Mappers.dialogueCom.get(entity);

        // Lógica para mostrar o esconder el cuadro de diálogo según el estado del componente
        if (dialogueComponent.active) {
            if (dialogueComponent.dialogueTexts.isEmpty()) {
                // No hay más textos, ocultar el cuadro de diálogo
                if (touchDown) {
                    hideDialogue(dialogueComponent);
                }
            } else {
                // Verificar si se tocó la pantalla para avanzar al siguiente texto
                if (touchDown) {
                    showDialogue(dialogueComponent);
                    touchDown = false;  // Reiniciar la bandera de toque
                }
            }
        }
    }


    // Método para mostrar el siguiente texto del diálogo
    public void showDialogue(DialogueComponent dialogueComponent) {
        if (!dialogueComponent.dialogueTexts.isEmpty()) {
            String nextText = dialogueComponent.dialogueTexts.removeIndex(0);
            showDialogue(nextText);
        } else {
            hideDialogue(dialogueComponent);
        }
    }

    private Dialog dialog;
    // Método para activar el diálogo
    public void showDialogue(String text) {
        // Obtener la entidad que tiene el componente de diálogo
        // Activar el componente de diálogo con el texto proporcionado
        // Puedes implementar la lógica para obtener la entidad según tus necesidades
        label.setText(text);

        if (dialog == null){

            dialog = new Dialog("", screen.parent.assMan.manager.get("hud/dialogSkin.json", Skin.class));
            dialog.text(label);
            dialog.show(screen.hud.stage).setBounds(0, screen.hud.stage.getHeight(), screen.hud.stage.getWidth(), 75);
        }
    }

    // Método para ocultar el diálogo
    public void hideDialogue(DialogueComponent dialogueComponent) {

        if (dialogueComponent.active) {
            // Si el diálogo está activo, ocúltalo y destrúyelo
            dialogueComponent.active = false;
            if (dialog != null) {
                dialog.hide(); // Oculta el diálogo
                dialog.remove(); // Remueve el diálogo de la Stage
                dialog = null; // Establece la referencia del diálogo a null
            }
        }
    }
}
