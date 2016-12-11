package de.caffeineaddicted.ld37.actor;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;

/**
 * Created by niels on 10.12.16.
 */
public class MapConfig {

    public static Map readMap(String mapData) {
        Json json = new Json();
        json.setElementType(MapConfigWrapper.class, "tiles", TileConfig.class);
        final MapConfigWrapper mapConfig = json.fromJson(MapConfigWrapper.class, mapData);

        Map map = new Map(mapConfig.width, mapConfig.height) {
            @Override
            public void onCreate() {
                Tile[] tiles = new Tile[mapConfig.tiles.size()];
                for (int i = 0; i < tiles.length; ++i) {
                    TileConfig tileConfig = mapConfig.tiles.get(i);
                    Tile tile = new Tile(Tile.Type.getTypeByName(tileConfig.type));
                    tile.setStart(new Vector2(tileConfig.x, tileConfig.y));
                    if (tileConfig.x2 >= 0 && tileConfig.y2 >= 0) {
                        tile.setEnd(new Vector2(tileConfig.x2, tileConfig.y2));
                    }
                    tile.setKey(tileConfig.key);
                    tile.setTrigger(tileConfig.trigger);
                    tiles[i] = tile;
                }

                setFloor(tiles);
            }
        };


        map.setStart(new Vector2(mapConfig.start_x, mapConfig.start_y));
        map.setExit(new Vector2(mapConfig.end_x, mapConfig.end_y));
        return map;
    }

    public static class TileConfig implements Json.Serializable {
        public int x;
        public int y;
        public int x2;
        public int y2;
        public int key;
        public String type;
        public String trigger;

        @Override
        public void write(Json json) {
        }

        @Override
        public void read(Json json, JsonValue jsonData) {
            x = jsonData.getInt("x");
            y = jsonData.getInt("y");
            x2 = jsonData.getInt("x2", -1);
            y2 = jsonData.getInt("y2", -1);
            key = jsonData.getInt("key", 0);
            type = jsonData.getString("type", "Empty");
            trigger = jsonData.getString("trigger", "");
        }
    }

    public static class MapConfigWrapper {
        public ArrayList<TileConfig> tiles;
        public int start_x;
        public int start_y;
        public int end_x;
        public int end_y;
        public int width;
        public int height;
    }
}
