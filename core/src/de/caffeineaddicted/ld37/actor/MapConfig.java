package de.caffeineaddicted.ld37.actor;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import de.caffeineaddicted.ld37.utils.Assets;
import de.caffeineaddicted.sgl.SGL;

import java.util.ArrayList;

/**
 * Created by niels on 10.12.16.
 */
public class MapConfig
{
    public static class TileConfig implements Json.Serializable {
        public int x;
        public int y;
        public boolean key;
        public String type;

        @Override
        public void write(Json json) {
        }

        @Override
        public void read(Json json, JsonValue jsonData) {
            x = jsonData.getInt("x");
            y = jsonData.getInt("y");
            key = jsonData.getBoolean("key", false);
            type = jsonData.getString("type", "Empty");
        }
    }

    public static class MapConfigWrapper{
        public ArrayList<TileConfig> tiles;
        public int start_x;
        public int start_y;
        public int end_x;
        public int end_y;
        public int width;
        public int height;
    }

    public static Map readMap(String mapData){
        Json json = new Json();
        json.setElementType(MapConfigWrapper.class, "tiles", TileConfig.class);
        final MapConfigWrapper mapConfig = json.fromJson(MapConfigWrapper.class, mapData);

        return new Map(new Vector2(mapConfig.start_x, mapConfig.start_y),
                new Vector2(mapConfig.end_x, mapConfig.end_y),
                mapConfig.width, mapConfig.height) {
            @Override
            public void onCreate() {
                Tile[] tiles = new Tile[mapConfig.tiles.size()];
                for(int i = 0; i < tiles.length; ++i){
                    TileConfig tileConfig = mapConfig.tiles.get(i);
                    tiles[i] = new Tile(Tile.Type.getTypeByName(tileConfig.type),
                            tileConfig.key,
                            new Vector2(tileConfig.x, tileConfig.y));
                }

                setFloor(tiles);
            }
        };
    }
}
