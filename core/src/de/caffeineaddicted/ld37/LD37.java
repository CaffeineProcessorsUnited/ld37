package de.caffeineaddicted.ld37;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.LocalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import de.caffeineaddicted.ld37.actor.Map;
import de.caffeineaddicted.ld37.actor.MapLoader;
import de.caffeineaddicted.ld37.actor.MapWrapper;
import de.caffeineaddicted.ld37.input.screeninput;
import de.caffeineaddicted.ld37.screen.GameScreen;
import de.caffeineaddicted.ld37.screen.MenuScreen;
import de.caffeineaddicted.ld37.utils.Assets;
import de.caffeineaddicted.sgl.ApplicationConfiguration;
import de.caffeineaddicted.sgl.AttributeList;
import de.caffeineaddicted.sgl.SGL;
import de.caffeineaddicted.sgl.SGLGame;
import de.caffeineaddicted.sgl.input.SGLScreenInputMultiplexer;
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
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        supply(SGLAssets.class, new Assets());
        supply(InputMultiplexer.class, new InputMultiplexer());
        Gdx.input.setInputProcessor(provide(InputMultiplexer.class));
        provide(InputMultiplexer.class).addProcessor(provide(SGLScreenInputMultiplexer.class));
    }

    @Override
    protected void initScreens() {
        GameScreen screen = new GameScreen();
        supply(GameScreen.class, screen);
        supply(MenuScreen.class, new MenuScreen());
        supply(ShapeRenderer.class, new ShapeRenderer());
        supply(SpriteBatch.class, new SpriteBatch());
        provide(SGLRootScreen.class).loadScreen(screen);
        provide(SGLRootScreen.class).loadScreen(provide(MenuScreen.class));

    }

    @Override
    protected void startGame() {
        provide(SGLAssets.class).preload();
        provide(SGLAssets.class).load();
        provide(SGLAssets.class).finishLoading();
        supply(Skin.class, provide(SGLAssets.class).get("skin/uiskin.json", Skin.class));
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
