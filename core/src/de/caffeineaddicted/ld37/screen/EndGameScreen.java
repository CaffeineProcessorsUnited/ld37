package de.caffeineaddicted.ld37.screen;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import de.caffeineaddicted.ld37.LD37;
import de.caffeineaddicted.sgl.SGL;
import de.caffeineaddicted.sgl.etities.Image;
import de.caffeineaddicted.sgl.input.SGLScreenInputMultiplexer;
import de.caffeineaddicted.sgl.ui.screens.SGLRootScreen;
import de.caffeineaddicted.sgl.ui.screens.SGLStagedScreen;

/**
 * Created by felix on 12.12.16.
 */
public class EndGameScreen extends SGLStagedScreen<LD37> {
    private Image unicorn;
    private TextButton backtomenu;
    private Label congrats;

    @Override
    public void onBeauty() {
        unicorn.setPosition(getViewWidth() / 2 - unicorn.getWidth() / 2, getViewHeight() - unicorn.getHeight() * unicorn.getScaleY() - 40);
        congrats.setPosition(getViewWidth() / 2 - congrats.getWidth() / 2, getViewHeight() - congrats.getHeight() - 400);
        backtomenu.setPosition(getViewWidth() / 2 - backtomenu.getWidth() / 2, getViewHeight() - backtomenu.getHeight() - 500);
    }

    @Override
    public void dispose() {

    }

    @Override
    public void onDispose() {

    }

    @Override
    public void onCreate() {
        SGL.provide(SGLScreenInputMultiplexer.class).removeProcessor(this);
        unicorn = new Image("player/unicornendgame.png");
        unicorn.setScale(4);
        stage().addActor(unicorn);

        congrats = new Label("Congratulations, you completed all Levels.", SGL.provide(Skin.class));
        stage().addActor(congrats);

        backtomenu = new TextButton("Back to Main Menu", SGL.provide(Skin.class));
        backtomenu.setWidth(400);
        backtomenu.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                SGL.provide(SGLRootScreen.class).hideScreen(EndGameScreen.class);
                SGL.provide(SGLRootScreen.class).showScreen(MenuScreen.class, SGLRootScreen.ZINDEX.NEAR);
            }
        });
        stage().addActor(backtomenu);
    }

    @Override
    public void onHide() {
        SGL.provide(InputMultiplexer.class).removeProcessor(stage());
    }

    @Override
    public void onShow() {
        SGL.provide(InputMultiplexer.class).addProcessor(stage());
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }
}
