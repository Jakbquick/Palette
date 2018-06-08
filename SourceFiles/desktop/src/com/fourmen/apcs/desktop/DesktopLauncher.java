package com.fourmen.apcs.desktop;

import com.badlogic.gdx.Files;
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
		//config.fullscreen = true;
		config.vSyncEnabled = true;
		config.useGL30 = true;
		config.width = 1500;
		config.height = (int)(config.width * (double)(9.0/16.0));
		config.useGL30 = false;			// runs without using openGl so Collin's baka computer can handle it
		config.resizable = false;
		config.addIcon("Images/macthumbnail.png",Files.FileType.Internal);
		config.addIcon("Images/thumbnail.png",Files.FileType.Internal);
		config.addIcon("Images/lowresthumbnail.png",Files.FileType.Internal);
		config.fullscreen = true;

		new LwjglApplication(new Palette(config.width,config.height), config);
	}
}