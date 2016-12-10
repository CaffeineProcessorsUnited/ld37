package de.caffeineaddicted.ld37prep.screen;

import com.badlogic.gdx.math.Vector2;
import de.caffeineaddicted.ld37prep.LD37;
import de.caffeineaddicted.ld37prep.actor.Map;
import de.caffeineaddicted.ld37prep.actor.Tile;
import de.caffeineaddicted.ld37prep.actor.UnitPlayer;
import de.caffeineaddicted.ld37prep.actor.maps.Map01;
import de.caffeineaddicted.ld37prep.message.FireEverythingMessage;
import de.caffeineaddicted.sgl.SGL;
import de.caffeineaddicted.sgl.etities.Actor;
import de.caffeineaddicted.sgl.messages.Message;
import de.caffeineaddicted.sgl.messages.MessageReceiver;
import de.caffeineaddicted.sgl.ui.screens.SGLStagedScreen;

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
        player = new UnitPlayer();
        map = new Map01(new Vector2(0,0), new Vector2(0,0));
        map.create();
        addActor(player);
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

    public static class Z_INDEX {
        public static int Enemy = 5;
        public static int Projectile = 10;
    }
}
