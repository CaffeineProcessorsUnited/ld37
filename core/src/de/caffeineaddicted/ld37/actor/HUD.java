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

    Drawable background;
    Image keyPink, keyGold, keyGreen;

    public HUD() {
        background = new TextureRegionDrawable(new TextureRegion(SGL.provide(SGLAssets.class).get("hud.png", Texture.class)));
        keyPink = new Key("keys/keypink.png");
        keyGold = new Key("keys/keygold.png");
        keyGreen = new Key("keys/keygreen.png");
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

    }


    @Override
    public void update() {

    }
}
