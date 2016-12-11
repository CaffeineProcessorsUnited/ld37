package de.caffeineaddicted.ld37.actor;

import com.badlogic.gdx.files.FileHandle;

/**
 * Created by niels on 10.12.16.
 */
public class MapWrapper {
    private Map map;

    public MapWrapper(FileHandle file) {
        String data = file.readString();
        map = MapConfig.readMap(data);
    }

    public Map getMap() {
        return map;
    }
}
