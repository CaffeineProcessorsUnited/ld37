package de.caffeineaddicted.ld37.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import de.caffeineaddicted.sgl.SGL;
import de.caffeineaddicted.sgl.etities.Entity;
import de.caffeineaddicted.sgl.ui.interfaces.Creatable;
import de.caffeineaddicted.sgl.utils.SGLAssets;

import static java.lang.Math.abs;

public class Map extends Entity implements Creatable {
    public static int TileSize = 64;
    protected boolean created = false;
    int width;
    int height;
    private Tile[] floor = new Tile[0], wall;
    private Vector2 start;
    private Vector2 exit;
    private MapConfig.MapConfigWrapper mapConfig;
    private TiledDrawable background;

    public Map(int width, int height, MapConfig.MapConfigWrapper mapConfig) {
        this.width = width;
        this.height = height;
        this.mapConfig = mapConfig;
        this.wall = new Tile[2 * width + 2 * height + 4];
    }

    @Override
    public void create() {
        onCreate();
        created = true;
        background = new TiledDrawable(new TextureRegion(SGL.provide(SGLAssets.class).get("background/map.png", Texture.class)));
        for (Tile tile : floor) {
            tile.create();
            addActor(tile);
        }
        for (Tile tile : wall) {
            tile.create();
            addActor(tile);
        }
    }

    @Override
    public void onCreate() {
        Tile[] tiles = new Tile[mapConfig.tiles.size()];
        for (int i = 0; i < tiles.length; ++i) {
            MapConfig.TileConfig tileConfig = mapConfig.tiles.get(i);
            Tile tile = new Tile(Tile.Type.getTypeByName(tileConfig.type));
            tile.setStart(new Vector2(tileConfig.x, tileConfig.y));
            if (tileConfig.x2 >= 0 && tileConfig.y2 >= 0) {
                tile.setEnd(new Vector2(tileConfig.x2, tileConfig.y2));
            }
            tile.setKey(tileConfig.key);
            tile.setKeyHole(tileConfig.hole);
            tile.setTrigger(tileConfig.trigger);
            tiles[i] = tile;
        }
        setFloor(tiles);

    }

    public void reset() {
        created = false;
        floor = null;
        setStart(new Vector2(mapConfig.start_x, mapConfig.start_y));
        setExit(new Vector2(mapConfig.end_x, mapConfig.end_y));
        create();
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
        for (Tile tile: this.floor) {
            tile.getStart().add(1, 1);
        }
        start.add(1, 1);
        exit.add(1, 1);
        // calculate wall
        int i = 0, y, x;
        Tile tile;
        Tile.Type type = Tile.Type.Wall;
        for (y = 0; y < height + 2; y++) {
            for (x = 0; x < width + 2; x++) {
                if (y > 0 && y < height + 1 && x > 0 && x < width + 1) {
                    continue;
                }
                if (y == 0) {
                    type = Tile.Type.WallD;
                } else if (y == height + 1) {
                    type = Tile.Type.WallU;
                } else {
                    if (x == 0) {
                        type = Tile.Type.WallL;
                    } else {
                        type = Tile.Type.WallR;
                    }
                }
                if (x == 0) {
                    if (y == 0) {
                        type = Tile.Type.WallLD;
                    }
                    if (y == height + 1) {
                        type = Tile.Type.WallLU;
                    }
                }
                if (x == width + 1) {
                    if (y == 0) {
                        type = Tile.Type.WallRD;
                    }
                    if (y == height + 1) {
                        type = Tile.Type.WallRU;
                    }
                }
                tile = new Tile(type);
                tile.setStart(new Vector2(x, y));
                SGL.debug("i: " + i + " tile: " + tile.getStart().toString());
                //wall[(y == -1 ? 0 : 1) * (width + 2) + (Math.max(y, 0) * 2) + x] = tile;;
                wall[i++] = tile;
            }
        }
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

    public void setStart(Vector2 start) {
        this.start = start;
    }

    public Vector2 getExit() {
        return exit;
    }

    public void setExit(Vector2 exit) {
        this.exit = exit;
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
        for (Tile tile : wall) {
            tile.update();
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        /*
        batch.end();
        SGL.provide(ShapeRenderer.class).begin(ShapeRenderer.ShapeType.Filled);
        SGL.provide(ShapeRenderer.class).setColor(0.498f, 0.165f, 0.251f, 1f);
        SGL.provide(ShapeRenderer.class).rect(getX(), getY(), getWidth(), getHeight());
        SGL.provide(ShapeRenderer.class).end();
        batch.begin();
        */
        batch.setColor(1, 1, 1, 1);
        background.draw(batch, getX(), getY(), getWidth(), getHeight());
        for (Tile tile : floor) {
            tile.draw(batch, parentAlpha);
        }
        for (Tile tile : wall) {
            tile.draw(batch, parentAlpha);
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

}
