package com.john.epic;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MapMaker extends ApplicationAdapter {
	SpriteBatch batch;
	Texture redCircle;
	private float widthCircle = 300;
	private Music secretAgent;
	private FileHandle mapText;
	int cnt;
	@Override
	public void create () {
		batch = new SpriteBatch();
		redCircle = new Texture("Images/RedCircle.png");
		secretAgent = Gdx.audio.newMusic(Gdx.files.internal("Music/song1.mp3"));
		mapText = Gdx.files.local("MapTest1.txt");
		mapText.writeString("",false);
		cnt = 0;
	}

	@Override
	public void render () {
		secretAgent.play();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		if(Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY))
		{
			batch.draw(redCircle, 250 - (widthCircle / 2f), 250 - (widthCircle / 2f),
					widthCircle, widthCircle);
			cnt++;
			mapText.writeString(+secretAgent.getPosition() + " ",true);
		}
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		redCircle.dispose();
	}
	public void pause(){
		Gdx.app.exit();
	}
}
