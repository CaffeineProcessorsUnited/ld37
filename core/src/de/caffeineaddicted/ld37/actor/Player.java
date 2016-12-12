package de.caffeineaddicted.ld37.actor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import de.caffeineaddicted.ld37.screen.GameScreen;
import de.caffeineaddicted.sgl.SGL;
import de.caffeineaddicted.sgl.utils.SGLAssets;

public class Player extends UnitBase {
    final private float speed = 0.3f;
    private String ACTOR_BASE;
    private MovementDirection movingDir = MovementDirection.NONE, slipperyDir, keyDir = MovementDirection.NONE;
    private Tile currentTile;
    private boolean newTile = true, dieOnChange = false;
    private int keys = Key.KEY_NONE;

    private Vector2 teleportPosition;

    public Player() {
        zindex(GameScreen.ZINDEX.Player.idx);
        Animation animation = new Animation(SGL.provide(SGLAssets.class).get("player/unicornwalk.png", Texture.class), 4, 64, 64);
        animation.setFrameDuration(0.18f);
        ACTOR_BASE = addActor("unicorn", animation);

        getActor(ACTOR_BASE).setWidth(64);
        getActor(ACTOR_BASE).setHeight(64);
        setWidth(getActor(ACTOR_BASE).getWidth());
        setHeight(getActor(ACTOR_BASE).getHeight());

        update();
    }

    public void keyDown(MovementDirection dir) {
        keyDir = dir;
    }

    public void keyUp(MovementDirection dir) {
        if (keyDir == dir) {
            keyDir = MovementDirection.NONE;
        }
    }

    public void collectKey(int key) {
        keys |= key;
    }

    public boolean hasKey(int key) {
        //SGL.debug("has key " + key + "? " + ((keys & key) == key));
        return (keys & key) == key;
    }

    public boolean useKeys(int key) {
        if (hasKey(key)) {
            keys &= ~key;
            return true;
        }
        return false;
    }

    @Override
    public void update() {
    }

    @Override
    protected void onDie() {
        SGL.provide(GameScreen.class).loseGame();
    }


    @Override
    public void act(float delta) {
        super.act(delta);

        if (teleportPosition != null) {
            Vector2 pos = SGL.provide(GameScreen.class).getMap().calPixCoord(teleportPosition);
            pos.add(SGL.provide(GameScreen.class).getMap().getX(), SGL.provide(GameScreen.class).getMap().getY());
            currentTile.setTriggered(false);
            setPosition(pos.x, pos.y);
            teleportPosition = null;
            slipperyDir = MovementDirection.NONE;
            clearActions();
            SGL.provide(GameScreen.class).centerMap();
        }

        Tile tile = SGL.provide(GameScreen.class).getMap().getTileAt(getCenterPoint().x, getCenterPoint().y);

        if (tile == null)
            return;

        if (currentTile != tile) {
            newTile = true;
        }

        if (tile.isDead() || (newTile && dieOnChange) || (slipperyDir != null && !tile.canAccess(slipperyDir.flag()) && tile.getType().mode == Tile.MODE.FALLING)) {
            dieOnChange = false;
            onDie();
            SGL.error("U DEAD");
            return;
        }

        if (!hasActions()) {
            if (newTile) {
                newTile = false;
                tile.walkOver();
                if (tile.hasKey()) {
                    collectKey(tile.takeKey());
                }
                if (tile.isKeyHole()) {
                    if (tile.canAcceptKeys(keys)) {
                        keys = tile.fillKeyHole(keys);
                    }
                }
                tile.trigger();
            }
            if (tile.getType().slipery && slipperyDir != null && slipperyDir != MovementDirection.NONE) {
                createAction(slipperyDir, true);
            } else if (movingDir != MovementDirection.NONE) {
                createAction(movingDir, false);
                slipperyDir = movingDir;
                movingDir = MovementDirection.NONE;
            } else {
                movingDir = keyDir;
            }
        }
        if (hasActions()) {
            getActor(ACTOR_BASE, Animation.class).resume();
        } else {
            getActor(ACTOR_BASE, Animation.class).pause();
        }
        currentTile = tile;
    }

    public void createAction(MovementDirection dir, boolean wasslippery) {
        MoveByAction action = new MoveByAction();
        int accessflag = 0;
        switch (dir) {
            case UP:
                action.setAmount(0, Map.TileSize);
                accessflag = Tile.ACCESS_DOWN;
                break;
            case DOWN:
                action.setAmount(0, -Map.TileSize);
                accessflag = Tile.ACCESS_UP;
                break;
            case LEFT:
                action.setAmount(-Map.TileSize, 0);
                accessflag = Tile.ACCESS_RIGHT;
                break;
            case RIGHT:
                action.setAmount(Map.TileSize, 0);
                accessflag = Tile.ACCESS_LEFT;
                break;
        }
        Vector2 newpos = getCenterPoint().cpy().add(action.getAmountX(), action.getAmountY());
        Tile newtile = SGL.provide(GameScreen.class).getMap().getTileAt(newpos);
        if (newtile == null || (!newtile.canAccess(dir.flag()) && newtile.getType().mode == Tile.MODE.BLOCKING) || !newtile.canAcceptKeys(keys)) {
            if (wasslippery) {
                slipperyDir = MovementDirection.NONE;
            }
            return;
        }
        if (!newtile.canAccess(accessflag) && newtile.getType().mode == Tile.MODE.FALLING) {
            dieOnChange = true;
        }
        if (!wasslippery) {
            SGL.provide(GameScreen.class).nextMessage();
        }

        if (action.getAmountX() >= 0) {
            getActor(ACTOR_BASE).setScaleX(1);
        } else if (action.getAmountX() < 0) {
            getActor(ACTOR_BASE).setScaleX(-1);
        }

        if (action.getAmountY() > 0) {
            getActor(ACTOR_BASE).setRotation(90);
            getActor(ACTOR_BASE).setScaleY(1);
        } else if (action.getAmountY() < 0) {
            getActor(ACTOR_BASE).setRotation(-90);
        } else {
            getActor(ACTOR_BASE).setRotation(0);
        }
        currentTile.setTriggered(false);
        action.setDuration(speed);
        addAction(action);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        getActor(ACTOR_BASE).draw(batch, parentAlpha);
    }

    public void teleport(int x, int y) {
        teleportPosition = new Vector2(x, y);
    }

    public enum MovementDirection {
        NONE(Tile.ACCESS_NONE),
        LEFT(Tile.ACCESS_LEFT),
        RIGHT(Tile.ACCESS_RIGHT),
        UP(Tile.ACCESS_UP),
        DOWN(Tile.ACCESS_DOWN);

        private int flag;

        MovementDirection(int f) {
            flag = f;
        }

        public int flag() {
            return flag;
        }
    }
}
