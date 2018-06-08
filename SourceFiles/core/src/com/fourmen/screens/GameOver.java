package com.fourmen.screens;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.fourmen.tween.MusicAccessor;
import com.fourmen.utils.TextureFadeImage;

public class GameOver extends ScreenAdapter {
    private Stage stage;
    private TweenManager tweenManager;
    private float timerToFade,timeSinceStart;
    private Texture blackScreenTexture,palTex;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private float width = Gdx.graphics.getWidth();
    private float height = Gdx.graphics.getHeight();
    private Viewport viewport;
    private Sprite blackScreen;
    private Skin skin,skin2;
    private Table table;
    private TextureFadeImage blackScreenImage,paletteSad;
    private boolean gameStarted = false;
    private boolean startTimer = false;
    private Music music = Gdx.audio.newMusic(Gdx.files.internal("Music/SadPiano.mp3"));
    Texture palette = new Texture("ui/PaletteSad.png");

    public void show(){
        tweenManager = new TweenManager();
        timerToFade = 3.6f;
        blackScreenTexture = new Texture(Gdx.files.internal("Images/BlackScreen.jpg"));
        blackScreenImage = new TextureFadeImage(blackScreenTexture,Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight());
        Texture palette = new Texture("ui/PaletteSad.png");
        paletteSad = new TextureFadeImage(palette,palette.getWidth(),palette.getHeight());

        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.position.set(width / 2, height / 2, 0);
        camera.update();
        viewport = new ExtendViewport(width, height, camera);
        stage = new Stage(viewport,batch);
        blackScreen = new Sprite(blackScreenTexture);

        skin = new Skin(Gdx.files.internal("ui/PlayAgain.json"));
        skin2 = new Skin(Gdx.files.internal("ui/ExitButton.json"));


        table = new Table(skin);
        table.setFillParent(true);

        ImageButton playButton = new ImageButton(skin);

        playButton.setPosition(((Gdx.graphics.getWidth()/2f) - 150), Gdx.graphics.getHeight() / 3f);
        music.play();
        table.setBounds(0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

        playButton.addListener(new ClickListener()
        {
            public void clicked(InputEvent event, float x, float y) {
                if(!gameStarted) {
                    startGame();
                }
                event.stop();

            }
        });

        ImageButton exitButton = new ImageButton(skin2);
        exitButton.setPosition((Gdx.graphics.getWidth()/2f) - 100, Gdx.graphics.getHeight() / 6f);



        exitButton.addListener(new ClickListener()
        {
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
                event.stop();

            }
        });

        stage.addActor(paletteSad);
        stage.addActor(playButton);
        stage.addActor(exitButton);
        stage.addActor(blackScreenImage);
        Gdx.input.setInputProcessor(stage);
        blackScreenImage.fadeOut();
        paletteSad.fadeIn();
    }

    public void render(float delta){
        tweenManager.update(delta);
        tweenManager.update(delta);
        if (timerToFade <= 0){
            music.stop();
            ((Game) Gdx.app.getApplicationListener()).setScreen(new Box2DRender(Gdx.graphics.getWidth(),
                    Gdx.graphics.getHeight()));
        }
        if(startTimer){
            timerToFade -= delta;
        }
        timeSinceStart+= delta;
        stage.act(delta);
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();
        batch.begin();
        batch.draw(palette,(Gdx.graphics.getWidth() * .5f) - palette.getWidth() * .5f,
                Gdx.graphics.getHeight() - 500f,(float)palette.getWidth() * 2f,(float)palette.getHeight() * 2f);
        batch.end();
    }
    public void fadeMusic(){
        Tween.registerAccessor(Music.class, new MusicAccessor());
        Tween.set(music, MusicAccessor.VOLUME).cast(Music.class).target(music.getVolume()).start(tweenManager);
        Tween.to(music,0,3.5f).cast(Music.class).target(0).start(tweenManager);
    }
    public void startGame(){
        startTimer = true;
        fadeMusic();
        gameStarted = true;
        blackScreenImage.fadeIn();
    }
}
