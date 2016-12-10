package de.caffeineaddicted.ld37.input;

import com.badlogic.gdx.Input;
import de.caffeineaddicted.ld37.actor.UnitPlayer;
import de.caffeineaddicted.ld37.message.FireEverythingMessage;
import de.caffeineaddicted.ld37.screen.GameScreen;
import de.caffeineaddicted.sgl.SGL;
import de.caffeineaddicted.sgl.input.SGLInputProcessor;

/**
 * Created by niels on 20.08.16.
 */
public class GameInputProcessor extends SGLInputProcessor {

    private float startDragX, startDragY;

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.UP:
                SGL.provide(GameScreen.class).getPlayer().move(UnitPlayer.MovementDirection.UP);
                break;
            case Input.Keys.LEFT:
                SGL.provide(GameScreen.class).getPlayer().move(UnitPlayer.MovementDirection.LEFT);
                break;
            case Input.Keys.DOWN:
                SGL.provide(GameScreen.class).getPlayer().move(UnitPlayer.MovementDirection.DOWN);
                break;
            case Input.Keys.RIGHT:
                SGL.provide(GameScreen.class).getPlayer().move(UnitPlayer.MovementDirection.RIGHT);
                break;
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        SGL.debug("touchDown: " + screenX + "," + screenY);
        startDragX = screenX;
        startDragY = screenY;
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        SGL.debug("touchUp: " + screenX + "," + screenY);
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        SGL.debug("touchDragged: " + screenX + "," + screenY);
        SGL.provide(GameScreen.class).drag(screenX - startDragX, screenY - startDragY);
        startDragX = screenX;
        startDragY = screenY;
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        SGL.debug("mouseMoved: " + screenX + "," + screenY);
        return true;
    }


}
