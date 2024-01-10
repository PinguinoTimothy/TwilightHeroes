package com.twilightheroes.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.twilightheroes.game.screens.MainScreen;

public class TwilightHeroes extends Game {

	public static final int V_WIDTH = 400;
	public static final int V_HEIGHT = 208;
	public  static  final float PPM = 100;

	public static final short PLAYER_BIT = 2;
	public static final short HITBOX_BIT = 4;

	public static final short SOLID_BIT = 8;
	public static final short ENEMY_BIT = 16;

	public static final short EXIT_BIT = 32;
	public static final short DODGING_BIT = 64;

	public SpriteBatch batch;

	private Skin touchpadSkin;
	private Touchpad.TouchpadStyle touchpadStyle;
	private Touchpad touchpad;

	private OrthographicCamera camara2d;

	private Stage stage;
	private Sprite grafico;
	private float velocidade = 5;
	private World world;

	@Override
	public void create () {
		batch = new SpriteBatch();
		world = new World(new Vector2(0,0f),true);
		setScreen(new MainScreen(this));

	}


	@Override
	public void render() {
		super.render();

	}


}
