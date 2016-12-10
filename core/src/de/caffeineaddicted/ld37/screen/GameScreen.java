package de.caffeineaddicted.ld37.screen;

import com.badlogic.gdx.math.Vector2;
import de.caffeineaddicted.ld37.LD37;
import de.caffeineaddicted.ld37.actor.Map;
import de.caffeineaddicted.ld37.actor.MapConfig;
import de.caffeineaddicted.ld37.actor.MapWrapper;
import de.caffeineaddicted.ld37.actor.UnitPlayer;
import de.caffeineaddicted.ld37.actor.maps.Map01;
import de.caffeineaddicted.ld37.input.GameInputProcessor;
import de.caffeineaddicted.ld37.message.FireEverythingMessage;
import de.caffeineaddicted.ld37.utils.Assets;
import de.caffeineaddicted.sgl.SGL;
import de.caffeineaddicted.sgl.etities.Actor;
import de.caffeineaddicted.sgl.input.SGLScreenInputMultiplexer;
import de.caffeineaddicted.sgl.messages.Message;
import de.caffeineaddicted.sgl.messages.MessageReceiver;
import de.caffeineaddicted.sgl.ui.screens.SGLStagedScreen;
import de.caffeineaddicted.sgl.utils.SGLAssets;

import java.util.ArrayList;

/**
 * @author Malte Heinzelmann
 */
public class GameScreen extends SGLStagedScreen<LD37> {

    public static float height = 5;
    public ArrayList<Actor> deleteLater = new ArrayList<Actor>();
    private UnitPlayer player;
    private Map map;

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

    public static class Z_INDEX {
        public static int Enemy = 5;
        public static int Projectile = 10;
    }
}
