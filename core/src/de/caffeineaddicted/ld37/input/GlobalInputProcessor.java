package de.caffeineaddicted.ld37.input;

import com.badlogic.gdx.Input;
import de.caffeineaddicted.ld37.message.MainMenuMessage;
import de.caffeineaddicted.sgl.SGL;
import de.caffeineaddicted.sgl.input.SGLInputProcessor;

/**
 * @author Malte Heinzelmann
 */
public class GlobalInputProcessor extends SGLInputProcessor {

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.ESCAPE:
                SGL.message(new MainMenuMessage());
                break;
        }
        return false;
    }

}
