package de.caffeineaddicted.ld37.actor;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

public class MapLoader extends AsynchronousAssetLoader<MapWrapper, MapLoader.MapParameter> {
    private MapWrapper map;

    public MapLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, MapParameter parameter) {
        this.map = null;
        this.map = new MapWrapper(file);
    }

    @Override
    public MapWrapper loadSync(AssetManager manager, String fileName, FileHandle file, MapParameter parameter) {
        MapWrapper map = this.map;
        this.map = null;

        return map;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, MapParameter parameter) {
        return null;
    }

    public static class MapParameter extends AssetLoaderParameters<MapWrapper> {
    }

}
