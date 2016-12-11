package de.caffeineaddicted.ld37.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import de.caffeineaddicted.ld37.LD37;
import de.caffeineaddicted.sgl.SGL;
import de.caffeineaddicted.sgl.ui.screens.SGLStagedScreen;
import de.caffeineaddicted.sgl.utils.SGLAssets;

/**
 * @author Malte Heinzelmann
 */
public class BackgroundScreen extends SGLStagedScreen<LD37> {

    private Texture texture;
    private TiledDrawable background;
    private Vector2 pos = new Vector2(), size = new Vector2();

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
        texture = SGL.provide(SGLAssets.class).get("background/game.png", Texture.class);
        background = new TiledDrawable(new TextureRegion(texture));
        moveBy(0, 0);
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
        SGL.provide(SpriteBatch.class).begin();
        SGL.provide(SpriteBatch.class).setColor(1, 1, 1, 1);
        background.draw(SGL.provide(SpriteBatch.class), pos.x, pos.y, size.x, size.y);
        SGL.provide(SpriteBatch.class).end();
    }

    public void moveBy(float x, float y) {
        pos.add(x, y);
        while (pos.x > 0) {
            pos.sub(texture.getWidth(), 0);
        }
        while (pos.y > 0) {
            pos.sub(0, texture.getHeight());
        }
        size.set(getViewWidth() - pos.x, getViewHeight() - pos.y);
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
