package de.caffeineaddicted.ld37.actor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
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
    private Image keyGold, keyPink, keyGreen;
    private Label txtInventory;
    private int padding = 5;

    public HUD() {
        zindex(GameScreen.ZINDEX.Hud.idx);
        setAutoWidth(false);
        setAutoHeight(false);
        txtInventory = new Label("Inventory:", SGL.provide(Skin.class));
        background = new TextureRegionDrawable(new TextureRegion(SGL.provide(SGLAssets.class).get("ui/hud.png", Texture.class)));
        //background = new Image("hud.png");
        keyGold = new Key(Key.KEY_GOLD);
        //addActor(keyGold);
        keyPink = new Key(Key.KEY_PINK);
        //addActor(keyPink);
        keyGreen = new Key(Key.KEY_GREEN);
        //addActor(keyGreen);
        setWidth(Math.max(3 * (padding + keyGold.getWidth()) + padding, 2 * padding + txtInventory.getWidth()));
        setHeight(keyGold.getHeight() + (3 * padding) + txtInventory.getHeight());
    }

    @Override
    public void positionChanged() {
        super.positionChanged();
        keyGold.setPosition(getX() + padding, getY() + padding);
        keyPink.setPosition(getX() + padding + keyGold.getWidth(), getY() + padding);
        keyGreen.setPosition(getX() + padding + keyGold.getWidth() + padding + keyPink.getWidth(), getY() + padding);
        txtInventory.setPosition(getX() + padding, getY() + (2 * padding) + keyGold.getHeight());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        keyGold.setVisible(SGL.provide(GameScreen.class).getPlayer().hasKey(Key.KEY_GOLD));
        keyPink.setVisible(SGL.provide(GameScreen.class).getPlayer().hasKey(Key.KEY_PINK));
        keyGreen.setVisible(SGL.provide(GameScreen.class).getPlayer().hasKey(Key.KEY_GREEN));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.setColor(0.32f, 0.32f, 0.32f, 0.5f);
        background.draw(batch, getX(), getY(), getWidth(), getHeight());
        if (txtInventory.isVisible()) {
            txtInventory.draw(batch, parentAlpha);
        }
        if (keyGold.isVisible()) {
            keyGold.draw(batch, parentAlpha);
        }
        if (keyPink.isVisible()) {
            keyPink.draw(batch, parentAlpha);
        }
        if (keyGreen.isVisible()) {
            keyGreen.draw(batch, parentAlpha);
        }
    }


    @Override
    public void update() {

    }
}
