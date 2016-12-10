package de.caffeineaddicted.ld37.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import de.caffeineaddicted.ld37.screen.GameScreen;
import de.caffeineaddicted.sgl.SGL;

public class UnitPlayer extends UnitBase {
    final private float speed = 64;
    private int collectedKeys;
    private String ACTOR_BASE;
    private boolean moving;
    private MovementDirection movingDir;
    private Tile currentTile;

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
        if (!moving) {
            movingDir = dir;
            moving = true;
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
        boolean onNextBlock = false;
        if (tile != currentTile) {
            onNextBlock = true;
        }

        if (tile != null) {
            tile.walkOver();
        }

        if (tile == null || tile.isDead()) {
            onDie();
            SGL.error("U DEAD");
            return;
        }

        if (tile.hasKey()) {
            tile.takeKey();
            collectKey();
        }

        if (moving) {
            if (movingDir == MovementDirection.LEFT) {
                moveBy(-speed * delta, 0);
            } else if (movingDir == MovementDirection.RIGHT) {
                moveBy(speed * delta, 0);
            } else if (movingDir == MovementDirection.UP) {
                moveBy(0, speed * delta);
            } else if (movingDir == MovementDirection.DOWN) {
                moveBy(0, -speed * delta);
            }

            boolean slippery = tile.getType().slipery;
            if (!slippery && onNextBlock) {
                moving = false;
            }
        }

        currentTile = tile;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        getActor(ACTOR_BASE).draw(batch, parentAlpha);
    }

    public enum MovementDirection {
        LEFT,
        RIGHT,
        UP,
        DOWN
    }
}
