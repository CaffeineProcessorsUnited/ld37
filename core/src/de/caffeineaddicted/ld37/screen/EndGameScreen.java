package de.caffeineaddicted.ld37.screen;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import de.caffeineaddicted.ld37.LD37;
import de.caffeineaddicted.sgl.SGL;
import de.caffeineaddicted.sgl.ui.screens.SGLRootScreen;
import de.caffeineaddicted.sgl.ui.screens.SGLStagedScreen;

/**
 * Created by felix on 12.12.16.
 */
public class EndGameScreen extends SGLStagedScreen<LD37> {
    private TextButton backtomenu;
    private Label congrats;

    @Override
    public void onBeauty() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void onDispose() {

    }

    @Override
    public void onCreate() {
        congrats = new Label("Congratulations, you completed all Levels.", SGL.provide(Skin.class));
        congrats.setWidth(600);
        stage().addActor(congrats);

        backtomenu = new TextButton("Back to Main Menu", SGL.provide(Skin.class));
        congrats.setWidth(400);
        congrats.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                SGL.provide(GameScreen.class).loadMap(0);
                SGL.provide(SGLRootScreen.class).hideScreen(EndGameScreen.class);
                SGL.provide(SGLRootScreen.class).showScreen(MenuScreen.class, SGLRootScreen.ZINDEX.NEAR);
            }
        });
        stage().addActor(backtomenu);
    }

    @Override
    public void onHide() {

    }

    @Override
    public void onShow() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }
}
