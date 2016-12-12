package de.caffeineaddicted.ld37.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import de.caffeineaddicted.ld37.LD37;
import de.caffeineaddicted.sgl.AttributeList;
import de.caffeineaddicted.sgl.SGL;
import de.caffeineaddicted.sgl.SGLGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        SGLGame game = new LD37();
        config.fullscreen = true;
        config.width = SGL.game().config().get(AttributeList.WIDTH) + 100;
        config.height = SGL.game().config().get(AttributeList.HEIGHT);
        config.resizable = SGL.game().config().get(AttributeList.RESIZABLE);
        new LwjglApplication(game, config);
	}
}
