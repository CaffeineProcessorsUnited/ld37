package de.caffeineaddicted.ld37.utils;

import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
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
        load("background/menu.png", Texture.class);
        load("ui/speech.png", Texture.class);

        load("music/song0.mp3", Music.class);
        load("music/song1.mp3", Music.class);
        load("music/song2.mp3", Music.class);
        load("music/song3.mp3", Music.class);
        load("music/song4.mp3", Music.class);
        load("music/song5.mp3", Music.class);
        load("music/song6.mp3", Music.class);
        load("music/song7.mp3", Music.class);
        load("music/song8.mp3", Music.class);
    }

    @Override
    public void onLoad() {
        load("ui/hud.png", Texture.class);
        load("ui/message.png", Texture.class);

        load("player/unicornwalk.png", Texture.class);
        load("player/unicornfalling.png", Texture.class);
        load("player/unicornclimbing.png", Texture.class);
        load("player/ladder.png", Texture.class);

        load("background/map.png", Texture.class);
        load("background/menu.png", Texture.class);
        load("background/game.png", Texture.class);

        load("tiles/tile_empty.png", Texture.class);
        load("tiles/stonebroke.png", Texture.class);
        load("tiles/stonehalf.png", Texture.class);
        load("tiles/stone.png", Texture.class);
        load("tiles/icebroke.png", Texture.class);
        load("tiles/ice.png", Texture.class);
        load("tiles/woodplankhorizontal.png", Texture.class);
        load("tiles/woodplankvertical.png", Texture.class);
        load("tiles/metal.png", Texture.class);
        load("tiles/entry.png", Texture.class);
        load("tiles/exit.png", Texture.class);
        load("tiles/action.png", Texture.class);

        load("walls/wall.png", Texture.class);
        load("walls/wallleft.png", Texture.class);
        load("walls/wallright.png", Texture.class);
        load("walls/wallup.png", Texture.class);
        load("walls/walldown.png", Texture.class);
        load("walls/wallcornerupperleft.png", Texture.class);
        load("walls/wallcornerupperright.png", Texture.class);
        load("walls/wallcornerlowerleft.png", Texture.class);
        load("walls/wallcornerlowerright.png", Texture.class);

        load("keys/key1.png", Texture.class);
        load("keys/key2.png", Texture.class);
        load("keys/key4.png", Texture.class);
        load("keys/lock1.png", Texture.class);
        load("keys/lock2.png", Texture.class);
        load("keys/lock3.png", Texture.class);
        load("keys/lock4.png", Texture.class);
        load("keys/lock5.png", Texture.class);
        load("keys/lock6.png", Texture.class);
        load("keys/lock7.png", Texture.class);

        load("maps/-1.json", MapWrapper.class);
        load("maps/0.json", MapWrapper.class);
        load("maps/1.json", MapWrapper.class);
        load("maps/2.json", MapWrapper.class);
        load("maps/3.json", MapWrapper.class);
        load("maps/4.json", MapWrapper.class);

    }
}
