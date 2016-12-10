package de.caffeineaddicted.ld37prep.utils;

import com.badlogic.gdx.graphics.Texture;
import de.caffeineaddicted.sgl.utils.SGLAssets;

public class Assets extends SGLAssets {


    @Override
    public void onSetup() {

    }

    @Override
    public void onPreload() {

    }

    @Override
    public void onLoad() {
        load("player.png", Texture.class);
    }
}
