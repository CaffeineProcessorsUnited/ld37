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
    }

    @Override
    public void onLoad() {
        load("unicornwalk.png", Texture.class);
        load("tile_empty.png", Texture.class);
        load("stonebroke.png", Texture.class);
        load("stonehalf.png", Texture.class);
        load("stone.png", Texture.class);
        load("icebroke.png", Texture.class);
        load("ice.png", Texture.class);

        load("keygold.png", Texture.class);
        load("keypink.png", Texture.class);
        load("keygreen.png", Texture.class);

        load("maps/01.json", MapWrapper.class);
        load("maps/02.json", MapWrapper.class);

    }
}
