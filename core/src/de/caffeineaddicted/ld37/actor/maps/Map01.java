package de.caffeineaddicted.ld37.actor.maps;

import com.badlogic.gdx.math.Vector2;
import de.caffeineaddicted.ld37.actor.Map;
import de.caffeineaddicted.ld37.actor.Tile;

public class Map01 extends Map {
    public Map01(Vector2 start, Vector2 exit){
        super(start, exit);
    }

    public void onCreate(){
        Tile[] tiles = new Tile[100];
        for(int i = 0; i < 10; ++i){
            for(int j = 0; j < 10; ++j) {
                tiles[i*10 + j] = new Tile(Tile.Type.Stone, new Vector2(i, j));
            }
        }
        setFloor(tiles);
    }
}
