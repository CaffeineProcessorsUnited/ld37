package de.caffeineaddicted.ld37prep.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import de.caffeineaddicted.ld37prep.screen.GameScreen;
import de.caffeineaddicted.sgl.SGL;

public class UnitPlayer extends UnitBase {
    private int collectedKeys;
    private String ACTOR_BASE;
    private boolean moving;
    private MovementDirection movingDir;
    final private float speed = 5;
    private Tile currentTile;

    public UnitPlayer() {
        ACTOR_BASE = addTexture("player.png");
        setWidth(getActor(ACTOR_BASE).getWidth());
        setHeight(getActor(ACTOR_BASE).getHeight());

        collectedKeys = 0;

        update();
    }

    public void move(MovementDirection dir){
        if(!moving){
            movingDir = dir;
            moving = true;
        }
    }

    public void collectKey(){
        collectedKeys += 1;
    }

    public boolean useKeys(int numKeys){
        if(numKeys <= collectedKeys){
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

        Tile tile = SGL.provide(Map.class).getTileAt(getCenterPoint().x, getCenterPoint().y);
        boolean onNextBlock = false;
        if(tile != currentTile){
            onNextBlock = true;
        }

        if(tile != null && onNextBlock){
            tile.walkOver();
        }

        if( tile == null || !tile.isAlive()){
            onDie();
            return;
        }

        if(tile.hasKey){
            tile.takeKey();
            collectKey();
        }

        if(moving) {
            if (movingDir == MovementDirection.LEFT) {
                moveBy(-speed*delta, 0);
            } else if (movingDir == MovementDirection.RIGHT) {
                moveBy(speed*delta, 0);
            } else if (movingDir == MovementDirection.UP) {
                moveBy(0, -speed*delta);
            } else if (movingDir == MovementDirection.DOWN) {
                moveBy(0, speed*delta);
            }

            boolean slippery = tile.isSlippery();
            if(!slippery && onNextBlock){
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
