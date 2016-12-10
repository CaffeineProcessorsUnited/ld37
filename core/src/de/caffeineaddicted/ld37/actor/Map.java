package de.caffeineaddicted.ld37.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import de.caffeineaddicted.sgl.SGL;
import de.caffeineaddicted.sgl.etities.Entity;
import de.caffeineaddicted.sgl.ui.interfaces.Creatable;

import static java.lang.Math.abs;

abstract public class Map extends Entity implements Creatable {
    public static int TileSize = 64;
    protected boolean created = false;
    private Tile[] floor;
    private Vector2 start;
    private Vector2 exit;
    int width;
    int height;

    public Map(Vector2 start, Vector2 exit, int width, int height) {
        this.start = start;
        this.exit = exit;
        this.width = width;
        this.height = height;
    }

    @Override
    public void create() {
        onCreate();
        created = true;
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

    public void setFloor(Tile... floor) {
        this.floor = floor;
    }

    public Tile getTileAt(Vector2 pos) {
        return getTileAt(pos.x, pos.y);
    }

    public Tile getTileAt(float x, float y) {
        for (Tile tile : floor) {
            if (abs(tile.getCenterPoint().x - x) < (TileSize / 2) &&
                    abs(tile.getCenterPoint().y - y) < (TileSize / 2)) {
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
        return calPixCoord((int) v.x, (int) v.y);
    }

    public Vector2 calPixCoord(int x, int y) {
        int x_pix = x * TileSize;//(int) (x / (dimx+1) * SGL.provide(GameScreen.class).getViewWidth());
        int y_pix = y * TileSize;//(int) (y / (dimy+1) * SGL.provide(GameScreen.class).getViewHeight());
        return new Vector2(x_pix, y_pix);
    }

    public void update() {
        for (Tile tile : floor) {
            tile.update();
        }
    }

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
