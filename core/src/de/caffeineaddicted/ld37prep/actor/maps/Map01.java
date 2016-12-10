package de.caffeineaddicted.ld37prep.actor.maps;

import com.badlogic.gdx.math.Vector2;
import de.caffeineaddicted.ld37prep.actor.Map;
import de.caffeineaddicted.ld37prep.actor.Tile;

public class Map01 extends Map {
    public Map01(Vector2 start, Vector2 exit){
        super(start, exit);
    }

    public void onCreate(){
        Tile[] tiles = new Tile[100];
        for(int i = 0; i < 100; ++i){
            tiles[i] = new Tile(Tile.Type.Empty, new Vector2(10*(i%10), 10*(i/10)));
        }
        super.setFloor(tiles);
    }
}
