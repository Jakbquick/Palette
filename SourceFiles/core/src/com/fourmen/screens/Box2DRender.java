package com.fourmen.screens;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.fourmen.actors.Box2DEnemy;
import com.fourmen.actors.Box2DPlayer;
import com.fourmen.actors.PlayerBounds;
import com.fourmen.box2D.Beam;
import com.fourmen.box2D.Walls;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fourmen.tween.SpriteAccessor;
import com.fourmen.utils.*;


public class Box2DRender extends ScreenAdapter {
    private Music caveMusic;
    private Texture blackTexture;
    private Sprite blackSprite;
    private TweenManager tweenManager;
    private static float WORLD_WIDTH;
    private static float WORLD_HEIGHT;
    private boolean isPaused;
    private boolean steppedOn = false;
    public enum State
    {
        PAUSE,
        RUN,
        RESUME,
        STOPPED
    }
    private TextureRegion redRegion;
    SpriteBatch batch;
    World world;
    ContactListener contactListener;
    Walls walls;

    Box2DPlayer player;
    Box2DEnemy enemy;
    private float scale = 3000;
    private float BOUND_HEIGHT = scale * (3f/5f);

    private PlayerBounds playerBounds = new PlayerBounds(scale,BOUND_HEIGHT);

    Box2DDebugRenderer debugRenderer;

    private OrthographicCamera camera,uiCamera;
    private Viewport viewport, uiViewport;
    private float timeSinceStart;
    private RhythmView rhythmView;
    private boolean debugMode;
    private float tempTimer = 0;
    private Vector2 circlePosition = new Vector2(1400,900);
    private Animation<TextureRegion> redCircle;
    private PlayerHealth playerHealth;

    public Box2DRender(int width, int height){
        WORLD_WIDTH = width;
        WORLD_HEIGHT = height;
    }

    public void show() {
        redCircle = new Animation<TextureRegion>(.03f, Animator.setUpSpriteSheet("Images/circle.png", 1,71));

        setUpTween();
        caveMusic = Gdx.audio.newMusic(Gdx.files.internal("Music/CaveMusic.mp3"));
        caveMusic.setLooping(true);
        caveMusic.play();       //future reminder to stop this music when boss spawns(step in circle)
        batch = new SpriteBatch();
        world = new World(new Vector2(0, 0), true);
        contactListener = new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();
                //Gdx.app.log("beginContact", "between " + fixtureA.toString() + " and " + fixtureB.toString());

                for(Beam beam : player.getBeams()) {

                    if ((fixtureA.getBody().equals(beam.body) && fixtureB.getBody().equals(enemy.getPlayerBody()))
                            || (fixtureB.getBody().equals(beam.body) && fixtureA.getBody().equals(enemy.getPlayerBody()))) {
                        enemy.updateCollisions(1);

                        enemy.updateDamage(beam.getHitValue());
                        System.out.println(beam.getHitValue());

                        /*
                        if (fixtureA.getBody().equals(beam.body)) {
                            Beam colBeam = (Beam) fixtureA.getBody().getUserData();
                            enemy.damage += colBeam.getHitValue();
                            System.out.println(colBeam.getHitValue());
                        }
                        if (fixtureB.getBody().equals(beam.body)) {
                            Beam colBeam = (Beam) fixtureB.getBody().getUserData();
                            enemy.damage += colBeam.getHitValue();
                            System.out.println(colBeam.getHitValue());
                        }
                        */
                    }
                }

                if ((fixtureA.getBody().equals(player.getPlayerBody()) && fixtureB.getBody().equals(enemy.getPlayerBody()))
                        || (fixtureB.getBody().equals(player.getPlayerBody()) && fixtureA.getBody().equals(enemy.getPlayerBody()))) {
                    player.updateCollisions(1);
                }
            }

            @Override
            public void endContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();
                //Gdx.app.log("endContact", "between " + fixtureA.toString() + " and " + fixtureB.toString());

                for(Beam beam : player.getBeams()) {

                    if ((fixtureA.getBody().equals(beam.body) && fixtureB.getBody().equals(enemy.getPlayerBody()))
                            || (fixtureB.getBody().equals(beam.body) && fixtureA.getBody().equals(enemy.getPlayerBody()))) {
                        enemy.updateCollisions(-1);

                        enemy.updateDamage(-beam.getHitValue());
                        System.out.println(-beam.getHitValue());

                        /*
                        if (fixtureA.getBody().equals(beam.body)) {
                            Beam colBeam = (Beam) fixtureA.getBody().getUserData();
                            enemy.damage -= colBeam.getHitValue();
                        }
                        if (fixtureB.getBody().equals(beam.body)) {
                            Beam colBeam = (Beam) fixtureB.getBody().getUserData();
                            enemy.damage -= colBeam.getHitValue();
                        }
                        */
                    }
                }

                if ((fixtureA.getBody().equals(player.getPlayerBody()) && fixtureB.getBody().equals(enemy.getPlayerBody()))
                        || (fixtureB.getBody().equals(player.getPlayerBody()) && fixtureA.getBody().equals(enemy.getPlayerBody()))) {
                    player.updateCollisions(-1);
                }
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        };
        world.setContactListener(contactListener);
        debugRenderer = new Box2DDebugRenderer();
        player = new Box2DPlayer(world, playerBounds);
        enemy = new Box2DEnemy(world, playerBounds, player);
        walls = new Walls(world,0,0, scale);
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(player.getX(), player.getY(), 0);
        camera.update();
        uiCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        uiCamera.position.set(Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/2f, 0);
        uiCamera.update();

        //viewport = new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        //viewport = new ExtendViewport(camera.viewportWidth / 2f, camera.viewportHeight / 2f, camera);
        viewport = new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        uiViewport = new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT, uiCamera);
        timeSinceStart = 0;

        rhythmView = new RhythmView(batch);
        playerHealth = new PlayerHealth(batch,player.health);
    }


    public void render(float delta) {
        if(player.getHealth() <= 0 || (rhythmView.getSongLength() + 1 < rhythmView.getSongPosition())){

        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.P)){
            player.subractHealth(30);
            System.out.println(player.getHealth());
        }
        if(player.getPosition().dst(circlePosition) < 300){
            steppedOn = true;
            rhythmView.startMusic();
            caveMusic.stop();
        }
        tweenManager.update(delta);
        rhythmView.update(delta);
        playerHealth.update(player.health,delta);
        world.step(Gdx.graphics.getDeltaTime(), 6, 2);
        cameraUpdate();
        uiCamera.update();
        timeSinceStart += delta;
        update(delta);
        clearScreen();
        world.step(Gdx.graphics.getDeltaTime(), 6, 2);
        player.setHitValue(rhythmView.getScore());
        player.act();
        enemy.act();
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);

        if(Gdx.input.isKeyPressed(Input.Keys.L) && timeSinceStart >= tempTimer) {
            debugMode = !debugMode;
            tempTimer = timeSinceStart + .1f;
        }

        if(debugMode) {
            debugView();
        }

        batch.begin();
        if (!debugMode){
            walls.draw(batch);
            if(!steppedOn){
                redRegion = redCircle.getKeyFrame(timeSinceStart, true);
                batch.draw(redRegion,1100,600,600,600);
            }
            player.draw(batch);
            enemy.draw(batch);
        }
        blackSprite.draw(batch);
        batch.end();



        //code to render the ui
        batch.setProjectionMatrix(uiCamera.projection);
        batch.setTransformMatrix(uiCamera.view);
        batch.begin();
        rhythmView.draw();
        playerHealth.draw();
        batch.end();
    }
    private void debugView() {
            debugRenderer.render(world, camera.combined);
        }

    private void clearScreen() {
         Gdx.gl.glClearColor(com.badlogic.gdx.graphics.Color.BLACK.r, com.badlogic.gdx.graphics.Color.BLACK.g,
        com.badlogic.gdx.graphics.Color.BLACK.b, Color.BLACK.a);

        //Gdx.gl.glClearColor(255, 255, 255, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    }

    public void update(float delta) {
        //flower.drawDebug(shapeRenderer);

        // we should move this camera stuff to a new method
        float startX = camera.viewportWidth / 2;
        float startY = camera.viewportHeight / 2;

        CameraStyles.boundary(camera, startX, startY, scale - (2 * startX),
                BOUND_HEIGHT - (2 * startY));
        walls.updateAnimations(delta);
        player.update(delta);
        enemy.update(delta);

    }

    public void drawDebug() {

    }

    public void dispose() {
        world.dispose();
        batch.dispose();
        debugRenderer.dispose();
        walls.dispose();
        world.destroyBody(walls.getBody());
        player.dispose();
        playerHealth.dispose();
    }

    private void cameraUpdate() {
        CameraStyles.lockOnTarget(camera, player);
    }

    private void checkCollisions() {

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    public void pause(){
        //Gdx.app.exit();
    }

    public void setUpTween(){
        tweenManager = new TweenManager();
        Tween.registerAccessor(Sprite.class, new SpriteAccessor());
        blackTexture = new Texture("Images/BlackScreen.jpg");
        blackSprite = new Sprite(blackTexture);
        blackSprite.setPosition(0,0);
        blackSprite.setSize(WORLD_WIDTH * 2f,WORLD_HEIGHT * 2f);
        Tween.set(blackSprite, SpriteAccessor.ALPHA).target(1).start(tweenManager);
        Tween.to(blackSprite,SpriteAccessor.ALPHA,1.7f).target(0).start(tweenManager);
    }
}

