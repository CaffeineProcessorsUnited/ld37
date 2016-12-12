package de.caffeineaddicted.ld37.actor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import de.caffeineaddicted.ld37.screen.GameScreen;
import de.caffeineaddicted.sgl.SGL;
import de.caffeineaddicted.sgl.etities.Entity;
import de.caffeineaddicted.sgl.utils.SGLAssets;

/**
 * @author Malte Heinzelmann
 */
public class MessageList extends Entity {

    private Drawable background;
    private int history;
    private DelayedRemovalArray<Label> messages;
    private float padding = 2;

    public MessageList() {
        this(5);
    }

    public MessageList(int history) {
        zindex(GameScreen.ZINDEX.Messages.idx);
        background = new TextureRegionDrawable(new TextureRegion(SGL.provide(SGLAssets.class).get("ui/message.png", Texture.class)));
        messages = new DelayedRemovalArray<Label>();
        this.history = history;
        setAutoWidth(false);
        setAutoHeight(false);
    }

    public void clear() {
        messages.clear();
    }

    public void postMessage(String message) {
        messages.add(new Label(message, SGL.provide(Skin.class)));
        SGL.debug("size:" + messages.size);
        while (messages.size > history) {
            SGL.debug("size:" + messages.size);
            messages.removeIndex(0);
        }
        Label l;
        float height = 0;
        float width = 0;
        for (int i = messages.size - 1; i >= 0; i--) {
            l = messages.get(i);
            l.setPosition(0, height);
            height += l.getHeight() + padding;
            width = Math.max(width, l.getWidth());
            SGL.debug("w:" + width + " h:" + height);
        }
        setWidth(width);
        setHeight(height);
        // 0 is the oldest message
    }

    @Override
    public void update() {

    }

    @Override
    public void act(float delta) {
        for (Label l: messages) {
            if (!l.hasActions()) {
                // Add all required actions
                l.addAction(Actions.sequence(
                        Actions.delay(5, Actions.alpha(0, 1)),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                messages.removeValue(l, true);
                            }
                        })
                ));
            }
            l.act(delta);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        for (Label l: messages) {
            batch.setColor(0.32f, 0.32f, 0.32f, 0.5f);
            background.draw(batch, l.getX(), l.getY(), getWidth(), l.getHeight());
            batch.setColor(1, 1, 1, 1);
            l.draw(batch, parentAlpha);
        }
    }
}