package de.caffeineaddicted.ld37.actor;

import de.caffeineaddicted.ld37.screen.GameScreen;
import de.caffeineaddicted.sgl.etities.Image;

public class ActionMarker extends Image {

    public ActionMarker() {
        super("ui/action.png");
        zindex(GameScreen.ZINDEX.Marker.idx);
    }
}
