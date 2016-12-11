package de.caffeineaddicted.ld37.actor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import de.caffeineaddicted.ld37.screen.GameScreen;
import de.caffeineaddicted.sgl.SGL;
import de.caffeineaddicted.sgl.etities.Entity;
import de.caffeineaddicted.sgl.etities.Image;
import de.caffeineaddicted.sgl.utils.SGLAssets;

/**
 * @author Malte Heinzelmann
 */
public class HUD extends Entity {

    private Drawable background;
    //private Image background;
    private Image keyPink, keyGold, keyGreen;
    private int padding = 5;

    public HUD() {
        zindex(GameScreen.ZINDEX.Hud.idx);
        setAutoWidth(false);
        setAutoHeight(false);
        background = new TextureRegionDrawable(new TextureRegion(SGL.provide(SGLAssets.class).get("hud.png", Texture.class)));
        //background = new Image("hud.png");
        keyPink = new Key("keys/keypink.png");
        //addActor(keyPink);
        keyGold = new Key("keys/keygold.png");
        //addActor(keyGold);
        keyGreen = new Key("keys/keygreen.png");
        //addActor(keyGreen);
        setWidth(3 * (padding + keyPink.getWidth()) + padding);
        setHeight(keyPink.getHeight() + (2 * padding));
    }

    @Override
    public void positionChanged() {
        super.positionChanged();
        keyPink.setPosition(getX() + padding, getY() + padding);
        keyGold.setPosition(getX() + padding + keyPink.getWidth(), getY() + padding);
        keyGreen.setPosition(getX() + padding + keyPink.getWidth() + padding + keyGold.getWidth(), getY() + padding);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        keyPink.setVisible(SGL.provide(GameScreen.class).getPlayer().hasKey(Key.KEY_PINK));
        keyGold.setVisible(SGL.provide(GameScreen.class).getPlayer().hasKey(Key.KEY_GOLD));
        keyGreen.setVisible(SGL.provide(GameScreen.class).getPlayer().hasKey(Key.KEY_GREEN));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.setColor(0.32f, 0.32f, 0.32f, 0.5f);
        background.draw(batch, getX(), getY(), getWidth(), getHeight());
        if  (keyPink.isVisible()) {
            keyPink.draw(batch, parentAlpha);
        }
        if  (keyGold.isVisible()) {
            keyGold.draw(batch, parentAlpha);
        }
        if  (keyGreen.isVisible()) {
            keyGreen.draw(batch, parentAlpha);
        }
    }


    @Override
    public void update() {

    }
}
