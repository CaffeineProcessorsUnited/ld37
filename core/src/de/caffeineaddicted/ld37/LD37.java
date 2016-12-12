package de.caffeineaddicted.ld37;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.*;
import de.caffeineaddicted.ld37.input.GlobalInputProcessor;
import de.caffeineaddicted.ld37.message.MainMenuMessage;
import de.caffeineaddicted.ld37.screen.BackgroundScreen;
import de.caffeineaddicted.ld37.screen.GameScreen;
import de.caffeineaddicted.ld37.screen.MenuScreen;
import de.caffeineaddicted.ld37.utils.Assets;
import de.caffeineaddicted.ld37.utils.MusicPlayer;
import de.caffeineaddicted.sgl.ApplicationConfiguration;
import de.caffeineaddicted.sgl.AttributeList;
import de.caffeineaddicted.sgl.SGL;
import de.caffeineaddicted.sgl.SGLGame;
import de.caffeineaddicted.sgl.input.SGLScreenInputMultiplexer;
import de.caffeineaddicted.sgl.messages.Message;
import de.caffeineaddicted.sgl.messages.MessageReceiver;
import de.caffeineaddicted.sgl.ui.screens.SGLRootScreen;
import de.caffeineaddicted.sgl.utils.SGLAssets;

public class LD37 extends SGLGame {

    private static final ApplicationConfiguration applicationConfiguration =
            new ApplicationConfiguration()
                    .set(AttributeList.WIDTH, 1280)
                    .set(AttributeList.HEIGHT, 720)
                    .set(AttributeList.RESIZABLE, true);
    private boolean paused;

    @Override
    protected void initGame() {
        supply(Viewport.class, new FitViewport(applicationConfiguration.get(AttributeList.WIDTH), applicationConfiguration.get(AttributeList.HEIGHT)));
        //supply(Viewport.class, new ScreenViewport());
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        supply(SGLAssets.class, new Assets());
        supply(InputMultiplexer.class, new InputMultiplexer());
        Gdx.input.setInputProcessor(provide(InputMultiplexer.class));
        provide(InputMultiplexer.class).addProcessor(provide(SGLScreenInputMultiplexer.class));
        provide(InputMultiplexer.class).addProcessor(new GlobalInputProcessor());
        SGL.registerMessageReceiver(MainMenuMessage.class, new MessageReceiver() {
            @Override
            public void receiveMessage(Message message) {
                provide(SGLRootScreen.class).hideScreen(GameScreen.class);
                provide(SGLRootScreen.class).showScreen(MenuScreen.class, SGLRootScreen.ZINDEX.NEAREST);

            }
        });
    }

    @Override
    protected void initScreens() {
        supply(BackgroundScreen.class, new BackgroundScreen());
        supply(GameScreen.class, new GameScreen());
        supply(MenuScreen.class, new MenuScreen());
        supply(ShapeRenderer.class, new ShapeRenderer());
        supply(SpriteBatch.class, new SpriteBatch());
        provide(SGLRootScreen.class).loadScreen(provide(BackgroundScreen.class));
        provide(SGLRootScreen.class).loadScreen(provide(GameScreen.class));
        provide(SGLRootScreen.class).loadScreen(provide(MenuScreen.class));

    }

    @Override
    protected void startGame() {
        provide(SGLAssets.class).preload();
        provide(SGLAssets.class).finishLoading();

        supply(MusicPlayer.class, new MusicPlayer());
        provide(MusicPlayer.class).add("music/song0.mp3");
        provide(MusicPlayer.class).add("music/song1.mp3");
        provide(MusicPlayer.class).add("music/song2.mp3");
        provide(MusicPlayer.class).add("music/song3.mp3");
        provide(MusicPlayer.class).add("music/song4.mp3");
        provide(MusicPlayer.class).add("music/song5.mp3");
        provide(MusicPlayer.class).add("music/song6.mp3");
        provide(MusicPlayer.class).add("music/song7.mp3");
        provide(MusicPlayer.class).add("music/song8.mp3");

        provide(MusicPlayer.class).play();
        provide(MusicPlayer.class).setShuffle(true);

        provide(SGLAssets.class).load();
        provide(SGLAssets.class).finishLoading();

        supply(Skin.class, provide(SGLAssets.class).get("skin/uiskin.json", Skin.class));
        provide(SGLRootScreen.class).showScreen(BackgroundScreen.class, SGLRootScreen.ZINDEX.FAREST);
        provide(SGLRootScreen.class).showScreen(MenuScreen.class, SGLRootScreen.ZINDEX.NEAREST);
    }

    @Override
    public String getAppName() {
        return "LD37";
    }

    @Override
    public ApplicationConfiguration config() {
        return applicationConfiguration;
    }

    @Override
    public void onPause() {
        paused = true;
    }

    @Override
    public void onResume() {
        paused = false;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDispose() {

    }

    @Override
    public void onResize(int width, int height) {
        SGL.debug("resize");
    }

    public static class CONSTANTS {

        public final static String PREFERENCE_NAME = "profile";

        /*
        Preferences keys
         */
        public final static String PREF_KEY_HIGHSCORE = "highscore";
        public final static int PREF_DEF_HIGHSCORE = 0;
        /*
        Bundle keys
         */
        public static final String BUNDLE_SCORE = "score";
        public static final String BUNDLE_HARDCORE = "hardcore";
    }
}
