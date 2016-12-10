package de.caffeineaddicted.ld37prep.input;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import de.caffeineaddicted.ld37prep.message.FireEverythingMessage;
import de.caffeineaddicted.ld37prep.screen.GameScreen;
import de.caffeineaddicted.sgl.SGL;
import de.caffeineaddicted.sgl.input.SGLInputProcessor;

/**
 * Created by niels on 20.08.16.
 */
public class GameInputProcessor extends SGLInputProcessor {

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.SPACE:
                SGL.message(new FireEverythingMessage());
                break;
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        SGL.debug("touchDown: " + screenX + "," + screenY);
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
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        SGL.debug("mouseMoved: " + screenX + "," + screenY);
        return true;
    }


}
