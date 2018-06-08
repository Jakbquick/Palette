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
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fourmen.utils.ResizableImage;
import com.fourmen.utils.TextureFadeImage;

public class WinScreen extends ScreenAdapter {
    private Stage stage;
    private TweenManager tweenManager;
    private float timerToFade,timeSinceStart;
    private Texture winScreenTexture;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Viewport viewport;
    private Skin skin2;

    private float width = Gdx.graphics.getWidth();
    private float height = Gdx.graphics.getHeight();
    private Table table;
    private ResizableImage background;
    public WinScreen() {

    }

    public void show(){
        tweenManager = new TweenManager();
        timerToFade = 3.6f;
        winScreenTexture = new Texture(Gdx.files.internal("Images/winscreen.jpg"));
        background = new ResizableImage(winScreenTexture,(int)width,
                (int)height);
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.position.set(width / 2, height / 2, 0);
        camera.update();
        viewport = new ExtendViewport(width, height, camera);
        stage = new Stage(viewport,batch);
        skin2 = new Skin(Gdx.files.internal("ui/ExitButton.json"));

        table = new Table(skin2);
        table.setBackground(new TextureRegionDrawable(new TextureRegion(winScreenTexture)));
        table.setFillParent(true);
        ImageButton exitButton = new ImageButton(skin2);
        exitButton.setPosition((Gdx.graphics.getWidth()/2f) + 300, Gdx.graphics.getHeight() / 3f);



        exitButton.addListener(new ClickListener()
        {
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
                event.stop();

            }
        });
        stage.addActor(background);
        stage.addActor(exitButton);
        Gdx.input.setInputProcessor(stage);
    }

    public void render(float delta){
        //Gdx.gl.glClearColor(0,0,0,1);
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
        batch.begin();
        //batch.draw(winScreenTexture,Gdx.graphics.getWidth() - width,
                //Gdx.graphics.getHeight() - height,width,height);
        batch.end();
    }
}
