package de.caffeineaddicted.ld37.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.caffeineaddicted.ld37.LD37;
import de.caffeineaddicted.sgl.SGL;
import de.caffeineaddicted.sgl.input.SGLScreenInputMultiplexer;
import de.caffeineaddicted.sgl.ui.screens.SGLRootScreen;
import de.caffeineaddicted.sgl.ui.screens.SGLStagedScreen;
import de.caffeineaddicted.sgl.utils.SGLAssets;

import javax.swing.*;
import java.util.ArrayList;

/**
 * @author Malte Heinzelmann
 */
public class MenuScreen extends SGLStagedScreen<LD37> {

    private static Vector2[] resolutions = new Vector2[] {
            new Vector2(1920, 1080),
            new Vector2(1600, 900),
            new Vector2(1280, 720),
            new Vector2(800, 600)
    };
    private TextButton btnContinue, btnStart, btnCustomMap, btnResolution, btnExit;
    private Label btnCustomMapHelp;
    private int state = 0;
    private Float[] states = new Float[]{
            0.5f, 2f, 0.5f,
            0.5f, 2f, 0.5f
    };
    private ArrayList<Label> speechLabels = new ArrayList<Label>();
    private Drawable speechBackground;
    private TiledDrawable background;
    private float speechPadding = 10;
    private float timer = 0, speechAlpha = 0;

    @Override
    public void onBeauty() {
        btnContinue.setPosition(getViewWidth() / 2 - btnContinue.getWidth() / 2, getViewHeight() - 200);
        btnStart.setPosition(getViewWidth() / 2 - btnStart.getWidth() / 2, getViewHeight() - 250);
        btnCustomMap.setPosition(getViewWidth() / 2 - btnCustomMap.getWidth() / 2, getViewHeight() - 300);
        btnCustomMapHelp.setPosition(getViewWidth() / 2 - btnCustomMap.getWidth() / 2, getViewHeight() - 350);
        btnResolution.setPosition(getViewWidth() / 2 - btnResolution.getWidth() / 2, getViewHeight() - 450);
        btnExit.setPosition(getViewWidth() / 2 - btnExit.getWidth() / 2, getViewHeight() - 500);
    }

    @Override
    public void dispose() {

    }

    @Override
    public void onDispose() {

    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        dirty();
    }

    @Override
    public void onBeforeAct(float delta) {
        btnContinue.setDisabled(!SGL.provide(GameScreen.class).isCreated());
        if (SGL.provide(GameScreen.class).isCreated()) {
            btnStart.setText("Restart Game");
        }
        btnCustomMapHelp.setVisible(SGL.provide(GameScreen.class).getUseCustomMaps());

        if (state < states.length) {
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
    }

    @Override
    public void onCreate() {
        //stage().setDebugAll(true);
        SGL.provide(SGLScreenInputMultiplexer.class).removeProcessor(this);
        background = new TiledDrawable(new TextureRegionDrawable(new TextureRegion(SGL.provide(SGLAssets.class).get("background/menu.png", Texture.class))));

        speechBackground = new TextureRegionDrawable(new TextureRegion(SGL.provide(SGLAssets.class).get("ui/speech.png", Texture.class)));
        Label t1 = new Label("Hey! Welcome to \n.", SGL.provide(Skin.class));
        t1.setPosition(getViewWidth() / 2 - t1.getWidth() / 2, 100);
        //speechLabels.add(t1);
        Label t2 = new Label("Try to solve the puzzles.", SGL.provide(Skin.class));
        t2.setPosition(getViewWidth() / 2 - t2.getWidth() / 2, 100);
        //speechLabels.add(t2);

        btnContinue = new TextButton("Continue", SGL.provide(Skin.class));
        btnContinue.setWidth(400);
        btnContinue.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if (((TextButton) event.getListenerActor()).isDisabled()) {
                    return;
                }
                SGL.provide(SGLRootScreen.class).hideScreen(MenuScreen.class);
                SGL.provide(SGLRootScreen.class).showScreen(GameScreen.class, SGLRootScreen.ZINDEX.NEAR);
            }
        });
        stage().addActor(btnContinue);

        btnStart = new TextButton("Start Game", SGL.provide(Skin.class));
        btnStart.setWidth(400);
        btnStart.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                SGL.provide(SGLRootScreen.class).hideScreen(MenuScreen.class);
                SGL.provide(SGLRootScreen.class).showScreen(GameScreen.class, SGLRootScreen.ZINDEX.NEAR);
                SGL.provide(GameScreen.class).loadMap(5);
            }
        });
        stage().addActor(btnStart);

        btnCustomMap = new TextButton("", SGL.provide(Skin.class));
        btnCustomMap.setWidth(400);
        btnCustomMap.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                SGL.provide(GameScreen.class).toggleUseCustomMaps();
                updateBtnCustomMap();
            }
        });
        updateBtnCustomMap();
        stage().addActor(btnCustomMap);

        btnCustomMapHelp = new Label("Put your maps (0.json, 1.json, ...) into a folder\n\"maps\" in the same directory as your jar file", SGL.provide(Skin.class));
        btnCustomMapHelp.setWidth(400);
        btnCustomMapHelp.setAlignment(Align.center);
        stage().addActor(btnCustomMapHelp);

        btnResolution = new TextButton("", SGL.provide(Skin.class));
        btnResolution.setWidth(400);
        btnResolution.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                int i = 0;
                Vector2 r = null;
                for (i = 0; i < resolutions.length; i++) {
                    r = resolutions[i];
                    if (r.x == SGL.provide(Viewport.class).getWorldWidth() && r.y == SGL.provide(Viewport.class).getWorldHeight()) {
                        break;
                    }
                    r = null;
                }
                if (r == null) {
                    r = resolutions[resolutions.length - 1];
                } else {
                    r = resolutions[(i + 1) % resolutions.length];
                }
                //SGL.game().resize((int) r.x, (int) r.y);
                SGL.provide(Viewport.class).setWorldSize((int) r.x, (int) r.y);
                SGL.provide(Viewport.class).apply(true);
                SGL.game().resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                updateBtnResolution();
            }
        });
        updateBtnResolution();
        stage().addActor(btnResolution);


        btnExit = new TextButton("Exit", SGL.provide(Skin.class));
        btnExit.setWidth(400);
        btnExit.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        stage().addActor(btnExit);
    }

    public void updateBtnCustomMap() {
        btnCustomMap.setText("Use custom maps: " + (SGL.provide(GameScreen.class).getUseCustomMaps() ? "YES" : "NO"));
    }

    public void updateBtnResolution() {
        btnResolution.setText("Resolution: " + (int) SGL.provide(Viewport.class).getWorldWidth() + "x" + (int) SGL.provide(Viewport.class).getWorldHeight());
    }

    @Override
    public void onBeforeDraw() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        SGL.provide(ShapeRenderer.class).setProjectionMatrix(camera.combined);
        SGL.provide(ShapeRenderer.class).begin(ShapeRenderer.ShapeType.Filled);
        SGL.provide(ShapeRenderer.class).setColor(0f, 0f, 0f, 0.6f);
        SGL.provide(ShapeRenderer.class).rect(stage().getViewOrigX(), stage().getViewOrigY(), getViewWidth(), getViewHeight());
        SGL.provide(ShapeRenderer.class).end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        SGL.provide(SpriteBatch.class).begin();
        SGL.provide(SpriteBatch.class).setColor(0.7f, 0.7f, 0.7f, 1f);
        background.draw(SGL.provide(SpriteBatch.class), 0, 0, getViewWidth(), getViewHeight());
        SGL.provide(SpriteBatch.class).end();
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
