package de.caffeineaddicted.ld37.actor;

import com.badlogic.gdx.math.Vector2;
import de.caffeineaddicted.ld37.screen.GameScreen;
import de.caffeineaddicted.sgl.SGL;
import de.caffeineaddicted.sgl.etities.Entity;
import de.caffeineaddicted.sgl.ui.interfaces.Creatable;
import de.caffeineaddicted.sgl.ui.interfaces.Mortal;

public class Tile extends Entity implements Mortal, Creatable {
    private Tile.Type type;
    private int stepsLeft;
    private boolean hasKey = false;
    private boolean created = false;
    private Vector2 move;
    private Vector2 moveLeft;
    private boolean moveReversed = false;

    private Vector2 start, end;
    private boolean dieing = false;

    private boolean dead = false;

    public Tile(Tile.Type type, Vector2 start) {
        this(type, false, start, null);
    }

    public Tile(Tile.Type type, Vector2 start, Vector2 end) {
        this(type, false, start, end);
    }

    public Tile(Tile.Type type, boolean hasKey, Vector2 start, Vector2 end) {
        this.type = type;
        stepsLeft = type.durability;
        this.hasKey = hasKey;
        this.start = start;
        this.end = end;

    }

    @Override
    public void act(float delta) {
        if (!isCreated()) {
            create();
        }
        super.act(delta);
        if (end != null && type.speed > 0) {
            float percent = delta / type.speed;
            float mX = move.x * (move.x == 0 ? 0 : Map.TileSize / move.x) * percent;
            float mY = move.y * (move.y == 0 ? 0 : Map.TileSize / move.y) * percent;
            mX *= (moveReversed) ? -1 : 1;
            mY *= (moveReversed) ? -1 : 1;
            if (Math.abs(moveLeft.x) < mX) {
                mX = moveLeft.x;
            }
            if (Math.abs(moveLeft.y) < mY) {
                mY = moveLeft.y;
            }
            moveLeft.sub(mX, mY);
            moveBy(mX, mY);
            //SGL.debug("percent=" + percent + ";dx=" + dX + ";dy=" + dY + ";mx=" + mX + ";my=" + mY);
            if ((moveReversed ? -moveLeft.x : moveLeft.x) < 1 && (moveReversed ? -moveLeft.y : moveLeft.y) < 1) {
                SGL.debug("reverse!");
                moveReversed = !moveReversed;
                moveLeft.set(moveReversed ? -move.x : move.x, moveReversed ? -move.y : move.y);
            }
        }
        if (dieing) {
            if (SGL.provide(GameScreen.class).getMap().getTileAt(getCenterPoint()) != SGL.provide(GameScreen.class).getMap().getTileAt(SGL.provide(GameScreen.class).getPlayer().getCenterPoint())) {
                // Player left tile. We can now die in peace.
                dieing = false;
                dead = true;
                setTexture();
            }
        }
    }

    public Vector2 getStart() {
        return start;
    }

    public Vector2 getEnd() {
        return end;
    }

    @Override
    public boolean isDead() {
        return dead;
    }

    public boolean hasKey() {
        return hasKey;
    }

    public void takeKey() {
        hasKey = false;
    }

    public Tile.Type getType() {
        return type;
    }

    @Override
    public void die() {

    }

    public void onDie() {

    }

    public void walkOver() {
        SGL.debug("durability: " + type.durability);
        if (type.durability > 0) {
            SGL.debug("stepsLeft: " + stepsLeft);
            if (--stepsLeft < 1) {
                dieing = true;
            }
        }
        if (!(type.durability > 0 && stepsLeft < 1)) {
            setTexture();
        }
    }

    @Override
    public void update() {

    }

    @Override
    public void create() {
        Vector2 vecstart = SGL.provide(GameScreen.class).getMap().calPixCoord(start);
        setCenterPosition(vecstart.x, vecstart.y);
        if (end != null) {
            Vector2 vecend = SGL.provide(GameScreen.class).getMap().calPixCoord(end);
            move = vecend.cpy().sub(vecstart);
            moveLeft = move.cpy();
            SGL.debug(move.toString());
        }
        setTexture();
        sizeChanged();
        SGL.debug(getWidth() + "," + getHeight());
        created = true;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public boolean isCreated() {
        return created;
    }

    public void setTexture() {
        clear();
        int a = type.assets.length - 1;
        if (dieing || dead) {
            a = 0;
        } else {
            if (stepsLeft > 0) {
                a = stepsLeft;
            } else {
                a = 1;
            }
        }
        String Texture = addTexture(type.assets[a]);
    }

    public enum Type {

        Empty(0, false, 0, "tile_empty.png"),
        Stone(2, false, 4, "stonebroke.png", "stonehalf.png", "stone.png"),
        Ice(1, true, 2, "icebroke.png", "ice.png");

        public final int durability;
        public final boolean slipery;
        public final float speed;
        public final String[] assets;

        Type(int durability, boolean slippery, float speed, String... assets) {
            this.durability = durability;
            this.slipery = slippery;
            this.speed = speed;
            this.assets = assets;
        }
    }
}
