package de.caffeineaddicted.ld37.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.Align;
import de.caffeineaddicted.ld37.LD37;
import de.caffeineaddicted.ld37.actor.HUD;
import de.caffeineaddicted.ld37.actor.Map;
import de.caffeineaddicted.ld37.actor.MapWrapper;
import de.caffeineaddicted.ld37.actor.Player;
import de.caffeineaddicted.ld37.input.GameInputProcessor;
import de.caffeineaddicted.ld37.message.FireEverythingMessage;
import de.caffeineaddicted.sgl.SGL;
import de.caffeineaddicted.sgl.etities.Actor;
import de.caffeineaddicted.sgl.input.SGLScreenInputMultiplexer;
import de.caffeineaddicted.sgl.messages.Message;
import de.caffeineaddicted.sgl.messages.MessageReceiver;
import de.caffeineaddicted.sgl.ui.screens.SGLStagedScreen;
import de.caffeineaddicted.sgl.utils.SGLAssets;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Malte Heinzelmann
 */
public class GameScreen extends SGLStagedScreen<LD37> {

    public ArrayList<Actor> deleteLater = new ArrayList<Actor>();
    private Player player;
    private Map map;
    private Queue<Label> messageQueue = new LinkedBlockingQueue<Label>();
    private Drawable speechBackground;
    private float speechPadding = 10;
    private int fade = 0;
    private float timer = 0, fadeDuration = 1, fadeAlpha = 0, fadeAction = 0;
    private HUD hud;
    private boolean dead = false;
    private int currentMap;
    private float cameraBaseSpeed = 2, cameraSpeedX, cameraSpeedY;
    private boolean cameraMovement = false;


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
                    int n = 1;
                    loadPreviousMap(n);
                    showMessage("You fell down and landed " + n + " level" + (n > 1 ? "s" : "") + " below.");
                } else if (fadeAction == 2) {
                    loadNextMap();
                    showMessage("You climb the ladder and find another room.");
                }
            }
        } else if (fade == 2) {
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
            if (hud != null) {
                hud.act(delta);
            }
        }
    }

    @Override
    public void onAfterAct(float delta) {
        Label label = messageQueue.peek();
        if (label != null) {
            label.act(delta);
            if (!label.hasActions()) {
                messageQueue.poll();
            }
        }
        // center player
        if (player.hasActions() || cameraMovement) {
            cameraMovement = true;
            float dX = getViewWidth() / 2 - player.getX();
            float dY = getViewHeight() / 2 - player.getY();
            if (cameraSpeedX == 0) {
                cameraSpeedX = dX / cameraBaseSpeed;
            } else if (dX < 1f) {
                cameraSpeedX = 0;
            }
            if (cameraSpeedY == 0) {
                cameraSpeedY = dY / cameraBaseSpeed;
            } else if (dY < 1f) {
                cameraSpeedY = 0;
            }
            if (dX < 1f && dY < 1f) {
                cameraMovement = false;
            }
            moveMapBy(Math.min(dX, cameraSpeedX * delta), Math.min(dY, cameraSpeedY * delta));
        }
    }

    @Override
    public void onBeforeDraw() {
        SGL.provide(SpriteBatch.class).begin();
        map.draw(SGL.provide(SpriteBatch.class), 1f);
        player.draw(SGL.provide(SpriteBatch.class), 1f);
        hud.draw(SGL.provide(SpriteBatch.class), 1f);
        SGL.provide(SpriteBatch.class).end();
    }

    @Override
    public void onAfterDraw() {
        if (fade > 0) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            SGL.provide(ShapeRenderer.class).begin(ShapeRenderer.ShapeType.Filled);
            SGL.provide(ShapeRenderer.class).setColor(0, 0, 0, fadeAlpha);
            SGL.provide(ShapeRenderer.class).rect(stage().getViewOrigX(), stage().getViewOrigY(), stage().getWidth(), stage().getHeight());
            SGL.provide(ShapeRenderer.class).end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
        SGL.provide(SpriteBatch.class).begin();
        Label label = messageQueue.peek();
        if (label != null) {
            SGL.provide(SpriteBatch.class).setColor(0.32f, 0.32f, 0.32f, label.getColor().a);
            speechBackground.draw(SGL.provide(SpriteBatch.class),
                    label.getX() - speechPadding,
                    label.getY() - speechPadding,
                    label.getWidth() + 2 * speechPadding,
                    label.getHeight() + 2 * speechPadding
            );
            messageQueue.peek().draw(SGL.provide(SpriteBatch.class), 1f);
        }
        SGL.provide(SpriteBatch.class).end();
    }

    @Override
    public void onBeauty() {

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
        loadMap(0);
        hud = new HUD();
        hud.setPosition(0, getViewHeight() - hud.getHeight());
        //addActor(player);
        //addActor(map);
        //addActor(hud);
        /*MoveToAction a = new MoveToAction();
        a.setAlignment(Align.center);
        a.setPosition(200, 0);
        a.setDuration(2);
        map.addAction(a);*/
        SGL.registerMessageReceiver(FireEverythingMessage.class, new MessageReceiver() {
            @Override
            public void receiveMessage(Message message) {

            }
        });

        speechBackground = new TextureRegionDrawable(new TextureRegion(SGL.provide(SGLAssets.class).get("ui/speech.png", Texture.class)));
    }

    public void loadMap(int i) {
        int maxMaps = 10; // TODO: Check with some class
        currentMap = Math.max(0, i);
        currentMap = Math.min(maxMaps, currentMap);
        player = new Player();
        map = SGL.provide(SGLAssets.class).get("maps/" + currentMap + ".json", MapWrapper.class).getMap();
        map.reset();
        Vector2 spawn = map.calPixCoord(map.getStart());
        SGL.debug("spawn " + spawn.toString());
        player.setPosition(map.getX() + spawn.x, map.getY() + spawn.y);
        reset();
    }

    private void reset() {
        speechPadding = 10;
        fade = 0;
        timer = 0;
        fadeAlpha = 0;
        fadeAction = 0;
        dead = false;
        float dX = getViewWidth() / 2 - player.getX();
        float dY = getViewHeight() / 2 - player.getY();
        moveMapBy(dX, dY);
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
        messageQueue.clear();
    }

    public void winGame() {
        dead = true;
        fade = 1;
        timer = 0;
        fadeAction = 2;
        messageQueue.clear();
    }

    public void loadPreviousMap() {
        loadPreviousMap(1);
    }

    public void loadPreviousMap(int n) {
        loadMap(currentMap - n);
    }

    public void loadNextMap() {
        loadMap(currentMap + 1);
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
        //SGL.debug(x + "," + y);
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

    public void moveMapBy(float x, float y) {
        map.moveBy(x, y);
        player.moveBy(x, y);
        SGL.provide(BackgroundScreen.class).moveBy(x, y);
    }

    public void showMessage(String trigger) {
        Label label = new Label(trigger, SGL.provide(Skin.class));
        label.setColor(1f, 1f, 1f, 0f);
        label.setPosition(getViewWidth() / 2, 100, Align.center);
        label.addAction(Actions.sequence(Actions.alpha(1, 0.4f), Actions.delay(Math.max(2, trigger.length() / 16)), Actions.alpha(0, 0.2f)));
        messageQueue.add(label);
    }

    public void nextMessage() {
        messageQueue.poll();
    }

    public enum ZINDEX {
        Tile(10), KeyHole(20), Key(30), Player(40), Hud(50), Messages(100);

        public final int idx;

        ZINDEX(int idx) {
            this.idx = idx;
        }
    }
}
