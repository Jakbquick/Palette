package com.fourmen.apcs;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fourmen.screens.GameScreen;
import com.fourmen.screens.Splash;

public class Palette extends Game {
	public static final String TITLE = "Palette", VERSION = "0.0";
	
	@Override
	public void create () {

		//setScreen(new Splash());
		setScreen(new GameScreen());
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		super.dispose();
	}
}
