package com.fourmen.screens;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
import com.fourmen.box2D.Walls;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fourmen.tween.SpriteAccessor;
import com.fourmen.utils.BodyEditorLoader;
import com.fourmen.utils.CameraStyles;


public class Box2DRender extends ScreenAdapter {
    private Music caveMusic;
    private Texture blackTexture;
    private Sprite blackSprite;
    private TweenManager tweenManager;
    private static float WORLD_WIDTH;
    private static float WORLD_HEIGHT;
    private boolean isPaused;
    public enum State
    {
        PAUSE,
        RUN,
        RESUME,
        STOPPED
    }

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

    private OrthographicCamera camera;
    private Viewport viewport;
    private float timeSinceStart;

    private boolean debugMode;
    private float tempTimer = 0;

    Texture fadedBlack = new Texture(Gdx.files.internal("Images/FadedBlack.png"));
    Texture pauseText = new Texture(Gdx.files.internal("Images/PauseText.png"));

    public Box2DRender(int width, int height){
        WORLD_WIDTH = width;
        WORLD_HEIGHT = height;
    }

    public void show() {
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
        enemy = new Box2DEnemy(world, player);
        walls = new Walls(world,0,0, scale);
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(player.getX(), player.getY(), 0);
        camera.update();
        //viewport = new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        viewport = new ExtendViewport(camera.viewportWidth / 2f, camera.viewportHeight / 2f, camera);
        viewport = new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        timeSinceStart = 0;
    }


    public void render(float delta) {
        tweenManager.update(delta);
        world.step(Gdx.graphics.getDeltaTime(), 6, 2);
        cameraUpdate();
        timeSinceStart += delta;
        update(delta);
        clearScreen();
        world.step(Gdx.graphics.getDeltaTime(), 6, 2);
        player.act();
        enemy.act();
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        batch.begin();
        if(Gdx.input.isKeyPressed(Input.Keys.L) && timeSinceStart >= tempTimer) {
            debugMode = !debugMode;
            tempTimer = timeSinceStart + .1f;
        }
        if(debugMode) {
            debugView();
        }
        else {
            walls.draw(batch);
            player.draw(batch);
        }

        blackSprite.draw(batch);
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
        player.updateTimers(delta);
        enemy.updateTimers(delta);

    }

    public void drawDebug() {

    }

    public void dispose() {
        world.dispose();
        batch.dispose();
        debugRenderer.dispose();
        walls.dispose();
        world.destroyBody(walls.getBody());
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
        Gdx.app.exit();
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

