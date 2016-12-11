package de.caffeineaddicted.ld37.actor;

import de.caffeineaddicted.ld37.screen.GameScreen;
import de.caffeineaddicted.sgl.etities.Image;

public class Key extends Image {
    public final static int KEY_NONE = 0;
    public final static int KEY_GOLD = 1;
    public final static int KEY_PINK = 2;
    public final static int KEY_GREEN = 4;
    public final static int KEY_ALL = KEY_PINK + KEY_GOLD + KEY_GREEN;

    public Key(String image) {
        super(image);
        zindex(GameScreen.ZINDEX.Key.idx);
    }
}
