package com.twilightheroes.game.tools;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.twilightheroes.game.screens.MainScreen;

import jdk.tools.jmod.Main;

public class MyInputProcessor extends InputAdapter {

    public float screenSizeX;
    public float screenSizeY;

    public MainScreen screen;

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (screenX < screenSizeX/2){
            screen.crearTouchpad(screenX,screenY);
        }
        return true;
    }

    public void resize(float screenSizeX, float screenSizeY){
        this.screenSizeX =  screenSizeX;
        this.screenSizeY = screenSizeY;
    }

    public MyInputProcessor(float screenSizeX, float screenSizeY, MainScreen context){
        this.screen = context;
        this.screenSizeX = screenSizeX;
        this.screenSizeY = screenSizeY;
    }
}
