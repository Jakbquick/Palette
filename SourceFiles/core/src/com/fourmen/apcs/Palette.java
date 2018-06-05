package com.fourmen.apcs;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fourmen.screens.Box2DRender;
import com.fourmen.screens.GameScreen;
import com.fourmen.screens.Splash;
import com.fourmen.screens.MainMenu;

public class Palette extends Game {

	public static final String TITLE = "Palette", VERSION = "0.0";
	int screenWidth, screenHeight;
	public Palette(int width, int height){
		screenWidth = width;
		screenHeight = height;
	}
	@Override
	public void create () {

		//setScreen(new Splash(screenWidth, screenHeight));
		//setScreen(new GameScreen(screenWidth,screenHeight));
		//setScreen(new Box2DRender(screenWidth,screenHeight));
        setScreen(new Splash(screenWidth,screenHeight));
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
