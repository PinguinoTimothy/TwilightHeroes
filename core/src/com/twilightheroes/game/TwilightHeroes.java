package com.twilightheroes.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.twilightheroes.game.screens.EndScreen;
import com.twilightheroes.game.screens.MainScreen;
import com.twilightheroes.game.tools.B2AssetManager;
import com.twilightheroes.game.tools.B2WorldCreator;

public class TwilightHeroes extends Game {

	public static final int V_WIDTH = 400;
	public static final int V_HEIGHT = 208;
	public  static  final float PPM = 100;

	public static final short PLAYER_BIT = 2;
	public static final short HITBOX_BIT = 4;

	public static final short SOLID_BIT = 8;
	public static final short ENEMY_BIT = 16;

	public static final short EXIT_BIT = 32;
	public static final short INMUNE_BIT = 64;

	public SpriteBatch batch;

	private Skin touchpadSkin;
	private Touchpad.TouchpadStyle touchpadStyle;
	private Touchpad touchpad;

	private OrthographicCamera camara2d;

	private Stage stage;
	private Sprite grafico;
	private float velocidade = 5;
	private World world;

	public final static int MENU = 0;
	public final static int PREFERENCES = 1;
	public final static int APPLICATION = 2;
	public final static int ENDGAME = 3;
	private MainScreen mainScreen;
	private EndScreen endScreen;
	public B2AssetManager assMan = new B2AssetManager();

	@Override
	public void create () {
		batch = new SpriteBatch();
		world = new World(new Vector2(0,0f),true);
		assMan.loadImages();
		assMan.manager.finishLoading();
		setScreen(new MainScreen(this));

	}

	public void changeScreen(int screen){
		switch(screen){
			/*
			case MENU:
				if(menuScreen == null) menuScreen = new MenuScreen(this);
				this.setScreen(menuScreen);
				break;


			case PREFERENCES:
				if(preferencesScreen == null) preferencesScreen = new PreferencesScreen(this);
				this.setScreen(preferencesScreen);
				break;

				 */
			case APPLICATION:
				// always make new game screen so game can't start midway
				mainScreen = new MainScreen(this);
				this.setScreen(mainScreen);
				break;
			case ENDGAME:
				if(endScreen == null) endScreen = new EndScreen(this);
				this.setScreen(endScreen);
				break;
		}
	}


	@Override
	public void render() {
		super.render();

	}

	@Override
	public void dispose() {
		super.dispose();
		assMan.manager.dispose();
	}
}
