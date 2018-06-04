package com.john.epic.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.john.epic.MapMaker;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "BEATMAKER";
		config.vSyncEnabled = true;
		config.useGL30 = true;
		config.width = 500;
		config.height = config.width;
		config.useGL30 = false;			// runs without using openGl so Collin's baka computer can handle it
		config.resizable = false;
		new LwjglApplication(new MapMaker(), config);
	}
}
