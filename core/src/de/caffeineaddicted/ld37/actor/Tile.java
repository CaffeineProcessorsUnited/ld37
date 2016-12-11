package de.caffeineaddicted.ld37.actor;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import de.caffeineaddicted.ld37.screen.GameScreen;
import de.caffeineaddicted.sgl.SGL;
import de.caffeineaddicted.sgl.etities.Entity;
import de.caffeineaddicted.sgl.ui.interfaces.Creatable;
import de.caffeineaddicted.sgl.ui.interfaces.Mortal;

import java.util.Arrays;

import static de.caffeineaddicted.ld37.actor.Key.KEY_GOLD;
import static de.caffeineaddicted.ld37.actor.Key.KEY_GREEN;
import static de.caffeineaddicted.ld37.actor.Key.KEY_PINK;

public class Tile extends Entity implements Mortal, Creatable {
    public final static int ACCESS_NONE = 0;
    public final static int ACCESS_LEFT = 1;
    public final static int ACCESS_RIGHT = 2;
    public final static int ACCESS_UP = 4;
    public final static int ACCESS_DOWN = 8;
    public final static int ACCESS_HORIZONTAL = ACCESS_LEFT + ACCESS_RIGHT;
    public final static int ACCESS_VERTICAL = ACCESS_UP + ACCESS_DOWN;
    public final static int ACCESS_ALL = ACCESS_LEFT + ACCESS_RIGHT + ACCESS_UP + ACCESS_DOWN;
    private Tile.Type type;
    private int stepsLeft;
    private int key = 0;
    private boolean created = false;
    private Vector2 move;
    private Vector2 moveLeft;
    private boolean moveReversed = false;
    private Vector2 start, end;
    private boolean dieing = false;
    private boolean dead = false;

    private String trigger;
    private boolean triggered;

    public Tile(Tile.Type type) {
        this.type = type;
        stepsLeft = type.durability;
        key = 0;
        zindex(GameScreen.ZINDEX.Tile.idx);
    }

    public void setKey(int key) {
        this.key = key;
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

    public void setStart(Vector2 start) {
        this.start = start;
    }

    public Vector2 getEnd() {
        return end;
    }

    public void setEnd(Vector2 end) {
        this.end = end;
    }

    @Override
    public boolean isDead() {
        return dead;
    }

    public boolean hasKey() {
        return key > 0;
    }

    public boolean hasKey(int type) {
        return type == key;
    }

    public int getKeyType() {
        return key;
    }

    public int takeKey() {
        int k = key;
        key = 0;
        setTexture();
        return k;
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
        SGL.debug("HEY?");
        if (type.durability > 0) {
            if (--stepsLeft < 1) {
                dieing = true;
            }
        }
        if (!(type.durability > 0 && stepsLeft < 1)) {
            setTexture();
        }

        if (!isTriggered()) {
            if (!trigger.isEmpty()) {
                triggerAction(trigger);
            }
            setTriggered(true);
        }
    }

    private void triggerAction(String action) {
        String[] actionList = action.split(":");
        String actionCall = actionList[0];
        String[] actionParam = Arrays.copyOfRange(actionList, 1, actionList.length);

        TileTriggerActions.call(actionCall, actionParam);
    }

    private void triggerMessage(String message) {
    }

    public boolean canAccess(int direction) {
        return (type.access & direction) == direction;
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
        if (a >= type.assets.length) {
            a = 0;
        }
        String Texture = addTexture(type.assets[a]);
        if (hasKey()) {
            switch (getKeyType()) {
                case KEY_GOLD:
                    addActor(new Key("keys/keygold.png"));
                    break;
                case KEY_PINK:
                    addActor(new Key("keys/keypink.png"));
                    break;
                case KEY_GREEN:
                    addActor(new Key("keys/keygreen.png"));
                    break;
            }
        }
    }

    public void setType(Type type) {
        this.type = type;
        stepsLeft = type.durability;
        setTexture();
    }

    @Override
    public String toString() {
        return "Tile(" + getX(Align.center) + "," + getY(Align.center) + ")";
    }

    public boolean isTriggered() {
        return triggered;
    }

    public void setTriggered(boolean triggered) {
        this.triggered = triggered;
    }

    public String getTrigger() {
        return trigger;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

    public enum Type {

        Empty(0, false, 0, ACCESS_ALL, Key.KEY_NONE, "tiles/tile_empty.png"),
        Stone(2, false, 4, ACCESS_ALL, Key.KEY_NONE, "tiles/stonebroke.png", "tiles/stonehalf.png", "tiles/stone.png"),
        Ice(1, true, 2, ACCESS_ALL, Key.KEY_NONE, "tiles/icebroke.png", "tiles/ice.png"),
        // TODO: Parameter
        Wall(1, true, 2, ACCESS_NONE, Key.KEY_NONE, "tiles/icebroke.png", "tiles/ice.png"),
        HPlank(0, false, 2, ACCESS_HORIZONTAL, Key.KEY_NONE, "tiles/woodplankhorizontal.png"),
        VPlank(0, false, 2, ACCESS_VERTICAL, Key.KEY_NONE, "tiles/woodplankvertical.png"),
        // TODO: Parameter
        DoorPink(0, false, 2, ACCESS_VERTICAL, Key.KEY_PINK, "tiles/metal.png"),
        // TODO: Parameter
        DoorGold(0, false, 2, ACCESS_VERTICAL, KEY_GOLD, "tiles/metal.png"),
        // TODO: Parameter
        DoorGreen(0, false, 2, ACCESS_VERTICAL, Key.KEY_GREEN, "tiles/metal.png"),
        Metal(0, false, 2, ACCESS_ALL, Key.KEY_NONE, "tiles/metal.png");

        public final int durability;
        public final boolean slipery;
        public final float speed;
        public final int access;
        public final String[] assets;

        Type(int durability, boolean slippery, float speed, int access, int keys, String... assets) {
            this.durability = durability;
            this.slipery = slippery;
            this.speed = speed;
            this.access = access;
            this.assets = assets;
        }

        public static Type getTypeByName(String name) {
            for (Type type : values()) {
                if (type.name().equalsIgnoreCase(name)) {
                    return type;
                }
            }
            return null;
        }
    }

    public static class TileTriggerActions {
        public static void call(String name, String[] params){
            if(name.equalsIgnoreCase("message")){
                Message(params);
            } else if(name.equalsIgnoreCase("replace")){
                Replace(params);
            }
        }
        public static void Message(String[] params){
            SGL.provide(GameScreen.class).showMessage(String.join(":", params));
        }

        public static void Replace(String[] params){
            for(int i = 0; i+2 < params.length; i+=3){
                int x = Integer.parseInt(params[i+0]);
                int y = Integer.parseInt(params[i+1]);
                String t = params[i+2];

                int idx = x*SGL.provide(GameScreen.class).getMap().height+y;
                Tile tile = SGL.provide(GameScreen.class).getMap().getFloor()[idx];
                if(tile != null){
                    tile.setType(Tile.Type.getTypeByName(t));
                }
            }
        }
    }
}
