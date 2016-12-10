package de.caffeineaddicted.ld37.actor;

import de.caffeineaddicted.ld37.screen.GameScreen;
import de.caffeineaddicted.sgl.etities.Image;

public class UnitKey extends Image {
    public UnitKey() {
        super("keygold.png");
        zindex(GameScreen.ZINDEX.Key.idx);
    }
}
