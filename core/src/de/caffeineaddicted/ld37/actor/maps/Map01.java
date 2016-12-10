package de.caffeineaddicted.ld37.actor.maps;

import com.badlogic.gdx.math.Vector2;
import de.caffeineaddicted.ld37.actor.Map;
import de.caffeineaddicted.ld37.actor.Tile;
import de.caffeineaddicted.sgl.SGL;

import java.util.ArrayList;


public class Map01 extends Map {
    public Map01(Vector2 start, Vector2 exit) {
        super(start, exit, 10, 10);
    }

    public void onCreate() {
        ArrayList<Tile> tiles = new ArrayList<Tile>();
        for (int x = 0; x < 10; ++x) {
            for (int y = 0; y < 10; ++y) {
                int i = x * 10 + y;
                if (x == 7 && y > 4 && y < 9) {
                    if (y == 5)
                        tiles.add(new Tile(Tile.Type.Stone, new Vector2(x, y), new Vector2(x, 8)));
                } else {
                    if (x == y) {
                        tiles.add(new Tile(Tile.Type.Ice, new Vector2(x, y)));
                    } else {
                        tiles.add(new Tile(Tile.Type.Stone, new Vector2(x, y)));
                    }
                }
                SGL.debug(x + " " + y + " " + (x * 10 + y));
            }
        }
        Tile[] tilearray = new Tile[tiles.size()];
        setFloor(tiles.toArray(tilearray));
    }
}
