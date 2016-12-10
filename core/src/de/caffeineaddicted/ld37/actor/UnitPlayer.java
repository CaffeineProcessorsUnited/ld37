package de.caffeineaddicted.ld37.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import de.caffeineaddicted.ld37.screen.GameScreen;
import de.caffeineaddicted.sgl.SGL;

public class UnitPlayer extends UnitBase {
    final private float speed = 1;
    private int collectedKeys;
    private String ACTOR_BASE;
    private MovementDirection movingDir = MovementDirection.NONE, slipperyDir;
    private Tile currentTile;
    private boolean newTile = true;

    public UnitPlayer() {
        ACTOR_BASE = addTexture("player.png");
        getActor(ACTOR_BASE).setWidth(64);
        getActor(ACTOR_BASE).setHeight(64);
        setWidth(getActor(ACTOR_BASE).getWidth());
        setHeight(getActor(ACTOR_BASE).getHeight());

        collectedKeys = 0;

        update();
    }

    public void move(MovementDirection dir) {
        if (!hasActions()) {
            movingDir = dir;
        }
    }

    public void collectKey() {
        collectedKeys += 1;
    }

    public boolean useKeys(int numKeys) {
        if (numKeys <= collectedKeys) {
            collectedKeys -= numKeys;
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

        if (!hasActions()) {
            if (newTile) {
                newTile = false;
                if (tile != null) {
                    tile.walkOver();
                }
                if (tile != null && tile.hasKey()) {
                    tile.takeKey();
                    collectKey();
                }
            }
            if (tile == null || tile.isDead()) {
                onDie();
                SGL.error("U DEAD");
                return;
            }
            if (tile != null && tile.getType().slipery && slipperyDir != null) {
                addAction(createAction(slipperyDir));
            }
            if (movingDir != MovementDirection.NONE) {
                addAction(createAction(movingDir));
                slipperyDir = movingDir;
                movingDir = MovementDirection.NONE;
            }
        }
        if (currentTile != tile) {
            newTile = true;
        }
        currentTile = tile;
    }

    public Action createAction(MovementDirection dir) {
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
        action.setDuration(speed);
        return action;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        getActor(ACTOR_BASE).draw(batch, parentAlpha);
    }

    public enum MovementDirection {
        NONE,
        LEFT,
        RIGHT,
        UP,
        DOWN
    }
}
