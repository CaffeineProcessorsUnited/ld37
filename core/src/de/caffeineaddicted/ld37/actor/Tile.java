package de.caffeineaddicted.ld37.actor;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import de.caffeineaddicted.ld37.screen.GameScreen;
import de.caffeineaddicted.sgl.SGL;
import de.caffeineaddicted.sgl.etities.Entity;
import de.caffeineaddicted.sgl.ui.interfaces.Creatable;
import de.caffeineaddicted.sgl.ui.interfaces.Mortal;

import java.util.Arrays;

import static de.caffeineaddicted.ld37.actor.Key.KEY_GOLD;
import static de.caffeineaddicted.ld37.actor.Key.KEY_NONE;

public class Tile extends Entity implements Mortal, Creatable {
    public final static int ACCESS_NONE = 0;
    public final static int ACCESS_LEFT = 1;
    public final static int ACCESS_RIGHT = 2;
    public final static int ACCESS_UP = 4;
    public final static int ACCESS_DOWN = 8;
    public final static int ACCESS_HORIZONTAL = ACCESS_LEFT + ACCESS_RIGHT;
    public final static int ACCESS_VERTICAL = ACCESS_UP + ACCESS_DOWN;
    public final static int ACCESS_ALL = ACCESS_LEFT + ACCESS_RIGHT + ACCESS_UP + ACCESS_DOWN;

    public final static int TRIGGER_NONE = 0;
    public final static int TRIGGER_HINT = 1;
    public final static int TRIGGER_ACTION = 2;
    public final static int TRIGGER_ALL = TRIGGER_HINT + TRIGGER_ACTION;

    private Tile.Type type;
    private int stepsLeft;
    private int key = 0;
    private int keyHole = 0;
    private boolean created = false;
    private Vector2 start, end;
    private boolean dieing = false;
    private boolean dead = false;
    private String trigger;
    private boolean triggered;
    private int triggerflags;
    private int visitCounter = 0;

    private boolean justUnlocked = false;
    private boolean actionStarted = false;

    public Tile(Tile.Type type) {
        this.type = type;
        stepsLeft = type.durability;
        key = keyHole = 0;
        zindex(GameScreen.ZINDEX.Tile.idx);
    }

    public void setKey(int key) {
        this.key = key;
        if (isCreated()) {
            setTexture();
        }
    }

    @Override
    public void act(float delta) {
        if (!isCreated()) {
            create();
        }
        super.act(delta);
        if (end != null && !actionStarted) {
            actionStarted = true;
            addAction(Actions.forever(Actions.sequence(
                    Actions.moveBy(
                            (end.x - start.x) * Map.TileSize,
                            (end.y - start.y) * Map.TileSize,
                            2f),
                    Actions.moveBy(
                            (start.x - end.x) * Map.TileSize,
                            (start.y - end.y) * Map.TileSize,
                            2f)
            )));
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

    public void setType(Type type) {
        this.type = type;
        stepsLeft = type.durability;
        dieing = false;
        dead = false;
        setTexture();
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
        justUnlocked = false;
        if (type == Type.Exit) {
            SGL.provide(GameScreen.class).winGame();
        }
    }

    public void trigger() {
        if (!isTriggered()) {
            visitCounter++;
            if (!trigger.isEmpty()) {
                triggerAction(trigger);
            }
            setTriggered(true);
        }
    }

    private void triggerAction(String action) {

        String[] actionList;
        if (action.contains(";")) {
            actionList = action.split(";");
        } else {
            actionList = new String[]{action};
        }
        for (String a : actionList) {
            String[] _actionList = a.split(":");
            String type = _actionList[0];
            String[] actionParams = Arrays.copyOfRange(_actionList, 1, _actionList.length);

            TileTriggerActions.call(this, type, actionParams);
        }
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
        setTexture();
        sizeChanged();
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
        if (type != Type.Empty && hasTrigger(TRIGGER_HINT)) {
            addActor(new HintMarker());
        }
        if (type != Type.Empty && hasTrigger(TRIGGER_ACTION)) {
            addActor(new ActionMarker());
        }
        if (keyHole > 0) {
            addActor(new KeyHole(keyHole));
        }
        if (hasKey()) {
            addActor(new Key(getKeyType()));
        }
    }

    public boolean hasTrigger(int flags) {
        return (triggerflags & flags) == flags;
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
        // TODO: Catch all
        if (trigger.contains("message:")) {
            triggerflags += TRIGGER_HINT;
        }
        if (trigger.contains("teleport:") ||
                trigger.contains("replace:") ||
                trigger.contains("key:") ||
                trigger.contains("hole:")) {
            triggerflags += TRIGGER_ACTION;
        }
    }

    public boolean isKeyHole() {
        return keyHole > 0;
    }

    public void setKeyHole(int keyHole) {
        this.keyHole = keyHole;
    }

    public boolean canAcceptKeys(int keys) {
        return (keys & keyHole) == keyHole;
    }

    public boolean isJustUnlocked() {
        return justUnlocked;
    }

    public int fillKeyHole(int keys) {
        if (canAcceptKeys(keys)) {
            int oldKeys = keys & ~keyHole;
            keyHole = 0;
            justUnlocked = true;
            setTexture();
            return oldKeys;
        }
        return keys;
    }

    public int getVisitCounter() {
        return visitCounter;
    }

    public enum MODE {
        BLOCKING, FALLING
    }

    public enum Type {

        Empty(0, false, 0, ACCESS_NONE, MODE.FALLING, Key.KEY_NONE, "tiles/tile_empty.png"),
        Stone(2, false, 4, ACCESS_ALL, MODE.BLOCKING, Key.KEY_NONE, "tiles/stonebroke.png", "tiles/stonehalf.png", "tiles/stone.png"),
        DamagedStone(1, false, 4, ACCESS_ALL, MODE.BLOCKING, Key.KEY_NONE, "tiles/stonebroke.png", "tiles/stonehalf.png"),
        Ice(1, true, 2, ACCESS_ALL, MODE.BLOCKING, Key.KEY_NONE, "tiles/icebroke.png", "tiles/ice.png"),

        HPlank(0, false, 2, ACCESS_HORIZONTAL, MODE.FALLING, Key.KEY_NONE, "tiles/woodplankhorizontal.png"),
        VPlank(0, false, 2, ACCESS_VERTICAL, MODE.FALLING, Key.KEY_NONE, "tiles/woodplankvertical.png"),
        LPlank(0, false, 2, ACCESS_LEFT, MODE.FALLING, Key.KEY_NONE, "tiles/woodplankleft.png"),
        RPlank(0, false, 2, ACCESS_RIGHT, MODE.FALLING, Key.KEY_NONE, "tiles/woodplankright.png"),
        UPlank(0, false, 2, ACCESS_UP, MODE.FALLING, Key.KEY_NONE, "tiles/woodplankup.png"),
        DPlank(0, false, 2, ACCESS_DOWN, MODE.FALLING, Key.KEY_NONE, "tiles/woodplankdown.png"),
        // TODO: Parameter
        DoorPink(0, false, 2, ACCESS_ALL, MODE.BLOCKING, Key.KEY_PINK, "tiles/metal.png"),
        // TODO: Parameter
        DoorGold(0, false, 2, ACCESS_ALL, MODE.BLOCKING, KEY_GOLD, "tiles/metal.png"),
        // TODO: Parameter
        DoorGreen(0, false, 2, ACCESS_ALL, MODE.BLOCKING, Key.KEY_GREEN, "tiles/metal.png"),
        Metal(0, false, 2, ACCESS_ALL, MODE.BLOCKING, Key.KEY_NONE, "tiles/metal.png"),

        Wall(0, false, 2, ACCESS_NONE, MODE.BLOCKING, Key.KEY_NONE, "walls/wall.png"),
        WallL(0, false, 2, ACCESS_NONE, MODE.BLOCKING, Key.KEY_NONE, "walls/wallleft.png"),
        WallR(0, false, 2, ACCESS_NONE, MODE.BLOCKING, Key.KEY_NONE, "walls/wallright.png"),
        WallU(0, false, 2, ACCESS_NONE, MODE.BLOCKING, Key.KEY_NONE, "walls/wallup.png"),
        WallD(0, false, 2, ACCESS_NONE, MODE.BLOCKING, Key.KEY_NONE, "walls/walldown.png"),
        WallLU(0, false, 2, ACCESS_NONE, MODE.BLOCKING, Key.KEY_NONE, "walls/wallcornerupperleft.png"),
        WallRU(0, false, 2, ACCESS_NONE, MODE.BLOCKING, Key.KEY_NONE, "walls/wallcornerupperright.png"),
        WallLD(0, false, 2, ACCESS_NONE, MODE.BLOCKING, Key.KEY_NONE, "walls/wallcornerlowerleft.png"),
        WallRD(0, false, 2, ACCESS_NONE, MODE.BLOCKING, Key.KEY_NONE, "walls/wallcornerlowerright.png"),

        Entry(0, false, 0, ACCESS_ALL, MODE.BLOCKING, KEY_NONE, "tiles/entry.png"),
        Exit(0, false, 0, ACCESS_ALL, MODE.BLOCKING, KEY_NONE, "tiles/exit.png"),;

        public final int durability;
        public final boolean slipery;
        public final float speed;
        public final int access;
        public final MODE mode;
        public final String[] assets;

        Type(int durability, boolean slippery, float speed, int access, MODE mode, int keys, String... assets) {
            this.durability = durability;
            this.slipery = slippery;
            this.speed = speed;
            this.access = access;
            this.mode = mode;
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

        public static void call(Tile tile, String name, String[] params) {
            if (name.equalsIgnoreCase("message")) {
                Message(params);
            } else if (name.equalsIgnoreCase("replace")) {
                Replace(params);
            } else if (name.equalsIgnoreCase("unlock")) {
                Unlock(tile, params);
            } else if (name.equalsIgnoreCase("key")) {
                Key(params);
            } else if (name.equalsIgnoreCase("hole")) {
                KeyHole(params);
            } else if (name.equalsIgnoreCase("teleport")) {
                Teleport(params);
            } else if (name.equalsIgnoreCase("visit")) {
                Visit(tile, params);
            } else if (name.equalsIgnoreCase("delay")) {
                Delay(tile, params);
            }
        }

        public static void Message(String[] params) {
            SGL.provide(GameScreen.class).showMessage(String.join(":", params));
        }

        public static void Replace(String[] params) {
            for (int i = 0; i + 2 < params.length; i += 3) {
                int x = Integer.parseInt(params[i + 0]);
                int y = Integer.parseInt(params[i + 1]);
                String t = params[i + 2];

                Tile tile = SGL.provide(GameScreen.class).getMap().getTile(x, y);
                if (tile != null) {
                    tile.setType(Tile.Type.getTypeByName(t));
                }
            }
        }

        public static void Unlock(Tile tile, String[] params) {
            if (tile.isJustUnlocked()) {
                String[] data = Arrays.copyOfRange(params, 1, params.length);
                call(tile, params[0], data);
            }
        }

        public static void Key(String[] params) {
            for (int i = 0; i + 2 < params.length; i += 3) {
                int x = Integer.parseInt(params[i + 0]);
                int y = Integer.parseInt(params[i + 1]);
                int k = Integer.parseInt(params[i + 2]);

                int idx = x * SGL.provide(GameScreen.class).getMap().height + y;
                Tile tile = SGL.provide(GameScreen.class).getMap().getFloor()[idx];
                if (tile != null) {
                    tile.setKey(k);
                }
            }
        }

        public static void KeyHole(String[] params) {
            for (int i = 0; i + 2 < params.length; i += 3) {
                int x = Integer.parseInt(params[i + 0]);
                int y = Integer.parseInt(params[i + 1]);
                int k = Integer.parseInt(params[i + 2]);

                int idx = x * SGL.provide(GameScreen.class).getMap().height + y;
                Tile ttile = SGL.provide(GameScreen.class).getMap().getFloor()[idx];
                if (ttile != null) {
                    ttile.setKeyHole(k);
                }
            }
        }

        public static void Teleport(String[] params) {
            int x = Integer.parseInt(params[0]);
            int y = Integer.parseInt(params[1]);

            SGL.provide(GameScreen.class).getPlayer().teleport(x + 1, y + 1); //Walls (todo: moving tiles)
        }

        public static void Visit(Tile tile, String[] params) {
            int count = Integer.parseInt(params[0]);
            if (tile.getVisitCounter() == count) {
                String[] data = Arrays.copyOfRange(params, 2, params.length);
                call(tile, params[1], data);
            }
        }

        public static void Delay(Tile tile, String[] params) {
            int count = Integer.parseInt(params[0]);
            new Thread(new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(1000 * count);
                    } catch (InterruptedException e) {
                    }
                    Vector2 pos = SGL.provide(GameScreen.class).getPlayer().getCenterPoint();
                    Tile ttile = SGL.provide(GameScreen.class).getMap().getTileAt(pos.x, pos.y);
                    if (tile == ttile) {
                        String[] data = Arrays.copyOfRange(params, 2, params.length);
                        call(tile, params[1], data);
                    }
                }
            }).start();
        }
    }
}
