package de.caffeineaddicted.ld37.actor;

import com.badlogic.gdx.graphics.Texture;

/**
 * @author Malte Heinzelmann
 */
public class Animation extends de.caffeineaddicted.sgl.etities.Animation {
    public Animation(Texture texture, int count, int width, int height) {
        super(texture, count, width, height);
    }

    public Animation(Texture texture, int count, int width, int height, boolean loop) {
        super(texture, count, width, height, loop);
    }

    @Override
    public void setFrameDuration(float duration) {
        super.setFrameDuration(duration);
        animationChanged();
    }
}
