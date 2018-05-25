package com.fourmen.apcs.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.fourmen.apcs.Palette;

import static com.fourmen.apcs.Palette.TITLE;
/**
 *this class constitures the main class of the Palette game. Begins game with desired configuration for
 * desktop play.
 */
public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = Palette.TITLE + " v " + Palette.VERSION;
		config.vSyncEnabled = true;
		config.useGL30 = true;
		config.width = 1280;
		config.height = 720;
		config.useGL30 = false;

		new LwjglApplication(new Palette(), config);
	}
}
