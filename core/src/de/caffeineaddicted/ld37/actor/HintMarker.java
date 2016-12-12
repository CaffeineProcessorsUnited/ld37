package de.caffeineaddicted.ld37.actor;

import de.caffeineaddicted.ld37.screen.GameScreen;
import de.caffeineaddicted.sgl.etities.Image;

public class HintMarker extends Image {

    public HintMarker() {
        super("ui/hint.png");
        zindex(GameScreen.ZINDEX.Marker.idx);
    }
}
