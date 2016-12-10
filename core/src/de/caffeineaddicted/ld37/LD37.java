package de.caffeineaddicted.ld37;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.LocalFileHandleResolver;
import de.caffeineaddicted.ld37.actor.Map;
import de.caffeineaddicted.ld37.actor.MapLoader;
import de.caffeineaddicted.ld37.actor.MapWrapper;
import de.caffeineaddicted.ld37.screen.GameScreen;
import de.caffeineaddicted.ld37.utils.Assets;
import de.caffeineaddicted.sgl.ApplicationConfiguration;
import de.caffeineaddicted.sgl.AttributeList;
import de.caffeineaddicted.sgl.SGL;
import de.caffeineaddicted.sgl.SGLGame;
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
    }

    @Override
    protected void initScreens() {
        GameScreen screen = new GameScreen();
        supply(GameScreen.class, screen);
        provide(SGLRootScreen.class).loadScreen(screen);

    }

    @Override
    protected void startGame() {
        provide(SGLAssets.class).setup();
        provide(SGLAssets.class).setLoader(MapWrapper.class,
                new MapLoader(new InternalFileHandleResolver())
            );

        provide(SGLAssets.class).preload();
        provide(SGLAssets.class).load();
        provide(SGLAssets.class).finishLoading();
        provide(SGLRootScreen.class).showScreen(GameScreen.class, SGLRootScreen.ZINDEX.NEAR);
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
