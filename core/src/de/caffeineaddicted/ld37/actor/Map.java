package de.caffeineaddicted.ld37.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import de.caffeineaddicted.ld37.screen.GameScreen;
import de.caffeineaddicted.sgl.SGL;
import de.caffeineaddicted.sgl.etities.Entity;
import de.caffeineaddicted.sgl.ui.interfaces.Creatable;

import static java.lang.Math.abs;

abstract public class Map extends Entity implements Creatable {
    protected boolean created = false;
    private Tile[] floor;
    private Vector2 start;
    private Vector2 exit;

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
            SGL.debug("++" + currentx + " " + currenty);
        }
        for (Tile tile : floor) {
            tile.create();
            addActor(tile);
        }
    }

    @Override
    public boolean isCreated() {
        return created;
    }

    public Tile[] getFloor() {
        return floor;
    }

    public void setFloor(Tile[] floor) {
        this.floor = floor;
    }

    public Tile getTileAt(Vector2 pos) {
        return getTileAt(pos.x, pos.y);
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
        int x_pix = (int) (x * TileSize);
        int y_pix = (int) (y * TileSize);
        return new Vector2(x_pix, y_pix);
    }

    public void update() {
        for (Tile tile : floor) {
            tile.update();
        }
    }

    public static int TileSize = 64;

    @Override
    public void draw(Batch batch, float parentAlpha) {
        for (Tile tile : floor) {
            tile.draw(batch, parentAlpha);
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }
}
