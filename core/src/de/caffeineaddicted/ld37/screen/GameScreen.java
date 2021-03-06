package de.caffeineaddicted.ld37.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import de.caffeineaddicted.ld37.LD37;
import de.caffeineaddicted.ld37.actor.*;
import de.caffeineaddicted.ld37.input.GameInputProcessor;
import de.caffeineaddicted.ld37.message.FireEverythingMessage;
import de.caffeineaddicted.sgl.SGL;
import de.caffeineaddicted.sgl.etities.Actor;
import de.caffeineaddicted.sgl.input.SGLScreenInputMultiplexer;
import de.caffeineaddicted.sgl.messages.Message;
import de.caffeineaddicted.sgl.messages.MessageReceiver;
import de.caffeineaddicted.sgl.ui.screens.SGLRootScreen;
import de.caffeineaddicted.sgl.ui.screens.SGLStagedScreen;
import de.caffeineaddicted.sgl.utils.MathUtils;
import de.caffeineaddicted.sgl.utils.SGLAssets;

import java.util.ArrayList;

/**
 * @author Malte Heinzelmann
 */
public class GameScreen extends SGLStagedScreen<LD37> {

    public ArrayList<Actor> deleteLater = new ArrayList<Actor>();
    private Player player;
    private Map map;
    private MessageList messages;
    private int fade = 0;
    private float timer = 0, fadeDuration = 0.5f, fadeAlpha = 0, fadeAction = 0;
    private HUD hud;
    private boolean dead = false;
    private int currentMap;
    private float cameraBaseSpeed = 256;
    private boolean cameraMovement = false;
    private TextureRegionDrawable unicornFallingDrawable, unicornClimbingDrawable, unicordLadderDrawable;
    private float unicornFallingDuration = 0.4f, unicornFalling = 0, unicornFallingX, unicornFallingScale, unicornFallingRotation;
    private float unicornClimbingDuration = 1.2f, unicornClimbing = 0, unicornClimbingX, unicornClimbingScale;
    private boolean useCustomMaps = false;
    private boolean hardMode = true;
    private boolean loaded = false;

    public void onBeforeAct(float delta) {
        for (Actor a : deleteLater) {
            stage().removeActor(a);
        }
        if (fade == 1) {
            timer += delta;
            fadeAlpha = Math.min(timer / fadeDuration, 1f);
            if (timer >= fadeDuration) {
                timer = 0;
                fade++;
                if (fadeAction == 1) {
                    loadPreviousMap(isHardMode() ? 1 : 0);
                    if (isHardMode()) {
                        showMessage("You fell down and landed 1 level below...");
                    } else {
                        showMessage("You fell down...\nLuckily you could hold on to a cloud and return to the start.");
                    }
                    unicornFallingX = MathUtils.random((int) (getViewWidth() / 100) * 20, (int) (getViewWidth() - (getViewWidth() / 100) * 20));
                    unicornFallingScale = (float) MathUtils.random(200, 400) / 100;
                    unicornFallingRotation = (float) MathUtils.random(40, 180) * ((MathUtils.random(0, 1) == 0 ? 1 : -1));
                } else if (fadeAction == 2) {
                    loadNextMap();
                    showMessage("You climb the rainbow and arrive at the next level.");
                    unicornClimbingX = MathUtils.random((int) (getViewWidth() / 100) * 20, (int) (getViewWidth() - (getViewWidth() / 100) * 20));
                    unicornClimbingScale = MathUtils.random(200, 400) / 100;
                }
            }
        } else if (fade == 2) {
            timer += delta;
            fadeAlpha = 1;
            if (fadeAction == 1) {
                unicornFalling = Math.min(timer / unicornFallingDuration, 1f);
                if (timer >= unicornFallingDuration) {
                    unicornFalling = 0;
                    timer = 0;
                    fade++;
                }
            } else if (fadeAction == 2) {
                unicornClimbing = Math.min(timer / unicornClimbingDuration, 1f);
                if (timer >= unicornClimbingDuration) {
                    unicornClimbing = 0;
                    timer = 0;
                    fade++;
                }
            }
        } else if (fade == 3) {
            timer += delta;
            fadeAlpha = 1 - Math.min(timer / fadeDuration, 1f);
            if (timer >= fadeDuration) {
                timer = 0;
                fade = 0;
                dead = false;
            }
        }
        if (!dead) {
            if (player != null) {
                player.act(delta);
            }
            if (map != null) {
                map.act(delta);
            }
            if (messages != null) {
                messages.act(delta);
            }
            if (hud != null) {
                hud.act(delta);
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        dirty();
    }

    @Override
    public void onAfterAct(float delta) {
        // center player
        if (player.hasActions() || cameraMovement) {
            cameraMovement = true;
            float dX = getViewWidth() / 2 - player.getX();
            float dY = getViewHeight() / 2 - player.getY();
            moveMapBy(Math.signum(dX) * Math.min(Math.abs(dX), Math.abs(cameraBaseSpeed) * delta), Math.signum(dY) * Math.min(Math.abs(dY), Math.abs(cameraBaseSpeed) * delta));
            if (Math.abs(dX) < 1 && Math.abs(dY) < 1) {
                cameraMovement = false;
            }
        }
    }

    @Override
    public void onBeforeDraw() {
        SGL.provide(SpriteBatch.class).begin();
        map.draw(SGL.provide(SpriteBatch.class), 1f);
        player.draw(SGL.provide(SpriteBatch.class), 1f);
        messages.draw(SGL.provide(SpriteBatch.class), 1f);
        hud.draw(SGL.provide(SpriteBatch.class), 1f);
        SGL.provide(SpriteBatch.class).end();
        Gdx.gl.glEnable(GL20.GL_BLEND);
    }

    @Override
    public void onAfterDraw() {
        if (fade > 0) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            SGL.provide(ShapeRenderer.class).setProjectionMatrix(camera.combined);
            SGL.provide(ShapeRenderer.class).begin(ShapeRenderer.ShapeType.Filled);
            SGL.provide(ShapeRenderer.class).setColor(0, 0, 0, fadeAlpha);
            SGL.provide(ShapeRenderer.class).rect(0, 0, getViewWidth(), getViewHeight());
            SGL.provide(ShapeRenderer.class).end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
        SGL.provide(SpriteBatch.class).begin();
        if (fade == 2) {
            if (unicornFalling > 0) {
                SGL.provide(SpriteBatch.class).setColor(1, 1, 1, 1);
                unicornFallingDrawable.draw(
                        SGL.provide(SpriteBatch.class),
                        unicornFallingX,
                        getViewHeight() * (1 - unicornFalling),
                        unicornFallingDrawable.getMinWidth() / 2,
                        unicornFallingDrawable.getMinHeight() / 2,
                        unicornFallingDrawable.getMinWidth(),
                        unicornFallingDrawable.getMinHeight(),
                        unicornFallingScale,
                        unicornFallingScale,
                        -unicornFallingRotation * unicornFalling);
            }
            if (unicornClimbing > 0) {
                SGL.provide(SpriteBatch.class).setColor(1, 1, 1, 0.6f);
                unicordLadderDrawable.draw(
                        SGL.provide(SpriteBatch.class),
                        unicornClimbingX - unicornClimbingDrawable.getMinWidth() / 2,
                        0,
                        unicordLadderDrawable.getMinWidth() / 2,
                        unicordLadderDrawable.getMinHeight() / 2,
                        unicornClimbingDrawable.getMinWidth(),
                        getViewHeight(),
                        unicornClimbingScale,
                        unicornClimbingScale,
                        0f);
                unicordLadderDrawable.draw(
                        SGL.provide(SpriteBatch.class),
                        unicornClimbingX + unicornClimbingDrawable.getMinWidth() / 2,
                        0,
                        unicordLadderDrawable.getMinWidth() / 2,
                        unicordLadderDrawable.getMinHeight() / 2,
                        unicornClimbingDrawable.getMinWidth(),
                        getViewHeight(),
                        unicornClimbingScale,
                        unicornClimbingScale,
                        0f);
                SGL.provide(SpriteBatch.class).setColor(1, 1, 1, 1);
                unicornClimbingDrawable.draw(
                        SGL.provide(SpriteBatch.class),
                        unicornClimbingX,
                        getViewHeight() * unicornClimbing,
                        unicornClimbingDrawable.getMinWidth() / 2,
                        unicornClimbingDrawable.getMinHeight() / 2,
                        unicornClimbingDrawable.getMinWidth(),
                        unicornClimbingDrawable.getMinHeight(),
                        unicornClimbingScale,
                        unicornClimbingScale,
                        0f);
            }
        }
        SGL.provide(SpriteBatch.class).end();
    }

    @Override
    public void onBeauty() {
        hud.setPosition(0, getViewHeight() - hud.getHeight());
        centerMap();
    }

    @Override
    public void dispose() {

    }

    @Override
    public void onDispose() {

    }

    @Override
    public void onCreate() {
        SGL.provide(SGLScreenInputMultiplexer.class).addProcessor(this, new GameInputProcessor());
        loadMap(5);
        messages = new MessageList();
        hud = new HUD();

        SGL.registerMessageReceiver(FireEverythingMessage.class, new MessageReceiver() {
            @Override
            public void receiveMessage(Message message) {

            }
        });
        unicornFallingDrawable = new TextureRegionDrawable(new TextureRegion(SGL.provide(SGLAssets.class).get("player/unicornfalling.png", Texture.class)));
        unicornClimbingDrawable = new TextureRegionDrawable(new TextureRegion(SGL.provide(SGLAssets.class).get("player/unicornclimbing.png", Texture.class)));
        unicordLadderDrawable = new TextureRegionDrawable(new TextureRegion(SGL.provide(SGLAssets.class).get("player/ladder.png", Texture.class)));
    }

    public void reloadMap() {
        loadMap(currentMap);
    }

    public void loadMap(int i) {
        loaded = false;
        currentMap = Math.max(-1, i);
        player = new Player();
        map = null;
        String fileName = "maps/" + currentMap + ".json";
        if (useCustomMaps && currentMap >= 0) {
            FileHandle file = Gdx.files.local("./" + fileName);
            if (file.exists() && file.file().canRead()) {
                try {
                    map = new MapWrapper(file).getMap();
                } catch (Exception e) {
                    SGL.error("Could not load the custom map in \"./" + fileName + "\"! Using default maps.");
                }
            }
        } else {
            if (SGL.provide(SGLAssets.class).isLoaded(fileName, MapWrapper.class)) {
                map = SGL.provide(SGLAssets.class).get(fileName, MapWrapper.class).getMap();
            }
        }
        if (map == null) {
            //loaded is false
            SGL.provide(SGLRootScreen.class).hideScreen(GameScreen.class);
            SGL.provide(SGLRootScreen.class).showScreen(EndGameScreen.class, SGLRootScreen.ZINDEX.NEAREST);
            return;
        }
        loaded = true;
        map.reset();
        Vector2 spawn = map.calPixCoord(map.getStart());
        player.setPosition(map.getX() + spawn.x, map.getY() + spawn.y);
        reset();
    }

    private void reset() {
        if (messages != null) {
            messages.clear();
        }
        dead = false;
        centerMap();
    }

    @Override
    public void onHide() {

    }

    @Override
    public void onShow() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {
    }

    public void loseGame() {
        dead = true;
        fade = 1;
        timer = 0;
        fadeAction = 1;
    }

    public void winGame() {
        dead = true;
        fade = 1;
        timer = 0;
        fadeAction = 2;
    }

    public void loadPreviousMap() {
        loadPreviousMap(1);
    }

    public void loadPreviousMap(int n) {
        loadMap(currentMap - n);
    }

    public void loadNextMap() {
        loadNextMap(1);
    }

    public void loadNextMap(int n) {
        loadMap(currentMap + n);
    }

    public void addActor(Actor actor) {
        stage().addActor(actor);
    }

    public Map getMap() {
        return map;
    }

    public int getNumActorsOfType(Class<? extends Actor> type) {
        int count = 0;
        for (com.badlogic.gdx.scenes.scene2d.Actor actor : stage().getActors()) {
            if (type.isInstance(actor)) {
                count++;
            }
        }
        return count;
    }

    public Player getPlayer() {
        return player;
    }

    public void drag(float x, float y) {
        if (player.hasActions()) {
            return;
        }
        if (x < 0) {
            if (map.getWidth() < getViewWidth()) {
                // map is smaller than view width
                if (map.getX() + x < 0 && map.getX() + map.getWidth() + x < getViewWidth()) {
                    x = -map.getX();
                }
            } else {
                // map is wider than view width
                if (map.getX() + map.getWidth() + x < getViewWidth()) {
                    // dont go left, right border ist at screen border
                    x = getViewWidth() - map.getWidth() - map.getX();
                }
            }
        } else if (x > 0) {
            if (map.getWidth() < getViewWidth()) {
                if (map.getX() + map.getWidth() + x > getViewWidth()) {
                    // dont go left, right border ist at screen border
                    x = getViewWidth() - map.getWidth() - map.getX();
                }
            } else {
                if (map.getX() + x > 0) {
                    x = -map.getX();
                }
            }
        }
        if (y < 0) {
            if (map.getHeight() < getViewHeight()) {
                if (map.getY() + y < 0 && map.getY() + map.getHeight() + y < getViewHeight()) {
                    y = -map.getY();
                }
            } else {
                // map is wider than view width
                if (map.getY() + map.getHeight() + y < getViewHeight()) {
                    // dont go left, right border ist at screen border
                    y = getViewHeight() - map.getHeight() - map.getY();
                }
            }
        } else if (y > 0) {
            if (map.getHeight() < getViewHeight()) {
                if (map.getY() + map.getHeight() + y > getViewHeight()) {
                    // dont go left, right border ist at screen border
                    y = getViewHeight() - map.getHeight() - map.getY();
                }
            } else {
                if (map.getY() + y > 0) {
                    y = -map.getY();
                }
            }
        }
        moveMapBy(x, y);
    }

    public void centerMap() {
        float dX = getViewWidth() / 2 - player.getX();
        float dY = getViewHeight() / 2 - player.getY();
        moveMapBy(dX, dY);
    }

    public void moveMapBy(float x, float y) {
        map.moveBy(x, y);
        player.moveBy(x, y);
        SGL.provide(BackgroundScreen.class).moveBy(x, y);
    }

    public void showMessage(String message) {
        messages.postMessage(message);
    }

    public boolean toggleUseCustomMaps() {
        return setUseCustomMaps(!getUseCustomMaps());
    }

    public boolean getUseCustomMaps() {
        return useCustomMaps;
    }

    public boolean setUseCustomMaps(boolean customMaps) {
        useCustomMaps = customMaps;
        return useCustomMaps;
    }

    public boolean toggleHardMode() {
        return setHardMode(!isHardMode());
    }

    public boolean isHardMode() {
        return hardMode;
    }

    public boolean setHardMode(boolean hardMode) {
        this.hardMode = hardMode;
        return hardMode;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public enum ZINDEX {
        Tile(10), Marker(15), KeyHole(20), Key(30), Player(40), Hud(50), Messages(100);

        public final int idx;

        ZINDEX(int idx) {
            this.idx = idx;
        }
    }
}
