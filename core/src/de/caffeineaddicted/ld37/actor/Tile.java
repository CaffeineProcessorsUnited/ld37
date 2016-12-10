package de.caffeineaddicted.ld37.actor;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.utils.Align;
import de.caffeineaddicted.ld37.screen.GameScreen;
import de.caffeineaddicted.sgl.SGL;
import de.caffeineaddicted.sgl.etities.Entity;
import de.caffeineaddicted.sgl.ui.interfaces.Creatable;
import de.caffeineaddicted.sgl.ui.interfaces.Mortal;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Tile extends Entity implements Mortal, Creatable {
    private Tile.Type type;
    private int stepsLeft;
    private boolean hasKey = false;
    private boolean created = false;
    private MoveToAction mta;

    private Vector2 start, end;
    private boolean dieing = false;

    private boolean dead = false;

    public Tile(Tile.Type type, Vector2 start) {
        this(type, false, start, null);
    }

    public Tile(Tile.Type type, boolean hasKey, Vector2 start) {
        this(type, hasKey, start, null);
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
        if (end != null) {
            mta = new MoveToAction();
            mta.setAlignment(Align.center);
            mta.setReverse(false);
            addAction(mta);
        }
    }

    @Override
    public void act(float delta) {
        if (!isCreated()) {
            create();
        }
        super.act(delta);
        if (!dead && end != null && !hasActions()) {
            mta.setReverse(!mta.isReverse());
            mta.restart();
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
        if (type.durability > 0) {
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
        Vector2 pos = SGL.provide(GameScreen.class).getMap().calPixCoord(start);
        setCenterPosition(pos.x, pos.y);
        setTexture();
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
        addTexture(type.assets[a]);
    }

    public enum Type {

        Empty(0, false, "tile_empty.png"),
        Stone(2, false, "stonebroke.png", "stonehalf.png", "stone.png"),
        Ice(1, true, "icebroke.png", "ice.png");

        public final int durability;
        public final boolean slipery;
        public final String[] assets;

        Type(int durability, boolean slippery, String... assets) {
            this.durability = durability;
            this.slipery = slippery;
            this.assets = assets;
        }

        public static Type getTypeByName(String name){
            for(Type type: values()){
                if(type.name().equals(name)){
                    return type;
                }
            }
            return null;
        }
    }
}
