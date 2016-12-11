package de.caffeineaddicted.ld37.input;

import com.badlogic.gdx.Input;
import de.caffeineaddicted.ld37.actor.Player;
import de.caffeineaddicted.ld37.screen.GameScreen;
import de.caffeineaddicted.sgl.SGL;
import de.caffeineaddicted.sgl.input.SGLInputProcessor;

/**
 * Created by niels on 20.08.16.
 */
public class GameInputProcessor extends SGLInputProcessor {

    private float lastDragX, lastDragY;

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.UP:
                SGL.provide(GameScreen.class).getPlayer().keyDown(Player.MovementDirection.UP);
                break;
            case Input.Keys.LEFT:
                SGL.provide(GameScreen.class).getPlayer().keyDown(Player.MovementDirection.LEFT);
                break;
            case Input.Keys.DOWN:
                SGL.provide(GameScreen.class).getPlayer().keyDown(Player.MovementDirection.DOWN);
                break;
            case Input.Keys.RIGHT:
                SGL.provide(GameScreen.class).getPlayer().keyDown(Player.MovementDirection.RIGHT);
                break;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.UP:
                SGL.provide(GameScreen.class).getPlayer().keyUp(Player.MovementDirection.UP);
                break;
            case Input.Keys.LEFT:
                SGL.provide(GameScreen.class).getPlayer().keyUp(Player.MovementDirection.LEFT);
                break;
            case Input.Keys.DOWN:
                SGL.provide(GameScreen.class).getPlayer().keyUp(Player.MovementDirection.DOWN);
                break;
            case Input.Keys.RIGHT:
                SGL.provide(GameScreen.class).getPlayer().keyUp(Player.MovementDirection.RIGHT);
                break;
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        SGL.debug("touchDown: " + screenX + "," + screenY);
        lastDragX = screenX;
        lastDragY = screenY;
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        SGL.debug("touchUp: " + screenX + "," + screenY);
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        SGL.debug("touchDragged: " + screenX + "," + screenY + " distance: " + (screenX - lastDragX) + "," + (screenY - lastDragY));
        SGL.debug("x:" + lastDragX + " y:" + lastDragY);
        SGL.provide(GameScreen.class).drag(screenX - lastDragX, screenY - lastDragY);
        lastDragX = screenX;
        lastDragY = screenY;
        SGL.debug("x:" + lastDragX + " y:" + lastDragY);
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        //SGL.debug("mouseMoved: " + screenX + "," + screenY);
        return false;
    }


}
