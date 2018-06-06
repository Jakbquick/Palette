package com.fourmen.screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fourmen.tween.SpriteAccessor;

public class Splash implements Screen {
    private Sprite splash;
    private Texture splashTexture;
    private SpriteBatch batch;
    private TweenManager tweenManager;
    private int width,height;
    private Music music,beach;
    public Splash(int width, int height){
        this.width = width;
        this.height = height;
    }

    @Override
    public void show() {
        splashTexture = new Texture("images/Logo.jpg");
        splash = new Sprite(splashTexture);

        music = Gdx.audio.newMusic(Gdx.files.internal("Music/LaurenPiano.mp3"));
        beach = Gdx.audio.newMusic(Gdx.files.internal("Music/BeachMusic.mp3"));
        beach.setLooping(true);
        beach.play();
        music.setLooping(true);
        music.play();

        batch = new SpriteBatch();
        tweenManager = new TweenManager();
        Tween.registerAccessor(Sprite.class, new SpriteAccessor());
        splash.setPosition(-10,0);
        splash.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Tween.set(splash, SpriteAccessor.ALPHA).target(0).start(tweenManager);
        Tween.to(splash,SpriteAccessor.ALPHA,1.8f).target(1).repeatYoyo(1, 2).setCallback(new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new MainMenu(width,height,music,beach));
            }
        }).start(tweenManager);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        tweenManager.update(delta);

        if(Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)){
            ((Game)Gdx.app.getApplicationListener()).setScreen(new MainMenu(width,height,music,beach));
        }
        batch.begin();
        splash.draw(batch);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        splash.getTexture().dispose();

    }

}
