package com.twilightheroes.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.twilightheroes.game.screens.AchivmentsScreen;
import com.twilightheroes.game.screens.EndScreen;
import com.twilightheroes.game.screens.MagicScreen;
import com.twilightheroes.game.screens.MainScreen;
import com.twilightheroes.game.screens.OptionScreen;
import com.twilightheroes.game.tools.B2AssetManager;
import com.twilightheroes.game.tools.KillCounter;
import com.twilightheroes.game.tools.PlayerSettings;
import com.twilightheroes.game.tools.WidgetContainer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

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

	public PlayerSettings playerSettings = new PlayerSettings();

	public B2AssetManager assMan = new B2AssetManager();


	Preferences prefs;

	public boolean accelerometerOn;
	public boolean vibratorOn ;
	public float musicVolume ;
	public languages language ;


	public boolean inGame = false;

	public int previousScreen;

	public JsonValue jsonMultilanguage;
	public Array<WidgetContainer> widgets = new Array<>();



	@Override
	public void create () {

		assMan.loadImages();
		assMan.manager.finishLoading();

try {
	prefs = Gdx.app.getPreferences("Preferences");
	accelerometerOn = prefs.getBoolean("accelerometerOn",true);
	vibratorOn = prefs.getBoolean("vibratorOn",true);
	musicVolume = prefs.getFloat("musicVolume",100);
	language = languages.values()[prefs.getInteger("language",0)];
	playerSettings.spell1 = prefs.getString("spell1","frostSpear");
	playerSettings.spell2 = prefs.getString("spell2","healingSigil");
	playerSettings.level = prefs.getInteger("level",0);
	playerSettings.money = prefs.getInteger("money",0);

}catch (NullPointerException ex){}

jsonMultilanguage = new JsonReader().parse(Gdx.files.internal("config/language.json")).get(language.name());
killCounterHandler(true);

		mainScreen = new MainScreen(this);

		changeScreen(MENU);

		AchivmentsScreen achivmentsScreen = new AchivmentsScreen();
		setScreen(achivmentsScreen);
	}

	public void killCounterHandler(boolean read){
		FileHandle fileHandle = Gdx.files.local("config/killCounter.json");
		if (read){



			JsonValue jsonKillCounter = new JsonReader().parse(fileHandle);

			for (int i = 0; i < jsonKillCounter.size; i++) {
				KillCounter killCounter = new KillCounter();
				killCounter.enemyName = jsonKillCounter.get(i).getString("enemyName");
				killCounter.killCount = jsonKillCounter.get(i).getInt("killCount");
				playerSettings.killCounter.add(killCounter);

			}

		}else{


			Json json = new Json();
			json.setOutputType(JsonWriter.OutputType.json);

			String txt = json.toJson(playerSettings.killCounter);

			fileHandle.writeString(json.prettyPrint(txt), false);

		}
	}

	public void updateLanguage(){
		jsonMultilanguage = new JsonReader().parse(Gdx.files.internal("config/language.json")).get(language.name());
			for (WidgetContainer screen: widgets)
			{

				for (Actor act: screen.widgets)
				{
					if (act instanceof TextButton){
						((TextButton)act).setText(jsonMultilanguage.get(screen.nameScreen).get(act.getName()).asString());
					} else if (act instanceof Label) {
						((Label) act).setText(jsonMultilanguage.get(screen.nameScreen).get(act.getName()).asString());
					}

				}

			}

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
		if (mainScreen != null){
			mainScreen.dispose();
		}
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
		prefs.putString("spell1", playerSettings.spell1);
		prefs.putString("spell2", playerSettings.spell2);
		prefs.putInteger("level", playerSettings.level);
		prefs.putInteger("money", playerSettings.money);
		prefs.flush();
		killCounterHandler(false);
	}

	@Override
	public void dispose() {
		super.dispose();
mainScreen.dispose();
optionScreen.dispose();
magicScreen.dispose();
menuScreen.dispose();

		assMan.manager.dispose();
	}


}
