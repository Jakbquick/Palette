package com.fourmen.screens;


import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fourmen.tween.SpriteAccessor;
import com.fourmen.utils.AnimatedImage;
import com.fourmen.utils.Animator;
import com.fourmen.utils.ResizableImage;
import com.fourmen.utils.TextureFadeImage;

public class MainMenu implements Screen {

    private Stage stage;
    private TextureAtlas atlas;
    private Skin skin;
    private Table table;
    private Texture buttonExit, playButton, background,blackScreenTexture, mountains;
    private BitmapFont font;
    private int width, height;
    private TweenManager tweenManager;
    private SpriteBatch batch;
    private Sprite blackScreen;
    private Viewport viewport;
    private OrthographicCamera camera;
    private TextureRegion bgstate;
    private float timeSinceStart;
    private Animation<TextureRegion> bg, clouds;
    private AnimatedImage bgActor, cloudActor;
    private Music music;
    private Image cliff,blackScreenImage;




    public MainMenu(int width, int height, Music music) {
        this.width = width;
        this.height = height;
        this.music = music;
    }


    @Override
    public void show() {
        bg = new Animation<TextureRegion>(.3f, Animator.setUpSpriteSheet("Images/bgSpriteSheet.png",
                1,12));
        bgActor = new AnimatedImage(bg,width,height);
        setUpAnimations();
        timeSinceStart = 0;
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.position.set(width / 2, height / 2, 0);
        camera.update();
        viewport = new ExtendViewport(width, height, camera);
        stage = new Stage(viewport,batch);
        blackScreen = new Sprite(blackScreenTexture);
        setUpTweenManager();


        skin = new Skin(Gdx.files.internal("ui/skinexport.json"));

        table = new Table(skin);
        table.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture("Images/Titlescreen.png"))));
        table.setFillParent(true);
        table.setDebug(true);

        stage.addActor(table);
        table.setBounds(0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

        stage.addActor(bgActor);
        stage.addActor(cloudActor);
        stage.addActor(cliff);
        

        ImageButton playbutt = new ImageButton(skin);

        playbutt.setPosition(280, 300);



        playbutt.addListener(new ClickListener()
        {
            public void clicked(InputEvent event, float x, float y) {
                music.stop();
                music.dispose();
                ((Game) Gdx.app.getApplicationListener()).setScreen(new Box2DRender(width,height));

                event.stop();

            }
        });





        table.debug();
        stage.addActor(table);
        stage.addActor(playbutt);
        stage.addActor(blackScreenImage);
        Gdx.input.setInputProcessor(stage);
        table.setDebug(false);


    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(255,255,255,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);

        bgstate = bg.getKeyFrame(timeSinceStart,true);
        stage.getBatch().begin();
        //blackScreen.draw(batch);
        stage.getBatch().draw(bgstate,width,height);
        stage.getBatch().end();

        stage.draw();
    }

    @Override
    public void resize(int width, int height) {


    }

    public void setUpAnimations(){
        clouds = new Animation<TextureRegion>(.3f, Animator.setUpSpriteSheet("Images/cloudSheet.png",
                1,25));
        cloudActor = new AnimatedImage(clouds,width,height);
        mountains = new Texture("Images/hedge2.png");
        cliff = new ResizableImage(mountains,width,height);
        blackScreenTexture = new Texture(Gdx.files.internal("Images/BlackScreen.jpg"));
        blackScreenImage = new TextureFadeImage(blackScreenTexture,width,height);
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
        blackScreen.getTexture().dispose();

    }
    public void setUpTweenManager(){
        tweenManager = new TweenManager();
        Tween.registerAccessor(Sprite.class, new SpriteAccessor());
        blackScreen.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Tween.set(blackScreen, SpriteAccessor.ALPHA).target(0).start(tweenManager);
        Tween.to(blackScreen,SpriteAccessor.ALPHA,2).target(1).repeatYoyo(1, 2).setCallback(new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {

            }
        }).start(tweenManager);
    }
}
