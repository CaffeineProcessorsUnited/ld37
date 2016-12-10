package de.caffeineaddicted.ld37.actor.maps;

import com.badlogic.gdx.math.Vector2;
import de.caffeineaddicted.ld37.actor.Map;
import de.caffeineaddicted.ld37.actor.Tile;
import de.caffeineaddicted.sgl.SGL;


public class Map01 extends Map {
    public Map01(Vector2 start, Vector2 exit){
        super(start, exit);
    }

    public void onCreate(){
        Tile[] tiles = new Tile[100];
        for(int x = 0; x < 10; ++x){
            for(int y = 0; y < 10; ++y){
                int i = x*10+y;
                if(x == y){
                    tiles[i] = new Tile(Tile.Type.Stone, new Vector2(x, y));
                } else {
                    tiles[i] = new Tile(Tile.Type.Ice, new Vector2(x, y));
                }
                SGL.debug(x+" "+y+" "+(x*10+y));
            }
        }
        setFloor(tiles);
    }
}
