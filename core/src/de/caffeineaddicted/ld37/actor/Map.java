package de.caffeineaddicted.ld37.actor;

import com.badlogic.gdx.math.Vector2;
import de.caffeineaddicted.ld37.screen.GameScreen;
import de.caffeineaddicted.sgl.SGL;
import de.caffeineaddicted.sgl.etities.Entity;
import de.caffeineaddicted.sgl.ui.interfaces.Creatable;

import static java.lang.Math.abs;

abstract public class Map extends Entity implements Creatable{
    private Tile[] floor;
    private Vector2 start;
    private Vector2 exit;
    private int dimx, dimy;

    protected boolean created = false;

    public Map(Vector2 start, Vector2 exit) {
        this.start = start;
        this.exit = exit;
    }

    @Override
    public void create() {
        onCreate();
        created = true;
        int currentx = 0;
        int currenty = 0;
        for (Tile tile : floor) {
            currentx = Math.max(currentx, (int) tile.getStart().x);
            currenty = Math.max(currenty, (int) tile.getStart().y);
        }
        this.dimx = currentx;
        this.dimy = currenty;
        for (Tile tile : floor) {
            tile.create();
        }
    }

    @Override
    public boolean isCreated() {
        return created;
    }

    public void setFloor(Tile[] floor){
        this.floor = floor;
    }

    public Tile[] getFloor() {
        return floor;
    }

    public Tile getTileAt(float x, float y) {
        for (Tile tile : floor) {
            if (abs(tile.getCenterPoint().x - x) < tile.getWidth() &&
                    abs(tile.getCenterPoint().y - y) < tile.getHeight()) {
                return tile;
            }
        }
        return null;
    }

    public Vector2 getStart() {
        return start;
    }

    public Vector2 getExit() {
        return exit;
    }

    public Vector2 calPixCoord(Vector2 v) {
        return calPixCoord(v.x, v.y);
    }

    public Vector2 calPixCoord(float x, float y) {
        int x_pix = (int) (x / dimx * SGL.provide(GameScreen.class).getViewWidth());
        int y_pix = (int) (y / dimy * SGL.provide(GameScreen.class).getViewHeight());
        return new Vector2(x_pix, y_pix);
    }

    public void update() {
        for (Tile tile : floor) {
            tile.update();
        }
    }
/*
    @Override
    public void draw(Batch batch, float parentAlpha){
        for(Tile tile: floor){
            tile.draw(batch, parentAlpha);
        }
    }*/
}
