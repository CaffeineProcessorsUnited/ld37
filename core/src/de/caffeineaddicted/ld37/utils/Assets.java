package de.caffeineaddicted.ld37.utils;

import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import de.caffeineaddicted.ld37.actor.MapLoader;
import de.caffeineaddicted.ld37.actor.MapWrapper;
import de.caffeineaddicted.sgl.utils.SGLAssets;

public class Assets extends SGLAssets {


    @Override
    public void onSetup() {
        setLoader(MapWrapper.class,
                new MapLoader(new InternalFileHandleResolver())
        );
    }

    @Override
    public void onPreload() {
        load("skin/uiskin.json", Skin.class);
        load("background.png", Texture.class);
        load("speech.png", Texture.class);
        load("hud.png", Texture.class);
    }

    @Override
    public void onLoad() {
        load("player/unicornwalk.png", Texture.class);

        load("tiles/tile_empty.png", Texture.class);
        load("tiles/stonebroke.png", Texture.class);
        load("tiles/stonehalf.png", Texture.class);
        load("tiles/stone.png", Texture.class);
        load("tiles/icebroke.png", Texture.class);
        load("tiles/ice.png", Texture.class);

        load("keys/keygold.png", Texture.class);
        load("keys/keypink.png", Texture.class);
        load("keys/keygreen.png", Texture.class);

        load("maps/01.json", MapWrapper.class);
        load("maps/02.json", MapWrapper.class);
        load("maps/03.json", MapWrapper.class);

    }
}
