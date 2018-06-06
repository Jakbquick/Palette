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
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fourmen.tween.MusicAccessor;
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
    private TextureRegion bgstate,cloud1State;
    private float timeSinceStart,cloudDuration,hairDuration;
    private Animation<TextureRegion> bg, clouds, cloud1, hairBlow1,hairBlow2,blankAnimation,cloud2;
    private AnimatedImage bgActor, cloudActor,cloud1Image,hair1Image,hair2Image,cloud2Image;
    private Music music,beach;
    private Image cliff,hedgeImage;
    private float timerToFade;
    private Sound gameStart;
    private boolean startTimer, fadeOutMusic, fadeToBlack;
    private TextureFadeImage blackScreenImage;






    public MainMenu(int width, int height, Music music,Music beach) {
        this.width = width;
        this.height = height;
        this.music = music;
        this.beach = beach;
    }


    @Override
    public void show() {
        tweenManager = new TweenManager();
        fadeOutMusic = false;
        fadeToBlack = false;

        gameStart = Gdx.audio.newSound(Gdx.files.internal("Music/GameStart.mp3"));
        timerToFade = 3.6f;
        blankAnimation = new Animation<TextureRegion>(.1f, Animator.setUpSpriteSheet("Images/transparent.png",
                1,16));
        bg = new Animation<TextureRegion>(.25f, Animator.setUpSpriteSheet("Images/bgSpriteSheet.png",
                1,12));
        bgActor = new AnimatedImage(bg,width,height,'y');
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


        skin = new Skin(Gdx.files.internal("ui/SkinNew.json"));

        table = new Table(skin);
        table.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture("Images/Titlescreen.png"))));
        table.setFillParent(true);
        table.setDebug(true);

        stage.addActor(table);
        table.setBounds(0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

        stage.addActor(bgActor);
        stage.addActor(cloudActor);
        stage.addActor(cliff);
        stage.addActor(cloud1Image);
        stage.addActor(cloud2Image);
        stage.addActor(hair1Image);
        stage.addActor(hair2Image);
        stage.addActor(hedgeImage);
        

        ImageButton playbutt = new ImageButton(skin);

        playbutt.setPosition(280, 250);



        playbutt.addListener(new ClickListener()
        {
            public void clicked(InputEvent event, float x, float y) {
                startGame();
                event.stop();

            }
        });





        table.debug();
        stage.addActor(table);
        stage.addActor(playbutt);

        stage.addActor(blackScreenImage);
        Gdx.input.setInputProcessor(stage);
        table.setDebug(false);

        blackScreenImage.fadeOut();
    }

    @Override
    public void render(float delta) {
        tweenManager.update(delta);
        if(Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)){
            startGame();
        }
        if(fadeOutMusic){
            fadeMusic();
        }
        if (timerToFade <= 0){
            music.stop();
            beach.stop();
            ((Game) Gdx.app.getApplicationListener()).setScreen(new Box2DRender(width,height));
        }
        if(startTimer){
            timerToFade -= delta;
        }
        if(timeSinceStart >= cloudDuration-.30){
            cloud1Image.setAnimation(blankAnimation);
            cloud2Image.setAnimation(cloud2);
        }
        if(timeSinceStart >= hairDuration-.15){
            hair1Image.setAnimation(blankAnimation);
            hair2Image.setAnimation(hairBlow2);
        }

        timeSinceStart+= delta;
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
        cloudActor = new AnimatedImage(clouds,width,height,'y');
        mountains = new Texture("Images/hedge2.png");
        cliff = new ResizableImage(mountains,width,height);

        blackScreenTexture = new Texture(Gdx.files.internal("Images/BlackScreen.jpg"));
        blackScreenImage = new TextureFadeImage(blackScreenTexture,width,height);

        cloud1 = new Animation<TextureRegion>(.1f, Animator.setUpSpriteSheet("Images/cloud1.png",
                1,16));

        cloud1Image = new AnimatedImage(cloud1, width, height,'n');

        hairBlow1 = new Animation<TextureRegion>(.15f, Animator.setUpSpriteSheet("Images/spriteWhitePix.png",
                1,15));
        hair1Image = new AnimatedImage(hairBlow1, width, height,'n');
        hedgeImage = new ResizableImage(new Texture("Images/hedge.png"),width,height);
        hairBlow2 = new Animation<TextureRegion>(.20f, Animator.setUpSpriteSheet("Images/hairAfter.png",
                1,7));
        hair2Image = new AnimatedImage(blankAnimation,width,height,'y');

        hairDuration = hairBlow1.getAnimationDuration();
        cloudDuration = cloud1.getAnimationDuration();

        cloud2 = new Animation<TextureRegion>(.20f, Animator.setUpSpriteSheet("Images/afterpoof.png",
                1,7));
        cloud2Image = new AnimatedImage(blankAnimation,width,height,'y');
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
        music.dispose();
        gameStart.dispose();
        beach.dispose();
    }
    public void setUpTweenManager(){


    }
    public void fadeMusic(){
        Tween.registerAccessor(Music.class, new MusicAccessor());
        Tween.set(music, MusicAccessor.VOLUME).cast(Music.class).target(music.getVolume()).start(tweenManager);
        Tween.to(music,0,3.5f).cast(Music.class).target(0).start(tweenManager);
        Tween.set(beach, MusicAccessor.VOLUME).cast(Music.class).target(beach.getVolume()).start(tweenManager);
        Tween.to(beach,0,3.5f).cast(Music.class).target(0).start(tweenManager);
    }
    public void startGame(){
        startTimer = true;
        gameStart.play();
        fadeMusic();
        fadeToBlack = true;
        blackScreenImage.fadeIn();

    }
}
