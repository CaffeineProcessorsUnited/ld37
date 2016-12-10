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
        load("tile_empty.png", Texture.class);
        load("stonebroke.png", Texture.class);
        load("stonehalf.png", Texture.class);
        load("stone.png", Texture.class);
        load("icebroke.png", Texture.class);
        load("ice.png", Texture.class);

    }
}
