package de.caffeineaddicted.ld37.actor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import de.caffeineaddicted.ld37.screen.GameScreen;
import de.caffeineaddicted.sgl.SGL;
import de.caffeineaddicted.sgl.utils.SGLAssets;

public class Player extends UnitBase {
    final private float speed = 0.3f;
    private String ACTOR_BASE;
    private MovementDirection movingDir = MovementDirection.NONE, slipperyDir, keyDir = MovementDirection.NONE;
    private Tile currentTile;
    private boolean newTile = true;
    private int keys = Key.KEY_NONE;

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

        Tile tile = SGL.provide(GameScreen.class).getMap().getTileAt(getCenterPoint().x, getCenterPoint().y);

        if (currentTile != tile) {
            newTile = true;
        }

        if (tile == null || tile.isDead() || (slipperyDir != null && !tile.canAccess(slipperyDir.flag()))) {
            onDie();
            SGL.error("U DEAD");
            return;
        }

        if (!hasActions()) {
            if (newTile) {
                newTile = false;
                if (tile != null) {
                    tile.walkOver();
                    if (tile.hasKey()) {
                        collectKey(tile.takeKey());
                    }
                    if(tile.isKeyHole()){
                        if(tile.canAcceptKeys(keys)){
                            keys = tile.fillKeyHole(keys);
                        }
                    }
                    tile.trigger();
                }
            }
            if (tile.getType().slipery && slipperyDir != null) {
                createAction(slipperyDir);
            } else if (movingDir != MovementDirection.NONE) {
                createAction(movingDir);
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

    public void createAction(MovementDirection dir) {
        MoveByAction action = new MoveByAction();
        switch (dir) {
            case UP:
                action.setAmount(0, Map.TileSize);
                break;
            case DOWN:
                action.setAmount(0, -Map.TileSize);
                break;
            case LEFT:
                action.setAmount(-Map.TileSize, 0);
                break;
            case RIGHT:
                action.setAmount(Map.TileSize, 0);
                break;
        }
        Vector2 newpos = getCenterPoint().cpy().add(action.getAmountX(), action.getAmountY());
        Tile newtile = SGL.provide(GameScreen.class).getMap().getTileAt(newpos);
        if (newtile == null ) {//|| !newtile.canAccess(dir.flag())) {
            // Cant move there
            return;
        }
        SGL.provide(GameScreen.class).nextMessage();
        action.setDuration(speed);
        addAction(action);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        getActor(ACTOR_BASE).draw(batch, parentAlpha);
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
