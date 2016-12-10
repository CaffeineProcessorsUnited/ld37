package de.caffeineaddicted.ld37prep.actor;

import com.badlogic.gdx.math.Vector2;
import de.caffeineaddicted.ld37prep.action.MoveToAction;
import de.caffeineaddicted.ld37prep.screen.GameScreen;
import de.caffeineaddicted.sgl.SGL;
import de.caffeineaddicted.sgl.etities.Entity;
import de.caffeineaddicted.sgl.ui.interfaces.Mortal;

abstract public class Tile extends Entity implements Mortal {
    private Tile.Type type;
    private float stepsLeft;
    private boolean hasKey = false;
    private Vector2 start, end;
    private MoveToAction mta;
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
        setCenterPosition(SGL.provide(GameScreen.class).getMap().g2s(start));
        this.end = end;
        if (end != null) {
            mta = new MoveToAction();
            mta.setUpdater(new MoveToAction.Updater() {
                @Override
                public void update(MoveToAction action, float percent) {
                    action.getTarget().setCenterPosition(action.startX + (action.endX - action.startX) * percent, action.startY + (action.endY - action.startY) * percent);
                }
            });
            mta.setReverse(false);
            addAction(mta);
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (!dead && end != null && !hasActions()) {
            mta.setReverse(!mta.isReverse());
            mta.restart();
        }
        if (dieing) {
            if (!getCenterPoint().equals(SGL.provide(GameScreen.class).getPlayer().getCenterPoint())) {
                // Player left tile. We can now die in peace.
                dieing = false;
                dead = true;
            }
        }
    }

    public void onDie() {

    }

    public void walkOver() {
        if (type.durability > 0) {
            if (--stepsLeft < 0) {
                dieing = true;
            }
        }
    }

    public enum Type {

        Empty(0, false, "tile_empty.png");

        public final float durability;
        public final boolean slipery;
        public final String[] assets;

        Type(float durability, boolean slippery, String... assets) {
            this.durability = durability;
            this.slipery = slippery;
            this.assets = assets;
        }
    }
}
