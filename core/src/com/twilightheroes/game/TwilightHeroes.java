package com.twilightheroes.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.twilightheroes.game.screens.BestiaryScreen;
import com.twilightheroes.game.screens.CreditScreen;
import com.twilightheroes.game.screens.EndScreen;
import com.twilightheroes.game.screens.HelpScreen;
import com.twilightheroes.game.screens.MagicScreen;
import com.twilightheroes.game.screens.MainScreen;
import com.twilightheroes.game.screens.OptionScreen;
import com.twilightheroes.game.screens.WinScreen;
import com.twilightheroes.game.tools.B2AssetManager;
import com.twilightheroes.game.tools.KillCounter;
import com.twilightheroes.game.tools.PlayerSettings;
import com.twilightheroes.game.tools.WidgetContainer;

/**
 * Clase principal que extiende la clase Game de LibGDX.
 * Controla la logica del juego y la gestion de pantallas.
 */
public class TwilightHeroes extends Game {

    /**
     * The constant V_WIDTH.
     */
    public static final int V_WIDTH = 400;
    /**
     * The constant V_HEIGHT.
     */
    public static final int V_HEIGHT = 208;
    /**
     * The constant PPM.
     */
    public static final float PPM = 100;
    /**
     * The constant PLAYER_BIT.
     */
    public static final short PLAYER_BIT = 2;
    /**
     * The constant HITBOX_BIT.
     */
    public static final short HITBOX_BIT = 4;
    /**
     * The constant SOLID_BIT.
     */
    public static final short SOLID_BIT = 8;
    /**
     * The constant ENEMY_BIT.
     */
    public static final short ENEMY_BIT = 16;
    /**
     * The constant EXIT_BIT.
     */
    public static final short EXIT_BIT = 32;
    /**
     * The constant INMUNE_BIT.
     */
    public static final short INMUNE_BIT = 64;
    /**
     * The constant BULLET_BIT.
     */
    public static final short BULLET_BIT = 128;
    /**
     * The constant INTERACTIVE_BIT.
     */
    public static final short INTERACTIVE_BIT = 256;
    /**
     * The constant MENU.
     */
    public final static int MENU = 0;
    /**
     * The constant OPTIONS.
     */
    public final static int OPTIONS = 1;
    /**
     * The constant APPLICATION.
     */
    public final static int APPLICATION = 2;
    /**
     * The constant MAGIC.
     */
    public final static int MAGIC = 3;
    /**
     * The constant ENDGAME.
     */
    public final static int ENDGAME = 4;
    /**
     * The constant BESTIARY.
     */
    public final static int BESTIARY = 5;
    /**
     * The constant WIN.
     */
    public final static int WIN = 6;
    /**
     * The constant CREDITS.
     */
    public final static int CREDITS = 7;
    /**
     * The constant HELP.
     */
    public final static int HELP = 8;


    /**
     * The Main screen.
     */
    public MainScreen mainScreen;
    /**
     * The Player settings.
     */
    public PlayerSettings playerSettings = new PlayerSettings();
    /**
     * The Ass man.
     */
    public B2AssetManager assMan = new B2AssetManager();
    /**
     * The Accelerometer on.
     */
    public boolean accelerometerOn;
    /**
     * The Vibrator on.
     */
    public boolean vibratorOn;
    /**
     * The Music volume.
     */
    public float musicVolume;
    /**
     * The Language.
     */
    public languages language;
    /**
     * The In game.
     */
    public boolean inGame = false;
    /**
     * The Previous screen.
     */
    public int previousScreen;
    /**
     * The Json multilanguage.
     */
    public JsonValue jsonMultilanguage;
    /**
     * The Widgets.
     */
    public Array<WidgetContainer> widgets = new Array<>();
    /**
     * The Prefs.
     */
    Preferences prefs;

    private com.twilightheroes.game.screens.MenuScreen menuScreen;
    private MagicScreen magicScreen;
    private EndScreen endScreen;
    private OptionScreen optionScreen;
    private BestiaryScreen bestiaryScreen;
    private WinScreen winScreen;
    private CreditScreen creditScreen;
private HelpScreen helpScreen;

    /**
     * Carga los assets, las preferencias e inicia el menu
     */
    @Override
    public void create() {

        assMan.loadImages();
        assMan.manager.finishLoading();


        prefs = Gdx.app.getPreferences("Preferences");
        accelerometerOn = prefs.getBoolean("accelerometerOn", true);
        vibratorOn = prefs.getBoolean("vibratorOn", true);
        musicVolume = prefs.getFloat("musicVolume", 100);
        language = languages.values()[prefs.getInteger("language", 0)];
        playerSettings.spell1 = prefs.getString("spell1", "frostSpear");
        playerSettings.spell2 = prefs.getString("spell2", "healingSigil");
        if (playerSettings.spell2.equals("earthSpike")) playerSettings.spell2 = "healingSigil";
        playerSettings.level = prefs.getInteger("level", 0);
        playerSettings.lastObelisk = prefs.getInteger("lastObelisk", 0);
        playerSettings.money = prefs.getInteger("money", 0);
        for (int i = 0; i < playerSettings.doorsOpened.length; i++) {
            playerSettings.doorsOpened[i] = prefs.getBoolean("door_"+i,false);
        }

        jsonMultilanguage = new JsonReader().parse(Gdx.files.internal("config/language.json")).get(language.name());
        killCounterHandler(true);

        changeScreen(MENU);

    }

    /**
     * Se encarga de leer/escribir el contador de enemigos muertos para la screen Bestiario
     *
     * @param read Decide si va a leer (True) o escribir (False)
     */
    public void killCounterHandler(boolean read) {
        FileHandle fileHandle = Gdx.files.local("config/killCounter.json");
        if (read) {


            if (fileHandle.exists()) {
                JsonValue jsonKillCounter = new JsonReader().parse(fileHandle);

                if (jsonKillCounter != null) {
                    for (int i = 0; i < jsonKillCounter.size; i++) {
                        KillCounter killCounter = new KillCounter();
                        killCounter.enemyName = jsonKillCounter.get(i).getString("enemyName");
                        killCounter.killCount = jsonKillCounter.get(i).getInt("killCount");
                        playerSettings.killCounter.add(killCounter);

                    }
                }
            }
        } else {


            Json json = new Json();
            json.setOutputType(JsonWriter.OutputType.json);

            String txt = json.toJson(playerSettings.killCounter);

            fileHandle.writeString(json.prettyPrint(txt), false);

        }
    }

    /**
     * Se encarga de cambiar el idioma
     */
    public void updateLanguage() {
        jsonMultilanguage = new JsonReader().parse(Gdx.files.internal("config/language.json")).get(language.name());
        for (WidgetContainer screen : widgets) {

            for (Actor act : screen.widgets) {
                if (act instanceof TextButton) {
                    ((TextButton) act).setText(jsonMultilanguage.get(screen.nameScreen).get(act.getName()).asString());
                } else if (act instanceof Label) {
                    ((Label) act).setText(jsonMultilanguage.get(screen.nameScreen).get(act.getName()).asString());
                }

            }

        }

    }

    /**
     * Se encarga de cambiar la screen actual
     *
     * @param screen La screen a la que va a cambiar
     */
    public void changeScreen(int screen) {

        switch (screen) {

            case MENU:
                if (menuScreen == null)
                    menuScreen = new com.twilightheroes.game.screens.MenuScreen(this);
                previousScreen = MENU;
                inGame = false;
                this.setScreen(menuScreen);
                break;


            case OPTIONS:
                if (optionScreen == null) optionScreen = new OptionScreen(this);
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
                if (magicScreen == null) magicScreen = new MagicScreen(this);

                this.setScreen(magicScreen);
                break;
            case ENDGAME:
                if (endScreen == null) endScreen = new EndScreen(this);
                inGame = false;
                previousScreen = ENDGAME;
                this.setScreen(endScreen);
                break;
            case BESTIARY:
                if (bestiaryScreen == null) bestiaryScreen = new BestiaryScreen(this);
                this.setScreen(bestiaryScreen);
                break;
            case WIN:
                if (winScreen == null) winScreen = new WinScreen(this);
                this.setScreen(winScreen);
                break;
            case CREDITS:
                if (creditScreen == null) creditScreen = new CreditScreen(this);
                this.setScreen(creditScreen);
                break;
            case HELP:
                if (helpScreen == null) helpScreen = new HelpScreen(this);
                this.setScreen(helpScreen);
                break;
        }
    }

    /**
     * Reinicia la screen del juego
     */
    public void restart() {
        if (mainScreen != null) {
            mainScreen.dispose();
        }
        mainScreen = new MainScreen(this);
        changeScreen(APPLICATION);
    }

    /**
     * Renderiza la screen
     */
    @Override
    public void render() {
        super.render();

    }

    /**
     * Cuando se pausa el juego guarda las preferencias de usuario, las puertas que se han abierto y el nivel
     */
    @Override
    public void pause() {
        super.pause();
        prefs.putBoolean("accelerometerOn", accelerometerOn);
        prefs.putBoolean("vibratorOn", vibratorOn);
        prefs.putFloat("musicVolume", musicVolume);
        prefs.putInteger("language", language.ordinal());
        prefs.putString("spell1", playerSettings.spell1);
        prefs.putString("spell2", playerSettings.spell2);
        prefs.putInteger("level", playerSettings.level);
        prefs.putInteger("lastObelisk", playerSettings.lastObelisk);
        prefs.putInteger("money", playerSettings.money);
        for (int i = 0; i < playerSettings.doorsOpened.length; i++) {
            prefs.putBoolean("door_"+i,playerSettings.doorsOpened[i]);
        }
        prefs.flush();
        killCounterHandler(false);
    }

    /**
     * Se encarga de hacer dispose a las distintas pantallas y al asset manager
     */
    @Override
    public void dispose() {
        super.dispose();
        mainScreen.dispose();
        optionScreen.dispose();
        magicScreen.dispose();
        menuScreen.dispose();

        assMan.manager.dispose();
    }

    /**
     * Enumerado con los idiomas
     */
    public enum languages {
        /**
         * Es languages.
         */
        ES,
        /**
         * En languages.
         */
        EN
    }


}
