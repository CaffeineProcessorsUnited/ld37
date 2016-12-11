package de.caffeineaddicted.ld37.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import de.caffeineaddicted.ld37.LD37;
import de.caffeineaddicted.sgl.SGL;
import de.caffeineaddicted.sgl.input.SGLScreenInputMultiplexer;
import de.caffeineaddicted.sgl.ui.screens.SGLRootScreen;
import de.caffeineaddicted.sgl.ui.screens.SGLStagedScreen;
import de.caffeineaddicted.sgl.utils.SGLAssets;

import java.util.ArrayList;

/**
 * @author Malte Heinzelmann
 */
public class MenuScreen extends SGLStagedScreen<LD37> {

    TextButton btnStart;
    private int state = 0;
    private Integer[] states = new Integer[] {
            1, 2, 1,
            1, 4, 1
    };
    private ArrayList<Label> speechLabels = new ArrayList<Label>();
    private Drawable speechBackground;
    private float speechPadding = 10;
    private float timer = 0, speechAlpha = 0;

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
    public void onBeforeAct(float delta) {
        if (state >= states.length) {
            return;
        }
        timer += delta;
        switch (state % 3) {
            case 0:
                speechAlpha = Math.min(timer / states[state], 1f);
                break;
            case 1:
                speechAlpha = 1f;
                break;
            case 2:
                speechAlpha = 1 - Math.min(timer / states[state], 1f);
                break;
        }
        if (timer >= states[state]) {
            timer -= states[state++];
        }
    }

    @Override
    public void onCreate() {
        stage().setDebugAll(true);
        SGL.provide(SGLScreenInputMultiplexer.class).removeProcessor(this);
        Image background = new Image(SGL.provide(SGLAssets.class).get("background.png", Texture.class));
        background.setSize(getViewWidth(), getViewHeight());
        background.setZIndex(0);
        stage().addActor(background);
        speechBackground = new TextureRegionDrawable(new TextureRegion(SGL.provide(SGLAssets.class).get("speech.png", Texture.class)));
        Label t1 = new Label("Hey!", SGL.provide(Skin.class));
        t1.setPosition(getViewWidth() / 2 - t1.getWidth() / 2, 400);
        speechLabels.add(t1);
        Label t2 = new Label("Try to solve the puzzles", SGL.provide(Skin.class));
        t2.setPosition(getViewWidth() / 2 - t2.getWidth(), 400);
        speechLabels.add(t2);

        btnStart = new TextButton("Start Game", SGL.provide(Skin.class));
        btnStart.setPosition(getViewWidth() / 2 - btnStart.getWidth() / 2, 500);
        btnStart.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                SGL.provide(SGLRootScreen.class).hideScreen(MenuScreen.class);
                SGL.provide(SGLRootScreen.class).showScreen(GameScreen.class, SGLRootScreen.ZINDEX.NEAR);
            }
        });
        btnStart.setZIndex(10);

        //btnStart.setStyle(SGL.provide(Skin.class).get(TextButton.TextButtonStyle.class).);
        stage().addActor(btnStart);
    }

    @Override
    public void onBeforeDraw() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        SGL.provide(ShapeRenderer.class).begin(ShapeRenderer.ShapeType.Filled);
        SGL.provide(ShapeRenderer.class).setColor(0f, 0f, 0f, 0.6f);
        SGL.provide(ShapeRenderer.class).rect(stage().getViewOrigX(), stage().getViewOrigY(), stage().getWidth(), stage().getHeight());
        SGL.provide(ShapeRenderer.class).end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    @Override
    public void onAfterDraw() {
        SGL.provide(SpriteBatch.class).begin();
        SGL.provide(SpriteBatch.class).setColor(1f, 1f, 1f, 1f);
        int speech = (int) Math.floor(state / 3);
        if (speech < speechLabels.size()) {
            SGL.provide(SpriteBatch.class).setColor(0.32f, 0.32f, 0.32f, speechAlpha);
            speechBackground.draw(SGL.provide(SpriteBatch.class), speechLabels.get(speech).getX() - speechPadding, speechLabels.get(speech).getY() - speechPadding, speechLabels.get(speech).getWidth() + 2 * speechPadding, speechLabels.get(speech).getHeight() + 2 * speechPadding);
            speechLabels.get(speech).draw(SGL.provide(SpriteBatch.class), speechAlpha);
        }
        SGL.provide(SpriteBatch.class).end();
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
