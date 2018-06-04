package com.fourmen.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fourmen.actors.Enemy;
import com.fourmen.actors.Player;
import com.fourmen.actors.PlayerBounds;
import com.fourmen.utils.Animator;
import com.fourmen.utils.CameraStyles;

public class GameScreen extends ScreenAdapter {

    private static float WORLD_WIDTH;
    private static float WORLD_HEIGHT;

    private float BOUND_WIDTH = 3000;
    private float BOUND_HEIGHT = BOUND_WIDTH * (3f/5f);

    private ShapeRenderer shapeRenderer;
    private Viewport viewport;
    private Camera camera;
    private SpriteBatch batch;
    private PlayerBounds playerBounds = new PlayerBounds(BOUND_WIDTH,BOUND_HEIGHT);
    private Player player;
    private Animation<TextureRegion> floor;
    private TextureRegion state;
    private float timeSinceStart;
    private Enemy enemy;

    private int cameraVal;
    public String cameraType;

    public GameScreen(int width, int height){
        WORLD_WIDTH = width;
        WORLD_HEIGHT = height;
    }
    @Override
    public void show() {
        cameraVal = 0;
        cameraType = "None yet";
        camera = new OrthographicCamera();
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        camera.update();
        viewport = new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        shapeRenderer = new ShapeRenderer();
        floor = new Animation<TextureRegion>(0.25f, Animator.setUpSpriteSheet("Images/FloorSprites.png", 1, 26));
        batch = new SpriteBatch();
        player = new Player();
        enemy = new Enemy();
        timeSinceStart = 0;

    }

    @Override
    public void render(float delta) {
        timeSinceStart += delta;
        clearScreen();
        cameraUpdate();
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        batch.begin();
        state = floor.getKeyFrame(timeSinceStart, true);
        batch.draw(state, 0,0,BOUND_WIDTH,BOUND_HEIGHT);
        updateAnimations(batch);
        batch.end();
        blockPlayerLeavingTheWorld();
        drawDebug();
        update(delta);
        enemy.update(delta);
        shapeRenderer.end();
        player.act();       //add to update instead
        enemy.getPlayer(player);
        enemy.act();
        blockPlayerLeavingTheWorld();
    }

    /**
     * this class will be used to implement
     * @param delta
     */
    //private void update(float delta){

    //}

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    private void drawDebug() {
        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        //playerBounds.drawDebug(shapeRenderer);
        //shapeRenderer.end();
        //shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        player.drawDebug(shapeRenderer);
        enemy.drawDebug(shapeRenderer);
        shapeRenderer.end();
    }

    private void cameraUpdate(){
        switch (cameraVal) {
            case 0:
                CameraStyles.lockOnTarget(camera,player);
                cameraType = "Lerp on Player Only";
                break;
            case 1:
                CameraStyles.lockAverageBetweenTargets(camera, player, enemy);
                cameraType = "Lerp between enemy and Player";
        }
        //CameraStyles.lockAverageBetweenTargets(camera,player, enemy);
        //CameraStyles.lockOnTarget(camera,player);
    }

    private void blockPlayerLeavingTheWorld() {
        player.setPosition(MathUtils.clamp(player.getX(),playerBounds.getW1(),playerBounds.getWidth()+ playerBounds.getW1() - player.getPlayerWidth()), MathUtils.clamp(player.getY(), playerBounds.getH2(), playerBounds.getHeight() + playerBounds.getH2() - player.getPlayerHeight()));
        enemy.setPosition(MathUtils.clamp(enemy.getX(),playerBounds.getW1(),playerBounds.getWidth()+ playerBounds.getW1() - enemy.getEnemyWidth()), MathUtils.clamp(enemy.getY(), playerBounds.getH2(), playerBounds.getHeight() + playerBounds.getH2() - enemy.getEnemyHeight()));
    }



    private void clearScreen() {
        Gdx.gl.glClearColor(com.badlogic.gdx.graphics.Color.BLACK.r, com.badlogic.gdx.graphics.Color.BLACK.g,
                com.badlogic.gdx.graphics.Color.BLACK.b, Color.BLACK.a);
        //Gdx.gl.glClearColor(255,255,255,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    }

    public void update(float delta){

        // we should move this camera stuff to a new method
        float startX = camera.viewportWidth /2;
        float startY = camera.viewportHeight /2;

        CameraStyles.boundary(camera, startX, startY, BOUND_WIDTH - (2 * startX),
                BOUND_HEIGHT - (2 * startY));

        player.update(delta);
    }

    public void updateAnimations(SpriteBatch batch) {
        player.updateAnimations(batch);
    }

    public void dispose(){
        batch.dispose();



    }
}
