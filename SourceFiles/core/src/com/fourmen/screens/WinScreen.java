package com.fourmen.screens;

import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fourmen.utils.TextureFadeImage;

public class WinScreen extends ScreenAdapter {
    private Stage stage;
    private TweenManager tweenManager;
    private float timerToFade,timeSinceStart;
    private Texture winScreenTexture;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private float width = Gdx.graphics.getWidth();
    private float height = Gdx.graphics.getHeight();
    private Viewport viewport;

    public WinScreen() {
        
    }

    public void show(){
        tweenManager = new TweenManager();
        timerToFade = 3.6f;
        winScreenTexture = new Texture(Gdx.files.internal("Images/winscreen.jpg"));

        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.position.set(width / 2, height / 2, 0);
        camera.update();
        viewport = new ExtendViewport(width, height, camera);
    }

    public void render(float delta){
        batch.begin();
        batch.draw(winScreenTexture,Gdx.graphics.getWidth() - 1500f,
                Gdx.graphics.getHeight() - 900f,(float)winScreenTexture.getWidth() * .5f,(float)winScreenTexture.getHeight() * .6f);
        batch.end();
    }
}
