package de.caffeineaddicted.ld37.screen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import de.caffeineaddicted.ld37.LD37;
import de.caffeineaddicted.ld37.actor.Map;
import de.caffeineaddicted.ld37.actor.MapWrapper;
import de.caffeineaddicted.ld37.actor.UnitPlayer;
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

    public static float height = 5;
    public ArrayList<Actor> deleteLater = new ArrayList<Actor>();
    private UnitPlayer player;
    private Map map;
    private Queue<Label> messageQueue = new LinkedBlockingQueue<Label>();
    private Drawable speechBackground;
    private float speechPadding = 10;
    private float timer = 0, speechAlpha = 0;


    public void onBeforeAct(float delta) {
        for (Actor a : deleteLater) {
            stage().removeActor(a);
        }
        if (player != null)
            player.act(delta);

        if (map != null)
            map.act(delta);
    }

    @Override
    public void onAfterAct(float delta) {
        super.onAfterAct(delta);
        Label label = messageQueue.peek();
        if(label != null) {
            label.act(delta);
            if (!label.hasActions()) {
                messageQueue.poll();
            }
        }
    }

    @Override
    public void onAfterDraw() {
        super.onAfterDraw();
        SGL.provide(SpriteBatch.class).begin();
        Label label = messageQueue.peek();
        if(label != null) {
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
        player = new UnitPlayer();
        map = SGL.provide(SGLAssets.class).get("maps/02.json", MapWrapper.class).getMap();
        map.create();
        addActor(player);
        addActor(map);
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

        speechBackground = new TextureRegionDrawable(new TextureRegion(SGL.provide(SGLAssets.class).get("speech.png", Texture.class)));
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

    public UnitPlayer getPlayer() {
        return player;
    }

    public void drag(float x, float y) {
        SGL.debug(x + "," + y);
        //stage().getCamera().position.add(x, y, 0);
        map.moveBy(x, y);
        player.moveBy(x, y);
    }

    public void showMessage(String trigger) {
        Label label = new Label(trigger, SGL.provide(Skin.class));
        label.setColor(1f,1f,1f,0f);
        label.setPosition(getViewWidth()/2,100, Align.center);
        label.addAction(Actions.sequence(Actions.alpha(1,1),Actions.delay(4),Actions.alpha(0,1)));
        label.setZIndex(ZINDEX.Messages.idx);

        messageQueue.add(label);
    }

    public enum ZINDEX {
        Tile(10), Key(20), Player(30), Messages(100);

        public final int idx;

        ZINDEX(int idx) {
            this.idx = idx;
        }
    }
}
