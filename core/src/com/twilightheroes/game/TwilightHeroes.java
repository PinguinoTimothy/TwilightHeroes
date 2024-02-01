package com.twilightheroes.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.twilightheroes.game.screens.EndScreen;
import com.twilightheroes.game.screens.MagicScreen;
import com.twilightheroes.game.screens.MainScreen;
import com.twilightheroes.game.screens.OptionScreen;
import com.twilightheroes.game.tools.B2AssetManager;

public class TwilightHeroes extends Game {

	public enum languages{
		ES,
		EN
	}
	public static final int V_WIDTH = 400;
	public static final int V_HEIGHT = 208;
	public  static  final float PPM = 100;

	public static final short PLAYER_BIT = 2;
	public static final short HITBOX_BIT = 4;

	public static final short SOLID_BIT = 8;
	public static final short ENEMY_BIT = 16;

	public static final short EXIT_BIT = 32;
	public static final short INMUNE_BIT = 64;
	public static final short BULLET_BIT = 128;


	public ShapeRenderer shapeRenderer;


	private Skin touchpadSkin;
	private Touchpad.TouchpadStyle touchpadStyle;
	private Touchpad touchpad;

	private OrthographicCamera camara2d;

	private Stage stage;
	private Sprite grafico;
	private float velocidade = 5;
	private World world;

	public final static int MENU = 0;
	public final static int OPTIONS = 1;
	public final static int APPLICATION = 2;
	public final static int MAGIC = 3;
	public final static int ENDGAME = 4;

	public MainScreen mainScreen;
	private com.twilightheroes.game.screens.MenuScreen menuScreen;
	private MagicScreen magicScreen;
	private EndScreen endScreen;
	private OptionScreen optionScreen;
	public B2AssetManager assMan = new B2AssetManager();


	Preferences prefs;

	public boolean accelerometerOn;
	public boolean vibratorOn ;
	public float musicVolume ;
	public languages language ;


	public boolean inGame = false;

	public int previousScreen;




	@Override
	public void create () {

		world = new World(new Vector2(0,0f),true);
		assMan.loadImages();
		assMan.manager.finishLoading();

try {
	prefs = Gdx.app.getPreferences("Preferences");
	accelerometerOn = prefs.getBoolean("accelerometerOn",true);
	vibratorOn = prefs.getBoolean("vibratorOn",true);
	musicVolume = prefs.getFloat("musicVolume",100);
	language = languages.values()[prefs.getInteger("language",0)];
}catch (NullPointerException ex){}

		mainScreen = new MainScreen(this);

		changeScreen(MENU);

	}

	public void changeScreen(int screen){
		switch(screen){

			case MENU:
				if(menuScreen == null) menuScreen = new com.twilightheroes.game.screens.MenuScreen(this);
				previousScreen = MENU;
				inGame = false;
				this.setScreen(menuScreen);
				break;


			case OPTIONS:
				if(optionScreen == null) optionScreen = new OptionScreen(this);
				this.setScreen(optionScreen);
				break;


			case APPLICATION:
				// always make new game screen so game can't start midway
				mainScreen.resume();
				inGame = true;
				previousScreen = APPLICATION;
				this.setScreen(mainScreen);
				break;
			case MAGIC:
				// always make new game screen so game can't start midway
				if(magicScreen == null) magicScreen = new MagicScreen(this);

				this.setScreen(magicScreen);
				break;
			case ENDGAME:
				if(endScreen == null) endScreen = new EndScreen(this);
				inGame = false;
				this.setScreen(endScreen);
				break;
		}
	}

	public void restart(){
		mainScreen = new MainScreen(this);
		changeScreen(APPLICATION);
	}


	@Override
	public void render() {
		super.render();

	}

	@Override
	public void pause() {
		super.pause();
		prefs.putBoolean("accelerometerOn", accelerometerOn);
		prefs.putBoolean("vibratorOn", vibratorOn);
		prefs.putFloat("musicVolume",musicVolume);
		prefs.putInteger("language",language.ordinal());

		prefs.flush();
	}

	@Override
	public void dispose() {
		super.dispose();

		assMan.manager.dispose();
	}


}
