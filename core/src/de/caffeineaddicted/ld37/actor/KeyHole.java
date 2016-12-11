package de.caffeineaddicted.ld37.actor;

import de.caffeineaddicted.ld37.screen.GameScreen;
import de.caffeineaddicted.sgl.etities.Image;

public class KeyHole extends Image {

    public KeyHole(int keyHole) {
        super("keys/lock" + keyHole + ".png");
        zindex(GameScreen.ZINDEX.KeyHole.idx);
    }
}
