package de.caffeineaddicted.ld37prep.actor;

import com.badlogic.gdx.math.Vector2;
import de.caffeineaddicted.ld37prep.screen.GameScreen;
import de.caffeineaddicted.sgl.SGL;
import de.caffeineaddicted.sgl.etities.Entity;

import java.util.ArrayList;

import static java.lang.Math.abs;

/**
 * Created by felix on 10.12.16.
 */
public class Map extends Entity {
    private Tile[] floor;
    private Vector2[] keypos;
    private Vector2 start;
    private Vector2 exit;
    private int dimx, dimy;

    public Map(Tile[] floor, Vector2[] keypos, Vector2 start, Vector2 exit) {
        this.floor = floor;
        this.keypos = keypos;
        this.start = start;
        this.exit = exit;
        int currentx = 0;
        int currenty = 0;
        for(Tile tile:floor) {
            currentx = Math.max(currentx, tile.getStart());
            currenty = Math.max(currenty, tile.getStart());
        }
        this.dimx = currentx;
        this.dimy = currenty;
    }

    public Tile[] getFloor() {
        return floor;
    }

    public Tile getTileAt(float x, float y){
        for(Tile tile: floor){
            if(abs(tile.getCenterPoint().x-x) < tile.getWidth() &&
                    abs(tile.getCenterPoint().y-y) < tile.getHeight()){
                return tile;
            }
        }
        return null;
    }

    public Vector2[] getKeypos() {
        return keypos;
    }


    public Vector2 getStart() {
        return start;
    }

    public Vector2 getExit() {
        return exit;
    }

    public Vector2 calPixCoord(float x, float y){
        int x_pix = (int) (x / dimx * SGL.provide(GameScreen.class).getViewWidth());
        int y_pix = (int) (y / dimy * SGL.provide(GameScreen.class).getViewHeight());
        return new Vector2(x_pix, y_pix);
    }

    public void update() {
        for (Tile tile:floor) {
            tile.update();
        }
    }
}
