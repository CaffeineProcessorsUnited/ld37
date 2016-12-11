package de.caffeineaddicted.ld37.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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
public class BackgroundScreen extends SGLStagedScreen<LD37> {

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

    }

    @Override
    public void onBeforeDraw() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        SGL.provide(ShapeRenderer.class).begin(ShapeRenderer.ShapeType.Filled);
        SGL.provide(ShapeRenderer.class).setColor(0.165f, 0.498f, 0.251f, 1f);
        SGL.provide(ShapeRenderer.class).rect(stage().getViewOrigX(), stage().getViewOrigY(), stage().getWidth(), stage().getHeight());
        SGL.provide(ShapeRenderer.class).end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    @Override
    public void onAfterDraw() {
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
